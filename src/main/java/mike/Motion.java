package mike;

import java.applet.AudioClip;

public abstract class Motion {
	
	private final double moveFactor = 2;
	private final double initialAcceleration;
	private long timeLastUpdated;
	private double pxToMetres;
	private double metresToPx;
	private double velocity;
	private double acceleration;
	private double displacement;
	
	public enum Status {STOPPED, MOVING};
	public Status status;
	
	AudioClip bounceAudio;
	
	public Motion(int pos, double velocity, double acceleration, double pxToMetres, AudioClip bounceAudio) {
		this.pxToMetres = pxToMetres;
		this.displacement = pos*this.pxToMetres;
		this.velocity = velocity;
		this.initialAcceleration = acceleration;
		this.acceleration = initialAcceleration;
		this.metresToPx = 1/this.pxToMetres;
		this.bounceAudio = bounceAudio;
		this.timeLastUpdated = System.currentTimeMillis();
	}
	
	public int updatePosition() {
		velocity = velocity + (acceleration * dt());
		displacement += (velocity*dt()) + ((acceleration*dt()*dt())/2);
		int pos = (int)(displacement*metresToPx);
		timeLastUpdated = System.currentTimeMillis();
		return pos;
	}

	public void bounce(double energyLossThroughBounce, double boundary) {
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
			
			double distancePastBoundary = displacement-(boundary*pxToMetres);
			double velocityPastBoundary = velocity + (acceleration * dt());
			double timeDifference = distancePastBoundary/velocityPastBoundary;
			double velocityAtBoundary = velocity + (acceleration * (dt()-timeDifference));
			velocity = (velocityAtBoundary*-1) * energyLossThroughBounce;
			displacement = boundary * pxToMetres;
			timeLastUpdated = System.currentTimeMillis();
//		}

//		if(status == Status.MOVING ){	
//			if(Game.DEBUG) System.out.println("STOPPED");
//			stopMovement(pos);
//		}
		//bounceAudio.play();
	}
	
	public void applyForce(int forceVector) {
		velocity = (forceVector*pxToMetres)*moveFactor;
		this.acceleration = initialAcceleration;
		startMotion();
	}
	
	public void startMotion() {
		if(Math.abs(velocity) > 0 || Math.abs(acceleration) > 0) {
			this.acceleration = initialAcceleration;
			status = Status.MOVING;
			timeLastUpdated = System.currentTimeMillis();
		}
		else {
			status = Status.STOPPED;
		}
	}
	
	public void stopMotion() {
		velocity = 0;
		status = Status.STOPPED;
	}
	
	public void rest() {
		stopMotion();
		acceleration = 0;
	}
	
	private double dt() {
		return (System.currentTimeMillis() - (double)(timeLastUpdated))/(double)(1000);
	}

}