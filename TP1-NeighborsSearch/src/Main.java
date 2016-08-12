import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

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
				cim.matrix[x][y].set.add(p);
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

	public static void prettyPrint(Map<Particle, Set<Particle>> map) {
		for (Particle p : map.keySet()) {
			System.out.println("Particle n: " + p.id);
			for (Particle neighbor : map.get(p)) {
				System.out.println("\tNeighbor: " + neighbor.id);
			}
		}
	}

	public static void generateOvitoInput() {
		Scanner scanner = new Scanner(System.in);
		int N = scanner.nextInt();
		double L = scanner.nextDouble();
		double R = scanner.nextDouble();

		System.out.println(N);
		System.out.println("Uniform random particles");

		for (int i = 0; i < N; i++) {
			System.out.println((i + 1) + "\t" + Math.random() * L + "\t" + Math.random() * L + "\t" + Math.random() * R);
		}
		scanner.close();
	}

	public static void main(String[] args) {

		CellIndexMethod cim = load();
		if (cim == null) {
			return;
		}
		Map<Particle, Set<Particle>> neighborsBF = cim.findNeighborsBruteForce();
		prettyPrint(neighborsBF);
//		System.out.println("---------------------------");
//		Map<Particle, Set<Particle>> neighbors = cim.findNeighbors();
//		prettyPrint(neighbors);
		
//		generateOvitoInput();

	}
}
