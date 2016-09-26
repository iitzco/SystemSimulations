package itba.ss.TP4_MarsTravel;

import java.util.List;

public class GravityAccelerator implements Accelerator {

    // G -> m3/(kg*s2)

    static final double G = 6.693E-11;

    double getForce(Particle p, Particle other) {
        return G * p.mass * other.mass / Math.pow(getDistance(p, other), 2);
    }

    public static double getDistance(Particle p, Particle other) {
        return Math.sqrt(Math.pow(p.x - other.x, 2) + Math.pow(p.y - other.y, 2));
    }

    public double getForceX(Particle p, List<Particle> l) {

        double ret = 0;
        for (Particle particle : l) {
            ret -= getForce(p, particle) * (p.x - particle.x) / getDistance(p, particle);
        }
        return ret;
    }

    public double getForceY(Particle p, List<Particle> l) {
        double ret = 0;
        for (Particle particle : l) {
            ret -= getForce(p, particle) * (p.y - particle.y) / getDistance(p, particle);
        }
        return ret;
    }
}
