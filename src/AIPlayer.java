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
            gameStatus = "finished";
            return null;
        } else {
            return getRandomMove(validMoves);
        }
    }


}
