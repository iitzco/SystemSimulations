package itba.ss.TP4_MarsTravel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class GravityAccelerator implements Accelerator {

	// G -> m3/(kg*s2)

	static final BigDecimal G = new BigDecimal(6.693).multiply(new BigDecimal(Math.pow(10, -11)));

	private BigDecimal getForce(Particle p, Particle other) {
		BigDecimal ret = G
				.multiply(p.mass.multiply(other.mass).divide(getDistance(p, other).pow(2), MathContext.DECIMAL32));
		return ret;
	}

	private BigDecimal getDistance(Particle p, Particle other) {
		return bigSqrt(p.x.subtract(other.x).pow(2).add(p.y.subtract(other.y).pow(2)));
	}

	public BigDecimal getForceX(Particle p, List<Particle> l) {
		BigDecimal ret = new BigDecimal(0);
		for (Particle particle : l) {
			ret = ret.subtract(getForce(p, particle)
					.multiply((p.x.subtract(particle.x)).divide(getDistance(p, particle), MathContext.DECIMAL32)));
		}
		return ret;
	}

	public BigDecimal getForceY(Particle p, List<Particle> l) {
		BigDecimal ret = new BigDecimal(0);
		for (Particle particle : l) {
			ret = ret.subtract(getForce(p, particle)
					.multiply((p.y.subtract(particle.y)).divide(getDistance(p, particle), MathContext.DECIMAL32)));
		}
		return ret;
	}

	private static final BigDecimal SQRT_DIG = new BigDecimal(150);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

	private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
		BigDecimal fx = xn.pow(2).add(c.negate());
		BigDecimal fpx = xn.multiply(new BigDecimal(2));
		BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.HALF_DOWN);
		xn1 = xn.add(xn1.negate());
		BigDecimal currentSquare = xn1.pow(2);
		BigDecimal currentPrecision = currentSquare.subtract(c);
		currentPrecision = currentPrecision.abs();
		if (currentPrecision.compareTo(precision) <= -1) {
			return xn1;
		}
		return sqrtNewtonRaphson(c, xn1, precision);
	}

	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	public static BigDecimal bigSqrt(BigDecimal c) {
		return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
	}

}
