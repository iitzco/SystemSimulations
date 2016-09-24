package ar.edu.itba.TP4_Oscillator;

public class OscillatorAccelerator implements Accelerator {

	private double k;
	private double g;

	public OscillatorAccelerator(double k, double g) {
		super();
		this.k = k;
		this.g = g;
	}

	public double getForceX(double time, Particle p) {
		return -k * p.x - g * p.speedX;
	}

	public double getForceY(double time, Particle p) {
		return -k * p.y - g * p.speedY;
	}

}
