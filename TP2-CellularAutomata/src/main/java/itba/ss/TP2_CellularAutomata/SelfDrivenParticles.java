package itba.ss.TP2_CellularAutomata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SelfDrivenParticles {

	final double EPSILON = 0.0001;

	Set<MovingParticle> particles;

	double L;
	int N;
	double rc;

	double speed;

	double eta;

	int iterations;

	public SelfDrivenParticles(double L, double rc, int N, int iterations, double eta, double speed) {
		this.L = L;
		this.rc = rc;
		this.N = N;
		this.iterations = iterations;
		this.eta = eta;
		this.speed = speed;
	}

	public void startSimulation() {
		createParticles();
		CellIndexMethod cim = new CellIndexMethod(L, (int) (Math.floor(L / rc) - EPSILON), rc, true);
		printState(0);

		for (int i = 0; i < iterations; i++) {
			this.evolve(cim);
			printState(i + 1);
		}
	}

	private void evolve(CellIndexMethod cim) {
		cim.loadNewData(particles);
		Map<MovingParticle, Set<MovingParticle>> neighbors = cim.findNeighbors();
		Set<MovingParticle> newGeneration = new HashSet<>();
		for (MovingParticle p : particles) {
			double sinSum = Math.sin(p.angle);
			double cosSum = Math.cos(p.angle);
			Set<MovingParticle> set = neighbors.get(p);
			for (MovingParticle n : set) {
				sinSum += Math.sin(n.angle);
				cosSum += Math.cos(n.angle);
			}
			double x = p.x + Math.cos(p.angle) * speed;
			double y = p.y + Math.sin(p.angle) * speed;
			if (x < 0)
				x += L;
			else if (x >= L)
				x -= L;
			if (y < 0)
				y += L;
			else if (y >= L)
				y -= L;
			double aux_angle = Math.atan2(sinSum / (set.size() + 1), cosSum / (set.size() + 1))
					+ (Math.random() * eta - (eta / 2));

			newGeneration.add(new MovingParticle(p.id, speed, aux_angle, x, y));
		}
		particles = newGeneration;
	}

	private void printState(int i) {
		System.out.println(N + 4);
		System.out.println("t" + i);
		for (MovingParticle movingParticle : particles) {
			System.out.println(movingParticle.id + "\t" + movingParticle.x + "\t" + movingParticle.y + "\t"
					+ Math.cos(movingParticle.angle) * speed + "\t" + Math.sin(movingParticle.angle) * speed + "\t"
					+ movingParticle.angle + "\t" + (((255 / (2 * Math.PI)) * movingParticle.angle) + (255 / 2)));
		}
		System.out.println(N + 1 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 2 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 3 + "\t" + 0 + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
		System.out.println(N + 4 + "\t" + L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0);
	}

	private void createParticles() {
		particles = new HashSet<>();
		for (int i = 0; i < N; i++) {
			particles.add(new MovingParticle(i + 1, speed, (Math.random() * 2 * Math.PI) - Math.PI, Math.random() * L,
					Math.random() * L));
		}
	}

	public static void main(String[] args) {
		if (args.length < 6) {
			System.err.println("Must provide L rc N it pert speed");
			return;
		}
		SelfDrivenParticles selfDrivenParticles = new SelfDrivenParticles(Double.valueOf(args[0]),
				Double.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), Double.valueOf(args[4]),
				Double.valueOf(args[5]));
		selfDrivenParticles.startSimulation();
	}

}
