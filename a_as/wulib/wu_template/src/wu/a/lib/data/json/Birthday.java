package wu.a.lib.data.json;

public class Birthday {
	private String birthday;

	public Birthday(String birthday) {
		super();
		this.birthday = birthday;
	}
	
	public Birthday() {
	}
	// setter��getter
		
	@Override
	public String toString() {
		return this.birthday;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
