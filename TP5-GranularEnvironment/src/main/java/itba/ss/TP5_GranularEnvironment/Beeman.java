package itba.ss.TP5_GranularEnvironment;

import java.util.Set;

public class Beeman implements IntegralMethod {

	private double deltaT;
	private Accelerator accelerator;

	public Beeman(double deltaT, Accelerator accelerator) {
		super();
		this.deltaT = deltaT;
		this.accelerator = accelerator;
	}

	public Particle moveParticle(Particle p, Set<Particle> set) {
		Particle nextP = new Particle(p.id, p.r, p.x, p.y, p.mass);
		nextP.prevSpeedX = p.speedX;
		nextP.prevSpeedY = p.speedX;

		setPositions(nextP, p, set);
		setSpeeds(nextP, p, set);

		nextP.prevAccX = p.fX / p.mass;
		nextP.prevAccY = p.fY / p.mass;

		return nextP;
	}

	private void setSpeeds(Particle nextP, Particle p, Set<Particle> set) {
		double m = p.mass;

		nextP.speedX = p.speedX + (1.0 / 3) * (accelerator.getForceX(p, set) / m) * deltaT
				+ (5.0 / 6) * (accelerator.getForceX(p, set) / m) * deltaT - (1.0 / 6) * p.prevAccX * deltaT;
		nextP.speedY = p.speedY + (1.0 / 3) * (accelerator.getForceY(p, set) / m) * deltaT
				+ (5.0 / 6) * (accelerator.getForceY(p, set) / m) * deltaT - (1.0 / 6) * p.prevAccY * deltaT;

	}

	private void setPositions(Particle nextP, Particle p, Set<Particle> set) {
		double m = p.mass;

		nextP.x = p.x + p.speedX * deltaT + (2.0 / 3) * (accelerator.getForceX(p, set) / m) * Math.pow(deltaT, 2)
				- (1.0 / 6) * p.prevAccX * Math.pow(deltaT, 2);
		nextP.y = p.y + p.speedY * deltaT + (2.0 / 3) * (accelerator.getForceY(p, set) / m) * Math.pow(deltaT, 2)
				- (1.0 / 6) * p.prevAccY * Math.pow(deltaT, 2);
	}

	public String getName() {
		return "Beeman";
	}

}
