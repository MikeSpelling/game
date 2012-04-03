package mike;

import java.util.ArrayList;


/**
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private double pxToMetres;
	private Boundary topBoundary;
	private Boundary bottomBoundary;
	private Boundary leftBoundary;
	private Boundary rightBoundary;
	
	
	public CollisionDetector(double top, double energyLossTop, double bottom, double energyLossBottom,
			double left, double energyLossLeft, double right, double energyLossRight, double pxToMetres) {
		
		this.pxToMetres = pxToMetres;
		this.topBoundary = new Boundary(top*pxToMetres, energyLossTop);
		this.bottomBoundary = new Boundary(bottom*pxToMetres, energyLossBottom);
		this.leftBoundary = new Boundary(left*pxToMetres, energyLossLeft);
		this.rightBoundary = new Boundary(right*pxToMetres, energyLossRight);
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
		// Detect boundaries and update displacement if hit
		
		// X Boundaries
		if ((ball.getxDisplacement()+(ball.radius*pxToMetres)) >= rightBoundary.location) {
			ball.getMotionX().bounce(rightBoundary.location, rightBoundary.energyLoss);
			// If collided again after bounce, set to be at the boundary
			if ((ball.getxDisplacement()+(ball.radius*pxToMetres)) >= rightBoundary.location) {
				ball.setxDisplacement(rightBoundary.location-(ball.radius*pxToMetres));
			}
		}
		if ((ball.getxDisplacement()-(ball.radius*pxToMetres)) <= leftBoundary.location) {
			ball.getMotionX().bounce(leftBoundary.location, leftBoundary.energyLoss);
			// If collided again after bounce, set to be at the boundary
			if ((ball.getxDisplacement()-(ball.radius*pxToMetres)) <= leftBoundary.location) {
				ball.setxDisplacement(leftBoundary.location+(ball.radius*pxToMetres));
			}
		}
		
		// Y Boundaries
		if ((ball.getyDisplacement()+(ball.radius*pxToMetres)) >= bottomBoundary.location) {
			ball.getMotionY().bounce(bottomBoundary.location, bottomBoundary.energyLoss);
			// If collided again after bounce, set to be at the boundary
			if ((ball.getyDisplacement()+(ball.radius*pxToMetres)) >= bottomBoundary.location) {
				ball.setyDisplacement(bottomBoundary.location-(ball.radius*pxToMetres));
			}
		}
		if ((ball.getyDisplacement()-(ball.radius*pxToMetres)) <= topBoundary.location) {
			ball.getMotionY().bounce(topBoundary.location, topBoundary.energyLoss);
			// If collided again after bounce, set to be at the boundary
			if ((ball.getyDisplacement()-(ball.radius*pxToMetres)) <= topBoundary.location) {
				ball.setyDisplacement(topBoundary.location+(ball.radius*pxToMetres));
			}
		}		
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