

import java.sql.Date;

import controller.LoginController;
import controller.TransactionHandler;

public class Main{
	private LoginController loginController;

	public Main() {
		loginController = LoginController.getInstance();
		loginController.showForm();
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
