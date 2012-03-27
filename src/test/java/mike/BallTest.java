package mike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


public class BallTest {
	
	double pxToMetres = 1;
	double energyLossCollision = 1;
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
		assertFalse("Balls should not intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testContainsTrue() {
		
		int x1 = 100; 		int x2 = 200;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 60;	int radius2 = 60;		
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainsOther() {
		
		int x1 = 100; 		int x2 = 110;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 100;	int radius2 = 10;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainedByOther() {
		
		int x1 = 80; 		int x2 = 100;
		int y1 = 80; 		int y2 = 100;
		int radius1 = 10;	int radius2 = 100;				
		double mass1 = 0.1;	double mass2 = 0.2;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
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
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX1, motionY1, collisionDetector, collisionDetector);
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
	@Test
	public void testCollide() {
		int x1 = 100;
		int y1 = 100;
		int radius1 = 10;
		double mass1 = 0.1;
		when(motionX1.getVelocity()).thenReturn(10.0);
		when(motionX1.getDisplacement()).thenReturn(x1*pxToMetres);
		when(motionY1.getVelocity()).thenReturn(0.0);
		when(motionY1.getDisplacement()).thenReturn(y1*pxToMetres);
		
		int x2 = 120;
		int y2 = 100;
		int radius2 = 10;
		double mass2 = 0.2;
		when(motionX2.getVelocity()).thenReturn(-10.0);
		when(motionX2.getAcceleration()).thenReturn(x2*pxToMetres);
		when(motionY2.getVelocity()).thenReturn(0.0);
		when(motionY2.getAcceleration()).thenReturn(y2*pxToMetres);
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, collisionDetector, collisionDetector);
		
		ball.collide(otherBall, energyLossCollision);
		
		double expectedVelocityX1 = 0;
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = 0;
		double expectedVelocityY2 = 0;
		
		assertEquals(expectedVelocityX1, ball.getMotionX().getVelocity(), errorMargin);
		assertEquals(expectedVelocityY1, ball.getMotionY().getVelocity(), errorMargin);
		assertEquals(expectedVelocityX2, otherBall.getMotionX().getVelocity(), errorMargin);
		assertEquals(expectedVelocityY2, otherBall.getMotionY().getVelocity(), errorMargin);
		
		fail("TODO - Calculate expected results");
	}
}