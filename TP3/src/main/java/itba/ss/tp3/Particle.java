package itba.ss.tp3;

public class Particle {
	private final int id;
	private final double r;
	private double x;
	private double y;
	private double speedX;
	private double speedY;
	private final double mass;

	public Particle(int id, double r, double x, double y, double speedX, double speedY, double mass) {
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.mass = mass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Particle other = (Particle) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	public double getX() {

		return x;
	}

	public double getY() {
		return y;
	}

	public double getSpeedX() {
		return speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public double getMass() {
		return mass;
	}

	public double getR() {
		return this.r;
	}

	public int getId() {
		return this.id;
	}

	public void move(double deltaSeconds) {
		this.x += this.speedX * deltaSeconds;
		this.y += this.speedY * deltaSeconds;
	}

	public void invertSpeedX() {
		this.speedX *= (-1);

	}

	public void invertSpeedY() {
		this.speedY *= (-1);

	}

	public void modifySpeedX(double d) {
		this.speedX += d;
	}

	public void modifySpeedY(double d) {
		this.speedY += d;
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f) - (%f, %f) - %f]", x, y, speedX, speedY, r);
	}
}