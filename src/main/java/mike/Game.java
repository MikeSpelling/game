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
	
	private int widthPx = 800;
	private int heightPx = 600;
	
	private List<Ball> balls = new ArrayList<Ball>();;
	
	private CollisionDetector collisionDetector;
	
	private AudioClip bounceAudio;
	
	private Image bufferImage;
	private Image backgroundImage;
	private Graphics bufferGraphics;
	
	public final static boolean DEBUG = true;
	

	// Method is called the first time you enter the HTML site with the applet
	public void init() {
		// Defaults
		int radius = 15;
		double energyLossThroughBounce = 0.7;
		double heightMetres = 10;
		double initialVelocityX = 0;
		double initialVelocityY = 0;
		double accelerationX = 0;
		double accelerationY = accelerationDueToGravity;
		int numBalls = 3;
		
		// Load parameters
		String paramRadius = this.getParameter("Radius");
		String paramWidthPx = this.getParameter("WidthPx");
		String paramHeightPx = this.getParameter("HeightPx");
		String paramHeightMetres = this.getParameter("HeightMetres");
		String paramInitialVelocityX = this.getParameter("InitialVelocityX");
		String paramInitialVelocityY = this.getParameter("InitialVelocityY");
		String paramAccelerationX = this.getParameter("AccelerationX");
		String paramAccelerationY = this.getParameter("AccelerationY");
		String paramNumBalls = this.getParameter("NumBalls");
		if (paramRadius != null) radius = Integer.parseInt(paramRadius);
		if (paramWidthPx != null) widthPx = Integer.parseInt(paramWidthPx);
		if (paramHeightPx != null) heightPx = Integer.parseInt(paramHeightPx);
		if (paramHeightMetres != null) heightMetres = Integer.parseInt(paramHeightMetres);
		if (paramInitialVelocityX != null) initialVelocityX = Integer.parseInt(paramInitialVelocityX);
		if (paramInitialVelocityY != null) initialVelocityY = Integer.parseInt(paramInitialVelocityY);
		if (paramAccelerationX != null) accelerationX = Integer.parseInt(paramAccelerationX);
		if (paramAccelerationY != null) accelerationX = Integer.parseInt(paramAccelerationY);
		if (paramNumBalls != null) numBalls = Integer.parseInt(paramNumBalls);

		// Get files
		bounceAudio = getAudioClip(getCodeBase(), "bounce.au");
		backgroundImage = getImage(getCodeBase(), "land.GIF");
		
		bufferImage = createImage(widthPx, heightPx);
		bufferGraphics = bufferImage.getGraphics();
		
		// Create boundary detector
		int boundaryTop = radius;
		int boundaryBottom = heightPx-radius;
		int boundaryLeft = radius;
		int boundaryRight = widthPx-radius;
		collisionDetector = new CollisionDetector(boundaryTop, boundaryBottom, boundaryLeft, boundaryRight, 
				energyLossThroughBounce);
		
		// Calculate positioning
		double pxToMetres = heightMetres/(double)heightPx;		
		int xSpacing = (widthPx/numBalls);
		int x = (widthPx/numBalls)/2;
		int y = radius + (heightPx/4);
			
		// Create balls
		for (int i = 0; i < numBalls; i++) {
			balls.add(new Ball(x + (xSpacing*i), y, radius,
					new MotionX(x + (xSpacing*i), initialVelocityX, accelerationX, pxToMetres, bounceAudio), 
					new MotionY(y, initialVelocityY, accelerationY, pxToMetres, bounceAudio)));
		}
		
		// Start game
		Thread thread = new Thread(this);
		thread.start();
	}
	
	// Overidden run method for thread
	public void run() {
		while(true) {
			repaint(); // Calls update, then paint
			
			try { Thread.sleep(1);	}
			catch (InterruptedException ex) {}
			
			for (Ball ball : balls) {
				ball.x = ball.getMotionX().getNewPos();
				ball.y = ball.getMotionY().getNewPos();
			}
			collisionDetector.detectCollisions(balls);
			
		}
	}
	
	// Called before paint with repaint()
	public void update(Graphics screenGraphics) {
		bufferGraphics.setColor(Color.RED);
		paint(bufferGraphics);

		// Draw image on the screen
		screenGraphics.drawImage(bufferImage, 0, 0, this);
	}
	
	public void paint (Graphics buffer) {
		buffer.drawImage(backgroundImage, 0, 0, widthPx, heightPx, this);
		for (Ball ball : balls) {
			buffer.fillOval (ball.x - ball.getRadius(), ball.y - ball.getRadius(), 
					2 * ball.getRadius(), 2 * ball.getRadius());
		}
	}
	
	public boolean mouseDown (Event e, int x, int y) {
		for (Ball ball : balls) {
			ball.getMotionX().applyForce(ball.x, x);
			ball.getMotionY().applyForce(ball.y, y);
		}
		return true; // Have to return something
	}
	
	public boolean keyDown (Event e, int key) {
		if (key == Event.LEFT) {
			
		}
		else if (key == Event.RIGHT) {

		}	
		else if (key == 32) { // Space bar
			
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