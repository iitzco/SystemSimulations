package itba.ss.TP5_GranularEnvironment;

import java.util.Set;

public interface Accelerator {
	
	public double getForceX(Particle p, Set<Particle> set);
	
	public double getForceY(Particle p, Set<Particle> set);

}
