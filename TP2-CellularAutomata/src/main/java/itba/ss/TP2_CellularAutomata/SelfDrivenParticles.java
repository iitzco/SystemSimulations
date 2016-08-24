package itba.ss.TP2_CellularAutomata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

public class SelfDrivenParticles {

	final double SPEED = 1;
	final double EPSILON = 0.0001;

	Set<MovingParticle> particles;

	double L;
	int N;
	double rc;

	double eta;

	int iterations;

	public SelfDrivenParticles(double L, double rc, int N, int iterations, double eta) {
		this.L = L;
		this.rc = rc;
		this.N = N;
		this.iterations = iterations;
		this.eta = eta;
	}

	public void startSimulation() {
		this.particles = createParticles();
		CellIndexMethod cim = new CellIndexMethod(L, (int) (Math.floor(L / rc) - EPSILON), rc, true);
		printState(0);

		for (int i = 0; i < iterations; i++) {
			this.evolve(cim);
			printState(i + 1);
		}
	}

	private void evolve(CellIndexMethod cim) {
		cim.loadNewData(particles);
		Map<Particle, Set<Particle>> neighbors = cim.findNeighbors();
		Set<MovingParticle> newGeneration = new HashSet<>();
		for (MovingParticle p : particles) {
			double sinSum = Math.sin(p.angle);
			double cosSum = Math.cos(p.angle);
			Set<Particle> set = neighbors.get(p);
			for (Particle n : set) {
				MovingParticle mpn = (MovingParticle) n;
				sinSum += Math.sin(mpn.angle);
				cosSum += Math.cos(mpn.angle);
			}
			double aux_angle = Math.atan2(sinSum / (set.size() + 1), cosSum / (set.size() + 1))
					+ (Math.random() * eta - (eta / 2));
			double x = p.x + Math.cos(aux_angle) * SPEED;
			double y = p.y + Math.sin(aux_angle) * SPEED;
			if (x < 0)
				x += L;
			else if (x > L)
				x -= L;
			if (y < 0)
				y += L;
			else if (y > L)
				y -= L;

			newGeneration.add(new MovingParticle(p.id, SPEED, aux_angle, x, y));
		}
		particles = newGeneration;
	}

	private void printState(int i) {
		System.out.println(N + 4);
		System.out.println("t" + i);
		for (MovingParticle movingParticle : particles) {
			System.out.println(movingParticle.id + "\t" + movingParticle.x + "\t" + movingParticle.y + "\t"
					+ movingParticle.r + "\t" + Math.cos(movingParticle.angle) + "\t"
					+ Math.sin(movingParticle.angle));
		}
		System.out.println(N + 1 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 2 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 3 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 4 + "\t" + L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0);
	}

	private Set<MovingParticle> createParticles() {
		particles = new HashSet<>();
		for (int i = 0; i < N; i++) {
			particles.add(new MovingParticle(i + 1, SPEED, (Math.random() * 2 * Math.PI) - Math.PI, Math.random() * L,
					Math.random() * L));
		}
		return particles;
	}

	public static void main(String[] args) {
		if (args.length < 5) {
			System.err.println("Must provide L rc N it pert");
			return;
		}
		SelfDrivenParticles selfDrivenParticles = new SelfDrivenParticles(Double.valueOf(args[0]),
				Double.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Double.valueOf(args[4]));
		selfDrivenParticles.startSimulation();
	}

}
