package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;

public class Role {
	private static Connect db = Connect.getConnection();
	private Integer ID;
	private String role;
	
	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Role() {
		// TODO Auto-generated constructor stub
	}

	public static Role getRole(Integer ID) throws SQLException {
		Role r = new Role();
		String query = "SELECT * FROM `roles` WHERE `roles`.`id` = " + ID + ";";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			r.setID(result.getInt("id"));
			r.setRole(result.getString("name"));
		}
		return r;
	}
	
	public static Vector<Role> getAllRole() throws SQLException{
		Vector<Role> roleList = new Vector<Role>();
		String query = "SELECT * FROM `roles`;";
		ResultSet result = db.executeQuery(query);
		while(result.next()) {
			Role r = new Role();
			r.setID(result.getInt("id"));
			r.setRole(result.getString("name"));
			roleList.add(r);
		}
		return roleList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getRole();
	}
}
