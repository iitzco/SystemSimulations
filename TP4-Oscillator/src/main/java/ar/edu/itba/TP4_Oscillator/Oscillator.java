package ar.edu.itba.TP4_Oscillator;

import java.util.LinkedList;
import java.util.List;

public class Oscillator {

	Accelerator accelerator;
	IntegralMethod integralMethod;
	double k;
	double g;
	double m;
	double deltaT;

	Particle p;

	public Oscillator(Accelerator accelerator, IntegralMethod integralMethod, double k, double g, double m, double tf,
			double deltaT) {
		super();
		this.accelerator = accelerator;
		this.integralMethod = integralMethod;
		this.k = k;
		this.g = g;
		this.m = m;
		this.deltaT = deltaT;

		p = new Particle(0, 0, 1, 0, -(g / (2 * m)), 0, m);
	}

	public void regressParticle() {
		p.prevX = p.x - deltaT * p.speedX + Math.pow(deltaT, 2) * accelerator.getForceX(0, p) / (2 * p.mass);
		p.prevY = 0;

		p.prevSpeedX = p.speedX - (deltaT / p.mass) * accelerator.getForceX(0, p);
		p.prevSpeedY = 0;
	}

	private void initializeR() {
		p.rListX[0] = p.x;
		p.rListX[1] = p.speedX;
		p.rListX[2] = (accelerator.getForceX(0, p))/p.mass;
		p.rListX[3] = -(k / m) * p.rListX[1] - (g / m) * p.rListX[2];
		p.rListX[4] = -(k / m) * p.rListX[2] - (g / m) * p.rListX[3];
		p.rListX[5] = -(k / m) * p.rListX[3] - (g / m) * p.rListX[4];
	}

	public static void run(List<Oscillator> list, double tf, double deltaT) {
		double currentTime = 0;
		for (Oscillator oscillator : list) {
			System.out.print(oscillator.integralMethod.getName() + "\t");
		}
		System.out.println();
		while (currentTime < tf) {
			for (Oscillator oscillator : list) {
				oscillator.p = oscillator.integralMethod.moveParticle(oscillator.p, currentTime);
				System.out.print(oscillator.p.x + "\t");
			}
			System.out.println();
			currentTime += deltaT;

		}
	}

	public static void main(String[] args) {

		double k = Math.pow(10, 4);
		double g = 100;
		double m = 70;
		double tf = 5;
		double deltaT = 0.01;

		Accelerator accelerator = new OscillatorAccelerator(k, g);
		IntegralMethod verlet = new OriginalVerlet(deltaT, accelerator);
		IntegralMethod beeman = new Beeman(deltaT, accelerator);
		IntegralMethod gear = new GearMethod(deltaT, accelerator);
		IntegralMethod analitic = new AnaliticMethod(k, g);

		Oscillator verletOscillator = new Oscillator(accelerator, verlet, k, g, m, tf, deltaT);
		verletOscillator.regressParticle();

		Oscillator beemanOscillator = new Oscillator(accelerator, beeman, k, g, m, tf, deltaT);
		beemanOscillator.regressParticle();

		Oscillator gearOscillator = new Oscillator(accelerator, gear, k, g, m, tf, deltaT);
		gearOscillator.initializeR();

		Oscillator analiticOscillator = new Oscillator(accelerator, analitic, k, g, m, tf, deltaT);

		List<Oscillator> l = new LinkedList<Oscillator>();
		l.add(verletOscillator);
		l.add(beemanOscillator);
		l.add(gearOscillator);
		l.add(analiticOscillator);

		run(l, tf, deltaT);

	}

}
