import java.util.ArrayList;
import java.util.*;

public class MCTS {

    boolean opponent;

    // sources

    // https://www.baeldung.com/java-monte-carlo-tree-search
    // https://medium.com/@quasimik/monte-carlo-tree-search-applied-to-letterpress-34f41c86e238

    public Move getNextMove(Board board, boolean nextPlayer, int moveTime){

        Node root = new Node();
        root.state.board = board;
        root.state.nextPlayer = nextPlayer;

        // storing the opponents colour
        opponent = !nextPlayer;

        // setting a maximum time to search for a good move
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (moveTime * 1000);

        while(System.currentTimeMillis() < endTime){

            // 1. select "promising" node using UCT
            // 2. if board doesn't have a winner, expand it
            // 3. Either "playout" the current node, or if it has children "playout" one of them
            // 4. Propagate the result back up the tree

            Node promisingNode = selectPromisingNode(root);

            Player player = new Player(nextPlayer, true);
            ArrayList<Piece> pieces = promisingNode.state.board.getPieces(nextPlayer);
            player.addPieces(pieces);

            ArrayList<Move> validMoves = player.getValidMoves(promisingNode.state.board);

            // if "promising" nodes game isn't finished, we aren't at a leaf node, so expand
            if(validMoves.size() != 0){

                expandNode(promisingNode);
            }

            // if "promising" node has children nodes, randomly choose one to play-out
            if(promisingNode.children.size() > 0){

                Random random = new Random();
                int randomIndex = random.nextInt(promisingNode.children.size());

                promisingNode = promisingNode.children.get(randomIndex);
            }

            boolean playoutResult = simulateRandomPlayout(promisingNode);
            backPropogation(promisingNode, playoutResult, nextPlayer);
        }

        Node winnerNode = root.getChildWithMaxScore();

        return winnerNode.state.move;
    }

    // Step 1- Selection
    private Node selectPromisingNode(Node rootNode){

        Node node = rootNode;

        while(node.children.size() != 0){

            node = findBestNodeWithUCT(node);
        }

        return node;
    }

    // Returns the best node to use, using the UCT
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

    // Step 2- Expansion
    // expanding out a non-leaf node, adding each of its children nodes to the tree
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

    // Step 3- Simulation
    // returns boolean value for whoever won simulation game
    private boolean simulateRandomPlayout(Node node){

        GameEngine gameEngine = new GameEngine();

        // creating a duplicate Board to be used in the simulation, so that the Node board isn't affected
        Board nodeBoard = node.state.board;
        Board simulBoard = nodeBoard.newBoard(nodeBoard, 0, 0, nodeBoard.getColumnBoardSize() - 1, nodeBoard.getRowBoardSize() - 1, -1);

        boolean nextPlayer = node.state.nextPlayer;

        // creating player objects and passing them their pieces
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new AIPlayer(nextPlayer));
        players.add(new AIPlayer(!nextPlayer));
        players.get(0).addPieces(simulBoard.getPieces(nextPlayer));
        players.get(1).addPieces(simulBoard.getPieces(!nextPlayer));
        Player currentPlayer = players.get(0);

        // if game is over (current player has no valid moves)
        // and the opponent has won
        if(players.get(0).getValidMoves(simulBoard).size() == 0
            && nextPlayer != opponent){

            return !nextPlayer;
        }

        // simulate the game till it finishes, playing random moves
        while(true){

            ArrayList<Move> validMoves = currentPlayer.getValidMoves(simulBoard);
            Random rand = new Random();

            if(validMoves.size() == 0){

                return currentPlayer.isWhite();
            }

            Move nextMove = validMoves.get(rand.nextInt(validMoves.size()));

            gameEngine.updateBoard(nextMove, simulBoard, false);
            currentPlayer = gameEngine.swapPlayers(players, currentPlayer);
        }

    }

    // Step 4- Back-Propagation
    // node = the node we simulated the game from
    // winner = winner of the simulations colour
    // player = player at the root nodes colour
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

    private class Node{

        State state;
        Node parent;
        ArrayList<Node> children;

        public Node(){

            state = new State();
            parent = null;
            children = new ArrayList<Node>();
        }

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

        public double getVisitPercentage(){

            return (double) this.state.visitCount / this.parent.state.visitCount;

        }

        public Node getChildWithMostVisits(){

            Node max = children.get(0);

            for(Node child: children){

                if(child.getVisitPercentage() > max.getVisitPercentage()){
                    max = child;
                }
            }
            return max;

        }

        // Returns a UCB (Upper Confidence Bound), using the formula
        public double getUCB() {

            // used to ensure that nodes that haven't been visited yet get priority
            if (this.state.visitCount == 0) {
                return Integer.MAX_VALUE;
            }

            // don't ever want to divide by 0, so this is added
            if(this.state.winScore == 0){

                this.state.winScore += 1;
            }

            return ((double) this.state.winScore / (double) this.state.visitCount)
                    + 1.41 * Math.sqrt(Math.log(this.parent.state.visitCount) / (double) this.state.visitCount);
        }
    }

    private class State {

        Board board;
        Move move;
        boolean nextPlayer;
        int visitCount;
        double winScore;

        // returns a list of all possible states, from the current state
        public ArrayList<State> getAllPossibleStates(){

            ArrayList<State> states = new ArrayList<State>();

            Player player = new Player(nextPlayer, true);
            ArrayList<Piece> pieces = board.getPieces(nextPlayer);
            player.addPieces(pieces);

            ArrayList<Move> validMoves = player.getValidMoves(board);

            for(Move move: validMoves){

                Board newBoard = board.playMove(board, move);

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
