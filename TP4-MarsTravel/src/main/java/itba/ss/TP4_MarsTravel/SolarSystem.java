package itba.ss.TP4_MarsTravel;

import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

	final static double YEAR = 3600 * 24 * 365;
	final static double DAY = 3600 * 24;
	final static double HOUR = 3600;
	final static double WEEK = DAY * 7;
	final static double MONTH = 30 * DAY;

	double launchSpeed;

	Accelerator accelerator;
	IntegralMethod integralMethod;

	double deltaT;

	final static double EPSILON = 0.000001;

	Particle sun;
	Particle earth;
	Particle ship;
	Particle mars;
	List<Particle> lForEarth = new ArrayList<Particle>();
	List<Particle> lForSun = new ArrayList<Particle>();
	List<Particle> lForMars = new ArrayList<Particle>();
	List<Particle> lForShip = new ArrayList<Particle>();

	boolean launched = false;
	double arrived = 0;

	public SolarSystem(Accelerator accelerator, IntegralMethod integralMethod, double deltaT) {
		this.accelerator = accelerator;
		this.integralMethod = integralMethod;
		this.deltaT = deltaT;

		initializeCorps();
	}

	private void updateLists() {
		this.lForEarth = new ArrayList<Particle>();
		this.lForEarth.add(sun);
		this.lForEarth.add(mars);

		this.lForSun = new ArrayList<Particle>();
		this.lForSun.add(earth);
		this.lForSun.add(mars);

		this.lForMars = new ArrayList<Particle>();
		this.lForMars.add(earth);
		this.lForMars.add(sun);

		if (launched) {
			this.lForShip = new ArrayList<Particle>();
			this.lForShip.add(earth);
			this.lForShip.add(sun);
			this.lForShip.add(mars);

			this.lForMars.add(ship);

			this.lForSun.add(ship);

			this.lForEarth.add(ship);

		}

	}

	private void initializeCorps() {
		earth = new Particle(0, 6371E3, // radio (m)
				1.391734353396533E11, -0.571059040560652E11, // position (m)
				10.801963811159256E3, 27.565215006898345E3, // speed (m/s)
				5.972E24 // mass (kg)
		);

		sun = new Particle(1, 695700E3, // radio (m)
				0, 0, // position (m)
				0, 0, // speed (m/s)
				1.988E30 // mass (kg)
		);

		mars = new Particle(2, 695700E3, // radio (m)
				0.831483493435295E11, -1.914579540822006E11, // position (m)
				23.637912321314047E3, 11.429021426712032E3, // speed (m/s)
				6.4185E23 // mass (kg)
		);

	}

	private void locateShip() {
		double distanceSunEarth = GravityAccelerator.getDistance(sun, earth);
		double diffX = earth.x - sun.x;
		double diffY = earth.y - sun.y;

		double distanceShipSun = distanceSunEarth + earth.r + 1500 * 1000; // (m)

		double angle = Math.atan2(diffY, diffX);

		double y = distanceShipSun * Math.sin(angle);
		double x = distanceShipSun * Math.cos(angle);

		double speed = (launchSpeed + 7.12) * 1000; // (m/s)
		double speedX = earth.speedX + speed * Math.cos((Math.PI / 2) + angle);
		double speedY = earth.speedY + speed * Math.sin((Math.PI / 2) + angle);

		this.ship = new Particle(3, 50, x, y, speedX, speedY, 2E5);

		regressParticle(ship, lForShip);
	}

	public void regressParticle(Particle p, List<Particle> l) {
		p.prevX = p.x - deltaT * p.speedX + accelerator.getForceX(p, l) * Math.pow(deltaT, 2) / (p.mass * 2);

		p.prevY = p.y - deltaT * p.speedY + accelerator.getForceY(p, l) * Math.pow(deltaT, 2) / (p.mass * 2);

		p.prevSpeedX = p.speedX - accelerator.getForceX(p, l) * deltaT / p.mass;

		p.prevSpeedY = p.speedY - accelerator.getForceY(p, l) * deltaT / p.mass;

	}

	public static void run(SolarSystem solarSystem, double tf, double deltaT, double deltaT2, double launchTime) {

		double currentTime = 0;

		while (currentTime < tf) {
			solarSystem.updateLists();

			if (!solarSystem.launched && Math.abs(currentTime - launchTime) < EPSILON) {
				solarSystem.launched = true;
				solarSystem.locateShip();
			}

			boolean isTimeToPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

			if (isTimeToPrint) {
				System.out.println(solarSystem.launched ? 4 : 3);
				System.out.println("t " + Math.round(currentTime / deltaT2));
				System.out.println(0 + "\t" + solarSystem.earth.x + "\t" + solarSystem.earth.y + "\t"
						+ solarSystem.earth.mass + "\t" + solarSystem.earth.r + "\t" + 0 + "\t" + 0 + "\t" + 1);
				System.out.println(1 + "\t" + solarSystem.sun.x + "\t" + solarSystem.sun.y + "\t" + solarSystem.sun.mass
						+ "\t" + solarSystem.sun.r + "\t" + 1 + "\t" + 1 + "\t" + 0);
				System.out.println(2 + "\t" + solarSystem.mars.x + "\t" + solarSystem.mars.y + "\t"
						+ solarSystem.mars.mass + "\t" + solarSystem.mars.r + "\t" + 1 + "\t" + 0 + "\t" + 0);
				if (solarSystem.launched) {
					System.out.println(3 + "\t" + solarSystem.ship.x + "\t" + solarSystem.ship.y + "\t"
							+ solarSystem.ship.mass + "\t" + solarSystem.ship.r + "\t" + 1 + "\t" + 1 + "\t" + 1);
				}
			}

			Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, solarSystem.lForEarth);
			Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, solarSystem.lForSun);
			Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, solarSystem.lForMars);
			if (solarSystem.launched) {
				Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, solarSystem.lForShip);
				solarSystem.ship = shipAux;
			}

			solarSystem.earth = earthAux;
			solarSystem.mars = marsAux;
			solarSystem.sun = sunAux;

			currentTime += deltaT;
		}
	}

	public static double parseExpression(String s) throws NumberFormatException {
		String[] vec = s.split("-");
		if (vec.length != 2)
			throw new NumberFormatException();

		Double ret = Double.valueOf(vec[0]);
		switch (vec[1].charAt(0)) {
		case 'S':
			ret *= 1;
			break;
		case 'H':
			ret *= HOUR;
			break;
		case 'D':
			ret *= DAY;
			break;
		case 'W':
			ret *= WEEK;
			break;
		case 'M':
			ret *= MONTH;
			break;
		case 'Y':
			ret *= YEAR;
			break;
		default:
			throw new NumberFormatException();
		}
		return ret;
	}

	public static void main(String[] args) {

		int option = 0;
		double deltaT = 0, tf = 0, deltaT2 = 0, launchTime = 0, from = 0, to = 0, step = 0, arrived = 0,
				launchSpeed = 0;

		try {
			option = Integer.valueOf(args[0]);
			deltaT = parseExpression(args[1]);
			tf = parseExpression(args[2]);
			launchSpeed = Double.valueOf(args[3]);
			if (option == 0) {
				if (args.length != 6)
					throw new NumberFormatException();
				deltaT2 = parseExpression(args[4]);
				launchTime = parseExpression(args[5]);
			} else if (option == 1) {
				if (args.length != 8)
					throw new NumberFormatException();
				from = parseExpression(args[4]);
				to = parseExpression(args[5]);
				step = parseExpression(args[6]);
				arrived = Double.valueOf(args[7]);
			}
		} catch (NumberFormatException e2) {
			System.err.println("Wrong parameters");
			return;
		}

		Accelerator accelerator = new GravityAccelerator();
		IntegralMethod beeman = new Beeman(deltaT, accelerator);

		SolarSystem solarSystem = new SolarSystem(accelerator, beeman, deltaT);

		solarSystem.updateLists();

		solarSystem.regressParticle(solarSystem.earth, solarSystem.lForEarth);
		solarSystem.regressParticle(solarSystem.sun, solarSystem.lForSun);
		solarSystem.regressParticle(solarSystem.mars, solarSystem.lForMars);

		if (option == 0) {
			solarSystem.launchSpeed = launchSpeed;
			run(solarSystem, tf, deltaT, deltaT2, launchTime);
		} else if (option == 1) {
			findLaunchTime(deltaT, accelerator, beeman, from, to, tf, step, arrived, launchSpeed);
		}

	}

	private static void findLaunchTime(double deltaT, Accelerator accelerator, IntegralMethod beeman, double from,
			double to, double tf, double step, double arrived, double launchSpeed) {
		for (double i = from; i < to && i < tf; i += step) {
			runSystem(accelerator, beeman, deltaT, tf, i, arrived, launchSpeed);
		}
	}

	private static void runSystem(Accelerator accelerator, IntegralMethod integralMethod, double deltaT, double tf,
			double launchTime, double arrived, double launchSpeed) {
		SolarSystem solarSystem = new SolarSystem(accelerator, integralMethod, deltaT);

		solarSystem.updateLists();

		solarSystem.regressParticle(solarSystem.earth, solarSystem.lForEarth);
		solarSystem.regressParticle(solarSystem.sun, solarSystem.lForSun);
		solarSystem.regressParticle(solarSystem.mars, solarSystem.lForMars);

		solarSystem.launched = false;
		solarSystem.arrived = arrived;
		solarSystem.launchSpeed = launchSpeed;

		runSimulation(solarSystem, deltaT, tf, launchTime);

	}

	private static void runSimulation(SolarSystem solarSystem, double deltaT, double tf, double launchTime) {

		double currentTime = 0;

		double minDistance = Double.MAX_VALUE;
		double minTime = 0;

		while (currentTime < tf) {
			solarSystem.updateLists();

			if (!solarSystem.launched && Math.abs(currentTime - launchTime) < EPSILON) {
				solarSystem.launched = true;
				solarSystem.locateShip();
			}

			Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, solarSystem.lForEarth);
			Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, solarSystem.lForSun);
			Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, solarSystem.lForMars);
			if (solarSystem.launched) {
				Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, solarSystem.lForShip);
				solarSystem.ship = shipAux;
			}

			solarSystem.earth = earthAux;
			solarSystem.mars = marsAux;
			solarSystem.sun = sunAux;

			if (solarSystem.launched) {
				double distance = GravityAccelerator.getDistance(solarSystem.mars, solarSystem.ship);
				if (minDistance > distance) {
					minDistance = distance;
					minTime = currentTime;
				}
				if (solarSystem.arrived()) {
					System.out.println("Arrived! Journey took " + (int) (minTime / DAY) + " days.");
					return;
				}
			}

			currentTime += deltaT;
		}
		System.out.println("Min distance " + minDistance + " meters at " + (int) (minTime / DAY) + " days.");
	}

	private boolean arrived() {
		return GravityAccelerator.getDistance(mars, ship) - mars.r - ship.r < arrived;
	}

}
