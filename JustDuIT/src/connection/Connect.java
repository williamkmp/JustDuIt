package connection;
import java.sql.*;

public final class Connect {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "justduit";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	public ResultSet rs;
	public ResultSetMetaData rsm;
	private Connection con;
	private Statement st;
	private static Connect connect;
	
	private Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			st = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Filed to connect to database, the system is terminated!");
			System.exit(0);
		}
	}
	
	public static synchronized Connect getConnection() {
		return connect = (connect == null) ? new Connect() : connect;
	}

	public ResultSet executeQuery(String query) {
		rs = null;
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void executeUpdate(String query) {
		try {
			st.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
	}
}
