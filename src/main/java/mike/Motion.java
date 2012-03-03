package mike;

import java.applet.AudioClip;

public class Motion {
	
	private final double moveFactor = 2;
	private final double friction = 0.5;
	private long timeResetVelocity;
	private int initPos;
	private double pxToMetres;
	private double metresToPx;
	private double initDisplacement;
	private double initVelocity;
	private double acceleration;
	private double displacement;
	
	public enum Status {STOPPED, MOVING};
	public Status status;
	
	AudioClip bounceAudio;
	
	public Motion(int pos, double initVelocity, double acceleration, double pxToMetres, AudioClip bounceAudio) {
		this.initPos = pos;
		this.pxToMetres = pxToMetres;
		this.metresToPx = 1/this.pxToMetres;
		this.initDisplacement = this.initPos*this.pxToMetres;
		this.acceleration = acceleration;
		this.bounceAudio = bounceAudio;
		this.initVelocity = initVelocity;
		
		startMovement();
		
		if(Game.DEBUG)
			System.out.println("initPos: " + initPos + ", pxToMetres: " + this.pxToMetres + ", initDisplacement: " + initDisplacement + ", acceleration: " + acceleration + ", initVelocity: " + initVelocity + ", status: " + status);
	}
	
	public int getPos() {
		displacement = initDisplacement + (initVelocity*getTimeMoving()) + ((acceleration*getTimeMoving()*getTimeMoving())/2);
		return (int)(displacement*metresToPx);
	}
	
	public int getPosWithFriction(int pos, double friction) {
		return getPos();
//		double velocity = initVelocity + (acceleration * getTimeMoving());
//		displacement = initDisplacement + (getTimeMoving() * ((velocity + initVelocity) / 2));
//		applyFriction(friction, velocity);
//		if(Math.abs(velocity) < 1.5) {
//			velocity = 0;
//			stopMovement(pos);
//		}
//		return (int)(displacement*metresToPx);
	}
	
	private void applyFriction(double friction, double velocity) {
		acceleration = -1 * velocity;
	}

	public void bounce(double energyLossThroughBounce, int pos, double boundary) {
		//if(getTimeMoving() > 0.125) { // TODO - Work out how best to stop
			// Calculate velocity as hits boundary, rather than past boundary
			double velocityPastBoundary = initVelocity + (acceleration * getTimeMoving());
			double distanceDifference = (pos-boundary)*pxToMetres;
			double timeDifference = distanceDifference/velocityPastBoundary;
			double velocityAtBoundary = initVelocity + (acceleration * (getTimeMoving()-timeDifference));
			initVelocity = (velocityAtBoundary*-1) * energyLossThroughBounce;
			initDisplacement = boundary * pxToMetres;
			timeResetVelocity = System.currentTimeMillis();
//		}
//		else if(status == Status.MOVING ){	
//			if(Game.DEBUG) System.out.println("STOPPED");
//			stopMovement(pos);
//		}
		//if(Game.DEBUG) System.out.println("BOUNCE - initDisplacement: " + initDisplacement + ", initVelocity: " + initVelocity + ", timeMoving: " + getTimeMoving() + ", status: " + status);
		//bounceAudio.play();
	}
	
	public void move(int pos, int newPos, double acceleration) {
		initVelocity = ((newPos-pos)*pxToMetres)*moveFactor;
		initDisplacement = pos*pxToMetres;
		this.acceleration = acceleration;
		startMovement();
		if(Game.DEBUG)
			System.out.println("MOVE - pos: " + pos + ", status: " + status);
	}
	
	public double getTimeMoving() {
		return (System.currentTimeMillis() - (double)(timeResetVelocity))/(double)(1000);
	}
	
	public void stopMovement(int pos) {
		initVelocity = 0;
		acceleration = 0;
		displacement = pos * pxToMetres;
		initDisplacement = pos * pxToMetres;
		status = Status.STOPPED;
	}
	
	public void startMovement() {
		if(Math.abs(initVelocity) > 0 || Math.abs(acceleration) > 0) {
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