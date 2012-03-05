package mike;

import java.applet.AudioClip;

public abstract class Motion {
	
	private final double moveFactor = 2;
	private long timeResetVelocity;
	private int initPos;
	private double pxToMetres;
	private double metresToPx;
	private double initDisplacement;
	private double initVelocity;
	private final double initialAcceleration;
	private double currentAcceleration;
	private double displacement;
	
	public enum Status {STOPPED, MOVING};
	public Status status;
	
	AudioClip bounceAudio;
	
	public Motion(int pos, double initVelocity, double acceleration, double pxToMetres, AudioClip bounceAudio) {
		this.initPos = pos;
		this.pxToMetres = pxToMetres;
		this.metresToPx = 1/this.pxToMetres;
		this.initDisplacement = this.initPos*this.pxToMetres;
		this.initialAcceleration = acceleration;
		this.currentAcceleration = initialAcceleration;
		this.bounceAudio = bounceAudio;
		this.initVelocity = initVelocity;
		
		startMovement();
	}
	
	public int getNewPos() {
		displacement = initDisplacement + (initVelocity*getTimeMoving()) + ((currentAcceleration*getTimeMoving()*getTimeMoving())/2);
		return (int)(displacement*metresToPx);
	}

	public void bounce(double energyLossThroughBounce, int pos, double boundary) {
		//if(getTimeMoving() > 0.125) { // TODO - Work out how best to stop
			// Calculate velocity as hits boundary, rather than past boundary
			double velocityPastBoundary = initVelocity + (currentAcceleration * getTimeMoving());
			double distanceDifference = (pos-boundary)*pxToMetres;
			double timeDifference = distanceDifference/velocityPastBoundary;
			double velocityAtBoundary = initVelocity + (currentAcceleration * (getTimeMoving()-timeDifference));
			initVelocity = (velocityAtBoundary*-1) * energyLossThroughBounce;
			initDisplacement = boundary * pxToMetres;
			timeResetVelocity = System.currentTimeMillis();
//			System.out.println("BOUNCE");
//		}
//		else if(status == Status.MOVING ){	
//			if(Game.DEBUG) System.out.println("STOPPED");
//			stopMovement(pos);
//		}
		//if(Game.DEBUG) System.out.println("BOUNCE - initDisplacement: " + initDisplacement + ", initVelocity: " + initVelocity + ", timeMoving: " + getTimeMoving() + ", status: " + status);
		//bounceAudio.play();
	}
	
	public void applyForce(int pos, int newPos) {
		initVelocity = ((newPos-pos)*pxToMetres)*moveFactor;
		initDisplacement = pos*pxToMetres;
		this.currentAcceleration = initialAcceleration;
		startMovement();
//		if(Game.DEBUG)
//			System.out.println("MOVE - pos: " + pos + ", status: " + status);
	}
	
	public double getTimeMoving() {
		return (System.currentTimeMillis() - (double)(timeResetVelocity))/(double)(1000);
	}
	
	public void stopMovement(int pos) {
		initVelocity = 0;
		currentAcceleration = 0;
		displacement = pos * pxToMetres;
		initDisplacement = pos * pxToMetres;
		status = Status.STOPPED;
	}
	
	public void startMovement() {
		if(Math.abs(initVelocity) > 0 || Math.abs(currentAcceleration) > 0) {
			if(status == Status.STOPPED) {
				System.out.println("STARTED");
			}
			status = Status.MOVING;
			timeResetVelocity = System.currentTimeMillis();
		}
		else {
			status = Status.STOPPED;
		}
	}

}