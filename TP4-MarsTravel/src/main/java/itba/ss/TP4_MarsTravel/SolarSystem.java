package itba.ss.TP4_MarsTravel;

import java.util.ArrayList;
import java.util.List;

public class SolarSystem {

    final static double YEAR = 3600*24*365;
    final static double DAY = 3600*24;
    final static double WEEK = DAY*7;

    final static double LAUNCH_SPEED = 8;

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

	public SolarSystem(Accelerator accelerator, IntegralMethod integralMethod, double deltaT) {
		this.accelerator = accelerator;
		this.integralMethod = integralMethod;
		this.deltaT = deltaT;

		initializeCorps();
	}

	private void updateLists(){
        this.lForEarth = new ArrayList<Particle>();
        this.lForEarth.add(sun);
        this.lForEarth.add(mars);

        this.lForSun = new ArrayList<Particle>();
        this.lForSun.add(earth);
        this.lForSun.add(mars);

        this.lForMars = new ArrayList<Particle>();
        this.lForMars.add(earth);
        this.lForMars.add(sun);

        if (launched){
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

		double speed = (LAUNCH_SPEED + 7.12) * 1000; // (m/s)
		double speedX = earth.speedX + speed * Math.cos((Math.PI / 2) + angle);
		double speedY = earth.speedY + speed * Math.sin((Math.PI / 2) + angle);

		this.ship = new Particle(3,
				50,
				x, y,
				speedX, speedY,
				2E5
		);

        regressParticle(ship, lForShip);
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
                System.out.println(0 + "\t" + solarSystem.earth.x + "\t"
                        + solarSystem.earth.y + "\t" + solarSystem.earth.mass + "\t"
                        + solarSystem.earth.r + "\t" + 0 + "\t" + 0 + "\t" + 1);
                System.out.println(1 + "\t" + solarSystem.sun.x + "\t" + solarSystem.sun.y
                        + "\t" + solarSystem.sun.mass + "\t"
                        + solarSystem.sun.r + "\t" + 1 + "\t" + 1 + "\t" + 0);
                System.out.println(2 + "\t" + solarSystem.mars.x + "\t" + solarSystem.mars.y
                        + "\t" + solarSystem.mars.mass + "\t"
                        + solarSystem.mars.r + "\t" + 1 + "\t" + 0 + "\t" + 0);
                if (solarSystem.launched) {
                    System.out.println(3 + "\t" + solarSystem.ship.x + "\t" + solarSystem.ship.y
                            + "\t" + solarSystem.ship.mass + "\t"
                            + solarSystem.ship.r + "\t" + 1 + "\t" + 1 + "\t" + 1);
                }
            }


            Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, solarSystem.lForEarth);
            Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, solarSystem.lForSun);
            Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, solarSystem.lForMars);
            if (solarSystem.launched){
                Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, solarSystem.lForShip);
                solarSystem.ship = shipAux;
            }

			solarSystem.earth = earthAux;
			solarSystem.mars = marsAux;
			solarSystem.sun = sunAux;

			currentTime += deltaT;
		}
	}

	public static void main(String[] args) {
		double deltaT = 360; // 1 hour
		double deltaT2 = 86400; // 1 day
		double tf = 10*31536000; // 1 year

        double launchTime = 31536000;

        int option = 1;
		Accelerator accelerator = new GravityAccelerator();
		IntegralMethod beeman = new Beeman(deltaT, accelerator);

		SolarSystem solarSystem = new SolarSystem(accelerator, beeman, deltaT);

        solarSystem.updateLists();

		solarSystem.regressParticle(solarSystem.earth, solarSystem.lForEarth);
		solarSystem.regressParticle(solarSystem.sun, solarSystem.lForSun);
		solarSystem.regressParticle(solarSystem.mars, solarSystem.lForMars);

        if (option==0) {
            run(solarSystem, tf, deltaT, deltaT2, launchTime);
        } else if (option == 1){
            double t = findLaunchTime(solarSystem, deltaT, accelerator, beeman);
            System.out.println(t);
        }

	}

    private static double findLaunchTime(SolarSystem solarSystem, double deltaT, Accelerator accelerator, IntegralMethod beeman) {
        double ret = 0;
        for (int i=0; i<3*YEAR; i+=WEEK){
            ret = runSystem(accelerator, beeman, deltaT, 3*YEAR, i);
            System.out.println(i/DAY);
            System.out.println(ret);
            if (ret != -1){
                System.out.println("Arrived at "+ret);
                return i;
            }
        }
        return -1;
    }

    private static double runSystem(Accelerator accelerator, IntegralMethod integralMethod, double deltaT, double tf, double launchTime){
        SolarSystem solarSystem = new SolarSystem(accelerator, integralMethod, deltaT);

        solarSystem.updateLists();

        solarSystem.regressParticle(solarSystem.earth, solarSystem.lForEarth);
        solarSystem.regressParticle(solarSystem.sun, solarSystem.lForSun);
        solarSystem.regressParticle(solarSystem.mars, solarSystem.lForMars);

        return runSimulation(solarSystem, deltaT, tf, launchTime);

    }

    private static double runSimulation(SolarSystem solarSystem, double deltaT, double tf, double launchTime) {

        double currentTime = 0;

        while (currentTime < tf) {
            solarSystem.updateLists();

            if (!solarSystem.launched && Math.abs(currentTime - launchTime) < EPSILON) {
                solarSystem.launched = true;
                solarSystem.locateShip();
            }

            Particle earthAux = solarSystem.integralMethod.moveParticle(solarSystem.earth, solarSystem.lForEarth);
            Particle sunAux = solarSystem.integralMethod.moveParticle(solarSystem.sun, solarSystem.lForSun);
            Particle marsAux = solarSystem.integralMethod.moveParticle(solarSystem.mars, solarSystem.lForMars);
            if (solarSystem.launched){
                Particle shipAux = solarSystem.integralMethod.moveParticle(solarSystem.ship, solarSystem.lForShip);
                solarSystem.ship = shipAux;
            }

            solarSystem.earth = earthAux;
            solarSystem.mars = marsAux;
            solarSystem.sun = sunAux;

            if (solarSystem.arrived()){
                return currentTime;
            }

            currentTime += deltaT;
        }
        return -1;
    }

    private boolean arrived() {
        return GravityAccelerator.getDistance(mars, ship) - mars.r - ship.r < 1000000E3;
    }

}
