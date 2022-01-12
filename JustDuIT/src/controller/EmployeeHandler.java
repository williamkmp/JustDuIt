package controller;

import java.sql.SQLException;
import java.util.Vector;

import javax.net.ssl.SSLEngineResult.Status;

import model.Employee;
import model.Role;
import view.employeeManagement.EmployeeManagementFrame;

public class EmployeeHandler {
	private static EmployeeHandler instance;
	private EmployeeManagementFrame frame;
	private HomeController home;
	
	private EmployeeHandler() {
		this.home = HomeController.getInstance();
	}
	
	public static synchronized EmployeeHandler getInstance() {
		if(instance == null) {
			instance = new EmployeeHandler();
		}
		return instance;
	}
	
	public void showPage() {
		if(this.frame == null) this.frame = EmployeeManagementFrame.getPage();
		this.frame.setVisible(true);
	}
	
	public void hidePage() {
		if(this.frame == null) this.frame = EmployeeManagementFrame.getPage();
		this.frame.setVisible(false);
	}
	
	public void backButtonHandler() {
		frame.dispose();
		home.showPage(home.getLoggedEmployee().getID());
	}
	
	public Vector<Employee> getAllEmployee(){
		Vector<Employee> employeeList = new Vector<Employee>();
		try {
			employeeList = Employee.getAllEmployee();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employeeList;
	}
	
	public boolean validateInsert(String name, Integer roleId, String username, String password, Integer salary) {
		boolean isValid = true;
		if(		password == null || password.length() <= 0 ||
				username == null || username.length() <= 0 ||
				roleId == null || (roleId > 4 && roleId < 1) ||
				name == null || name.length() <= 0) {
			isValid = false;
		}
		
		Vector<Employee> list;
		try {
			list = Employee.getAllEmployee();
			for(Employee e : list) {
				if(e.getUsername().equals(username)) {
					isValid = false;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		
		return isValid;
	}

	public boolean validateUpdate(Integer ID, String name, Integer roleId, String username, String password, Integer salary, String status) {
		boolean isValid = true;
		Vector<Employee> list;
		try {
			boolean exist = false;
			list = Employee.getAllEmployee();
			for(Employee e : list) {
				if(e.getID() == ID) {
					exist = true;
					break;
				}
			}
			if(exist) {				
				for(Employee e : list) {
					if(e.getUsername().equals(username) && e.getID() != ID) {
						isValid = false;
					}
				}
			}else {
				return false;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		if(		password == null || password.length() <= 0 ||
				username == null || username.length() <= 0 ||
				roleId == null || (roleId > 4 && roleId < 1) ||
				!(status.equals("Active") || status.equals("Not Active")) ||
				name == null || name.length() <= 0) {
			isValid = false;
		}
		return isValid;
	}
	
	public void addEmployee(String name, Integer roleId, String username, String password, String status, Integer salary) {
		if (!(status.equals("Not Active")|| status.equals("Active"))) status = "Active";
		Employee e = new Employee();
		e.setName(name);
		e.setUsername(username);
		e.setPassword(password);
		e.setSalary(salary);
		e.setStatus(status);
		e.setRoleID(roleId);
	
		try {
			e.insertEmployee();
		} catch (SQLException e1) {
			frame.showError();
		}
	}
	
	public void updateEmployee(Integer ID, String name, Integer roleId, String username, String password, String status, Integer salary) {
		Employee e;
		try {
			e = Employee.getEmployee(ID);
			e.setName(name);
			e.setUsername(username);
			e.setPassword(password);
			e.setSalary(salary);
			e.setStatus(status);
			e.setRoleID(roleId);
			e.updateEmployee();
		} catch (SQLException e1) {
			frame.showError();
		}
	}
	
	public void fireEmployee(Integer ID) {
		Employee e;
		try {
			e = Employee.getEmployee(ID);
			e.setStatus("Not Active");
			e.updateEmployee();
		} catch (SQLException e1) {
			frame.showError();
		}
	}
	
	public Employee getEmployee(Integer ID) {
		Employee e = null;
		try {
			e = Employee.getEmployee(ID);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return e;
	}
}
