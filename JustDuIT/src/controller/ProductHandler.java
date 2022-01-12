package controller;

import java.sql.SQLException;
import java.util.Vector;

import model.Product;
import view.productManagement.ProductManagementFrame;

public class ProductHandler {
	private static ProductHandler instance;
	private ProductManagementFrame frame;
	private HomeController home;
	
	private ProductHandler() {
		this.home = HomeController.getInstance();
	}
	
	public static synchronized ProductHandler getInstance() {
		if(instance == null) {
			instance = new ProductHandler();
		}
		return instance;
	}
	
	public Vector<Product> getAllProduct(){
		Vector<Product> productList = null;
		try {
			productList = Product.getAllProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productList;
	}
	
	public void addProduct(String name, String description, Integer price, Integer stock) {
		Product p = new Product();
		p.setName(name);
		p.setPrice(price);
		p.setDescription(description);
		p.setStock(stock);
		try {
			p.insertProduct();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reduceProduct(Integer ID, Integer reduceBy) {
		try {
			Product p = Product.getProduct(ID);
			p.setStock(p.getStock() - reduceBy);
			p.updateProduct();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validateProduct(String name, String description, Integer price, Integer stock) {
		return (
			name != null && name.length() > 0 &&
			description != null && description.length() > 0 &&
			price != null && price > 0 &&
			stock != null && stock > 0
		);
	}
	
	public void updateProduct(Integer ID, String name, String description, Integer price, Integer stock) {
		Product p;
		try {
			p = Product.getProduct(ID);
			if(p != null) {				
				p.setName(name);
				p.setDescription(description);
				p.setPrice(price);
				p.setStock(stock);
				p.updateProduct();
			}
		} catch (SQLException e) {
			frame.showError();
		}
	}

	public void deleteProduct(Integer ID) throws SQLException {
		Product p = Product.getProduct(ID);
		if(p != null) p.deleteProduct();
	}
	
	public void showPage() {
		if(this.frame == null) this.frame = ProductManagementFrame.getPage();
		this.frame.setVisible(true);
	}
	
	public void hidePage() {
		if(this.frame == null) this.frame = ProductManagementFrame.getPage();
		this.frame.setVisible(false);
	}
	
	public void backButtonHandler() {
		frame.dispose();
		home.showPage(home.getLoggedEmployee().getID());
	}
}
