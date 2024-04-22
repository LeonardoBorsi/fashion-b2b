package main.orm;
import java.sql.*;

public class ConnectionManager {
    
    private static final String url = "jdbc:postgresql://dpg-cnr278ol5elc73b09ki0-a.frankfurt-postgres.render.com/fashion_b2b";
    private static final String username = "leonardo";
    private static final String password = "jlMwwLj7nD81EL2Z2LuzbO1E2RNjZlWp";
    private static Connection connection = null;

    private ConnectionManager(){}

    static public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        if (connection == null) {
            connection = DriverManager.getConnection(url, username, password);
        }

        return connection;
    }
}