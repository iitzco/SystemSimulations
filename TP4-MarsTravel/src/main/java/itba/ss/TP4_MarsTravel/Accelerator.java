package itba.ss.TP4_MarsTravel;

import java.math.BigDecimal;
import java.util.List;

public interface Accelerator {

	public BigDecimal getForceX(Particle p, List<Particle> l);

	public BigDecimal getForceY(Particle p, List<Particle> l);

}
