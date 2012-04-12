package mike;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import mike.Motion.Status;


/**
 * Class to maintain the position, shape, motion and color
 * of a ball.
 *
 * @author Mike
 *
 */
public class Ball {

	private MotionX motionX;
	private MotionY motionY;
	
	private Color color;
	
	private double energyLoss;	
	private double mass;
	private double radius;
	private double x;
	private double y;


	public Ball(double x, double y, double radius, double mass, Color color, MotionX motionX, MotionY motionY, double energyLoss) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.radius = radius;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.color = color;
		this.energyLoss = energyLoss;
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}

	public void updatePosition() {
		// Update and retrieve displacements if moving
		if (motionX.isMoving()) x = motionX.updateDisplacement(x);
		if (motionY.isMoving()) y = motionY.updateDisplacement(y);
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
		Point2D positionHit = getPointOfContact(otherBall);
		double xPositionHit = positionHit.getX();
		double yPositionHit = positionHit.getY();
		
		double combinedEnergyLoss = this.energyLoss * otherBall.energyLoss;
		
		this.motionX.collide(this.getMass(), this.radius, otherBall, combinedEnergyLoss, xPositionHit);
		this.motionY.collide(this.getMass(), this.radius, otherBall, combinedEnergyLoss, yPositionHit);
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
	public Point2D getPointOfContact(Ball otherBall) {
		double xMidpoint;
		double yMidpoint;

		// Distance between balls
		double xDiff = this.x - otherBall.x;
		double yDiff = this.y - otherBall.y;

		// Calculate angle between balls
		double hypoteneuse = Math.sqrt((Math.abs(xDiff)*Math.abs(xDiff))+(Math.abs(yDiff)*Math.abs(yDiff)));
		if (hypoteneuse == 0) return new Point2D.Double(this.x, this.y);
		double angle = Math.asin(Math.abs(yDiff)/hypoteneuse);

		// Project x and y lengths using the angle and radius
		double thisXLength = this.radius * Math.cos(angle);
		double thisYLength = this.radius * Math.sin(angle);
		double otherXLength = otherBall.radius * Math.cos(angle);
		double otherYLength = otherBall.radius * Math.sin(angle);

		// Determine whether to add or subtract each length
		if (xDiff == 0) xMidpoint = this.x;
		else if (xDiff > 0) { // This ball to the right
			// Therefore contact to the left of this ball, right of other ball
			double thisXPosition = this.x - thisXLength;
			double otherXPosition = otherBall.x + otherXLength;
			// Balls may have gone past each other, calculate midpoint of collision
			xMidpoint = thisXPosition + Math.abs(thisXPosition-otherXPosition)/2;
		}
		else { // This ball to the left
			// Therefore contact to the right of this ball, left of other ball
			double thisXPosition = this.x + thisXLength;
			double otherXPosition = otherBall.x - otherXLength;
			xMidpoint = thisXPosition - Math.abs(thisXPosition-otherXPosition)/2;
		}
		
		if (yDiff == 0) yMidpoint = this.y;
		else if (yDiff > 0) { // This ball below
			// Therefore contact above this ball, below other ball
			double thisYPosition = this.y - thisYLength;
			double otherYPosition = otherBall.y + otherYLength;
			yMidpoint = thisYPosition + Math.abs(thisYPosition-otherYPosition)/2;
		}
		else { // This ball above
			// Therefore contact above this ball, below other ball
			double thisYPosition = this.y + thisYLength;
			double otherYPosition = otherBall.y - otherYLength;
			yMidpoint = thisYPosition - Math.abs(thisYPosition-otherYPosition)/2;
		}

		if (Game.DEBUG) {
			System.out.println("Ball at: " + this.getX() + ", " + this.getY() +
					"   Other ball at: " + otherBall.getX() + ", " + otherBall.getY() +
					"   Collision at: " + xMidpoint + ", " + yMidpoint);
		}
		return new Point2D.Double(xMidpoint, yMidpoint); // Return midpoint of collision
	}
	
	public void bounceX(double boundary, double energyLoss) {
		x = motionX.bounce(x, boundary, energyLoss);
	}
	
	public void bounceY(double boundary, double energyLoss) {
		y = motionY.bounce(y, boundary, energyLoss);
	}
	
	public void paintBall(Graphics buffer, double scale) {
		buffer.setColor(color);
		buffer.fillOval ((int)Math.round((x - radius)*scale), (int)Math.round((y - radius)*scale),
				(int)Math.round((2 * radius)*scale), (int)Math.round((2 * radius)*scale));
	}

	public MotionX getMotionX() {
		return motionX;
	}

	public MotionY getMotionY() {
		return motionY;
	}

	public double getEnergyLoss() {
		return energyLoss;
	}

	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getMass() {
		return mass;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}

}
