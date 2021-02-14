import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

    // Returns a move chosen using a monte-carlo tree search strategy
    public Move getMonteCarloMove(Board board){

        MCTS mcts = new MCTS();

        return mcts.getNextMove(board, this.isWhite(), 5);
    }

    // Returns a move chosen using the strategy of giving the opponent the least move options
    public Move getHeuristicMove(Board board){

        ArrayList<Move> validMoves = super.getValidMoves(board);

        int counter = 1;
        int minOpponentMoves = 0;
        Move bestMove = null;

        for(Move move: validMoves){

            Board newBoard = board.playMove(board, move);

            int opponentMoves = newBoard.getAllPossibleMoves(!this.isWhite()).size();

            if(counter == 1 || opponentMoves < minOpponentMoves){

                minOpponentMoves = opponentMoves;
                bestMove = move;
            }
            counter++;
        }

        return bestMove;
    }

    // Returns a move that is chosen using the endgame database of partitions
    public Move getEndgameMove(Board board){

        // TODO- change this so that the AIPlayer has its own partitions database hashmap object
        GameValue gameValue = board.evaluate(null);
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

            return getEndgameMove(board);
            //return getRandomMove(validMoves);
        }
    }

}
