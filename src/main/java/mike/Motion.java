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

	public static enum Status {STOPPED, MOVING};
	public Status status;

	private AudioClip bounceAudio;

	public abstract void bounce(double energyLossThroughBounce, int boundary);

	public Motion(int pos, double velocity, double acceleration, double pxToMetres, AudioClip bounceAudio) {
		this.pxToMetres = pxToMetres;
		this.displacement = pos*this.pxToMetres;
		this.velocity = velocity;
		this.initialAcceleration = acceleration;
		this.acceleration = initialAcceleration;
		this.metresToPx = 1/this.pxToMetres;
		this.bounceAudio = bounceAudio;
		this.timeLastUpdated = System.nanoTime();
	}

	public int updatePosition() {
		// Velocity = U at this point so V = Ut + (at^2)/2
		displacement += (velocity*dt()) + ((acceleration*dt()*dt())/2);
		// V = U + at
		velocity = velocity + (acceleration * dt());
		timeLastUpdated = System.nanoTime();
		return (int)(displacement*metresToPx);
	}

	public void applyForce(int forceVector) {
		velocity = (forceVector*pxToMetres)*moveFactor;
		this.acceleration = initialAcceleration;
		startMotion();
	}

	public void collide(int radius, int positionHit, double energyLossCollision) {
		int multiplier = 1;
		double newDisplacement = 1;
		if (velocity >= 0) {
			if (displacement >= (positionHit*pxToMetres))
				newDisplacement = (positionHit*pxToMetres) + (radius*pxToMetres);
			else {
				multiplier = -1;;
				newDisplacement = (positionHit*pxToMetres) - (radius*pxToMetres);
			}
		}
		else {
			if (displacement >= (positionHit*pxToMetres)) {
				multiplier = -1;
				newDisplacement = (positionHit*pxToMetres) + (radius*pxToMetres);
			}
			else
				newDisplacement = (positionHit*pxToMetres) - (radius*pxToMetres);
		}
		double newVelocity = (velocity*multiplier) * energyLossCollision;
//		System.out.println("Velocity: " + velocity + ", newVelocity: " + newVelocity +
//				", DisplacementPx: " + (displacement*metresToPx) + ", newDisplacementPx: " + (newDisplacement*metresToPx));
		displacement = newDisplacement;
		velocity = newVelocity;
		setTimeLastUpdated(System.nanoTime());
	}

	public void startMotion() {
		if(Math.abs(velocity) > 0 || Math.abs(acceleration) > 0) {
			this.acceleration = initialAcceleration;
			status = Status.MOVING;
			timeLastUpdated = System.nanoTime();
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

	protected double dt() {
		return (double)(System.nanoTime() - timeLastUpdated)*(double)(0.000000001);
	}

	protected void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	protected void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	protected void setDisplacement(double displacement) {
		this.displacement = displacement;
	}

	protected void setTimeLastUpdated(long timeLastUpdated) {
		this.timeLastUpdated = timeLastUpdated;
	}

	protected void setPxToMetres(double pxToMetres) {
		this.pxToMetres = pxToMetres;
	}

	protected void setMetresToPx(double metresToPx) {
		this.metresToPx = metresToPx;
	}

	protected double getVelocity() {
		return velocity;
	}

	protected double getAcceleration() {
		return acceleration;
	}

	protected double getDisplacement() {
		return displacement;
	}

	protected long getTimeLastUpdated() {
		return timeLastUpdated;
	}

	protected double getPxToMetres() {
		return pxToMetres;
	}

	protected double getMetresToPx() {
		return metresToPx;
	}

}