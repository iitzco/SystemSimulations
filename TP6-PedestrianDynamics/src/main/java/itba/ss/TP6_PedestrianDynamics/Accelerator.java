package itba.ss.TP6_PedestrianDynamics;

import java.util.Set;

public interface Accelerator {

	public double getForceX(Particle p, Set<Particle> set);

	public double getForceY(Particle p, Set<Particle> set);

	public void setContext(PedestrianDynamics pd);

}
