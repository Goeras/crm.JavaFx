package application;
	
import business.BusinessObjectManager;
import business.Customer;
import business.SaleListener;
import business.Seller;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	
	CrmUserWindow crmUserWindow = new CrmUserWindow();
	
	
	@Override
	public void start(Stage primaryStage) {
		
		SaleListener saleListener = new SaleListener(); // skapar upp ett objekt av lyssnarklassen
		BusinessObjectManager.getInstance().addPropertyChangeListener(saleListener); // Lägger till lyssnare
		BusinessObjectManager.getInstance().deserializeAll();// Läser in objekt från xml.
		BusinessObjectManager.getInstance().addStartingObjects(); // Lägger till dummies om listorna är tomma.
		
		BusinessObjectManager.getInstance().printAllSellersAndCustomersToConsole(); // Konsolprintmetod för utvecklaren under systemets uppbyggnad..
		
		crmUserWindow.logInWindow(); // kör igång programmet.
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
