package itba.ss.TPF_HourGlass;

public class Particle {
	final int id;
	final double r;

	double x;
	double y;

	public boolean isWall = false;

	double prevX;
	double prevY;

	double speedX;
	double speedY;

	double prevSpeedX;
	double prevSpeedY;

	double prevAccX = 0;
	double prevAccY = 0;

	Double fX = null;
	Double fY = null;

	final double mass;

	double[] rListX = new double[6];
	double[] rListY = new double[6];

	public Particle(int id, double r, double x, double y, double prevX, double prevY, double speedX, double speedY,
			double prevSpeedX, double prevSpeedY, double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.prevX = prevX;
		this.prevY = prevY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.prevSpeedX = prevSpeedX;
		this.prevSpeedY = prevSpeedY;
		this.mass = mass;
	}

	public Particle(int id, double r, double x, double y, double prevX, double prevY, double speedX, double speedY,
			double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.prevX = prevX;
		this.prevY = prevY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.mass = mass;
	}

	public Particle(int id, double r, double x, double y, double speedX, double speedY, double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.mass = mass;
	}

	public Particle(int id, double r, double prevX, double prevY, double mass) {
		super();
		this.id = id;
		this.r = r;
		this.prevX = prevX;
		this.prevY = prevY;
		this.mass = mass;
	}

	public Particle(int i, double r2, double mass) {
		this.id = i;
		this.r = r2;
		this.mass = mass;
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f) - (%f, %f) - %f]", x, y, speedX, speedY, r);
	}
}