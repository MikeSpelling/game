package mike;

import static org.junit.Assert.*;

import java.applet.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * Tests methods contained in Abstract class Motion,
 * implementation uses MotionX
 * 
 * @author Mike
 *
 */
public class MotionTest {
	
	double errorMargin = 0.01;
	double nanoToSeconds = 0.000000001;
	double pxToMetres = 1;
	
	@Mock AudioClip bounceAudio;

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testNoMovement() throws Exception {

		MotionX motionX = new MotionX(100, 0, 0, pxToMetres, bounceAudio);
		motionX.startMotion();

		Thread.sleep(1000);
		motionX.updateDisplacement();

		assertEquals("Displacement", 100, motionX.getDisplacement(), 0);
		assertEquals("Velocity", 0, motionX.getVelocity(), 0);
		assertEquals("Acceleration", 0, motionX.getAcceleration(), 0);
	}

	@Test
	public void testUpdatePositionVelocity() throws Exception {

		int position = 0;
		double velocity = 10;
		double acceleration = 0;

		MotionX motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// No acceleration therefore distance = velocity * time
		double displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);
	}

	@Test
	public void testUpdatePositionAcceleration() throws Exception {

		int position = 0;
		double velocity = 0;
		double acceleration = 10;

		MotionX motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		double displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);
	}

	@Test
	public void testUpdatePosition() throws Exception {

		int position = 100;
		double initVelocity = -20;
		double acceleration = 10;

		MotionX motionX = new MotionX(position, initVelocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// V = U + aT
		double velocity = initVelocity + (acceleration*dt);
		// Add S to original position: S = UT + (aT^2)/2
		double displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionX.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), errorMargin);
	}

	@Test
	public void testBounceRight() throws Exception {
		int boundary = 100;
		int position = 102;
		int velocity = 10;
		int acceleration = 10;

		MotionX motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();

		motionX.bounce(boundary, 1);
		
		assertEquals("Velocity", -5.49, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", 98.51, motionX.getDisplacement(), errorMargin);
	}
	
	@Test
	public void testBounceLeft() throws Exception {
		int boundary = 5;
		int position = 2;
		int velocity = -10;
		int acceleration = 10;

		MotionX motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();

		motionX.bounce(boundary, 1);
		
		assertEquals("Velocity", 15.30, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", 8.70, motionX.getDisplacement(), errorMargin);
	}

}