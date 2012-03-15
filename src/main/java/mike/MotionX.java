package mike;

import java.applet.AudioClip;

import mike.Motion.Status;

public class MotionX extends Motion {

	private final double friction = 0.5;

	public MotionX(int pos, double initVelocity, double acceleration,
		double pxToMetres, AudioClip bounceAudio) {
		super(pos, initVelocity, acceleration, pxToMetres, bounceAudio);
		if(Game.DEBUG)
			System.out.println("Motion X with initPos: " + pos + ", pxToMetres: " + pxToMetres + ", initDisplacement: " + (pos*pxToMetres) + ", acceleration: " + acceleration + ", initVelocity: " + initVelocity + ", status: " + status);
	}

	public void bounce(double boundary, double energyLossThroughBounce) {
		// TODO - Work out how best to stop
		double distancePastBoundary = getDisplacement()-boundary;
		double velocityAsHitsBoundarySquared = (getVelocity()*getVelocity())-(2*getAcceleration()*distancePastBoundary);
		if (Game.DEBUG) {System.out.println("\nboundary: " + boundary +
				", position: " + getDisplacement() +
				", velocity: " + getVelocity() +
				", velocityAsHitsBoundarySquared: " + velocityAsHitsBoundarySquared);}
		if (velocityAsHitsBoundarySquared > 0) {
			// Get velocity as it hits the boundary (U), working out whether
			// sqrt should be positive or negative
			double velocityAsHitsBoundary = Math.sqrt(velocityAsHitsBoundarySquared); // WIll be positive
			if (getVelocity() < 0) velocityAsHitsBoundary *= -1; // Travelling upwards therefore velocity will be negative
			// Get time spent travelling past the boundary
			// displacement/time if no acceleration
			double timeTravellingPastBoundary = distancePastBoundary/getVelocity();
			if (Math.abs(getAcceleration()) > 0) timeTravellingPastBoundary = (getVelocity()-velocityAsHitsBoundary)/getAcceleration();
			// Calculate bounced velocity
			double bouncedVelocity = (-1*velocityAsHitsBoundary) * energyLossThroughBounce;
			// Extrapolate calculated values for the time past boundary
			setVelocity(bouncedVelocity + (getAcceleration()*timeTravellingPastBoundary));
			setDisplacement(boundary + 
					((bouncedVelocity)*timeTravellingPastBoundary) + ((getAcceleration()*timeTravellingPastBoundary*timeTravellingPastBoundary)/2));
			if (Game.DEBUG) { System.out.println("distancePastBoundary: " + distancePastBoundary + 
						", velocityAsHitsBoundary: " + velocityAsHitsBoundary + 
						", timeTravellingPastBoundary: " + timeTravellingPastBoundary + 
						"\nbouncedVelocity: " + bouncedVelocity + 
						", velocity: " + getVelocity() + 
						", displacement: " + getDisplacement());}
		}
		else { // TODO
			setVelocity(0);
			setDisplacement(boundary);
			status = Status.STOPPED;
			System.out.println("X velocityAsHitsBoundarySquared < 0");
		}
		//bounceAudio.play();
	}

}
