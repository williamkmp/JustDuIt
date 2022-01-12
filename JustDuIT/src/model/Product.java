package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;

public class Product {
	private static Connect db = Connect.getConnection();
	private Integer ID;
	private String name;
	private String description;
	private Integer stock;
	private Integer price;
	
	public Product() {
		
	}

	public Product(Integer iD, String name, String description, Integer stock, Integer price) {
		super();
		ID = iD;
		this.name = name;
		this.description = description;
		this.stock = stock;
		this.price = price;
	}
	
	public static Vector<Product> getAllProduct() throws SQLException{
		Vector<Product> productList = new Vector<Product>();
		
		String query = "SELECT * FROM `products`";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			Product p = new Product();
			p.setID(result.getInt("id"));
			p.setName(result.getString("name"));
			p.setDescription(result.getString("description"));
			p.setPrice(result.getInt("price"));
			p.setStock(result.getInt("stock"));
			productList.add(p);
		}
		
		return productList;
	}
	
	public static Product getProduct(int id) throws SQLException {
		Product p = new Product();
		String query = "SELECT * FROM `products` WHERE `products`.`id` = " + id + ";";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			p.setID(result.getInt("id"));
			p.setName(result.getString("name"));
			p.setDescription(result.getString("description"));
			p.setPrice(result.getInt("price"));
			p.setStock(result.getInt("stock"));
		}
		return p;
	}
	
	public void insertProduct() throws SQLException {
		String query = "INSERT INTO `products`(`name`, `description`, `price`, `stock`) VALUES (?, ?, ?, ?)";
		PreparedStatement s = db.prepareStatement(query);
		s.setString(1, this.getName());
		s.setString(2, this.getDescription());
		s.setInt(3, this.getPrice());
		s.setInt(4, this.getStock());
		s.execute();
		
		ResultSet result = s.getGeneratedKeys();
		while(result.next()) {
			this.ID = result.getInt(1);
			System.out.println(this.ID);
		}
	}
	
	public void updateProduct() throws SQLException {
		String query = "UPDATE `products` SET name = ?, description = ?, price = ?,  stock = ? WHERE id = " + this.getID() + ";";
		PreparedStatement s = db.prepareStatement(query);
		s.setString(1, this.getName());
		s.setString(2, this.getDescription());
		s.setInt(3, this.getPrice());
		s.setInt(4, this.getStock());
		s.execute();
	}
	
	public void deleteProduct() {
		String query = "DELETE FROM `products` WHERE `products`.`id` = " + this.getID() + ";";
		db.executeUpdate(query);
	}
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	

}
