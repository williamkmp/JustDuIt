package view.home;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.EmployeeHandler;
import controller.HomeController;
import model.Employee;

public class HomeFrame extends JFrame implements ActionListener{
	private static HomeFrame instance = null;
	private HomeController controller = null;
	private EmployeeHandler employeeController;
	private static final Dimension SIZE = new Dimension(1600, 900);
	
	private JLabel title;
	private JPanel northPanel, southPanel, centerPanel, eastPanel, westPanel;
	private JButton transactionManagementButton, productManagementButton, employeeManagementButton, managerButton;
	
	private HomeFrame(Integer empId) {
		this.controller = HomeController.getInstance();
		this.employeeController = EmployeeHandler.getInstance();
		
		this.setTitle("Jus Du It");
		this.setSize(SIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout(200, 60));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.initComponents();
		this.mountComponents();
		this.mountListeners();
		
		this.setPrivilege(empId);
		this.setVisible(false);
	}
	
	public static synchronized HomeFrame getPage(Integer empId) {
		if(instance == null) {
			instance = new HomeFrame(empId);
		}
		return instance;
	}
	
	private void setPrivilege(Integer empId) {
		Employee e = employeeController.getEmployee(empId);
		Integer role = e.getRoleID();
		if(role == 1) {
			//HRD
			transactionManagementButton.setEnabled(false);
			productManagementButton.setEnabled(false);
			employeeManagementButton.setEnabled(true);
			managerButton.setEnabled(false);
		}else if(role == 2) {
			//PM
			transactionManagementButton.setEnabled(false);
			productManagementButton.setEnabled(true);
			employeeManagementButton.setEnabled(false);
			managerButton.setEnabled(false);
		}else if(role == 3) {
			//MNG
			transactionManagementButton.setEnabled(false);
			productManagementButton.setEnabled(false);
			employeeManagementButton.setEnabled(true);
			managerButton.setEnabled(true);
		}else if(role == 4) {
			//CH
			transactionManagementButton.setEnabled(true);
			productManagementButton.setEnabled(false);
			employeeManagementButton.setEnabled(false);
			managerButton.setEnabled(false);
		}
	}
	
	private void initComponents() {
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		
		transactionManagementButton = new JButton("Manage Transaction");
		productManagementButton = new JButton("Manage Products");
		employeeManagementButton= new JButton("Manage Employee");
		managerButton = new JButton("Reports");
		
		title = new JLabel("Menu");
	}
	
	private void mountComponents() {
		northPanel.add(title);
		
		centerPanel.setLayout(new GridLayout(4, 1, 60, 60));
		centerPanel.add(transactionManagementButton);
		centerPanel.add(productManagementButton);
		centerPanel.add(employeeManagementButton);
		centerPanel.add(managerButton);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
	}
	
	private void mountListeners() {
		transactionManagementButton.addActionListener(this);
		productManagementButton.addActionListener(this);
		employeeManagementButton.addActionListener(this);
		managerButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == transactionManagementButton) {;
			controller.showTransactionManagementPage();
		}else 
		if(e.getSource() == productManagementButton) {
			controller.showProductManagementPage();
		}else 
		if(e.getSource() == employeeManagementButton) {
			controller.showEmployeeManagementPage();
		}else
		if(e.getSource() == managerButton) {
			controller.showReportPage();
		}
		
	}

}
