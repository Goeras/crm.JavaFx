package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Seller implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3546819275557444490L;
	private int id; // 100-999
	private String password;
	private String name;
	private String adress;
	private HashSet<Customer> customers = new HashSet<>(); // Hashset då vi bara behöver lägga till säljarens kunder en gång.
	private List<Sale> sales = new ArrayList<>(); // alla säljarens försäljningar.
	private Queue<String> messages = new LinkedList<>(); // Meddelanden då annan säljare gör försäljning till samma kund.
	
	public Seller() {}
	
	public Seller(int id, String password, String name, String adress) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.adress = adress;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public HashSet<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(HashSet<Customer> customers) {
		this.customers = customers;
	}
	public void addCustomer(Customer customer) {
		this.customers.add(customer);
	}
	public void removeCustomer(Customer customer) {
		this.customers.remove(customer);
	}
	public List<Sale> getSales() {
		return sales;
	}
	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}
	public void addSale(Sale sale) {
		this.sales.add(sale);
	}
	public Queue<String> getMessages() {
		return messages;
	}
	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}
	public void addMessage(String message) {
		this.messages.add(message);
	}
	public List<String> pollMessagesToList() { // Pollar varje nytt meddelande ur Queue och returnerar som en List.
		List<String> newMessages = new ArrayList<>();
		while(!messages.isEmpty()) {
			String message = messages.poll();
			newMessages.add(message);
		}
		return newMessages;
	}

	
}
