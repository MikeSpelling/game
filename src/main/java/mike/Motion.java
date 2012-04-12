package mike;

import java.applet.AudioClip;

public abstract class Motion {

	private final double initialAcceleration;
	private long timeLastUpdated;
	private double velocity;
	private double acceleration;

	public static enum Status {STOPPED, MOVING};
	public Status status;

	private AudioClip bounceAudio;
	
	public abstract void collide(double mass, double radius, Ball otherBall, double energyLoss, double positionHit);


	public Motion(double velocity, double acceleration, AudioClip bounceAudio) {
		this.velocity = velocity;
		this.initialAcceleration = acceleration;
		this.acceleration = initialAcceleration;
		this.bounceAudio = bounceAudio;
		this.timeLastUpdated = System.nanoTime();
	}

	public double updateDisplacement(double displacement) {
		double time = dt();
		// Velocity = U at this point so S = Ut + (at^2)/2
		double newDisplacement = displacement + (velocity*time) + ((acceleration*time*time)/2);
		// V = U + at
		velocity = velocity + (acceleration * time);
		timeLastUpdated = System.nanoTime();
		return newDisplacement;
	}

	/**
	 * Calculate new velocity and displacement based on
	 * how far the ball has passed the boundary and its
	 * final speed
	 *
	 * @param boundary
	 * @param energyLossThroughBounce
	 * @return
	 */
	public double bounce(double displacement, double boundary, double energyLossThroughBounce) {
		double distancePastBoundary = displacement-boundary;
		double velocityAsHitsBoundarySquared = (velocity*velocity)-(2*acceleration*distancePastBoundary);
		
		if (velocityAsHitsBoundarySquared > 0) {
			// Get velocity as it hits the boundary (U), working out whether
			// sqrt should be positive or negative
			double velocityAsHitsBoundary = Math.sqrt(velocityAsHitsBoundarySquared); // Will be positive
			if (velocity < 0) velocityAsHitsBoundary *= -1; // Travelling upwards/left therefore velocity will be negative

			// Get time spent travelling past the boundary (displacement/time if no acceleration)
			double timeTravellingPastBoundary = distancePastBoundary/velocity;
			if (Math.abs(acceleration) > 0) timeTravellingPastBoundary = (velocity-velocityAsHitsBoundary)/acceleration;

			// Calculate bounced velocity
			double bouncedVelocity = (-1*velocityAsHitsBoundary) * energyLossThroughBounce;

			// Extrapolate calculated values for the time past boundary
			velocity = bouncedVelocity + (acceleration*timeTravellingPastBoundary);
			// Calculate new displacement, setting to boundary if still within
			displacement = boundary +
				((bouncedVelocity)*timeTravellingPastBoundary) + ((acceleration*timeTravellingPastBoundary*timeTravellingPastBoundary)/2);
		}
		else stopMotion(); // Cannot sqrt negative - something gone wrong?
		
		//bounceAudio.play();
		return displacement;
	}

	public void applyForce(double force, double mass) {
		velocity = (force/mass)*2;
		startMotion();
	}

	public void startMotion() {
		this.acceleration = initialAcceleration;
		status = Status.MOVING;
		timeLastUpdated = System.nanoTime();
	}

	public void stopMotion() {
		velocity = 0;
		acceleration = 0;
		status = Status.STOPPED;
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

	protected void setTimeLastUpdated(long timeLastUpdated) {
		this.timeLastUpdated = timeLastUpdated;
	}

	protected double getVelocity() {
		return velocity;
	}

	protected double getAcceleration() {
		return acceleration;
	}

	protected long getTimeLastUpdated() {
		return timeLastUpdated;
	}

}