package op.sql.entries;

public class TripBookingEntry {

	private final String date;
	private final String startDate;
	private final float price;
	private final String name;
	private final String address;
	private final String iban;
	private final String birthDate;
	private final String sex;
	private final boolean cancInsurance;
	private final int tripId;
	
	public TripBookingEntry(String date, String startDate, float price, String name, String address, String iban,
			String birthDate, String sex, boolean cancInsurance, int tripId) {
		this.date = date;
		this.startDate = startDate;
		this.price = price;
		this.name = name;
		this.address = address;
		this.iban = iban;
		this.birthDate = birthDate;
		this.sex = sex;
		this.cancInsurance = cancInsurance;
		this.tripId = tripId;
	}

	public String getDate() {
		return date;
	}

	public String getStartDate() {
		return startDate;
	}

	public float getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getIban() {
		return iban;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public String getSex() {
		return sex;
	}

	public boolean isCancInsurance() {
		return cancInsurance;
	}
	
	public int getTripId() {
		return tripId;
	}
}
