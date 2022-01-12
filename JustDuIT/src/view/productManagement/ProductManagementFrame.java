package view.productManagement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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

import controller.HomeController;
import controller.ProductHandler;
import model.Product;

public class ProductManagementFrame extends JFrame implements ActionListener, MouseListener{
	private static ProductManagementFrame instance;
	private static final Dimension SIZE = new Dimension(1600, 900);

	private ProductHandler controller;
	
	private JLabel title;
	private JPanel northPanel, centerPanel, southPanel, eastPanel, westPanel, footerButtonsPanel, footerFormPanels;
	private JTable productTable;
	private DefaultTableModel productTableModel;
	private JScrollPane productPane;
	private JButton backButton, insertButton, updateButton, deleteButton;
	private JTextField idField, nameField, priceField, stockField;
	private JTextArea descriptionField;
	
	private ProductManagementFrame() {
		this.controller = ProductHandler.getInstance();
		
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
	
	public static ProductManagementFrame getPage() {
		if(instance == null) {
			instance = new ProductManagementFrame();
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
		productTable = new JTable();
		productTableModel = new DefaultTableModel();
		productPane = new JScrollPane(productTable);
		footerButtonsPanel = new JPanel();
		footerFormPanels = new JPanel();
		insertButton = new JButton("Insert");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		title = new JLabel("Product Management");
		backButton = new JButton("<=");
		idField = new JTextField(5);
		nameField = new JTextField(15);
		descriptionField = new JTextArea(3, 30) ;
		priceField = new JTextField(10);
		stockField = new JTextField(10);
	}
	
	private void mountComponents() {
		northPanel.add(backButton);
		northPanel.add(title);
		
		productPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		productPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(productPane, BorderLayout.CENTER);
		this.initTable();
		
		footerButtonsPanel.add(insertButton);
		footerButtonsPanel.add(updateButton);
		footerButtonsPanel.add(deleteButton);
		
		footerFormPanels.add(idField);
		footerFormPanels.add(nameField);
		footerFormPanels.add(descriptionField);
		footerFormPanels.add(priceField);
		footerFormPanels.add(stockField);
		
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
		productTableModel.addColumn("ID");
		productTableModel.addColumn("Name");
		productTableModel.addColumn("Description");
		productTableModel.addColumn("Price");
		productTableModel.addColumn("Stock");
		
		Vector<Product> productList = controller.getAllProduct();
		for(Product product : productList) {
			Object[] data = {
					product.getID(),
					product.getName().toString(),
					product.getDescription().toString(),
					product.getPrice(),
					product.getStock()
			};
			productTableModel.addRow(data);
		}
		productTable.setModel(productTableModel);
		productTableModel.fireTableDataChanged();
	}
	
	private void refreshTable() {
		for (int i = productTableModel.getRowCount() - 1; i >= 0; i--) {
			productTableModel.removeRow(i);
		}
		
		Vector<Product> productList = controller.getAllProduct();
		for(Product product : productList) {
			Object[] data = {
					product.getID(),
					product.getName().toString(),
					product.getDescription().toString(),
					product.getPrice(),
					product.getStock()
			};
			productTableModel.addRow(data);
		}
		productTable.setModel(productTableModel);
		productTableModel.fireTableDataChanged();
	}

	private void mountListeners() {
		backButton.addActionListener(this);
		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		deleteButton.addActionListener(this);
		productTable.addMouseListener(this);
	}
	
	private void productClicked(Integer id){
		Product p;
		try {
			p = Product.getProduct(id);
			idField.setText(p.getID().toString());
			nameField.setText(p.getName());
			descriptionField.setText(p.getDescription());
			priceField.setText(p.getPrice().toString());
			stockField.setText(p.getStock().toString());
		} catch (SQLException e) {
			this.showError();
		}
	}
	
	//insert
	private void insertButtonClicked() {
		String name = null, description = null;
		Integer price = null, stock = null;
		boolean isValid = true;
		try {
			name = nameField.getText().trim();
			description = descriptionField.getText().trim();
			price = Integer.parseInt(priceField.getText().trim());
			stock = Integer.parseInt(stockField.getText().trim());
			isValid = controller.validateProduct(name, description, price, stock);
		} catch (Exception e) {
			isValid = false;
		}
		if(isValid) {
			controller.addProduct(name, description, price, stock);
		}else {
			this.showError();
		}
		this.refreshTable();
	}

	//update
	private void updateButtonClicked() {
		String name = null, description = null;
		Integer price = null, stock = null, ID = null;
		boolean isValid = true;
		try {
			ID = Integer.parseInt(idField.getText().trim());
			name = nameField.getText().trim();
			description = descriptionField.getText().trim();
			price = Integer.parseInt(priceField.getText().trim());
			stock = Integer.parseInt(stockField.getText().trim());
			isValid = controller.validateProduct(name, description, price, stock);
		} catch (Exception e) {
			isValid = false;
		}
		
		if(isValid) {
			controller.updateProduct(ID, name, description, price, stock);
		}else {
			this.showError();
		}
		
		this.refreshTable();
	}
	
	//delete
	private void deleteButtonClicked() {
		Integer ID = null;
		try {
			ID = Integer.parseInt(idField.getText().trim());
			controller.deleteProduct(ID);
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
		if (e.getSource() == deleteButton) {
			this.deleteButtonClicked();
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
		int row = productTable.rowAtPoint(e.getPoint());
		int id = (int) productTable.getValueAt(row,0);
		this.productClicked(id);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
