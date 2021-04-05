import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.sql.*;

/**
 * Unit tests used to test connecting to and using
 * the endgame database.
 * H2, Java SQL database.
 */
public class DatabaseTests {

    static String url = "jdbc:h2:file:" + "./endgameDatabase";
    static String tableName = "testingTable";
    static GameValue gameValue;
    static byte[] gameValueAsBytes;

    /**
     * Before running any database tests, drop the old table and
     * create a new one.
     */
    @BeforeClass
    public static void setup(){

        // Generating a GameValue to be used during testing, as an object and as a byte array
        try {
            gameValue = new GameValue();
            gameValue.left.add(new GameValue());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameValue);
            gameValueAsBytes = baos.toByteArray();

        } catch (IOException e){
            System.out.println(e);
        }

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            String createTable = "CREATE TABLE " + tableName + " (" +
                    "key  INT," +
                    "value MEDIUMBLOB," +
                    "PRIMARY KEY (key)" +
                    ");";

            Statement statement = connection.createStatement();

            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            statement.executeUpdate(createTable);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Before each test, we must clear all rows from the table,
     * to ensure that each test starts with a fresh table,
     * as we can't ensure the ordering of the tests
     */
    @Before
    public void clearTable(){

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing adding 3 rows, retrieving all rows from the
     * database, and returning the size of the database.
     */
    @Test
    public void testRetrieveDatabaseSize(){

        int databaseSize = 0;

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + tableName + " (key, value) VALUES (?, ?)");

            ByteArrayInputStream bais = new ByteArrayInputStream(gameValueAsBytes);
            preparedStatement.setBinaryStream(2, bais, gameValueAsBytes.length);

            // 1. Storing 3 rows in database
            preparedStatement.setInt(1, 1);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, 2);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, 3);
            preparedStatement.executeUpdate();

            // 2. Retrieving rows from database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            // 3. Counting number of rows
            while(resultSet.next()){

                databaseSize++;
            }

            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }

        assertTrue(databaseSize == 3);
    }

    /**
     * Testing adding and retrieving a specific row from the database using a key,
     * and convert the value back to our original object type, a GameValue
     */
    @Test
    public void testQueryForKey(){

        GameValue retrievedGameValue = null;

        try{

            Connection connection = DriverManager.getConnection(url, "connorMacfarlane", "password");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + tableName + " (key, value) VALUES (?, ?)");

            ByteArrayInputStream bais = new ByteArrayInputStream(gameValueAsBytes);
            preparedStatement.setBinaryStream(2, bais, gameValueAsBytes.length);

            // Part 1- Storing 3 rows in database

            preparedStatement.setInt(1, 1);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, 123123);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, 3);
            preparedStatement.executeUpdate();

            // PART 2- Retrieving row from database

            int key = 123123;
            String query = "SELECT * FROM " + tableName + " WHERE key = " + String.valueOf(key);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Check if value found
            if(resultSet.next()){

                Blob blob = resultSet.getBlob("value");

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                blob.free();

                ByteArrayInputStream baip = new ByteArrayInputStream(blobAsBytes);
                ObjectInputStream ois = new ObjectInputStream(baip);

                retrievedGameValue = (GameValue) ois.readObject();


            } else {

                System.out.println("key not found");
            }
            resultSet.close();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }

        assertTrue(gameValue.equals(retrievedGameValue));
    }

}
