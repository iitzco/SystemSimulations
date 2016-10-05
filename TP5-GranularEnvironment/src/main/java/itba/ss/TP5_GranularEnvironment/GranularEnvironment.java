package itba.ss.TP5_GranularEnvironment;

import java.util.ArrayList;
import java.util.List;

public class GranularEnvironment {

	static final double MASS = 0.01;
	static final double MAX_TRIES = 10000;

	double L;
	double W;
	double D;

	double m = 0.01;
	double kt = 2;
	double kn = Math.pow(10, 5);

	List<Particle> particles;
	int N;

	public GranularEnvironment(double l, double w, double d) {
		super();
		L = l;
		W = w;
		D = d;

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
				y = Math.random() * (this.L - 2 * r) + r;
				tries++;
				if (tries == MAX_TRIES) {
					flag = false;
					break;
				}
			} while (overlap(x, y, diameter / 2));
			this.particles.add(new Particle(id++, diameter / 2, x, y, 0, 0, MASS));
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

	public void printOvitoState() {
		System.out.println(N);
		System.out.println("0");
		for (Particle p : particles) {
			System.out.println(p.id + "\t" + p.x + "\t" + p.y + "\t" + p.r + "\t" + p.mass);
		}
	}

	public double totalArea() {
		double ret = 0;
		for (Particle particle : particles) {
			ret += Math.PI * particle.r * particle.r;
		}
		return ret;
	}

	public static void main(String[] args) {

		GranularEnvironment g = new GranularEnvironment(10, 5, 2);
		g.printOvitoState();

	}
}
