package business;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SaleListener implements PropertyChangeListener{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if("listOfAllSales".equals(evt.getPropertyName())) { // Kollar om ändringen gäller "listOfAllSales"-listan
			if(evt.getNewValue() instanceof Sale) { // Kollar om värdet i ändringen är av typen Sale
				Sale newSale = (Sale) evt.getNewValue(); // iom att det handlar om Sale, så kan vi nu skapa upp och koppla ändringen till ett Sale objekt.
				Seller thisSeller = newSale.getSeller();
				Customer saleCustomer = newSale.getCustomer(); // Letar fram säljaren för dett sale.
				for (Seller seller : BusinessObjectManager.getInstance().getListOfAllSellers()) // letar igenom alla säljare i systemet.
					if(seller.getCustomers().contains(saleCustomer) && !(seller == thisSeller)) { // om även denna säljare har samma kund
						seller.addMessage(newSale.getDate()+ // Lägg till ett meddelande i dennes message-Queue.
								". Säljare: "+newSale.getSeller().getName()+
								", ID:"+newSale.getSeller().getId()+
								", har sålt "+newSale.getProductName()+
								" till kund: "+saleCustomer.getName()+
								", ID: "+saleCustomer.getId());
					}
			}
		}

	}

}
