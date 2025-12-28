package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectDB {
	static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=DBKhachSan;encrypt=false";
	static final String USER = "sa";
	static final String PASSWORD = "sql2025";
	
	public static Connection getConnection() throws SQLException {
		Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
		if(con == null) {
			JOptionPane.showMessageDialog(null, "Không thể kết nối database");
		}
		return con;
	}
}
