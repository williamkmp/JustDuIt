package view.employeeManagement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.EmployeeHandler;
import controller.HomeController;
import controller.RoleHandler;
import model.Employee;
import model.Product;
import model.Role;

public class EmployeeManagementFrame extends JFrame implements ActionListener, MouseListener{
	private static EmployeeManagementFrame instance;
	private static final Dimension SIZE = new Dimension(1600, 900);

	private EmployeeHandler controller;
	private RoleHandler roleController;
	
	private JLabel title;
	private JPanel northPanel, centerPanel, southPanel, eastPanel, westPanel, footerButtonsPanel, footerFormPanels;
	private JTable employeeTable;
	private DefaultTableModel employeeTableModel;
	private JScrollPane employeePane;
	private JButton backButton, insertButton, updateButton, fireButton;
	private JTextField idField, nameField, userNameField, salaryField, statusField, passwordField;
	private JComboBox<Role> roleField; 
	
	private EmployeeManagementFrame() {
		this.controller = EmployeeHandler.getInstance();
		this.roleController = RoleHandler.getInstance();
		
		this.setTitle("Just Du It!");
		this.setSize(SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout(20, 20));
		
		this.initComponents();
		this.mountComponents();
		this.mountListeners();
		
		this.setVisible(false);
	}
	
	public static EmployeeManagementFrame getPage() {
		if(instance == null) {
			instance = new EmployeeManagementFrame();
		}
		return instance;
	}
	
	public void showError() {
		JOptionPane.showMessageDialog(this, "ERROR", "FAILED", JOptionPane.ERROR_MESSAGE);
	}
	
	private void initComponents() {
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		employeeTable = new JTable();
		employeeTableModel = new DefaultTableModel();
		employeePane = new JScrollPane(employeeTable);
		footerButtonsPanel = new JPanel();
		footerFormPanels = new JPanel();
		insertButton = new JButton("Insert");
		updateButton = new JButton("Update");
		fireButton = new JButton("Fire");
		title = new JLabel("Employee Management");
		backButton = new JButton("<=");
		idField = new JTextField(4);
		nameField = new JTextField(15);
		salaryField = new JTextField(10);
		statusField = new JTextField(10);
		passwordField = new JTextField(10);
		userNameField = new JTextField(10);
		roleField = new JComboBox<Role>(roleController.getAllRole());
	}
	
	private void mountComponents() {
		northPanel.add(backButton);
		northPanel.add(title);
		
		employeePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		employeePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(employeePane, BorderLayout.CENTER);
		this.initTable();
		
		footerButtonsPanel.add(insertButton);
		footerButtonsPanel.add(updateButton);
		footerButtonsPanel.add(fireButton);
		
		footerFormPanels.add(idField);
		footerFormPanels.add(nameField);
		footerFormPanels.add(roleField);
		footerFormPanels.add(userNameField);
		footerFormPanels.add(passwordField);
		footerFormPanels.add(salaryField);
		footerFormPanels.add(statusField);
		
		southPanel.setPreferredSize(new Dimension(0, 150));
		southPanel.setLayout(new GridLayout(2,1));
		southPanel.add(footerFormPanels);
		southPanel.add(footerButtonsPanel);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
	}
	
	private void initTable() {
		employeeTableModel.addColumn("ID");
		employeeTableModel.addColumn("Name");
		employeeTableModel.addColumn("Role");
		employeeTableModel.addColumn("Username");
		employeeTableModel.addColumn("Password");
		employeeTableModel.addColumn("Salary");
		employeeTableModel.addColumn("Status");
		
		Vector<Employee> employeeList = controller.getAllEmployee();
		for(Employee employee : employeeList) {
			Object[] data = {
					employee.getID(),
					employee.getName().toString(),
					roleController.getRole(employee.getRoleID()).getRole(),
					employee.getUsername(),
					employee.getPassword(),
					employee.getSalary().toString(),
					employee.getStatus()
			};
			employeeTableModel.addRow(data);
		}
		employeeTable.setModel(employeeTableModel);
		employeeTableModel.fireTableDataChanged();
	}
	
	private void refreshTable() {
		for (int i = employeeTableModel.getRowCount() - 1; i >= 0; i--) {
			employeeTableModel.removeRow(i);
		}
		
		Vector<Employee> employeeList = controller.getAllEmployee();
		for(Employee employee : employeeList) {
			Object[] data = {
					employee.getID(),
					employee.getName().toString(),
					roleController.getRole(employee.getRoleID()).getRole(),
					employee.getUsername(),
					employee.getPassword(),
					employee.getSalary().toString(),
					employee.getStatus()
			};
			employeeTableModel.addRow(data);
		}
		employeeTable.setModel(employeeTableModel);
		employeeTableModel.fireTableDataChanged();
	}

	private void mountListeners() {
		backButton.addActionListener(this);
		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		fireButton.addActionListener(this);
		employeeTable.addMouseListener(this);
	}

	private void productClicked(Integer id){
		Employee e;
		try {
			e = Employee.getEmployee(id);
			idField.setText(e.getID().toString());
			nameField.setText(e.getName());
			salaryField.setText(e.getSalary().toString());
			userNameField.setText(e.getUsername());
			passwordField.setText(e.getPassword());
			statusField.setText(e.getStatus());
			roleField.setSelectedIndex(e.getRoleID()-1);
		} catch (SQLException e1) {
			this.showError();
		}
	}
	
	//insert
	private void insertButtonClicked() {
		String status = null, name = null, userName = null, password = null; 
		Integer salary = null, roleId = null;
		boolean isValid = true;
		
		try {
			name = nameField.getText().trim();
			userName = userNameField.getText().trim();
			password = passwordField.getText().trim();
			status = statusField.getText().trim();
			salary = Integer.parseInt(salaryField.getText());
			Role r = (Role) roleField.getSelectedItem();
			roleId = r.getID();
			isValid = controller.validateInsert(name, roleId, userName, password, salary);
		} catch (Exception e) {
			isValid = false;
		}
		
		if(isValid) {
			controller.addEmployee(name, roleId, userName, password, status, salary);
		}else {
			this.showError();
		}
		
		this.refreshTable();
	}

	//update
	private void updateButtonClicked() {
		String status = null, name = null, userName = null, password = null; 
		Integer ID = null, salary = null, roleId = null;
		boolean isValid = true;
		
		try {
			name = nameField.getText().trim();
			userName = userNameField.getText().trim();
			password = passwordField.getText().trim();
			status = statusField.getText().trim();
			ID = Integer.parseInt(idField.getText());
			salary = Integer.parseInt(salaryField.getText());
			Role r = (Role) roleField.getSelectedItem();
			roleId = r.getID();
			isValid = controller.validateUpdate(ID, name, roleId, userName, password, salary, status);
		} catch (Exception e) {
			isValid = false;
		}
		
		if(isValid) {
			controller.updateEmployee(ID, name, roleId, userName, password, status, salary);
		}else {
			this.showError();
		}
		
		this.refreshTable();
	}
	
	//delete
	private void fireButtonClicked() {
		Integer ID = null;
		try {
			ID = Integer.parseInt(idField.getText());
			controller.fireEmployee(ID);
		} catch (Exception e) {
			this.showError();
		}
		
		this.refreshTable();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			controller.backButtonHandler();
		}else
		if (e.getSource() == insertButton) {
			this.insertButtonClicked();
		}else
		if (e.getSource() == updateButton) {
			this.updateButtonClicked();
		}else
		if (e.getSource() == fireButton) {
			this.fireButtonClicked();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int row = employeeTable.rowAtPoint(e.getPoint());
		int id = (int) employeeTable.getValueAt(row,0);
		this.productClicked(id);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
