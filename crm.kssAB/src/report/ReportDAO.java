package report;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Singleton. Innehåller lista över exporterade DAO-object och interna metoder för filhantering.

public class ReportDAO { 

	private List<Report> reportsDAOList = new ArrayList<>();
	private final String filepath = ".DAO.xml";
	
	private static ReportDAO instance;
	
	public static ReportDAO getInstance() {
		if(instance == null) {
			instance = new ReportDAO();
		}
		instance.loadReportsFromFile();
		return instance;
	}
	
	private ReportDAO() {}
	
	public List<Report> getAll(){
		return new ArrayList<>(reportsDAOList);
	}
	
	public Report get(int id) {
		Report reportToReturn = null;
		for(Report report : reportsDAOList) {
			if(report.getReportNumber() == id) {
				reportToReturn = report;
			}
		}
		return reportToReturn;
	}
	
	public void save(Report report) {
		reportsDAOList.add(report);
		saveReportsToFile();
	}
	
	public void update(Report currentReport, Report newReport) { // Söker igenom rapporterna och ersätter med den nya uppdaterade rapporten.
		for (int i = 0; i < reportsDAOList.size(); i++) {
	        Report existingReport = reportsDAOList.get(i);
	        if (existingReport.getReportNumber() == currentReport.getReportNumber()) {
	            reportsDAOList.set(i, newReport); // Ersätt den befintliga rapporten med den nya rapporten
	            saveReportsToFile(); // Spara ändringarna till filen
	            break; // Avsluta loopen eftersom du har hittat och uppdaterat rapporten
	        }
	    }
	}
	
	public void delete(int id) {
        Report reportToRemove = null;
        for (Report report : reportsDAOList) {
            if (report.getReportNumber() == id) {
                reportToRemove = report;
                reportsDAOList.remove(reportToRemove);
            }
        }
	}
	public void loadReportsFromFile() {
		try {
			File file = new File(filepath);

			if (file.exists()) // kollar om filen existerar.
			{
				if(file.length() > 0) 
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
					@SuppressWarnings("unchecked")
					List<Report> deserializedList = (List<Report>) xmlDecoder.readObject();
					xmlDecoder.close();
					fileInputStream.close();
					reportsDAOList.clear();
					reportsDAOList.addAll(deserializedList);
				}
				else {
					System.out.println("File is empty.");
				}
			} else {
				System.out.println("File does not exist: " + filepath);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("File is probably empty.");
		}
	}
	
	public void saveReportsToFile() {
		try {
            
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            xmlEncoder.writeObject(reportsDAOList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
	}
}
