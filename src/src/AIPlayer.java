import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player implements Serializable {

    // constructor
    public AIPlayer(boolean white){
        super(white, false);
    }

    public Move getRandomMove(ArrayList<Move> moves){
        Random rand = new Random();
        return moves.get(rand.nextInt(moves.size()));
    }

    @Override
    public Move getMove(Board board, String gameStatus){

        ArrayList<Move> validMoves = super.getValidMoves(board);

        if(validMoves.isEmpty()){
            return null;
        } else {
            return getRandomMove(validMoves);
        }
    }

    // heuristics
    
    // D1 in upper left corner
    // D2 in lower right corner

    // D1 is min number of queen moves, from one of players amazons to square
    // D2 is min number of king moves, from one of players amazons to square

    // if a player has D1 better than their opponents D1, they have better access








}
