package ar.edu.itba.TP4_Oscillator;

public class App {

	public static void main(String[] args) {

		double k = Math.pow(10, 4);
		double g = 100;
		double m = 70;
		double tf = 5;

		double deltaT = 0.01;

		Accelerator accelerator = new OscillatorAccelerator(k, g);
		IntegralMethod integralMethod = new OriginalVerlet(deltaT, accelerator);

		Particle p = new Particle(0, 0, 1, 0, -(m / 2 * g), 0, m);
		p.setPrevX(p.getX() - deltaT * p.getSpeedX()
				+ Math.pow(deltaT, 2) * accelerator.getForceX(0, p) / (2 * p.getMass()));
		p.setPrevY(0);

		double currentTime = 0;
		while (currentTime < tf) {
			p = integralMethod.moveParticle(p, currentTime);
			System.out.println(p.getX());
			currentTime += deltaT;
		}
	}
}
