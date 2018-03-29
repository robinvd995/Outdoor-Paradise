package op.sql.entries;

public class TripExcursionBookingEntry {

	private final int tripId;
	private final int excursionId;
	
	public TripExcursionBookingEntry(int tripId, int excursionId) {
		this.tripId = tripId;
		this.excursionId = excursionId;
	}

	public int getTripId() {
		return tripId;
	}

	public int getExcursionId() {
		return excursionId;
	}
}
