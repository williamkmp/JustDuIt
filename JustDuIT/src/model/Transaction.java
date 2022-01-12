package model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;
import controller.TransactionHandler;

public class Transaction {
	private static Connect db = Connect.getConnection();
	private Integer ID;
	private Date date;
	private Integer employeeId;
	private Integer paymentTypeID;
	private Integer total;

	public Transaction() {
		super();
		this.ID = null;
		this.date = null;
		this.employeeId = null;
		this.paymentTypeID = null;
		this.total = null;
	}

	public Transaction(Integer ID, Date date, Integer employeeId, Integer paymentTypeID) {
		super();
		this.ID = ID;
		this.date = date;
		this.employeeId = employeeId;
		this.paymentTypeID = paymentTypeID;
		this.total = 0;
	}

	public static Vector<Transaction> getAllTransaction() throws SQLException {
		Vector<Transaction> transactionList = new Vector<Transaction>();

		String query = "SELECT * FROM `transactions`";

		ResultSet result = db.executeQuery(query);

		while (result.next()) {
			Transaction T = new Transaction();
			T.setID(result.getInt("id"));
			T.setDate(result.getDate("purchase_date"));
			T.setEmployeeId(result.getInt("employee_id"));
			T.setPaymentTypeID(result.getInt("payment_type_id"));
			transactionList.add(T);
		}

		return transactionList;
	}

	public static Transaction getTransaction(Integer ID) throws SQLException {
		Transaction t = new Transaction();
		String query = "SELECT * FROM `transactions` WHERE `transactions`.`id` = " + ID + ";";
		ResultSet result = db.executeQuery(query);
		while (result.next()) {
			t.setID(result.getInt("id"));
			t.setDate(result.getDate("purchase_date"));
			t.setEmployeeId(result.getInt("employee_id"));
			t.setPaymentTypeID(result.getInt("payment_type_id"));
		}
		return t;
	}

	public void addTransaction() throws SQLException {
		String query = "INSERT INTO transactions (`purchase_date`, `employee_id`, `payment_type_id`)"
				+ "VALUES (?, ?, ?);";
		PreparedStatement s = db.prepareStatement(query);
		s.setDate(1, this.date);
		s.setInt(2, this.employeeId);
		s.setInt(3, this.paymentTypeID);
		s.execute();

		ResultSet result = s.getGeneratedKeys();
		while (result.next()) {
			this.ID = result.getInt(1);
			System.out.println(this.ID);
		}
	}

	public static Vector<Transaction> getTransactions(Integer month, Integer year) throws SQLException {
		Vector<Transaction> transactionList = new Vector<Transaction>();
		String query = "SELECT * FROM `transactions` WHERE MONTH(`transactions`.`purchase_date`) = " + month
				+ " AND YEAR(`transactions`.`purchase_date`) = " + year + "; ";

		ResultSet result = db.executeQuery(query);
		while (result.next()) {
			Transaction T = new Transaction();
			T.setID(result.getInt("id"));
			T.setDate(result.getDate("purchase_date"));
			T.setEmployeeId(result.getInt("employee_id"));
			T.setPaymentTypeID(result.getInt("payment_type_id"));
			transactionList.add(T);
		}
		return transactionList;
	}

	public Integer getID() {
		return this.ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getPaymentType() {
		return this.paymentTypeID;
	}

	public void setPaymentTypeID(Integer paymentTypeID) {
		this.paymentTypeID = paymentTypeID;
	}

	public Integer getTotal() throws SQLException {
		Integer total = 0;
		String query = "SELECT `transactions`.`id`,`transactions`.`purchase_date`,`transactions`.`payment_type_id`,`transactions`.`employee_id`,SUM(`products`.`price` * `transaction_items`.`quantity`) AS `total` FROM `transactions` JOIN `transaction_items` ON `transactions`.`id` = `transaction_items`.`transaction_id` JOIN `products` ON `products`.`id` = `transaction_items`.`product_id` WHERE `transactions`.`id` = "
				+ this.getID()
				+ " GROUP BY `transactions`.`id`, `transactions`.`purchase_date`, `transactions`.`payment_type_id`,`transactions`.`employee_id`;";
		ResultSet result = db.executeQuery(query);
		while (result.next()) {
			total = result.getInt("total");
		}
		this.total = total;
		return this.total;
	}

}
