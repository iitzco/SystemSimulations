package itba.ss.TP5_GranularEnvironment;

import java.util.Set;

public interface IntegralMethod {

	public Particle moveParticle(Particle p, Set<Particle> set);

	public String getName();

}
