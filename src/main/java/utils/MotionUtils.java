package utils;

import models.Motion;
import models.Motion.Status;


public class MotionUtils {


	public double updateDisplacement(Motion motion, double displacement) {
		double time = (double)(System.nanoTime() - motion.getTimeLastUpdated())*(double)(0.000000001);
		// Velocity = U at this point so S = Ut + (at^2)/2
		double newDisplacement = displacement + (motion.getVelocity()*time) + ((motion.getAcceleration()*time*time)/2);
		// V = U + at
		motion.setVelocity(motion.getVelocity() + (motion.getAcceleration() * time));
		motion.setTimeLastUpdated(System.nanoTime());
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
	public double bounce(Motion motion, double displacement, double boundary, double energyLossThroughBounce) {
		double distancePastBoundary = displacement-boundary;
		double velocityAsHitsBoundarySquared = (motion.getVelocity()*motion.getVelocity())-(2*motion.getAcceleration()*distancePastBoundary);

		if (velocityAsHitsBoundarySquared > 0) {
			// Get velocity as it hits the boundary (U), working out whether
			// sqrt should be positive or negative
			double velocityAsHitsBoundary = Math.sqrt(velocityAsHitsBoundarySquared); // Will be positive
			if (motion.getVelocity() < 0) velocityAsHitsBoundary *= -1; // Travelling upwards/left therefore velocity will be negative

			// Get time spent travelling past the boundary (displacement/time if no acceleration)
			double timeTravellingPastBoundary = distancePastBoundary/motion.getVelocity();
			if (Math.abs(motion.getAcceleration()) > 0)
				timeTravellingPastBoundary = (motion.getVelocity()-velocityAsHitsBoundary)/motion.getAcceleration();

			// Calculate bounced velocity
			double bouncedVelocity = (-1*velocityAsHitsBoundary) * energyLossThroughBounce;

			// Extrapolate calculated values for the time past boundary
			motion.setVelocity(bouncedVelocity + (motion.getAcceleration()*timeTravellingPastBoundary));
			// Calculate new displacement, setting to boundary if still within
			displacement = boundary +
				((bouncedVelocity)*timeTravellingPastBoundary) +
				((motion.getAcceleration()*timeTravellingPastBoundary*timeTravellingPastBoundary)/2);
		}
		else stopMotion(motion); // Cannot sqrt negative - something gone wrong?

		return displacement;
	}

	public void applyForce(Motion motion, double force, double mass) {
		motion.setVelocity((force/mass)*2);
		startMotion(motion);
	}

	public void startMotion(Motion motion) {
		motion.setAcceleration(motion.getInitialAcceleration());
		motion.setStatus(Status.MOVING);
		motion.setTimeLastUpdated(System.nanoTime());
	}

	public void stopMotion(Motion motion) {
		motion.setVelocity(0);
		motion.setAcceleration(0);
		motion.setStatus(Status.STOPPED);
	}

	public void collide(Motion motion1, double mass1, double radius1, Motion motion2, double mass2, double radius2,
			double energyLoss, double positionHit) {
		double newVelocity1 = ( ((mass1-mass2) * motion1.getVelocity()) + (2*mass2*motion2.getVelocity()) )
			/ (mass1+mass2);
		double newVelocity2 = ( ((mass2-mass1) * motion2.getVelocity()) + (2*mass1*motion1.getVelocity()) )
			/ (mass1+mass2);
		motion1.setVelocity(newVelocity1*energyLoss);
		motion2.setVelocity(newVelocity2*energyLoss);
	}

}