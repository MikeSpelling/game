package mike;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.List;

public class Balls {

	public List<Ball> balls;
	public double energyLossCollision;
	
	
	// ------- Constructors
	public Balls() {
		this.balls = new ArrayList<Ball>();
		this.energyLossCollision = 1;
	}	
	
	public Balls(double energyLossCollision) {
		this.balls = new ArrayList<Ball>();
		this.energyLossCollision = energyLossCollision;
	}	

	public Balls(List<Ball> balls, double energyLossCollision) {
		this.balls = balls;
		this.energyLossCollision = energyLossCollision;
	}
	
	// ------- Methods
	public void startMotion() {
		for (Ball ball : balls) {
			ball.getMotionX().startMotion();
			ball.getMotionY().startMotion();
		}
	}
	
	public void stopMotion() {
		for (Ball ball : balls) {
			ball.getMotionX().stopMotion();
			ball.getMotionY().stopMotion();
		}
	}
	
	public void applyForce(int x, int width, int y, int height) {
		for (Ball ball : balls) {
			ball.getMotionX().applyForce((double)(x-ball.x)/width, ball.mass);
			ball.getMotionY().applyForce((double)(y-ball.y)/height, ball.mass);
			//break; // REMOVE to affect all balls
		}
	}
	
	public void updatePosition() {
		for (Ball ball : balls) {
			ball.updatePosition();
		}
	}
	
	/**
	 * Detects if any ball currently in a position which
	 * collides with another ball or a boundary.
	 * Does not take into account the path of the ball
	 * which may have crossed another, if both were moving fast
	 * enough.
	 *
	 * @param balls
	 */
	public void detectCollisions() {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ball.contains(otherBall)) {
					ball.collide(otherBall, energyLossCollision);
				}
			}
		}
	}
	
	public void paintBalls(Graphics buffer) {
		for (Ball ball : balls) {
			buffer.setColor(ball.color);
			buffer.fillOval (ball.x - ball.radius, ball.y - ball.radius,
					2 * ball.radius, 2 * ball.radius);
		}
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public double getEnergyLossCollision() {
		return energyLossCollision;
	}

	public void setEnergyLossCollision(double energyLossCollision) {
		this.energyLossCollision = energyLossCollision;
	}
	
	public void add(Ball ball) {
		balls.add(ball);
	}
	
	public void add(int i, Ball ball) {
		balls.add(i, ball);
	}
	
	public void remove(int i) {
		balls.remove(i);
	}
	
	public Ball get(int i) {
		return balls.get(i);
	}
	
	public void clear() {
		balls.clear();
	}
	
	public boolean isEmpty() {
		return balls.isEmpty();
	}
	
	public int size() {
		return balls.size();
	}
}