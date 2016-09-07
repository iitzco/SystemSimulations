package itba.ss.tp3;

public class Crash {
	final Particle a;
	final Particle b;
	final double seconds;
	Wall wall;
	Double j;

	public Particle getA() {
		return a;
	}

	public Particle getB() {
		return b;
	}

	public double getSeconds() {
		return seconds;
	}

	public boolean isWallCrash() {
		return wall != null;
	}

	public boolean isHorizontalWallCrash() {
		return wall == Wall.HORIZONTAL;
	}

	public boolean isVerticalWallCrash() {
		return wall == Wall.VERTICAL;
	}

	public Crash(Particle a, Particle b, double seconds) {
		this.a = a;
		this.b = b;
		this.seconds = seconds;
		this.wall = null;
		this.j = null;
	}

	public Crash(Particle a, double seconds, Wall wall) {
		this.a = a;
		this.b = null;
		this.seconds = seconds;
		this.wall = wall;
		this.j = null;
	}

	// Returns J
	public double getElasticCrash() {
		if (isWallCrash()) {
			throw new RuntimeException("Wall crash does not apply to J");
		}
		if (j != null)
			return j;
		j = 2 * a.getMass() * b.getMass() * Crash.getVR(a, b);
		double sigma = getSigma(a, b);
		j /= sigma * (a.getMass() + b.getMass());
		return j;
	}

	public double getElasticCrashX() {
		return (getElasticCrash() * getDeltaX(a, b)) / getSigma(a, b);
	}

	public double getElasticCrashY() {
		return (getElasticCrash() * getDeltaY(a, b)) / getSigma(a, b);
	}

	public static double getVR(Particle a, Particle b) {
		double deltaVX = b.getSpeedX() - a.getSpeedX();
		double deltaVY = b.getSpeedY() - a.getSpeedY();

		double deltaX = getDeltaX(a, b);
		double deltaY = getDeltaY(a, b);

		return deltaVX * deltaX + deltaVY * deltaY;
	}

	public static double getDeltaX(Particle a, Particle b) {
		return b.getX() - a.getX();
	}

	public static double getDeltaY(Particle a, Particle b) {
		return b.getY() - a.getY();
	}

	public static double getSigma(Particle a, Particle b) {
		return a.getR() + b.getR();
	}
}
