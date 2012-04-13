package models;


public class Motion {

	private final double initialAcceleration;
	private long timeLastUpdated;
	private double velocity;
	private double acceleration;
	private Status status;

	public static enum Status {STOPPED, MOVING};	


	public Motion(double velocity, double acceleration) {
		this.velocity = velocity;
		this.initialAcceleration = acceleration;
		this.acceleration = initialAcceleration;
		this.timeLastUpdated = System.nanoTime();
		if (velocity != 0 || acceleration != 0) this.status = Status.MOVING;
		else this.status = Status.STOPPED;
	}
	
	public boolean isMoving() {
		return status == Status.MOVING;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public void setTimeLastUpdated(long timeLastUpdated) {
		this.timeLastUpdated = timeLastUpdated;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public long getTimeLastUpdated() {
		return timeLastUpdated;
	}

	public double getInitialAcceleration() {
		return initialAcceleration;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}