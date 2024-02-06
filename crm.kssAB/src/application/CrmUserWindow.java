package application;

import java.time.LocalDate;

import business.BusinessObjectManager;
import business.Customer;
import business.Sale;
import business.Seller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import report.Report;
import report.ReportManager;

public class CrmUserWindow {

	AlertBox alertBox = new AlertBox();
	ChoiceBox<Customer> choiceBoxCustomer = new ChoiceBox<>(); // choicebox för säljarens kunder.
	Stage logInStage;

	Seller seller; // = inloggad säjare.

	public void crmPrimaryWindow(Seller seller) { 
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Inloggad som " + seller.getName() + ", ID: " + seller.getId()); // Visa inloggad säljare.
		
		// Hämtar säljarens lista på kunder.
		ObservableList<Customer> observableCustomersList = 
				FXCollections.observableArrayList(seller.getCustomers());
		
		Label messageLabel = new Label();
		messageLabel.setText("Nya meddelanden");
		
		// Hämtar och visar säljarens Queue för meddelanden i en ListView.
		ObservableList<String> observableStringList = FXCollections.observableArrayList(seller.pollMessagesToList());
		ListView<String> messageView = new ListView<>(observableStringList);
		messageView.setPrefHeight(300);
		
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
		  choiceBoxReport.getItems().addAll("Alla dina sälj", "Specifik kund", "Specifik Produkt");
		  choiceBoxReport.setValue("Alla dina sälj");
		 
		
		Button createReportButton = new Button("Skapa rapport");
		createReportButton.setOnAction(e -> {
			if(choiceBoxReport.getValue().equals("Alla dina sälj")) {
				createReportBySellerWindow();
			}
			else if(choiceBoxReport.getValue().equals("Specifik kund")) {
				createReportByCustomerWindow();
			}
			else if(choiceBoxReport.getValue().equals("Specifik Produkt")) {
				createReportByProductWindow();
			}
		});
		
		Button showAllReportsButton = new Button("Visa & Exportera");
		showAllReportsButton.setOnAction( e -> showAllReportsWindow());
		
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
		hBoxCenter.getChildren().addAll(reportLabel, choiceBoxReport, createReportButton, showAllReportsButton);
		
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
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();

	}
	public void createReportBySellerWindow() { // createNewReport(String title, String introduction, String objectName, int numberOfTrasactions, double amount)
		Stage createSellerReportStage = new Stage();
		createSellerReportStage.initModality(Modality.APPLICATION_MODAL);
		createSellerReportStage.setTitle("Ny rapport");
		
		String objectName = seller.getName();
		int numberOfSales = seller.getSales().size();
		double totalAmount = BusinessObjectManager.getInstance().getTotalSellAmountFromSeller(seller);
		
		Label label = new Label();
		label.setText("Fyll i 'Titel' och 'Introduktion' i fälten nedan.");
		
		TextField titleField = new TextField(); // titel, intro, objektnamn, antal, summa.
		titleField.setPromptText("Titel");
		titleField.setMaxWidth(110);
		
		TextField introductionField = new TextField();
		introductionField.setPromptText("Introduktion");
		introductionField.setMaxWidth(110);
		
		Button createButton = new Button("Skapa");
		createButton.setOnAction(e -> {
			Report report = ReportManager.getInstance().createNewReport(titleField.getText(), introductionField.getText(), objectName, numberOfSales, totalAmount);
			alertBox.alertBox("Ny rapport skapad", "Rapportnummer: "+report.getReportNumber());
			createSellerReportStage.close();
		});
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, titleField, introductionField, createButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(10));
		
		Scene scene = new Scene(vBox, 300, 300);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		createSellerReportStage.setScene(scene);
		createSellerReportStage.show();
	}
	
	public void createReportByProductWindow(){ // createNewReport(String title, String introduction, String objectName, int numberOfTrasactions, double amount)
		Stage createProductReportStage = new Stage();
		createProductReportStage.initModality(Modality.APPLICATION_MODAL);
		createProductReportStage.setTitle("Ny rapport");
		
		Label label = new Label();
		label.setText("Fyll i 'Titel', 'Introduktion'\noch produktens namn i fälten nedan.");
		
		TextField titleField = new TextField(); // titel, intro, objektnamn, antal, summa.
		titleField.setPromptText("Titel");
		titleField.setMaxWidth(110);
		
		TextField introductionField = new TextField();
		introductionField.setPromptText("Introduktion");
		introductionField.setMaxWidth(110);
		
		TextField productField = new TextField();
		productField.setPromptText("Produkt");
		productField.setMaxWidth(110);
		
		Button createButton = new Button("Skapa");
		createButton.setOnAction(e -> {
			try {
				int numberOfPurchases = BusinessObjectManager.getInstance().getTotalNumberOfSalesForProduct(productField.getText());
				double totalAmount = BusinessObjectManager.getInstance().getTotalSaleAmountOfProduct(productField.getText());
				Report report = ReportManager.getInstance().createNewReport(titleField.getText(), introductionField.getText(), productField.getText(), numberOfPurchases, totalAmount);
				alertBox.alertBox("Ny rapport skapad", "Rapportnummer: "+report.getReportNumber());
				createProductReportStage.close();
				
			}
			catch(NumberFormatException nfe){
				alertBox.alertBox("Fel inmatning", "Kontrollera ifyllda fält.");
			}
		});
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, titleField, introductionField, productField, createButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(10));
		
		Scene scene = new Scene(vBox, 300, 300);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		createProductReportStage.setScene(scene);
		createProductReportStage.show();
	}
	
	public void createReportByCustomerWindow() { // createNewReport(String title, String introduction, String objectName, int numberOfTrasactions, double amount)
		Stage createCustomerReportStage = new Stage();
		createCustomerReportStage.initModality(Modality.APPLICATION_MODAL);
		createCustomerReportStage.setTitle("Ny rapport");
		
		Label label = new Label();
		label.setText("Fyll i 'Titel', 'Introduktion'\noch kundens ID i fälten nedan.");
		
		TextField titleField = new TextField(); // titel, intro, objektnamn, antal, summa.
		titleField.setPromptText("Titel");
		titleField.setMaxWidth(110);
		
		TextField introductionField = new TextField();
		introductionField.setPromptText("Introduktion");
		introductionField.setMaxWidth(110);

		TextField idField = new TextField();
		idField.setPromptText("Kundens ID");
		idField.setMaxWidth(110);

		Button createButton = new Button("Skapa");
		createButton.setOnAction(e -> {
			try {
				int id = Integer.parseInt(idField.getText());
				Customer customer = BusinessObjectManager.getInstance().findCustomerById(id);
				int numberOfPurchases = BusinessObjectManager.getInstance().getNumberOfItemsPerCustomer(customer);
				double totalAmount = BusinessObjectManager.getInstance().getTotalPurchaseAmountFromCustomer(customer);
				Report report = ReportManager.getInstance().createNewReport(titleField.getText(), introductionField.getText(), customer.getName(), numberOfPurchases, totalAmount);
				alertBox.alertBox("Ny rapport skapad", "Rapportnummer: "+report.getReportNumber());
				createCustomerReportStage.close();
				
			}
			catch(NumberFormatException nfe){
				alertBox.alertBox("Fel inmatning", "Kontrollera ifyllda fält.");
			}
		});
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, titleField, introductionField, idField, createButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(10));
		
		Scene scene = new Scene(vBox, 300, 300);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		createCustomerReportStage.setScene(scene);
		createCustomerReportStage.show();
	}
	
	public void showAllReportsWindow() {
		Stage showAllReportStage = new Stage();
		showAllReportStage.initModality(Modality.APPLICATION_MODAL);
		showAllReportStage.setTitle("Alla Raporter");
		ObservableList<Report> reportList = FXCollections.observableArrayList(ReportManager.getInstance().getAllReports());
		
		ListView<Report >listView = new ListView<>(reportList);
        listView.setPrefSize(400, 300);
        
        listView.setCellFactory(new Callback<ListView<Report>, ListCell<Report>>() {
            @Override
            public ListCell<Report> call(ListView<Report> param) {
                return new ListCell<Report>() {
                    @Override
                    protected void updateItem(Report item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.toString());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        
        Button exportButton = new Button("Exportera rapport");
        exportButton.setOnAction(e -> {
        	Report selectedReport = listView.getSelectionModel().getSelectedItem();
            if (selectedReport != null) {
            	exportDAOWindow(selectedReport);
            	 //ReportManager.getInstance().createExportDAO(selectedReport);
            	 //alertBox.alertBox("Exportering lyckad", "Rapport "+selectedReport.getReportNumber()+" exporterad till XML");
            	 showAllReportStage.close();
            }
        });
        
        Button exitButton = new Button("Avsluta");
        exitButton.setOnAction(e -> showAllReportStage.close());
        
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(listView, exportButton, exitButton);

        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        showAllReportStage.setScene(scene);
        showAllReportStage.show();
        
	}
	
	public void exportDAOWindow(Report report) {
		Stage stage = new Stage();
		stage.setTitle("Val för export");
		stage.initModality(Modality.APPLICATION_MODAL);
		
		CheckBox titleCheckBox = new CheckBox("Titel: "+report.getTitle());
        CheckBox introductionCheckBox = new CheckBox("Intro: "+report.getIntroduction());
        CheckBox objectNameCheckBox = new CheckBox("Namn: "+report.getObjectName());
        CheckBox numberOfTransactionsCheckBox = new CheckBox("Antal Transaktioner: "+Integer.toString(report.getNumberOfTransactions())); // Konvertera int till sträng
        CheckBox totalAmountCheckBox = new CheckBox("Total Summa: "+Double.toString(report.getTotalAmount())); // Konvertera double till sträng
		
        Button exportButton = new Button("Bekräfta val och exportera");
        exportButton.setOnAction(e ->{
        	String title = "";
        	String intro = "";
        	String objectName = "";
        	int transactionVolume = 0;
        	double transactionAmount = 0;
        	
        	if (titleCheckBox.isSelected()) {
                title = report.getTitle();
            }
            if (introductionCheckBox.isSelected()) {
                intro = report.getIntroduction();
            }
            if (objectNameCheckBox.isSelected()) {
                objectName = report.getObjectName();
            }
            if (numberOfTransactionsCheckBox.isSelected()) {
                transactionVolume = report.getNumberOfTransactions();
            }
            if (totalAmountCheckBox.isSelected()) {
                transactionAmount = report.getTotalAmount();
            }
            
            Report newReport = ReportManager.getInstance().createNewReport(
            		title, intro, objectName, transactionVolume, transactionAmount);
            ReportManager.getInstance().createExportDAO(newReport);
            alertBox.alertBox("Rapport Exporterad", "ID för exporterad rapport: "+newReport.getReportNumber());
            stage.close();
        });
        
        Button exitButton = new Button("Avbryt");
        exitButton.setOnAction(e -> stage.close());
        
		VBox vBoxCheck = new VBox();
		vBoxCheck.setAlignment(Pos.CENTER_LEFT);
		vBoxCheck.setSpacing(20);
		vBoxCheck.setPadding(new Insets(10));
		vBoxCheck.getChildren().addAll(titleCheckBox, introductionCheckBox, objectNameCheckBox, 
				numberOfTransactionsCheckBox, totalAmountCheckBox, exportButton);
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(20);
		hBox.setPadding(new Insets(10));
		hBox.getChildren().addAll(exportButton, exitButton);
		
		VBox layout = new VBox();
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(20);
		layout.setPadding(new Insets(10));
		layout.getChildren().addAll(vBoxCheck, hBox);
		
		Scene scene = new Scene(layout, 500, 300);
		stage.setScene(scene);
		stage.show();
        
	}
	
	@SuppressWarnings("unchecked")
	public void showAllCustomersWindow() {
		Stage showAllCustomersStage = new Stage();
		showAllCustomersStage.setTitle("Alla kunder i systemet");
		showAllCustomersStage.initModality(Modality.APPLICATION_MODAL);
		
		ObservableList<Customer> observableSalesList = 
				FXCollections.observableArrayList(BusinessObjectManager.getInstance()
						.getListOfAllCustomers());
		
		TableView<Customer> tableView = new TableView<>(observableSalesList);
		
		TableColumn<Customer, String> nameColumn = new TableColumn<>("Namn");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		TableColumn<Customer, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		TableColumn<Customer, String> adressColumn = new TableColumn<>("Adress");
		adressColumn.setCellValueFactory(new PropertyValueFactory<>("adress"));
		
		tableView.getColumns().addAll(nameColumn, idColumn, adressColumn);
		
		Button exitButton = new Button("Stäng fönster");
		exitButton.setOnAction(e -> showAllCustomersStage.close());
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.setPadding(new Insets(10));
		vBox.getChildren().addAll(tableView, exitButton);
		
		Scene scene = new Scene(vBox, 300, 300);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		showAllCustomersStage.setScene(scene);
		showAllCustomersStage.show();
		
	}
	
	@SuppressWarnings("unchecked")
	public void showSalesWindow(Customer customer) { 
		Stage showSalesStage = new Stage();
		showSalesStage.initModality(Modality.APPLICATION_MODAL);
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
		
		Scene scene = new Scene(tableView, 500, 500);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		showSalesStage.setScene(scene);
		showSalesStage.show();
	}
	
	public void newSaleWindow() {
		Stage newSaleStage = new Stage();
		newSaleStage.initModality(Modality.APPLICATION_MODAL);
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
			createCustomerWindow();
		});
		
		Button createSale = new Button("Spara försäljningen");
		createSale.setOnAction(e -> {
			boolean isPriceDouble = BusinessObjectManager.getInstance().validateIfInputIsDouble(productPriceField.getText());
			boolean isNumberOfItemsInteger = BusinessObjectManager.getInstance().validateIfInputIsDouble(numberOfProductsField.getText());
			Customer customer = BusinessObjectManager.getInstance().findCustomerById(Integer.parseInt(customerIdField.getText()));
			if(isPriceDouble && isNumberOfItemsInteger && !(customer==null)) {
				BusinessObjectManager.getInstance().createNewSale(seller, customer, productNameField.getText(), productPriceField.getText(), numberOfProductsField.getText());
				alertBox.alertBox("Försäljning registrerad", "Försäljning till "+customer.getId()+" sparad");
				productNameField.clear();
				productPriceField.clear();
				numberOfProductsField.clear();
				customerIdField.clear();
				if(!choiceBoxCustomer.getItems().contains(customer)) {
					choiceBoxCustomer.getItems().add(customer);
					}
			}
			else {
				alertBox.alertBox("Kan ej registrera försäljning", "Kontrollera uppgifterna");
			}
		});
		
		Button exitButton = new Button("Tillbaka");
		exitButton.setOnAction(e -> newSaleStage.close());
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(20);
		vBox.getChildren().addAll(newCustomerButton, productNameField, productPriceField, numberOfProductsField, customerIdField, createSale, exitButton);
		
		Scene scene = new Scene(vBox, 200, 350);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		newSaleStage.setScene(scene);
		newSaleStage.show();
		
	}


	public void logInWindow() { 
try {
			logInStage = new Stage();
			logInStage.initModality(Modality.APPLICATION_MODAL);
			
			logInStage.setOnCloseRequest( e -> { // event då användaren stänger programmet på krysset uppe i hörnet.
				e.consume(); // consumar användarens val att stänga programmet, anropar istället metod closeProgram()
				closeProgram(); // closeProgram öppnar en ConfirmBox, sparar sedan objekt till xml-fil innan programmet avslutas.
			});
			
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
					alertBox.alertBox("Inloggnings misslyckades", "");
				}
			});
			
			Button newSellerButton = new Button("Nytt säljarkonto");
			newSellerButton.setOnAction(e -> {
				createSellerWindow();
			});
			
			Button closeProgramButton = new Button("Avsluta");
			closeProgramButton.setOnAction( e -> closeProgram());
			
			VBox vBox = new VBox();
			vBox.setAlignment(Pos.CENTER);
			vBox.getChildren().addAll(infoLabel, idField, passWordField, logInButton, newSellerButton, closeProgramButton);
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
		stage.initModality(Modality.APPLICATION_MODAL);
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
        			alertBox.alertBox("Konto skapat", "Ditt ID: "+id+"\n\n(Kom ihåg ditt ID och lösenord)");
        			stage.close();
        		}
        	}
        	else {
        		alertBox.alertBox("Gick ej att skapa konto", "Kontrollera att alla fält är korrekt ifyllda.");
        	}
        });
        
        Button exitButton = new Button("Avbryt");
        exitButton.setOnAction(e -> stage.close());
		
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(nameField, adressField, passWordField, passWordFieldTwo, createButton, exitButton);
        
        Scene scene = new Scene(vBox, 400, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
	}
	
	public void createCustomerWindow() {
		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Skapa nytt konto");
		
		Label label = new Label();
		label.setText("Fyll i kundens namn och adress nedan.");
		
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
        			alertBox.alertBox("Kund tillagd", "Kundens ID: "+id);
        			stage.close();
        		}
        	}
        	else {
        		alertBox.alertBox("Gick ej att skapa kund", "Kontrollera att alla fält är korrekt ifyllda.");
        	}
        });
        
        Button exitButton = new Button("Avbryt");
        exitButton.setOnAction(e -> stage.close());
		
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.getChildren().addAll(label, nameField, adressField, createButton, exitButton);
        
        Scene scene = new Scene(vBox, 400, 400);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
	}
	
	public void closeProgram() {
		Boolean answer = alertBox.confirmBox("Bekräftelse", "Avsluta Program?");
		if (answer == true)
		{
			BusinessObjectManager.getInstance().serializeAll(); // Sparar bokningar och rum till XML innan avslut.
			logInStage.close();
			System.out.println("Sparat till xml.\nTack å hej, leverpastej..");
		}
	}
}
