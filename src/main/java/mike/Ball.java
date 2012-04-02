package mike;

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
	private final double pxToMetres;
	private final MotionX motionX;
	private final MotionY motionY;
	private final CollisionDetector collisionDetectorX;
	private final CollisionDetector collisionDetectorY;
	public double mass;
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
	public Ball(int x, int y, int radius, double mass, Color color,
			MotionX motionX, MotionY motionY, CollisionDetector collisionDetectorX, CollisionDetector collisionDetectorY) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.motionX = motionX;
		this.motionY = motionY;
		this.pxToMetres = motionX.getPxToMetres();
		this.metresToPx = 1/pxToMetres;
		this.color = color;
		this.collisionDetectorX = collisionDetectorX;
		this.collisionDetectorY = collisionDetectorY;
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}

	public void updatePosition() {
		// Get current displacements
		double newXDisplacement = motionX.getDisplacement();
		double newYDisplacement = motionY.getDisplacement();
		
		// Update and retrieve displacements if moving
		if (isXMoving()) {
			newXDisplacement = motionX.updateDisplacement();
		}
		if (isYMoving()) {
			newYDisplacement = motionY.updateDisplacement();
		}
		
		// Detect boundaries and update displacement if hit
		if (collisionDetectorX.hasHitBoundary(newXDisplacement)) {
			collisionDetectorX.setTarget(newXDisplacement);
			newXDisplacement = motionX.bounce(collisionDetectorX.getBoundary(), collisionDetectorX.getEnergyLoss());
			// If collided again after bounce, set to be at the boundary
			if (collisionDetectorX.hasHitBoundary(newXDisplacement)) {
				motionX.setDisplacement(collisionDetectorX.getBoundary());
				newXDisplacement = motionX.getDisplacement();
			}
		}
		if (collisionDetectorY.hasHitBoundary(newYDisplacement)) {
			collisionDetectorY.setTarget(newYDisplacement);
			newYDisplacement = motionY.bounce(collisionDetectorY.getBoundary(), collisionDetectorY.getEnergyLoss());
			if (collisionDetectorY.hasHitBoundary(newYDisplacement)) {
				motionY.setDisplacement(collisionDetectorY.getBoundary());
				newYDisplacement = motionY.getDisplacement();
			}
		}
		
		// Convert to px
		this.x = (int)(Math.round(newXDisplacement*metresToPx));
		this.y = (int)(Math.round(newYDisplacement*metresToPx));
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
	public void collide(Ball otherBall, double energyLossCollision) {
		Point positionHit = getPointOfContact(otherBall);
		double xPositionHit = positionHit.x * pxToMetres;
		double yPositionHit = positionHit.y * pxToMetres;
		
//		double x1diff = xPositionHit-this.motionX.getDisplacement();
//		double y1diff = yPositionHit-this.motionY.getDisplacement();
//		double hyp1 = Math.sqrt((x1diff*x1diff) + (y1diff * y1diff));
//		double angle1 = Math.asin(Math.abs(y1diff)/hyp1);
//		
//		double x2diff = xPositionHit-otherBall.motionX.getDisplacement();
//		double y2diff = yPositionHit-otherBall.motionY.getDisplacement();
//		double hyp2 = Math.sqrt((x2diff*x2diff) + (y2diff * y2diff));
//		double angle2 = Math.asin(Math.abs(y2diff)/hyp2);
//		
//		if (x1diff > 0) this.motionX.setDisplacement(xPositionHit - (this.radius*Math.cos(angle1)));
//		else this.motionX.setDisplacement(xPositionHit + (this.radius*Math.cos(angle1)));
//		
//		if (y1diff > 0) this.motionY.setDisplacement(yPositionHit - (this.radius*Math.sin(angle1)));
//		else this.motionY.setDisplacement(yPositionHit + (this.radius*Math.sin(angle1)));
//		
//		if (x2diff > 0) otherBall.motionX.setDisplacement(xPositionHit - (otherBall.radius*Math.cos(angle2)));
//		else otherBall.motionX.setDisplacement(xPositionHit + (otherBall.radius*Math.cos(angle2)));
//		
//		if (y2diff > 0) otherBall.motionY.setDisplacement(yPositionHit - (otherBall.radius*Math.sin(angle2)));
//		else otherBall.motionY.setDisplacement(yPositionHit + (otherBall.radius*Math.sin(angle2)));

		double v1x = this.motionX.getVelocity();
		double v1y = this.motionY.getVelocity();
		double v2x = otherBall.motionX.getVelocity();
		double v2y = otherBall.motionY.getVelocity();
		
		double newv1x = (otherBall.mass*v2x)/(this.mass+otherBall.mass);
		double newv1y = (otherBall.mass*v2y)/(this.mass+otherBall.mass);
		double newv2x = (this.mass*v1x)/(this.mass+otherBall.mass);
		double newv2y = (this.mass*v1y)/(this.mass+otherBall.mass);
		
		this.motionX.setVelocity(newv1x*energyLossCollision);
		this.motionY.setVelocity(newv1y*energyLossCollision);
		otherBall.motionX.setVelocity(newv2x*energyLossCollision);
		otherBall.motionY.setVelocity(newv2y*energyLossCollision);
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

	public double getPxToMetres() {
		return pxToMetres;
	}

	public MotionX getMotionX() {
		return motionX;
	}

	public MotionY getMotionY() {
		return motionY;
	}

}