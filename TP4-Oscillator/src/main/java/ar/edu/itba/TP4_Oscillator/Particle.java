package ar.edu.itba.TP4_Oscillator;

public class Particle {
	private final int id;
	private final double r;
	private double x;
	private double y;
	private double prevX;
	private double prevY;
	private double speedX;
	private double speedY;
	private final double mass;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		long temp;
		temp = Double.doubleToLongBits(mass);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(prevX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(prevY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(r);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(speedX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(speedY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(mass) != Double.doubleToLongBits(other.mass))
			return false;
		if (Double.doubleToLongBits(prevX) != Double.doubleToLongBits(other.prevX))
			return false;
		if (Double.doubleToLongBits(prevY) != Double.doubleToLongBits(other.prevY))
			return false;
		if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
			return false;
		if (Double.doubleToLongBits(speedX) != Double.doubleToLongBits(other.speedX))
			return false;
		if (Double.doubleToLongBits(speedY) != Double.doubleToLongBits(other.speedY))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
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

	public double getPrevX() {
		return prevX;
	}

	public double getPrevY() {
		return prevY;
	}

	public void setPrevX(double prevX) {
		this.prevX = prevX;
	}

	public void setPrevY(double prevY) {
		this.prevY = prevY;
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f) - (%f, %f) - %f]", x, y, speedX, speedY, r);
	}
}