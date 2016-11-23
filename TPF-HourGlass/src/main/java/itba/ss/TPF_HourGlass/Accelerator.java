package itba.ss.TPF_HourGlass;

import java.util.Set;

public interface Accelerator {
	
	public double getForceX(Particle p, Set<Particle> set);
	
	public double getForceY(Particle p, Set<Particle> set);

}
