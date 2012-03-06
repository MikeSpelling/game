package mike;

import java.applet.AudioClip;
import org.junit.Before;
import org.junit.Test;


public class MotionTest {
	
	private AudioClip bounceAudio;
	private Ball ball;
	
	@Before
	public void before() throws Exception {
	}
	
	@Test
	public void testInitMotion() {
		int position = 10;
		int velocity = 0;
		int acceleration = 10;
		double pxToMetres = 600/10;
		MotionX motionX = new MotionX(position, velocity, acceleration, pxToMetres, bounceAudio);
	}

}