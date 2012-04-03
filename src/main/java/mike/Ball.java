package mike;

import java.awt.Color;
import java.awt.Graphics;
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

	private final double pxToMetres;
	private final double metresToPx;
	private final MotionX motionX;
	private final MotionY motionY;
	private double energyLoss;	
	
	public double mass;
	public int radius;
	public int x;
	public int y;
	public Color color;


	public Ball(int x, int y, int radius, double mass, Color color,	MotionX motionX, MotionY motionY, double energyLoss) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.color = color;
		this.energyLoss = energyLoss;
		this.motionX = motionX;
		this.motionY = motionY;
		this.pxToMetres = motionX.getPxToMetres();
		this.metresToPx = motionX.getMetresToPx();
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}

	public void updatePosition() {
		// Update and retrieve displacements if moving
		if (isXMoving()) {
			double xDisplacement = motionX.updateDisplacement();
			this.x = (int)(Math.round(xDisplacement*metresToPx));
		}
		if (isYMoving()) {
			double yDisplacement = motionY.updateDisplacement();
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
	public void collide(Ball otherBall) {
		Point positionHit = getPointOfContact(otherBall);
		double xPositionHit = positionHit.x * pxToMetres;
		double yPositionHit = positionHit.y * pxToMetres;
		
		double combinedEnergyLoss = this.energyLoss * otherBall.energyLoss;
		
		this.motionX.collide(this.mass, this.radius, otherBall.mass, otherBall.radius, otherBall.getMotionX().getVelocity(), combinedEnergyLoss, xPositionHit);
		this.motionY.collide(this.mass, this.radius, otherBall.mass, otherBall.radius, otherBall.getMotionY().getVelocity(), combinedEnergyLoss, yPositionHit);
		otherBall.motionX.collide(otherBall.mass, otherBall.radius, this.mass, this.radius, this.getMotionX().getVelocity(), combinedEnergyLoss, xPositionHit);
		otherBall.motionY.collide(otherBall.mass, otherBall.radius, this.mass, this.radius, this.getMotionY().getVelocity(), combinedEnergyLoss, yPositionHit);
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
	
	public void paintBall(Graphics buffer) {
		buffer.setColor(color);
		buffer.fillOval (x - radius, y - radius,
				2 * radius, 2 * radius);
	}

	public boolean isXMoving() {
		return motionX.status == Status.MOVING;
	}

	public boolean isYMoving() {
		return motionY.status == Status.MOVING;
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

	public double getxDisplacement() {
		return motionX.getDisplacement();
	}
	
	public double getyDisplacement() {
		return motionY.getDisplacement();
	}

	public void setxDisplacement(double xDisplacement) {
		this.motionX.setDisplacement(xDisplacement);
		this.x = (int)Math.round(xDisplacement*metresToPx);
	}

	public void setyDisplacement(double yDisplacement) {
		this.y = (int)Math.round(yDisplacement*metresToPx);
	}

	public double getEnergyLoss() {
		return energyLoss;
	}

	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}

}