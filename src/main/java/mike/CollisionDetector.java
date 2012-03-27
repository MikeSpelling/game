package mike;


/**
 * 
 * @author Mike
 *
 */
public class CollisionDetector {
	
	private Boundary minBoundary;
	private Boundary maxBoundary;
	private Double boundary;
	private Double energyLoss;
	
	
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
	
	public boolean hasHitBoundary(double displacement) {		
		
		if ((displacement >= maxBoundary.location) || (displacement <= minBoundary.location)) {
			return true;
		}
			return false;
	}
	
	public void setTarget(double displacement) {
		if (displacement >= maxBoundary.location) {
			this.boundary = maxBoundary.location;
			this.energyLoss = maxBoundary.energyLoss;
		}
		else if (displacement <= minBoundary.location) {
			this.boundary =  minBoundary.location;
			this.energyLoss = minBoundary.energyLoss;
		}
		else {
			this.boundary = null;
			this.energyLoss = null;
		}
	}

	public Double getBoundary() {
		return this.boundary;
	}

	public Double getEnergyLoss() {
		return this.energyLoss;
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