package mike;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class CollisionDetectorTest {
	
	CollisionDetector collisionDetector;
	List<Ball> balls = new ArrayList<Ball>();
	
	double energyLossTop = 0.1;
	double energyLossBottom = 0.2;
	double energyLossLeft = 0.3;
	double energyLossRight = 0.4;
	double energyLossCollision = 0.5;
	
	@Mock MotionX motionX;
	@Mock MotionY motionY;
	
	@Mock Ball ball1;
	@Mock Ball ball2;
	@Mock Ball ball3;
	@Mock Ball ball4;
	@Mock Ball ball5;
	
	@Before
	public void before() throws Exception {		
		MockitoAnnotations.initMocks(this);
		
		int top = 0;
		int left = 0;
		int bottom = 100;
		int right = 100;
		
		collisionDetector = new CollisionDetector(top, energyLossTop, bottom, energyLossBottom, 
				left, energyLossLeft, right, energyLossRight, energyLossCollision);
		
		balls.add(ball1); balls.add(ball2); balls.add(ball3); balls.add(ball4); balls.add(ball5);
	}
	
	@Test
	public void testDetectCollisionsBalls() throws Exception {
		
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
		verify(ball1, never()).collide(eq(ball1), anyDouble());
		verify(ball1).collide(ball2,energyLossCollision);
		verify(ball1, never()).collide(eq(ball3), anyDouble());
		verify(ball1, never()).collide(eq(ball4), anyDouble());
		verify(ball1, never()).collide(eq(ball5), anyDouble());
		// ball2 already hit ball1 but should hit others
		verify(ball2, never()).collide(eq(ball1), anyDouble());
		verify(ball2, never()).collide(eq(ball2), anyDouble());
		verify(ball2).collide(ball3,energyLossCollision);
		verify(ball2).collide(ball4,energyLossCollision);
		verify(ball2).collide(ball5,energyLossCollision);
		// ball3 already hit 2 but should hit 5
		verify(ball3, never()).collide(eq(ball1), anyDouble());
		verify(ball3, never()).collide(eq(ball2), anyDouble());
		verify(ball3, never()).collide(eq(ball3), anyDouble());
		verify(ball3, never()).collide(eq(ball4), anyDouble());
		verify(ball3).collide(ball5,energyLossCollision);
		// ball4 already hit 2
		verify(ball4, never()).collide(any(Ball.class), anyDouble());
		// last ball already taken into account
		verify(ball5, never()).collide(any(Ball.class), anyDouble());
	}
	
	@Test
	public void testDetectCollisionsBoundaries() throws Exception {

		when(ball1.contains(any(Ball.class))).thenReturn(false);
		when(ball2.contains(any(Ball.class))).thenReturn(false);
		when(ball3.contains(any(Ball.class))).thenReturn(false);
		when(ball4.contains(any(Ball.class))).thenReturn(false);
		when(ball5.contains(any(Ball.class))).thenReturn(false);
		
		ball1.x = 10;
		ball1.y = 10;
		ball1.radius = 10;
		
		ball2.x = 90;
		ball2.y = 90;
		ball2.radius = 11;
		
		ball3.x = 99;
		ball3.y = 3;
		ball3.radius = 5;
		
		ball4.x = 20;
		ball4.y = 90;
		ball4.radius = 20;
		
		ball5.x = 1;
		ball5.y = 1;
		ball5.radius = 0;

		collisionDetector.detectCollisions(balls);
		
		// Balls don't collide
		verify(ball1, never()).collide(any(Ball.class), anyDouble());
		verify(ball2, never()).collide(any(Ball.class), anyDouble());
		verify(ball3, never()).collide(any(Ball.class), anyDouble());
		verify(ball4, never()).collide(any(Ball.class), anyDouble());
		verify(ball5, never()).collide(any(Ball.class), anyDouble());
		
		// Detect boundaries
		verify(ball1).hitBoundaryX(10, energyLossLeft);
		verify(ball1).hitBoundaryY(10, energyLossTop);
		verify(ball2).hitBoundaryX(89, energyLossRight);
		verify(ball2).hitBoundaryY(89, energyLossBottom);
		verify(ball3).hitBoundaryX(95, energyLossRight);
		verify(ball3).hitBoundaryY(5, energyLossTop);
		verify(ball4).hitBoundaryX(20, energyLossLeft);
		verify(ball4).hitBoundaryY(80, energyLossBottom);
		verify(ball5, never()).hitBoundaryX(anyInt(), anyDouble());
		verify(ball5, never()).hitBoundaryY(anyInt(), anyDouble());
	}
	
	@Test
	public void testDetectCollisionsBoundariesIndependantXY() throws Exception {

		when(ball1.contains(any(Ball.class))).thenReturn(false);
		when(ball2.contains(any(Ball.class))).thenReturn(false);
		when(ball3.contains(any(Ball.class))).thenReturn(false);
		when(ball4.contains(any(Ball.class))).thenReturn(false);
		when(ball5.contains(any(Ball.class))).thenReturn(false);
		
		// Top
		ball1.x = 50;
		ball1.y = 10;
		ball1.radius = 10;
		
		// Right
		ball2.x = 90;
		ball2.y = 20;
		ball2.radius = 11;
		
		// Bottom
		ball3.x = 60;
		ball3.y = 99;
		ball3.radius = 5;
		
		// Left
		ball4.x = 20;
		ball4.y = 70;
		ball4.radius = 20;
		
		ball5.x = 1000;
		ball5.y = 1000;
		ball5.radius = 90;

		collisionDetector.detectCollisions(balls);
		
		// Balls don't collide
		verify(ball1, never()).collide(any(Ball.class), anyDouble());
		verify(ball2, never()).collide(any(Ball.class), anyDouble());
		verify(ball3, never()).collide(any(Ball.class), anyDouble());
		verify(ball4, never()).collide(any(Ball.class), anyDouble());
		verify(ball5, never()).collide(any(Ball.class), anyDouble());
		
		// Detect boundaries
		verify(ball1, never()).hitBoundaryX(anyInt(), anyDouble());
		verify(ball1).hitBoundaryY(10, energyLossTop);
		verify(ball2).hitBoundaryX(89, energyLossRight);
		verify(ball2, never()).hitBoundaryY(anyInt(), anyDouble());
		verify(ball3, never()).hitBoundaryX(anyInt(), anyDouble());
		verify(ball3).hitBoundaryY(95, energyLossBottom);
		verify(ball4).hitBoundaryX(20, energyLossLeft);
		verify(ball4, never()).hitBoundaryY(anyInt(), anyDouble());
		verify(ball5).hitBoundaryX(10, energyLossRight);
		verify(ball5).hitBoundaryY(10, energyLossBottom);
	}

}