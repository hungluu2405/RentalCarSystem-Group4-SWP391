package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection connection;
    public DBContext() {
        this.connection = getConnection(); // 👈 dòng này phải nằm TRONG constructor
    }
    public Connection getConnection() {
        try {
            // Load SQL Server Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // JDBC URL cho SQL Server
            String url = "jdbc:sqlserver://localhost:1433;databaseName=CarRentalDB;encrypt=false";
            String user = "sa";        // user SQL Server
            String password = "sa"; // pass SQL Server

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối SQL Server thành công!");
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("❌ Lỗi kết nối SQL Server: " + e.getMessage());
            return null;
        }
    }

    // Test nhanh
    public static void main(String[] args) {
        DBContext test = new DBContext();
        Connection conn = test.getConnection();
        System.out.println(conn);
    }
}
