package itba.ss.TP6_PedestrianDynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PedestrianDynamics {

	static final double EPSILON = 0.000001;

	static final double MASS = 70;

	static final double MARGIN = 0.1;

	static final double DISTANCE_BOTTOM = 5;
	static final double DISTANCE_LIMIT = 1;

	static final double EXTRA_TIME_WHEN_FINISHED = 1;

	double L;
	double W;
	double D;

	double m = 0.01;

	double tf;
	double dt;
	double dt2;

	double desiredSpeed;

	int maxParticles;

	List<Particle> particles;
	Set<Particle> escapedParticles;
	List<Double> times;

	int N;

	long caudal = 0;

	IntegralMethod integralMethod;

	boolean finished;
	double extraTime;

	public PedestrianDynamics(double l, double w, double d, double tf, double dT, double dT2, int maxParticles,
			double desiredSpeed, IntegralMethod integralMethod) {
		super();
		L = l;
		W = w;
		D = d;

		this.tf = tf;
		this.dt = dT;
		this.dt2 = dT2;

		this.desiredSpeed = desiredSpeed;

		this.maxParticles = maxParticles;

		this.integralMethod = integralMethod;

		locateParticles(maxParticles);

		integralMethod.getAccelerator().setContext(this);

		escapedParticles = new HashSet<>();
		times = new ArrayList<>();

		finished = false;
		extraTime = 0;
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
			Particle p = new Particle(id++, diameter / 2, x, y, 0, 0, MASS, desiredSpeed);
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
		while (currentTime < tf && !(finished && extraTime > EXTRA_TIME_WHEN_FINISHED)) {
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
				addPossibleEscape(p, currentTime);
			}
			if (Math.abs(currentTime / dt2 - Math.round(currentTime / dt2)) < EPSILON) {
				printOvitoState(iteration, neighbors);
			}

			this.particles = nextGen;
			currentTime += dt;
			iteration++;
			if (finished)
				extraTime += dt;
		}
	}

	private void addPossibleEscape(Particle particle, double currentTime) {
		if (particle.y - particle.r < getBaseLine()) {
			if (!escapedParticles.contains(particle)) {
				escapedParticles.add(particle);
				times.add(currentTime);
				System.err.println(currentTime);
				if (escapedParticles.size() == N) {
					finished = true;
				}
			}
		}
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
		// X - right
		p = new Particle(N + 1, particle.r, particle.mass);
		p.isWall = true;
		p.x = -particle.r;
		p.y = particle.y;
		p.speedX = 0;
		p.speedY = 0;
		ret.add(p);
		// X - left
		p = new Particle(N + 2, particle.r, particle.mass);
		p.isWall = true;
		p.x = W + particle.r;
		p.y = particle.y;
		p.speedX = 0;
		p.speedY = 0;
		ret.add(p);
		// Y - BOTTOM
		if (particle.x <= (W / 2 - D / 2) || particle.x >= (W / 2 + D / 2)) {
			p = new Particle(N + 3, particle.r, particle.mass);
			p.isWall = true;
			p.y = getBaseLine() - particle.r;
			p.x = particle.x;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		} else {
			if (Math.abs(particle.y - getBaseLine()) < particle.r) {
				if (particle.x - particle.r <= (W / 2 - D / 2)
						&& getDistance(particle.x, particle.y, (W / 2 - D / 2), (getBaseLine())) < particle.r) {
					p = new Particle(N + 4, 0, particle.mass);
					p.y = getBaseLine();
					p.x = (W / 2 - D / 2);
					p.isWall = true;
					p.speedX = 0;
					p.speedY = 0;
					ret.add(p);
				} else if (particle.x + particle.r >= (W / 2 + D / 2)
						&& getDistance(particle.x, particle.y, (W / 2 + D / 2), (getBaseLine())) < particle.r) {
					p = new Particle(N + 5, 0, particle.mass);
					p.y = getBaseLine();
					p.x = (W / 2 + D / 2);
					p.speedX = 0;
					p.isWall = true;
					p.speedY = 0;
					ret.add(p);
				}
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

		System.out.println(particles.size() + 6);
		System.out.println("t " + iteration);

		double max_speed = findMaxSpeed();

		for (Particle p : particles) {
			double relative_speed = 1;
			if (max_speed > 0) {
				relative_speed = Math.sqrt(Math.pow(p.speedX, 2) + Math.pow(p.speedY, 2)) / max_speed;
			}
			System.out.println(
					p.id + "\t" + p.x + "\t" + p.y + "\t" + p.r + "\t" + p.mass + "\t" + relative_speed + "\t0\t1\t10");
		}
		System.out
				.println(particles.size() + "\t" + 0 + "\t" + getBaseLine() + "\t" + 0 + "\t" + MASS + "\t0\t0\t0\t1");
		System.out.println(
				particles.size() + 1 + "\t" + W + "\t" + getBaseLine() + "\t" + 0 + "\t" + MASS + "\t0\t0\t1\t2");
		System.out.println(
				particles.size() + 2 + "\t" + 0 + "\t" + (L + getBaseLine()) + "\t" + 0 + "\t" + MASS + "\t0\t0\t1\t3");
		System.out.println(
				particles.size() + 3 + "\t" + W + "\t" + (L + getBaseLine()) + "\t" + 0 + "\t" + MASS + "\t0\t0\t1\t4");
		System.out.println(particles.size() + 4 + "\t" + (W / 2 - D / 2) + "\t" + getBaseLine() + "\t" + 0 + "\t" + MASS
				+ "\t0\t0\t1\t5");
		System.out.println(particles.size() + 5 + "\t" + (W / 2 + D / 2) + "\t" + getBaseLine() + "\t" + 0 + "\t" + MASS
				+ "\t0\t0\t1\t6");
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
		if (p.x - p.r <= W / 2 - D / 2 + MARGIN)
			return W / 2 - D / 2 + p.r + MARGIN;
		if (p.x + p.r >= W / 2 + D / 2 - MARGIN)
			return W / 2 + D / 2 - p.r - MARGIN;
		return p.x;
	}

	public double getTargetY(Particle p) {
		if (p.y < getBaseLine())
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

		double desiredSpeed = 6;

		int maxParticles = Integer.MAX_VALUE;

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
			maxParticles = Integer.valueOf(args[8]);
			desiredSpeed = Double.valueOf(args[9]);
		} catch (Exception e) {
			System.err.println("Wrong Parameters. Expect L W D deltaT deltaT2 tf kn kt maxParticles desiredSpeed");
			return;
		}

		Accelerator accelerator = new DynamicsAccelerator(kn, kt);
		IntegralMethod integralMethod = new Beeman(deltaT, accelerator);

		PedestrianDynamics p = new PedestrianDynamics(L, W, D, tf, deltaT, deltaT2, maxParticles, desiredSpeed,
				integralMethod);
		p.run(option);

	}
}
