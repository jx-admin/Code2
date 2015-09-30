package wu.a.wuliu.model;

public class Book {
	private long bookTime;
	private int status;
	private String startAddressName;
	private double startAddressLat;
	private double startAddressLu;
	private String destAddressName;
	private double destAddressLat;
	private double destAddressLu;
	private float fee;
	public long getBookTime() {
		return bookTime;
	}
	public void setBookTime(long bookTime) {
		this.bookTime = bookTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStartAddressName() {
		return startAddressName;
	}
	public void setStartAddressName(String startAddressName) {
		this.startAddressName = startAddressName;
	}
	public double getStartAddressLat() {
		return startAddressLat;
	}
	public void setStartAddressLat(double startAddressLat) {
		this.startAddressLat = startAddressLat;
	}
	public double getStartAddressLu() {
		return startAddressLu;
	}
	public void setStartAddressLu(double startAddressLu) {
		this.startAddressLu = startAddressLu;
	}
	public String getDestAddressName() {
		return destAddressName;
	}
	public void setDestAddressName(String destAddressName) {
		this.destAddressName = destAddressName;
	}
	public double getDestAddressLat() {
		return destAddressLat;
	}
	public void setDestAddressLat(double destAddressLat) {
		this.destAddressLat = destAddressLat;
	}
	public double getDestAddressLu() {
		return destAddressLu;
	}
	public void setDestAddressLu(double destAddressLu) {
		this.destAddressLu = destAddressLu;
	}
	public float getFee() {
		return fee;
	}
	public void setFee(float fee) {
		this.fee = fee;
	}

}
