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
	
	private double mass;
	private int radius;
	private int x;
	private int y;
	private Color color;


	public Ball(int x, int y, int radius, double mass, Color color,	MotionX motionX, MotionY motionY, double energyLoss) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.pxToMetres = motionX.getPxToMetres();
		this.metresToPx = motionX.getMetresToPx();
		this.radius = radius;
		this.mass = mass;
		this.setX(x);
		this.setY(y);
		this.color = color;
		this.energyLoss = energyLoss;
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}

	public void updatePosition() {
		// Update and retrieve displacements if moving
		if (isXMoving()) {
			double xDisplacement = motionX.updateDisplacement();
			this.setX((int)(Math.round(xDisplacement*metresToPx)));
		}
		if (isYMoving()) {
			double yDisplacement = motionY.updateDisplacement();
			this.setY((int)(Math.round(yDisplacement*metresToPx)));
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
		double width = Math.abs(this.getX() - otherBall.getX());
		double height = Math.abs(this.getY() - otherBall.getY());
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
	public Point getPointOfContact(Ball otherBall) {
		int xMidpoint;
		int yMidpoint;

		// Distance between balls
		int xDiff = this.getX() - otherBall.getX();
		int yDiff = this.getY() - otherBall.getY();

		// Calculate angle between balls
		double hypoteneuse = Math.sqrt((Math.abs(xDiff)*Math.abs(xDiff))+(Math.abs(yDiff)*Math.abs(yDiff)));
		if (hypoteneuse == 0) return new Point (this.getX(), this.getY());
		double angle = Math.asin(Math.abs(yDiff)/hypoteneuse);

		// Project x and y lengths using the angle and radius
		double thisXLength = this.radius * Math.cos(angle);
		double thisYLength = this.radius * Math.sin(angle);
		double otherXLength = otherBall.radius * Math.cos(angle);
		double otherYLength = otherBall.radius * Math.sin(angle);

		// Determine whether to add or subtract each length
		if (xDiff > 0) { // This ball to the right
			// Therefore contact to the left of this ball, right of other ball
			int thisXPosition = (int)Math.round(this.getX() - thisXLength);
			int otherXPosition = (int)Math.round(otherBall.getX() + otherXLength);
			// Balls may have gone past each other, calculate midpoint of collision
			xMidpoint = thisXPosition + Math.abs(thisXPosition-otherXPosition)/2;
		}
		else { // This ball to the left
			// Therefore contact to the right of this ball, left of other ball
			int thisXPosition = (int)Math.round(this.getX() + thisXLength);
			int otherXPosition = (int)Math.round(otherBall.getX() - otherXLength);
			xMidpoint = thisXPosition - Math.abs(thisXPosition-otherXPosition)/2;
		}
		if (yDiff > 0) { // This ball below
			// Therefore contact above this ball, below other ball
			int thisYPosition = (int)Math.round(this.getY() - thisYLength);
			int otherYPosition = (int)Math.round(otherBall.getY() + otherYLength);
			yMidpoint = thisYPosition + Math.abs(thisYPosition-otherYPosition)/2;
		}
		else { // This ball above
			// Therefore contact above this ball, below other ball
			int thisYPosition = (int)Math.round(this.getY() + thisYLength);
			int otherYPosition = (int)Math.round(otherBall.getY() - otherYLength);
			yMidpoint = thisYPosition - Math.abs(thisYPosition-otherYPosition)/2;
		}

		if (Game.DEBUG) {
			System.out.println("Ball at: " + this.getX() + ", " + this.getY() +
					"   Other ball at: " + otherBall.getX() + ", " + otherBall.getY() +
					"   Collision at: " + xMidpoint + ", " + yMidpoint);
		}
		return new Point(xMidpoint, yMidpoint); // Return midpoint of collision
	}
	
	public void bounceX(double boundary, double energyLoss) {
		motionX.bounce(boundary, energyLoss);
		this.x = (int)Math.round(motionX.getDisplacement()*metresToPx);
	}
	
	public void bounceY(double boundary, double energyLoss) {
		motionY.bounce(boundary, energyLoss);
		this.y = (int)Math.round(motionY.getDisplacement()*metresToPx);
	}
	
	public void paintBall(Graphics buffer) {
		buffer.setColor(color);
		buffer.fillOval (getX() - radius, getY() - radius,
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
		this.setX((int)Math.round(xDisplacement*metresToPx));
	}

	public void setyDisplacement(double yDisplacement) {
		this.motionY.setDisplacement(yDisplacement);
		this.setY((int)Math.round(yDisplacement*metresToPx));
	}

	public double getEnergyLoss() {
		return energyLoss;
	}

	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}
	
	public double getRadiusMetres() {
		return this.radius*pxToMetres;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getMass() {
		return mass;
	}

}