package itba.ss.TP5_GranularEnvironment;

public class Beeman implements IntegralMethod {

	private double deltaT;
	private Accelerator accelerator;

	public Beeman(double deltaT, Accelerator accelerator) {
		super();
		this.deltaT = deltaT;
		this.accelerator = accelerator;
	}

	public Particle moveParticle(Particle p, double time) {
		Particle nextP = new Particle(p.id, p.r, p.x, p.y, p.mass);
		nextP.prevSpeedX = p.speedX;
		nextP.prevSpeedY = p.speedX;

		setPositions(nextP, p, time);
		setSpeeds(nextP, p, time);
		return nextP;
	}

	private void setSpeeds(Particle nextP, Particle p, double time) {
		double m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, 0, 0, p.prevSpeedX, p.prevSpeedY, 0, 0, p.mass);
		Particle currP = new Particle(p.id, p.r, nextP.x, nextP.y, 0, 0, p.speedX, p.speedY, 0, 0, p.mass);

		nextP.speedX = p.speedX + (1.0 / 3) * (accelerator.getForceX(time + deltaT, currP) / m) * deltaT
				+ (5.0 / 6) * (accelerator.getForceX(time, p) / m) * deltaT
				- (1.0 / 6) * (accelerator.getForceX(time - deltaT, previousP) / m) * deltaT;
		nextP.speedY = p.speedY + (1.0 / 3) * (accelerator.getForceY(time + deltaT, currP) / m) * deltaT
				+ (5.0 / 6) * (accelerator.getForceY(time, p) / m) * deltaT
				- (1.0 / 6) * (accelerator.getForceY(time - deltaT, previousP) / m) * deltaT;

	}

	private void setPositions(Particle nextP, Particle p, double time) {
		double m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, 0, 0, p.prevSpeedX, p.prevSpeedY, 0, 0, p.mass);

		nextP.x = p.x + p.speedX * deltaT + (2.0 / 3) * (accelerator.getForceX(time, p) / m) * Math.pow(deltaT, 2)
				- (1.0 / 6) * (accelerator.getForceX(time - deltaT, previousP) / m) * Math.pow(deltaT, 2);
		nextP.y = p.y + p.speedY * deltaT + (2.0 / 3) * (accelerator.getForceY(time, p) / m) * Math.pow(deltaT, 2)
				- (1.0 / 6) * (accelerator.getForceY(time - deltaT, previousP) / m) * Math.pow(deltaT, 2);
	}

	public String getName() {
		return "Beeman";
	}

}
