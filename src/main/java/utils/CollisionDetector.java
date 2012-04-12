package utils;

import java.util.ArrayList;

import models.Ball;


/**
 * Utility class to store boundaries and detect collisions
 * of balls between each other and the boundaries
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private Boundary topBoundary;
	private Boundary bottomBoundary;
	private Boundary leftBoundary;
	private Boundary rightBoundary;
	
	private BallUtils ballUtils;
	
	
	public CollisionDetector(double top, double energyLossTop, double bottom, double energyLossBottom,
			double left, double energyLossLeft, double right, double energyLossRight, BallUtils ballUtils) {
		
		this.topBoundary = new Boundary(top, energyLossTop);
		this.bottomBoundary = new Boundary(bottom, energyLossBottom);
		this.leftBoundary = new Boundary(left, energyLossLeft);
		this.rightBoundary = new Boundary(right, energyLossRight);
		this.ballUtils = ballUtils;
	}
	
	public void detectCollisionsAndBoundary(ArrayList<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			
			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ballUtils.contains(ball, otherBall)) {
					ballUtils.collide(ball, otherBall);
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
				if (ballUtils.contains(ball, otherBall)) {
					ballUtils.collide(ball, otherBall);
				}
			}
		}
	}
	
	public void detectBoundary(Ball ball) {
		double radius = ball.getRadius();
		double rightCalibrated = rightBoundary.position - radius;
		double leftCalibrated = leftBoundary.position + radius;
		double topCalibrated = topBoundary.position + radius;
		double bottomCalibrated = bottomBoundary.position - radius;
		
		if (ball.getX() >= rightCalibrated) {
			ballUtils.bounceX(ball, rightCalibrated, rightBoundary.energyLoss);
		}
		if (ball.getX() <= leftCalibrated) {
			ballUtils.bounceX(ball, leftCalibrated, leftBoundary.energyLoss);
		}		
		if (ball.getY() >= bottomCalibrated) {
			ballUtils.bounceY(ball, bottomCalibrated, bottomBoundary.energyLoss);
		}
		if (ball.getY() <= topCalibrated) {
			ballUtils.bounceY(ball, topCalibrated, topBoundary.energyLoss);
		}	
		
		// Check hasnt bounced back past another boundary
		if (ball.getX() > rightCalibrated) ball.setX(rightCalibrated);
		else if (ball.getX() < leftCalibrated) ball.setX(leftCalibrated);
		if (ball.getY() > bottomCalibrated) ball.setY(bottomCalibrated);
		else if (ball.getY() < topCalibrated) ball.setY(topCalibrated);
	}
	
	private class Boundary {
		
		public double position;
		public double energyLoss;
		
		public Boundary(double position, double energyLoss) {
			this.position = position;
			this.energyLoss = energyLoss;
		}
	}

}