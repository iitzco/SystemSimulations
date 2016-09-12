package itba.ss.tp3;

import java.util.HashSet;
import java.util.Set;

import org.jscience.physics.amount.Constants;

public class BrownianMovement {
	double L = 0.5;
	double seconds;
	Set<Particle> particles;
	Particle bigParticle;
	boolean interpolate;
	int iteration;

	BrownianMovement(int nParticles, double averageSpeed, double seconds) {
		this.seconds = seconds;
		this.iteration = 0;
		particles = new HashSet<Particle>();

		bigParticle = new Particle(0, 0.05, L / 2, L / 2, 0, 0, 1);
		particles.add(bigParticle);

		for (int i = 0; i < nParticles; i++) {
			Particle p = null;
			do {
				double x = Math.random() * (L - 2 * 0.005) + 0.005;
				double y = Math.random() * (L - 2 * 0.005) + 0.005;
				double speedX = Math.random() * averageSpeed * 2 - averageSpeed;
				double speedY = Math.random() * averageSpeed * 2 - averageSpeed;
				p = new Particle(i + 1, 0.005, x, y, speedX, speedY, 0.1);
			} while (overlap(p));
			particles.add(p);
		}
	}

	private boolean overlap(Particle p) {
		for (Particle particle : particles) {
			if (Math.sqrt(Math.pow(particle.getX() - p.getX(), 2)
					+ Math.pow(particle.getY() - p.getY(), 2)) < (particle.getR() + p.getR()))
				return true;
		}
		return false;
	}

	public void run(int option) {
		double secondsLeft = this.seconds;
		int buckets = 10;
		boolean[] secondsBuckets = new boolean[buckets];

		while (secondsLeft > 0) {
			Crash nextCrash = getNextCrash();
			if (nextCrash.getSeconds() > secondsLeft)
				return;
			if (option == 1) {
				System.out.println(nextCrash.getSeconds());
			}
			jump(nextCrash.getSeconds());
			if (option == 0) {
				printState(iteration);
			}
			if (option == 3) {
				double auxTime = this.seconds - secondsLeft + nextCrash.getSeconds();
				int index = (int) Math.floor((auxTime / this.seconds) * buckets);
				while (index >= 0 && !secondsBuckets[index]) {
					calculateBigParticlePosition(auxTime - (index * this.seconds / buckets));
					secondsBuckets[index] = true;
					index--;
				}
			}
			iteration++;
			crash(nextCrash);
			secondsLeft -= nextCrash.getSeconds();
		}
	}

	private double getTemperature() {
		double ret = 0;
		for (Particle p : particles) {
			double squaredV = Math.pow(p.getSpeedX(), 2) + Math.pow(p.getSpeedY(), 2);
			ret += (squaredV * p.getMass()) / Constants.k.getEstimatedValue();
		}
		return ret;
	}

	private void calculateBigParticlePosition(double d) {
		double x = bigParticle.getX() - (bigParticle.getSpeedX() * d);
		double y = bigParticle.getY() - (bigParticle.getSpeedY() * d);
		double squaredDistance = Math.pow(L / 2 - x, 2) + Math.pow(L / 2 - y, 2);
		System.out.println(squaredDistance);
	}

	private void jump(double deltaSeconds) {
		for (Particle particle : particles) {
			particle.move(deltaSeconds);
		}
	}

	private void printState(int i) {
		int n = particles.size();
		System.out.println(n + 4);
		System.out.println("t" + i);
		for (Particle movingParticle : particles) {
			System.out.println(movingParticle.getId() + "\t" + movingParticle.getX() + "\t" + movingParticle.getY()
					+ "\t" + movingParticle.getR() + "\t" + movingParticle.getMass() + "\t" + movingParticle.getSpeedX()
					+ "\t" + movingParticle.getSpeedY());
		}
		System.out.println(n + 1 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(n + 2 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(n + 3 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(n + 4 + "\t" + L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
	}

	private void crash(Crash crash) {
		if (crash.isWallCrash()) {
			if (crash.isHorizontalWallCrash()) {
				crash.getA().invertSpeedX();
			} else {
				crash.getA().invertSpeedY();
			}
		} else {
			crash.getA().modifySpeedX(crash.getElasticCrashX() / crash.getA().getMass());
			crash.getB().modifySpeedX(-crash.getElasticCrashX() / crash.getB().getMass());
			crash.getA().modifySpeedY(crash.getElasticCrashY() / crash.getA().getMass());
			crash.getB().modifySpeedY(-crash.getElasticCrashY() / crash.getB().getMass());
		}

	}

	private Crash getNextCrash() {
		Crash minCrash = null;

		for (Particle a : particles) {

			double crashVerticalWall = Double.MAX_VALUE;
			double crashHorizontalWall = Double.MAX_VALUE;

			if (a.getSpeedX() > 0) {
				crashHorizontalWall = (this.L - a.getR() - a.getX()) / a.getSpeedX();
			} else if (a.getSpeedX() < 0) {
				crashHorizontalWall = (0 + a.getR() - a.getX()) / a.getSpeedX();
			}

			if (a.getSpeedY() > 0) {
				crashVerticalWall = (this.L - a.getR() - a.getY()) / a.getSpeedY();
			} else if (a.getSpeedY() < 0) {
				crashVerticalWall = (0 + a.getR() - a.getY()) / a.getSpeedY();
			}

			double timeToCrash = Math.min(crashHorizontalWall, crashVerticalWall);

			if (minCrash == null || timeToCrash < minCrash.getSeconds()) {
				minCrash = new Crash(a, timeToCrash,
						crashHorizontalWall < crashVerticalWall ? Wall.HORIZONTAL : Wall.VERTICAL);
			}

			for (Particle b : particles) {
				if (a.getId() >= b.getId()) {
					continue;
				}
				timeToCrash = timeToCrash(a, b);
				if (minCrash == null || timeToCrash < minCrash.getSeconds()) {
					minCrash = new Crash(a, b, timeToCrash);
				}
			}
		}
		return minCrash;
	}

	public static double timeToCrash(Particle a, Particle b) {
		double sigma = a.getR() + b.getR();
		double deltaVX = b.getSpeedX() - a.getSpeedX();
		double deltaVY = b.getSpeedY() - a.getSpeedY();

		double deltaX = b.getX() - a.getX();
		double deltaY = b.getY() - a.getY();

		double vr = deltaVX * deltaX + deltaVY * deltaY;

		if (vr >= 0) {
			return Double.MAX_VALUE;
		}

		double vv = Math.pow(deltaVX, 2) + Math.pow(deltaVY, 2);
		double rr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);

		double d = Math.pow(vr, 2) - vv * (rr - Math.pow(sigma, 2));

		if (d < 0) {
			return Double.MAX_VALUE;
		}

		return -(vr + Math.sqrt(d)) / vv;
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Must provide N[int] avgSpeed[double] seconds[double] option[0|1|2]");
			return;
		}
		try {
			int option = Integer.valueOf(args[3]);
			BrownianMovement brownianMovement = new BrownianMovement(Integer.valueOf(args[0]), Double.valueOf(args[1]),
					Double.valueOf(args[2]));
			System.err.println(brownianMovement.getTemperature());
			brownianMovement.run(option);
		} catch (NumberFormatException e) {
			System.err.println("Must provide N[int] avgSpeed[double] seconds[double]");
		}

	}
}
