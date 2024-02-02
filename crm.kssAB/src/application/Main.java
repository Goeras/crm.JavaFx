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
		
		SaleListener saleListener = new SaleListener();
		BusinessObjectManager.getInstance().addPropertyChangeListener(saleListener);
		//BusinessObjectManager businessObjectManager = BusinessObjectManager.getInstance();
		//businessObjectManager.addPropertyChangeListener(saleListener);
		
		BusinessObjectManager.getInstance().addStartingObjects();
		BusinessObjectManager.getInstance().printAllSellersAndCustomersToConsole(); // Konsolprintmetod för utvecklaren under systemets uppbyggnad..
		
		crmUserWindow.logInWindow();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
