package view.transactionManagement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.print.attribute.standard.JobMediaSheetsCompleted;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.CartItemHandler;
import controller.EmployeeHandler;
import controller.HomeController;
import controller.ProductHandler;
import controller.TransactionHandler;
import model.CartItem;
import model.Product;
import model.Transaction;

public class TransactionManagementFrame extends JFrame implements ActionListener{
	private static TransactionManagementFrame instance;
	private static final Dimension SIZE = new Dimension(1600, 900);

	private TransactionHandler controller;
	private CartItemHandler cart;
	
	private JLabel title;
	private JPanel northPanel, centerPanel, southPanel, eastPanel, westPanel;
	private JButton backButton, checkOutButton, resetButton;
	private JScrollPane productPane, cartPane;
	private DefaultTableModel productTableModle, cartTableModel;
	private JTable productTable, cartTable;
	
	private TransactionManagementFrame() {
		this.controller = TransactionHandler.getInstance();
		this.cart = CartItemHandler.getInstnace();
		
		this.setTitle("Just Du It!");
		this.setSize(SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout(20, 20));
		
		this.initComponents();
		this.mountComponents();
		this.mountFrameListeners();
		
		this.setVisible(false);
	}
	
	public static TransactionManagementFrame getPage() {
		if(instance == null) {
			instance = new TransactionManagementFrame();
		}
		return instance;
	}
	
	private void initComponents() {
		title = new JLabel("Transaction Management");
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		productTable = new JTable();
		cartTable = new JTable();
		productTableModle = new DefaultTableModel();
		cartTableModel = new DefaultTableModel();
		productTable = new JTable(productTableModle);
		cartTable = new JTable(cartTableModel);
		cartPane = new JScrollPane(this.cartTable);
		productPane = new JScrollPane(this.productTable);
		
		
		backButton = new JButton("<=");
		checkOutButton = new JButton("Check Out");
		resetButton = new JButton("Cancel");
		this.initTable();

	}
	
	private void mountComponents() {
		northPanel.add(backButton);
		northPanel.add(title);
		
		centerPanel.setLayout(new GridLayout(1, 2));
		
		productPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		productPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		cartPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		cartPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		centerPanel.add(productPane);
		centerPanel.add(cartPane);
		
		southPanel.add(checkOutButton);
		southPanel.add(resetButton);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
	}

	private void mountFrameListeners() {
		backButton.addActionListener(this);
		checkOutButton.addActionListener(this);
		resetButton.addActionListener(this);
		productTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int row = productTable.rowAtPoint(e.getPoint());
				int id = (int) productTable.getValueAt(row,0);
				String name = (String) productTable.getValueAt(row,1);
				addProductToCart(id,name);
			}
		});
	}
	
	public void showError() {
		JOptionPane.showMessageDialog(this, "ERROR", "FAILED", JOptionPane.ERROR_MESSAGE);
	}
	
	public void initTable() {
		productTableModle.addColumn("Id");
		productTableModle.addColumn("Name");
		productTableModle.addColumn("Price");
		productTableModle.addColumn("Stock");
		
		cartTableModel.addColumn("Id");
		cartTableModel.addColumn("Name");
		cartTableModel.addColumn("Price");
		cartTableModel.addColumn("Quantity");
		try {
			
			Vector<Product> products = Product.getAllProduct();
			for(Product p : products) {
				Object[] data = {p.getID(),p.getName().toString(), p.getPrice().toString(), p.getStock().toString()};
				this.productTableModle.addRow(data);
			}
			
			Vector<CartItem> cartItemList = cart.getListCartItem();
			for(CartItem c : cartItemList) {
				Product p = Product.getProduct(c.getProductId());
				Object[] data = {
						p.getID(),
						p.getName(),
						p.getPrice(),
						c.getQuantity()
				};
				this.cartTableModel.addRow(data);
			}
			
		} catch (SQLException e) {
			this.showError();
			e.printStackTrace();
		}
		this.productTable.setModel(this.productTableModle);
		this.cartTable.setModel(this.cartTableModel);
		
		this.productTableModle.fireTableDataChanged();
		this.cartTableModel.fireTableDataChanged();
		
	}
	
	private void refreshTable() {
		try {
			for (int i = productTableModle.getRowCount() - 1; i >= 0; i--) {
				productTableModle.removeRow(i);
			}
			Vector<Product> products = Product.getAllProduct();
			for(Product p : products) {
				Object[] data = {p.getID(),p.getName().toString(), p.getPrice().toString(), p.getStock().toString()};
				this.productTableModle.addRow(data);
			}
			
			for (int i = cartTableModel.getRowCount() - 1; i >= 0; i--) {
				cartTableModel.removeRow(i);
			}
			Vector<CartItem> cartItemList = cart.getListCartItem();
			for(CartItem c : cartItemList) {
				Product p = Product.getProduct(c.getProductId());
				Object[] data = {
						p.getID(),
						p.getName().toString(),
						p.getPrice().toString(),
						c.getQuantity().toString()
				};
				this.cartTableModel.addRow(data);
			}
			
		} catch (SQLException e) {
			this.showError();
			e.printStackTrace();
		}
		this.productTableModle.fireTableDataChanged();
		this.cartTableModel.fireTableDataChanged();
	}
	
	private void addProductToCart(int id, String name){
		int quantity = -1;
		Product p = null;
		try {			
			Integer ans = Integer.parseInt( JOptionPane.showInputDialog(this,"Quantity for " + name));
			p = Product.getProduct(id);
			quantity = ans.intValue();
		} catch (Exception e) {
			this.showError();
		}
		if(quantity != -1 && p != null) {
			if(quantity <= p.getStock() || quantity > 0) {				
				cart.addToCart(id, quantity);
			}else {
				this.showError();
			}
		}else {
			this.showError();
		}
		this.refreshTable();
	}
	
	private void checkoutButtonClick() {
		if(this.cart.getListCartItem().size() > 0) {			
			Integer paymentType = JOptionPane.showConfirmDialog(this, "payment is Cash(Yes) Credit(No)") + 1;
			if(paymentType == 1 || paymentType == 2) {
				Transaction header = controller.insertTarnsaction(paymentType);
				controller.checkOut(cart.getListCartItem(), header.getID());
				this.cart.clearCartItemList();
				this.refreshTable();
			}
		}
	}
	
	private void resetButton() {
		this.cart.clearCartItemList();
		this.refreshTable();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			controller.backButtonHandler();
		}else
		if(e.getSource() == checkOutButton) {
			this.checkoutButtonClick();
		}else
		if(e.getSource() == resetButton ) {
			this.resetButton();
		}
	}
}
