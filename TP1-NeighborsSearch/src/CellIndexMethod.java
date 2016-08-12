import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * Example input:
10
1
0,25
4
1 1 1
1 1 1
1 1 1
1 1 1
1
 */

public class CellIndexMethod {

	Cell[][] matrix;
	Map<Particle, Point> allParticles;
	double L;
	double rc;
	double cellLen;
	int N;
	int M;
	boolean contour;

	private static class Cell {

		List<Particle> list;

		public Cell() {
			list = new ArrayList<>();
		}

	}

	private static class Particle {

		static int count = 1;

		int id;
		double x;
		double y;
		double r;

		public Particle(double x, double y, double r) {
			this.id = count++;
			this.x = x;
			this.y = y;
			this.r = r;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Particle other = (Particle) obj;
			if (id != other.id)
				return false;
			return true;
		}

	}

	private void addNeighbors(Cell c, Particle p, Map<Particle, List<Particle>> m) {
		for (Particle candidate : c.list) {
			if (!candidate.equals(p)) {
				double distance = Math.sqrt(Math.pow(p.x - candidate.x, 2) + Math.pow(p.y - candidate.y, 2)) - p.r
						- candidate.r;
				if (distance <= rc && !m.get(p).contains(candidate)) {
					m.get(p).add(candidate);
					if (!m.containsKey(candidate))
						m.put(candidate, new ArrayList<>());
					m.get(candidate).add(p);
				}
			}
		}
	}

	public Map<Particle, List<Particle>> findNeighborsBruteForce() {
		Map<Particle, List<Particle>> map = new HashMap<>();

		for (Particle particle : allParticles.keySet()) {
			if (!map.containsKey(particle))
				map.put(particle, new ArrayList<>());
			for (Particle particle2 : allParticles.keySet()) {
				if (!particle.equals(particle2) && !map.get(particle).contains(particle2)) {
					double distance = Math
							.sqrt(Math.pow(particle.x - particle2.x, 2) + Math.pow(particle.y - particle2.y, 2))
							- particle.r - particle2.r;
					if (distance <= rc) {
						map.get(particle).add(particle2);
						if (!map.containsKey(particle2))
							map.put(particle2, new ArrayList<>());
						map.get(particle2).add(particle);
					}
				}
			}
		}
		return map;
	}

	public Map<Particle, List<Particle>> findNeighbors() {
		Map<Particle, List<Particle>> map = new HashMap<>();

		for (Particle particle : allParticles.keySet()) {
			if (!map.containsKey(particle))
				map.put(particle, new ArrayList<>());
			Point coords = allParticles.get(particle);
			Cell aux;

			aux = matrix[coords.x][coords.y];
			addNeighbors(aux, particle, map);

			if (coords.x - 1 >= 0) {
				aux = matrix[coords.x - 1][coords.y];
				addNeighbors(aux, particle, map);
			}

			if (coords.x - 1 >= 0 && coords.y + 1 < M) {
				aux = matrix[coords.x - 1][coords.y + 1];
				addNeighbors(aux, particle, map);
			}

			if (coords.y + 1 < M) {
				aux = matrix[coords.x][coords.y + 1];
				addNeighbors(aux, particle, map);
			}

			if (coords.x + 1 < M && coords.y + 1 < M) {
				aux = matrix[coords.x + 1][coords.y + 1];
				addNeighbors(aux, particle, map);
			}

		}

		return map;
	}

	private static CellIndexMethod load() {
		Scanner scanner = new Scanner(System.in);
		CellIndexMethod cim = new CellIndexMethod();

		cim.L = scanner.nextInt();
		cim.M = scanner.nextInt();
		cim.rc = scanner.nextDouble();
		cim.N = scanner.nextInt();
		cim.allParticles = new HashMap<>();

		cim.cellLen = ((double) cim.L) / cim.M;

		cim.matrix = new Cell[cim.M][cim.M];

		double maxRad = 0;

		for (int i = 0; i < cim.N; i++) {
			Particle p = new Particle(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
			int x = (int) (p.x / cim.cellLen);
			int y = (int) (p.y / cim.cellLen);
			if (cim.matrix[x][y] == null)
				cim.matrix[x][y] = new Cell();
			try {
				cim.matrix[x][y].list.add(p);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Invalid Particle, exceeds space.");
				scanner.close();
				return null;
			}
			cim.allParticles.put(p, new Point(x, y));
			if (maxRad < p.r)
				maxRad = p.r;
		}

		if (cim.cellLen <= cim.rc + 2 * maxRad) {
			System.out.println("Invalid Parameter L and M.");
			scanner.close();
			return null;
		}

		cim.contour = scanner.nextInt() != 0;
		scanner.close();

		return cim;
	}

	public static void main(String[] args) {

		CellIndexMethod cim = load();
		if (cim == null) {
			return;
		}

		Map<Particle, List<Particle>> neighbors = cim.findNeighbors();

		for (Particle p : neighbors.keySet()) {
			System.out.println("Particle n: " + p.id);
			for (Particle n : neighbors.get(p)) {
				System.out.println("\tNeighbor: " + n.id);
			}
		}
		
		neighbors = cim.findNeighborsBruteForce();

		for (Particle p : neighbors.keySet()) {
			System.out.println("Particle n: " + p.id);
			for (Particle n : neighbors.get(p)) {
				System.out.println("\tNeighbor: " + n.id);
			}
		}

	}

}