package itba.ss.TP5_GranularEnvironment;

import java.util.Set;

public class GranularAccelerator implements Accelerator {

	private double kn;
	private double kt;
	private static final double G = -10;

	public GranularAccelerator(double kn, double kt) {
		super();
		this.kn = kn;
		this.kt = kt;
	}

	public double getForceX(Particle p, Set<Particle> set) {
		if (p.fX == null) {
			setForces(p, set);
		}
		return p.fX;
	}

	public double getForceY(Particle p, Set<Particle> set) {
		if (p.fY == null) {
			setForces(p, set);
		}
		return p.fY;
	}

	private void setForces(Particle p, Set<Particle> set) {
		double fX = 0;
		double fY = p.mass * G;
		for (Particle particle : set) {
			fX += getFN(p, particle) * getENX(p, particle) + getFT(p, particle) * (-(getENY(p, particle)));
			fY += getFN(p, particle) * getENY(p, particle) + getFT(p, particle) * getENX(p, particle);
		}
		p.fX = fX;
		p.fY = fY;
	}

	private double getENY(Particle p, Particle particle) {
		return (particle.y - p.y) / getDistance(particle.x, particle.y, p.x, p.y);
	}

	private double getENX(Particle p, Particle particle) {
		return (particle.x - p.x) / getDistance(particle.x, particle.y, p.x, p.y);
	}

	private double getFN(Particle p, Particle other) {
		return -kn * getEpsilon(p, other);
	}

	private double getFT(Particle p, Particle other) {
		return -kt * getEpsilon(p, other) * (((p.speedX - other.speedX) * (-getENY(p, other)))
				+ ((p.speedY - other.speedY) * (getENX(p, other))));
	}

	private double getEpsilon(Particle p, Particle other) {
		return p.r + other.r - (getDistance(p.x, p.y, other.x, other.y));
	}

	private double getDistance(double x0, double y0, double x1, double y1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
	}

}
