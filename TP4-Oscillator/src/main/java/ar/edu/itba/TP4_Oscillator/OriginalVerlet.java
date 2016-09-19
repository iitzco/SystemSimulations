package ar.edu.itba.TP4_Oscillator;

public class OriginalVerlet implements IntegralMethod {

	private double deltaT;
	private Accelerator accelerator;

	public OriginalVerlet(double deltaT, Accelerator accelerator) {
		super();
		this.deltaT = deltaT;
		this.accelerator = accelerator;
	}

	public Particle moveParticle(Particle p, double time) {
		Particle nextP = new Particle(p.getId(), p.getR(), p.getX(), p.getY(), p.getMass());
		setPositions(nextP, p, time);
		setSpeeds(nextP, p);
		return nextP;
	}

	private void setSpeeds(Particle nextP, Particle p) {
		nextP.setSpeedX((nextP.getX() - p.getPrevX()) / (2 * deltaT));
		nextP.setSpeedY((nextP.getY() - p.getPrevY()) / (2 * deltaT));
	}

	private void setPositions(Particle nextP, Particle p, double time) {
		nextP.setX(2 * p.getX() - p.getPrevX() + (Math.pow(deltaT, 2) / p.getMass()) * accelerator.getForceX(time, p));
		nextP.setY(2 * p.getY() - p.getPrevY() + (Math.pow(deltaT, 2) / p.getMass()) * accelerator.getForceY(time, p));
	}

}
