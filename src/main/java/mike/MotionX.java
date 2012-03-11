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

	public void bounce(double energyLossThroughBounce, int boundary) {
		// TODO - Work out how best to stop and boundary interactions
		// Calculate velocity as hits boundary, rather than past boundary
//		double distanceBeyondBoundary = displacement-(boundary*pxToMetres);
//		double velocityAsHitsBoundarySquared = (velocity*velocity)-(2*acceleration*distanceBeyondBoundary);
//		if (velocityAsHitsBoundarySquared < 0 && status == Status.MOVING) rest();
//		else if (status == Status.MOVING){
//			double velocityAsHitsBoundary = Math.sqrt(velocityAsHitsBoundarySquared);
//			double bouncedVelocity = velocityAsHitsBoundary * -1 * energyLossThroughBounce;
//			double timePastBoundary = (velocity-velocityAsHitsBoundary)/acceleration;
//			double newDisplacement = (boundary*pxToMetres) + ((bouncedVelocity*timePastBoundary) + ((acceleration*timePastBoundary*timePastBoundary)/2));
//			double newVelocity = bouncedVelocity + (acceleration*timePastBoundary);
			//timeLastUpdated = System.currentTimeMillis();
//			System.out.println("Velocity: " + velocity +
//					", displacement: " + displacement +
//					", distanceBeyondBoundary: " + distanceBeyondBoundary +
//					", velocityAsHitsBoundary: " + velocityAsHitsBoundary +
//					", bouncedVelocity: " + bouncedVelocity +
//					", timePastBoundary: " + timePastBoundary +
//					", newDisplacement: " + newDisplacement +
//					", newVelocity: " + newVelocity);
//			double velocityPastBoundary = getVelocity() + (getAcceleration() * dt());
//			double timeDifference = distancePastBoundary/velocityPastBoundary;
//			double velocityAtBoundary = getVelocity() + (getAcceleration() * (dt()-timeDifference));
			double distancePastBoundary = getDisplacement()-(boundary*getPxToMetres());
			double velocityAsHitsBoundarySquared = (getVelocity()*getVelocity())-(2*getAcceleration()*distancePastBoundary);// Should have lost velocity due to gravity and hence not have moved so far
			setDisplacement(boundary * getPxToMetres() - distancePastBoundary);
			setVelocity((getVelocity()*-1) * energyLossThroughBounce);
			setTimeLastUpdated(System.nanoTime());
//		}

//		if(status == Status.MOVING ){
//			if(Game.DEBUG) System.out.println("STOPPED");
//			stopMovement(pos);
//		}
		//bounceAudio.play();
	}

}
