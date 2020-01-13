package onlenploris.flower;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import onlenploris.DatabaseConnection;


public class Flower {
	
	private long id;
	private String name;
	private String type;
	private int stock;
	private int price;

	public Flower(long id, String name, String type, int stock, int price) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.stock = stock;
		this.price = price;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getStock() {
		return stock;
	}

	public int getPrice() {
		return price;
	}
	

	public static Vector<Flower> all() throws SQLException {	
		Vector<Flower> list_flower = new Vector<Flower>();
		
		String query = "SELECT flower.id, flower.name, type.name, stock, price FROM "
				+ DatabaseConnection.FLOWER_TABLE_NAME + " AS flower JOIN "
				+ DatabaseConnection.TYPE_TABLE_NAME + " AS type ON flower.type = type.id";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;	
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet res = pst.executeQuery();
		
		while(res.next()) {
			long res_id = res.getLong(1);
			String res_name = res.getString(2);
			String res_type = res.getString(3);
			int res_stock = res.getInt(4);
			int res_price = res.getInt(5);
			list_flower.add(new Flower(res_id, res_name, res_type, res_stock, res_price));
		}
		res.close();
		pst.close();
		DatabaseConnection.close();
		return list_flower;
	}
	
	public static Flower id(long id) throws SQLException {
		Flower flower = null;
		
		String query = "SELECT flower.id, flower.name, type.name, stock, price FROM "
				+ DatabaseConnection.FLOWER_TABLE_NAME + " AS flower JOIN "
				+ DatabaseConnection.TYPE_TABLE_NAME + " AS type ON flower.type = type.id "
				+ "WHERE flower.id = ?";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;		
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setLong(1, id);
		ResultSet res = pst.executeQuery();
		
		if(res.next()) {
			long res_id = res.getLong("id");
			String res_name = res.getString("name");
			String res_type = res.getString("type");
			int res_stock = res.getInt("stock");
			int res_price = res.getInt("price");
			flower =  new Flower(res_id, res_name, res_type, res_stock, res_price);
		}
		res.close();
		pst.close();
		DatabaseConnection.close();
		return flower;
	}
	
	public static void insert(String name, String type, int stock, int price) throws SQLException {	
		String subQuery = "(SELECT id FROM " + DatabaseConnection.TYPE_TABLE_NAME
				+ " WHERE name=?)";
		String query = "INSERT INTO " + DatabaseConnection.FLOWER_TABLE_NAME
				+ "(name, type, stock, price) "
				+ "VALUES(?, " + subQuery + ", ?, ?)";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, name);
		pst.setString(2, type);
		pst.setInt(3, stock);
		pst.setInt(4, price);
		pst.execute();
		pst.close();
		DatabaseConnection.close();
	}
	
	public static void update(long id, String name, String type, int stock, int price) throws SQLException {
		String subQuery = "(SELECT id FROM " + DatabaseConnection.TYPE_TABLE_NAME
				+ " WHERE name=?)";
		String query = "UPDATE " + DatabaseConnection.FLOWER_TABLE_NAME
				+ " SET name = ?, type = " + subQuery
				+ ", stock = ?, price = ? WHERE id = ?";
		
		DatabaseConnection.open();
		Connection conn = DatabaseConnection.conn;
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, name);
		pst.setString(2, type);
		pst.setInt(3, stock);
		pst.setInt(4, price);
		pst.setLong(5, id);
		pst.execute();
		pst.close();
		DatabaseConnection.close();
	}
	
	public static void delete(long id) throws SQLException {
		String query = "DELETE FROM " + DatabaseConnection.FLOWER_TABLE_NAME
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
