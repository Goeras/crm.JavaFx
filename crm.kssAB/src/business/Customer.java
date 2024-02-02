package business;

import java.util.ArrayList;
import java.util.List;

public class Customer { // lägga en lyssnare här och meddela alla säljare?

	private int id; // 1000 - 9999
	private String name;
	private String adress;
	private List<Sale> purchases = new ArrayList<>();
	
	public Customer() {}
	
	public Customer(int id, String name, String adress) {
		this.id = id;
		this.name = name;
		this.adress = adress;
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
	public List<Sale> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<Sale> pruchases) {
		this.purchases = pruchases;
	}
	public void addPurchase(Sale purschase) {
		
	}

	@Override
	public String toString() {
		return name+", ID: "+id;
	}
	
	
	
}
