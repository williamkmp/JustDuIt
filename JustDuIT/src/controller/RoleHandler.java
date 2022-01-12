package controller;

import java.sql.SQLException;
import java.util.Vector;

import model.Role;

public class RoleHandler {
	private static RoleHandler instance;
	private RoleHandler() {
		
	}
	
	public static synchronized RoleHandler getInstance() {
		if(instance == null) {
			instance = new RoleHandler();
		}
		return instance;
	}
	
	public Vector<Role> getAllRole(){
		Vector<Role> roleList = new Vector<Role>();
		try {
			roleList = Role.getAllRole();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleList;
	}
	
	public Role getRole(Integer ID) {
		Role r = null;
		try {
			r = Role.getRole(ID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}
}
