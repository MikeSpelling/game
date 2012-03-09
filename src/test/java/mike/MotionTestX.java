package mike;

import static org.junit.Assert.*;

import java.applet.AudioClip;

import org.junit.Before;
import org.junit.Test;


public class MotionTestX {
	
	private AudioClip bounceAudio;
	double pxToMetres = 1;
	
	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testUpdatePosition() throws Exception {
		
		int position = 0;
		int velocity = 0;
		int acceleration = 10;	
		
		Motion motion = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
		motion.startMotion();
		Thread.sleep(1000);
		motion.updatePosition();
		assertEquals("Velocity", 10, motion.getVelocity(), 0.01);
		assertEquals("Displacement", 5, motion.getDisplacement(), 0.01);
		Thread.sleep(4000);
		motion.updatePosition();
		assertEquals("Velocity", 50, motion.getVelocity(), 0.01);
	}

}