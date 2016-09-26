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

		mars = new Particle(2, new BigDecimal(3389.9 * 1000),
				new BigDecimal(0.831483493435295).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(-1.914579540822006).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(23.637912321314047).multiply(new BigDecimal(1000)),
				new BigDecimal(11.429021426712032).multiply(new BigDecimal(1000)),
				new BigDecimal(6.4185).multiply(new BigDecimal(10).pow(23)));

		mars = new Particle(2, new BigDecimal(3389.9 * 1000),
				new BigDecimal(0.831483493435295).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(-1.914579540822006).multiply(new BigDecimal(10).pow(11)),
				new BigDecimal(23.637912321314047).multiply(new BigDecimal(1000)),
				new BigDecimal(11.429021426712032).multiply(new BigDecimal(1000)),
				new BigDecimal(6.4185).multiply(new BigDecimal(10).pow(23)));

		locateShip();

	}

	private void locateShip() {
		BigDecimal distanceSunEarth = getDistance(sun, earth);
		BigDecimal diffX = earth.x.subtract(sun.x);
		BigDecimal diffY = earth.y.subtract(sun.y);

		double angle = Math.atan(diffY.divide(diffX, MathContext.DECIMAL32).doubleValue());

		BigDecimal distanceShipSun = distanceSunEarth.add(earth.r).add(new BigDecimal(1500 * 1000));

		BigDecimal y = distanceShipSun.multiply(new BigDecimal(Math.sin(angle)));
		BigDecimal x = distanceShipSun.multiply(new BigDecimal(Math.cos(angle)));

		BigDecimal speed = new BigDecimal(3 * 1000);
		BigDecimal speedX = earth.speedX.add(speed.multiply(new BigDecimal(Math.cos((Math.PI / 2) - angle))));
		BigDecimal speedY = earth.speedY.add(speed.multiply(new BigDecimal(Math.sin((Math.PI / 2) - angle))));

		ship = new Particle(3, new BigDecimal(50), x, y, speedX, speedY,
				new BigDecimal(2).multiply(new BigDecimal(10).pow(5)));

		// ship.prevX = x;
		// ship.prevY = y;
		// ship.prevSpeedX = speedX;
		// ship.prevSpeedY = speedY;

	}

	private BigDecimal getDistance(Particle p, Particle other) {
		return GravityAccelerator.bigSqrt(p.x.subtract(other.x).pow(2).add(p.y.subtract(other.y).pow(2)));
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
			lForEarth.add(solarSystem.mars);
			lForEarth.add(solarSystem.ship);

			List<Particle> lForSun = new ArrayList<Particle>();
			lForSun.add(solarSystem.earth);
			lForSun.add(solarSystem.mars);
			lForSun.add(solarSystem.ship);

			List<Particle> lForMars = new ArrayList<Particle>();
			lForMars.add(solarSystem.earth);
			lForMars.add(solarSystem.sun);
			lForMars.add(solarSystem.ship);

			List<Particle> lForShip = new ArrayList<Particle>();
			lForShip.add(solarSystem.earth);
			lForShip.add(solarSystem.sun);
			lForShip.add(solarSystem.mars);


			boolean isTimeToPrint = Math.abs(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue()
					- Math.round(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue())) < EPSILON;
			if (isTimeToPrint) {
				System.out.println(4);
				System.out.println("t " + Math.round(currentTime.divide(deltaT2, MathContext.DECIMAL32).doubleValue()));
				System.out.println(0 + "\t" + solarSystem.earth.x.doubleValue() + "\t"
						+ solarSystem.earth.y.doubleValue() + "\t" + solarSystem.earth.mass.doubleValue() + "\t"
						+ solarSystem.earth.r.multiply(new BigDecimal("10")) + "\t" + 0 + "\t" + 0 + "\t" + 1);
				System.out.println(1 + "\t" + solarSystem.sun.x.doubleValue() + "\t" + solarSystem.sun.y.doubleValue()
						+ "\t" + solarSystem.sun.mass.doubleValue() + "\t"
						+ solarSystem.sun.r.multiply(new BigDecimal("10")) + "\t" + 1 + "\t" + 1 + "\t" + 0);
				System.out.println(2 + "\t" + solarSystem.mars.x.doubleValue() + "\t" + solarSystem.mars.y.doubleValue()
						+ "\t" + solarSystem.mars.mass.doubleValue() + "\t"
						+ solarSystem.mars.r.multiply(new BigDecimal("10")) + "\t" + 1 + "\t" + 0 + "\t" + 0);
				System.out.println(3 + "\t" + solarSystem.ship.x.doubleValue() + "\t" + solarSystem.ship.y.doubleValue()
						+ "\t" + solarSystem.ship.mass.doubleValue() + "\t"
						+ solarSystem.ship.r.multiply(new BigDecimal("10")) + "\t" + 1 + "\t" + 1 + "\t" + 1);
			}

			Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, lForEarth);
			Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, lForSun);
			Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, lForMars);
			Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, lForShip);

			solarSystem.earth = earthAux;
			solarSystem.mars = marsAux;
			solarSystem.ship = shipAux;
			solarSystem.sun = sunAux;

			currentTime = currentTime.add(deltaT);
		}
	}

	public static void main(String[] args) {

		BigDecimal deltaT = new BigDecimal(3600);
		BigDecimal deltaT2 = new BigDecimal(86400);
		BigDecimal tf = new BigDecimal(31536000);

		Accelerator accelerator = new GravityAccelerator();
		IntegralMethod beeman = new Beeman(deltaT, accelerator);

		SolarSystem solarSystem = new SolarSystem(accelerator, beeman, deltaT);

		List<Particle> lForEarth = new ArrayList<Particle>();
		lForEarth.add(solarSystem.sun);
		lForEarth.add(solarSystem.mars);
		lForEarth.add(solarSystem.ship);

		List<Particle> lForSun = new ArrayList<Particle>();
		lForSun.add(solarSystem.earth);
		lForSun.add(solarSystem.mars);
		lForSun.add(solarSystem.ship);

		List<Particle> lForMars = new ArrayList<Particle>();
		lForMars.add(solarSystem.earth);
		lForMars.add(solarSystem.sun);
		lForMars.add(solarSystem.ship);

		List<Particle> lForShip = new ArrayList<Particle>();
		lForShip.add(solarSystem.earth);
		lForShip.add(solarSystem.sun);
		lForShip.add(solarSystem.mars);

		solarSystem.regressParticle(solarSystem.earth, lForEarth);
		solarSystem.regressParticle(solarSystem.sun, lForSun);
		solarSystem.regressParticle(solarSystem.mars, lForMars);
		solarSystem.regressParticle(solarSystem.ship, lForShip);

		runForOvito(solarSystem, tf, deltaT, deltaT2);

	}

}
