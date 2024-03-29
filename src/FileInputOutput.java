import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.sql.*;

/**
 * File input/output for both saved games in
 * GameFile objects, as well as the Endgame Database,
 * a mapping of boards hashcodes to evaluated GameValue objects.
 */
public class FileInputOutput {

    String databaseURL = "jdbc:h2:file:" + "./endgameDatabase";
    String tableName = "gameValues";

    /**
     * Resets the current endgame database, dropping the table, and creating a new one
     */
    public void resetEndgameDatabase(){

        try{

            Connection connection = DriverManager.getConnection(databaseURL, "connorMacfarlane", "password");

            String createTable = "CREATE TABLE " + tableName + " (" +
                    "key INT," +
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
     * Queries the database for a GameValue associated with the
     * specified key. If found, returns GameValue, otherwise null
     * @param key key to check database for
     * @return Either GameValue associated with key, or null
     */
    public GameValue queryDatabase(int key){

        try{

            Connection connection = DriverManager.getConnection(databaseURL, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM " + tableName + " WHERE key = " + String.valueOf(key);

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

                resultSet.close();
                connection.close();

                return gameValue;

            } else {

                resultSet.close();
                connection.close();

                return null;
            }

        } catch (Exception e ){
            System.out.println(e);
        }

        // if there was an issue with accessing the database, treat as if not key not found
        return null;
    }

    /**
     * Adds the a key and value pair to the endgame database. Doesn't handle
     * duplicates, so this must only be called after checking that the key doesn't
     * already exist in the database, using the query method.
     * @param key Key to add to the database, boards hashcode value
     * @param value Value to add to the database, GameValue for the board
     * @return
     */
    public void addGameValueToDatabase(int key, GameValue value){

        try{

            Connection connection = DriverManager.getConnection(databaseURL, "connorMacfarlane", "password");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);

            byte[] gameValueAsBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(gameValueAsBytes);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + tableName + " (key, value) VALUES (?, ?)");

            preparedStatement.setInt(1, key);

            preparedStatement.setBinaryStream(2, bais, gameValueAsBytes.length);
            preparedStatement.executeUpdate();

        } catch (Exception e ){
            System.out.println(e);
        }

    }

    /**
     * Getting the number of entries in the endgame database
     * @return
     */
    public int getEndgameDatabaseSize(){

        try{

            Connection connection = DriverManager.getConnection(
                    databaseURL, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            int rowCount = 0;

            while(resultSet.next()){

                rowCount++;
            }

            return rowCount;

        } catch (Exception e ){
            System.out.println(e);
        }

        return 0;
    }

    /**
     * Outputting a GameFile object to file
     * @param gameFile GameFile to be stored
     */
    public void outputGameFile(GameFile gameFile){

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(new File("savedGames/previousGame.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(gameFile);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        }

    }

    /**
     * Retrieving the most recent saved game from file
     * @return a GameFile object
     */
    public GameFile getGameFile(){

        try{
            FileInputStream fileInputStream = new FileInputStream(new File("savedGames/previousGame.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            GameFile gameFile = (GameFile) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return gameFile;

        } catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        } catch (ClassNotFoundException e){
            System.out.println(e);
        }
        return null;
    }


    // OLD METHODS
    /**
     * Filling the test database with the HashMap of partitions
     * @param partitionsDB HashMap of partitions
     */
    public void fillDatabase(HashMap<Integer, GameValue> partitionsDB){

        try{

            Connection connection = DriverManager.getConnection(databaseURL, "connorMacfarlane", "password");

            // This has to be done, as INSERT ... ON DUPLICATE REPLACE isn't working
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + tableName + " (key, value) VALUES (?, ?)");

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
    public HashMap<Integer, GameValue> retrievePartitionsDatabase(){

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        try{

            Connection connection = DriverManager.getConnection(
                    databaseURL, "connorMacfarlane", "password");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            while(resultSet.next()){

                Blob blob = resultSet.getBlob("value");

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                blob.free();

                ByteArrayInputStream baip = new ByteArrayInputStream(blobAsBytes);
                ObjectInputStream ois = new ObjectInputStream(baip);

                GameValue gameValue = (GameValue) ois.readObject();

                int key = resultSet.getInt("key");

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
}
