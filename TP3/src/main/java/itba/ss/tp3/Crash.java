package itba.ss.tp3;

public class Crash {
    final Particle a;
    final Particle b;
    final double seconds;
    boolean wallCrash;

    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }

    public double getSeconds() {
        return seconds;
    }

    public Crash(Particle a, Particle b, double seconds) {
        this.a = a;
        this.b = b;
        this.seconds = seconds;
        this.wallCrash = false;
    }
    public Crash(Particle a, double seconds) {
        this.a = a;
        this.b = null;
        this.seconds = seconds;
        this.wallCrash = true;
    }
}
