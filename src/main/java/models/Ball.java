package models;


import java.awt.Color;


/**
 * Model class to maintain the position, radius, mass, energyLoss, 
 * motion and color of a ball.
 *
 * @author Mike
 *
 */
public class Ball {

	private double x;
	private double y;
	private double radius;
	private double mass;	
	private double energyLoss;	
	
	private Motion motionX;
	private Motion motionY;
	
	private Color color;


	public Ball(double x, double y, double radius, double mass, Color color, Motion motionX, Motion motionY, double energyLoss) {
		this.motionX = motionX;
		this.motionY = motionY;
		this.radius = radius;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.color = color;
		this.energyLoss = energyLoss;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public double getEnergyLoss() {
		return energyLoss;
	}
	
	public void setEnergyLoss(double energyLoss) {
		this.energyLoss = energyLoss;
	}

	public Motion getMotionX() {
		return motionX;
	}

	public void setMotionX(Motion motionX) {
		this.motionX = motionX;
	}

	public Motion getMotionY() {
		return motionY;
	}

	public void setMotionY(Motion motionY) {
		this.motionY = motionY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}