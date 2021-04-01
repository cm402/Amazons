import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;

/**
 * Unit tests used to test connecting to and using
 * the endgame database.
 * H2, Java SQL database.
 */
public class DatabaseTests {

    String url = "jdbc:h2:file:" + "./testDatabase";

    /**
     * Testing that we can connect to the database successfully.
     */
    @Test
    public void testConnection(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can create and connect to a database on file successfully.
     */
    @Test
    public void testConnectionFile(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can create a table in the database
     */
    @Test
    public void testCreateTable(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            String createTable = "CREATE TABLE testDatabase (" +
                                    "key  INT," +
                                    "value MEDIUMBLOB," +
                                    "PRIMARY KEY (key)" +
                                 ");";

            Statement statement = connection.createStatement();

            statement.executeUpdate("DROP TABLE IF EXISTS testDatabase");
            statement.executeUpdate(createTable);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can insert a row into the database
     */
    @Test
    public void testInsert(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            String insertStatement = "INSERT INTO testDatabase (key) VALUES (1)";

            Statement statement = connection.createStatement();
            statement.executeUpdate(insertStatement);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can return a whole table from the database
     */
    @Test
    public void testSelect(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            String selectQuery = "SELECT * FROM testDatabase";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()){

                int key = resultSet.getInt("key");
                System.out.println("key = " + key);

            }

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can clear a table in the database
     */
    @Test
    public void testClearDatabase(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM testDatabase");

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing inserting a row into the database, with a testing
     * key value of 1, and a GameValue object
     */
    @Test
    public void testInsertObjects(){

        try{

            GameValue gameValue = new GameValue();
            gameValue.left.add(new GameValue());
            //gameValue.right.add(new GameValue());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameValue);

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            byte[] gameValueAsBytes = baos.toByteArray();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO testDatabase (key, value) VALUES (2, ?)");
            ByteArrayInputStream bais = new ByteArrayInputStream(gameValueAsBytes);

            preparedStatement.setBinaryStream(1, bais, gameValueAsBytes.length);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }
    }

    /**
     * Testing retrieving all rows from the database, and convert
     * the value back to our original object type, a GameValue
     */
    @Test
    public void testSelectObjects(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM testDatabase");

            while(resultSet.next()){

                Blob blob = resultSet.getBlob("value");

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                blob.free();

                ByteArrayInputStream baip = new ByteArrayInputStream(blobAsBytes);
                ObjectInputStream ois = new ObjectInputStream(baip);

                GameValue gameValue = (GameValue) ois.readObject();

                System.out.println(gameValue);
            }

            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }
    }

    /**
     * Testing retrieving a specific keys row from the database, and convert
     * the value back to our original object type, a GameValue
     */
    @Test
    public void testQueryForKey(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();

            int key = 3;
            String query = "SELECT * FROM testDatabase WHERE key = " + String.valueOf(key);

            ResultSet resultSet = statement.executeQuery(query);

            // Check if value found
            if(resultSet.next()){

                Blob blob = resultSet.getBlob("value");

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                blob.free();

                ByteArrayInputStream baip = new ByteArrayInputStream(blobAsBytes);
                ObjectInputStream ois = new ObjectInputStream(baip);

                GameValue gameValue = (GameValue) ois.readObject();

                int returnedKey = resultSet.getInt("key");
                System.out.println(returnedKey + " " + gameValue);
            } else {

                System.out.print("key not found");
            }
            resultSet.close();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }
    }

}
