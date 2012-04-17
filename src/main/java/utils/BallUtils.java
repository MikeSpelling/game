package utils;

import game.Game;

import java.awt.Graphics;
import java.awt.geom.Point2D;

import models.Ball;
import models.Motion.Status;


/**
 * Class to perform a number of operations
 * on Ball objects.
 * 
 * @author Mike
 *
 */
public class BallUtils {
	
	private MotionUtils motionUtils;
	
	
	public BallUtils(MotionUtils motionUtils) {
		this.motionUtils = motionUtils;
	}
	
	
	public void startMotion(Ball ball) {
		ball.getMotionX().setAcceleration(ball.getMotionX().getInitialAcceleration());
		ball.getMotionX().setStatus(Status.MOVING);
		ball.getMotionX().setTimeLastUpdated(System.nanoTime());
		
		ball.getMotionY().setAcceleration(ball.getMotionY().getInitialAcceleration());
		ball.getMotionY().setStatus(Status.MOVING);
		ball.getMotionY().setTimeLastUpdated(System.nanoTime());
	}
	
	public void stopMotion(Ball ball) {
		ball.getMotionX().setAcceleration(0);
		ball.getMotionX().setVelocity(0);
		ball.getMotionX().setStatus(Status.STOPPED);
		
		ball.getMotionY().setAcceleration(0);
		ball.getMotionY().setVelocity(0);
		ball.getMotionY().setStatus(Status.STOPPED);
	}

	public void updatePosition(Ball ball) {
		// Update and retrieve displacements if moving
		if (ball.getMotionX().isMoving())
			ball.setX(motionUtils.updateDisplacement(ball.getMotionX(), ball.getX()));
		if (ball.getMotionY().isMoving())
			ball.setY(motionUtils.updateDisplacement(ball.getMotionY(), ball.getY()));
	}

	public boolean overlap(Ball ball1, Ball ball2) {
		// Calculate length between balls
		double width = Math.abs(ball1.getX() - ball2.getX());
		double height = Math.abs(ball1.getY() - ball2.getY());
		double length = Math.sqrt((width*width)+(height*height));

		// Determine if radius' intersect
		return (ball1.getRadius() >= length-ball2.getRadius());
	}

	/**
	 * Assuming balls intersect, gets the point of contact
	 * and affects the motion of the ball.
	 *
	 */
	public void collide(Ball ball1, Ball ball2) {
		Point2D positionHit = getPointOfContact(ball1, ball2);
		double xPositionHit = positionHit.getX();
		double yPositionHit = positionHit.getY();
		
		double combinedEnergyLoss = ball1.getEnergyLoss() * ball2.getEnergyLoss();
		
		motionUtils.collide(ball1.getMotionX(), ball1.getMass(), ball1.getRadius(), 
				ball2.getMotionX(), ball2.getMass(), ball2.getRadius(), combinedEnergyLoss, xPositionHit);
		motionUtils.collide(ball1.getMotionY(), ball1.getMass(), ball1.getRadius(), 
				ball2.getMotionY(), ball2.getMass(), ball2.getRadius(), combinedEnergyLoss, yPositionHit);
	}

	/**
	 * Calculates the point of collision between 2 balls.
	 * This assumes contains(ball) has been called to check balls intersect,
	 * random results will occur if there is no point of contact.
	 * In the case of overlapping boundaries the midpoint is returned.
	 *
	 * @param otherBall
	 * @return
	 */
	public Point2D getPointOfContact(Ball ball1, Ball ball2) {
		double xMidpoint;
		double yMidpoint;

		// Distance between balls
		double xDiff = ball1.getX() - ball2.getX();
		double yDiff = ball1.getY() - ball2.getY();

		// Calculate angle between balls
		double hypoteneuse = Math.sqrt((Math.abs(xDiff)*Math.abs(xDiff))+(Math.abs(yDiff)*Math.abs(yDiff)));
		if (hypoteneuse == 0) return new Point2D.Double(ball1.getX(), ball1.getY());
		double angle = Math.asin(Math.abs(yDiff)/hypoteneuse);

		// Project x and y lengths using the angle and radius
		double thisXLength = ball1.getRadius() * Math.cos(angle);
		double thisYLength = ball1.getRadius() * Math.sin(angle);
		double otherXLength = ball2.getRadius() * Math.cos(angle);
		double otherYLength = ball2.getRadius() * Math.sin(angle);

		// Determine whether to add or subtract each length
		if (xDiff == 0) xMidpoint = ball1.getX();
		else if (xDiff > 0) { // This ball to the right
			// Therefore contact to the left of this ball, right of other ball
			double position1 = ball1.getX() - thisXLength;
			double position2 = ball2.getX() + otherXLength;
			// Balls may have gone past each other, calculate midpoint of collision
			xMidpoint = position1 + Math.abs(position1-position2)/2;
		}
		else { // This ball to the left
			// Therefore contact to the right of this ball, left of other ball
			double position1 = ball1.getX() + thisXLength;
			double position2 = ball2.getX() - otherXLength;
			xMidpoint = position1 - Math.abs(position1-position2)/2;
		}
		
		if (yDiff == 0) yMidpoint = ball1.getY();
		else if (yDiff > 0) { // This ball below
			// Therefore contact above this ball, below other ball
			double position1 = ball1.getY() - thisYLength;
			double position2 = ball2.getY() + otherYLength;
			yMidpoint = position1 + Math.abs(position1-position2)/2;
		}
		else { // This ball above
			// Therefore contact above this ball, below other ball
			double position1 = ball1.getY() + thisYLength;
			double position2 = ball2.getY() - otherYLength;
			yMidpoint = position1 - Math.abs(position1-position2)/2;
		}

		if (Game.DEBUG) {
			System.out.println("Ball at: " + ball1.getX() + ", " + ball1.getY() +
					"   Other ball at: " + ball2.getX() + ", " + ball2.getY() +
					"   Collision at: " + xMidpoint + ", " + yMidpoint);
		}
		return new Point2D.Double(xMidpoint, yMidpoint); // Return midpoint of collision
	}
	
	public void bounceX(Ball ball, double boundary, double energyLoss) {
		ball.setX(motionUtils.bounce(ball.getMotionX(), ball.getX(), boundary, energyLoss));
	}
	
	public void bounceY(Ball ball, double boundary, double energyLoss) {
		ball.setY(motionUtils.bounce(ball.getMotionY(), ball.getY(), boundary, energyLoss));
	}
	
	public void paintBall(Ball ball, Graphics buffer, double scale) {
		buffer.setColor(ball.getColor());
		buffer.fillOval ((int)Math.round((ball.getX() - ball.getRadius())*scale), (int)Math.round((ball.getY() - ball.getRadius())*scale),
				(int)Math.round((2 * ball.getRadius())*scale), (int)Math.round((2 * ball.getRadius())*scale));
	}

	public void applyAccelerationTowards(Ball ball, Point2D midpoint) {
		double xLen = Math.abs(ball.getX() - midpoint.getX());
		double yLen = Math.abs(ball.getY() - midpoint.getY());
		double angle = Math.atan(yLen/xLen);
		double xAcc = Game.ACCELERATION_DUE_TO_GRAVITY * Math.cos(angle);
		double yAcc = Game.ACCELERATION_DUE_TO_GRAVITY * Math.sin(angle);
		if (ball.getX() > midpoint.getX())
			ball.getMotionX().setAcceleration(-xAcc);
		else
			ball.getMotionX().setAcceleration(xAcc);
		if (ball.getY() > midpoint.getY())
			ball.getMotionY().setAcceleration(-yAcc);
		else
			ball.getMotionY().setAcceleration(yAcc);
	}

}