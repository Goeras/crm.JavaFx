package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer implements Serializable{ /**
 * 
 */
	private static final long serialVersionUID = -3116262366970447301L;

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
		this.purchases.add(purschase);
	}

	@Override
	public String toString() {
		return name+", ID: "+id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Customer otherCustomer = (Customer) obj;
		return id == otherCustomer.id; // Jämför baserat på kund-ID
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
