package op.sql.entries;

public class TripEntry {

	private int id;
	private String description;
	private int duration;
	private float price;
	private int min_participants;
	private int max_participants;
	private boolean children_allowed;
	
	public TripEntry(int id, String description, int duration, float price, int min_participants, int max_participants, boolean children_allowed) {
		this.id = id;
		this.description = description;
		this.duration = duration;
		this.price = price;
		this.min_participants = min_participants;
		this.max_participants = max_participants;
		this.children_allowed = children_allowed;
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public float getPrice() {
		return price;
	}
	
	public int getMin_participants() {
		return min_participants;
	}
	
	public int getMax_participants() {
		return max_participants;
	}
	
	public boolean isChildren_allowed() {
		return children_allowed;
	}
	
	public String toString(){
		return description;
	}
}
