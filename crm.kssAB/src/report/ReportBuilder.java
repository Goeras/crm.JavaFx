package report;

public class ReportBuilder {

	private Report report =  new Report();
	
	public ReportBuilder addTitle(String title) {
		report.setTitle(title);
		return this;
	}
	
	public ReportBuilder addIntroduction(String introduction) {
		report.setIntroduction(introduction);
		return this;
	}
	
	public ReportBuilder addObjectName(String objectName) {
		report.setObjectName(objectName);
		return this;
	}
	
	public ReportBuilder addNumberOfTransactions(int numberOfTransactions) {
		report.setNumberOfTransactions(numberOfTransactions);
		return this;
	}
	
	public ReportBuilder addTotalAmount(double totalAmount) {
		report.setTotalAmount(totalAmount);;
		return this;
	}
	
	public ReportBuilder addReportNumber() {
		report.setReportNumber(ReportManager.getInstance().createUniqueReportId());
		return this;
	}
	
	public Report build() {
		if(report.getTitle().equals(null))
			throw new RuntimeException("Title missing");
		return report;
	}
	
}
