package mike;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class CollisionDetectorTest {
	
	private CollisionDetector collisionDetector;
	
	private double energyLossLeft = 0.1;
	private double energyLossRight = 0.2;
	private double energyLossTop = 0.3;
	private double energyLossBottom = 0.4;
	private double left = 0;
	private double right = 10;
	private double top = 1;
	private double bottom = 10;
	
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	
	@Mock private MotionX motionX;
	@Mock private MotionY motionY;
	
	@Mock private Ball ball1;
	@Mock private Ball ball2;
	@Mock private Ball ball3;
	@Mock private Ball ball4;
	@Mock private Ball ball5;
	
	@Mock Color color;
	
	
	@Before
	public void before() throws Exception {		
		MockitoAnnotations.initMocks(this);
		
		collisionDetector = new CollisionDetector(top, energyLossTop, bottom, energyLossBottom, left, energyLossLeft, right, energyLossRight);
		
		balls.add(ball1); balls.add(ball2); balls.add(ball3); balls.add(ball4); balls.add(ball5);
	}
	
	@Test
	public void testDetectCollisions() throws Exception {
		
		when(ball1.contains(ball2)).thenReturn(true);
		when(ball1.contains(ball3)).thenReturn(false);
		when(ball1.contains(ball4)).thenReturn(false);
		when(ball1.contains(ball5)).thenReturn(false);
		
		when(ball2.contains(any(Ball.class))).thenReturn(true);
		
		when(ball3.contains(ball4)).thenReturn(false);
		when(ball3.contains(ball5)).thenReturn(true);
		
		when(ball4.contains(ball5)).thenReturn(false);

		collisionDetector.detectCollisions(balls);
		
		// ball1 hits ball2 only
		verify(ball1, never()).collide(ball1);
		verify(ball1).collide(ball2);
		verify(ball1, never()).collide(ball3);
		verify(ball1, never()).collide(ball4);
		verify(ball1, never()).collide(ball5);
		// ball2 already hit ball1 but should hit others
		verify(ball2, never()).collide(ball1);
		verify(ball2, never()).collide(ball2);
		verify(ball2).collide(ball3);
		verify(ball2).collide(ball4);
		verify(ball2).collide(ball5);
		// ball3 should hit ball5 (already hit ball2)
		verify(ball3, never()).collide(ball1);
		verify(ball3, never()).collide(ball2);
		verify(ball3, never()).collide(ball3);
		verify(ball3, never()).collide(ball4);
		verify(ball3).collide(ball5);
		// ball4 already hit 2
		verify(ball4, never()).collide(any(Ball.class));
		// last ball already taken into account
		verify(ball5, never()).collide(any(Ball.class));
	}
	
	@Test
	public void testDetectBoundaryNone() throws Exception {
		int x = 5;
		int y = 5;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX, never()).bounce(anyDouble(), anyDouble(), anyDouble());
		verify(motionY, never()).bounce(anyDouble(), anyDouble(), anyDouble());
	}
	
	@Test
	public void testDetectBoundaryLeft() throws Exception {
		int x = 0;
		int y = 5;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, left+radius, energyLossLeft);
		verify(motionY, never()).bounce(anyDouble(), anyDouble(), anyDouble());
	}
	
	@Test
	public void testDetectBoundaryRight() throws Exception {
		int x = 20;
		int y = 5;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, right-radius, energyLossRight);
		verify(motionY, never()).bounce(anyDouble(), anyDouble(), anyDouble());
	}
	
	@Test
	public void testDetectBoundaryTop() throws Exception {
		int x = 5;
		int y = 0;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX, never()).bounce(anyDouble(), anyDouble(), anyDouble());
		verify(motionY).bounce(y, top+radius, energyLossTop);
	}
	
	@Test
	public void testDetectBoundaryBottom() throws Exception {
		int x = 5;
		int y = 120;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX, never()).bounce(anyDouble(), anyDouble(), anyDouble());
		verify(motionY).bounce(y, bottom-radius, energyLossBottom);
	}
	
	@Test
	public void testDetectBoundaryBottomLeft() throws Exception {
		int x = -1;
		int y = 120;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, left+radius, energyLossLeft);
		verify(motionY).bounce(y, bottom-radius, energyLossBottom);
	}
	
	@Test
	public void testDetectBoundaryBottomRight() throws Exception {
		int x = 11;
		int y = 120;
		int radius = 11;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, right-radius, energyLossRight);
		verify(motionY).bounce(y, bottom-radius, energyLossBottom);
	}
	
	@Test
	public void testDetectBoundaryTopLeft() throws Exception {
		int x = -1;
		int y = -123;
		int radius = 1;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, left+radius, energyLossLeft);
		verify(motionY).bounce(y, top+radius, energyLossTop);
	}
	
	@Test
	public void testDetectBoundaryTopRight() throws Exception {
		int x = 11;
		int y = 1;
		int radius = 5;
		double mass = 1;
		double energyLoss = 1.1;
		
		Ball ball = new Ball(x, y, radius, mass, color, motionX, motionY, energyLoss);
		
		collisionDetector.detectBoundary(ball);
		
		verify(motionX).bounce(x, right-radius, energyLossRight);
		verify(motionY).bounce(y, top+radius, energyLossTop);
	}
	
	@Test
	public void testDetectCollisionsAndBoundary() throws Exception {
		when(ball1.getMotionX()).thenReturn(motionX);
		when(ball1.getMotionY()).thenReturn(motionY);
		when(ball1.getX()).thenReturn(1.0);
		when(ball1.getRadius()).thenReturn(5.0);
		when(ball2.getMotionX()).thenReturn(motionX);
		when(ball2.getMotionY()).thenReturn(motionY);
		when(ball2.getX()).thenReturn(1.0);
		when(ball2.getRadius()).thenReturn(5.0);
		when(ball3.getMotionX()).thenReturn(motionX);
		when(ball3.getMotionY()).thenReturn(motionY);
		when(ball3.getX()).thenReturn(1.0);
		when(ball3.getRadius()).thenReturn(5.0);
		when(ball4.getMotionX()).thenReturn(motionX);
		when(ball4.getMotionY()).thenReturn(motionY);
		when(ball4.getX()).thenReturn(1.0);
		when(ball4.getRadius()).thenReturn(5.0);
		when(ball5.getMotionX()).thenReturn(motionX);
		when(ball5.getMotionY()).thenReturn(motionY);
		when(ball5.getX()).thenReturn(1.0);
		when(ball5.getRadius()).thenReturn(5.0);
		
		when(ball1.contains(ball2)).thenReturn(true);
		when(ball1.contains(ball3)).thenReturn(false);
		when(ball1.contains(ball4)).thenReturn(false);
		when(ball1.contains(ball5)).thenReturn(false);
		
		when(ball2.contains(any(Ball.class))).thenReturn(true);
		
		when(ball3.contains(ball4)).thenReturn(false);
		when(ball3.contains(ball5)).thenReturn(true);
		
		when(ball4.contains(ball5)).thenReturn(false);

		collisionDetector.detectCollisionsAndBoundary(balls);
		
		// ball1 hits ball2 only
		verify(ball1, never()).collide(ball1);
		verify(ball1).collide(ball2);
		verify(ball1, never()).collide(ball3);
		verify(ball1, never()).collide(ball4);
		verify(ball1, never()).collide(ball5);
		// ball2 already hit ball1 but should hit others
		verify(ball2, never()).collide(ball1);
		verify(ball2, never()).collide(ball2);
		verify(ball2).collide(ball3);
		verify(ball2).collide(ball4);
		verify(ball2).collide(ball5);
		// ball3 should hit ball5 (already hit ball2)
		verify(ball3, never()).collide(ball1);
		verify(ball3, never()).collide(ball2);
		verify(ball3, never()).collide(ball3);
		verify(ball3, never()).collide(ball4);
		verify(ball3).collide(ball5);
		// ball4 already hit 2
		verify(ball4, never()).collide(any(Ball.class));
		// last ball already taken into account
		verify(ball5, never()).collide(any(Ball.class));
		
		verify(ball1).bounceX(left+ball1.getRadius(), energyLossLeft);
		verify(ball1).bounceY(top+ball1.getRadius(), energyLossTop);
		verify(ball2).bounceX(left+ball2.getRadius(), energyLossLeft);
		verify(ball2).bounceY(top+ball2.getRadius(), energyLossTop);
		verify(ball3).bounceX(left+ball3.getRadius(), energyLossLeft);
		verify(ball3).bounceY(top+ball3.getRadius(), energyLossTop);
		verify(ball4).bounceX(left+ball4.getRadius(), energyLossLeft);
		verify(ball4).bounceY(top+ball4.getRadius(), energyLossTop);
		verify(ball5).bounceX(left+ball5.getRadius(), energyLossLeft);
		verify(ball5).bounceY(top+ball5.getRadius(), energyLossTop);
	}

}