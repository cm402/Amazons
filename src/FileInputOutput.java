import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileInputOutput {

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

    public HashMap<Integer, GameValue> getPartitionsDB(){

        try{
            FileInputStream fileInputStream = new FileInputStream(new File("partitionsDB.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

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

    public void outputGameFile(GameFile gameFile){

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(new File("savedGames/game1.txt"));
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

    public GameFile getGameFile(){

        try{
            FileInputStream fileInputStream = new FileInputStream(new File("savedGames/game1.txt"));
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
