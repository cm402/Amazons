import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests used throughout development of the
 * board class, allowing for testing of its methods.
 */
public class BoardTests {

    public static Board board;

    /**
     * Example Board that is used in the some of the tests
     */
    @BeforeClass
    public static void setup(){

        board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

    }

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
     * Testing horizontally flipping a board works for equality checking
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
     * Testing vertically flipping a board works for equality checking
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

    /**
     * Testing evaluating a 3x3 board, takes roughly 20 seconds, no asserts used
     */
    @Test
    public void testEvaluateLarger(){

        Board board = new Board(3, 3);
        board.setupBoard();
        board.burnSquare(2,2);

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        GameValue gameValue = board.evaluate(null);

    }

    /**
     * Testing evaluating an empty board, should evaluate to "0"
     */
    @Test
    public void testEvaluateEmpty(){

        Board board = new Board(3, 2);
        board.setupBoard();

        GameValue zero = new GameValue();

        GameValue gameValue = board.evaluate(null);

        // testing empty board, should return "0"
        assertTrue(gameValue.equals(zero));
    }

    /**
     *  Returns a GameValue object, for a specified integer
     */
    public GameValue getValue(int value){

        GameValue newGameValue = new GameValue();
        GameValue oldGameValue = new GameValue();

        while(value != 0){

            newGameValue = new GameValue();

            if(value > 0){
                newGameValue.left.add(oldGameValue);
                value--;
            } else {
                newGameValue.right.add(oldGameValue);
                value++;
            }

            oldGameValue = newGameValue;
        }

        return newGameValue;
    }

    /**
     *  Testing evaluating a board with only one piece, should evaluate to
     *  the number of empty squares
     */
    @Test
    public void testEvalutateOnePiece() {

        Board board = new Board(3, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0, 1));
        board.addPiece(0, 1, blackPieces.get(0));

        GameValue gameValue = board.evaluate(null);
        GameValue five = getValue(5);
        assertTrue(gameValue.equals(five));
    }

    /**
     * Testing that a board hashcode returns a consistent value
     */
    @Test
    public void testHashCode(){

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

        int hashCode = board.hashCode();

        for(int i = 0; i < 5; i++){
            assertEquals(hashCode, board.hashCode());
        }

        board.getSquare(0, 1).removeAmazon();
        assertNotEquals(hashCode, board.hashCode());
        board.addPiece(0, 1, blackPieces.get(0));

        for(int i = 0; i < 5; i++) {
            assertEquals(hashCode, board.hashCode());
        }
    }

    /**
     * Testing a board is inverted correctly, including the piece's stored positions
     */
    @Test
    public void testInvertBoard(){

        Board board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(board.getSquare(0, 1));
        board.addPiece(0, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(1, 0));
        board.addPiece(1, 0, whitePiece);

        Board board2 = new Board(3, 2);
        board2.setupBoard();

        Piece blackPiece2 = new Piece(false);
        blackPiece2.setPosition(board2.getSquare(1, 0));
        board2.addPiece(1, 0, blackPiece2);

        Piece whitePiece2 = new Piece(true);
        whitePiece2.setPosition(board2.getSquare(0, 1));
        board2.addPiece(0, 1, whitePiece2);

        Board invertedBoard = board.invert();
        assertEquals(board2, invertedBoard);

        assertEquals(whitePiece.getPosition().getX(), blackPiece2.getPosition().getX());
        assertEquals(whitePiece.getPosition().getY(), blackPiece2.getPosition().getY());
        assertEquals(blackPiece.getPosition().getX(), whitePiece2.getPosition().getX());
        assertEquals(blackPiece.getPosition().getY(), whitePiece2.getPosition().getY());
    }

    /**
     * Testing the speed increase shown by using the Endgame Database optimisation
     */
    @Test
    public void testPartitionsDBSpeed(){

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        // first evaluation, will actually evaluate the board
        long start = System.currentTimeMillis();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        long end = System.currentTimeMillis();
        long firstEvaluationTime = end - start;

        // second evaluation, will retrieve from the partitions DB
        start = System.currentTimeMillis();
        GameValue retrievedGameValue = board.evaluate(partitionsDB);
        end = System.currentTimeMillis();
        long secondEvaluationTime = end - start;

        assertTrue(evaluatedGameValue.equals(retrievedGameValue));
        assertTrue(secondEvaluationTime < firstEvaluationTime);
    }

    /**
     * Testing storing and retrieving the Endgame Database from file works correctly.
     */
    @Test
    public void testPartitionsDBSaved(){

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        // first evaluation, will actually evaluate the board
        long start = System.currentTimeMillis();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        long end = System.currentTimeMillis();
        long firstEvaluationTime = end - start;

        // Storing in the endgame database
        FileInputOutput fio = new FileInputOutput();
        fio.outputDB(partitionsDB);

        // Retrieving the endgame database, from file
        HashMap<Integer, GameValue> retrievedPartitionsDB = fio.getPartitionsDB();

        // second evaluation, will retrieve from the partitions DB
        start = System.currentTimeMillis();
        GameValue retrievedGameValue = board.evaluate(retrievedPartitionsDB);
        end = System.currentTimeMillis();
        long secondEvaluationTime = end - start;

        assertTrue(evaluatedGameValue.equals(retrievedGameValue));
        assertTrue(secondEvaluationTime < firstEvaluationTime);
    }

    /**
     * Checking if a list of valid moves contains a specific move
     * @param move move we are looking for
     * @param validMoves list of moves to check
     * @return true if list contains move, false otherwise
     */
    public boolean containsMove(Move move, ArrayList<Move> validMoves){

        System.out.println("move = " + move.toString());
        for(Move validMove: validMoves){

            System.out.println("valid move = " + validMove.toString());
            if(move.toString().equals(validMove.toString())){
                return true;
            }
        }

        return false;
    }

    /**
     * Checking that both the evaluated and retrieved moves are valid, given the board
     * @param evaluatedGameValue The GameValue object evaluated at run-time
     * @param retrievedGameValue The transformed GameValue object retrieved from the Endgame Database
     */
    public void checkMoves(GameValue evaluatedGameValue, GameValue retrievedGameValue, Board board,
                           ArrayList<Piece> blackPieces, ArrayList<Piece> whitePieces){

        AIPlayer blackPlayer = new AIPlayer(false);
        AIPlayer whitePlayer = new AIPlayer(true);
        blackPlayer.addPieces(blackPieces);
        whitePlayer.addPieces(whitePieces);

        ArrayList<Move> blackValidMoves = blackPlayer.getValidMoves(board);
        ArrayList<Move> whiteValidMoves = whitePlayer.getValidMoves(board);

        for(int i = 0; i < evaluatedGameValue.left.size(); i++){

            Move evaluatedBlackMove = evaluatedGameValue.left.get(i).move;
            Move retrievedBlackMove = retrievedGameValue.left.get(i).move;
            assertTrue(containsMove(evaluatedBlackMove, blackValidMoves));
            assertTrue(containsMove(retrievedBlackMove, blackValidMoves));
        }

        for(int i = 0; i < evaluatedGameValue.right.size(); i++){

            Move evaluatedWhiteMove = evaluatedGameValue.right.get(i).move;
            Move retrievedWhiteMove = retrievedGameValue.right.get(i).move;
            assertTrue(containsMove(evaluatedWhiteMove, whiteValidMoves));
            assertTrue(containsMove(retrievedWhiteMove, whiteValidMoves));
        }
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 0, no transformation
     *   -------------            -------------
     * 1 |   | W | B |          1 |   | W | B |
     *   -------------            -------------
     * 0 | X |   | X |          0 | X |   | X |
     *   -------------            -------------
     *     A   B   C                A   B   C
     *  Original board         Smallest Hash Board
     */
    @Test
    public void testMovesTransformed0(){

        board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(1,1));
        board.addPiece(1, 1, whitePiece);

        board.burnSquare(0, 0);
        board.burnSquare(2, 0);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 1, vertical flip
     *   -------------            -------------
     * 1 |   |   |   |          1 |   |   |   |
     *   -------------            -------------
     * 0 | B |   | W |          0 | W |   | B |
     *   -------------            -------------
     *     A   B   C                A   B   C
     *  Original board         Smallest Hash Board
     */
    @Test
    public void testMovesTransformed1(){

        board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(2,0));
        board.addPiece(2, 0, whitePiece);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 2, horizontal flip
     *   -------------            -------------
     * 1 |   |   |   |          1 | B | W |   |
     *   -------------            -------------
     * 0 | B | W |   |          0 |   |   |   |
     *   -------------            -------------
     *     A   B   C                A   B   C
     *  Original board         Smallest Hash Board
     */
    @Test
    public void testMovesTransformed2(){

        board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePiece);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 3, rotate 180 degrees
     *   -------------            -------------
     * 1 |   |   | W |          1 |   |   | B |
     *   -------------            -------------
     * 0 | B |   |   |          0 | W |   |   |
     *   -------------            -------------
     *     A   B   C                A   B   C
     *  Original board         Smallest Hash Board
     */
    @Test
    public void testMovesTransformed3(){

        board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, whitePiece);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 4, rotate 90 degrees anti-clockwise
     *   -------------        -------------
     * 2 | X | B | W |      2 | W | X |   |
     *   -------------        -------------
     * 1 |   |   | X |      1 | B |   |   |
     *   -------------        -------------
     * 0 |   |   |   |      0 | X |   |   |
     *   -------------        -------------
     *     A   B   C
     *  Original board     Smallest Hash Board
     */
    @Test
    public void testMovesTransformed4(){

        board = new Board(3, 3);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(1,2));
        board.addPiece(1, 2, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, whitePiece);

        board.burnSquare(0, 2);
        board.burnSquare(2, 1);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);

    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 5, rotate 90 degrees clockwise
     *   -------------        -------------
     * 2 | X | X | W |      2 |   |   | X |
     *   -------------        -------------
     * 1 |   |   | B |      1 |   |   | X |
     *   -------------        -------------
     * 0 |   |   |   |      0 |   | B | W |
     *   -------------        -------------
     *     A   B   C            A   B   C
     *  Original board     Smallest Hash Board
     */
    @Test
    public void testMovesTransformed5(){

        board = new Board(3, 3);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, whitePiece);

        board.burnSquare(0, 2);
        board.burnSquare(1, 2);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);

    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 6, rotate 90 degrees anti-clockwise, then vertical flip
     *   -------------        -------------
     * 2 | X |   | B |      2 | X |   |   |
     *   -------------        -------------
     * 1 |   | X | W |      1 |   | X |   |
     *   -------------        -------------
     * 0 |   |   |   |      0 | B | W |   |
     *   -------------        -------------
     *     A   B   C            A   B   C
     *  Original board     Smallest Hash Board
     */
    @Test
    public void testMovesTransformed6(){

        board = new Board(3, 3);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, whitePiece);

        board.burnSquare(0, 2);
        board.burnSquare(1, 1);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);
    }

    /**
     * Testing that moves are transformed to the smallest hash version,
     * and correctly back after being retrieved from the endgame database.
     * Transformation = 7, rotate 90 degrees clockwise, then vertical flip
     *   -------------        -------------
     * 2 | X | W | B |      2 |   |   | B |
     *   -------------        -------------
     * 1 |   | X |   |      1 |   | X | W |
     *   -------------        -------------
     * 0 |   |   |   |      0 |   |   | X |
     *   -------------        -------------
     *     A   B   C            A   B   C
     *  Original board     Smallest Hash Board
     */
    @Test
    public void testMovesTransformed7(){

        board = new Board(3, 3);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPiece.setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, blackPiece);

        Piece whitePiece = new Piece(true);
        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);
        whitePiece.setPosition(board.getSquare(1,2));
        board.addPiece(1, 2, whitePiece);

        board.burnSquare(0, 2);
        board.burnSquare(1, 1);

        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();
        GameValue evaluatedGameValue = board.evaluate(partitionsDB);
        GameValue retrievedGameValue = board.evaluate(partitionsDB);

        checkMoves(evaluatedGameValue, retrievedGameValue, board, blackPieces, whitePieces);

    }

    /** Testing evaluate when board can be split into partitions
     *   -----------------
     * 3 | X | X | X | W |
     *   -----------------
     * 2 | W |   | X |   |
     *   -----------------
     * 1 | B |   | X | B |
     *   -----------------
     * 0 |   |   | X |   |
     *   -----------------
     *     A   B   C   D
     */
    @Test
    public void testEvaluateWithSplit(){

        board = new Board(4, 4);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(0,2));
        board.addPiece(0, 2, whitePiece);

        Piece blackPiece2 = new Piece(false);
        blackPiece2.setPosition(board.getSquare(3, 1));
        board.addPiece(3,1, blackPiece2);

        Piece whitePiece2 = new Piece(true);
        whitePiece2.setPosition(board.getSquare(3,3));
        board.addPiece(3, 3, whitePiece2);

        board.burnSquare(2, 0);
        board.burnSquare(2, 1);
        board.burnSquare(2, 2);
        board.burnSquare(0, 3);
        board.burnSquare(1, 3);
        board.burnSquare(2, 3);

        GameValue gameValue = board.evaluate(null);
        System.out.println("test");
    }

}
