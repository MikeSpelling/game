package models;


import java.awt.Color;

import utils.MotionX;
import utils.MotionY;



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
	
	private MotionX motionX;
	private MotionY motionY;
	
	private Color color;


	public Ball(double x, double y, double radius, double mass, Color color, MotionX motionX, MotionY motionY, double energyLoss) {
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

	public MotionX getMotionX() {
		return motionX;
	}

	public void setMotionX(MotionX motionX) {
		this.motionX = motionX;
	}

	public MotionY getMotionY() {
		return motionY;
	}

	public void setMotionY(MotionY motionY) {
		this.motionY = motionY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}