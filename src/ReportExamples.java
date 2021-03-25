import java.util.ArrayList;

/**
 * Used to print examples of aspects of the program
 * working correctly for the implementation section
 * of the report.
 */
public class ReportExamples {

    /**
     * Used to show the printing of a 6 x 6 board
     */
    public void boardPrintExample(){

        Board board1 = new Board(6, 6);
        board1.resetBoard(new Player(false, false), new Player(true, false));

        board1.printBoard();
    }

    /**
     * Used to show an example of recursive simplification
     * Note- Added print statements in simplify to show this working
     */
    public void boardSimplifyExample(){

        Board board1 = new Board(4, 4);
        board1.setupBoard();

        board1.burnSquare(0, 0);
        board1.burnSquare(0, 1);
        board1.burnSquare(0, 2);
        board1.burnSquare(0, 3);
        board1.burnSquare(1, 0);
        board1.burnSquare(2, 0);
        board1.burnSquare(3, 0);

        board1.getSquare(2, 3).setAmazon(new Piece(true));
        board1.getSquare(3,2).setAmazon(new Piece(false));

        board1.simplify();

    }

    /**
     * Used to show equality of 2 boards
     */
    public void boardEqualsExample(){

        Board board1 = new Board(3, 4);
        board1.setupBoard();

        board1.burnSquare(0, 0);
        board1.burnSquare(1, 0);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 3).setAmazon(new Piece(true));
        board1.getSquare(2,2).setAmazon(new Piece(false));

        Board board2 = new Board(3, 3);
        board2.setupBoard();

        board2.getSquare(0, 1).setAmazon(new Piece(true));
        board2.getSquare(1,2).setAmazon(new Piece(false));

        board1.printBoard();
        board2.printBoard();

        System.out.println("board1.equals(board2) returns " + board1.equals(board2));
    }

    /**
     * Shows a board being split into partitions
     */
    public void boardSplitExample(){

        Board board1 = new Board(5, 4);
        board1.setupBoard();

        board1.burnSquare(0, 1);
        board1.burnSquare(1, 0);
        board1.burnSquare(1, 1);
        board1.burnSquare(1, 2);
        board1.burnSquare(1, 3);
        board1.burnSquare(2, 1);
        board1.burnSquare(3, 1);
        board1.burnSquare(4, 1);

        board1.getSquare(3, 2).setAmazon(new Piece(true));
        board1.getSquare(0, 0).setAmazon(new Piece(false));

        board1.printBoard();

        Board partition1 = new Board(1, 1);
        partition1.setupBoard();
        partition1.getSquare(0, 0).setAmazon(new Piece(false));

        Board partition2 = new Board(3, 2);
        partition2.setupBoard();
        partition2.getSquare(1, 0).setAmazon(new Piece(true));

        ArrayList<Board> partitions = board1.split();

        for(Board partition: partitions){
            partition.printBoard();
        }

        System.out.println(partition1.equals(partitions.get(0)));
        System.out.println(partition2.equals(partitions.get(3)));

    }

    /**
     * Shows a 3 by 2 board being evaluated into a game value
     */
    public void boardEvaluateExample(){

        Board board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.burnSquare(2,1);
        board.burnSquare(0,1);

        board.printBoard();

        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue.toString());

    }

    /**
     * Shows a board and the smallest hash value associated, for endgame database diagram
     */
    public void endgameDatabaseExample(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(1,1));
        board.addPiece(1, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(2,0));
        board.addPiece(2, 0, whitePieces.get(0));

        board.burnSquare(2,2);

        board.printBoard();

        Board.SmallestHashValue smallestHashValue = board.getSmallestHashValue();

        smallestHashValue.board.printBoard();

        System.out.println("smallest hash value = " + smallestHashValue.hashValue);
        System.out.println("transformation used = " + smallestHashValue.transformation);

        Board board2 = new Board(3, 3);
        board2.setupBoard();

        ArrayList<Piece> blackPieces2 = new ArrayList<Piece>();
        blackPieces2.add(new Piece(false));
        blackPieces2.get(0).setPosition(board2.getSquare(0,0));
        board2.addPiece(0, 0, blackPieces2.get(0));

        ArrayList<Piece> whitePieces2 = new ArrayList<Piece>();
        whitePieces2.add(new Piece(true));
        whitePieces2.get(0).setPosition(board2.getSquare(1,1));
        board2.addPiece(1, 1, whitePieces2.get(0));

        board2.burnSquare(2,0);

        board2.printBoard();
    }

    /**
     * Shows a 3 by 2 board being used to generate a hashcode
     */
    public void boardHashCodeExample(){

        Board board = new Board(3, 2);
        board.setupBoard();

        // adding the piece to the board correctly so that we can look at valid moves
        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        System.out.println(board.hashCode());

    }

    public void boardTransformationsExamples(){

        Board board = new Board(3, 3);
        board.setupBoard();

        // adding the piece to the board correctly so that we can look at valid moves
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        Board rotatedBoard = board.rotate().rotate().rotate();

        rotatedBoard.printBoard();
    }
}
