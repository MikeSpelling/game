package utils;

import game.Game;

import java.applet.AudioClip;
import java.util.ArrayList;

import models.Ball;


/**
 * Utility class to store boundaries and detect collisions
 * of balls between each other and the boundaries
 *
 * @author Mike
 *
 */
public class CollisionDetector {

	private Boundary topBoundary;
	private Boundary bottomBoundary;
	private Boundary leftBoundary;
	private Boundary rightBoundary;

	private final BallUtils ballUtils;
	private AudioClip bounceAudio;
	private AudioClip collideAudio;


	public CollisionDetector(double top, double energyLossTop, double bottom, double energyLossBottom,
			double left, double energyLossLeft, double right, double energyLossRight,
			BallUtils ballUtils, AudioClip bounceAudio, AudioClip collideAudio) {

		this.topBoundary = new Boundary(top, energyLossTop);
		this.bottomBoundary = new Boundary(bottom, energyLossBottom);
		this.leftBoundary = new Boundary(left, energyLossLeft);
		this.rightBoundary = new Boundary(right, energyLossRight);

		this.ballUtils = ballUtils;
		this.bounceAudio = bounceAudio;
		this.collideAudio = collideAudio;
	}

	public void detectCollisionsAndBoundary(ArrayList<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);

			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ballUtils.contains(ball, otherBall)) {
					ballUtils.collide(ball, otherBall);
				}
			}
			detectBoundary(ball);
		}
	}

	public void detectCollisions(ArrayList<Ball> balls) {
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);

			// Detect collisions with other balls
			for (int k = i+1; k < balls.size(); k++) {
				Ball otherBall = balls.get(k);
				if (ballUtils.contains(ball, otherBall)) {
					ballUtils.collide(ball, otherBall);
					if(!Game.MUTED) collideAudio.play();
				}
			}
		}
	}

	public void detectBoundary(Ball ball) {
		double radius = ball.getRadius();
		double rightCalibrated = rightBoundary.position - radius;
		double leftCalibrated = leftBoundary.position + radius;
		double topCalibrated = topBoundary.position + radius;
		double bottomCalibrated = bottomBoundary.position - radius;

		if (ball.getX() > rightCalibrated) {
			ballUtils.bounceX(ball, rightCalibrated, rightBoundary.energyLoss);
			if(!Game.MUTED) bounceAudio.play();
		}
		if (ball.getX() < leftCalibrated) {
			ballUtils.bounceX(ball, leftCalibrated, leftBoundary.energyLoss);
			if(!Game.MUTED) bounceAudio.play();
		}
		if (ball.getY() > bottomCalibrated) {
			ballUtils.bounceY(ball, bottomCalibrated, bottomBoundary.energyLoss);
			if(!Game.MUTED) bounceAudio.play();
		}
		if (ball.getY() < topCalibrated) {
			ballUtils.bounceY(ball, topCalibrated, topBoundary.energyLoss);
			if(!Game.MUTED) bounceAudio.play();
		}

		// Check hasnt bounced back past another boundary
		if (ball.getX() > rightCalibrated) ball.setX(rightCalibrated);
		else if (ball.getX() < leftCalibrated) ball.setX(leftCalibrated);
		if (ball.getY() > bottomCalibrated) ball.setY(bottomCalibrated);
		else if (ball.getY() < topCalibrated) ball.setY(topCalibrated);
	}

	private class Boundary {

		public double position;
		public double energyLoss;

		public Boundary(double position, double energyLoss) {
			this.position = position;
			this.energyLoss = energyLoss;
		}
	}

}