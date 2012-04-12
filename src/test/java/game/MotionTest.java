package game;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import models.Ball;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import utils.MotionX;
import utils.MotionY;


/**
 * Tests methods contained in Abstract class Motion,
 * implementation uses MotionX
 * 
 * @author Mike
 *
 */
public class MotionTest {
	
	private double errorMargin = 0.01;
	private double nanoToSeconds = 0.000000001;
	
	@Mock private Ball ball1;
	@Mock private Ball ball2;

	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testNoMovement() throws Exception {

		MotionX motionX = new MotionX(0, 0);
		motionX.startMotion();

		Thread.sleep(1000);
		double actualDisplacement = motionX.updateDisplacement(100);

		assertEquals("Displacement", 100, actualDisplacement, 0);
		assertEquals("Velocity", 0, motionX.getVelocity(), 0);
		assertEquals("Acceleration", 0, motionX.getAcceleration(), 0);
	}

	@Test
	public void testUpdatePositionVelocity() throws Exception {

		double velocity = 10;
		double acceleration = 0;

		MotionX motionX = new MotionX(velocity, acceleration);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		double actualDisplacement = motionX.updateDisplacement(0);
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// No acceleration therefore distance = velocity * time
		double expectedDisplacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);

		Thread.sleep(1000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		expectedDisplacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);

		Thread.sleep(3000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		expectedDisplacement = velocity*dt;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);
	}

	@Test
	public void testUpdatePositionAcceleration() throws Exception {

		double position = 0;
		double velocity = 0;
		double acceleration = 10;

		MotionX motionX = new MotionX(velocity, acceleration);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		double actualDisplacement = motionX.updateDisplacement(position);
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		double expectedDisplacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);

		Thread.sleep(1000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		expectedDisplacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);

		Thread.sleep(3000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = dt*acceleration;
		expectedDisplacement = (velocity*dt)/2;
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", expectedDisplacement, actualDisplacement, errorMargin);
	}

	@Test
	public void testUpdatePosition() throws Exception {

		double position = 100;
		double initVelocity = -20;
		double acceleration = 10;

		MotionX motionX = new MotionX(initVelocity, acceleration);
		motionX.startMotion();
		long startTime = System.nanoTime();

		Thread.sleep(1000);
		double actualDisplacement = motionX.updateDisplacement(position);
		double dt = (System.nanoTime() - startTime)*nanoToSeconds;
		// V = U + aT
		double velocity = initVelocity + (acceleration*dt);
		// Add S to original position: S = UT + (aT^2)/2
		double displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, actualDisplacement, errorMargin);

		Thread.sleep(1000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, actualDisplacement, errorMargin);

		Thread.sleep(3000);
		actualDisplacement = motionX.updateDisplacement(actualDisplacement);
		dt = (System.nanoTime() - startTime)*nanoToSeconds;
		velocity = initVelocity + (acceleration*dt);
		displacement = position + ((initVelocity*dt) + ((acceleration*dt*dt)/2));
		assertEquals("Velocity", velocity, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", displacement, actualDisplacement, errorMargin);
	}

	@Test
	public void testBounceRight() throws Exception {
		double boundary = 100;
		double position = 102;
		double velocity = 10;
		double acceleration = 10;
		double energyLoss = 0.9;

		MotionX motionX = new MotionX(velocity, acceleration);
		motionX.startMotion();

		double actualDisplacement = motionX.bounce(position, boundary, energyLoss);
		
		assertEquals("Velocity", -5.49*energyLoss, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", 98.51*energyLoss, actualDisplacement, errorMargin);
	}
	
	@Test
	public void testBounceLeft() throws Exception {
		double boundary = 5;
		double position = 2;
		double velocity = -10;
		double acceleration = 10;
		double energyLoss = 0.8;

		MotionX motionX = new MotionX(velocity, acceleration);
		motionX.startMotion();

		double actualDisplacement = motionX.bounce(position, boundary, energyLoss);
		
		assertEquals("Velocity", 15.30*energyLoss, motionX.getVelocity(), errorMargin);
		assertEquals("Displacement", 8.70*energyLoss, actualDisplacement, errorMargin);
	}
	
	@Test
	public void testCollideLeft() {
		// GIVEN
		double acc = 0;		
		double radius1 = 10;
		double energyLoss = 0.5;
		
		double mass1 = 0.1;
		double xVelocity1 = -10;
		double yVelocity1 = 0;
		MotionX motionX1 = new MotionX(xVelocity1, acc);
		MotionY motionY1 = new MotionY(yVelocity1, acc);
		when(ball1.getMass()).thenReturn(mass1);
		when(ball1.getMotionX()).thenReturn(motionX1);
		when(ball1.getMotionY()).thenReturn(motionY1);
		
		double radius2 = 10;
		double mass2 = 0.2;
		double xVelocity2 = 10;
		double yVelocity2 = 0;
		MotionX motionX2 = new MotionX(xVelocity2, acc);
		MotionY motionY2 = new MotionY(yVelocity2, acc);	
		when(ball2.getMass()).thenReturn(mass2);
		when(ball2.getMotionX()).thenReturn(motionX2);
		when(ball2.getMotionY()).thenReturn(motionY2);
		
		double xPositionHit = 110;
		double yPositionHit = 100;
		
		// WHEN
		motionX1.collide(mass1, radius1, ball2, energyLoss, xPositionHit);
		motionY1.collide(mass1, radius1, ball2, energyLoss, yPositionHit);
		motionX2.collide(mass2, radius2, ball1, energyLoss, xPositionHit);
		motionY2.collide(mass2, radius2, ball1, energyLoss, yPositionHit);
		
		//THEN
		double expectedVelocityX1 = 16.667*(energyLoss*energyLoss);
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = -3.333*(energyLoss*energyLoss);
		double expectedVelocityY2 = 0;
		
		assertEquals("First balls X velocity", expectedVelocityX1, motionX1.getVelocity(), errorMargin);
		assertEquals("First balls Y velocity", expectedVelocityY1, motionY1.getVelocity(), errorMargin);
		assertEquals("Second balls X velocity", expectedVelocityX2, ball2.getMotionX().getVelocity(), errorMargin);
		assertEquals("Second balls Y velocity", expectedVelocityY2, ball2.getMotionY().getVelocity(), errorMargin);
	}
	
	@Test
	public void testCollideRightBothFinishLeft() {
		// GIVEN
		double acc = 0;		
		double radius1 = 10;
		double energyLoss = 0.5;
		
		double mass1 = 0.3;
		double xVelocity1 = 2;
		double yVelocity1 = 0;
		MotionX motionX1 = new MotionX(xVelocity1, acc);
		MotionY motionY1 = new MotionY(yVelocity1, acc);
		when(ball1.getMotionX()).thenReturn(motionX1);
		when(ball1.getMotionY()).thenReturn(motionY1);
		
		double radius2 = 20;
		double mass2 = 0.4;
		double xVelocity2 = -20;
		double yVelocity2 = 0;
		MotionX motionX2 = new MotionX(xVelocity2, acc);
		MotionY motionY2 = new MotionY(yVelocity2, acc);		
		when(ball2.getMotionX()).thenReturn(motionX2);
		when(ball2.getMotionY()).thenReturn(motionY2);
		
		double xPositionHit = 130;
		double yPositionHit = 100;
		
		// WHEN
		motionX1.collide(mass1, radius1, ball2, energyLoss, xPositionHit);
		motionY1.collide(mass1, radius1, ball2, energyLoss, yPositionHit);
		motionX2.collide(mass2, radius2, ball1, energyLoss, xPositionHit);
		motionY2.collide(mass2, radius2,ball1, energyLoss, yPositionHit);
		
		//THEN
		double expectedVelocityX1 = -23.143*(energyLoss*energyLoss);
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = -1.143*(energyLoss*energyLoss);
		double expectedVelocityY2 = 0;
		
		assertEquals("First balls X velocity", expectedVelocityX1, motionX1.getVelocity(), errorMargin);
		assertEquals("First balls Y velocity", expectedVelocityY1, motionY1.getVelocity(), errorMargin);
		assertEquals("Second balls X velocity", expectedVelocityX2, ball2.getMotionX().getVelocity(), errorMargin);
		assertEquals("Second balls Y velocity", expectedVelocityY2, ball2.getMotionY().getVelocity(), errorMargin);
	}

}