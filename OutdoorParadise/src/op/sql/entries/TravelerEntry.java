package op.sql.entries;

public class TravelerEntry {

	private final String name;
	private final String birthDate;
	
	public TravelerEntry(String name, String birthDate) {
		this.name = name;
		this.birthDate = birthDate;
	}
	
	public String getName() {
		return name;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
}
