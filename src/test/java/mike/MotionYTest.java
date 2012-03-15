package mike;

import static org.junit.Assert.*;

import java.applet.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class MotionYTest {
	
	double errorMargin = 0.01;
	double nanoToSeconds = 0.000000001;
	double pxToMetres = 1;
	
	@Mock AudioClip bounceAudio;

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testNoMovement() throws Exception {

		MotionY motionY = new MotionY(100, 0, 0, pxToMetres, bounceAudio);
		motionY.startMotion();

		Thread.sleep(1000);
		motionY.updateDisplacement();

		assertEquals("Displacement", 100, motionY.getDisplacement(), 0);
		assertEquals("Velocity", 0, motionY.getVelocity(), 0);
		assertEquals("Acceleration", 0, motionY.getAcceleration(), 0);
	}

	@Test
	public void testUpdatePositionVelocity() throws Exception {

		int position = 0;
		double velocity = 10;
		double acceleration = 0;

		MotionY motionY = new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionY.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionY.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// No acceleration therefore distance = velocity * time
		double displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);
	}

	@Test
	public void testUpdatePositionAcceleration() throws Exception {

		int position = 0;
		double velocity = 0;
		double acceleration = 10;

		MotionY motionY = new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionY.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionY.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		double displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);
	}

	@Test
	public void testUpdatePosition() throws Exception {

		int position = 100;
		double initVelocity = -20;
		double acceleration = 10;

		MotionY motionY = new MotionY(position, initVelocity, acceleration, pxToMetres, bounceAudio);
		motionY.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionY.updateDisplacement();
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// V = U + aT
		double velocity = initVelocity + (acceleration*dt);
		// Add S to original position: S = UT + (aT^2)/2
		double displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(1000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);

		Thread.sleep(3000);
		motionY.updateDisplacement();
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, motionY.getDisplacement(), errorMargin);
	}

	@Test
	public void testBounceRight() throws Exception {
		int boundary = 100;
		int position = 102;
		int velocity = 10;
		int acceleration = 10;

		MotionY motionY = new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionY.startMotion();

		motionY.bounce(boundary, 1);
		
		assertEquals("Velocity", -5.49, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", 98.51, motionY.getDisplacement(), errorMargin);
	}
	
	@Test
	public void testBounceLeft() throws Exception {
		int boundary = 5;
		int position = 2;
		int velocity = -10;
		int acceleration = 10;

		MotionY motionY = new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionY.startMotion();

		motionY.bounce(boundary, 1);
		
		assertEquals("Velocity", 15.30, motionY.getVelocity(), errorMargin);
		assertEquals("Displacement", 8.70, motionY.getDisplacement(), errorMargin);
	}

}