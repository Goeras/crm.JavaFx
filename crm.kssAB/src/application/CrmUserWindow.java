package application;

import java.time.LocalDate;

import business.BusinessObjectManager;
import business.CrmSystem;
import business.Customer;
import business.Sale;
import business.Seller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CrmUserWindow {

	CrmSystem crmSystem = new CrmSystem();
	AlertBox alertBox = new AlertBox();
	ChoiceBox<Customer> choiceBoxCustomer = new ChoiceBox<>();

	Seller seller;

	public void crmPrimaryWindow(Seller seller) { 
		
		// Hämtar säljarens lista på kunder.
		ObservableList<Customer> observableCustomersList = 
				FXCollections.observableArrayList(seller.getCustomers());
		
		Label messageLabel = new Label();
		messageLabel.setText("Nya meddelanden");
		
		// Hämtar och visar säljarens Queue för meddelanden i en ListView.
		ObservableList<String> observableStringList = FXCollections.observableArrayList(seller.pollMessagesToList());
		ListView<String> messageView = new ListView<>(observableStringList);
		messageView.setPrefHeight(300);
		
		Stage stage = new Stage();
		stage.setTitle("Inloggad som " + seller.getName() + ", ID: " + seller.getId()); // Visa inloggad säljare.
		
		Label showAllCustomersLabel = new Label(); // Tom label bara för att passa bättre med boxen bredvid.
		
		Button viewAllCustomersButton = new Button("Visa alla kunder");
		viewAllCustomersButton.setOnAction(e -> showAllCustomersWindow());
		
		Button createNewSaleButton = new Button("Registrera ny försäljning");
		createNewSaleButton.setOnAction( e ->{
			newSaleWindow();
		});
		
		// Sätt alla värden i choiceBoxCustomer.
		choiceBoxCustomer.setItems(observableCustomersList);
		choiceBoxCustomer.getItems().add(null); // Bara för att få ett blankt alternativ = null, visar alla säljarens sälj.
		choiceBoxCustomer.setValue(null);
		
		Label saleViewLabel = new Label();
		saleViewLabel.setText("Välj kund eller lämna blankt för alla");
		
		// Visa försäljningar baserat på val ifrån choiceBoxCustomer.
		Button showSalesButton = new Button("Dina försäljningar");
		showSalesButton.setOnAction(e -> showSalesWindow(choiceBoxCustomer.getValue()));
		
		Label reportLabel = new Label();
		reportLabel.setText("Alternativ för att skapa rapport");
		
		ChoiceBox<String> choiceBoxReport = new ChoiceBox<>();
		choiceBoxReport.getItems().addAll("Alla", "Kund", "Produkt");
		choiceBoxReport.setValue("Alla");
		
		Button createReportButton = new Button("Skapa rapport");
		createReportButton.setOnAction(e -> {
			showReportWindow(choiceBoxReport.getValue());
		});
		
		Button exitButton = new Button("Logga ut");
		exitButton.setOnAction(e -> stage.close());
		
		VBox saleViewBox = new VBox();
		saleViewBox.setAlignment(Pos.CENTER);
		saleViewBox.setPadding(new Insets(20));
		saleViewBox.setSpacing(20);
		saleViewBox.getChildren().addAll(saleViewLabel, choiceBoxCustomer, showSalesButton);
		
		VBox newSaleBox = new VBox();
		newSaleBox.setAlignment(Pos.CENTER);
		newSaleBox.setPadding(new Insets(20));
		newSaleBox.setSpacing(20);
		newSaleBox.getChildren().addAll(showAllCustomersLabel, createNewSaleButton, viewAllCustomersButton);
		
		
		HBox hBoxTop = new HBox();
		hBoxTop.setAlignment(Pos.CENTER);
		hBoxTop.setPadding(new Insets(20));
		hBoxTop.setSpacing(20);
		hBoxTop.getChildren().addAll(saleViewBox, newSaleBox);
		
		VBox hBoxCenter = new VBox();
		hBoxCenter.setAlignment(Pos.CENTER);
		hBoxCenter.setPadding(new Insets(20));
		hBoxCenter.setSpacing(20);
		hBoxCenter.getChildren().addAll(reportLabel, choiceBoxReport, createReportButton);
		
		HBox hBoxBottom = new HBox();
		hBoxBottom.setAlignment(Pos.CENTER_RIGHT);
		hBoxBottom.setPadding(new Insets(20));
		hBoxBottom.setSpacing(20);
		hBoxBottom.getChildren().addAll(exitButton);
		
		VBox vBoxRight = new VBox();
		vBoxRight.setAlignment(Pos.CENTER);
		vBoxRight.setPadding(new Insets(20));
		vBoxRight.setSpacing(20);
		vBoxRight.getChildren().addAll(messageLabel, messageView);
		
		BorderPane layout = new BorderPane();
		layout.setTop(hBoxTop);
		layout.setCenter(hBoxCenter);
		layout.setBottom(hBoxBottom);
		layout.setRight(vBoxRight);
		
		Scene scene = new Scene(layout, 600, 600);
		
		stage.setScene(scene);
		stage.show();

	}
	
	public void showReportWindow(String string) {
		
	}
	
	public void showAllCustomersWindow() {
		// Visa alla kunder i systemet.
	}
	
	@SuppressWarnings("unchecked")
	public void showSalesWindow(Customer customer) { 
		Stage showSalesStage = new Stage();
		showSalesStage.setTitle("Dina försäljningar");
		
		ObservableList<Sale> observableSalesList = 
				FXCollections.observableArrayList(BusinessObjectManager.getInstance()
						.getListOfSalesByCustomer(customer, seller));

		Label label = new Label();
		label.setAlignment(Pos.CENTER);
		if(!(customer==null)) {
		label.setText("Kund: "+customer.getName()+"\n"
				+ "ID: "+customer.getId()+"\n"
				+ "Adress: "+customer.getAdress());
		}
		
		TableView<Sale> tableView = new TableView<>(observableSalesList);
		
		TableColumn<Sale, String> nameColumn = new TableColumn<>("Produktnamn");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
		
		TableColumn<Sale, Integer> idColumn = new TableColumn<>("Försäljning ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("saleId"));
		
		TableColumn<Sale, Double> priceColumn = new TableColumn<>("Pris");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		
		TableColumn<Sale, Integer> quantityColumn = new TableColumn<>("Antal");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfItems"));
		
		TableColumn<Sale, LocalDate> dateColumn = new TableColumn<>("Datum");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<Sale, Customer> customerColumn = new TableColumn<>("Kund");
		customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
		
		tableView.getColumns().addAll(nameColumn, idColumn, priceColumn, quantityColumn, dateColumn, customerColumn);
		
		Scene scene = new Scene(tableView, 480, 500);
		showSalesStage.setScene(scene);
		showSalesStage.show();
	}
	
	public void newSaleWindow() {
		Stage newSaleStage = new Stage();
		newSaleStage.setTitle("Registrera ny försäljning");
		
		TextField productNameField = new TextField();
		productNameField.setPromptText("Produktnamn");
		productNameField.setMaxWidth(110);
		
		TextField productPriceField = new TextField();
		productPriceField.setPromptText("Pris på produkt");
		productPriceField.setMaxWidth(110);
		
		TextField numberOfProductsField = new TextField(); 
		numberOfProductsField.setPromptText("Antal produkter");
		numberOfProductsField.setMaxWidth(110);
		
		TextField customerIdField = new TextField(); // OBS: Här vore möjligt att lägga en typ choicebox med befintliga kunder istället för att skriva in ID.
		customerIdField.setPromptText("Kundens ID");
		customerIdField.setMaxWidth(110);
		
		Button newCustomerButton = new Button("Skapa ny kund"); 
		newCustomerButton.setOnAction(e -> { // Skapa nytt kundkonto.
			
		});
		
		Button createSale = new Button("Spara försäljningen");
		createSale.setOnAction(e -> {
			boolean isPriceDouble = BusinessObjectManager.getInstance().validateIfInputIsDouble(productPriceField.getText());
			boolean isNumberOfItemsInteger = BusinessObjectManager.getInstance().validateIfInputIsDouble(numberOfProductsField.getText());
			Customer customer = BusinessObjectManager.getInstance().findCustomerById(Integer.parseInt(customerIdField.getText()));
			if(isPriceDouble && isNumberOfItemsInteger && !(customer==null)) {
				BusinessObjectManager.getInstance().createNewSale(seller, customer, productNameField.getText(), productPriceField.getText(), numberOfProductsField.getText());
				alertBox.confirmBox("Försäljning registrerad", "Försäljning till "+customer.getId()+" sparad");
				productNameField.clear();
				productPriceField.clear();
				numberOfProductsField.clear();
				customerIdField.clear();
				if(!choiceBoxCustomer.getItems().contains(customer)) {
					choiceBoxCustomer.getItems().add(customer);
					}
			}
			else {
				alertBox.confirmBox("Kan ej registrera försäljning", "Kontrollera uppgifterna");
			}
		});
		
		Button exitButton = new Button("Tillbaka");
		exitButton.setOnAction(e -> newSaleStage.close());
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.getChildren().addAll(newCustomerButton, productNameField, productPriceField, numberOfProductsField, customerIdField, createSale, exitButton);
		
		Scene scene = new Scene(vBox, 200, 350);
		newSaleStage.setScene(scene);
		newSaleStage.show();
		
	}


	public void logInWindow() { 
try {
			Stage logInStage = new Stage();
			
			Label infoLabel = new Label();
			infoLabel.setText("Logga in med ditt ID eller skapa nytt konto");
			
			TextField idField = new TextField();
			idField.setPromptText("ID");
			idField.setMaxWidth(110);
	        
	        TextField passWordField = new TextField();
	        passWordField.setPromptText("Lösenord"); // val att sätta denna text till dold *** ?
	        passWordField.setMaxWidth(110);
	        
			Button logInButton = new Button("Logga in");
			logInButton.setOnAction(e -> {
				seller = BusinessObjectManager.getInstance().logInValidation(idField.getText(), passWordField.getText());
				if(!(seller == null)) {
					crmPrimaryWindow(seller);
					idField.clear();
					passWordField.clear();
				}
				else {
					alertBox.confirmBox("Inloggnings misslyckades", "");
				}
			});
			
			Button newSellerButton = new Button("Nytt säljarkonto");
			newSellerButton.setOnAction(e -> {
				createSellerWindow();
			});
			
			VBox vBox = new VBox();
			vBox.setAlignment(Pos.CENTER);
			vBox.getChildren().addAll(infoLabel, idField, passWordField, logInButton, newSellerButton);
			vBox.setSpacing(20);

			Scene scene = new Scene(vBox,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			logInStage.setTitle("Kundhanteringssystem 3000");
			logInStage.setScene(scene);
			logInStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createSellerWindow() {
		
		Stage stage = new Stage();
		stage.setTitle("Skapa nytt konto");
		
		TextField nameField = new TextField();
		nameField.setPromptText("Namn");
		nameField.setMaxWidth(110);
		
		TextField adressField = new TextField();
		adressField.setPromptText("Adress");
		adressField.setMaxWidth(110);
        
        TextField passWordField = new TextField();
        passWordField.setPromptText("Lösenord"); // val att sätta denna text till dold *** ?
        passWordField.setMaxWidth(110);
        
        TextField passWordFieldTwo = new TextField();
        passWordFieldTwo.setPromptText("Upprepa Lösenord"); // val att sätta denna text till dold *** ?
        passWordFieldTwo.setMaxWidth(110);
        
        Button createButton = new Button("Skapa Säljare");
        createButton.setOnAction(e ->{
        	if (!nameField.getText().isEmpty() && !adressField.getText().isEmpty() && !passWordField.getText().isEmpty() && passWordField.getText().equals(passWordFieldTwo.getText())){
        		int id = BusinessObjectManager.getInstance().createNewSeller(nameField.getText(), adressField.getText(), passWordField.getText());
        		if(!(id==0)) {
        			alertBox.confirmBox("Konto skapat", "Ditt ID: "+id+"\n\n(Kom ihåg ditt ID och lösenord)");
        			stage.close();
        		}
        	}
        	else {
        		alertBox.confirmBox("Gick ej att skapa konto", "Kontrollera att alla fält är korrekt ifyllda.");
        	}
        });
        
        Button exitButton = new Button("Avbryt");
        exitButton.setOnAction(e -> stage.close());
		
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(nameField, adressField, passWordField, passWordFieldTwo, createButton, exitButton);
        
        Scene scene = new Scene(vBox, 400, 400);
        stage.setScene(scene);
        stage.show();
	}
	
	public void createCustomerWindow() {
		
		Stage stage = new Stage();
		stage.setTitle("Skapa nytt konto");
		
		TextField nameField = new TextField();
		nameField.setPromptText("Namn");
		nameField.setMaxWidth(110);
		
		TextField adressField = new TextField();
		adressField.setPromptText("Adress");
		adressField.setMaxWidth(110);
        
        Button createButton = new Button("Skapa Kund");
        createButton.setOnAction(e ->{
        	if (!nameField.getText().isEmpty() && !adressField.getText().isEmpty()){
        		int id = BusinessObjectManager.getInstance().createNewCustomer(nameField.getText(), adressField.getText());
        		if(!(id==0)) {
        			alertBox.confirmBox("Kund tillagd", "Kundens ID: "+id);
        			stage.close();
        		}
        	}
        	else {
        		alertBox.confirmBox("Gick ej att skapa kund", "Kontrollera att alla fält är korrekt ifyllda.");
        	}
        });
        
        Button exitButton = new Button("Avbryt");
        exitButton.setOnAction(e -> stage.close());
		
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(nameField, adressField, createButton, exitButton);
        
        Scene scene = new Scene(vBox, 400, 400);
        stage.setScene(scene);
        stage.show();
	}
}
