package op.sql.entries;

public class TripProductEntry {

	private int product;
	private int trip;
	private float discount;
	
	public TripProductEntry(int product, int trip, float discount){
		this.product = product;
		this.trip = trip;
		this.discount = discount;
	}
	
	public int getProduct(){
		return product;
	}
	
	public int getTrip(){
		return trip;
	}
	
	public float getDiscount(){
		return discount;
	}

	@Override
	public String toString() {
		return "Product [product=" + product + ", trip=" + trip + ", discount=" + discount + "]";
	}
	
	
}
