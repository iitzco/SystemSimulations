package itba.ss.TP5_GranularEnvironment;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {

	Cell[][] matrix;
	Map<Particle, Point> allParticles;
	double L;
	double rc;
	double cellLen;
	int N;
	int M;
	boolean contour;

	public CellIndexMethod(int L, int M, double rc, boolean contour) {
		this.L = L;
		this.rc = rc;
		this.contour = contour;
		this.M = M;
	}

	private void addNeighbors(Cell c, Particle p, Map<Particle, Set<Particle>> m, int deltaX, int deltaY) {
		for (Particle candidate : c.set) {
			if (!candidate.equals(p)) {
				if (!m.get(p).contains(candidate)) {
					double distance = Math.max(getDistance(p, candidate, deltaX, deltaY), 0);
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
			Point coords = allParticles.get(particle);
			Cell aux;

			aux = matrix[coords.x][coords.y];
			addNeighbors(aux, particle, map, 0, 0);

			aux = matrix[(coords.x - 1 + M) % M][coords.y];
			if (coords.x - 1 >= 0) {
				addNeighbors(aux, particle, map, 0, 0);
			} else if (contour) {
				addNeighbors(aux, particle, map, -1, 0);
			}

			aux = matrix[(coords.x - 1 + M) % M][(coords.y + 1) % M];
			if (coords.x - 1 >= 0 && coords.y + 1 < M) {
				addNeighbors(aux, particle, map, 0, 0);
			} else if (contour) {
				addNeighbors(aux, particle, map, coords.x - 1 >= 0 ? 0 : -1, coords.y + 1 < M ? 0 : 1);
			}

			aux = matrix[coords.x][(coords.y + 1) % M];
			if (coords.y + 1 < M) {
				addNeighbors(aux, particle, map, 0, 0);
			} else if (contour) {
				addNeighbors(aux, particle, map, 0, 1);
			}

			aux = matrix[(coords.x + 1) % M][(coords.y + 1) % M];
			if (coords.x + 1 < M && coords.y + 1 < M) {
				addNeighbors(aux, particle, map, 0, 0);
			} else if (contour) {
				addNeighbors(aux, particle, map, coords.x + 1 < M ? 0 : 1, coords.y + 1 < M ? 0 : 1);
			}

		}

		return map;
	}

	private double getDistance(Particle fixed, Particle moving, int deltaX, int deltaY) {
		return Math
				.sqrt(Math.pow(fixed.x - (moving.x + deltaX * L), 2) + Math.pow(fixed.y - (moving.y + deltaY * L), 2))
				- fixed.r - moving.r;
	}

	public void load(Set<Particle> particles) {

		allParticles = new HashMap<>();

		cellLen = ((double) L) / M;

		matrix = new Cell[M][M];

		double maxRad = 0;

		for (Particle p : particles) {
			int x = (int) (p.x / cellLen);
			int y = (int) (p.y / cellLen);
			if (matrix[x][y] == null)
				matrix[x][y] = new Cell();
			matrix[x][y].set.add(p);
			allParticles.put(p, new Point(x, y));
			if (maxRad < p.r)
				maxRad = p.r;
		}
	}

}