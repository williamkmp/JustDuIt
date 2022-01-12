package view.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.ProductHandler;
import controller.TransactionHandler;
import model.Product;
import model.Transaction;
import model.TransactionItem;

public class ReportDetailFrame extends JFrame{
	private ProductHandler productController;
	private TransactionHandler transactionController;
	private JLabel title, total;
	private JPanel northPanel, centerPanel, southPanel, eastPanel, westPanel;
	private JTable productTable;
	private DefaultTableModel productTableModel;
	private JScrollPane productPane;
	
	private Dimension SIZE = new Dimension(700, 400);
	public ReportDetailFrame(Integer ID) {
		this.setTitle("Transaction Details");
		this.setSize(SIZE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		productController = ProductHandler.getInstance(); 
		transactionController = TransactionHandler.getInstance();
		
		Transaction T = transactionController.getTransaction(ID);
		Vector<TransactionItem> list = transactionController.getTransactionDetail(ID);
		
		northPanel = new JPanel();
		southPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		
		title = new JLabel("Transaction ID: " + ID + " Date: " + T.getDate().toLocalDate());
		try {
			total = new JLabel("Total : Rp " + T.getTotal());
		} catch (SQLException e1) {
			total = new JLabel("Total : ERROR");
		}
		
		productTableModel = new DefaultTableModel();
		productTable = new JTable();
		
		productTableModel.addColumn("ID");
		productTableModel.addColumn("Name");
		productTableModel.addColumn("Price");
		productTableModel.addColumn("Quantity");
		
		for (TransactionItem item : list) {
			Product p;
			try {
				p = Product.getProduct(item.getProductId());
				Object[] data = {
					item.getProductId().toString(),
					p.getName().toString(),
					p.getPrice().toString(),
					item.getQuantity()
				};
				productTableModel.addRow(data);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		productTable.setModel(productTableModel);
		productTableModel.fireTableDataChanged();
		
		productPane = new JScrollPane(productTable);
		productPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		productPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		northPanel.add(title);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(productPane, BorderLayout.CENTER);
		southPanel.add(total);
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		this.add(eastPanel, BorderLayout.EAST);
		this.add(westPanel, BorderLayout.WEST);
		
		this.setVisible(true);
	}
}
