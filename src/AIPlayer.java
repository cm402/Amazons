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

    public Move getBetterMove(Board board){

        GameValue gameValue = board.evaluate();
        gameValue.simplify();

        if(this.isWhite()){

            return gameValue.right.get(0).move;
        } else {

            return gameValue.left.get(0).move;
        }
    }

    @Override
    public Move getMove(Board board){

        ArrayList<Move> validMoves = super.getValidMoves(board);

        if(validMoves.isEmpty()){
            return null;
        } else {

            return getBetterMove(board);
            //return getRandomMove(validMoves);
        }
    }

}
