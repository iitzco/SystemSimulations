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

		locateParticles(10);
	}

	private void locateParticles(int n) {
		this.particles = new ArrayList<Particle>();

		boolean flag = true;
		int id = 0;

		while (flag && particles.size() < n) {
			double diameter = Math.random() * (this.D / 5 - this.D / 7) + (this.D / 7);
			double r = diameter / 2;
			double x = 0, y = 0;
			int tries = 0;
			do {
				x = Math.random() * (this.W - 2 * r) + r;
				y = Math.random() * (this.L - 2 * r) + r;
				tries++;
				if (tries == MAX_TRIES) {
					flag = false;
					break;
				}
			} while (overlap(x, y, diameter / 2));
			Particle p = new Particle(id++, diameter / 2, x, y, 0, 0, MASS);
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

	public void run() {
		double currentTime = 0;
		CellIndexMethod method = new CellIndexMethod(this.L, 2 * this.D / 5, false);
		int iteration = 0;
		while (currentTime < tf) {
			method.load(new HashSet<>(this.particles));
			Map<Particle, Set<Particle>> neighbors = method.findNeighbors();
			List<Particle> nextGen = new ArrayList<>();
			neighbors = filterNeighbors(neighbors);

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
			p.x = -particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		}
		if (particle.x + particle.r >= W) {
			p = new Particle(0, particle.r, particle.mass);
			p.x = W + particle.r;
			p.y = particle.y;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		}
		if (particle.y - particle.r < 0) {
			p = new Particle(0, particle.r, particle.mass);
			p.y = -particle.r;
			p.x = particle.x;
			p.speedX = 0;
			p.speedY = 0;
			ret.add(p);
		}
		if (particle.y + particle.r >= L) {
			p = new Particle(0, particle.r, particle.mass);
			p.y = particle.r;
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
		System.out.println(particles.size()+4);
		System.out.println("t " + iteration);
		for (Particle p : particles) {
			System.out.println(p.id + "\t" + p.x + "\t" + p.y + "\t" + p.r + "\t" + p.mass);
		}
		System.out.println(particles.size() + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size()+1 + "\t" + W + "\t" + 0 + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size()+2 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + MASS);
		System.out.println(particles.size()+3 + "\t" + W + "\t" + L + "\t" + 0 + "\t" + MASS);
	}

	public double totalArea() {
		double ret = 0;
		for (Particle particle : particles) {
			ret += Math.PI * particle.r * particle.r;
		}
		return ret;
	}

	public static void main(String[] args) {
		double L = 10;
		double W = 5;
		double D = 2;

		double tf = 5;
		double deltaT = 0.00001;
		double deltaT2 = 0.02;

		double kn = 1E5;
		double kt = 2 * kn;

		Accelerator accelerator = new GranularAccelerator(kn, kt);
		IntegralMethod integralMethod = new Beeman(deltaT, accelerator);

		GranularEnvironment g = new GranularEnvironment(L, W, D, tf, deltaT, deltaT2, integralMethod);
		g.run();

	}
}
