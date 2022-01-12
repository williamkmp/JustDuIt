package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;
 

public class Employee {
	private static Connect db  = Connect.getConnection();
	
	private Integer ID;
	private Integer roleID;
	private String name;
	private String username;
	private Integer salary;
	private String status;
	private String password;
	
	public Employee() {
		super();
		this.ID = null;
		this.roleID = null;
		this.name = null;
		this.username = null;
		this.salary = null;
		this.status = null;
		this.password = null;
	}
	
	public Employee(Integer ID, Integer roleID, String name, String username, Integer salary, String status,String password) {
		super();
		this.ID = ID;
		this.roleID = roleID;
		this.name = name;
		this.username = username;
		this.salary = salary;
		this.status = status;
		this.password = password;
	}
	
	public static Vector<Employee> getAllEmployee() throws SQLException{
		Vector<Employee> employeeList = new Vector<Employee>();
		
		String query = "SELECT * FROM employees ;";
		ResultSet result = db.executeQuery(query);
		
		
		while(result.next()) {
			Integer eID = result.getInt("id");
			Integer eRoleID = result.getInt("role_id");
			String eName = result.getString("name");
			String  eUsername = result.getString("username");
			Integer eSalary = result.getInt("salary");
			String eStatus = result.getString("status");
			String password = result.getString("password");
			
			Employee e = new Employee(eID, eRoleID, eName, eUsername, eSalary, eStatus, password);
			employeeList.add(e);
		}
		
		return employeeList;
	}
	
	public static Employee getEmployee(Integer id) throws SQLException {
		Employee e = null;
		String query = "SELECT * FROM employees WHERE employees.id = " + id + ";";
		ResultSet result = db.executeQuery(query);
		
		while(result.next()) {
			Integer eID = result.getInt("id");
			Integer eRoleID = result.getInt("role_id");
			String eName = result.getString("name");
			String  eUsername = result.getString("username");
			Integer eSalary = result.getInt("salary");
			String eStatus = result.getString("status");
			String password = result.getString("password");
			
			e = new Employee(eID, eRoleID, eName, eUsername, eSalary, eStatus, password);
		}
		return e;
	}
	
	public void insertEmployee() throws SQLException {
		String query = "INSERT INTO `employees` (`role_id`, `name`, `username`, `salary`, `status`, `password`) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement s = db.prepareStatement(query);
		s.setInt(1, this.getRoleID());
		s.setString(2, this.getName());
		s.setString(3, this.getUsername());
		s.setInt(4, this.getSalary());
		s.setString(5, this.getStatus());
		s.setString(6, this.getPassword());
		s.execute();
		
		ResultSet result = s.getGeneratedKeys();
		while(result.next()) {
			this.ID = result.getInt(1);
			System.out.println(this.ID);
		}
	}
	
	public void updateEmployee() throws SQLException {
		String query = "UPDATE `employees` SET role_id = ?, name = ?, username = ?, salary = ?, status = ?, password = ? WHERE id = " + this.getID() + ";"; 
		PreparedStatement s = db.prepareStatement(query);
		s.setInt(1, this.getRoleID());
		s.setString(2, this.getName());
		s.setString(3, this.getUsername());
		s.setInt(4, this.getSalary());
		s.setString(5, this.getStatus());
		s.setString(6, this.getPassword());
		s.execute();
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public Integer getRoleID() {
		return roleID;
	}

	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
