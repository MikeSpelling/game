package mike;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.applet.AudioClip;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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
	private double pxToMetres = 1;
	private double energyLoss = 1;
	
	@Mock private Ball ball1;
	@Mock private Ball ball2;
	@Mock private AudioClip bounceAudio;

	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
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
	
	@Test
	public void testCollideLeft() {
		// GIVEN
		double acc = 0;
		
		int x1 = 120;
		int y1 = 100;
		int radius1 = 10;
		double mass1 = 0.1;
		double xVelocity1 = -10;
		double yVelocity1 = 0;
		MotionX motionX1 = new MotionX(x1, xVelocity1, acc, pxToMetres, bounceAudio);
		MotionY motionY1 = new MotionY(y1, yVelocity1, acc, pxToMetres, bounceAudio);
		when(ball1.getMass()).thenReturn(mass1);
		when(ball1.getMotionX()).thenReturn(motionX1);
		when(ball1.getMotionY()).thenReturn(motionY1);
		
		int x2 = 100;
		int y2 = 100;
		int radius2 = 10;
		double mass2 = 0.2;
		double xVelocity2 = 10;
		double yVelocity2 = 0;
		MotionX motionX2 = new MotionX(x2, xVelocity2, acc, pxToMetres, bounceAudio);
		MotionY motionY2 = new MotionY(y2, yVelocity2, acc, pxToMetres, bounceAudio);		
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
		double expectedVelocityX1 = 16.667;
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = -3.333;
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
		
		int x1 = 120;
		int y1 = 100;
		int radius1 = 10;
		double mass1 = 0.3;
		double xVelocity1 = 2;
		double yVelocity1 = 0;
		MotionX motionX1 = new MotionX(x1, xVelocity1, acc, pxToMetres, bounceAudio);
		MotionY motionY1 = new MotionY(y1, yVelocity1, acc, pxToMetres, bounceAudio);
		when(ball1.getMotionX()).thenReturn(motionX1);
		when(ball1.getMotionY()).thenReturn(motionY1);
		
		int x2 = 150;
		int y2 = 100;
		int radius2 = 20;
		double mass2 = 0.4;
		double xVelocity2 = -20;
		double yVelocity2 = 0;
		MotionX motionX2 = new MotionX(x2, xVelocity2, acc, pxToMetres, bounceAudio);
		MotionY motionY2 = new MotionY(y2, yVelocity2, acc, pxToMetres, bounceAudio);		
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
		double expectedVelocityX1 = -23.143;
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = -1.143;
		double expectedVelocityY2 = 0;
		
		assertEquals("First balls X velocity", expectedVelocityX1, motionX1.getVelocity(), errorMargin);
		assertEquals("First balls Y velocity", expectedVelocityY1, motionY1.getVelocity(), errorMargin);
		assertEquals("Second balls X velocity", expectedVelocityX2, ball2.getMotionX().getVelocity(), errorMargin);
		assertEquals("Second balls Y velocity", expectedVelocityY2, ball2.getMotionY().getVelocity(), errorMargin);
	}

}