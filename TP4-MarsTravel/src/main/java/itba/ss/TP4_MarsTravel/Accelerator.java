package itba.ss.TP4_MarsTravel;

import java.util.List;

public interface Accelerator {

	public double getForceX(Particle p, List<Particle> l);

	public double getForceY(Particle p, List<Particle> l);

}
