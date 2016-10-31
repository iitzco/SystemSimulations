package itba.ss.TP6_PedestrianDynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PedestrianDynamics {

	static final double EPSILON = 0.000001;

	static final double MASS = 70;

	static final double DISTANCE_BOTTOM = 5;
	static final double DISTANCE_LIMIT = 1;

	double L;
	double W;
	double D;

	double m = 0.01;

	double tf;
	double dt;
	double dt2;

	boolean open;

	int maxParticles;

	List<Particle> particles;

	int N;

	long caudal = 0;

	IntegralMethod integralMethod;

	double desiredForce = 6;

	public PedestrianDynamics(double l, double w, double d, double tf, double dT, double dT2, boolean open,
			int maxParticles, IntegralMethod integralMethod) {
		super();
		L = l;
		W = w;
		D = d;

		this.tf = tf;
		this.dt = dT;
		this.dt2 = dT2;

		this.maxParticles = maxParticles;
		this.open = open;

		this.integralMethod = integralMethod;

		locateParticles(maxParticles);

		integralMethod.getAccelerator().setContext(this);
	}

	private void locateParticles(int size) {
		this.particles = new ArrayList<Particle>();

		int id = 0;

		while (particles.size() < size) {
			double diameter = Math.random() * (0.08) + 0.5;
			double r = diameter / 2;
			double x = 0, y = 0;
			do {
				x = Math.random() * (this.W - 2 * r) + r;
				y = Math.random() * (this.L - 2 * r) + r + getBaseLine();
			} while (overlap(x, y, diameter / 2));
			Particle p = new Particle(id++, diameter / 2, x, y, 0, 0, MASS, desiredForce);
			this.particles.add(p);
		}
		this.N = this.particles.size();
	}

	private boolean overlap(double x, double y, double r) {
		for (Particle p : particles) {
			if (getDistance(p.x, p.y, x, y) < (p.r + r))
				return true;
		}
		return false;
	}

	private double getDistance(double x0, double y0, double x1, double y1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
	}

	public void run(int option) {
		double currentTime = 0;

		CellIndexMethod method = new CellIndexMethod(this.L + this.getBaseLine(), 2, false);
		int iteration = 0;
		while (currentTime < tf) {
			checkRelocation();

			try {
				method.load(new HashSet<>(this.particles));
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Delta T was too big. Try with smaller");
				System.exit(1);
			}
			Map<Particle, Set<Particle>> neighbors = method.findNeighbors();

			List<Particle> nextGen = new ArrayList<Particle>();

			neighbors = filterNeighbors(neighbors);

			addWallParticles(neighbors);

			for (Particle particle : particles) {
				Particle p = integralMethod.moveParticle(particle, neighbors.get(particle));
				nextGen.add(p);
			}
			if (Math.abs(currentTime / dt2 - Math.round(currentTime / dt2)) < EPSILON) {
				double roundOff = Math.round(currentTime * 1000.0) / 1000.0;
				switch (option) {
				case 0:
					printOvitoState(iteration, neighbors);
					break;
				case 1:
					double energy = getEnergy();
					System.out.println(roundOff + "\t" + energy + "\t" + (energy * particles.size()));
					break;
				case 2:
					System.out.println(roundOff + "\t" + caudal);
					caudal = 0;
					break;
				}
			}
			this.particles = nextGen;
			currentTime += dt;
			iteration++;
		}
	}

	private double getEnergy() {
		double ret = 0;
		for (Particle particle : particles) {
			ret += (1.0 / 2) * particle.mass * (Math.pow(particle.speedX, 2) + Math.pow(particle.speedY, 2));
		}
		return ret / particles.size();
	}

	private void checkRelocation() {
		List<Particle> toRemove = new ArrayList<>();
		for (Particle particle : particles) {
			if (particle.y - particle.r < DISTANCE_LIMIT) {
				toRemove.add(particle);
				caudal++;
			}
		}
		for (Particle particle : toRemove) {
			this.particles.remove(particle);
		}
	}

	private void addWallParticles(Map<Particle, Set<Particle>> neighbors) {
		for (Particle particle : particles) {
			List<Particle> newParticles = getContactParticles(particle);
			neighbors.get(particle).addAll(newParticles);
		}
	}

	private List<Particle> getContactParticles(Particle particle) {
		List<Particle> ret = new ArrayList<>();
		Particle p = null;

		if (particle.x - particle.r < 0) {
			p = new Particle(0, particle.r, particle.mass);
			p.isWall = true;
			p.x = -particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		} else if (particle.x + particle.r >= W) {
			p = new Particle(0, particle.r, particle.mass);
			p.isWall = true;
			p.x = W + particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		}
		if (this.open) {
			if (Math.abs(particle.y - getBaseLine()) < particle.r) {
				if (particle.x <= (W / 2 - D / 2) || particle.x >= (W / 2 + D / 2)) {
					p = new Particle(0, particle.r, particle.mass);
					p.isWall = true;
					p.y = getBaseLine() - particle.r;
					p.x = particle.x;
					p.speedX = 0;
					p.speedY = 0;
					ret.add(p);
				} else if (particle.x - particle.r <= (W / 2 - D / 2)
						&& getDistance(particle.x, particle.y, (W / 2 - D / 2), (getBaseLine())) < particle.r) {
					p = new Particle(0, 0, particle.mass);
					p.y = getBaseLine();
					p.x = (W / 2 - D / 2);
					p.isWall = true;
					p.speedX = 0;
					p.speedY = 0;
					ret.add(p);
				} else if (particle.x + particle.r >= (W / 2 + D / 2)
						&& getDistance(particle.x, particle.y, (W / 2 + D / 2), (getBaseLine())) < particle.r) {
					p = new Particle(0, 0, particle.mass);
					p.y = getBaseLine();
					p.x = (W / 2 + D / 2);
					p.speedX = 0;
					p.isWall = true;
					p.speedY = 0;
					ret.add(p);
				}
			}
		} else {
			if (particle.y - particle.r < (getBaseLine())) {
				p = new Particle(0, particle.r, particle.mass);
				p.isWall = true;
				p.x = particle.x;
				p.y = getBaseLine() - particle.r;
				p.speedX = 0;
				p.speedY = 0;
				ret.add(p);
			}
		}
		return ret;

	}

	public Map<Particle, Set<Particle>> filterNeighbors(Map<Particle, Set<Particle>> original) {
		Map<Particle, Set<Particle>> ret = new HashMap<>();
		for (Particle particle : original.keySet()) {
			HashSet<Particle> set = new HashSet<>();
			for (Particle neighbor : original.get(particle)) {
				if (getDistance(particle.x, particle.y, neighbor.x, neighbor.y) <= (particle.r + neighbor.r)) {
					set.add(neighbor);
				}
			}
			ret.put(particle, set);
		}
		return ret;
	}

	public void printOvitoState(int iteration, Map<Particle, Set<Particle>> otherp) {

		List<Particle> walls = new LinkedList<Particle>();
		System.out.println(particles.size() + 4 + walls.size());
		System.out.println("t " + iteration);

		double max_speed = findMaxSpeed();

		for (Particle p : particles) {
			double relative_speed = 1;
			if (max_speed > 0) {
				relative_speed = Math.sqrt(Math.pow(p.speedX, 2) + Math.pow(p.speedY, 2)) / max_speed;
			}
			System.out.println(
					p.id + "\t" + p.x + "\t" + p.y + "\t" + p.r + "\t" + p.mass + "\t" + relative_speed + "\t 0\t 1");
		}
		System.out.println(particles.size() + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + MASS + "\t 0\t0\t 0");
		System.out.println(particles.size() + 1 + "\t" + W + "\t" + 0 + "\t" + 0 + "\t" + MASS + "\t 0\t 0\t 1");
		System.out.println(particles.size() + 2 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + MASS + "\t 0\t 0\t 1");
		System.out.println(particles.size() + 3 + "\t" + W + "\t" + L + "\t" + 0 + "\t" + MASS + "\t 0\t 0\t 1");
	}

	private double findMaxSpeed() {
		double ret = 0;
		for (Particle particle : particles) {
			double speed = Math.sqrt(Math.pow(particle.speedX, 2) + Math.pow(particle.speedY, 2));
			if (speed > ret) {
				ret = speed;
			}
		}
		return ret;
	}

	public double totalArea() {
		double ret = 0;
		for (Particle particle : particles) {
			ret += Math.PI * particle.r * particle.r;
		}
		return ret;
	}

	public double getTargetX(Particle p) {
		return W / 2;
	}

	public double getTargetY(Particle p) {
		if (p.y + p.r < getBaseLine())
			return 0;
		return getBaseLine();
	}

	private double getBaseLine() {
		return DISTANCE_BOTTOM + DISTANCE_LIMIT;
	}

	public static void main(String[] args) {
		double L = 10;
		double W = 5;
		double D = 2;

		double tf = 5;
		double deltaT = 0.000001;
		double deltaT2 = 0.02;
		double kn = 1E5;
		double kt = 2 * kn;

		int maxParticles = Integer.MAX_VALUE;

		boolean open = true;
		int option = 0;

		try {
			L = Double.valueOf(args[0]);
			W = Double.valueOf(args[1]);
			D = Double.valueOf(args[2]);
			deltaT = Double.valueOf(args[3]);
			deltaT2 = Double.valueOf(args[4]);
			tf = Double.valueOf(args[5]);
			kn = Double.valueOf(args[6]);
			kt = Double.valueOf(args[7]);
			if (args[8].toLowerCase().equals("open"))
				open = true;
			else if (args[8].toLowerCase().equals("closed"))
				open = false;
			else
				throw new Exception();
			if (args[9].toLowerCase().equals("ovito"))
				option = 0;
			else if (args[9].toLowerCase().equals("energy"))
				option = 1;
			else if (args[9].toLowerCase().equals("flow"))
				option = 2;
			else
				throw new Exception();
			if (args.length == 11) {
				maxParticles = Integer.valueOf(args[10]);
			}
		} catch (Exception e) {
			System.err.println(
					"Wrong Parameters. Expect L W D deltaT deltaT2 tf kn kt [open|closed] [ovito|energy|flow] (maxParticles)");
			return;
		}

		Accelerator accelerator = new DynamicsAccelerator(kn, kt);
		IntegralMethod integralMethod = new Beeman(deltaT, accelerator);

		PedestrianDynamics p = new PedestrianDynamics(L, W, D, tf, deltaT, deltaT2, open, maxParticles, integralMethod);
		p.run(option);

	}
}
