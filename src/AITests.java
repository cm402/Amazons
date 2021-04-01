import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Unit tests used throughout development of the
 * AI class, allowing for testing of its methods.
 */
public class AITests {

    public Board board;
    public ArrayList<Piece> whitePieces;
    public ArrayList<Piece> blackPieces;

    /**
     * Example Board to be used in the tests
     *   -------------
     * 1 | B |   |   |
     *   -------------
     * 0 |   | W |   |
     *   -------------
     *     A   B   C
     */
    @Before
    public void setup(){

        board = new Board(3, 2);
        board.setupBoard();

        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePiece);

        whitePieces = new ArrayList<>();
        whitePieces.add(whitePiece);

        blackPieces = new ArrayList<>();
        blackPieces.add(blackPiece);
    }

    /**
     * Testing that the Heuristic AI returns one of the
     * best moves.
     */
    @Test
    public void testMonteCarloMove(){

        AIPlayer player = new AIPlayer(false);
        player.setAIType("mcts");
        player.addPieces(blackPieces);

        Move move = player.getMove(board);
        System.out.println(move);
    }

    /**
     * Testing that the Heuristic AI returns a move that
     * limits the opponents moves.
     */
    @Test
    public void testHeuristicMove(){

        AIPlayer player = new AIPlayer(false);
        player.setAIType("heuristic");
        player.addPieces(blackPieces);

        Move move = player.getMove(board);
        System.out.println(move);
    }

    /**
     * Testing that the CGT AI returns one of the best moves.
     * This test shows the evaluate() method returning before
     * the 5 seconds, and so the GameValue is then used to return
     * a move.
     */
    @Test
    public void testCGTMove(){

        AIPlayer player = new AIPlayer(false);
        player.setAIType("cgt");
        player.addPieces(blackPieces);

        Move move = player.getMove(board);
        System.out.println(move);
    }

    /**
     * Testing that the CGT AI returns one of the best moves,
     * using the endgame database optimisation.
     */
    @Test
    public void testCGTMoveDatabase(){

        Board biggerBoard = new Board(3, 3);
        biggerBoard.setupBoard();

        biggerBoard.burnSquare(2, 0);
        biggerBoard.burnSquare(2, 2);

        Piece blackPiece = new Piece(false);
        blackPiece.setPosition(biggerBoard.getSquare(0,1));
        biggerBoard.addPiece(0, 1, blackPiece);

        Piece whitePiece = new Piece(true);
        whitePiece.setPosition(biggerBoard.getSquare(1,0));
        biggerBoard.addPiece(1, 0, whitePiece);

        ArrayList<Piece> blackPiecesNew = new ArrayList<>();
        blackPiecesNew.add(blackPiece);

        //FileInputOutput fio = new FileInputOutput();
        //HashMap<Integer, GameValue> partitionsDB = fio.getPartitionsDB();

        AIPlayer player = new AIPlayer(false, "cgt");
        player.addPieces(blackPiecesNew);

        Move move = player.getMove(biggerBoard);
        System.out.println(move);
    }

    /**
     * Testing that the random AI returns a valid move
     */
    @Test
    public void testRandomMove(){

        AIPlayer player = new AIPlayer(false);
        player.setAIType("random");
        player.addPieces(blackPieces);

        Move move = player.getMove(board);
        System.out.println(move);
    }
}
