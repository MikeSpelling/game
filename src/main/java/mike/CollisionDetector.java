package mike;

import java.util.List;

public class CollisionDetector {
	
	private Boundary top;
	private Boundary bottom;
	private Boundary left;
	private Boundary right;
	private double energyLossCollision;
	
	public CollisionDetector(int top, double energyLossTop, int bottom, double energyLossBottom, 
			int left, double energyLossLeft, int right, double energyLossRight, double energyLossCollision) {
		
		this.top = new Boundary(top, energyLossTop);
		this.bottom = new Boundary(bottom, energyLossBottom);
		this.left = new Boundary(left, energyLossLeft);
		this.right = new Boundary(right, energyLossRight);
		this.energyLossCollision = energyLossCollision;
	}

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
		int boundaryTop = top.position + ball.radius;
		int boundaryBottom = bottom.position-ball.radius;
		int boundaryLeft = left.position + ball.radius;
		int boundaryRight = right.position - ball.radius;
		
		if (ball.x >= boundaryRight) {
			ball.hitBoundaryX(boundaryRight, right.energyLoss);
		}
		else if (ball.x <= boundaryLeft) {
			ball.hitBoundaryX(boundaryLeft, left.energyLoss);
		}
		if (ball.y >= boundaryBottom) {
			ball.hitBoundaryY(boundaryBottom, bottom.energyLoss);
		}
		else if (ball.y <= boundaryTop) {
			ball.hitBoundaryY(boundaryTop, top.energyLoss);
		}
	}
	
	private class Boundary {
		
		public int position;
		public double energyLoss;
		
		public Boundary(int position, double energyLoss) {
			this.position = position;
			this.energyLoss = energyLoss;
		}
	}

}