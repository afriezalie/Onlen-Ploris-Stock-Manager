package onlenploris.ftype;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import onlenploris.DatabaseConnection;

public class Ftype {
	
	private long id;
	private String name;
	
	
	public Ftype(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static Vector<Ftype> all() throws SQLException {
		Vector<Ftype> list_type = new Vector<Ftype>();
		
		String query = "SELECT * FROM " + DatabaseConnection.TYPE_TABLE_NAME;
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet res = pst.executeQuery();
		
		while(res.next()) {
			long res_id = res.getLong(1);
			String res_name = res.getString(2);	
			list_type.add(new Ftype(res_id, res_name));
		}
		res.close();
		pst.close();
		DatabaseConnection.close();
		return list_type;
	}
	
	public static Vector<String> all_names() throws SQLException {
		Vector<String> list_type = new Vector<String>();
		
		String query = "SELECT name FROM " + DatabaseConnection.TYPE_TABLE_NAME;
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;				
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet res = pst.executeQuery();
		
		while(res.next()) {
			String res_name = res.getString(1);
			list_type.add(res_name);
		}
		res.close();
		pst.close();
		DatabaseConnection.close();
		return list_type;
	}
	
	public static void insert(String name) throws SQLException {
		String query = "INSERT INTO " + DatabaseConnection.TYPE_TABLE_NAME
				+ "(name) VALUES(?)";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, name);
		pst.execute();
		pst.close();
		DatabaseConnection.close();
	}
	
	public static void update(long id, String name) throws SQLException {
		String query = "UPDATE " + DatabaseConnection.TYPE_TABLE_NAME
				+ " SET name = ? WHERE id = ?";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, name);
		pst.setLong(2, id);
		pst.execute();
		pst.close();
		DatabaseConnection.close();
	}
	
	public static void delete(long id) throws SQLException {
		String query = "DELETE FROM " + DatabaseConnection.TYPE_TABLE_NAME
				+ " WHERE id = ?";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setLong(1, id);
		pst.execute();
		pst.close();
		DatabaseConnection.close();
	}

}
