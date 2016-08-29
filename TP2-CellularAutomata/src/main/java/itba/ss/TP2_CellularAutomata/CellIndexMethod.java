package itba.ss.TP2_CellularAutomata;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {

	Cell[][] matrix;
	Map<MovingParticle, Point> allParticles;
	double L;
	double rc;
	double cellLen;
	int N;
	int M;
	boolean contour;

	public CellIndexMethod(double L, int M, double rc, boolean contour) {
		this.L = L;
		this.rc = rc;
		this.contour = contour;
		this.M = M;
		this.cellLen = ((double) this.L) / this.M;
		this.matrix = new Cell[this.M][this.M];
	}

	public void loadNewData(Set<MovingParticle> l) {
		this.allParticles = new HashMap<>();
		double maxRad = 0;

		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				matrix[i][j] = new Cell();
			}
		}

		for (MovingParticle p : l) {
			int x = (int) (p.x / this.cellLen);
			int y = (int) (p.y / this.cellLen);
			this.matrix[x][y].set.add(p);
			this.allParticles.put(p, new Point(x, y));
			if (maxRad < p.r)
				maxRad = p.r;
		}
	}

	private void addNeighbors(Cell c, MovingParticle p, Map<MovingParticle, Set<MovingParticle>> m, int deltaX,
			int deltaY) {
		for (MovingParticle candidate : c.set) {
			if (!candidate.equals(p)) {
				if (!m.get(p).contains(candidate)) {
					double distance = Math.max(getDistance(p, candidate, deltaX, deltaY), 0);
					if (distance <= rc) {
						m.get(p).add(candidate);
						if (!m.containsKey(candidate))
							m.put(candidate, new HashSet<>());
						m.get(candidate).add(p);
					}
				}
			}
		}
	}

	public Map<MovingParticle, Set<MovingParticle>> findNeighbors() {
		Map<MovingParticle, Set<MovingParticle>> map = new HashMap<>();

		for (MovingParticle particle : allParticles.keySet()) {
			if (!map.containsKey(particle))
				map.put(particle, new HashSet<>());
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

	private double getDistance(MovingParticle fixed, MovingParticle moving, int deltaX, int deltaY) {
		return Math
				.sqrt(Math.pow(fixed.x - (moving.x + deltaX * L), 2) + Math.pow(fixed.y - (moving.y + deltaY * L), 2))
				- fixed.r - moving.r;
	}

}