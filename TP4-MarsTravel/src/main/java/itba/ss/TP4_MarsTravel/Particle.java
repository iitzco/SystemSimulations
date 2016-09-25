package itba.ss.TP4_MarsTravel;

import java.math.BigDecimal;

public class Particle {
	final int id;
	final BigDecimal r;

	BigDecimal x;
	BigDecimal y;
	BigDecimal prevX;
	BigDecimal prevY;

	BigDecimal speedX;
	BigDecimal speedY;

	BigDecimal prevSpeedX;
	BigDecimal prevSpeedY;

	final BigDecimal mass;

	double[] rListX = new double[6];
	double[] rListY = new double[6];

	public Particle(int id, BigDecimal r, BigDecimal x, BigDecimal y, BigDecimal prevX, BigDecimal prevY,
			BigDecimal speedX, BigDecimal speedY, BigDecimal prevSpeedX, BigDecimal prevSpeedY, BigDecimal mass) {
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

	public Particle(int id, BigDecimal r, BigDecimal x, BigDecimal y, BigDecimal prevX, BigDecimal prevY, BigDecimal speedX, BigDecimal speedY,
			BigDecimal mass) {
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

	public Particle(int id, BigDecimal r, BigDecimal x, BigDecimal y, BigDecimal speedX, BigDecimal speedY, BigDecimal mass) {
		super();
		this.id = id;
		this.r = r;
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.mass = mass;
	}

	public Particle(int id, BigDecimal r, BigDecimal prevX, BigDecimal prevY, BigDecimal mass) {
		super();
		this.id = id;
		this.r = r;
		this.prevX = prevX;
		this.prevY = prevY;
		this.mass = mass;
	}

	@Override
	public String toString() {
		return String.format("[(%f, %f) - (%f, %f) - %f]", x, y, speedX, speedY, r);
	}
}