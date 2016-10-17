package itba.ss.TP5_GranularEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GranularEnvironment {

	static final double EPSILON = 0.000001;

	static final double MASS = 0.01;
	static final double MAX_TRIES = 10000;

	static final double DISTANCE_BOTTOM = 1;

	double L;
	double W;
	double D;

	double m = 0.01;

	double tf;
	double dt;
	double dt2;

	List<Particle> particles;

	int N;

	IntegralMethod integralMethod;

	public GranularEnvironment(double l, double w, double d, double tf, double dT, double dT2,
			IntegralMethod integralMethod) {
		super();
		L = l;
		W = w;
		D = d;

		this.tf = tf;
		this.dt = dT;
		this.dt2 = dT2;

		this.integralMethod = integralMethod;

		locateParticles();
	}

	private void locateParticles() {
		this.particles = new ArrayList<Particle>();

		boolean flag = true;
		int id = 0;

		while (flag) {
			double diameter = Math.random() * (this.D / 5 - this.D / 7) + (this.D / 7);
			double r = diameter / 2;
			double x = 0, y = 0;
			int tries = 0;
			do {
				x = Math.random() * (this.W - 2 * r) + r;
				y = Math.random() * (this.L - 2 * r) + r + (DISTANCE_BOTTOM + D / 5);
				tries++;
				if (tries == MAX_TRIES) {
					flag = false;
					break;
				}
			} while (overlap(x, y, diameter / 2));
			if (flag) {
				Particle p = new Particle(id++, diameter / 2, x, y, 0, 0, MASS);
				this.particles.add(p);
			}
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

	public void run() {
		double currentTime = 0;
		CellIndexMethod method = new CellIndexMethod(5 * (this.L + (DISTANCE_BOTTOM + D / 5)), this.D / 5, false);
		int iteration = 0;
		while (currentTime < tf) {
			try {
				method.load(new HashSet<>(this.particles));
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Delta T was too big. Try with smaller");
				System.exit(1);
			}
			Map<Particle, Set<Particle>> neighbors = method.findNeighbors();
			List<Particle> nextGen = new ArrayList<>();
			neighbors = filterNeighbors(neighbors);

			checkRelocation();

			addWallParticles(neighbors);

			for (Particle particle : particles) {
				Particle p = integralMethod.moveParticle(particle, neighbors.get(particle));
				nextGen.add(p);
			}
			if (Math.abs(currentTime / dt2 - Math.round(currentTime / dt2)) < EPSILON)
				printOvitoState(iteration);
			this.particles = nextGen;
			currentTime += dt;
			iteration++;
		}
	}

	private void checkRelocation() {
		for (Particle particle : particles) {
			if (particle.y - particle.r < D / 5) {
				relocateParticle(particle);
			}
		}

	}

	private void addWallParticles(Map<Particle, Set<Particle>> neighbors) {
		for (Particle particle : particles) {
			List<Particle> newParticles = getContactParticles(particle);
			neighbors.get(particle).addAll(newParticles);
		}
	}

	private void relocateParticle(Particle particle) {
		particle.speedX = 0;
		particle.speedY = 0;
		particle.prevSpeedX = 0;
		particle.prevSpeedY = 0;
		particle.prevAccX = 0;
		particle.prevAccY = 0;
		particle.x = Math.random() * (this.W - 2 * particle.r) + particle.r;
		Particle max = findHighestParticle(particle.x, particle.r);
		double y = 0;
		if (max == null) {
			y = L;
		} else {
			y = Math.max(max.y + max.r, L);
		}
		particle.y = y + particle.r;

	}

	private Particle findHighestParticle(double x, double r) {
		double aux = 0;
		Particle p = null;
		for (Particle particle : particles) {
			if (Math.abs(particle.x - x) < particle.r + r && particle.y > aux) {
				aux = particle.y;
				p = particle;
			}
		}
		return p;
	}

	private List<Particle> getContactParticles(Particle particle) {
		List<Particle> ret = new ArrayList<>();
		Particle p = null;

		if (particle.x - particle.r < 0) {
			p = new Particle(0, particle.r, particle.mass);
			p.x = -particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		} else if (particle.x + particle.r >= W) {
			p = new Particle(0, particle.r, particle.mass);
			p.x = W + particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		}
		if (particle.y - particle.r < (DISTANCE_BOTTOM + D / 5) && !(particle.x - particle.r >= (W / 2 - D / 2)
				&& particle.x + particle.r < (W / 2 + D / 2) && particle.y - particle.r < (DISTANCE_BOTTOM + D / 5))) {
			p = new Particle(0, particle.r, particle.mass);
			p.y = (DISTANCE_BOTTOM + D / 5) - particle.r;
			p.x = particle.x;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
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

	public void printOvitoState(int iteration) {
		System.out.println(particles.size() + 4);
		System.out.println("t " + iteration);
		for (Particle p : particles) {
			System.out.println(p.id + "\t" + p.x + "\t" + p.y + "\t" + p.r + "\t" + p.mass);
		}
		System.out.println(particles.size() + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size() + 1 + "\t" + W + "\t" + 0 + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size() + 2 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size() + 3 + "\t" + W + "\t" + L + "\t" + 0 + "\t" + MASS);
	}

	public double totalArea() {
		double ret = 0;
		for (Particle particle : particles) {
			ret += Math.PI * particle.r * particle.r;
		}
		return ret;
	}

	public static void main(String[] args) {
		double L = 1;
		double W = 0.75;
		double D = 0.1 * W;

		double tf = 5;
		double deltaT = 0.00001;
		double deltaT2 = 0.02;

		try {
			L = Double.valueOf(args[0]);
			W = Double.valueOf(args[1]);
			D = Double.valueOf(args[2]);
			deltaT = Double.valueOf(args[3]);
			deltaT2 = Double.valueOf(args[4]);
			tf = Double.valueOf(args[5]);
		} catch (Exception e) {
			System.err.println("Wrong Parameters. Expect L W D deltaT deltaT2 tf");
			return;
		}

		double kn = 1E5;
		double kt = 2 * kn;

		Accelerator accelerator = new GranularAccelerator(kn, kt);
		IntegralMethod integralMethod = new Beeman(deltaT, accelerator);

		GranularEnvironment g = new GranularEnvironment(L, W, D, tf, deltaT, deltaT2, integralMethod);
		g.run();

	}
}
