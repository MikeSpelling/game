package mike;


/**
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private Boundary minBoundary;
	private Boundary maxBoundary;
	private double boundary = 0;
	private double energyLoss = 0;
	
	
	/**
	 * 
	 * @param radius
	 * @param min
	 * @param energyLossMin
	 * @param max
	 * @param energyLossMax
	 */
	public CollisionDetector(double min, double energyLossMin, double max, double energyLossMax) {
		
		this.minBoundary = new Boundary(min, energyLossMin);
		this.maxBoundary = new Boundary(max, energyLossMax);
	}
	
	public boolean detectBoundary(double displacement) {		
		
		if (displacement >= maxBoundary.location) {
			this.boundary = maxBoundary.location;
			this.energyLoss = maxBoundary.energyLoss;
			return true;
		}
		else if (displacement <= minBoundary.location) {
			this.boundary = minBoundary.location;
			this.energyLoss = minBoundary.energyLoss;
			return true;
		}
		else {
			return false;
		}
	}

	public double getBoundary() {
		return boundary;
	}

	public double getEnergyLoss() {
		return energyLoss;
	}
	
	private class Boundary {
		
		public double location;
		public double energyLoss;
		
		public Boundary(double position, double energyLoss) {
			this.location = position;
			this.energyLoss = energyLoss;
		}
	}

}