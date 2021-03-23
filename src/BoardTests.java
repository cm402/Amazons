import java.util.HashMap;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTests {

    /**
     * Testing that a board is simplified correctly, with
     * removal of columns on both left and right side
     */
    @Test
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

        Board board2 = new Board(2,2);
        board2.setupBoard();

        assertFalse(board1.equals(board2));

        board2.burnSquare(1, 1);

        assertTrue(board1.equals(board2));

    }

    /**
     * Testing that a board is simplified correctly, with
     * removal of rows on both top and bottom of board
     */
    @Test
    public void testSimplifyRows(){

        Board board1 = new Board(2, 4);
        board1.setupBoard();
        board1.burnSquare(0, 0);
        board1.burnSquare(1, 0);
        board1.burnSquare(0, 3);
        board1.burnSquare(1, 3);
        board1.burnSquare(1, 2);

        Board board2 = new Board(2,2);
        board2.setupBoard();

        assertFalse(board1.equals(board2));

        board2.burnSquare(1, 1);

        assertTrue(board1.equals(board2));

    }

    /**
     * Testing that a board is simplified correctly, including
     * a piece
     */
    @Test
    public void testSimplifyWithPieces(){

        Board board1 = new Board(3, 2);
        board1.setupBoard();
        board1.burnSquare(0, 0);
        board1.burnSquare(0, 1);
        board1.getSquare(2, 1).setAmazon(new Piece(false));

        Board board2 = new Board(2,2);
        board2.setupBoard();

        assertFalse(board1.equals(board2));

        board2.getSquare(1, 1).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing a 90 degrees clockwise rotation of a board
     */
    @Test
    public void testRotate90(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();
        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);
        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        Board board2 = new Board(3, 4);
        board2.setupBoard();
        board2.burnSquare(0, 0);
        board2.burnSquare(2, 2);
        board2.getSquare(1, 1).setAmazon(new Piece(true));

        assertFalse(board1.equals(board2));

        board2.getSquare(0, 3).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing a 180 degrees clockwise rotation of a board
     */
    @Test
    public void testRotate180(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();
        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);
        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        Board board2 = new Board(4, 3);
        board2.setupBoard();
        board2.burnSquare(1, 2);
        board2.burnSquare(3, 0);
        board2.getSquare(2, 1).setAmazon(new Piece(true));

        assertFalse(board1.equals(board2));

        board2.getSquare(0, 0).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing a 270 degrees clockwise rotation of a board
     */
    @Test
    public void testRotate270(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();
        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);
        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        Board board2 = new Board(3, 4);
        board2.setupBoard();
        board2.burnSquare(0, 1);
        board2.burnSquare(2, 3);
        board2.getSquare(1, 2).setAmazon(new Piece(true));

        assertFalse(board1.equals(board2));

        board2.getSquare(2, 0).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing horizontally flipping a board
     */
    @Test
    public void testFlipHorizontal(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();
        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);
        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        Board board2 = new Board(4, 3);
        board2.setupBoard();
        board2.burnSquare(1, 0);
        board2.burnSquare(3, 2);
        board2.getSquare(2, 1).setAmazon(new Piece(true));

        assertFalse(board1.equals(board2));

        board2.getSquare(0, 2).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing vertically flipping a board
     */
    @Test
    public void testFlipVertical(){

        Board board1 = new Board(4, 3);
        board1.setupBoard();
        board1.burnSquare(0, 2);
        board1.burnSquare(2, 0);
        board1.getSquare(1, 1).setAmazon(new Piece(true));
        board1.getSquare(3, 2).setAmazon(new Piece(false));

        Board board2 = new Board(4, 3);
        board2.setupBoard();
        board2.burnSquare(0, 0);
        board2.burnSquare(2, 2);
        board2.getSquare(1, 1).setAmazon(new Piece(true));

        assertFalse(board1.equals(board2));

        board2.getSquare(3, 0).setAmazon(new Piece(false));

        assertTrue(board1.equals(board2));
    }

    /**
     * Testing splitting a board into 4 partitions, with a burnt column and row
     */
    @Test
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

        Board partition0 = new Board(1, 1);
        partition0.setupBoard();
        partition0.getSquare(0, 0).setAmazon(new Piece(false));

        Board partition1 = new Board(1, 2);
        partition1.setupBoard();

        Board partition2 = new Board(3, 1);
        partition2.setupBoard();

        Board partition3 = new Board(3, 2);
        partition3.setupBoard();
        partition3.getSquare(1, 0).setAmazon(new Piece(true));

        ArrayList<Board> partitions = board1.split();

        assertTrue(partition0.equals(partitions.get(0)));
        assertTrue(partition1.equals(partitions.get(1)));
        assertTrue(partition2.equals(partitions.get(2)));
        assertTrue(partition3.equals(partitions.get(3)));
    }

    /**
     * Testing splitting a board into 2 partitions,
     * one internal and one the same size as the original board
     */
    @Test
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

        Board partition1 = new Board(5, 4);
        partition1.setupBoard();
        partition1.getSquare(2, 0).setAmazon(new Piece(true));

        partition1.burnSquare(1, 1);
        partition1.burnSquare(1, 2);
        partition1.burnSquare(1, 3);
        partition1.burnSquare(2, 1);
        partition1.burnSquare(2, 2);
        partition1.burnSquare(2, 3);
        partition1.burnSquare(3, 1);
        partition1.burnSquare(3, 2);
        partition1.burnSquare(3, 3);

        Board partition2 = new Board(1, 2);
        partition2.setupBoard();
        partition2.getSquare(0, 0).setAmazon(new Piece(false));

        ArrayList<Board> partitions = board1.split();

        assertTrue(partition1.equals(partitions.get(0)));
        assertTrue(partition2.equals(partitions.get(1)));

    }

    /**
     * Testing evaluating of a 4 square long line board partition
     *  with one of each piece, which evaluates to "-1*"
     */
    @Test
    public void testEvalutate(){

        Board board = new Board(4, 1);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(2,0));
        board.addPiece(2, 0, whitePieces.get(0));

        GameValue zero = new GameValue();
        GameValue minusOne = new GameValue();
        minusOne.right.add(zero);
        GameValue minusOneStar = new GameValue();
        minusOneStar.left.add(minusOne);
        minusOneStar.right.add(minusOne);

        GameValue gameValue = board.evaluate(null);
        assertTrue(gameValue.equals(minusOneStar));
    }

    // testing evaluating of a standard board partition, shape, 3 by 2 box with 1 of each piece

    /**
     * Testing evaluating a 3x2 partition, with 1 of each piece
     */
    @Test
    public void testEvalutate2(){

        Board board = new Board(3, 2);
        board.setupBoard();
        board.burnSquare(2, 1);
        board.burnSquare(0, 0);

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        GameValue zero = new GameValue();
        GameValue star = new GameValue();
        star.left.add(zero);
        star.right.add(zero);
        GameValue minusOne = new GameValue();
        minusOne.right.add(zero);
        GameValue starMinus1 = new GameValue();
        starMinus1.left.add(star);
        starMinus1.right.add(minusOne);

        GameValue gameValue = board.evaluate(null);
        assertTrue(gameValue.equals(starMinus1));
    }

    // testing evaluating of a bigger board size, 3 by 3
    // Seems to now work, but will need to also test storing this in partitions DB
    @Test
    public void testEvalutate3(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue.toString());
        //gameValue.simplify();
        //System.out.println(gameValue.toString());

    }

    // tests that a board with 1 players Amazon, or no amazons return correct value
    @Test
    public void testEvalutate4(){

        Board board = new Board(3, 2);
        board.setupBoard();

        board.printBoard();

        // testing empty board, should return "0"
        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue.toString());

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        board.printBoard();

        // testing board with 5 squares left and a black piece, should return "5"
        gameValue = board.evaluate(null);
        System.out.println(gameValue.toString());
    }

    public void testOutcomeClassFuzzy(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        board1.printBoard();

        GameValue gameValue = board1.evaluate(null);
        System.out.println(gameValue.getOutcomeClass());
        // The outcome of this should be "First"

    }

    public void testOutcomeClassPositive(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        board1.printBoard();

        GameValue gameValue = board1.evaluate(null);
        System.out.println(gameValue.getOutcomeClass());
        // The outcome of this should be "Left"
    }

    public void testOutcomeClassNegative(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, whitePieces.get(0));

        board1.printBoard();

        GameValue gameValue = board1.evaluate(null);
        System.out.println(gameValue.getOutcomeClass());
        // The outcome of this should be "Right"
    }

    public void testOutcomeClassZero(){

        Board board1 = new Board(4, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        board1.printBoard();

        GameValue gameValue = board1.evaluate(null);
        System.out.println(gameValue.getOutcomeClass());
        // The outcome of this should be "Second"
    }

    public void testHashCode(){

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

        for(int i = 0; i < 5; i++){
            System.out.println(board.hashCode());
        }

        System.out.println("Changing board");
        board.getSquare(0, 1).removeAmazon();

        for(int i = 0; i < 5; i++){
            System.out.println(board.hashCode());
        }

        System.out.println("Reverting changes");
        board.addPiece(0, 1, blackPieces.get(0));

        for(int i = 0; i < 5; i++) {
            System.out.println(board.hashCode());
        }

    }

    public void testInvertBoard(){

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

        Board invertedBoard = board.invert();

        invertedBoard.printBoard();

    }

    public void testPartitionsDBSpeed(){

        Board board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        // first evaluation, will actually evaluate the board
        long start = System.currentTimeMillis();
        GameValue gameValue = board.evaluate(partitionsDB);
        long end = System.currentTimeMillis();
        long firstEvaluationTime = end - start;

        // second evaluation, will retrieve from the partitions DB
        start = System.currentTimeMillis();
        GameValue newGameValue = board.evaluate(partitionsDB);
        end = System.currentTimeMillis();
        long secondEvaluationTime = end - start;

        System.out.println("The first evalutation took " + firstEvaluationTime + " ns");
        System.out.println("The second evalutation took " + secondEvaluationTime + " ns");
    }

    public void testPartitionsDBSaved(){

        Board board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        GameValue gameValue1 = board.evaluate(partitionsDB);

        board.burnSquare(0, 0);
        board.burnSquare(1, 1);

        GameValue gameValue2 = board.evaluate(partitionsDB);
        board.printBoard();

        FileInputOutput fio = new FileInputOutput();
        fio.outputDB(partitionsDB);

        HashMap<Integer, GameValue> newPartitionsDB = fio.getPartitionsDB();

        System.out.println(newPartitionsDB);
    }

    public void testPartitionsDBLarge(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        // getting partitions Database from file
        FileInputOutput fio = new FileInputOutput();
        HashMap<Integer, GameValue> partitionsDB = fio.getPartitionsDB();

        GameValue gameValue = board.evaluate(partitionsDB);

        // storing GameValue object to file
        fio.outputDB(partitionsDB);
    }

    public void testEvaluateSplit(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, whitePieces.get(0));

        board.burnSquare(1, 0);
        board.burnSquare(1, 1);
        board.burnSquare(1, 2);

        board.printBoard();

        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue);
    }
    public void testEvaluateSplit2(){

        Board board = new Board(3, 4);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,2));
        board.addPiece(0, 2, blackPieces.get(0));
        blackPieces.add(new Piece(false));
        blackPieces.get(1).setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, whitePieces.get(0));
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, whitePieces.get(0));

        board.burnSquare(1, 0);
        board.burnSquare(1, 1);
        board.burnSquare(1, 2);
        board.burnSquare(1, 3);

        board.printBoard();

        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue);
    }

    public void testGetGameValue(){

        // Board 1
        Board board = new Board(2, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,1));
        board.addPiece(1, 1, whitePieces.get(0));

        board.burnSquare(1, 0);

        // Board 2
        Board board2 = new Board(2, 2);
        board2.setupBoard();

        ArrayList<Piece> blackPieces2 = new ArrayList<Piece>();
        blackPieces2.add(new Piece(false));
        blackPieces2.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces2.get(0));

        ArrayList<Piece> whitePieces2 = new ArrayList<Piece>();
        whitePieces2.add(new Piece(true));
        whitePieces2.get(0).setPosition(board.getSquare(1,1));
        board.addPiece(1, 1, whitePieces2.get(0));

        board2.burnSquare(1, 0);

        // getting partitions Database from file
        FileInputOutput fio = new FileInputOutput();
        HashMap<Integer, GameValue> partitionsDB = fio.getPartitionsDB();

        GameValue gameValue = board.evaluate(null);
        GameValue gameValue2 = board.evaluate(null);

        System.out.println("testing");

    }

    public void testTransformingSquares(){

        Square rotatedSquare = new Square(2, 1, null, true);


        for(int i = 0; i < 4; i++){

            Board board = new Board(3, 3);
            board.setupBoard();

            board.burnSquare(rotatedSquare.getX(), rotatedSquare.getY());
            board.printBoard();

            rotatedSquare = board.rotatePoint(rotatedSquare);

        }

    }

    // testing that we correctly transform our GameValue objects
    public void testTransformingGameValues(){

        Board board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        GameValue gameValue = board.evaluate(null);
        System.out.println(gameValue.toString());

    }

}
