package itba.ss.TP4_MarsTravel;

import java.util.List;

public class Beeman implements IntegralMethod {

	private double deltaT;
	private Accelerator accelerator;

	public Beeman(double deltaT, Accelerator accelerator) {
		super();
		this.deltaT = deltaT;
		this.accelerator = accelerator;
	}

	private void setSpeeds(Particle nextP, Particle p, List<Particle> l) {
		double m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, 0, 0, p.prevSpeedX, p.prevSpeedY, 0,
				0, p.mass);
		Particle currP = new Particle(p.id, p.r, nextP.x, nextP.y, 0, 0, p.speedX, p.speedY, 0, 0, p.mass);

		nextP.speedX = p.speedX
				+ (1.0 / 3) * accelerator.getForceX(currP, l) * deltaT / m
				+ (5.0 / 6) * accelerator.getForceX(p, l) * deltaT / m
				- (1.0 / 6) * accelerator.getForceX(previousP, l) * deltaT / m;

		nextP.speedY = p.speedY
				+ (1.0 / 3) * accelerator.getForceY(currP, l) * deltaT / m
				+ (5.0 / 6) * accelerator.getForceY(p, l) * deltaT / m
				- (1.0 / 6) * accelerator.getForceY(previousP, l) * deltaT / m;
	}

	private void setPositions(Particle nextP, Particle p, List<Particle> l) {
		double m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, 0, 0, p.prevSpeedX, p.prevSpeedY, 0,
				0, p.mass);

//		System.out.println(String.format("getForce %f", accelerator.getForce(p, l)));
//        System.out.println(String.format("getForceX %f", accelerator.getForceX(p, l)));

		nextP.x = p.x + p.speedX * deltaT
				+ (2.0 / 3) * accelerator.getForceX(p, l) * Math.pow(deltaT, 2)/ m
				- (1.0 / 6) * accelerator.getForceX(previousP, l) * Math.pow(deltaT, 2) / m;

		nextP.y = p.y + p.speedY * deltaT
				+ (2.0 / 3) * accelerator.getForceY(p, l) * Math.pow(deltaT, 2)/ m
				- (1.0 / 6) * accelerator.getForceY(previousP, l) * Math.pow(deltaT, 2) / m;
	}

	public String getName() {
		return "Beeman";
	}

	public Particle moveParticle(Particle p, List<Particle> l) {
		Particle nextP = new Particle(p.id, p.r, p.x, p.y, p.mass);
		nextP.prevSpeedX = p.speedX;
		nextP.prevSpeedY = p.speedX;

		setPositions(nextP, p, l);
		setSpeeds(nextP, p, l);
		return nextP;
	}

}
