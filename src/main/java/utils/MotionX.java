package utils;

import java.applet.AudioClip;

import models.Ball;

public class MotionX extends Motion {

	private final double friction = 0.5;

	public MotionX(double initVelocity, double acceleration, AudioClip bounceAudio) {
		super(initVelocity, acceleration, bounceAudio);
	}
	
	public void collide(double mass, double radius, Ball otherBall, double energyLoss, double positionHit) {
		double newVelocity = ( ((mass-otherBall.getMass()) * this.getVelocity()) + (2*otherBall.getMass()*otherBall.getMotionX().getVelocity()) )
			/ (mass+otherBall.getMass());
		double otherNewVelocity = ( ((otherBall.getMass()-mass) * otherBall.getMotionX().getVelocity()) + (2*mass*this.getVelocity()) )
			/ (mass+otherBall.getMass());
		this.setVelocity(newVelocity*energyLoss);
		otherBall.getMotionX().setVelocity(otherNewVelocity*energyLoss);
	}

}
