import java.util.ArrayList;
import java.util.HashMap;

public class AITests {

    GameEngine engine = new GameEngine();

    public void testBasicAIGameWhiteFirst(){

        ArrayList<Player> players = engine.setupPlayers(0);
        ArrayList<Move> movesPlayed = new ArrayList<>();
        Player currentPlayer = players.get(0);

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

        players.get(0).addPieces(whitePieces);
        players.get(1).addPieces(blackPieces);

        board.printBoard();

        engine.startGame(board, movesPlayed, currentPlayer, players);

    }

    public void testBasicAIGameBlackFirst(){

        ArrayList<Player> players = engine.setupPlayers(0);
        ArrayList<Move> movesPlayed = new ArrayList<>();
        Player currentPlayer = players.get(1);

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

        players.get(0).addPieces(whitePieces);
        players.get(1).addPieces(blackPieces);

        board.printBoard();

        engine.startGame(board, movesPlayed, currentPlayer, players);

    }

    // testing AI play with 3 by 3 board
    public void testBiggerAIGame(){

        ArrayList<Player> players = engine.setupPlayers(0);
        ArrayList<Move> movesPlayed = new ArrayList<>();
        Player currentPlayer = players.get(0);

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

        players.get(0).addPieces(whitePieces);
        players.get(1).addPieces(blackPieces);

        board.printBoard();

        engine.startGame(board, movesPlayed, currentPlayer, players);
    }

    // testing AI play with 3 by 4 board
    public void testBiggerAIGame2(){

        ArrayList<Player> players = engine.setupPlayers(0);
        ArrayList<Move> movesPlayed = new ArrayList<>();
        Player currentPlayer = players.get(0);

        Board board = new Board(3, 4);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,1));
        board.addPiece(0, 1, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.burnSquare(2, 2);
        board.burnSquare(0, 0);
        board.burnSquare(0, 3);


        players.get(0).addPieces(whitePieces);
        players.get(1).addPieces(blackPieces);

        board.printBoard();

        engine.startGame(board, movesPlayed, currentPlayer, players);
    }

    // testing that the new move heuristic of playing a move that limits the
    // other players moves
    public void testHeuristicMove(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, whitePieces.get(0));

        AIPlayer player = new AIPlayer(false);
        player.addPieces(blackPieces);
        Move move = player.getHeuristicMove(board);

        board.printBoard();

        Board newBoard = board.playMove(move);
        newBoard.printBoard();

    }

    // testing that the new move heuristic of playing a move that limits the
    // other players moves
    public void testMonteCarloMove(){

        Board board = new Board(3, 3);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(2,2));
        board.addPiece(2, 2, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, whitePieces.get(0));

        AIPlayer player = new AIPlayer(false);
        player.addPieces(blackPieces);
        Move move = player.getMonteCarloMove(board);

        board.printBoard();

        Board newBoard = board.playMove(move);
        newBoard.printBoard();

    }

    // testing that an AIPlayer object that is passed the partitions DB, then uses it to search for a move
    // NOTE- ensure that partitionTests.fillPartitionsDB has been run first, to fill the partitions DB
    public void testAIWithPartitionsDB(){

        // 1. Setting up a board that will be stored in partitions database
        Board board = new Board(2, 2);
        board.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board.getSquare(0,0));
        board.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board.getSquare(1,0));
        board.addPiece(1, 0, whitePieces.get(0));

        board.printBoard();

        // 2. Reading Partitions DB from file (this will be done in GameEngine)
        FileInputOutput fio = new FileInputOutput();
        HashMap<Integer, GameValue> partitionsDB = fio.getPartitionsDB();

        // 3. Passing DB to AI player, using constructor
        AIPlayer player = new AIPlayer(false, partitionsDB);
        player.addPieces(blackPieces);

        Move move = player.getCGTMove(board);

        System.out.println(move.toString());

    }
}
