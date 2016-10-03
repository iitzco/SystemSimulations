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

	final static double EPSILON = 0.000001;

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
		p.rListX[2] = (accelerator.getForceX(0, p)) / p.mass;
		p.rListX[3] = -(k / m) * p.rListX[1] - (g / m) * p.rListX[2];
		p.rListX[4] = -(k / m) * p.rListX[2] - (g / m) * p.rListX[3];
		p.rListX[5] = -(k / m) * p.rListX[3] - (g / m) * p.rListX[4];
	}

	public static void runWithErrors(Oscillator analiticOscillator, List<Oscillator> list, double tf, double deltaT) {

		double currentTime = 0;

		double[] errors = new double[3];

		for (Oscillator oscillator : list) {
			System.out.print(oscillator.integralMethod.getName() + "\t");
		}
		System.out.println();
		while (currentTime < tf) {
			analiticOscillator.p = analiticOscillator.integralMethod.moveParticle(analiticOscillator.p, currentTime);
			for (int i = 0; i < list.size(); i++) {
				Oscillator oscillator = list.get(i);
				oscillator.p = oscillator.integralMethod.moveParticle(oscillator.p, currentTime);
				errors[i] += (Math.pow(analiticOscillator.p.x - oscillator.p.x, 2));
			}
			currentTime += deltaT;
		}

		for (int i = 0; i < errors.length; i++) {
			errors[i] /= (tf / deltaT);
			System.out.print(errors[i] + "\t");
		}
		System.out.println();
	}

	public static void runForGraphics(Oscillator analiticOscillator, List<Oscillator> list, double tf, double deltaT) {

		double currentTime = 0;

		for (Oscillator oscillator : list) {
			System.out.print(oscillator.integralMethod.getName() + "\t");
		}
		System.out.print(analiticOscillator.integralMethod.getName());
		System.out.println();
		while (currentTime < tf) {
			analiticOscillator.p = analiticOscillator.integralMethod.moveParticle(analiticOscillator.p, currentTime);
			for (int i = 0; i < list.size(); i++) {
				Oscillator oscillator = list.get(i);
				oscillator.p = oscillator.integralMethod.moveParticle(oscillator.p, currentTime);
				System.out.print(oscillator.p.x + "\t");
			}
			System.out.print(analiticOscillator.p.x + "\t");
			System.out.println();
			currentTime += deltaT;
		}

	}

	public static void runForOvito(Oscillator analiticOscillator, List<Oscillator> list, double tf, double deltaT,
			double deltaT2) {

		double currentTime = 0;

		int[][] rgb = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };

		while (currentTime < tf) {
			boolean isTimeToPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;
			if (isTimeToPrint) {
				System.out.println(8);
				System.out.println("t " + Math.round(currentTime / deltaT2));
			}
			analiticOscillator.p = analiticOscillator.integralMethod.moveParticle(analiticOscillator.p, currentTime);
			int i = 0;
			for (; i < list.size(); i++) {
				Oscillator oscillator = list.get(i);
				oscillator.p = oscillator.integralMethod.moveParticle(oscillator.p, currentTime);
				if (isTimeToPrint) {
					System.out.println(i + "\t" + oscillator.p.x + "\t" + i + "\t" + 0.4 + "\t" + rgb[i][0] + "\t"
							+ rgb[i][1] + "\t" + rgb[i][2]);
				}
			}
			if (isTimeToPrint) {
				System.out.println(
						i + "\t" + analiticOscillator.p.x + "\t" + i + "\t" + 0.4 + "\t" + 1 + "\t" + 1 + "\t" + 1);
				System.out.println(i + 1 + "\t" + (-1.5) + "\t" + 3 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
				System.out.println(i + 2 + "\t" + 1.5 + "\t" + 3 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
				System.out.println(i + 3 + "\t" + (-1.5) + "\t" + (-5) + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
				System.out.println(i + 4 + "\t" + 1.5 + "\t" + (-5) + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
			}

			currentTime += deltaT;
		}
	}

	public static void main(String[] args) {

		double k = Math.pow(10, 4);
		double g = 100;
		double m = 70;
		double tf = 5;
		double deltaT = 0.001;
		double deltaT2 = 0.02;

		// 0 para posiciones en dt, 1 para errores, 2 para Ovito.

		int option = 0;

		try {
			option = Integer.valueOf(args[0]);
			deltaT = Double.valueOf(args[1]);
			if (option == 2)
				deltaT2 = Double.valueOf(args[2]);
		} catch (Exception e) {
			System.err.println("Wrong Parameters. Expect [1|2|3] deltaT (deltaT2)");
			return;
		}

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

		switch (option) {
		case 0:
			runForGraphics(analiticOscillator, l, tf, deltaT);
			break;
		case 1:
			runWithErrors(analiticOscillator, l, tf, deltaT);
			break;
		case 2:
			runForOvito(analiticOscillator, l, tf, deltaT, deltaT2);
			break;
		default:
			break;
		}
	}

}
