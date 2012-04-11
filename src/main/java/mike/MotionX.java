package mike;

import java.applet.AudioClip;

public class MotionX extends Motion {

	private final double friction = 0.5;

	public MotionX(int pos, double initVelocity, double acceleration,
		double pxToMetres, AudioClip bounceAudio) {
		super(pos, initVelocity, acceleration, pxToMetres, bounceAudio);
		if(Game.DEBUG)
			System.out.println("Motion X with initPos: " + pos + ", pxToMetres: " + pxToMetres + ", initDisplacement: " + (pos*pxToMetres) + ", acceleration: " + acceleration + ", initVelocity: " + initVelocity + ", status: " + status);
	}
	
	public void collide(double mass, int radius, Ball otherBall, double energyLoss, double positionHit) {
		double newVelocity = ( ((mass-otherBall.getMass()) * this.getVelocity()) + (2*otherBall.getMass()*otherBall.getMotionX().getVelocity()) )
			/ (mass+otherBall.getMass());
		double otherNewVelocity = ( ((otherBall.getMass()-mass) * otherBall.getMotionX().getVelocity()) + (2*mass*this.getVelocity()) )
			/ (mass+otherBall.getMass());
		this.setVelocity(newVelocity*energyLoss);
		otherBall.getMotionX().setVelocity(otherNewVelocity*energyLoss);
	}

}
