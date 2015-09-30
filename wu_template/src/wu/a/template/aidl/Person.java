package wu.a.template.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {

	private boolean sex;
	private String name;

	public Person() {

	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeByte(sex ? (byte) 1 : 0);
	}

	public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {
		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}

		@Override
		public Person createFromParcel(Parcel in) {
			return new Person(in);
		}
	};

	public Person(Parcel in) {
		sex = in.readByte() == (byte) 1;
		name = in.readString();
	}

}
