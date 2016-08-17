package itba.ss;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

	private static CellIndexMethod load(int L, int M, double rc, boolean contour, String file)
			throws FileNotFoundException {

		Scanner scanner = new Scanner(new File(file));
		CellIndexMethod cim = new CellIndexMethod(L,M,rc,contour);

		cim.N = scanner.nextInt();
		scanner.nextLine();
		scanner.nextLine();
		cim.allParticles = new HashMap<>();

		cim.cellLen = ((double) cim.L) / cim.M;

		cim.matrix = new Cell[cim.M][cim.M];

		double maxRad = 0;

		for (int i = 0; i < cim.N; i++) {
			Particle p = new Particle(scanner.nextInt(), scanner.nextDouble(), scanner.nextDouble(),
					scanner.nextDouble());
			int x = (int) (p.x / cim.cellLen);
			int y = (int) (p.y / cim.cellLen);
			if (cim.matrix[x][y] == null)
				cim.matrix[x][y] = new Cell();
			try {
				cim.matrix[x][y].set.add(p);
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Invalid Particle, exceeds space.");
				scanner.close();
				return null;
			}
			cim.allParticles.put(p, new Point(x, y));
			if (maxRad < p.r)
				maxRad = p.r;
		}

		if (cim.cellLen <= cim.rc + 2 * maxRad) {
			System.err.println("Invalid Parameter L and M.");
			scanner.close();
			return null;
		}
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

	public static void generateOvitoInput(int N, double L, double R) {
		System.out.println(N);
		System.out.println("Uniform random particles");
		for (int i = 0; i < N; i++) {
			System.out
					.println((i + 1) + "\t" + Math.random() * L + "\t" + Math.random() * L + "\t" + Math.random() * R);
		}
	}

	public static void main(String[] args) {
		String option;

		option = args[0];
		if (option.equals("gen")) {
			try {
				int N = Integer.valueOf(args[1]);
				double L = Double.valueOf(args[2]);
				double R = Double.valueOf(args[3]);
				generateOvitoInput(N, L, R);
			} catch (Exception e) {
				System.err.println("Error in parameters. Must provide: find L M rc contour OR gen N L R");
			}
		} else if (option.equals("find")) {
			try {
				int L = Integer.valueOf(args[1]);
				int M = Integer.valueOf(args[2]);
				double rc = Double.valueOf(args[3]);
				boolean contour = Integer.valueOf(args[4]) != 0;
				String file = args[5];
				CellIndexMethod cim = load(L, M, rc, contour, file);
				if (cim == null) {
					return;
				}
				Map<Particle, Set<Particle>> neighborsBF = cim.findNeighborsBruteForce();
				prettyPrint(neighborsBF);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error in parameters. Must provide: find L M rc contour file OR gen N L R");
			}
		} else {
			System.err.println("Must provide gen or find");
		}
	}
}
