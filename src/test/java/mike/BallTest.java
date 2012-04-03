package mike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BallTest {
	
	double pxToMetres = 1;
	double energyLoss = 1;
	double errorMargin = 0.01;
	
	@Mock MotionX motionX1;
	@Mock MotionY motionY1;
	@Mock MotionX motionX2;
	@Mock MotionY motionY2;
	@Mock CollisionDetector collisionDetector;
	@Mock Color color;
	
	
	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		when(motionX1.getPxToMetres()).thenReturn(pxToMetres);
	}
	
	@Test
	public void testContainsFalse() {
		
		int x1 = 100; 		int x2 = 200;
		int y1 = 100; 		int y2 = 200;
		int radius1 = 10;	int radius2 = 10;		
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertFalse("Balls should not intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testContainsTrue() {
		
		int x1 = 100; 		int x2 = 200;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 60;	int radius2 = 60;		
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainsOther() {
		
		int x1 = 100; 		int x2 = 110;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 100;	int radius2 = 10;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainedByOther() {
		
		int x1 = 80; 		int x2 = 100;
		int y1 = 80; 		int y2 = 100;
		int radius1 = 10;	int radius2 = 100;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testGetPointOfContactRight() {
		
		int x1 = 100; 		int x2 = 125;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 20;	int radius2 = 5;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 120;
		int expectedY = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}

	@Test
	public void testGetPointOfContactLeft() {
		
		int x1 = 50; 		int x2 = 20;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 10;	int radius2 = 20;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 40;
		int expectedY = 100;				
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactAbove() {
		
		int x1 = 0; 		int x2 = 0;
		int y1 = 1000; 		int y2 = 1250;
		int radius1 = 100;	int radius2 = 150;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 0;
		int expectedY = 1100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactBelow() {
		
		int x1 = 100; 		int x2 = 100;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 1;	int radius2 = 9;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 100;
		int expectedY = 101;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactTopRight() {
		
		int x1 = 100; 		int x2 = 110;
		int y1 = 100; 		int y2 = 90;
		int radius1 = 10;	int radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 105;
		int expectedY = 95;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactTopLeft() {
		
		int x1 = 100; 		int x2 = 90;
		int y1 = 100; 		int y2 = 90;
		int radius1 = 10;	int radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 95;
		int expectedY = 95;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactBottomLeft() {
		
		int x1 = 100; 		int x2 = 90;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 10;	int radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 95;
		int expectedY = 105;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactBottomRight() {
		
		int x1 = 100; 		int x2 = 110;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 10;	int radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 105;
		int expectedY = 105;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactOnTop() {
		
		int x1 = 100; 		int x2 = 100;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 10;	int radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 100;
		int expectedY = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testGetPointOfContactDifferentRadius() {
		
		int x1 = 100; 		int x2 = 120;
		int y1 = 100; 		int y2 = 120;
		int radius1 = 10;	int radius2 = 30;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		int expectedX = 103;
		int expectedY = 103;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testCollideHorizontalRight() {
		// GIVEN
		int x1 = 100;
		int y1 = 100;
		int radius1 = 10;
		double mass1 = 0.1;
		double energyLoss1 = 0.7;
		double velocityX1 = 2;
		double velocityY1 = 3;
		when(motionX1.getVelocity()).thenReturn(velocityX1);
		when(motionY1.getVelocity()).thenReturn(velocityY1);
		
		int x2 = 120;
		int y2 = 100;
		int radius2 = 10;
		double mass2 = 0.2;
		double energyLoss2 = 0.6;
		double velocityX2 = 4;
		double velocityY2 = 5;
		when(motionX2.getVelocity()).thenReturn(velocityX2);
		when(motionY2.getVelocity()).thenReturn(velocityY2);
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss1);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, energyLoss2);		
		assertTrue("Balls should collide", ball.contains(otherBall));
		
		// WHEN
		ball.collide(otherBall);
		
		//THEN
		double combinedEnergyLoss = energyLoss1 * energyLoss2;
		double xPositionHit = 110;
		double yPositionHit = 100;
		verify(motionX1).collide(mass1, radius1, mass2, radius2, velocityX2, combinedEnergyLoss, xPositionHit);
		verify(motionY1).collide(mass1, radius1, mass2, radius2, velocityY2, combinedEnergyLoss, yPositionHit);
		verify(motionX2).collide(mass2, radius2, mass1, radius1, velocityX1, combinedEnergyLoss, xPositionHit);
		verify(motionY2).collide(mass2, radius2, mass1, radius1, velocityY1, combinedEnergyLoss, yPositionHit);
	}
	
}