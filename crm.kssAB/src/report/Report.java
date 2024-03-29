package report;

import java.io.Serializable;

public class Report implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5065913250896187696L;
	private String title;
	private String introduction;
	private String objectName;
	private int numberOfTransactions;
	private double totalAmount;
	private int reportNumber;
	
	public Report() {
	}
	public Report(String title, String introduction, String objectName, int numberOfTransactions, double totalAmount, int reportNumber) {
		this.title = title;
		this.introduction = introduction;
		this.objectName = objectName;
		this.numberOfTransactions = numberOfTransactions;
		this.totalAmount = totalAmount;
		this.reportNumber = reportNumber;
	}
	// Getters & Setters
	public int getReportNumber() {
		return this.reportNumber;
	}
	
	public void setReportNumber(int reportNumber) {
		this.reportNumber = reportNumber;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public int getNumberOfTransactions() {
		return numberOfTransactions;
	}
	public void setNumberOfTransactions(int numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "Titel: " + title + "\n"
				+ "Introduktion: " + introduction + "\n"
				+ "Namn: " + objectName + "\n"
				+ "Antal Transaktioner: " + numberOfTransactions + "\n"
				+ "Total Summa: " + totalAmount;
	}
	
	
	
}
