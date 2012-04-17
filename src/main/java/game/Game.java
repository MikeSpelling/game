package game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import models.Ball;
import models.Motion;
import utils.BallUtils;
import utils.CollisionDetector;
import utils.MotionUtils;


/**
 * Colliding balls
 * Friction for x?
 * Decide what the game should be - scroll background in x direction?
 * Allow objects such as planks?
 * @author spellm01
 *
 */
public class Game extends Applet implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private static final long serialVersionUID = -3248452394993145828L;

	public final static double ACCELERATION_DUE_TO_GRAVITY = 9.80665;
	public final static boolean DEBUG = false;
	public static boolean MUTED = true;
	public static boolean CIRCULAR = false;

	private int widthPx;
	private int heightPx;
	private double metresToPx;
	private Point2D midpoint;

	private final MotionUtils motionUtils = new MotionUtils();
	private final BallUtils ballUtils = new BallUtils(motionUtils);
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
		double radius = 25;
		double energyLossTop = 1;
		double energyLossBottom = 1;
		double energyLossLeft = 1;
		double energyLossRight = 1;
		double energyLossCollision = 1;
		double heightMetres = 10;
		double initialVelocityX = 0;
		double initialVelocityY = 0;
		double accelerationX = 0;
		double accelerationY = ACCELERATION_DUE_TO_GRAVITY;

		// Load parameters
		heightPx = getHeight();
		widthPx = getWidth();
		String paramNumBalls = this.getParameter("NumBalls");
		String paramRadius = this.getParameter("Radius");
		String paramHeightMetres = this.getParameter("HeightMetres");
		String paramInitialVelocityX = this.getParameter("InitialVelocityX");
		String paramInitialVelocityY = this.getParameter("InitialVelocityY");
		String paramAccelerationX = this.getParameter("AccelerationX");
		String paramAccelerationY = this.getParameter("AccelerationY");
		String paramEnergyLossLeft = this.getParameter("EnergyLossLeft");
		String paramEnergyLossRight = this.getParameter("EnergyLossRight");
		String paramEnergyLossTop = this.getParameter("EnergyLossTop");
		String paramEnergyLossBottom = this.getParameter("EnergyLossBottom");
		String paramEnergyLossCollision = this.getParameter("EnergyLossCollision");
		String paramCircular = this.getParameter("Circular");
		if (paramNumBalls != null) numBalls = Integer.parseInt(paramNumBalls);
		if (paramRadius != null) radius = Double.parseDouble(paramRadius);
		if (paramHeightMetres != null) heightMetres = Double.parseDouble(paramHeightMetres);
		if (paramInitialVelocityX != null) initialVelocityX = Double.parseDouble(paramInitialVelocityX);
		if (paramInitialVelocityY != null) initialVelocityY = Double.parseDouble(paramInitialVelocityY);
		if (paramAccelerationX != null) accelerationX = Double.parseDouble(paramAccelerationX);
		if (paramAccelerationY != null) accelerationY = Double.parseDouble(paramAccelerationY);
		if (paramEnergyLossLeft != null) energyLossLeft = Double.parseDouble(paramEnergyLossLeft);
		if (paramEnergyLossRight != null) energyLossRight = Double.parseDouble(paramEnergyLossRight);
		if (paramEnergyLossTop != null) energyLossTop = Double.parseDouble(paramEnergyLossTop);
		if (paramEnergyLossBottom != null) energyLossBottom = Double.parseDouble(paramEnergyLossBottom);
		if (paramEnergyLossCollision != null) energyLossCollision = Double.parseDouble(paramEnergyLossCollision);
		if (paramCircular != null) CIRCULAR = Boolean.parseBoolean(paramCircular);

		metresToPx = (double)heightPx/heightMetres;
		double widthMetres = widthPx*(1/metresToPx);
		midpoint = new Point.Double((widthPx*(1/metresToPx))/2, (heightPx*(1/metresToPx))/2);

		// Get files
		AudioClip bounceAudio = getAudioClip(getCodeBase(), "bounce.au");
		backgroundImage = getImage(getCodeBase(), "land.GIF");

		collisionDetector = new CollisionDetector(0, energyLossTop, heightMetres, energyLossBottom,
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
				double mass = newRadius/10;
				double xPos = x + (xSpacing*i);

				balls.add(new Ball(xPos*(1/metresToPx), y*(1/metresToPx), newRadius*(1/metresToPx), mass, color,
						new Motion(initialVelocityX, accelerationX),
						new Motion(initialVelocityY, accelerationY),
						energyLossCollision));
			}
		}

		// Start game
		Thread thread = new Thread(this);
		thread.start();
	}

	// Overridden run method for thread
	public void run() {
		for (Ball ball : balls) {
			ballUtils.startMotion(ball);
		}
		while(true) {
			repaint(); // Calls update, then paint

			try { Thread.sleep(1);	}
			catch (InterruptedException ex) {}

			for (Ball ball : balls) {
				if(Game.CIRCULAR)	ballUtils.applyAccelerationTowards(ball, midpoint);
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
				ballUtils.startMotion(ball);
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			for (Ball ball : balls) {
				ballUtils.stopMotion(ball);
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
		if (e.getButton() == MouseEvent.BUTTON1) {
			double x = e.getX()*(1/metresToPx);
			double y = e.getY()*(1/metresToPx);
			for (Ball ball : balls) {
				motionUtils.applyForce(ball.getMotionX(), (x-ball.getX())/(widthPx*(1/metresToPx)), ball.getMass());
				motionUtils.applyForce(ball.getMotionY(), (y-ball.getY())/(heightPx*(1/metresToPx)), ball.getMass());
				//break; // REMOVE to affect all balls
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3){
			double x = e.getX()*(1/metresToPx);
			double y = e.getY()*(1/metresToPx);
			double radius = 10 + (((float)e.getX()/(float)widthPx)*20);
			Color color = new Color((float)e.getX()/(float)widthPx, (float)e.getX()/(float)widthPx, (float)e.getY()/(float)heightPx);
			Ball ball = new Ball(x, y, radius*(1/metresToPx), radius/10, color, new Motion(0, 0), new Motion(0, ACCELERATION_DUE_TO_GRAVITY), 1);
			ballUtils.startMotion(ball);
			balls.add(ball);
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
