package itba.ss.TP4_MarsTravel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

	Accelerator accelerator;
	IntegralMethod integralMethod;

	BigDecimal deltaT;

	final static double EPSILON = 0.000001;

	Particle sun;
	Particle earth;
	Particle ship;
	Particle mars;

	public SolarSystem(Accelerator accelerator, IntegralMethod integralMethod, BigDecimal deltaT) {
		this.accelerator = accelerator;
		this.integralMethod = integralMethod;
		this.deltaT = deltaT;

		initializeCorps();
	}

	private void initializeCorps() {
		earth = new Particle(0, new BigDecimal(6371 * 1000),
				new BigDecimal(1.391734353396533).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(-0.571059040560652).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(10.801963811159256).multiply(new BigDecimal(1000)),
				new BigDecimal(27.565215006898345).multiply(new BigDecimal(1000)),
				new BigDecimal(5.972).multiply(new BigDecimal(10).pow(24)));

		sun = new Particle(1, new BigDecimal(695700 * 1000), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
				new BigDecimal(0), new BigDecimal(1.988).multiply(new BigDecimal(10).pow(30)));

		// mars = new Particle(2, 3389.9 * 1000, 0.831483493435295 *
		// Math.pow(10, 8) * 1000,
		// -1.914579540822006 * Math.pow(10, 8) * 1000, 23.637912321314047 *
		// 1000, 11.429021426712032 * 1000,
		// 6.4185 * Math.pow(10, 23));
		// ship = new Particle(3, 1, x, y, speedX, speedY, 2*Math.pow(10, 5));
	}

	public void regressParticle(Particle p, List<Particle> l) {
		p.prevX = p.x.subtract(deltaT.multiply(p.speedX)).add(deltaT.pow(2).multiply(
				accelerator.getForceX(p, l).divide(p.mass.multiply(new BigDecimal(2)), MathContext.DECIMAL32)));

		p.prevY = p.y.subtract(deltaT.multiply(p.speedY)).add(deltaT.pow(2).multiply(
				accelerator.getForceY(p, l).divide(p.mass.multiply(new BigDecimal(2)), MathContext.DECIMAL32)));

		p.prevSpeedX = p.speedX
				.subtract(deltaT.divide(p.mass, MathContext.DECIMAL32).multiply(accelerator.getForceX(p, l)));
		p.prevSpeedY = p.speedY
				.subtract(deltaT.divide(p.mass, MathContext.DECIMAL32).multiply(accelerator.getForceY(p, l)));

	}

	public static void runForOvito(SolarSystem solarSystem, BigDecimal tf, BigDecimal deltaT, BigDecimal deltaT2) {

		BigDecimal currentTime = new BigDecimal(0);

		while (currentTime.compareTo(tf) == -1) {
			List<Particle> lForEarth = new ArrayList<Particle>();
			lForEarth.add(solarSystem.sun);
			List<Particle> lForSun = new ArrayList<Particle>();
			lForSun.add(solarSystem.earth);

			boolean isTimeToPrint = Math.abs(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue())
					- Math.round(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue()) < EPSILON;
			if (isTimeToPrint) {
				System.out.println(2);
				System.out.println("t " + Math.round(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue()));
			}
			solarSystem.earth = solarSystem.integralMethod.moveParticle(solarSystem.earth, lForEarth);
			solarSystem.sun = solarSystem.integralMethod.moveParticle(solarSystem.sun, lForSun);
			if (isTimeToPrint) {
				System.out
						.println(0 + "\t" + solarSystem.earth.x.doubleValue() + "\t" + solarSystem.earth.y.doubleValue()
								+ "\t" + solarSystem.earth.mass.doubleValue() + "\t" + solarSystem.earth.r);
				System.out.println(1 + "\t" + solarSystem.sun.x.doubleValue() + "\t" + solarSystem.sun.y.doubleValue()
						+ "\t" + solarSystem.sun.mass.doubleValue() + "\t" + solarSystem.sun.r);
			}

			currentTime = currentTime.add(deltaT);
		}
	}

	public static void main(String[] args) {

		BigDecimal deltaT = new BigDecimal(86400);
		BigDecimal deltaT2 = new BigDecimal(86400);
		BigDecimal tf = new BigDecimal(31536000);

		Accelerator accelerator = new GravityAccelerator();
		IntegralMethod beeman = new Beeman(deltaT, accelerator);

		SolarSystem solarSystem = new SolarSystem(accelerator, beeman, deltaT);

		List<Particle> lForEarth = new ArrayList<Particle>();
		lForEarth.add(solarSystem.sun);
		List<Particle> lForSun = new ArrayList<Particle>();
		lForSun.add(solarSystem.earth);

		solarSystem.regressParticle(solarSystem.earth, lForEarth);
		solarSystem.regressParticle(solarSystem.sun, lForSun);

		runForOvito(solarSystem, tf, deltaT, deltaT2);

	}

}
