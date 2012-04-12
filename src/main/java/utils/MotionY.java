package utils;

import models.Ball;

public class MotionY extends Motion {

	public MotionY(double initVelocity, double acceleration) {
		super(initVelocity, acceleration);
	}
	
	public void collide(double mass, double radius, Ball otherBall, double energyLoss, double positionHit) {
		double newVelocity = ( ((mass-otherBall.getMass()) * this.getVelocity()) + (2*otherBall.getMass()*otherBall.getMotionY().getVelocity()) )
			/ (mass+otherBall.getMass());
		double otherNewVelocity = ( ((otherBall.getMass()-mass) * otherBall.getMotionX().getVelocity()) + (2*mass*this.getVelocity()) )
			/ (mass+otherBall.getMass());
		this.setVelocity(newVelocity*energyLoss);
		otherBall.getMotionY().setVelocity(otherNewVelocity*energyLoss);
	}

}