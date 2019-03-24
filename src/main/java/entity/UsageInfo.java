package entity;

public class UsageInfo {
	private String userID;
	private String packageID;
	private int callUsage;
	private int freeCallRemain;
	private int mailUsage;
	private int freeMailRemain;
	private double localDataUsage;
	private double freeLocalDataRemain;
	private double domainDataUsage;
	private double freeDomainDataRemain;
	private double account;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPackageID() {
		return packageID;
	}
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	public int getCallUsage() {
		return callUsage;
	}
	public void setCallUsage(int callUsage) {
		this.callUsage = callUsage;
	}
	public int getFreeCallRemain() {
		return freeCallRemain;
	}
	public void setFreeCallRemain(int freeCallRemain) {
		this.freeCallRemain = freeCallRemain;
	}
	public int getMailUsage() {
		return mailUsage;
	}
	public void setMailUsage(int mailUsage) {
		this.mailUsage = mailUsage;
	}
	public int getFreeMailRemain() {
		return freeMailRemain;
	}
	public void setFreeMailRemain(int freeMailRemain) {
		this.freeMailRemain = freeMailRemain;
	}
	public double getLocalDataUsage() {
		return localDataUsage;
	}
	public void setLocalDataUsage(double localDataUsage) {
		this.localDataUsage = localDataUsage;
	}
	public double getFreeLocalDataRemain() {
		return freeLocalDataRemain;
	}
	public void setFreeLocalDataRemain(double freeLocalDataRemain) {
		this.freeLocalDataRemain = freeLocalDataRemain;
	}
	public double getDomainDataUsage() {
		return domainDataUsage;
	}
	public void setDomainDataUsage(double domainDataUsage) {
		this.domainDataUsage = domainDataUsage;
	}
	public double getFreeDomainDataRemain() {
		return freeDomainDataRemain;
	}
	public void setFreeDomainDataRemain(double freeDomainDataRemain) {
		this.freeDomainDataRemain = freeDomainDataRemain;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
}
