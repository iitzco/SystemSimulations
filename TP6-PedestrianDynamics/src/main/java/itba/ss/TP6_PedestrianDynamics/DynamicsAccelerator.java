package itba.ss.TP6_PedestrianDynamics;

import java.util.Set;

public class DynamicsAccelerator implements Accelerator {

	private double kn;
	private double kt;

	private double A = 2000;
	private double B = 0.08;

	private PedestrianDynamics pd;

	public DynamicsAccelerator(double kn, double kt) {
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
		double fY = 0;
		for (Particle particle : set) {
			double epsilon = getEpsilon(p, particle);
			if (epsilon < 0) {
				// Granular Force
				fX += getFN(p, particle) * getENX(p, particle) + getFT(p, particle) * (-(getENY(p, particle)));
				fY += getFN(p, particle) * getENY(p, particle) + getFT(p, particle) * getENX(p, particle);
			}
			// Social Force
			if (!particle.isWall) {
				fX += A * Math.exp(-epsilon / B) * getENX(p, particle);
				fY += A * Math.exp(-epsilon / B) * getENY(p, particle);
			}

		}
		// Desired Force
		double x = pd.getTargetX(p);
		double y = pd.getTargetY(p);

		fX += p.mass * (((p.desiredSpeed * getENX(p, x, y)) - p.speedX)) / 0.5;
		fY += p.mass * (((p.desiredSpeed * getENY(p, x, y)) - p.speedY)) / 0.5;

		p.fX = fX;
		p.fY = fY;
	}

	private double getENY(Particle p, double x, double y) {
		return (y - p.y) / getDistance(x, y, p.x, p.y);
	}

	private double getENX(Particle p, double x, double y) {
		return (x - p.x) / getDistance(x, y, p.x, p.y);
	}

	private double getENY(Particle p, Particle particle) {
		return (particle.y - p.y) / getDistance(particle.x, particle.y, p.x, p.y);
	}

	private double getENX(Particle p, Particle particle) {
		return (particle.x - p.x) / getDistance(particle.x, particle.y, p.x, p.y);
	}

	private double getFN(Particle p, Particle other) {
		return -kn * (-1) * getEpsilon(p, other);
	}

	private double getFT(Particle p, Particle other) {
		return -kt * (-1) * getEpsilon(p, other) * (((p.speedX - other.speedX) * (-getENY(p, other)))
				+ ((p.speedY - other.speedY) * (getENX(p, other))));
	}

	private double getEpsilon(Particle p, Particle other) {
		return (getDistance(p.x, p.y, other.x, other.y)) - (p.r + other.r);
	}

	private double getDistance(double x0, double y0, double x1, double y1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
	}

	@Override
	public void setContext(PedestrianDynamics pd) {
		this.pd = pd;
	}

}
