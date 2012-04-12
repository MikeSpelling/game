package mike;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;


/**
 * Colliding balls
 * Create single x/y point of reference between ball/motion. Pass displacment into motion methods.
 * Friction for x?
 * Tests
 * Circular gravity mode? Decide what the game should be - scroll background in x direction?
 * Allow objects such as planks?
 * @author spellm01
 *
 */
public class Game extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = -3248452394993145828L;

	public static final double accelerationDueToGravity = 9.80665;
	public final static boolean DEBUG = false;

	private int widthPx;
	private int heightPx;
	
	private ArrayList<Ball> balls;
	private CollisionDetector collisionDetector;

	private Image bufferImage;
	private Image backgroundImage;
	private Graphics bufferGraphics;


	// Method is called the first time you enter the HTML site with the applet
	public void init() {
		
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		// Defaults
		int numBalls = 1;
		int radius = 20;
		double energyLossTop = 1;
		double energyLossBottom = 1;
		double energyLossLeft = 1;
		double energyLossRight = 1;
		double heightMetres = 10;
		double initialVelocityX = 0;
		double initialVelocityY = 0;
		double accelerationX = 0;
		double accelerationY = accelerationDueToGravity;

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
		collisionDetector =	new CollisionDetector(0, energyLossTop, heightMetres, energyLossBottom, 
				0, energyLossLeft, widthPx*pxToMetres, energyLossRight);

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
			balls = new ArrayList<Ball>();
			for (int i = 0; i < numBalls; i++) {
				Color color = new Color((float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls));
				int newRadius = (radius*(1+i))/numBalls;
				double mass = (double)(i+1)/10;
				int xPos = x + (xSpacing*i);
				double energyLoss = 1;

				balls.add(new Ball(xPos, y, newRadius, mass, color, 
						new MotionX(xPos, initialVelocityX, accelerationX, pxToMetres, bounceAudio),
						new MotionY(y, initialVelocityY, accelerationY, pxToMetres, bounceAudio),
						energyLoss));
			}
		}

		// Start game
		Thread thread = new Thread(this);
		thread.start();
	}

	// Overridden run method for thread
	public void run() {
		for (Ball ball : balls) {
			ball.getMotionX().startMotion();
			ball.getMotionY().startMotion();
		}
		while(true) {
			repaint(); // Calls update, then paint

			try { Thread.sleep(1);	}
			catch (InterruptedException ex) {}

			for (Ball ball : balls) {
				ball.updatePosition();
			}
			collisionDetector.detectCollisionsAndBoundary(balls);
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
			ball.paintBall(buffer);
		}
	}

	public boolean mouseDown (Event e, int x, int y) {
		for (Ball ball : balls) {
			ball.getMotionX().applyForce((double)(x-ball.getX())/widthPx, ball.getMass());
			ball.getMotionY().applyForce((double)(y-ball.getY())/heightPx, ball.getMass());
			//break; // REMOVE to affect all balls
		}
		return true; // Have to return something
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

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) { // Space bar
			for (Ball ball : balls) {
				ball.getMotionX().stopMotion();
				ball.getMotionY().stopMotion();
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			for (Ball ball : balls) {
				ball.getMotionX().startMotion();
				ball.getMotionY().startMotion();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		for (Ball ball : balls) {
			ball.getMotionX().applyForce((double)(x-ball.getX())/widthPx, ball.getMass());
			ball.getMotionY().applyForce((double)(y-ball.getY())/heightPx, ball.getMass());
			//break; // REMOVE to affect all balls
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		
	}

}
