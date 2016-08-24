package itba.ss.TP2_CellularAutomata;

public class MovingParticle extends Particle{
	
	double speed;
	double angle;

	public MovingParticle(int id, double speed, double angle, double x, double y) {
		super(id, x, y, 0);
		this.speed = speed;
		this.angle = angle;
	}
}
