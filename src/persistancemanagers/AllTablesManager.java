package persistancemanagers;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class AllTablesManager {

    private String url = null;
    private String user = null;
    private String password = null;

    private Properties prop = new Properties();
    private Connection conn = null;
    private InputStream input = null;

    public Connection connect() {

        try {
            input = new FileInputStream("src/properties");

            prop.load(input);

            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
