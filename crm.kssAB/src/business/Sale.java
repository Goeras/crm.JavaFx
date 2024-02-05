package business;

import java.io.Serializable;
import java.time.LocalDate;

public class Sale implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6854202571904838970L;
	private String productName;
	private int saleId; // 10000 - 99999
	private double price;
	private int numberOfItems;
	private LocalDate date;
	private Customer customer;
	private Seller seller;
	
	public Sale() {}
	
	public Sale(String productName, int saleId, double price, int numberOfItems, LocalDate date, Customer customer, Seller seller) {
		this.productName = productName;
		this.saleId = saleId;
		this.price = price;
		this.numberOfItems = numberOfItems;
		this.date = date;
		this.customer = customer;
		this.seller = seller;
	}
	
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int sellId) {
		this.saleId = sellId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	
}
