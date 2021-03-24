import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 4L;

    HashMap<Integer, GameValue> partitionsDB;

    String AIType;

    public AIPlayer(boolean white){
        super(white, false);
    }

    public AIPlayer(boolean white, HashMap<Integer, GameValue> partitionsDB){
        super(white, false);
        this.partitionsDB = partitionsDB;
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

            Board newBoard = board.playMove(move);

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
    public Move getCGTMove(Board board){

        GameValue gameValue = board.evaluate(partitionsDB);
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

            System.out.println("AI is thinking");

            // When no AI type specified, default is MCTS
            return getMonteCarloMove(board);
            /*
            if(AIType.equals("MCTS")){

                return getMonteCarloMove(board);

            } else if(AIType.equals("Heuristic")){

                return getHeuristicMove(board);

            } else if(AIType.equals("CGT")){

                return getCGTMove(board);

            } else if(AIType.equals("Random")){

                return getRandomMove(validMoves);

            }
            */

            // TODO- use experiments to decide when to switch between move strategies
            // Initial plan
            // 1. Heuristic for first 5 or 10 moves, depending on board size
            // 2. Monte Carlo for middle-game
            // 3. Once board split into partitions that are small enough, use endgame move
        }
    }

}
