package controller;

import java.sql.SQLException;
import java.util.Vector;

import model.Employee;
import view.login.LoginFrame;

public class LoginController {
	private static LoginController instance = null;
	private HomeController home = null;
	private Employee model;
	private LoginFrame frame;
	
	private LoginController() {
		home = HomeController.getInstance();
	}
	
	public static synchronized LoginController getInstance() {
		if(instance == null) {
			instance = new LoginController();
		}
		return instance;
	}
	
	public void showForm() {
		this.frame = LoginFrame.getPage();
		this.frame.setVisible(true);
	}
	
	public void login (String username, String password) {
		boolean isValid = false;
		Integer employeeId = -1;
		try {
			Vector<Employee> empList = Employee.getAllEmployee();
			for(Employee e : empList) {
				if(e.getUsername().equals(username)) {
					if(e.getPassword().equals(password)) {
						isValid = true;
						employeeId = e.getID();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(isValid) {
			this.frame.dispose();
			this.home.showPage(employeeId);
		}else {
			this.frame.showLoginFail();
		}
	}

}
