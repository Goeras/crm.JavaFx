package business;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BusinessObjectManager {

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

	public void addStartingObjects() {
		Seller seller1 = new Seller();
		seller1.setName("admin");
		seller1.setId(1);
		seller1.setPassword("admin");
		listOfAllSellers.add(seller1);
		
		Customer customer1 = new Customer();
		customer1.setName("Testkund 1");
		customer1.setId(1111);
		listOfAllCustomers.add(customer1);
		
		Customer customer2 = new Customer();
		customer2.setName("Testkund 2");
		customer2.setId(2222);
		listOfAllCustomers.add(customer2);
		
		Sale sale1 = new Sale();
		sale1.setCustomer(customer1);
		sale1.setSaleId(35000000);
		sale1.setDate(LocalDate.now());
		sale1.setNumberOfItems(1);
		sale1.setPrice(29.99);
		sale1.setProductName("Snus");
		listOfAllSales.add(sale1);
		
		seller1.addCustomer(customer1);
		seller1.addSale(sale1);
		
		Sale sale2 = new Sale();
		sale2.setCustomer(customer1);
		sale2.setSaleId(35000000);
		sale2.setDate(LocalDate.now());
		sale2.setNumberOfItems(1);
		sale2.setPrice(29.99);
		sale2.setProductName("whiskey");
		listOfAllSales.add(sale2);
		
		Sale sale3 = new Sale();
		sale3.setCustomer(customer2);
		sale3.setSaleId(35000000);
		sale3.setDate(LocalDate.now());
		sale3.setNumberOfItems(1);
		sale3.setPrice(29.99);
		sale3.setProductName("Tobak");
		listOfAllSales.add(sale3);
		
		seller1.addCustomer(customer2);
		seller1.addSale(sale2);
		seller1.addSale(sale3);
	}
}
