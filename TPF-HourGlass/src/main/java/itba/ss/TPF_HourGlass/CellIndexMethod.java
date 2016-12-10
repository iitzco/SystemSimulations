package itba.ss.TPF_HourGlass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {

	private static class Point3D {

		int x;
		int y;
		int z;

		public Point3D(int x, int y, int z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}

	}

	Cell[][][] matrix;
	Map<Particle, Point3D> allParticles;
	double L;
	double rc;
	double cellLen;
	int N;
	int M;

	public CellIndexMethod(double L, double rc) {
		this.L = L;
		this.rc = rc;
		this.M = (int) Math.floor(L / rc);
	}

	private void addNeighbors(Cell c, Particle p, Map<Particle, Set<Particle>> m) {
		for (Particle candidate : c.set) {
			if (!candidate.equals(p)) {
				if (!m.get(p).contains(candidate)) {
					double distance = Math.max(getDistance(p, candidate), 0);
					if (distance <= rc) {
						m.get(p).add(candidate);
						if (!m.containsKey(candidate))
							m.put(candidate, new HashSet<Particle>());
						m.get(candidate).add(p);
					}
				}
			}
		}
	}

	public Map<Particle, Set<Particle>> findNeighbors() {
		Map<Particle, Set<Particle>> map = new HashMap<>();

		for (Particle particle : allParticles.keySet()) {
			if (!map.containsKey(particle))
				map.put(particle, new HashSet<Particle>());
			Point3D coords = allParticles.get(particle);
			Cell aux;

			for (int i = -1; i <= 1; i++) {
				if (coords.z + i >= 0 && coords.z + i < M) {
					aux = matrix[coords.x][coords.y][coords.z + i];
					addNeighbors(aux, particle, map);

					aux = matrix[(coords.x - 1 + M) % M][coords.y][coords.z + i];
					if (coords.x - 1 >= 0)
						addNeighbors(aux, particle, map);

					aux = matrix[(coords.x - 1 + M) % M][(coords.y + 1) % M][coords.z + i];
					if (coords.x - 1 >= 0 && coords.y + 1 < M)
						addNeighbors(aux, particle, map);

					aux = matrix[coords.x][(coords.y + 1) % M][coords.z + i];
					if (coords.y + 1 < M)
						addNeighbors(aux, particle, map);

					aux = matrix[(coords.x + 1) % M][(coords.y + 1) % M][coords.z + i];

					if (coords.x + 1 < M && coords.y + 1 < M)
						addNeighbors(aux, particle, map);
				}

			}

		}

		return map;
	}

	private double getDistance(Particle fixed, Particle moving) {
		return Math.sqrt(
				Math.pow(fixed.x - moving.x, 2) + Math.pow(fixed.y - moving.y, 2) + Math.pow(fixed.z - moving.z, 2))
				- fixed.r - moving.r;
	}

	public void load(Set<Particle> particles, double R, double bottom) {

		allParticles = new HashMap<>();

		cellLen = ((double) L) / M;

		matrix = new Cell[M][M][M];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				for (int k = 0; k < matrix.length; k++) {
					matrix[i][j][k] = new Cell();
				}
			}
		}

		double maxRad = 0;

		for (Particle p : particles) {

			int x = (int) ((p.x + R) / cellLen);
			int y = (int) ((p.y + R) / cellLen);
			int z = (int) ((p.z - bottom) / cellLen);
//			System.err.println("X " + x + " Y " + y + " Z " + z);
			matrix[x][y][z].set.add(p);
			allParticles.put(p, new Point3D(x, y, z));
			if (maxRad < p.r)
				maxRad = p.r;

		}
	}

}