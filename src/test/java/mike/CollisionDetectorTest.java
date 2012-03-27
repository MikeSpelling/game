package mike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class CollisionDetectorTest {
	
	CollisionDetector collisionDetector;
	double min = 1.5;
	double max = 9.5;
	double energyLossMin = 0.1;
	double energyLossMax = 0.2;
	
	
	@Test
	public void testHasHitBoundary() throws Exception {
		collisionDetector = new CollisionDetector(min, energyLossMin, max, energyLossMax);
		
		assertTrue("Should be true", collisionDetector.hasHitBoundary(1));
		assertTrue("Should be true", collisionDetector.hasHitBoundary(10));
		assertTrue("Should be true", collisionDetector.hasHitBoundary(-1));
		assertFalse("Should be false", collisionDetector.hasHitBoundary(5));
	}
	
	@Test
	public void testSetAndGet() throws Exception {
		double errorMargin = 0;
		collisionDetector = new CollisionDetector(min, energyLossMin, max, energyLossMax);
		
		collisionDetector.setTarget(1);
		assertEquals(min, collisionDetector.getBoundary(), errorMargin);
		assertEquals(energyLossMin, collisionDetector.getEnergyLoss(), errorMargin);
		collisionDetector.setTarget(10);
		assertEquals(max, collisionDetector.getBoundary(), errorMargin);
		assertEquals(energyLossMax, collisionDetector.getEnergyLoss(), errorMargin);
		collisionDetector.setTarget(-1);
		assertEquals(min, collisionDetector.getBoundary(), errorMargin);
		assertEquals(energyLossMin, collisionDetector.getEnergyLoss(), errorMargin);
		collisionDetector.setTarget(5);
		assertEquals(null, collisionDetector.getBoundary());
		assertEquals(null, collisionDetector.getEnergyLoss());
	}

}