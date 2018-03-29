package op.sql.entries;

public class TripProductBookingEntry {

	private final int tripBookingId;
	private final int tripId;
	private final int productId;
	
	public TripProductBookingEntry(int tripBookingId, int tripId, int productId) {
		this.tripBookingId = tripBookingId;
		this.tripId = tripId;
		this.productId = productId;
	}

	public int getTripBookingId() {
		return tripBookingId;
	}

	public int getTripId() {
		return tripId;
	}

	public int getProductId() {
		return productId;
	}
}
