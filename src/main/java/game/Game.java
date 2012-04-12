package game;

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

import utils.BallUtils;
import utils.CollisionDetector;
import utils.MotionX;
import utils.MotionY;

import models.Ball;


/**
 * Colliding balls
 * Friction for x?
 * Circular gravity mode? Decide what the game should be - scroll background in x direction?
 * Allow objects such as planks?
 * @author spellm01
 *
 */
public class Game extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = -3248452394993145828L;

	public final static double accelerationDueToGravity = 9.80665;
	public final static boolean DEBUG = false;
	public final static boolean muted = true;

	private int widthPx;
	private int heightPx;
	private double metresToPx;
	
	private BallUtils ballUtils = new BallUtils();
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
		int numBalls = 3;
		double radius = 20;
		double energyLossTop = 0.8;
		double energyLossBottom = 0.8;
		double energyLossLeft = 0.8;
		double energyLossRight = 0.8;
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
		if (paramRadius != null) radius = Double.parseDouble(paramRadius);
		if (paramHeightMetres != null) heightMetres = Double.parseDouble(paramHeightMetres);
		if (paramInitialVelocityX != null) initialVelocityX = Double.parseDouble(paramInitialVelocityX);
		if (paramInitialVelocityY != null) initialVelocityY = Double.parseDouble(paramInitialVelocityY);
		if (paramAccelerationX != null) accelerationX = Double.parseDouble(paramAccelerationX);
		if (paramAccelerationY != null) accelerationY = Double.parseDouble(paramAccelerationY);
		if (paramNumBalls != null) numBalls = Integer.parseInt(paramNumBalls);
		
		metresToPx = (double)heightPx/heightMetres;
		double widthMetres = widthPx*(1/metresToPx);
		
		// Get files
		AudioClip bounceAudio = getAudioClip(getCodeBase(), "bounce.au");
		backgroundImage = getImage(getCodeBase(), "land.GIF");
		
		collisionDetector =	new CollisionDetector(0, energyLossTop, heightMetres, energyLossBottom, 
				0, energyLossLeft, widthMetres, energyLossRight, ballUtils, bounceAudio, bounceAudio);

		bufferImage = createImage(widthPx, heightPx);
		bufferGraphics = bufferImage.getGraphics();

		if (numBalls > 0) {
			// Calculate positioning
			double xSpacing = (widthPx/numBalls);
			double x = (widthPx/numBalls)/2;
			double y = radius + (heightPx/4);
			
			// Create balls
			balls = new ArrayList<Ball>();
			for (int i = 0; i < numBalls; i++) {
				Color color = new Color((float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls), (float)(i)/(float)(numBalls));
				double newRadius = (radius*(1+i))/numBalls;
				double mass = (double)(i+1)/10;
				double xPos = x + (xSpacing*i);
				double energyLoss = 1;

				balls.add(new Ball(xPos*(1/metresToPx), y*(1/metresToPx), newRadius*(1/metresToPx), mass, color, 
						new MotionX(initialVelocityX, accelerationX),
						new MotionY(initialVelocityY, accelerationY),
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
				ballUtils.updatePosition(ball);
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
			ballUtils.paintBall(ball, buffer, metresToPx);
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
		double x = e.getX()*(1/metresToPx);
		double y = e.getY()*(1/metresToPx);
		for (Ball ball : balls) {
			ball.getMotionX().applyForce((x-ball.getX())/(widthPx*(1/metresToPx)), ball.getMass());
			ball.getMotionY().applyForce((y-ball.getY())/(heightPx*(1/metresToPx)), ball.getMass());
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