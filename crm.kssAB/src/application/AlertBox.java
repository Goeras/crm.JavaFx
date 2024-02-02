package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertBox {

	public void confirmBox(String title, String message) // En AlertBox som tar strängar för titel och meddelande för att kunna använda metoden i olika sammanhang.
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ButtonType buttonTypeOK = new ButtonType("Ok");
        
        alert.getButtonTypes().setAll(buttonTypeOK);
        alert.showAndWait();
	}
	
}
