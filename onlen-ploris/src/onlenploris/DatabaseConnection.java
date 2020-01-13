package onlenploris;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
	
	public static Connection conn;
	public static String FLOWER_TABLE_NAME = "flowers";
	public static String TYPE_TABLE_NAME = "flower_types";
	
	private static String DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PWD;
	
	public static void open() throws SQLException {
		String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
				+ "?useTimezone=true&serverTimezone=UTC";	
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, DB_USER, DB_PWD);		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() throws SQLException {
		conn.close();
	}
	
	public static void connect(String host, String port,
			String db_name, String username, String password) throws SQLException {
		
		DB_HOST = host;
		DB_PORT = port;
		DB_NAME = db_name;
		DB_USER = username;
		DB_PWD = password;
		
		check_flower_type_table();
		check_flower_table();
		
	}
	
	public static void check_flower_table() throws SQLException {
		String flower_table = null;
		
		open();
		ResultSet res = conn.getMetaData().getTables(null, null, FLOWER_TABLE_NAME, new String[]{"TABLE"});
		while(res.next()) {
			flower_table = res.getString("TABLE_NAME");
		}
		res.close();
		close();
		
		if(flower_table == null) {
			create_flower_table();
		}
	}
	
	public static void create_flower_table() throws SQLException {
		String query = "CREATE TABLE " + FLOWER_TABLE_NAME + " ("
				+ "id bigint NOT NULL AUTO_INCREMENT, "
				+ "name varchar(255) NOT NULL, "
				+ "type bigint NOT NULL, "
				+ "stock int NOT NULL, "
				+ "price int NOT NULL, "
				+ "PRIMARY KEY (id), "
				+ "FOREIGN KEY (type) REFERENCES " + TYPE_TABLE_NAME + " (id) "
				+ "ON DELETE CASCADE"
				+ ")";
		
		open();
		PreparedStatement pst = conn.prepareStatement(query);
		pst.execute();
		pst.close();
		close();
	}
	
	public static void check_flower_type_table() throws SQLException {
		String flower_type_table = null;
		
		open();
		ResultSet res = conn.getMetaData().getTables(null, null, TYPE_TABLE_NAME, new String[]{"TABLE"});
		while(res.next()) {
			flower_type_table = res.getString("TABLE_NAME");
		}
		res.close();
		close();
		
		if(flower_type_table == null) {
			create_flower_type_table();
		}
		
	}
	
	public static void create_flower_type_table() throws SQLException {
		String query = "CREATE TABLE " + TYPE_TABLE_NAME + " ("
				+ "id bigint NOT NULL AUTO_INCREMENT, "
				+ "name varchar(255) NOT NULL, "
				+ "PRIMARY KEY (id)"
				+ ")";
		
		open();
		PreparedStatement pst = conn.prepareStatement(query);
		pst.execute();
		pst.close();
		close();
	}
}
