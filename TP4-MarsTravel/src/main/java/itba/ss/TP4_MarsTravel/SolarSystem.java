package itba.ss.TP4_MarsTravel;

import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

	Accelerator accelerator;
	IntegralMethod integralMethod;

	double deltaT;

	final static double EPSILON = 0.000001;

	Particle sun;
	Particle earth;
	Particle ship;
	Particle mars;

	public SolarSystem(Accelerator accelerator, IntegralMethod integralMethod, double deltaT) {
		this.accelerator = accelerator;
		this.integralMethod = integralMethod;
		this.deltaT = deltaT;

		initializeCorps();
	}

	private void initializeCorps() {
		earth = new Particle(0,
				6371E3, // radio (m)
				1.391734353396533E11, -0.571059040560652E11, // position (m)
				10.801963811159256E3, 27.565215006898345E3, // speed (m/s)
				5.972E24 // mass (kg)
		);

		sun = new Particle(1,
				695700E3, // radio (m)
				0, 0, // position (m)
				0, 0, // speed (m/s)
				1.988E30 // mass (kg)
		);

		mars = new Particle(2,
				695700E3, // radio (m)
				0.831483493435295E11, -1.914579540822006E11, // position (m)
				23.637912321314047E3, 11.429021426712032E3, // speed (m/s)
				6.4185E23 // mass (kg)
		);


		locateShip();

	}

	private void locateShip() {
		double distanceSunEarth = GravityAccelerator.getDistance(sun, earth);
		double diffX = earth.x - sun.x;
		double diffY = earth.y - sun.y;

		double angle = Math.atan(diffY / diffX);

		double distanceShipSun = distanceSunEarth + earth.r + 1500 * 1000; // (m)

		double y = distanceShipSun * Math.sin(angle);
		double x = distanceShipSun * Math.cos(angle);

		double speed = 3 * 1000; // (m/s)
		double speedX = earth.speedX + speed * Math.cos((Math.PI / 2) - angle);
		double speedY = earth.speedY + speed * Math.sin((Math.PI / 2) - angle);

		ship = new Particle(3,
				50,
				x, y,
				speedX, speedY,
				2E5
		);
	}


	public void regressParticle(Particle p, List<Particle> l) {
		p.prevX = p.x
				- deltaT * p.speedX
				+ accelerator.getForceX(p, l) * Math.pow(deltaT, 2) / ( p.mass * 2 );

		p.prevY = p.y
				- deltaT * p.speedY
				+ accelerator.getForceY(p, l) * Math.pow(deltaT, 2) / ( p.mass * 2 );

        p.prevSpeedX = p.speedX
				- accelerator.getForceX(p, l) * deltaT / p.mass;


		p.prevSpeedY = p.speedY
				- accelerator.getForceY(p, l) * deltaT / p.mass;

	}

	public static void runForOvito(SolarSystem solarSystem, double tf, double deltaT, double deltaT2) {

		double currentTime = 0;

		while (currentTime < tf) {
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

			boolean isTimeToPrint = Math.abs(currentTime/deltaT2 - Math.round(currentTime/deltaT2)) < EPSILON;

			if (isTimeToPrint) {
				System.out.println(4);
				System.out.println("t " + Math.round(currentTime / deltaT2));
				System.out.println(0 + "\t" + solarSystem.earth.x + "\t"
						+ solarSystem.earth.y + "\t" + solarSystem.earth.mass + "\t"
						+ solarSystem.earth.r + "\t" + 0 + "\t" + 0 + "\t" + 1);
				System.out.println(1 + "\t" + solarSystem.sun.x + "\t" + solarSystem.sun.y
						+ "\t" + solarSystem.sun.mass + "\t"
						+ solarSystem.sun.r + "\t" + 1 + "\t" + 1 + "\t" + 0);
				System.out.println(2 + "\t" + solarSystem.mars.x + "\t" + solarSystem.mars.y
						+ "\t" + solarSystem.mars.mass + "\t"
						+ solarSystem.mars.r + "\t" + 1 + "\t" + 0 + "\t" + 0);
				System.out.println(3 + "\t" + solarSystem.ship.x + "\t" + solarSystem.ship.y
						+ "\t" + solarSystem.ship.mass + "\t"
						+ solarSystem.ship.r + "\t" + 1 + "\t" + 1 + "\t" + 1);
			}

			Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, lForEarth);
			Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, lForSun);
			Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, lForMars);
			Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, lForShip);

			solarSystem.earth = earthAux;
			solarSystem.mars = marsAux;
			solarSystem.ship = shipAux;
			solarSystem.sun = sunAux;

			currentTime += deltaT;
		}
	}

	public static void main(String[] args) {
		double deltaT = 360; // 1 hour
		double deltaT2 = 86400; // 1 day
		double tf = 31536000; // 1 year

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
