package mike;

import java.util.ArrayList;


/**
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private Boundary topBoundary;
	private Boundary bottomBoundary;
	private Boundary leftBoundary;
	private Boundary rightBoundary;
	
	
	public CollisionDetector(double top, double energyLossTop, double bottom, double energyLossBottom,
			double left, double energyLossLeft, double right, double energyLossRight) {
		
		this.topBoundary = new Boundary(top, energyLossTop);
		this.bottomBoundary = new Boundary(bottom, energyLossBottom);
		this.leftBoundary = new Boundary(left, energyLossLeft);
		this.rightBoundary = new Boundary(right, energyLossRight);
	}
	
	public void detectCollisionsAndBoundary(ArrayList<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			
			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ball.contains(otherBall)) {
					ball.collide(otherBall);
				}
			}
			detectBoundary(ball);
		}
	}

	public void detectCollisions(ArrayList<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			
			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ball.contains(otherBall)) {
					ball.collide(otherBall);
				}
			}
		}
	}
	
	public void detectBoundary(Ball ball) {
		double radius = ball.getRadius();
		double rightCalibrated = rightBoundary.location - radius;
		double leftCalibrated = leftBoundary.location + radius;
		double topCalibrated = topBoundary.location + radius;
		double bottomCalibrated = bottomBoundary.location - radius;
		
		if (ball.getX() >= rightCalibrated) {
			ball.bounceX(rightCalibrated, rightBoundary.energyLoss);
		}
		if (ball.getX() <= leftCalibrated) {
			ball.bounceX(leftCalibrated, leftBoundary.energyLoss);
		}		
		if (ball.getY() >= bottomCalibrated) {
			ball.bounceY(bottomCalibrated, bottomBoundary.energyLoss);
		}
		if (ball.getY() <= topCalibrated) {
			ball.bounceY(topCalibrated, topBoundary.energyLoss);
		}	
		
		// Check hasnt bounced back past another boundary
		if (ball.getX() > rightCalibrated) ball.setX(rightCalibrated);
		else if (ball.getX() < leftCalibrated) ball.setX(leftCalibrated);
		if (ball.getY() > bottomCalibrated) ball.setY(bottomCalibrated);
		else if (ball.getY() < topCalibrated) ball.setY(topCalibrated);
	}
	
	private class Boundary {
		
		public double location;
		public double energyLoss;
		
		public Boundary(double position, double energyLoss) {
			this.location = position;
			this.energyLoss = energyLoss;
		}
	}

}