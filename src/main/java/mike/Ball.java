package mike;

public class Ball {
	
	private final MotionX motionX;	
	private final MotionY motionY;
	public final int radius;
	public int x;
	public int y;
	
	public Ball(int x, int y, int radius, MotionX motionX, MotionY motionY) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.motionX = motionX;
		this.motionY = motionY;
		if(Game.DEBUG)
			System.out.println("Ball created with x: " + x + ", y: " + y + ", radius: " + radius);
	}
	
	public void startMotion() {
		motionX.startMotion();
		motionY.startMotion();
	}
	
	public void stopMotion() {
		motionX.stopMotion();
		motionY.stopMotion();
	}

	public void applyForce(int clickedX, int clickedY) {
		motionX.applyForce(clickedX-x);
		motionY.applyForce(clickedY-y);
	}
	
	public void updatePosition() {
		x = motionX.updatePosition();
		y = motionY.updatePosition();
	}

	public void collideX(double energyLossThroughBounce, int boundary) {
		motionX.bounce(energyLossThroughBounce, boundary);
	}

	public void collideY(double energyLossThroughBounce, int boundary) {
		motionY.bounce(energyLossThroughBounce, boundary);
	}
	
}