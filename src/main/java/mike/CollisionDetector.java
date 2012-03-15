package mike;

import java.util.List;


/**
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private Boundary top;
	private Boundary bottom;
	private Boundary left;
	private Boundary right;
	private double energyLossCollision;
	private double pxToMetres;
	
	
	/**
	 * 
	 * @param top
	 * @param energyLossTop
	 * @param bottom
	 * @param energyLossBottom
	 * @param left
	 * @param energyLossLeft
	 * @param right
	 * @param energyLossRight
	 * @param energyLossCollision
	 * @param pxToMetres 
	 */
	public CollisionDetector(int top, double energyLossTop, int bottom, double energyLossBottom, 
			int left, double energyLossLeft, int right, double energyLossRight, double energyLossCollision, double pxToMetres) {
		
		this.top = new Boundary(top*pxToMetres, energyLossTop);
		this.bottom = new Boundary(bottom*pxToMetres, energyLossBottom);
		this.left = new Boundary(left*pxToMetres, energyLossLeft);
		this.right = new Boundary(right*pxToMetres, energyLossRight);
		this.energyLossCollision = energyLossCollision;
		this.pxToMetres = pxToMetres;
	}

	/**
	 * Detects if any ball currently in a position which
	 * collides with another ball or a boundary.
	 * Does not take into account the path of the ball
	 * which may have crossed another, if both were moving fast
	 * enough.
	 * 
	 * @param balls
	 */
	public void detectCollisions(List<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			// Detect walls
			detectBoundary(ball);
			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ball.contains(otherBall)) {
					ball.collide(otherBall, energyLossCollision);
				}
			}
		}		
	}
	
	private void detectBoundary(Ball ball) {		
		// Take radius into account
		double boundaryTop = top.distance + (ball.radius*pxToMetres);
		double boundaryBottom = bottom.distance - (ball.radius*pxToMetres);
		double boundaryLeft = left.distance + (ball.radius*pxToMetres);
		double boundaryRight = right.distance - (ball.radius*pxToMetres);
		
		if (ball.x*pxToMetres >= boundaryRight) {
			ball.hitBoundaryX(boundaryRight, right.energyLoss);
		}
		else if (ball.x*pxToMetres <= boundaryLeft) {
			ball.hitBoundaryX(boundaryLeft, left.energyLoss);
		}
		if (ball.y*pxToMetres >= boundaryBottom) {
			ball.hitBoundaryY(boundaryBottom, bottom.energyLoss);
		}
		else if (ball.y*pxToMetres <= boundaryTop) {
			ball.hitBoundaryY(boundaryTop, top.energyLoss);
		}
	}
	
	private class Boundary {
		
		public double distance;
		public double energyLoss;
		
		public Boundary(double position, double energyLoss) {
			this.distance = position;
			this.energyLoss = energyLoss;
		}
	}

}