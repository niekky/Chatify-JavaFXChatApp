package backend;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLManager {

    //	Connection c = null;
    Statement stmt = null;

    String database_name = "test";
    //	String database_name = "projectdb";
    String user = "postgres";
    String password = "****";

    protected int maxVal(String tableName, String columnName) {
        Connection connection = connectDatabase();
        try {

            stmt = connection.createStatement();

            String sql = "SELECT MAX( " + columnName + ")" +
                    "FROM " + tableName + ";";
            ResultSet rs = stmt.executeQuery(sql);

            int result = -1;

            if(rs.next())
                result = rs.getInt(1);

            stmt.close();
            connection.close();

            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return -1;

    }

    protected int getUserId(String data) {
        Connection connection = connectDatabase();

        try {

            connection.setAutoCommit(false);
            stmt = connection.createStatement();

            String sql = "SELECT user_id FROM users WHERE username = '" + data + "';";
            ResultSet rs = stmt.executeQuery(sql);

            int result = -1;

            if(rs.next())
                result = rs.getInt(1);

            stmt.close();
            connection.close();

            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }finally {}

        return -1;
    }

    protected String checkPassword(String userId) {
        Connection connection = connectDatabase();
        String result = null;
        try {

            connection.setAutoCommit(false);
            stmt = connection.createStatement();

            String sql = "SELECT password FROM users WHERE user_id = " + userId + ";";
            ResultSet rs = stmt.executeQuery(sql);


            if(rs.next()){
                result = rs.getString(1);
//				System.out.println(result);
            }

            stmt.close();
            connection.close();

            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }finally {}

        return result;
    }

    protected void addColumn(String tableName, String columns, String data) {

        Connection connection = connectDatabase();

        try {

            stmt = connection.createStatement();
            String sql = "INSERT INTO " + tableName +" ( " + columns + " ) " +
                    "VALUES ( " + data + " );";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.close();

        }catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }finally {}
    }

    protected Connection connectDatabase(){
        Connection connection = null;

        String database_name = "test";
        String user = "postgres";
        String password = "19781902Cfc";

        Dotenv dotenv = Dotenv.load();

        String url = dotenv.get("URL");
        String name = dotenv.get("USER");
        String pass = dotenv.get("PASSWORD");

        try {

//			// LOCAL DATABASE //
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/"+database_name,
//                    user,
//                    password);

//       // AWS DATABASE //
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    url,
                    name,
                    pass);

            //////////////////////////////////////////////////////

            return connection;


        } catch (Exception e){
            System.out.println("Connection Failed");
            System.out.println(".env file may be not found or .env file contains incorrect configuration!");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    protected boolean searchForMatch(String tableName, String columnName, String value) {

        boolean result = false;
        Connection connection = connectDatabase();
        try {

            stmt = connection.createStatement();
            String sql = "SELECT EXISTS (SELECT 1 FROM " + tableName +
                    " WHERE " + columnName + " = '" + value + "');";

            ResultSet rs = stmt.executeQuery(sql);

            if(rs.next()) {
                result = rs.getBoolean(1);
            }


            stmt.close();
            connection.close();

        }catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return result;

    }

    protected void updateColumn(String tableName, String columnName,  String value, String columnKey, String key) {
        Connection connection = connectDatabase();

        try {

            stmt = connection.createStatement();
            String sql = "UPDATE " + tableName +
                    " SET " + columnName + " = '" + value +
                    "' WHERE " + columnKey + " = '" + key + "';";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.close();

        }catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
