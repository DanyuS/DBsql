package entity;

public class PhonePackage {
	private String ID;
	private String type;
	private int freeCallRange;
	private int freeMailRange;
	private double freeLocalDataRange;
	private double freeDomainDataRange;
	private double basicPay;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFreeCallRange() {
		return freeCallRange;
	}
	public void setFreeCallRange(int freeCallRange) {
		this.freeCallRange = freeCallRange;
	}
	public int getFreeMailRange() {
		return freeMailRange;
	}
	public void setFreeMailRange(int freeMailRange) {
		this.freeMailRange = freeMailRange;
	}
	public double getFreeLocalDataRange() {
		return freeLocalDataRange;
	}
	public void setFreeLocalDataRange(double freeLocalDataRange) {
		this.freeLocalDataRange = freeLocalDataRange;
	}
	public double getFreeDomainDataRange() {
		return freeDomainDataRange;
	}
	public void setFreeDomainDataRange(double freeDomainDataRange) {
		this.freeDomainDataRange = freeDomainDataRange;
	}
	public double getBasicPay() {
		return basicPay;
	}
	public void setBasicPay(double basicPay) {
		this.basicPay = basicPay;
	}
}
