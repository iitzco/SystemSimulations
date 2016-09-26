package itba.ss.TP4_MarsTravel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class Beeman implements IntegralMethod {

	private BigDecimal deltaT;
	private Accelerator accelerator;

	public Beeman(BigDecimal deltaT, Accelerator accelerator) {
		super();
		this.deltaT = deltaT;
		this.accelerator = accelerator;
	}

	private void setSpeeds(Particle nextP, Particle p, List<Particle> l) {
		BigDecimal m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, null, null, p.prevSpeedX, p.prevSpeedY, null,
				null, p.mass);
		Particle currP = new Particle(p.id, p.r, nextP.x, nextP.y, null, null, p.speedX, p.speedY, null, null, p.mass);

		nextP.speedX = p.speedX
				.add(new BigDecimal(1.0 / 3)
						.multiply(accelerator.getForceX(currP, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)))
				.add(new BigDecimal(5.0 / 6)
						.multiply(accelerator.getForceX(p, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)))
				.subtract(new BigDecimal(1.0 / 6).multiply(
						accelerator.getForceX(previousP, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)));

		nextP.speedY = p.speedY
				.add(new BigDecimal(1.0 / 3)
						.multiply(accelerator.getForceY(currP, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)))
				.add(new BigDecimal(5.0 / 6)
						.multiply(accelerator.getForceY(p, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)))
				.subtract(new BigDecimal(1.0 / 6).multiply(
						accelerator.getForceY(previousP, l).divide(m, MathContext.DECIMAL32).multiply(deltaT)));

	}

	private void setPositions(Particle nextP, Particle p, List<Particle> l) {
		BigDecimal m = p.mass;

		Particle previousP = new Particle(p.id, p.r, p.prevX, p.prevY, null, null, p.prevSpeedX, p.prevSpeedY, null,
				null, p.mass);

		nextP.x = p.x.add(p.speedX.multiply(deltaT))
				.add(new BigDecimal(2.0 / 3).multiply(accelerator.getForceX(p, l).divide(m, MathContext.DECIMAL32))
						.multiply(deltaT.pow(2)))
				.subtract(new BigDecimal(1.0 / 6)
						.multiply(accelerator.getForceX(previousP, l).divide(m, MathContext.DECIMAL32))
						.multiply(deltaT.pow(2)));

		nextP.y = p.y.add(p.speedY.multiply(deltaT))
				.add(new BigDecimal(2.0 / 3).multiply(accelerator.getForceY(p, l).divide(m, MathContext.DECIMAL32))
						.multiply(deltaT.pow(2)))
				.subtract(new BigDecimal(1.0 / 6)
						.multiply(accelerator.getForceY(previousP, l).divide(m, MathContext.DECIMAL32))
						.multiply(deltaT.pow(2)));
	}

	public String getName() {
		return "Beeman";
	}

	public Particle moveParticle(Particle p, List<Particle> l) {
		Particle nextP = new Particle(p.id, p.r, p.x, p.y, p.mass);
		nextP.prevSpeedX = p.speedX;
		nextP.prevSpeedY = p.speedX;

		setPositions(nextP, p, l);
		setSpeeds(nextP, p, l);
		return nextP;
	}

}
