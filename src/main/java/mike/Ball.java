package mike;

public class Ball {
	
	private final MotionX motionX;	
	private final MotionY motionY;
	private final int radius;
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

	public MotionX getMotionX() {
		return motionX;
	}

	public MotionY getMotionY() {
		return motionY;
	}

	public int getRadius() {
		return radius;
	}
	
}