package itba.ss.tp3;


import java.util.HashSet;
import java.util.Set;

public class BrownianMovement {
    double L = 0.5;
    double seconds;
    Set<Particle> particles;

    BrownianMovement(int nParticles, double averageSpeed, double seconds) {
        this.seconds = seconds;

        particles = new HashSet<Particle>();

        particles.add(new Particle(0, 0.05, L/2, L/2, 0, 0, 100));

        for (int i = 0; i < nParticles; i++) {
            double x = Math.random() * L;
            double y = Math.random() * L;
            double speedX = Math.random() * averageSpeed * 2 - averageSpeed;
            double speedY = Math.random() * averageSpeed * 2 - averageSpeed;

            particles.add(new Particle(i + 1, 0.005, x, y, speedX, speedY, 0.1));
        }
    }

    public void run() {
        double secondsLeft = this.seconds;

        while (secondsLeft > 0) {
            Crash nextCrash = getNextCrash();
            if (nextCrash.getSeconds() < secondsLeft)
                return;
            jump(nextCrash.getSeconds());
            crash(nextCrash);
        }
    }

    private void jump(double deltaSeconds) {
        for (Particle particle : particles) {
            particle.move(deltaSeconds);
        }
    }

    private void crash(Crash crash) {

    }

    private Crash getNextCrash() {
        Crash minCrash = null;

        for (Particle a : particles) {

            double crashVerticalWall = (this.L - (Math.abs(a.getSpeedX())*a.getR()) - 0) / a.getSpeedX();
            double crashHorizontalWall = (this.L - (Math.abs(a.getSpeedY())*a.getR()) - 0) / a.getSpeedY();
            double timeToCrash = Math.min(crashHorizontalWall, crashVerticalWall);

            if (minCrash == null || timeToCrash < minCrash.getSeconds()){
                minCrash = new Crash(a, timeToCrash);
            }

            for (Particle b : particles) {
                if (a.getId() > b.getId()) {
                    continue;
                }
                timeToCrash = timeToCrash(a, b);
                if (minCrash != null && timeToCrash < minCrash.getSeconds()) {
                    minCrash = new Crash(a, b, timeToCrash);
                }
            }
        }

        return minCrash;
    }

    public static void main(String[] args) {
        return;
    }

    public static double timeToCrash(Particle a, Particle b) {
        double sigma = a.getR() + b.getR();
        double deltaVX = b.getSpeedX() - a.getSpeedX();
        double deltaVY = b.getSpeedY() - a.getSpeedY();

        double deltaX = b.getX() - a.getX();
        double deltaY = b.getY() - a.getY();

        double vr = deltaVX * deltaX + deltaVY * deltaY;


        if (vr >= 0) {
            return Double.MAX_VALUE;
        }

        double vv = Math.pow(deltaVX, 2) + Math.pow(deltaVY, 2);
        double rr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);

        double d = Math.pow(vr, 2) - vv * (rr - Math.pow(sigma, 2));

        if (d < 0) {
            return Double.MAX_VALUE;
        }


        return -(vr + Math.sqrt(d)) / vv;
    }
}

