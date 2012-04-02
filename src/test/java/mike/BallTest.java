package mike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


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
	public void testCollideHorizontalRight() {
		// GIVEN
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
		when(motionX2.getDisplacement()).thenReturn(x2*pxToMetres);
		when(motionY2.getVelocity()).thenReturn(0.0);
		when(motionY2.getDisplacement()).thenReturn(y2*pxToMetres);
		
		double expectedVelocityX1 = -6.667;
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = 3.333;
		double expectedVelocityY2 = 0;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, collisionDetector, collisionDetector);		
		assertTrue("Balls should collide", ball.contains(otherBall));
		
		// WHEN
		ArgumentCaptor<Double> actualX1 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualY1 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualX2 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualY2 = ArgumentCaptor.forClass(Double.class);
		ball.collide(otherBall, energyLossCollision);
		
		//THEN
		verify(motionX1).setVelocity(actualX1.capture());
		verify(motionY1).setVelocity(actualY1.capture());
		verify(motionX2).setVelocity(actualX2.capture());
		verify(motionY2).setVelocity(actualY2.capture());
		
		assertEquals("First balls X velocity", expectedVelocityX1, actualX1.getValue(), errorMargin);
		assertEquals("First balls Y velocity", expectedVelocityY1, actualY1.getValue(), errorMargin);
		assertEquals("Second balls X velocity", expectedVelocityX2, actualX2.getValue(), errorMargin);
		assertEquals("Second balls Y velocity", expectedVelocityY2, actualY2.getValue(), errorMargin);
	}
	
	@Test
	public void testCollideHorizontalLeft() {
		// GIVEN
		int x1 = 120;
		int y1 = 100;
		int radius1 = 10;
		double mass1 = 0.1;
		when(motionX1.getVelocity()).thenReturn(-10.0);
		when(motionX1.getDisplacement()).thenReturn(x1*pxToMetres);
		when(motionY1.getVelocity()).thenReturn(0.0);
		when(motionY1.getDisplacement()).thenReturn(y1*pxToMetres);
		
		int x2 = 100;
		int y2 = 100;
		int radius2 = 10;
		double mass2 = 0.2;
		when(motionX2.getVelocity()).thenReturn(10.0);
		when(motionX2.getDisplacement()).thenReturn(x2*pxToMetres);
		when(motionY2.getVelocity()).thenReturn(0.0);
		when(motionY2.getDisplacement()).thenReturn(y2*pxToMetres);
		
		double expectedVelocityX1 = 6.667;
		double expectedVelocityY1 = 0;
		double expectedVelocityX2 = -3.333;
		double expectedVelocityY2 = 0;
		
		Ball ball = new Ball(x1, y1, radius1, mass1, color, motionX1, motionY1, collisionDetector, collisionDetector);
		Ball otherBall = new Ball(x2, y2, radius2, mass2, color, motionX2, motionY2, collisionDetector, collisionDetector);		
		assertTrue("Balls should collide", ball.contains(otherBall));
		
		// WHEN
		ArgumentCaptor<Double> actualX1 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualY1 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualX2 = ArgumentCaptor.forClass(Double.class);
		ArgumentCaptor<Double> actualY2 = ArgumentCaptor.forClass(Double.class);
		ball.collide(otherBall, energyLossCollision);
		
		//THEN
		verify(motionX1).setVelocity(actualX1.capture());
		verify(motionY1).setVelocity(actualY1.capture());
		verify(motionX2).setVelocity(actualX2.capture());
		verify(motionY2).setVelocity(actualY2.capture());
		
		assertEquals("First balls X velocity", expectedVelocityX1, actualX1.getValue(), errorMargin);
		assertEquals("First balls Y velocity", expectedVelocityY1, actualY1.getValue(), errorMargin);
		assertEquals("Second balls X velocity", expectedVelocityX2, actualX2.getValue(), errorMargin);
		assertEquals("Second balls Y velocity", expectedVelocityY2, actualY2.getValue(), errorMargin);
	}
}