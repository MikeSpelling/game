package mike;

import static org.junit.Assert.*;

import java.applet.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class MotionXTest {

	@Mock AudioClip bounceAudio;
	double pxToMetres = 1;

	@Before
	public void before() throws Exception {
	}

	@Test
	public void testNoMovement() throws Exception {

		Motion motionX = new MotionX(100, 0, 0, pxToMetres, bounceAudio);
		motionX.startMotion();

		Thread.sleep(1000);
		motionX.updatePosition();

		assertEquals("Displacement", 100, motionX.getDisplacement(), 0);
		assertEquals("Velocity", 0, motionX.getVelocity(), 0);
		assertEquals("Acceleration", 0, motionX.getAcceleration(), 0);
	}

	@Test
	public void testUpdatePositionVelocity() throws Exception {

		int position = 0;
		double velocity = 10;
		double acceleration = 0;

		Motion motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updatePosition();
		double dt = (System.nanoTime() - startTime)*0.000000001;
		// No acceleration therefore distance = velocity * time
		double displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(1000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(3000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		displacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);
	}

	@Test
	public void testUpdatePositionAcceleration() throws Exception {

		int position = 0;
		double velocity = 0;
		double acceleration = 10;

		Motion motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updatePosition();
		double dt = (System.nanoTime() - startTime)*0.000000001;
		velocity = dt*acceleration;
		double displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(1000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(3000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		velocity = dt*acceleration;
		displacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);
	}

	@Test
	public void testUpdatePosition() throws Exception {

		int position = 100;
		double initVelocity = -20;
		double acceleration = 10;

		Motion motionX = new MotionX(position, initVelocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		motionX.updatePosition();
		double dt = (System.nanoTime() - startTime)*0.000000001;
		// V = U + aT
		double velocity = initVelocity + (acceleration*dt);
		// Add S to original position: S = UT + (aT^2)/2
		double displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(1000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);

		Thread.sleep(3000);
		motionX.updatePosition();
		dt = (System.nanoTime() - startTime)*0.000000001;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), 0.1);
		assertEquals("Displacement", displacement, motionX.getDisplacement(), 0.01);
	}

	@Test
	public void testBounce() throws Exception {
		int boundary = 100;
		int position = 105;
		int velocity = 10;
		int acceleration = 10;

		Motion motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motionX.startMotion();
		long startTime = System.nanoTime();

		motionX.bounce(1, boundary);
		double dt = System.nanoTime()-startTime;
		System.out.println("displacement: " + motionX.getDisplacement() +
				", velocity: " + motionX.getVelocity());
	}

}