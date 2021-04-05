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

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
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

        ArrayList<Board> partitions = board1.split(new ArrayList<>());

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

        ArrayList<Board> partitions = board1.split(new ArrayList<>());

        assertTrue(partition1.equals(partitions.get(0)));
        assertTrue(partition2.equals(partitions.get(1)));

    }

    /**
     * Testing evaluating of a 3 square line board partition
     *  with one of each piece, which evaluates to "1"
     */
    @Test
    public void testEvaluate(){

        Board board = new Board(3, 1);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, whitePieces.get(0));

        GameValue zero = new GameValue();
        GameValue one = new GameValue();
        one.left.add(zero);


        GameValue gameValue = board.evaluate();
        assertTrue(gameValue.equals(one));
    }

    /**
     * Testing evaluating a 3x2 partition, with 1 of each piece
     */
    @Test
    public void testEvaluate2(){

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

        GameValue gameValue = board.evaluate();
        assertTrue(gameValue.equals(starMinus1));
    }

    /**
     * Testing evaluating a 3x3 board
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

        GameValue gameValue = board.evaluate();
    }

    /**
     * Testing evaluating an empty board, should evaluate to "0"
     */
    @Test
    public void testEvaluateEmpty(){

        Board board = new Board(2, 2);
        board.setupBoard();

        GameValue zero = new GameValue();

        GameValue gameValue = board.evaluate();

        assertTrue(gameValue.equals(zero));
    }

    /**
     * Testing evaluating an full board, should evaluate to "0"
     */
    @Test
    public void testEvaluateFull(){

        Board board = new Board(2, 2);
        board.setupBoard();

        board.getSquare(0,0).burnSquare();
        board.getSquare(1,0).burnSquare();
        board.getSquare(1, 1).burnSquare();

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(1, 0));
        board.addPiece(1, 0, whitePiece);

        GameValue zero = new GameValue();

        GameValue gameValue = board.evaluate();

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

        GameValue gameValue = board.evaluate();
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
     * Testing that the rotatePoint() method works correctly
     */
    @Test
    public void testRotateSquare(){

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

        Square rotatedSquare = board.rotatePoint(board.getSquare(2,1));

        assertTrue(rotatedSquare.getX() == 1);
        assertTrue(rotatedSquare.getY() == 0);

        Board rotatedBoard = new Board(board.getRowBoardSize(), board.getColumnBoardSize());

        Square rotatedTwiceSquare = rotatedBoard.rotatePoint(rotatedSquare);
        assertTrue(rotatedTwiceSquare.getX() == 0);
        assertTrue(rotatedTwiceSquare.getY() == 0);
    }

    /**
     * Checking if a list of valid moves contains a specific move
     * @param move move we are looking for
     * @param validMoves list of moves to check
     * @return true if list contains move, false otherwise
     */
    public boolean containsMove(Move move, ArrayList<Move> validMoves){

        for(Move validMove: validMoves){

            if(move.toString().equals(validMove.toString())){
                return true;
            }
        }

        return false;
    }

    /**
     * Checking that all the moves for left and right stored in a game value object are valid
     * @param gameValue GameValue object to check moves of
     * @param board board that we are checking against
     * @param blackPieces black pieces on the board
     * @param whitePieces white pieces on the board
     */
    public void checkMoves(GameValue gameValue, Board board,
                           ArrayList<Piece> blackPieces, ArrayList<Piece> whitePieces){

        AIPlayer blackPlayer = new AIPlayer(false);
        AIPlayer whitePlayer = new AIPlayer(true);
        blackPlayer.addPieces(blackPieces);
        whitePlayer.addPieces(whitePieces);

        ArrayList<Move> blackValidMoves = blackPlayer.getValidMoves(board);
        ArrayList<Move> whiteValidMoves = whitePlayer.getValidMoves(board);

        for(int i = 0; i < gameValue.left.size(); i++){

            Move blackMove = gameValue.left.get(i).move;
            assertTrue(containsMove(blackMove, blackValidMoves));
        }

        for(int i = 0; i < gameValue.right.size(); i++){

            Move whiteMove = gameValue.right.get(i).move;
            assertTrue(containsMove(whiteMove, whiteValidMoves));
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);

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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);

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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
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

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);
    }

    /** Testing evaluate() in case where board can be split into partitions.
     * 4 partitions, 2 of which have pieces.
     * Board only has 1 good move for black, c1 -> b2 and shoot c1.
     *    -------------------------
     * 5 |   | X | X | B |   |   |
     *   -------------------------
     * 4 |   | X | X |   |   | W |
     *   -------------------------
     * 3 | X | X | X | X | X | X |
     *   -------------------------
     * 2 | X |   | W | X | X |   |
     *   -------------------------
     * 1 |   | X | B | X | X |   |
     *   -------------------------
     * 0 |   |   |   | X |   |   |
     *   -------------------------
     *     A   B   C   D   E   F
     */
    @Test
    public void testEvaluateWithSplit(){

        board = new Board(6, 6);
        board.setupBoard();

        // First partition w/ pieces
        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, whitePiece);

        board.burnSquare(0, 2);
        board.burnSquare(1, 1);

        // Second partition w/ pieces
        Piece blackPiece2 = new Piece(false);
        blackPiece2.setPosition(board.getSquare(3, 5));
        board.addPiece(3,5, blackPiece2);

        Piece whitePiece2 = new Piece(true);
        whitePiece2.setPosition(board.getSquare(5,4));
        board.addPiece(5, 4, whitePiece2);

        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPieces.add(blackPiece2);
        whitePieces.add(whitePiece);
        whitePieces.add(whitePiece2);

        // Filling in other parts of board
        board.burnSquare(0, 3);
        board.burnSquare(1, 3);
        board.burnSquare(1, 4);
        board.burnSquare(1, 5);
        board.burnSquare(2, 3);
        board.burnSquare(2, 4);
        board.burnSquare(2, 5);
        board.burnSquare(3, 0);
        board.burnSquare(3, 1);
        board.burnSquare(3, 2);
        board.burnSquare(3, 3);
        board.burnSquare(4, 1);
        board.burnSquare(4, 2);
        board.burnSquare(4, 3);
        board.burnSquare(5, 3);

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);

        Move blackMove = gameValue.left.get(0).move;

        assertTrue(blackMove.getStartPosition().getX() == 2);
        assertTrue(blackMove.getStartPosition().getY() == 1);

        assertTrue(blackMove.getEndPosition().getX() == 1);
        assertTrue(blackMove.getEndPosition().getY() == 2);

        assertTrue(blackMove.getBurnedSquare().getX() == 2);
        assertTrue(blackMove.getBurnedSquare().getY() == 1);
    }

    /**
     * Testing evaluate() in case where board can be split into partitions.
     * Whites move is to shoot back at the starting square, so this tests
     * that the starting and burned squares offset values are applied correctly,
     * when both the starting and burned squares refer to the same square object.
     *   -------------------------
     * 5 | X | X | X |   | X |   |
     *   -------------------------
     * 4 | X | X | B | X | X | X |
     *   -------------------------
     * 3 | X | X | X | X | W | X |
     *   -------------------------
     * 2 | X | X | X | X |   | X |
     *   -------------------------
     * 1 | W | X | B | X | X | X |
     *   -------------------------
     * 0 | X | X | X |   | X | X |
     *   -------------------------
     *     A   B   C   D   E   F
     */
    @Test
    public void testEvaluateWithSplit2(){

        board = new Board(6, 6);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(board.getSquare(2,1));
        board.addPiece(2, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, whitePiece);

        Piece blackPiece2 = new Piece(false);
        blackPiece2.setPosition(board.getSquare(2, 4));
        board.addPiece(2,4, blackPiece2);

        Piece whitePiece2 = new Piece(true);
        whitePiece2.setPosition(board.getSquare(4,3));
        board.addPiece(4, 3, whitePiece2);

        ArrayList<Piece> blackPieces = new ArrayList<>();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        blackPieces.add(blackPiece);
        blackPieces.add(blackPiece2);
        whitePieces.add(whitePiece);
        whitePieces.add(whitePiece2);

        // Filling in other parts of board
        board.burnSquare(0, 0);
        board.burnSquare(0, 2);
        board.burnSquare(0, 3);
        board.burnSquare(0, 4);
        board.burnSquare(0, 5);
        board.burnSquare(1, 0);
        board.burnSquare(1, 1);
        board.burnSquare(1, 2);
        board.burnSquare(1, 3);
        board.burnSquare(1, 4);
        board.burnSquare(1, 5);
        board.burnSquare(2, 0);
        board.burnSquare(2, 2);
        board.burnSquare(2, 3);
        board.burnSquare(2, 5);
        board.burnSquare(3, 1);
        board.burnSquare(3, 2);
        board.burnSquare(3, 3);
        board.burnSquare(3, 4);
        board.burnSquare(4, 0);
        board.burnSquare(4, 1);
        board.burnSquare(4, 4);
        board.burnSquare(4, 5);
        board.burnSquare(5, 0);
        board.burnSquare(5, 1);
        board.burnSquare(5, 2);
        board.burnSquare(5, 3);
        board.burnSquare(5, 4);

        GameValue gameValue = board.evaluate();

        checkMoves(gameValue, board, blackPieces, whitePieces);

    }

}
