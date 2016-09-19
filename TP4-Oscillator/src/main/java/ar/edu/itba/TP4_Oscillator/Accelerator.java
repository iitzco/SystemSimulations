package ar.edu.itba.TP4_Oscillator;

public interface Accelerator {
	
	public double getForceX(double time, Particle p);
	
	public double getForceY(double time, Particle p);

}
