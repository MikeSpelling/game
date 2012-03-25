package mike;

import java.applet.Applet;
import java.applet.AudioClip;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;
import java.util.List;


/**
 * Colliding balls
 * Friction for x?
 * Tests
 * Circular gravity mode? Decide what the game should be - scroll background in x direction?
 * Allow objects such as planks?
 * @author spellm01
 *
 */
public class Game extends Applet implements Runnable {

	private static final long serialVersionUID = -3248452394993145828L;

	public static final double accelerationDueToGravity = 9.80665;
	public final static boolean DEBUG = false;

	private int widthPx;
	private int heightPx;

	private List<Ball> balls = new ArrayList<Ball>();
	double energyLossCollision = 0.85;

	private Image bufferImage;
	private Image backgroundImage;
	private Graphics bufferGraphics;


	// Method is called the first time you enter the HTML site with the applet
	public void init() {
		// Defaults
		int numBalls = 2;
		int radius = 20;
		double energyLossTop = 0.9;
		double energyLossBottom = 0.9;
		double energyLossLeft = 0.9;
		double energyLossRight = 0.9;
		energyLossCollision = 0.9;
		double heightMetres = 10;
		double initialVelocityX = 0;
		double initialVelocityY = 0;
		double accelerationX = 0;
		double accelerationY = 0;//accelerationDueToGravity;

		// Load parameters
		heightPx = getHeight();
		widthPx = getWidth();
		String paramRadius = this.getParameter("Radius");
		String paramHeightMetres = this.getParameter("HeightMetres");
		String paramInitialVelocityX = this.getParameter("InitialVelocityX");
		String paramInitialVelocityY = this.getParameter("InitialVelocityY");
		String paramAccelerationX = this.getParameter("AccelerationX");
		String paramAccelerationY = this.getParameter("AccelerationY");
		String paramNumBalls = this.getParameter("NumBalls");
		if (paramRadius != null) radius = Integer.parseInt(paramRadius);
		if (paramHeightMetres != null) heightMetres = Integer.parseInt(paramHeightMetres);
		if (paramInitialVelocityX != null) initialVelocityX = Integer.parseInt(paramInitialVelocityX);
		if (paramInitialVelocityY != null) initialVelocityY = Integer.parseInt(paramInitialVelocityY);
		if (paramAccelerationX != null) accelerationX = Integer.parseInt(paramAccelerationX);
		if (paramAccelerationY != null) accelerationX = Integer.parseInt(paramAccelerationY);
		if (paramNumBalls != null) numBalls = Integer.parseInt(paramNumBalls);

		double pxToMetres = heightMetres/(double)heightPx;

		// Get files
		AudioClip bounceAudio = getAudioClip(getCodeBase(), "bounce.au");
		backgroundImage = getImage(getCodeBase(), "land.GIF");

		bufferImage = createImage(widthPx, heightPx);
		bufferGraphics = bufferImage.getGraphics();

		if (numBalls > 0) {
			// Calculate positioning
			int xSpacing = (widthPx/numBalls);
			int x = (widthPx/numBalls)/2;
			int y = radius + (heightPx/4);

			// Create balls
			for (int i = 0; i < numBalls; i++) {
				Color color = new Color((float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls));
				int newRadius = (radius*(1+i))/numBalls;
				double mass = (double)(i+1)/10;
				int xPos = x + (xSpacing*i);
				CollisionDetector collisionDetectorX =
					new CollisionDetector(newRadius*pxToMetres, energyLossLeft, (widthPx-newRadius)*pxToMetres, energyLossRight);
				CollisionDetector collisionDetectorY =
					new CollisionDetector(newRadius*pxToMetres, energyLossTop, (heightPx-newRadius)*pxToMetres, energyLossBottom);

				balls.add(new Ball(xPos, y, initialVelocityX, initialVelocityY, accelerationX, accelerationY,
						newRadius, mass, pxToMetres, bounceAudio, color, collisionDetectorX, collisionDetectorY));
			}
		}

		// Start game
		Thread thread = new Thread(this);
		thread.start();
	}

	// Overridden run method for thread
	public void run() {
		for (Ball ball : balls) {
			ball.startMotion();
		}
		while(true) {
			repaint(); // Calls update, then paint

			try { Thread.sleep(1);	}
			catch (InterruptedException ex) {}

			for (Ball ball : balls) {
				ball.updatePosition();
			}
			detectCollisions();
		}
	}

	// Called before paint with repaint()
	public void update(Graphics screenGraphics) {
		paint(bufferGraphics);

		// Draw image on the screen
		screenGraphics.drawImage(bufferImage, 0, 0, this);
	}

	public void paint (Graphics buffer) {
		buffer.drawImage(backgroundImage, 0, 0, widthPx, heightPx, this);
		for (Ball ball : balls) {
			buffer.setColor(ball.color);
			buffer.fillOval (ball.x - ball.radius, ball.y - ball.radius,
					2 * ball.radius, 2 * ball.radius);
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
	private void detectCollisions() {
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

	public boolean mouseDown (Event e, int x, int y) {
		for (Ball ball : balls) {
			ball.applyForce(x, widthPx, y, heightPx);
			break; // REMOVE to affect all balls
		}
		return true; // Have to return something
	}

	public boolean keyDown (Event e, int key) {
		if (key == Event.LEFT) {

		}
		else if (key == Event.RIGHT) {

		}
		else if (key == 32) { // Space bar
			for (Ball ball : balls) {
				ball.stopMotion();
			}
		}
		return true;
	}

	// Method is called every time you enter the HTML - site with the applet
	public void start() {

	}

	// Method is called if you leave the site with the applet
	public void stop() {

	}

	// Method is called if you leave the page finally (e. g. closing browser)
	public void destroy() {

	}

}