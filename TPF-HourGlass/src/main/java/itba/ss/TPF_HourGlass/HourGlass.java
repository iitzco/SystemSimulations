package itba.ss.TPF_HourGlass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HourGlass {

	static final double EPSILON = 0.000001;

	static final double MASS = 0.01;
	static final double MAX_TRIES = 10000;

	double R;
	double D;

	double m = 0.01;

	double tf;
	double dt;
	double dt2;

	boolean open;

	int maxParticles;

	List<Particle> particles;

	int N;

	long caudal = 0;

	IntegralMethod integralMethod;

	List<Particle> bounds;

	public HourGlass(double r, double d, double tf, double dT, double dT2, boolean open, int maxParticles,
			IntegralMethod integralMethod) {
		super();
		R = r;
		D = d;

		this.tf = tf;
		this.dt = dT;
		this.dt2 = dT2;

		this.maxParticles = maxParticles;
		this.open = open;

		this.integralMethod = integralMethod;

		locateParticles(maxParticles);
	}

	private void locateParticles(int size) {
		this.particles = new ArrayList<Particle>();

		boolean flag = true;
		int id = 0;

		while (flag && particles.size() < size) {
			double diameter = Math.random() * (this.D / 5 - this.D / 7) + (this.D / 7);
			double r = diameter / 2;
			double x = 0, y = 0, z = 0;
			int tries = 0;
			do {
				x = Math.random() * (this.R * 2 - 2 * r) - R + r;
				y = Math.random() * (this.R * 2 - 2 * r) - R + r;
				z = Math.random() * (this.R - 2 * r) + r;
				tries++;
				if (tries == MAX_TRIES) {
					flag = false;
					break;
				}
			} while (overlap(x, y, z, r) || !insideGlass(x, y, z, r));
			if (flag) {
				Particle p = new Particle(id++, diameter / 2, x, y, z, 0, 0, 0, MASS);
				this.particles.add(p);
			}
		}
		this.N = this.particles.size();
	}

	private boolean overlap(double x, double y, double z, double r) {
		for (Particle p : particles) {
			if (getDistance(p.x, p.y, p.z, x, y, z) < (p.r + r))
				return true;
		}
		return false;
	}

	private boolean insideGlass(double x, double y, double z, double r) {
		return distanceToCenter(x, y, z) < R - r;
	}

	private double distanceToCenter(double x, double y, double z) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z - R, 2));
	}

	private double distanceToCenter(Particle p) {
		return distanceToCenter(p.x, p.y, p.z);
	}

	private double getDistance(double x0, double y0, double z0, double x1, double y1, double z1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2) + Math.pow(z0 - z1, 2));
	}

	private boolean inContact(Particle p1, Particle p2) {
		return getDistance(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z) <= (p1.r + p2.r);
	}

	private double getMagnitude(double x, double y, double z) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	public void run() {
		double currentTime = 0;

		int iteration = 0;
		while (currentTime < tf) {

			// TODO -> cell_index method and then remember to filter neighbors!
			Map<Particle, Set<Particle>> neighbors = findNeighbors();

			List<Particle> nextGen = new ArrayList<>();

			addWallParticles(neighbors);

			for (Particle particle : particles) {
				Particle p = integralMethod.moveParticle(particle, neighbors.get(particle));
				nextGen.add(p);
			}
			if (Math.abs(currentTime / dt2 - Math.round(currentTime / dt2)) < EPSILON) {
				printOvitoState(iteration, neighbors);
			}
			this.particles = nextGen;
			currentTime += dt;
			iteration++;
		}
	}

	private void addWallParticles(Map<Particle, Set<Particle>> neighbors) {
		for (Particle particle : particles) {
			Particle newParticle = getContactParticle(particle);
			if (newParticle != null)
				neighbors.get(particle).add(newParticle);
		}
	}

	private Particle getContactParticle(Particle particle) {
		Particle p = null;

		double d = distanceToCenter(particle);
		if (d > (R - particle.r) && d < (R + particle.r)) {
			double vecX = particle.x;
			double vecY = particle.y;
			double vecZ = particle.z - R;

			double magnitude = getMagnitude(vecX, vecY, vecZ);

			double normVecX = vecX / magnitude;
			double normVecY = vecY / magnitude;
			double normVecZ = vecZ / magnitude;

			double x = normVecX * (R + particle.r);
			double y = normVecY * (R + particle.r);
			double z = normVecZ * (R + particle.r);

			p = new Particle(0, particle.r, particle.mass);
			p.isWall = true;
			p.x = x;
			p.y = y;
			p.z = z + R;
			p.speedX = 0;
			p.speedY = 0;
			p.speedZ = 0;
		}
		return p;

	}

	private Map<Particle, Set<Particle>> findNeighbors() {
		Map<Particle, Set<Particle>> ret = new HashMap<>();
		for (Particle p1 : particles) {
			if (!ret.containsKey(p1))
				ret.put(p1, new HashSet<>());
			for (Particle p2 : particles) {
				if (p1.id != p2.id && inContact(p1, p2)) {
					if (!ret.get(p1).contains(p2)) {
						ret.get(p1).add(p2);
						if (!ret.containsKey(p2))
							ret.put(p2, new HashSet<>());
						ret.get(p2).add(p1);
					}
				}
			}
		}
		return ret;
	}

	public void printOvitoState(int iteration, Map<Particle, Set<Particle>> otherp) {

		createBounds(particles.size());
		System.out.println(particles.size() + bounds.size());
		System.out.println("t " + iteration);

		for (Particle p : particles) {
			System.out.println(p.id + "\t" + p.x + "\t" + p.y + "\t" + p.z + "\t" + p.r + "\t" + p.mass + "\t" + 0);
		}

		for (Particle p : bounds) {
			System.out.println(p.id + "\t" + p.x + "\t" + p.y + "\t" + p.z + "\t" + p.r + "\t" + p.mass + "\t" + 0.5);
		}
	}

	private void createBounds(int size) {
		if (bounds == null) {
			bounds = new ArrayList<>();
			int base = size;
			double r = D / 100.0;
			double percentage = 50.0;
			Particle p = null;
			p = new Particle(base++, r, 0, 0, 0, 0, 0, 0, MASS);
			bounds.add(p);
			for (double i = R / percentage; i < R; i += (R / percentage)) {
				double z = -1 * (Math.sqrt(Math.pow(R, 2) - Math.pow(i, 2))) + R;
				for (double j = -i; j <= i; j += (i / percentage)) {
					double y = Math.sqrt(Math.pow(i, 2) - Math.pow(j, 2));
					p = new Particle(base++, r, j, y, z, 0, 0, 0, MASS);
					bounds.add(p);
					p = new Particle(base++, r, j, -y, z, 0, 0, 0, MASS);
					bounds.add(p);
				}
			}
		}
	}

	public static void main(String[] args) {
		double R = 10;
		double D = 2;

		double tf = 5;
		double deltaT = 0.000001;
		double deltaT2 = 0.02;
		double kn = 1E5;
		double kt = 2 * kn;

		int maxParticles = Integer.MAX_VALUE;

		boolean open = true;
		int option = 0;

		try {
			R = Double.valueOf(args[0]);
			D = Double.valueOf(args[1]);
			deltaT = Double.valueOf(args[2]);
			deltaT2 = Double.valueOf(args[3]);
			tf = Double.valueOf(args[4]);
			kn = Double.valueOf(args[5]);
			kt = Double.valueOf(args[6]);
			if (args.length == 8) {
				maxParticles = Integer.valueOf(args[7]);
			}
		} catch (Exception e) {
			System.err.println("Wrong Parameters. Expect R D deltaT deltaT2 tf kn kt (maxParticles)");
			return;
		}

		Accelerator accelerator = new GranularAccelerator(kn, kt);
		IntegralMethod integralMethod = new Beeman(deltaT, accelerator);

		HourGlass g = new HourGlass(R, D, tf, deltaT, deltaT2, open, maxParticles, integralMethod);
		g.run();

	}
}
