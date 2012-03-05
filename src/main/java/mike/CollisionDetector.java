package mike;

import java.util.List;

public class CollisionDetector {
	private int top;
	private int bottom;
	private int left;
	private int right;
	private double energyLossThroughBounce;
	
	public CollisionDetector(int top, int bottom, int left, int right, double energyLossThroughBounce) {
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.energyLossThroughBounce = energyLossThroughBounce;
	}
	
	public void detectCollisions(List<Ball> balls) {
		for (Ball ball : balls) {
			detectBoundary(ball);
		}
		
	}
	
	private void detectBoundary(Ball ball) {
		if (ball.x > right) {
			ball.x = right;
			ball.getMotionX().bounce(energyLossThroughBounce, ball.x, right);
		}
		else if (ball.x < left) {
			ball.x = left;
			ball.getMotionX().bounce(energyLossThroughBounce, ball.x, left);
		}
		if (ball.y > bottom) {
			ball.y = bottom;
			ball.getMotionY().bounce(energyLossThroughBounce, ball.y, bottom);
		}
		else if (ball.y < top) {
			ball.y = top;
			ball.getMotionY().bounce(energyLossThroughBounce, ball.y, top);
		}
	}

}
