import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

/**
 * Unit tests used to test connecting to and using
 * the endgame database.
 * H2, Java SQL database.
 */
public class DatabaseTests {

    /* CREATE TABLE endgameDatabase (
            key  INT,
            value MEDIUMBLOB
       );

       SELECT * FROM endgameDatabase

       java -jar h2*.jar to open console window
    */


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
     * Testing that we can create a table in the database
     */
    @Test
    public void testCreateTable(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            String createTable = "CREATE TABLE testDatabase (" +
                                "key  INT," +
                                "value MEDIUMBLOB" +
                            ");";

            Statement statement = connection.createStatement();
            statement.executeUpdate(createTable);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can create a table in the database
     */
    @Test
    public void testInsert(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            String insertStatement = "INSERT INTO testDatabase (key) VALUES (1)";

            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(insertStatement);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can create a table in the database
     */
    @Test
    public void testClearDatabase(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            String insertStatement = "DELETE FROM testDatabase";

            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(insertStatement);

            connection.close();

        } catch (SQLException e ){
            System.out.println(e);
        }
    }

    /**
     * Testing that we can read from a table
     */
    @Test
    public void testSelect(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

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
     * Testing inserting a row into the database, with a testing
     * key value of 1, and a GameValue object
     */
    @Test
    public void testInsertObjects(){

        try{

            GameValue gameValue = new GameValue();
            gameValue.left.add(new GameValue());
            gameValue.right.add(new GameValue());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gameValue);

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            byte[] gameValueAsBytes = baos.toByteArray();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO testDatabase (key, value) VALUES (1, ?)");
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
     * Testing retrieving a row from the database, and convert
     * the value back to our original object type, a GameValue
     */
    @Test
    public void testSelectObjects(){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

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
     * Filling the test database with the HashMap of partitions
     * @param partitionsDB HashMap of partitions
     */
    public void fillDatabase(HashMap<Integer, GameValue> partitionsDB){

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO testDatabase (key, value) VALUES (?, ?)");

            for(int key: partitionsDB.keySet()){

                GameValue gameValue = partitionsDB.get(key);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(gameValue);

                byte[] gameValueAsBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(gameValueAsBytes);

                preparedStatement.setInt(1, key);

                preparedStatement.setBinaryStream(2, bais, gameValueAsBytes.length);
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }
    }

    /**
     * Retrieving the test database and filling the HashMap with
     * its values
     * @return retrieved partitions HashMap
     */
    public HashMap<Integer, GameValue> retrieveDatabase(){

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        try{

            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:~/test", "connorMacfarlane", "password");

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

                int key = resultSet.getInt("key");

                //System.out.println(key + " " + gameValue);

                partitionsDB.put(key, gameValue);
            }

            statement.close();
            resultSet.close();
            connection.close();

        } catch (Exception e ){
            System.out.println(e);
        }

        return partitionsDB;
    }

    /**
     * Testing that filling the endgame database with
     * all board combinations up to 2x2, and then retrieving
     * this from the database, returns the same HashMap.
     */
    @Test
    public void testFillingAndRetrievingDatabase(){

        DatabaseFiller databaseFiller = new DatabaseFiller();
        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        // Getting all possible board variations, up to specified size
        ArrayList<Board> boards = databaseFiller.generateAllBoardCombinations(2);

        // Evaluating each board, storing the GameValues in the HashMap
        for(Board board: boards){

            board.evaluate(partitionsDB);
        }

        fillDatabase(partitionsDB);

        HashMap<Integer, GameValue> retrievedPartitionsDB = retrieveDatabase();

        // Checking that the partitions HashMap is the same before and
        // after it goes into the Database
        assertEquals(partitionsDB.size(), retrievedPartitionsDB.size());

        for(int key: partitionsDB.keySet()) {

            GameValue gameValue = partitionsDB.get(key);
            GameValue retrievedGameValue = partitionsDB.get(key);

            assertTrue(gameValue.equals(retrievedGameValue));
        }
    }

}
