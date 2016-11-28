package itba.ss.TPF_HourGlass;

public class Particle {
	final int id;
	final double r;

	double x;
	double y;
	double z;

	public boolean isWall = false;

	double prevX;
	double prevY;
	double prevZ;

	double speedX;
	double speedY;
	double speedZ;

	double prevSpeedX;
	double prevSpeedY;
	double prevSpeedZ;

	double prevAccX = 0;
	double prevAccY = 0;
	double prevAccZ = 0;

	Double fX = null;
	Double fY = null;
	Double fZ = null;

	final double mass;

	public Particle(int id, double r, double x, double y, double z, double prevX, double prevY, double prevZ,
			double speedX, double speedY, double speedZ, double prevSpeedX, double prevSpeedY, double prevSpeedZ,
			double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.z = z;
		this.prevX = prevX;
		this.prevY = prevY;
		this.prevZ = prevZ;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
		this.prevSpeedX = prevSpeedX;
		this.prevSpeedY = prevSpeedY;
		this.prevSpeedZ = prevSpeedZ;
		this.mass = mass;
	}

	public Particle(int id, double r, double x, double y, double z, double prevX, double prevY, double prevZ,
			double speedX, double speedY, double speedZ, double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.z = z;
		this.prevX = prevX;
		this.prevY = prevY;
		this.prevZ = prevZ;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
		this.mass = mass;
	}

	public Particle(int id, double r, double x, double y, double z, double speedX, double speedY, double speedZ,
			double mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.z = z;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
		this.mass = mass;
	}

	public Particle(int id, double r, double prevX, double prevY, double prevZ, double mass) {
		super();
		this.id = id;
		this.r = r;
		this.prevX = prevX;
		this.prevY = prevY;
		this.prevZ = prevZ;
		this.mass = mass;
	}

	public Particle(int i, double r, double mass) {
		this.id = i;
		this.r = r;
		this.mass = mass;
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f) - (%f, %f) - %f]", x, y, speedX, speedY, r);
	}

}