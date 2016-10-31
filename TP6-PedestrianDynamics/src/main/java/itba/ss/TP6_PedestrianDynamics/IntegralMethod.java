package itba.ss.TP6_PedestrianDynamics;

import java.util.Set;

public interface IntegralMethod {

	public Particle moveParticle(Particle p, Set<Particle> set);

	public String getName();

	public Accelerator getAccelerator();

}
