import java.lang.reflect.Array;
import java.util.ArrayList;

public class BoardTests {

    GameEngine engine = new GameEngine();

    // testing that a board is simplified correctly, with columns on left and right side
    public void testSimplifyColumns(){

        Board board1 = new Board(5, 2);
        board1.setupBoard();

        board1.burnSquare(0, 0);
        board1.burnSquare(0, 1);
        board1.burnSquare(1, 0);
        board1.burnSquare(1, 1);
        board1.burnSquare(3, 1);
        board1.burnSquare(4, 0);
        board1.burnSquare(4, 1);

        board1.printBoard();

        Board board2 = new Board(2,2);
        board2.setupBoard();

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as square isn't burned yet

        board2.burnSquare(1, 1);

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal

    }

    // testing that a board is simplified correctly, with columns on left and right side
    public void testSimplifyRows(){

        Board board1 = new Board(2, 4);
        board1.setupBoard();

        board1.burnSquare(0, 0);
        board1.burnSquare(1, 0);
        board1.burnSquare(0, 3);
        board1.burnSquare(1, 3);
        board1.burnSquare(1, 2);

        board1.printBoard();

        Board board2 = new Board(2,2);
        board2.setupBoard();

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as square isn't burned yet

        board2.burnSquare(1, 1);

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal

    }

    public void testSimplifyWithPieces(){


        Board board1 = new Board(3, 2);
        board1.setupBoard();

        board1.burnSquare(0, 0);
        board1.burnSquare(0, 1);

        board1.getSquare(2, 1).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(2,2);
        board2.setupBoard();

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as square isn't burned yet


        board2.getSquare(1, 1).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal

    }

    public void testRotate90(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();

        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(3, 4);
        board2.setupBoard();

        board2.burnSquare(0, 0);
        board2.burnSquare(2, 2);

        board2.getSquare(1, 1).setAmazon(new Piece(true));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as the boards are not equal yet

        board2.getSquare(0, 3).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal
    }

    public void testRotate180(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();

        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(4, 3);
        board2.setupBoard();

        board2.burnSquare(1, 2);
        board2.burnSquare(3, 0);

        board2.getSquare(2, 1).setAmazon(new Piece(true));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as the boards not equal

        board2.getSquare(0, 0).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards now equal
    }

    public void testRotate270(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();

        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(3, 4);
        board2.setupBoard();

        board2.burnSquare(0, 1);
        board2.burnSquare(2, 3);

        board2.getSquare(1, 2).setAmazon(new Piece(true));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns false, as the boards are not equal yet

        board2.getSquare(2, 0).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal
    }

    public void testFlipHorizontal(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();

        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(4, 3);
        board2.setupBoard();

        board2.burnSquare(1, 0);
        board2.burnSquare(3, 2);

        board2.getSquare(2, 1).setAmazon(new Piece(true));

        board2.printBoard();
        System.out.println(board1.equals(board2)); // returns false, as the boards are not equal yet

        board2.getSquare(0, 2).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal
    }

    public void testFlipVertical(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();

        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);

        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board board2 = new Board(4, 3);
        board2.setupBoard();

        board2.burnSquare(0, 0);
        board2.burnSquare(2, 2);

        board2.getSquare(1, 1).setAmazon(new Piece(true));

        board2.printBoard();
        System.out.println(board1.equals(board2)); // returns false, as the boards are not equal yet

        board2.getSquare(3, 0).setAmazon(new Piece(false));

        board2.printBoard();

        System.out.println(board1.equals(board2)); // returns true, as the boards are now equal
    }

    public void testSplit1(){

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

    public void testSplit2(){

        Board board1 = new Board(5, 4);
        board1.setupBoard();

        board1.burnSquare(1, 1);
        board1.burnSquare(1, 2);
        board1.burnSquare(1, 3);
        board1.burnSquare(2, 1);
        board1.burnSquare(3, 1);
        board1.burnSquare(3, 2);
        board1.burnSquare(3, 3);

        board1.getSquare(2, 0).setAmazon(new Piece(true));
        board1.getSquare(2, 2).setAmazon(new Piece(false));

        board1.printBoard();

        Board partition1 = new Board(1, 2);
        partition1.setupBoard();
        partition1.getSquare(0, 0).setAmazon(new Piece(false));

        Board partition2 = new Board(5, 4);
        partition2.setupBoard();
        partition2.getSquare(2, 0).setAmazon(new Piece(true));

        partition2.burnSquare(1, 1);
        partition2.burnSquare(1, 2);
        partition2.burnSquare(1, 3);
        partition2.burnSquare(2, 1);
        partition2.burnSquare(2, 2);
        partition2.burnSquare(2, 3);
        partition2.burnSquare(3, 1);
        partition2.burnSquare(3, 2);
        partition2.burnSquare(3, 3);

        partition1.printBoard();
        partition2.printBoard();

        ArrayList<Board> partitions = board1.split();


        for(Board partition: partitions){
            partition.printBoard();
        }

        System.out.println(partition1.equals(partitions.get(1)));
        System.out.println(partition2.equals(partitions.get(0)));

    }

    public void testEvalutate(){

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

        // create an AI player who uses black pieces, to see blacks valid moves
        AIPlayer player = new AIPlayer(false);
        player.addPieces(blackPieces);

        for(Move move: player.getValidMoves(board)){

            Board newBoard = board.newBoard(board, 0, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1, -1);

            Piece piece = move.getPiece();
            int x = move.getEndPosition().getX();
            int y = move.getEndPosition().getY();

            // moving piece to new square and burning square that is shot at
            newBoard.setSquarePiece(x, y, piece);
            newBoard.burnSquare(move.getBurnedSquare().getX(), move.getBurnedSquare().getY());

            // removing amazon from original square
            if(newBoard.getSquare(move.getStartPosition().getX(), move.getStartPosition().getY()).getAmazon() != null){
                newBoard.getSquare(move.getStartPosition().getX(), move.getStartPosition().getY()).removeAmazon();
            }

            newBoard.printBoard();

            System.out.println(move.toString());
        }



    }



}
