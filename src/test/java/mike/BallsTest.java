package mike;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BallsTest {
	
	double energyLossLeft = 0.1;
	double energyLossRight = 0.2;
	double energyLossTop = 0.3;
	double energyLossBottom = 0.4;
	
	double energyLossCollision = 0.5;
	Balls balls = new Balls(energyLossCollision);
	
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

		balls.detectCollisions();
		
		// ball1 hits ball2 only
		verify(ball1, never()).collide(eq(ball1), anyDouble());
		verify(ball1).collide(ball2, energyLossCollision);
		verify(ball1, never()).collide(eq(ball3), anyDouble());
		verify(ball1, never()).collide(eq(ball4), anyDouble());
		verify(ball1, never()).collide(eq(ball5), anyDouble());
		// ball2 already hit ball1 but should hit others
		verify(ball2, never()).collide(eq(ball1), anyDouble());
		verify(ball2, never()).collide(eq(ball2), anyDouble());
		verify(ball2).collide(ball3,energyLossCollision);
		verify(ball2).collide(ball4,energyLossCollision);
		verify(ball2).collide(ball5,energyLossCollision);
		// ball3 should hit ball5 (already hit ball2)
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

}