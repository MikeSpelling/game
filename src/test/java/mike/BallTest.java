package mike;

import static org.junit.Assert.*;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;


public class BallTest {
	
	private int position = 1;
	private int velocity = 1;
	private int acceleration = 1;	
	double pxToMetres = 1;
	private AudioClip bounceAudio;
	
	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testContainsFalse() {
		
		int x1 = 100; 		int x2 = 200;
		int y1 = 100; 		int y2 = 200;
		int radius1 = 10;	int radius2 = 10;		
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));

		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		assertFalse("Balls should not intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testContainsTrue() {
		
		int x1 = 100; 		int x2 = 200;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 60;	int radius2 = 60;		
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));

		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainsOther() {
		
		int x1 = 100; 		int x2 = 110;
		int y1 = 100; 		int y2 = 110;
		int radius1 = 100;	int radius2 = 10;		
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));

		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testFullyContainedByOther() {
		
		int x1 = 80; 		int x2 = 100;
		int y1 = 80; 		int y2 = 100;
		int radius1 = 10;	int radius2 = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));

		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		assertTrue("Balls should intersect", ball.contains(otherBall));
	}
	
	@Test
	public void testGetPointOfContactRight() {
		
		int x1 = 100; 		int x2 = 125;
		int y1 = 100; 		int y2 = 100;
		int radius1 = 20;	int radius2 = 5;
		
		int expectedX = 120;
		int expectedY = 100;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));

		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 40;
		int expectedY = 100;		
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 0;
		int expectedY = 1100;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 100;
		int expectedY = 101;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 105;
		int expectedY = 95;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 95;
		int expectedY = 95;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 95;
		int expectedY = 105;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
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
		
		int expectedX = 105;
		int expectedY = 105;
		
		Ball ball = new Ball(x1, y1, radius1, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		Ball otherBall = new Ball(x2, y2, radius2, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
		
		assertTrue("Balls should intersect", ball.contains(otherBall));
		Point point = ball.getPointOfContact(otherBall);
		
		assertEquals("X Position", expectedX, point.x);
		assertEquals("Y Position", expectedY, point.y);
	}
	
}