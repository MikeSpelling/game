package mike;

import java.util.ArrayList;


/**
 * 
 * @author Mike
 * 
 * Deals in metres, not px
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
		double radius = ball.getRadiusMetres();
		double rightCalibrated = rightBoundary.location - radius;
		double leftCalibrated = leftBoundary.location + radius;
		double topCalibrated = topBoundary.location + radius;
		double bottomCalibrated = bottomBoundary.location - radius;
		
		if (ball.getxDisplacement() >= rightCalibrated) {
			ball.bounceX(rightCalibrated, rightBoundary.energyLoss);
		}
		if (ball.getxDisplacement() <= leftCalibrated) {
			ball.getMotionX().bounce(leftCalibrated, leftBoundary.energyLoss);
		}		
		if (ball.getyDisplacement() >= bottomCalibrated) {
			ball.getMotionY().bounce(bottomCalibrated, bottomBoundary.energyLoss);
		}
		if (ball.getyDisplacement() <= topCalibrated) {
			ball.getMotionY().bounce(topCalibrated, topBoundary.energyLoss);
		}	
		
		// Check hasnt bounced back past another boundary
		if (ball.getxDisplacement() >= rightCalibrated) ball.setxDisplacement(rightCalibrated);
		else if (ball.getxDisplacement() <= leftCalibrated) ball.setxDisplacement(leftCalibrated);
		if (ball.getyDisplacement() >= bottomCalibrated) ball.setyDisplacement(bottomCalibrated);
		else if (ball.getyDisplacement() <= topCalibrated) ball.setyDisplacement(topCalibrated);
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