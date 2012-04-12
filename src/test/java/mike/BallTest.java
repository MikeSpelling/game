package mike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BallTest {
	
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
	}
	
	@Test
	public void testContainsFalse() {
		
		double x1 = 100; 		double x2 = 200;
		double y1 = 100; 		double y2 = 200;
		double radius1 = 10;	double radius2 = 10;		
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertFalse("Balls should not intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testContainsTrue() {
		
		double x1 = 100; 		double x2 = 200;
		double y1 = 100; 		double y2 = 100;
		double radius1 = 60;	double radius2 = 60;		
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainsOther() {
		
		double x1 = 100; 		double x2 = 110;
		double y1 = 100; 		double y2 = 110;
		double radius1 = 100;	double radius2 = 10;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainedByOther() {
		
		double x1 = 80; 		double x2 = 100;
		double y1 = 80; 		double y2 = 100;
		double radius1 = 10;	double radius2 = 100;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testGetPointOfContactRight() {
		
		double x1 = 100; 		double x2 = 125;
		double y1 = 100; 		double y2 = 100;
		double radius1 = 20;	double radius2 = 5;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 120;
		double expectedY = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}

	@Test
	public void testGetPointOfContactLeft() {
		
		double x1 = 50; 		double x2 = 20;
		double y1 = 100; 		double y2 = 100;
		double radius1 = 10;	double radius2 = 20;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 40;
		double expectedY = 100;				
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactAbove() {
		
		double x1 = 0; 		double x2 = 0;
		double y1 = 1000; 		double y2 = 1250;
		double radius1 = 100;	double radius2 = 150;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 0;
		double expectedY = 1100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactBelow() {
		
		double x1 = 100; 		double x2 = 100;
		double y1 = 100; 		double y2 = 110;
		double radius1 = 1;	double radius2 = 9;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 100;
		double expectedY = 101;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactTopRight() {
		
		double x1 = 100; 		double x2 = 110;
		double y1 = 100; 		double y2 = 90;
		double radius1 = 10;	double radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 105;
		double expectedY = 95;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactTopLeft() {
		
		double x1 = 100; 		double x2 = 90;
		double y1 = 100; 		double y2 = 90;
		double radius1 = 10;	double radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 95;
		double expectedY = 95;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactBottomLeft() {
		
		double x1 = 100; 		double x2 = 90;
		double y1 = 100; 		double y2 = 110;
		double radius1 = 10;	double radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 95;
		double expectedY = 105;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactBottomRight() {
		
		double x1 = 100; 		double x2 = 110;
		double y1 = 100; 		double y2 = 110;
		double radius1 = 10;	double radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 105;
		double expectedY = 105;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactOnTop() {
		
		double x1 = 100; 		double x2 = 100;
		double y1 = 100; 		double y2 = 100;
		double radius1 = 10;	double radius2 = 10;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 100;
		double expectedY = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0);
		assertEquals("Y Position", expectedY, point.getY(), 0);
	}
	
	@Test
	public void testGetPointOfContactDifferentRadius() {
		
		double x1 = 100; 		double x2 = 120;
		double y1 = 100; 		double y2 = 120;
		double radius1 = 10;	double radius2 = 30;
		double mass1 = 0.1;	double mass2 = 0.2;
		
		double expectedX = 102.9289;
		double expectedY = 102.9289;		
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, energyLoss);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point2D point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.getX(), 0.001);
		assertEquals("Y Position", expectedY, point.getY(), 0.001);
	}

	@Test
	public void testCollideRight() {
		// GIVEN
		double x1 = 100;
		double y1 = 100;
		double radius1 = 10;
		double mass1 = 0.1;
		double energyLoss1 = 0.7;
		double velocityX1 = 2;
		double velocityY1 = 3;
		when(motionX1.getVelocity()).thenReturn(velocityX1);
		when(motionY1.getVelocity()).thenReturn(velocityY1);
		
		double x2 = 120;
		double y2 = 100;
		double radius2 = 10;
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
		verify(motionX1).collide(mass1, radius1, otherBall, combinedEnergyLoss, xPositionHit);
		verify(motionY1).collide(mass1, radius1, otherBall, combinedEnergyLoss, yPositionHit);
	}
	
	@Test
	public void testCollideLeft() {
		// GIVEN
		double x1 = 100;
		double y1 = 100;
		double radius1 = 10;
		double mass1 = 0.1;
		double energyLoss1 = 0.7;
		double velocityX1 = 2;
		double velocityY1 = 3;
		when(motionX1.getVelocity()).thenReturn(velocityX1);
		when(motionY1.getVelocity()).thenReturn(velocityY1);
		
		double x2 = 70;
		double y2 = 100;
		double radius2 = 20;
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
		double xPositionHit = 90;
		double yPositionHit = 100;
		verify(motionX1).collide(mass1, radius1, otherBall, combinedEnergyLoss, xPositionHit);
		verify(motionY1).collide(mass1, radius1, otherBall, combinedEnergyLoss, yPositionHit);
	}
	
	@Test
	public void testCollideBottom() {
		// GIVEN
		double x1 = 100;
		double y1 = 100;
		double radius1 = 20;
		double mass1 = 0.1;
		double energyLoss1 = 0.7;
		double velocityX1 = 3;
		double velocityY1 = 2;
		when(motionX1.getVelocity()).thenReturn(velocityX1);
		when(motionY1.getVelocity()).thenReturn(velocityY1);
		
		double x2 = 100;
		double y2 = 130;
		double radius2 = 10;
		double mass2 = 0.2;
		double energyLoss2 = 0.6;
		double velocityX2 = 5;
		double velocityY2 = 4;
		when(motionX2.getVelocity()).thenReturn(velocityX2);
		when(motionY2.getVelocity()).thenReturn(velocityY2);
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss1);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, energyLoss2);		
		assertTrue("Balls should collide", ball.contains(otherBall));
		
		// WHEN
		ball.collide(otherBall);
		
		//THEN
		double combinedEnergyLoss = energyLoss1 * energyLoss2;
		double xPositionHit = 100;
		double yPositionHit = 120;
		verify(motionX1).collide(mass1, radius1, otherBall, combinedEnergyLoss, xPositionHit);
		verify(motionY1).collide(mass1, radius1, otherBall, combinedEnergyLoss, yPositionHit);
	}
	
	@Test
	public void testCollideTop() {
		// GIVEN
		double x1 = 100;
		double y1 = 100;
		double radius1 = 5;
		double mass1 = 0.1;
		double energyLoss1 = 0.7;
		double velocityX1 = 3;
		double velocityY1 = 2;
		when(motionX1.getVelocity()).thenReturn(velocityX1);
		when(motionY1.getVelocity()).thenReturn(velocityY1);
		
		double x2 = 100;
		double y2 = 90;
		double radius2 = 5;
		double mass2 = 0.2;
		double energyLoss2 = 0.6;
		double velocityX2 = 5;
		double velocityY2 = 4;
		when(motionX2.getVelocity()).thenReturn(velocityX2);
		when(motionY2.getVelocity()).thenReturn(velocityY2);
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, energyLoss1);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, energyLoss2);		
		assertTrue("Balls should collide", ball.contains(otherBall));
		
		// WHEN
		ball.collide(otherBall);
		
		//THEN
		double combinedEnergyLoss = energyLoss1 * energyLoss2;
		double xPositionHit = 100;
		double yPositionHit = 95;
		verify(motionX1).collide(mass1, radius1, otherBall, combinedEnergyLoss, xPositionHit);
		verify(motionY1).collide(mass1, radius1, otherBall, combinedEnergyLoss, yPositionHit);
	}
	
}