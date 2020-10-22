import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private boolean white;
    private boolean human;
    private ArrayList<Piece> pieces;

    public Player(boolean white, boolean human){
        this.white = white;
        this.human = human;
    }

    public void addPieces(ArrayList<Piece> pieces){
        this.pieces = pieces;
    }

    public ArrayList<Piece> getPieces(){
        return this.pieces;
    }


    public boolean isWhite(){
        return this.white;
    }

    public boolean isHuman(){
        return this.human;
    }



}