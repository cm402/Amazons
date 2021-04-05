import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Unit tests used throughout development of the
 * AI class, for testing of each strategy.
 */
public class AITests {

    public Board board;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;

    /**
     * Example Board to be used in the tests
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
    @Before
    public void setup(){

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

        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
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
    }

    /**
     * Checking if a list of valid moves contains the specified move
     * @param move move we are looking for
     * @return true if list contains move, false otherwise
     */
    public boolean isMoveValid(Move move, boolean isWhite){

        ArrayList<Move> validMoves;

        // Getting a list of valid move, depending on player colour
        if(isWhite){

            AIPlayer whitePlayer = new AIPlayer(true);
            whitePlayer.addPieces(whitePieces);
            validMoves = whitePlayer.getValidMoves(board);

        } else {

            AIPlayer blackPlayer = new AIPlayer(false);
            blackPlayer.addPieces(blackPieces);
            validMoves = blackPlayer.getValidMoves(board);
        }

        for(Move validMove: validMoves){

            if(move.toString().equals(validMove.toString())){
                return true;
            }
        }

        return false;
    }

    /**
     * Testing that the Heuristic AI returns one of the
     * best moves.
     */
    @Test
    public void testMonteCarloMove(){

        AIPlayer blackPlayer = new AIPlayer(false);
        blackPlayer.setAIType("mcts");
        blackPlayer.addPieces(blackPieces);
        Move blackMove = blackPlayer.getMove(board);

        AIPlayer whitePlayer = new AIPlayer(true);
        whitePlayer.setAIType("mcts");
        whitePlayer.addPieces(whitePieces);
        Move whiteMove = whitePlayer.getMove(board);

        assertTrue(isMoveValid(blackMove, false));
        assertTrue(isMoveValid(whiteMove, true));

    }

    /**
     * Testing that the Heuristic AI returns a move that
     * limits the opponents moves.
     */
    @Test
    public void testHeuristicMove(){

        AIPlayer blackPlayer = new AIPlayer(false);
        blackPlayer.setAIType("heuristic");
        blackPlayer.addPieces(blackPieces);
        Move blackMove = blackPlayer.getMove(board);

        AIPlayer whitePlayer = new AIPlayer(true);
        whitePlayer.setAIType("heuristic");
        whitePlayer.addPieces(whitePieces);
        Move whiteMove = whitePlayer.getMove(board);

        assertTrue(isMoveValid(blackMove, false));
        assertTrue(isMoveValid(whiteMove, true));
    }

    /**
     * Testing that the CGT AI returns one of the best moves.
     * This test shows the evaluate() method returning before
     * the 5 seconds, and so the GameValue is then used to return
     * a move.
     */
    @Test
    public void testCGTMove(){

        AIPlayer blackPlayer = new AIPlayer(false);
        blackPlayer.setAIType("cgt");
        blackPlayer.addPieces(blackPieces);
        Move blackMove = blackPlayer.getMove(board);

        AIPlayer whitePlayer = new AIPlayer(true);
        whitePlayer.setAIType("cgt");
        whitePlayer.addPieces(whitePieces);
        Move whiteMove = whitePlayer.getMove(board);

        assertTrue(isMoveValid(blackMove, false));
        assertTrue(isMoveValid(whiteMove, true));
    }

    /**
     * Testing that the random AI returns a valid move
     */
    @Test
    public void testRandomMove(){

        AIPlayer blackPlayer = new AIPlayer(false);
        blackPlayer.setAIType("random");
        blackPlayer.addPieces(blackPieces);
        Move blackMove = blackPlayer.getMove(board);

        AIPlayer whitePlayer = new AIPlayer(true);
        whitePlayer.setAIType("random");
        whitePlayer.addPieces(whitePieces);
        Move whiteMove = whitePlayer.getMove(board);

        assertTrue(isMoveValid(blackMove, false));
        assertTrue(isMoveValid(whiteMove, true));
    }
}
