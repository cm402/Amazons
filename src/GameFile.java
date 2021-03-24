import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores a previously played game, with the moves played and board size,
 * allowing the game to be stored in a file, as well as reviewed later.
 */
public class GameFile implements Serializable {

    private ArrayList<Move> movesPlayed;
    private int boardSize;

    public GameFile(ArrayList<Move> movesPlayed, int boardSize){
        this.movesPlayed = movesPlayed;
        this.boardSize = boardSize;
    }

    public ArrayList<Move> getMovesPlayed(){
        return this.movesPlayed;
    }

    public int getBoardSize() {
        return boardSize;
    }
}
