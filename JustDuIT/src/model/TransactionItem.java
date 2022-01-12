package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;

public class TransactionItem {
	private static Connect db = Connect.getConnection(); 
	private Integer transactionId;
	private Integer productId;
	private Integer quantity;
	
	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public TransactionItem() {
		// TODO Auto-generated constructor stub
	}
	
	public TransactionItem addTransactionItem() throws SQLException {
		String query = "INSERT INTO `transaction_items` (`transaction_id`, `product_id`, `quantity`) VALUES (?, ?, ?);";
		PreparedStatement s = db.prepareStatement(query);
		s.setInt(1, this.getTransactionId());
		s.setInt(2, this.getProductId());
		s.setInt(3, this.getQuantity());
		s.execute();
		return this;
	}
	
	public static TransactionItem getTransaction(Integer ID) throws SQLException {
		TransactionItem ti = new TransactionItem();
		String query = "SELECT * FROM `transaction_items` WHERE `transaction_items`.`id` = " + ID + ";";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			ti.setProductId(result.getInt("product_id"));
			ti.setTransactionId(result.getInt("transaction_id"));
			ti.setQuantity(result.getInt("quantity"));
		}
		return ti;
	}
	
	public static Vector<TransactionItem> getAllItemFromHeader(Integer headerId) throws SQLException{
		Vector<TransactionItem> itemList = new Vector<TransactionItem>();
		String query = "SELECT * FROM `transaction_items` WHERE `transaction_items`.`transaction_id` = " + headerId + ";";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			TransactionItem ti = new TransactionItem();
			ti.setProductId(result.getInt("product_id"));
			ti.setTransactionId(result.getInt("transaction_id"));
			ti.setQuantity(result.getInt("quantity"));
			itemList.add(ti);
		}
		return itemList;
	}

}
