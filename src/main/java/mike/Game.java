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
 * Simplify SUVATs
 * Fix energylossthroughbounce at 1
 * Fix when to stop moving - need mass/friction?
 * Collision detect balls
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

	private List<Ball> balls = new ArrayList<Ball>();;

	private CollisionDetector collisionDetector;

	private Image bufferImage;
	private Image backgroundImage;
	private Graphics bufferGraphics;


	// Method is called the first time you enter the HTML site with the applet
	public void init() {
		// Defaults
		int radius = 15;
		double energyLossTop = 0.7;
		double energyLossBottom = 0.7;
		double energyLossLeft = 0.7;
		double energyLossRight = 0.7;
		double energyLossCollision = 0.85;
		double heightMetres = 10;
		double initialVelocityX = 0;
		double initialVelocityY = 0;
		double accelerationX = 0;
		double accelerationY = accelerationDueToGravity;
		int numBalls = 1;

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

		// Create boundary detector
		collisionDetector = new CollisionDetector(0, energyLossTop, heightPx, energyLossBottom,
				0, energyLossLeft, widthPx, energyLossRight, energyLossCollision, pxToMetres);

		if (numBalls > 0) {
			// Calculate positioning
			int xSpacing = (widthPx/numBalls);
			int x = (widthPx/numBalls)/2;
			int y = radius + (heightPx/4);

			// Create balls
			for (int i = 0; i < numBalls; i++) {
				float r, b, g; r = g = b = (float)(i)/(float)(numBalls);
				int newRadius = (radius*(1+i))/numBalls;
				balls.add(new Ball(x + (xSpacing*i), y, newRadius, new Color(r, g, b),
						new MotionX(x + (xSpacing*i), initialVelocityX, accelerationX, pxToMetres, bounceAudio),
						new MotionY(y, initialVelocityY, accelerationY, pxToMetres, bounceAudio)));
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
			collisionDetector.detectCollisions(balls);
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

	public boolean mouseDown (Event e, int x, int y) {
		for (Ball ball : balls) {
			ball.applyForce(x, y);
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