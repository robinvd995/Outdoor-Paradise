package op.sql.entries;

public class ExcursionEntry {

	private int id;
	private String desc;
	private boolean guide;
	private float price;
	
	public ExcursionEntry(int id, String desc, boolean guide, float price) {
		this.id = id;
		this.desc = desc;
		this.guide = guide;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public boolean isGuide() {
		return guide;
	}

	public float getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Excursion [id=" + id + ", desc=" + desc + ", guide=" + guide + ", price=" + price + "]";
	}
}
