package controller;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.CartItem;
import model.Product;
import model.Transaction;
import model.TransactionItem;
import view.transactionManagement.TransactionManagementFrame;

public class TransactionHandler {
	private static TransactionHandler instance;
	private TransactionManagementFrame frame;
	private ProductHandler productController;
	private HomeController home;
	
	private TransactionHandler() {
		home = HomeController.getInstance();
		productController = ProductHandler.getInstance(); 
	}
	
	public static synchronized TransactionHandler getInstance() {
		if(instance == null) {
			instance = new TransactionHandler();
		}
		return instance;
	}
	
	
	public  Vector<Transaction> getAllTransaction() throws SQLException {
		Vector<Transaction> transactionList = Transaction.getAllTransaction();
		return transactionList;
	}
	
	public Transaction insertTarnsaction(Integer paymentTypeID) {
		long time = System.currentTimeMillis();
		Date currDate = new Date(time);
		Transaction t = new Transaction();
		t.setPaymentTypeID(paymentTypeID);
		t.setEmployeeId(home.getLoggedEmployee().getID());
		t.setDate(currDate);
		try {
			t.addTransaction();
		} catch (SQLException e) {
			frame.showError();
			e.printStackTrace();
		}
		return t;
	}
	
	public void checkOut(Vector<CartItem> cart, Integer TransactionID) {
		for(CartItem item : cart) {
			productController.reduceProduct(item.getProductId(), item.getQuantity());
			TransactionItem ti = new TransactionItem();
			ti.setProductId(item.getProductId());
			ti.setTransactionId(TransactionID);
			ti.setQuantity(item.getQuantity());
			try {
				ti.addTransactionItem();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Transaction getTransaction(Integer ID) {
		Transaction t = null;
		try {
			t = Transaction.getTransaction(ID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public Vector<TransactionItem> getTransactionDetail(Integer ID){
		Vector<TransactionItem> list = null;
		try {
			list = TransactionItem.getAllItemFromHeader(ID);
		} catch (SQLException e) {
			list = new Vector<TransactionItem>();
		};
		return list;
	}
	
	public void showPage() {
		if(this.frame == null) this.frame = TransactionManagementFrame.getPage();
		this.frame.setVisible(true);		
	}
	
	public Vector<Transaction> getAllTransactions(Integer month, Integer year){
		Vector<Transaction> list = null;
		
		try {
			list = Transaction.getTransactions(month, year);
		} catch (SQLException e) {
			list = new Vector<Transaction>();
		}
		
		return list;
	}
	
	public void hidePage() {
		if(this.frame == null) this.frame = TransactionManagementFrame.getPage();
		this.frame.setVisible(false);
	}
	
	public void backButtonHandler() {
		frame.dispose();
		home.showPage(home.getLoggedEmployee().getID());
	}
}

