package mike;

import java.applet.AudioClip;
import java.awt.Color;

import org.junit.Before;
import org.junit.Test;


public class CollisionDetectorTest {
	
	private int position = 1;
	private int velocity = 1;
	private int acceleration = 1;	
	double pxToMetres = 1;
	private AudioClip bounceAudio;
	
	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testUpdatePosition() throws Exception {
		int top = 0;
		int left = 0;
		int bottom = 100;
		int right = 100;
		double energyLossTop, energyLossBottom, energyLossLeft, energyLossRight, energyLossCollision;
		energyLossTop = energyLossBottom = energyLossLeft = energyLossRight = energyLossCollision = 0.5;
		
		CollisionDetector collisionDetector = new CollisionDetector(
				top, energyLossTop, bottom, energyLossBottom, left, energyLossLeft, right, energyLossRight, energyLossCollision);
		
		int x = 50;
		int y = 50;
		int radius = 10;
		
		Ball ball = new Ball(x, y, radius, new Color(0), 
				new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio), 
				new MotionY(position, velocity, acceleration, pxToMetres, bounceAudio));
	}

}