import java.util.ArrayList;
import java.util.*;

public class MCTS {

    boolean opponent;

    //https://www.baeldung.com/java-monte-carlo-tree-search

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
            Board promisingNodeBoard = promisingNode.state.board;

            Player player = new Player(nextPlayer, true);
            ArrayList<Piece> pieces = promisingNodeBoard.getPieces(nextPlayer);
            player.addPieces(pieces);

            ArrayList<Move> validMoves = player.getValidMoves(promisingNodeBoard);

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
            backPropogation(promisingNode, playoutResult);
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

    // Returns a UCT (Upper Confidence Bound), using the formula
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {

        // used to ensure that nodes that haven't been visited yet get priority
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return ((double) nodeWinScore / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    // Returns the best node to use, using the UCT
    public static Node findBestNodeWithUCT(Node node) {

        int parentVisit = node.state.visitCount;

        return Collections.max(node.children,
                Comparator.comparing(c -> uctValue(parentVisit, c.state.winScore, c.state.visitCount)));
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
    // returns boolean value for whoever won
    private boolean simulateRandomPlayout(Node node){

        GameEngine gameEngine = new GameEngine();
        Board board = node.state.board;
        boolean nextPlayer = node.state.nextPlayer;

        // creating player objects and passing them their pieces
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new AIPlayer(nextPlayer));
        players.add(new AIPlayer(!nextPlayer));
        players.get(0).addPieces(board.getPieces(nextPlayer));
        players.get(1).addPieces(board.getPieces(!nextPlayer));
        Player currentPlayer = players.get(0);

        // if game is over (current player has no valid moves)
        // and the opponent has won
        if(players.get(0).getValidMoves(board).size() == 0
            && nextPlayer != opponent){

            node.parent.state.winScore = Integer.MIN_VALUE;
            return !nextPlayer;
        }

        // simulate the game till it finishes, playing random moves
        while(true){

            ArrayList<Move> validMoves = currentPlayer.getValidMoves(board);
            Random rand = new Random();

            if(validMoves.size() == 0){

                return currentPlayer.isWhite();
            }

            Move nextMove = validMoves.get(rand.nextInt(validMoves.size()));

            gameEngine.updateBoard(nextMove, board, false);
            currentPlayer = gameEngine.swapPlayers(players, currentPlayer);
        }

    }

    // Step 4- Back-Propagation
    private void backPropogation(Node node, boolean player){

        Node tempNode = node;

        while(tempNode != null){

            tempNode.state.visitCount++;

            // if the current nodes player is whoever won the game at the
            // bottom of the tree, add 1 to their score at the current node
            if(tempNode.state.nextPlayer == player){

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
