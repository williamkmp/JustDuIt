package view.report;

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
import controller.ProductHandler;
import controller.ReportHandler;
import controller.RoleHandler;
import controller.TransactionHandler;
import model.Transaction;
import view.productManagement.ProductManagementFrame;

public class ReportManagementPage extends JFrame implements ActionListener, MouseListener{
	private static ReportManagementPage instance;
	private ReportHandler controller;
	private TransactionHandler transactionController;
	private EmployeeHandler employeeController;
	private RoleHandler roleController;
	private static final Dimension SIZE = new Dimension(1600, 900);
	
	private JLabel title, monthLable, yearLable;
	private JPanel northPanel, centerPanel, southPanel, eastPanel, westPanel, footerButtonsPanel, footerFormPanels;
	private JTable headerTable;
	private DefaultTableModel headerTableModel;
	private JScrollPane headertPane;
	private JButton backButton, enterButton, viewAllButton;
	private JTextField monthField, yearField;
	
	public ReportManagementPage() {
		this.controller = ReportHandler.getInstance();
		this.transactionController = TransactionHandler.getInstance();
		this.employeeController = EmployeeHandler.getInstance();
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
	
	public void showError() {
		JOptionPane.showMessageDialog(this, "ERROR", "FAILED", JOptionPane.ERROR_MESSAGE);
	}

	private void initComponents() {
		title = new JLabel("Report Management");
		backButton = new JButton("<=");
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		
		headerTable = new JTable();
		headerTableModel = new DefaultTableModel();
		headertPane = new JScrollPane(headerTable);
		
		monthLable = new JLabel("Month");
		yearLable = new JLabel("Year");
		monthField = new JTextField(3);
		yearField = new JTextField(6);
		
		enterButton = new JButton("Enter");
		viewAllButton = new JButton("View All");
		
		footerButtonsPanel = new JPanel();
		footerFormPanels = new JPanel();
	}

	private void mountComponents() {
		northPanel.add(backButton);
		northPanel.add(title);
		
		headertPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		headertPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(headertPane, BorderLayout.CENTER);
		this.initTable();
		
		footerFormPanels.add(monthLable);
		footerFormPanels.add(monthField);
		footerFormPanels.add(yearLable);
		footerFormPanels.add(yearField);
		
		footerButtonsPanel.add(enterButton);
		footerButtonsPanel.add(viewAllButton);
		
		southPanel.setLayout(new GridLayout(2, 1));
		southPanel.setPreferredSize(new Dimension(0, 100));
		southPanel.add(footerFormPanels);
		southPanel.add(footerButtonsPanel);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
	}
	
	private void initTable() {
		headerTableModel.addColumn("ID");
		headerTableModel.addColumn("Employee");
		headerTableModel.addColumn("Date");
		headerTableModel.addColumn("Type");
		headerTableModel.addColumn("Total");
		this.viewAll();;
	}
	
	private void viewAll() {
		for (int i = headerTableModel.getRowCount() - 1; i >= 0; i--) {
			headerTableModel.removeRow(i);
		}
		
		Vector<Transaction> headers = null;
		try {
			headers = transactionController.getAllTransaction();
			for (Transaction t : headers) {
				Object[] data = { t.getID(), employeeController.getEmployee(t.getEmployeeId()), t.getDate(),
						(t.getPaymentType() == 1) ? "Cash" : "Credit", t.getTotal() };
				headerTableModel.addRow(data);
			}
		} catch (SQLException e) {
			this.showError();
		}
		headerTable.setModel(headerTableModel);
		headerTableModel.fireTableDataChanged();
	}

	private void viewWithTime(Integer month, Integer year) {
		for (int i = headerTableModel.getRowCount() - 1; i >= 0; i--) {
			headerTableModel.removeRow(i);
		}
		
		Vector<Transaction> headers = null;
		try {
			headers = transactionController.getAllTransactions(month, year);
			for (Transaction t : headers) {
				Object[] data = { t.getID(), employeeController.getEmployee(t.getEmployeeId()), t.getDate(),
						(t.getPaymentType() == 1) ? "Cash" : "Credit", t.getTotal() };
				headerTableModel.addRow(data);
			}
		} catch (SQLException e) {
			this.showError();
		}
		headerTable.setModel(headerTableModel);
		headerTableModel.fireTableDataChanged();
	}
	
	private void mountListeners() {
		backButton.addActionListener(this);
		headerTable.addMouseListener(this);
		enterButton.addActionListener(this);
		viewAllButton.addActionListener(this);
	}
	
	private void clickedTransaction(Integer ID) {
		new ReportDetailFrame(ID);
	}
	
	private boolean validateTime(Integer month, Integer year) {
		boolean isValid = true;
		if(month == null || year == null) {
			isValid = false;
		}
		if(month < 1 || month > 12) {
			isValid = false;
		}
		if(year <= 0) {
			isValid = false;
		}
		return isValid;
	}
	
	private void viewTime() {
		Integer year = null, month = null;
		boolean isValid = true;
		try {
			year = Integer.parseInt(yearField.getText().trim());
			month = Integer.parseInt(monthField.getText().trim());
			isValid = validateTime(month, year);
		} catch (Exception e) {
			isValid = false;
		}
		
		if(isValid) {
			this.viewWithTime(month, year);
		}else {
			this.showError();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			this.controller.backButtonHandler();
		}else
		if (e.getSource() == viewAllButton) {
			this.viewAll();
		}else
		if(e.getSource() == enterButton) {
			this.viewTime();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		int row = headerTable.rowAtPoint(e.getPoint());
		int ID = (int) headerTable.getValueAt(row, 0);
		this.clickedTransaction(ID);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public static ReportManagementPage getPage() {
		if(instance == null) {
			instance = new ReportManagementPage();
		}
		return instance;
	}
}
