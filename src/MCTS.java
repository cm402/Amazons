import java.util.ArrayList;
import java.util.*;

/**
 * Monte-Carlo Tree Search algorithm implementation
 * Adapted from https://www.baeldung.com/java-monte-carlo-tree-search
 */
public class MCTS {

    boolean opponent; // stores the colour of the opponent

    /**
     * Getting a move choice, using the MCTS algorithm
     * @param board current state of the board
     * @param nextPlayer next player to play
     * @param moveTime amount of time allowed for selecting a move
     * @return selected move
     */
    public Move getNextMove(Board board, boolean nextPlayer, int moveTime){

        Node root = new Node();
        root.state.board = board;
        root.state.nextPlayer = nextPlayer;

        // storing the opponents colour
        opponent = !nextPlayer;

        // calculating the finishing time
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (moveTime * 1000);

        // looping until finishing time
        while(System.currentTimeMillis() < endTime){

            // 1. Select "promising" node using UCT
            Node promisingNode = selectPromisingNode(root);
            boolean playoutResult;

            // 2. If node not visited before, simulate game
            if (promisingNode.state.visitCount == 0) {

                playoutResult = simulateRandomPlayout(promisingNode);


            } else {

                // 3. otherwise, expand the tree and simulate a random child node
                expandNode(promisingNode);

                // if there are children nodes, randomly select one to simulate
                if(promisingNode.children.size() > 0){

                    Random random = new Random();
                    int randomIndex = random.nextInt(promisingNode.children.size());

                    promisingNode = promisingNode.children.get(randomIndex);

                }

                playoutResult = simulateRandomPlayout(promisingNode);
            }

            // 4. Finally, propagate the result back up the tree
            backPropogation(promisingNode, playoutResult, nextPlayer);
        }

        Node winnerNode = root.getChildWithMaxScore();

        return winnerNode.state.move;
    }

    /**
     * Step 1- Selecting a promising node
     * @param rootNode the root node
     * @return a "promising" node
     */
    private Node selectPromisingNode(Node rootNode){

        Node node = rootNode;

        while(node.children.size() != 0){

            node = findBestNodeWithUCT(node);
        }

        return node;
    }

    /**
     * Finding the best node to visit next, using Upper Confidence
     * Bound formula.
     * @param node
     * @return
     */
    public static Node findBestNodeWithUCT(Node node) {

        double max = 0;
        int bestNodeIndex = 0;

        for(int i = 0; i < node.children.size(); i++){

            double currentValue = node.children.get(i).getUCB();

            if(currentValue > max){

                max = currentValue;
                bestNodeIndex = i;
            }
        }
        return node.children.get(bestNodeIndex);
    }

    /**
     * Step 2- Expanding out a non-leaf node, by adding
     * each of its children nodes to the tree.
     * @param node non-leaf node to expand
     */
    private void expandNode(Node node) {

        ArrayList<State> possibleStates = node.state.getAllPossibleStates();

        for(State state: possibleStates) {

            Node newNode = new Node();
            newNode.state = state;
            newNode.parent = node;
            newNode.state.nextPlayer = !node.state.nextPlayer;
            node.children.add(newNode);
        }
    }

    /**
     * Step 3- Simulating a game, from the node given, using
     * random play, before returning the result.
     * @param node current node
     * @return winner of the simulation
     */
    private boolean simulateRandomPlayout(Node node){

        GameEngine gameEngine = new GameEngine();

        // creating a duplicate Board to be used in the simulation, so that the Node board isn't affected
        Board nodeBoard = node.state.board;
        Board simulBoard = nodeBoard.newBoard(0, 0, nodeBoard.getColumnBoardSize() - 1, nodeBoard.getRowBoardSize() - 1, -1);

        // setup of player objects
        boolean nextPlayer = node.state.nextPlayer;
        ArrayList<AIPlayer> players = new ArrayList<>();
        players.add(new AIPlayer(nextPlayer));
        players.add(new AIPlayer(!nextPlayer));
        players.get(0).addPieces(simulBoard.getPieces(nextPlayer));
        players.get(1).addPieces(simulBoard.getPieces(!nextPlayer));
        Player currentPlayer = players.get(0);

        // if game is over, return previous player to move
        if(players.get(0).getValidMoves(simulBoard).size() == 0){

            return !nextPlayer;
        }

        // simulate the game, returning winner
        while(true){

            ArrayList<Move> validMoves = currentPlayer.getValidMoves(simulBoard);

            if(validMoves.size() == 0){

                // if current player can't move, return other player
                return !currentPlayer.isWhite();
            }

            // random move choice
            Random rand = new Random();
            Move nextMove = validMoves.get(rand.nextInt(validMoves.size()));

            gameEngine.updateBoard(nextMove, simulBoard, false);

            // swap the players
            if(players.indexOf(currentPlayer) == 0){
                currentPlayer = players.get(1);
            } else {
                currentPlayer = players.get(0);
            }
        }

    }

    /**
     * Step 4- Propagating the simulation result back up
     * the tree, all the way to the root node
     * @param node simulated node
     * @param winner winner of the simulation
     * @param player root node player
     */
    private void backPropogation(Node node, boolean winner, boolean player){

        Node tempNode = node;

        while(tempNode != null){

            tempNode.state.visitCount++;

            // if the winner of the simulation is the player we
            // are finding a move for, add one to the score
            if(player == winner){
                tempNode.state.winScore += 1;
            }

            tempNode = tempNode.parent;
        }
    }

    /**
     * Represents a node in the tree, storing its associated board state,
     * as well as references to its parent and any children nodes.
     */
    private class Node{

        State state;
        Node parent;
        ArrayList<Node> children;

        public Node(){

            state = new State();
            parent = null;
            children = new ArrayList<Node>();
        }

        /**
         * Getting the child of "this" node with the most number
         * of simulation wins associated with it
         * @return child node with the most wins
         */
        public Node getChildWithMaxScore(){

            int counter = 1;
            Node maxChild = children.get(0);

            for(Node child: children){

                if(child.state.winScore > maxChild.state.winScore) {

                    maxChild = child;
                }
            }
            return maxChild;
        }

        /**
         * Getting how often this node is visited, as a percentage,
         * compared to its sibling nodes
         * @return Percentage of how often this node is visited
         */
        public double getVisitPercentage(){

            return (double) this.state.visitCount / this.parent.state.visitCount;

        }

        /**
         * Getting the child node that's visited the most.
         * Used for testing purposes.
         * @return Most visited child node
         */
        public Node getChildWithMostVisits(){

            Node max = children.get(0);

            for(Node child: children){

                if(child.getVisitPercentage() > max.getVisitPercentage()){
                    max = child;
                }
            }
            return max;
        }

        /**
         * Getting the UCB value of "this" node
         * @return UCB value for "this" node
         */
        public double getUCB() {

            /*
            // used to ensure that nodes that haven't been visited yet get priority
            if (this.state.visitCount == 0) {
                return Integer.MAX_VALUE;
            }
            */

            // don't ever want to divide by 0, so this is added
            if(this.state.winScore == 0){
                this.state.winScore += 1;
            }

            return ((double) this.state.winScore / (double) this.state.visitCount)
                    + 0.8 * Math.sqrt(Math.log(this.parent.state.visitCount) / (double) this.state.visitCount);
        }
    }

    /**
     * Represents a board state, containing information
     * about the current board, the next player to move,
     * how often it has been visited, how many simulated wins etc.
     */
    private class State {

        Board board;
        Move move;
        boolean nextPlayer;
        int visitCount;
        double winScore;

        /**
         * Getting a list of all possible board states, from the current state
         * @return
         */
        public ArrayList<State> getAllPossibleStates(){

            ArrayList<State> states = new ArrayList<State>();

            Player player = new Player(nextPlayer, true);
            ArrayList<Piece> pieces = board.getPieces(nextPlayer);
            player.addPieces(pieces);

            ArrayList<Move> validMoves = player.getValidMoves(board);

            for(Move move: validMoves){

                Board newBoard = board.playMove(move);

                State newState = new State();
                newState.move = move;
                newState.board = newBoard;
                newState.nextPlayer = !nextPlayer;

                states.add(newState);
            }

            return states;
        }
    }
}
