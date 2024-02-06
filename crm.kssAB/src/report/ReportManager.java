package report;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Singleton för att säkerställa att det ej skapas upp flera objekt och listor ur denna klass.
public class ReportManager {

	List<Report> listOfAllReports = new ArrayList<>(); // Sparas ej till fil. enbart exporterade DAO-rapporter sparas till fil.
	
	private static ReportManager instance;
	
	private ReportManager() {}
	
	public static ReportManager getInstance() {
		if(instance == null) {
			instance = new ReportManager();
		}
		return instance;
	}

	
	public Report createNewReport(String title, String introduction, String objectName, int numberOfTrasactions, double amount) {
		ReportBuilder builder = new ReportBuilder();
		Report report = builder
				.addTitle(title)
				.addIntroduction(introduction)
				.addObjectName(objectName)
				.addNumberOfTransactions(numberOfTrasactions)
				.addTotalAmount(amount)
				.addReportNumber()
				.build();
		
		addReport(report);
		return report;
	}
	
	public void createExportDAO(Report report) {
		ReportDAO.getInstance().save(report);
	}	
	
	public void addReport(Report report) {
		listOfAllReports.add(report);
	}
	
	public List<Report> getAllReports(){
		return listOfAllReports;
	}
	
	public int createUniqueReportId() {
		Random random = new Random();
		int randomReportNumber = random.nextInt(900000)+100000; // Skapar ett random bokningsnummer mellan 10000 och 99999.
		
		for(Report report : listOfAllReports) // Kontrollerar om bokningsnumret är unikt eller redan finns i systemet.
		{
			if(report.getReportNumber() == randomReportNumber)
			{
				return createUniqueReportId(); // om bokningsnumret redan finns så upprepas metoden tills dess att ett unikt nummer slumpats fram.
			}
		}
		return randomReportNumber; // Returnerar ett unikt bokningsnummer.
	}
	
	
	
}
