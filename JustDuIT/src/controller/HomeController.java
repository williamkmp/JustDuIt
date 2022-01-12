package controller;

import java.sql.SQLException;

import model.Employee;
import view.home.HomeFrame;

public class HomeController {
	private static HomeController instance = null;
	private HomeFrame frame;
	private EmployeeHandler employeeController;
	private TransactionHandler transactionController;
	private ProductHandler productController;
	private ReportHandler reportController;
	private Employee employee;
	
	private HomeController() {
		
	}
	
	public static synchronized HomeController getInstance() {
		if(instance == null) {
			instance = new HomeController();
		}
		return instance;
	}
	
	public void showPage(Integer employeeId) {
		if (this.frame == null) this.frame = HomeFrame.getPage(employeeId);
		try {
			this.employee = Employee.getEmployee(employeeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.frame.setVisible(true);
	}
	
	public void hidePage() {
		this.frame.setVisible(false);
	}
	
	public void showProductManagementPage() {
		this.frame.dispose();
		this.productController = ProductHandler.getInstance();
		this.productController.showPage();
	}
	
	public void showEmployeeManagementPage() {
		this.frame.dispose();
		this.employeeController = EmployeeHandler.getInstance();
		this.employeeController.showPage();
	}
	
	public void showTransactionManagementPage() {
		this.frame.dispose();
		this.transactionController = TransactionHandler.getInstance();
		this.transactionController.showPage();
	}
	
	public void showReportPage() {
		this.frame.dispose();
		this.reportController = ReportHandler.getInstance();
		this.reportController.showPage();
	}
	
	public Employee getLoggedEmployee() {
		return this.employee;
	}
}
