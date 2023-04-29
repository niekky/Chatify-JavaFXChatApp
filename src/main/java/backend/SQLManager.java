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
        }finally {}

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
        Dotenv dotenv = Dotenv.load();
        String url = dotenv.get("URL");
        String name = dotenv.get("USER");
        String pass = dotenv.get("PASSWORD");
        String database_name = "test";
        String user = "postgres";
        String password = "****";

        String databaseName = "postgres";
        String username = "postgres";
        String password = "1234";

        try {
//			// LOCAL DATABASE //
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/"+databaseName,
                    username,
                    password);


            // AWS DATABASE //
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(
//                    url,
//                    name,
//                    pass);
//
=======
            ////////////////////////////////////////////////////////

            return connection;


        } catch (Exception e){
            System.out.println("Connection Failed");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }
    protected void deleteData(String tableName) {

        Connection connection = connectDatabase();
        try {

            stmt = connection.createStatement();
            String sql = "TRUNCATE TABLE " + tableName + " CASCADE;";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.close();

        }catch(Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }finally {}
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
        }finally {}

        return result;

    }

    protected void updateColumn(String tableName, String columnName,  String value, String columnKey, String key) {

        //UPDATE my_table SET my_column = 'new_value' WHERE primary_key_column = 'primary_key_value';
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
        }finally {}
    }

    public void initializeDatabase(){


        Connection connection = connectDatabase();

        try {
            Statement stmt = connection.createStatement();

            String q1 = "SELECT * FROM rooms WHERE rooms.room_name = '"+database_name+"';";
            ResultSet rs = stmt.executeQuery(q1);

        } catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void createTable(String table_name, String[] features){

    }


    private void update(String table_name, String[] features, String[] data){

    }

    private void delete(String table_name){

    }

    private void check(){

    }
}
