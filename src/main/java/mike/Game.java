package mike;

import java.applet.Applet;
import java.applet.AudioClip;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

import mike.Motion.Status;



/**
 * Fix energylossthroughbounce at 1
 * Circular gravity mode? Decide what the game should be - scroll background in x direction?
 * Create Ball class to allow multiple balls - Objects such as planks?
 * Abstract Motion - create MotionX and MotionY children if want to use gravity and friction
 * Fix when to stop moving
 * @author spellm01
 *
 */
public class Game extends Applet implements Runnable {

	private static final long serialVersionUID = -3248452394993145828L;

	private final double accelerationDueToGravity = 9.80665;
	
	private final int widthPx = 800;
	private final int heightPx = 600;
	private final double heightMetres = 8;
	private final double initVelocityX = 0;
	private final double initVelocityY = 0;
	private final double accelerationX = 0;
	private final double accelerationY = accelerationDueToGravity;
	private final double energyLossThroughBounce = 0.7;
	private final double friction = 0.1;
	private final double pxToMetres = heightMetres/(double)heightPx;
	
	private final int radius = 20;
	private int xPos = radius + (widthPx/2);
	private int yPos = radius + (heightPx/10);
	
	private final Boundary boundary = new Boundary(heightPx-radius, radius, radius, widthPx-radius);
	
	private Motion motionX;	
	private Motion motionY;	
	
	private AudioClip bounceAudio;
	
	private Image bufferImage;
	private Image backgroundImage;
	private Graphics bufferGraphics;
	
	public final static boolean DEBUG = true;
	

	// init - method is called the first time you enter the HTML site with the applet
	public void init() {
		if(DEBUG)
			System.out.println("widthPx: " + widthPx + ", heightPx: " + heightPx + ", heightMetres: " + heightMetres + ", energyLoss: " + energyLossThroughBounce);
		bounceAudio = getAudioClip(getCodeBase(), "bounce.au");
		backgroundImage = getImage(getCodeBase(), "land.GIF");
		
		bufferImage = createImage(widthPx, heightPx);
		bufferGraphics = bufferImage.getGraphics();
		
		motionX = new Motion(xPos, initVelocityX, accelerationX, pxToMetres, bounceAudio);
		motionY = new Motion(yPos, initVelocityY, accelerationY, pxToMetres, bounceAudio);
			
		if(DEBUG)
			System.out.println("xPos: " + xPos + ", yPos: " + yPos + ", radius: " + radius);
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	// start - method is called every time you enter the HTML - site with the applet
	public void start() {
		
	}
	
	// stop - method is called if you leave the site with the applet
	public void stop() {
		
	}
	
	// destroy method is called if you leave the page finally (e. g. closing browser)
	public void destroy() {
		
	}
	
	public void update(Graphics screenGraphics) {
		bufferGraphics.setColor(Color.RED);
		paint(bufferGraphics);

		// Draw image on the screen
		screenGraphics.drawImage(bufferImage, 0, 0, this);
	}
	
	/** paint - method allows you to paint into your applet. This method is called e.g. if you move your browser window or if you call repaint() 
	 * @param yPos */
	public void paint (Graphics buffer) {
		buffer.drawImage(backgroundImage, 0, 0, widthPx, heightPx, this);
		buffer.fillOval (xPos - radius, yPos - radius, 2 * radius, 2 * radius);
	}
	
	// Overidden run method for thread
	public void run() {
		while(true) {
			repaint(); // Calls update, then paint

			try { Thread.sleep(1);	}
			catch (InterruptedException ex) {}
			
			yPos = motionY.getPos();
			if(motionY.status == Status.MOVING)
				xPos = motionX.getPos();
			else
				xPos = motionX.getPosWithFriction(xPos, friction);
			
			detectBoundary();
		}
	}

	private void detectBoundary() {
		if (xPos > boundary.RIGHT) {
			xPos = boundary.RIGHT;
			motionX.bounce(energyLossThroughBounce, xPos, boundary.RIGHT);
		}
		else if (xPos < boundary.LEFT) {
			xPos = boundary.LEFT;
			motionX.bounce(energyLossThroughBounce, xPos, boundary.LEFT);
		}
		if (yPos > boundary.BOTTOM) {
			yPos = boundary.BOTTOM;
			motionY.bounce(energyLossThroughBounce, yPos, boundary.BOTTOM);
		}
		else if (yPos < boundary.TOP) {
			yPos = boundary.TOP;
			motionY.bounce(energyLossThroughBounce, yPos, boundary.TOP);
		}
	}
	
	public boolean mouseDown (Event e, int x, int y) {
		motionX.move(xPos, x, accelerationX);
		motionY.move(yPos, y, accelerationY);

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
	
	private class Boundary {
		
		public int BOTTOM;
		public int TOP;
		public int LEFT;
		public int RIGHT;
		
		public Boundary(int bottom, int top, int left, int right) {
			this.BOTTOM = bottom;
			this.TOP = top;
			this.LEFT = left;
			this.RIGHT = right;
		}
	}
}
