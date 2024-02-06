package business;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fileProcessing.BusinessObjectFileProcessing;


// Singleton för att säkerställa att det bara finns en instans av denna klass och listor.
public class BusinessObjectManager {
	
	BusinessObjectFileProcessing fileProcessing = new BusinessObjectFileProcessing();

	private List<Customer> listOfAllCustomers = new ArrayList<>();
	private List<Seller> listOfAllSellers = new ArrayList<>();
	private List<Sale> listOfAllSales = new ArrayList<>();
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private static BusinessObjectManager instance;
	
	private BusinessObjectManager() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public static BusinessObjectManager getInstance() {
		if(instance == null) {
			instance = new BusinessObjectManager();
		}
		return instance;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public Seller logInValidation(String strId, String password) {

		Seller foundSeller = null;

		try {
			int id = Integer.parseInt(strId);

			for(Seller seller : BusinessObjectManager.getInstance().getListOfAllSellers())
				if(seller.getId() == id) {
					if (seller.getPassword().equals(password)) {
						foundSeller = seller;
					}
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return foundSeller; // är det bättre att returnera seller/null här? återstår att se..
	}
	
	public int createUniqueSellerId() {
		Random random = new Random();
		int randomSellerId = random.nextInt(900)+100; // Skapar ett random bokningsnummer mellan 100 och 999.
		
		for(Seller seller : listOfAllSellers) // Kontrollerar om bokningsnumret är unikt eller redan finns i systemet.
		{
			if(seller.getId() == randomSellerId)
			{
				return createUniqueSellerId(); // om bokningsnumret redan finns så upprepas metoden tills dess att ett unikt nummer slumpats fram.
			}
		}
		return randomSellerId; // Returnerar ett unikt bokningsnummer.
	}
	
	public int createUniqueSaleId() {
		Random random = new Random();
		int randomSaleId = random.nextInt(90000)+10000; // Skapar ett random bokningsnummer mellan 10000 och 99999.
		
		for(Sale sale : listOfAllSales) // Kontrollerar om bokningsnumret är unikt eller redan finns i systemet.
		{
			if(sale.getSaleId() == randomSaleId)
			{
				return createUniqueSaleId(); // om bokningsnumret redan finns så upprepas metoden tills dess att ett unikt nummer slumpats fram.
			}
		}
		return randomSaleId; // Returnerar ett unikt bokningsnummer.
	}
	
	public int createUniqueCustomerId() {
		Random random = new Random();
		int randomCustomerId = random.nextInt(9000)+1000; // Skapar ett random bokningsnummer mellan 1000 och 9999.
		
		for(Customer customer : listOfAllCustomers) // Kontrollerar om bokningsnumret är unikt eller redan finns i systemet.
		{
			if(customer.getId() == randomCustomerId)
			{
				return createUniqueCustomerId(); // om bokningsnumret redan finns så upprepas metoden tills dess att ett unikt nummer slumpats fram.
			}
		}
		return randomCustomerId; // Returnerar ett unikt bokningsnummer.
	}
	
	
	public int createNewSeller(String name, String adress, String password) {
		
		int id = createUniqueSellerId();
		Seller seller = new Seller(id, password, name, adress);
		listOfAllSellers.add(seller);
		
		return id;
	}
	
	public int createNewCustomer(String name, String adress) {
		
		int id = createUniqueCustomerId();
		Customer customer = new Customer(id, name, adress);
		listOfAllCustomers.add(customer);
		
		return id;
	}
	
	public void createNewSale(Seller seller, Customer customer, String productName, String price, String numberOfItems) {
		
		int saleId = createUniqueSaleId();
		LocalDate date = LocalDate.now(); // datum = Idag.
		
		Sale sale = new Sale(productName, saleId, Double.parseDouble(price), Integer.parseInt(numberOfItems), date, customer, seller);
		
		listOfAllSales.add(sale); // lägger till i listan över alla sälj.
		seller.addSale(sale); // lägger till försäljningen till säljarens List.
		seller.addCustomer(customer); // lägger till kunden till säljarens HashSet
		customer.addPurchase(sale); // lägger till försäljningen till kundens List.
		
		List<Seller> sellersWithSameCustomer = sellersWithSameCustomer(customer);
		if (!sellersWithSameCustomer.isEmpty()) {
	        propertyChangeSupport.firePropertyChange("listOfAllSales", null, sale);
	    }
	}
	
	public List<Seller> sellersWithSameCustomer(Customer customer){
		
		List<Seller> sellersWithSameCustomer = new ArrayList<>();
		
		for(Seller seller : listOfAllSellers) {
			if(seller.getCustomers().contains(customer)) {
				sellersWithSameCustomer.add(seller);
			}
		}
		return sellersWithSameCustomer;
	}
	
	public boolean validateIfInputIsInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		}
		catch(NumberFormatException nfe){
			nfe.printStackTrace();
			return false;
		}
	}
	
	public boolean validateIfInputIsDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		}
		catch(NumberFormatException nfe){
			nfe.printStackTrace();
			return false;
		}
	}
	
	public Customer findCustomerById(int id) {
		Customer foundCustomer = null;
		for(Customer customer : listOfAllCustomers) {
			if(customer.getId() == id) {
				foundCustomer = customer;
			}
		}
		return foundCustomer;
	}
	
	public List<Sale> getListOfSalesByCustomer(Customer customer, Seller seller) { // behöver välja vilken kund först.
		List<Sale> SalesList = new ArrayList<>();
		if(customer == null) {
			SalesList.addAll(seller.getSales());
		}
		else {
			for(Sale sale : seller.getSales()) {
				if(sale.getCustomer() == customer) {
					SalesList.add(sale);
				}
			}
		}
		return SalesList;
	}
	

	public List<Customer> getListOfAllCustomers() {
		return listOfAllCustomers;
	}

	public List<Seller> getListOfAllSellers() {
		return listOfAllSellers;
	}
	
	public void addSeller(Seller seller) {
		listOfAllSellers.add(seller);
	}
	
	public void addCustomer(Customer customer) {
		listOfAllCustomers.add(customer);
	}
	
	public void printAllSellersAndCustomersToConsole() { // Metod enbart för att underlätta för utvecklaren..
		System.out.println("Säljarkonton:");
		for (Seller seller : listOfAllSellers){
			System.out.println("Name: "+seller.getName()+", ID: "+seller.getId()+", Password: "+seller.getPassword().toString());
			
		}
		System.out.println("\nKundkonton");
		for (Customer customer : listOfAllCustomers){
			System.out.println("Name: "+customer.getName()+", ID: "+customer.getId());
			
		}
	}
	
	public List<Sale> getSalesByProduct(String product){ // Returnerar alla sälj oavsett säljare..
		List<Sale> listOfMatchingProducts = new ArrayList<>();
		for(Sale sale : listOfAllSales) {
			if(sale.getProductName().equalsIgnoreCase(product)) {
				listOfMatchingProducts.add(sale);
			}
		}
		return listOfMatchingProducts;
	}
	
	public int getNumberOfItemsPerCustomer(Customer customer) {
		int numberOfPurchases = 0;
		for(Sale sale : customer.getPurchases()) {
			numberOfPurchases += sale.getNumberOfItems();
		}
		return numberOfPurchases;
	}
	
	public int getNumberOfPurchasesPerCustomer(Customer customer) {
		
		return customer.getPurchases().size();
	}
	
	public Set<String> getSoldUniqueProducts(){
		Set<String> uniqueProducts = new HashSet<>();
		for(Sale sale : listOfAllSales) {
			uniqueProducts.add(sale.getProductName());
		}
		return uniqueProducts;
	}
	
	public double getTotalSellAmountFromSeller(Seller seller) {
		double amount = 0;
		for (Sale sale : seller.getSales()) {
			amount += sale.getPrice();
		}
		return amount;
	}
	
	public double getTotalPurchaseAmountFromCustomer(Customer customer) {
		double totalAmount = 0;
		for(Sale sale : customer.getPurchases()) {
			totalAmount += sale.getPrice();
		}
		return totalAmount;
	}
	public int getTotalNumberOfSalesForProduct(String product) {
		System.out.println(listOfAllSales.size());
		int numberOfSales = 0;
		for(Sale sale : listOfAllSales) {
			if(sale.getProductName().equalsIgnoreCase(product)) {
				numberOfSales += sale.getNumberOfItems();
			}
		}
		return numberOfSales;
	}
	
	public double getTotalSaleAmountOfProduct(String product) {
		double totalAmount = 0;
		for(Sale sale : listOfAllSales) {
			if(sale.getProductName().equalsIgnoreCase(product)) {
				totalAmount += sale.getPrice();
			}
		}
		return totalAmount;
	}
	
	public void serializeAll() {
		
		fileProcessing.serializeSeller(listOfAllSellers);
		fileProcessing.serializeCustomer(listOfAllCustomers);
		fileProcessing.serializeSale(listOfAllSales);
	}
	
	public void deserializeAll() {
		
		this.listOfAllSellers = fileProcessing.deserializeSeller(listOfAllSellers);
		this.listOfAllCustomers = fileProcessing.deserializeCustomer(listOfAllCustomers);
		this.listOfAllSales = fileProcessing.deserializeSale(listOfAllSales);
		
	}

	public void addStartingObjects() { // Lägger till några startobjekt av Seller och Customer.
		if(listOfAllSellers.isEmpty()) {
			
			createNewSeller("Michael Scott", "The Office", "0000");
			createNewSeller("Mark Hanna", "Wall Street", "0000");
			createNewSeller("Jordan Belfort", "Wall Street", "0000");
		}
		if(listOfAllCustomers.isEmpty()) {
			
			createNewCustomer("Johnny Knoxville", "Tennessee");
			createNewCustomer("Jokkmokks-Jocke", "Jokkmokk");
			createNewCustomer("Bengt Magnusson", "Ystad");
			createNewCustomer("Stefan Ingves", "Åbo");
		}
		
	}
}
