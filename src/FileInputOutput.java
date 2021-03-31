import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.sql.*;

/**
 * File input/output for both saved games in
 * GameFile objects, as well as the Endgame Database,
 * a mapping of boards hashcodes to evaluated GameValue objects.
 */
public class FileInputOutput {

    /**
     * Outputting the Endgame Database to file
     * @param partitionsDB Endgame Database, HashMap of evaluated boards GameValues
     */
    public void outputDB(HashMap<Integer, GameValue> partitionsDB){

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(new File("partitionsDB.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(partitionsDB);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        }

    }

    /**
     * Retrieving the Endgame Database from file
     * @return HashMap of evaluated boards GameValues
     */
    public HashMap<Integer, GameValue> getPartitionsDB(){

        try{
            FileInputStream fileInputStream = new FileInputStream(new File("partitionsDB.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Suppressing "unchecked" warning from casting the object read in at run-time, to the correct type
            @SuppressWarnings("unchecked")
            HashMap<Integer, GameValue> partitionsDB = (HashMap<Integer, GameValue>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return partitionsDB;

        } catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        } catch (ClassNotFoundException e){
            System.out.println(e);
        }
        return null;
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
}
