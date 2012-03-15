package mike;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Point;

import mike.Motion.Status;


/**
 * Class to maintain the position, shape, motion and color
 * of a ball.
 *
 * @author Mike
 *
 */
public class Ball {

	private final double metresToPx;
	private final MotionX motionX;
	private final MotionY motionY;
	private final CollisionDetector collisionDetectorX;
	private final CollisionDetector collisionDetectorY;
	public int radius;
	public int x;
	public int y;
	public Color color;

	/**
	 * Initialises ball with x, y position, radius, color and
	 * motion in the x and y plane.
	 * 
	 * @param x
	 * @param y
	 * @param initialVelocityX
	 * @param initialVelocityY
	 * @param accelerationX
	 * @param accelerationY
	 * @param radius
	 * @param pxToMetres
	 * @param bounceAudio
	 * @param color
	 * @param collisionDetectorX
	 * @param collisionDetectorY
	 */
	public Ball(int x, int y, double initialVelocityX, double initialVelocityY, double accelerationX, double accelerationY, 
			int radius, double pxToMetres, AudioClip bounceAudio, Color color, 
			CollisionDetector collisionDetectorX, CollisionDetector collisionDetectorY) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.motionX = new MotionX(x, initialVelocityX, accelerationX, pxToMetres, bounceAudio);
		this.motionY = new MotionY(y, initialVelocityY, accelerationY, pxToMetres, bounceAudio);
		this.metresToPx = 1/pxToMetres;
		this.color = color;
		this.collisionDetectorX = collisionDetectorX;
		this.collisionDetectorY = collisionDetectorY;
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}

	public void startMotion() {
		motionX.startMotion();
		motionY.startMotion();
	}

	public void stopMotion() {
		motionX.stopMotion();
		motionY.stopMotion();
	}

	public void applyForce(int clickedX, int clickedY) {
		motionX.applyForce(clickedX-x);
		motionY.applyForce(clickedY-y);
	}

	public void updatePosition() {
		if (isXMoving()) {
			// Update and retrieve displacements
			double xDisplacement = motionX.updateDisplacement();		
			// Detect boundaries and update displacement if hit
			if (collisionDetectorX.detectBoundary(xDisplacement)) {
				xDisplacement = motionX.bounce(collisionDetectorX.getBoundary(), collisionDetectorX.getEnergyLoss());
				// If collided again after bounce, set to be at the boundary
				if (collisionDetectorX.detectBoundary(xDisplacement)) {
					motionX.setDisplacement(collisionDetectorX.getBoundary());
					xDisplacement = motionX.getDisplacement();
				}
			}
			// Convert to px
			this.x = (int)(Math.round(xDisplacement*metresToPx));
		}
		
		if (isYMoving()) {
			double yDisplacement = motionY.updateDisplacement();
			if (collisionDetectorY.detectBoundary(yDisplacement)) {
				yDisplacement = motionY.bounce(collisionDetectorY.getBoundary(), collisionDetectorY.getEnergyLoss());
				if (collisionDetectorY.detectBoundary(yDisplacement)) {
					motionY.setDisplacement(collisionDetectorY.getBoundary());
					yDisplacement = motionY.getDisplacement();
				}
			}
			this.y = (int)(Math.round(yDisplacement*metresToPx));
		}
		
	}

	/**
	 * Calculates if ball contains the ball passed in.
	 *
	 * @param otherBall
	 * @return true if other ball is contained
	 */
	public boolean contains(Ball otherBall) {
		// Calculate length between balls
		double width = Math.abs(this.x - otherBall.x);
		double height = Math.abs(this.y - otherBall.y);
		double length = Math.sqrt((width*width)+(height*height));
		
		// Determine if radius' intersect
		return (radius >= length-otherBall.radius);
	}

	/**
	 * Assuming balls intersect, gets the point of contact
	 * and affects the motion of the ball.
	 *
	 * @param otherBall
	 * @param energyLossCollision
	 */
	public void collide(Ball otherBall, double energyLossCollision) { // TODO - Confirm x, y contact - implement motion.collide
		Point positionHit = getPointOfContact(otherBall);
		int xPosition = positionHit.x;
		int yPosition = positionHit.y;

		motionX.bounce(xPosition, energyLossCollision);
		motionY.bounce(yPosition, energyLossCollision);
		otherBall.motionX.bounce(xPosition, energyLossCollision);
		otherBall.motionY.bounce(yPosition, energyLossCollision);
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
	public Point getPointOfContact(Ball otherBall) {
		int xMidpoint;
		int yMidpoint;

		// Distance between balls
		int xDiff = this.x - otherBall.x;
		int yDiff = this.y - otherBall.y;

		// Calculate angle between balls
		double hypoteneuse = Math.sqrt((Math.abs(xDiff)*Math.abs(xDiff))+(Math.abs(yDiff)*Math.abs(yDiff)));
		if (hypoteneuse == 0) return new Point (this.x, this.y);
		double angle = Math.asin(Math.abs(yDiff)/hypoteneuse);

		// Project x and y lengths using the angle and radius
		double thisXLength = this.radius * Math.cos(angle);
		double thisYLength = this.radius * Math.sin(angle);
		double otherXLength = otherBall.radius * Math.cos(angle);
		double otherYLength = otherBall.radius * Math.sin(angle);

		// Determine whether to add or subtract each length
		if (xDiff > 0) { // This ball to the right
			// Therefore contact to the left of this ball, right of other ball
			int thisXPosition = (int)Math.round(this.x - thisXLength);
			int otherXPosition = (int)Math.round(otherBall.x + otherXLength);
			// Balls may have gone past each other, calculate midpoint of collision
			xMidpoint = thisXPosition + Math.abs(thisXPosition-otherXPosition)/2;
		}
		else { // This ball to the left
			// Therefore contact to the right of this ball, left of other ball
			int thisXPosition = (int)Math.round(this.x + thisXLength);
			int otherXPosition = (int)Math.round(otherBall.x - otherXLength);
			xMidpoint = thisXPosition - Math.abs(thisXPosition-otherXPosition)/2;
		}
		if (yDiff > 0) { // This ball below
			// Therefore contact above this ball, below other ball
			int thisYPosition = (int)Math.round(this.y - thisYLength);
			int otherYPosition = (int)Math.round(otherBall.y + otherYLength);
			yMidpoint = thisYPosition + Math.abs(thisYPosition-otherYPosition)/2;
		}
		else { // This ball above
			// Therefore contact above this ball, below other ball
			int thisYPosition = (int)Math.round(this.y + thisYLength);
			int otherYPosition = (int)Math.round(otherBall.y - otherYLength);
			yMidpoint = thisYPosition - Math.abs(thisYPosition-otherYPosition)/2;
		}

		if (Game.DEBUG) {
			System.out.println("Ball at: " + this.x + ", " + this.y +
					"   Other ball at: " + otherBall.x + ", " + otherBall.y +
					"   Collision at: " + xMidpoint + ", " + yMidpoint);
		}
		return new Point(xMidpoint, yMidpoint); // Return midpoint of collision
	}
	
	public boolean isXMoving() {
		return motionX.status == Status.MOVING;
	}
	
	public boolean isYMoving() {
		return motionY.status == Status.MOVING;
	}
	
	public void setAccelerationX(double acceleration) {
		motionX.setAcceleration(acceleration);
	}
	
	public void setAccelerationY(double acceleration) {
		motionY.setAcceleration(acceleration);
	}

}