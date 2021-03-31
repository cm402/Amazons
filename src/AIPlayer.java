import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Represents an AI Player, with 4 options for selecting moves.
 * 1. Monte-Carlo Tree Search
 * 2. Limit Opponents move options heuristic
 * 3. Combinatorial Game Theory evaluation
 * 4. Random Choice
 */
public class AIPlayer extends Player implements Serializable {

    private static final long serialVersionUID = 4L;
    HashMap<Integer, GameValue> partitionsDB;
    String AIType;

    public AIPlayer(boolean white){
        super(white, false);
        this.AIType = "Heuristic";
    }

    public AIPlayer(boolean white, String AIType, HashMap<Integer, GameValue> partitionsDB){
        super(white, false);
        this.AIType = AIType;
        this.partitionsDB = partitionsDB;
    }

    public AIPlayer(boolean white, HashMap<Integer, GameValue> partitionsDB) {
        super(white, false);
        this.partitionsDB = partitionsDB;
        AIType = "Heuristic";
    }

    public void setAIType(String AIType){

        this.AIType = AIType;
    }

    /**
     * Returns a move chosen using a monte-carlo tree search strategy
     * @param board current board
     * @return selected move
     */
    public Move getMonteCarloMove(Board board){

        MCTS mcts = new MCTS();
        //mcts.heuristicOptimisation = true;

        return mcts.getNextMove(board, this.isWhite(), 5);
    }

    /**
     * Returns a move chosen using the strategy of giving the opponent the least move options
     * @param board current board
     * @return selected move
     */
    public Move getHeuristicMove(Board board){

        ArrayList<Move> validMoves = super.getValidMoves(board);

        int counter = 1;
        int minOpponentMoves = 0;
        Move bestMove = null;

        // standard finding min algorithm used
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

    /**
     * Returns a move that is chosen using the Combinatorial Game Theory strategy,
     * of evaluating a board into a GameValue, coupled with the endgame database optimisations
     * @param board current board
     * @return selected move
     */
    public Move getCGTMove(Board board){

        final boolean isWhite = this.isWhite();
        final Move[] gameValueMove = new Move[1];

        // Creating a new thread that executes a lambda function to
        // evaluate the board object, into a GameValue
        Thread evaluateThread = new Thread(() -> {

            GameValue gameValue = board.evaluate(partitionsDB);
            gameValue.simplify();

            if(isWhite){

                gameValueMove[0] = gameValue.right.get(0).move;

            } else {

                gameValueMove[0] = gameValue.left.get(0).move;
            }
        });

        // Spawning a new thread, to try and evaluate board to a GameValue
        evaluateThread.start();

        try{

            // Waiting 5 seconds
            Thread.sleep(5000);

            // if other thread still evaluating, return a heuristic move
            if(evaluateThread.isAlive()){

                evaluateThread.interrupt();
                return getHeuristicMove(board);

            // otherwise, return evaluated move
            } else {

                return gameValueMove[0];
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        // in case of an exception, just return a heuristic move
        return getHeuristicMove(board);
    }

    /**
     * Returns a randomly chosen move, from the list of valid moves
     * @param moves List of valid possible moves
     * @return selected move
     */
    public Move getRandomMove(ArrayList<Move> moves){

        Random rand = new Random();
        return moves.get(rand.nextInt(moves.size()));
    }

    /**
     * Selecting what type of AI to choose a move using
     * @param board current board
     * @return selected move
     */
    @Override
    public Move getMove(Board board){

        ArrayList<Move> validMoves = super.getValidMoves(board);

        if(validMoves.isEmpty()){

            return null;

        } else {

            System.out.println("AI is thinking");

            if(AIType.equals("mcts")){

                return getMonteCarloMove(board);

            } else if(AIType.equals("heuristic")){

                return getHeuristicMove(board);

            } else if(AIType.equals("cgt")){

                return getCGTMove(board);

            } else if(AIType.equals("random")) {

                return getRandomMove(validMoves);

            } else {

                // When no AI type specified, default is random
                return getRandomMove(validMoves);
            }


            // TODO- use experiments to decide when to switch between move strategies
            // Initial plan
            // 1. Heuristic for first 5 or 10 moves, depending on board size
            // 2. Monte Carlo for middle-game
            // 3. Once board split into partitions that are small enough, use endgame move
        }
    }

}
