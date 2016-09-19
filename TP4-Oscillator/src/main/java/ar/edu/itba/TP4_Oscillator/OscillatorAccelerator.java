package ar.edu.itba.TP4_Oscillator;

public class OscillatorAccelerator implements Accelerator{
	
	private double k;
	private double g;


	public OscillatorAccelerator(double k, double g) {
		super();
		this.k = k;
		this.g = g;
	}

	public double getForceX(double time, Particle p) {
		return -k*p.getX()  - g*p.getSpeedX();
	}

	public double getForceY(double time, Particle p) {
		return -k*p.getY()  - g*p.getSpeedY();
	}

}
