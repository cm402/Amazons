import java.util.ArrayList;
import java.util.*;

public class MCTS {

    //https://www.baeldung.com/java-monte-carlo-tree-search

    public Move getNextMove(Board board, boolean nextPlayer, int moveTime){

        Tree tree = new Tree();
        Node root = tree.root;
        root.state.board = board;
        root.state.nextPlayer = nextPlayer;

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

            ArrayList<Piece> pieces = board.getPieces(nextPlayer);
            player.addPieces(pieces);

            ArrayList<Move> validMoves = player.getValidMoves(board);

            // if game isn't finished, we aren't at a leaf node, so expand
            if(validMoves.size() != 0){

                expandNode(promisingNode);
            }

            Node nodeToExplore = promisingNode;

            if(promisingNode.children.size() > 0){

                Random random = new Random();
                int randomIndex = random.nextInt(promisingNode.children.size());

                nodeToExplore = promisingNode.children.get(randomIndex);
            }

            boolean playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropogation(nodeToExplore, playoutResult);
        }

        Node winnerNode = root.getChildWithMaxScore();
        tree.root = winnerNode;

        return winnerNode.state.move;
    }

    private void backPropogation(Node nodeToExplore, boolean player){

        Node tempNode = nodeToExplore;

        while(tempNode != null){

            tempNode.state.visitCount++;

            if(tempNode.state.nextPlayer == player){

                tempNode.state.winScore += 1;
            }
            tempNode = tempNode.parent;
        }
    }

    // simulates the current node,
    private boolean simulateRandomPlayout(Node node){

        GameEngine gameEngine = new GameEngine();
        Board board = node.state.board;
        boolean nextPlayer = node.state.nextPlayer;

        // if board status indicates opponent has won, set parents
        // winscore to min integer and return, otherwise...

        // creating player objects and passing them their pieces
        ArrayList<Player> players = new ArrayList<Player>();

        players.add(new AIPlayer(nextPlayer));
        players.add(new AIPlayer(!nextPlayer));
        ArrayList<Piece> player1Pieces = board.getPieces(nextPlayer);
        ArrayList<Piece> player2Pieces = board.getPieces(!nextPlayer);
        players.get(0).addPieces(player1Pieces);
        players.get(1).addPieces(player2Pieces);

        Player currentPlayer = players.get(0);

        boolean result;

        while(true){

            ArrayList<Move> validMoves = currentPlayer.getValidMoves(board);
            Random rand = new Random();

            if(validMoves.size() == 0){
                result = currentPlayer.isWhite();
                break;
            }

            Move nextMove = validMoves.get(rand.nextInt(validMoves.size()));

            gameEngine.updateBoard(nextMove, board, false);
            currentPlayer = gameEngine.swapPlayers(players, currentPlayer);
        }

        return result;
    }

    private Node selectPromisingNode(Node rootNode){

        Node node = rootNode;
        while(node.children.size() != 0){

            node = findBestNodeWithUCT(node);
        }

        return node;
    }

    // Returns a UCT (Upper Confidence Bound), using the formula
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {

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

    private class Tree{
        Node root;

        public Tree(){

            root = new Node();
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

        // gets a list of all possible positions on the board and plays random move
        public void randomPlay(){


        }

    }
}
