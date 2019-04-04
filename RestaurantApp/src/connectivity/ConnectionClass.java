package connectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionClass
{
    private Connection connection;

    public Connection getConnection()
    {
        String dbName = "restaurant_db";
        String userName = "root";
        String password = "";

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        }
        catch( ClassNotFoundException | SQLException e )
        {
            e.printStackTrace();
        }

        return connection;
    }

    public static void createTables()
    {
        Connection connection = new ConnectionClass().getConnection();
        try
        {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users(name VARCHAR(100), password VARCHAR(100), position VARCHAR(100));";
            statement.execute( sql );
            connection.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }

    public static void createDB()
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=");
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS restaurant_db");
            connection.close();
        }
        catch( SQLException e )
        {
            e.printStackTrace();
        }
    }
}