package mike;

import java.applet.AudioClip;

public class MotionY extends Motion {

	public MotionY(int pos, double initVelocity, double acceleration,
			double pxToMetres, AudioClip bounceAudio) {
		super(pos, initVelocity, acceleration, pxToMetres, bounceAudio);

		if(Game.DEBUG)
			System.out.println("Motion Y with initPos: " + pos + ", pxToMetres: " + pxToMetres + ", initDisplacement: " + (pos*pxToMetres) + ", acceleration: " + acceleration + ", initVelocity: " + initVelocity + ", status: " + status);
	}

}
