package fileProcessing;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import business.Customer;
import business.Sale;
import business.Seller;

public class BusinessObjectFileProcessing {
	
public void serializeSeller(List<Seller> sellerList) {
        
        try {
            String filepath = ".sellers.xml";
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            // Specialare för att XMLEncoder ska kunna hantera LocalDate
            xmlEncoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());
            xmlEncoder.writeObject(sellerList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	
	public List<Seller> deserializeGuest(List<Seller> sellerList) {
		try {
			String filepath = ".sellers.xml";
			File file = new File(filepath);

			if (file.exists()) // kollar om filen existerar.
			{
				if(file.length() > 0) 
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
					@SuppressWarnings("unchecked")
					List<Seller> deserializedList = (List<Seller>) xmlDecoder.readObject();
					xmlDecoder.close();
					fileInputStream.close();
					sellerList.clear();
					sellerList.addAll(deserializedList);
				}
				else {
					System.out.println("File is empty.");
				}
			} else {
				System.out.println("File does not exist: " + filepath);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("File is probably empty.");
		}

		return sellerList;
	}
	
public void serializeCustomer(List<Customer> customerList) {
        
        try {
            String filepath = ".customers.xml";
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            // Specialare för att XMLEncoder ska kunna hantera LocalDate
            xmlEncoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());
            xmlEncoder.writeObject(customerList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	
	public List<Customer> deserializeCustomer(List<Customer> customerList) {
		try {
			String filepath = ".customers.xml";
			File file = new File(filepath);

			if (file.exists()) // kollar om filen existerar.
			{
				if(file.length() > 0) 
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
					@SuppressWarnings("unchecked")
					List<Customer> deserializedList = (List<Customer>) xmlDecoder.readObject();
					xmlDecoder.close();
					fileInputStream.close();
					customerList.clear();
					customerList.addAll(deserializedList);
				}
				else {
					System.out.println("File is empty.");
				}
			} else {
				System.out.println("File does not exist: " + filepath);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("File is probably empty.");
		}

		return customerList;
	}
	
public void serializeSale(List<Sale> salesList) {
        
        try {
            String filepath = ".sales.xml";
            File file = new File(filepath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);

            // Specialare för att XMLEncoder ska kunna hantera LocalDate
            xmlEncoder.setPersistenceDelegate(LocalDate.class, new LocalDatePersistenceDelegate());
            xmlEncoder.writeObject(salesList);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	
	public List<Sale> deserializeSale(List<Sale> salesList) {
		try {
			String filepath = ".sales.xml";
			File file = new File(filepath);

			if (file.exists()) // kollar om filen existerar.
			{
				if(file.length() > 0) 
				{
					FileInputStream fileInputStream = new FileInputStream(file);
					XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
					@SuppressWarnings("unchecked")
					List<Sale> deserializedList = (List<Sale>) xmlDecoder.readObject();
					xmlDecoder.close();
					fileInputStream.close();
					salesList.clear();
					salesList.addAll(deserializedList);
				}
				else {
					System.out.println("File is empty.");
				}
			} else {
				System.out.println("File does not exist: " + filepath);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("File is probably empty.");
		}

		return salesList;
	}
	
}
