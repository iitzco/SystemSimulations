package itba.ss.TP5_GranularEnvironment;

public class GranularAccelerator implements Accelerator {

	private double kn;
	private double kt;
	private final double G = 10;

	public GranularAccelerator(double kn, double kt) {
		super();
		this.kn = kn;
		this.kt = kt;
	}

	public double getForceX(double time, Particle p) {
		return 0;
	}

	public double getForceY(double time, Particle p) {
		return 0;
	}

}
