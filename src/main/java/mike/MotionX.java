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

}
