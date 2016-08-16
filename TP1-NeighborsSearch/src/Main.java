import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	private static CellIndexMethod load() {
		
		System.out.println("L M rc N x y r contour:");
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
			System.out.print(p.id + " ");
			for (Particle neighbor : map.get(p)) {
				System.out.print(neighbor.id + " ");
			}
			System.out.println();
		}
	}

	public static void generateOvitoInput() {
		System.out.println("N L R:");
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
		
		System.out.print("[1] To generate particles - [2] To calculate Neighbors: ");
		Scanner s = new Scanner(System.in);
		int option = s.nextInt();
		if (option==1){
			generateOvitoInput();
		}else if(option==2){
			CellIndexMethod cim = load();
			if (cim == null) {
				s.close();
				return;
			}
			Map<Particle, Set<Particle>> neighborsBF = cim.findNeighborsBruteForce();
			prettyPrint(neighborsBF);
		}
		else {
			System.out.println("wrong option");
		}
		s.close();
	}
}
