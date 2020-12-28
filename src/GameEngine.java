import java.util.ArrayList;

public class GameEngine {

    private Player setupPlayers(Player p1, Player p2){

        Player currentPlayer;

        // white goes first, so whoever is white to being is currentPlayer
        if(p1.isWhite()){
            currentPlayer = p1;
        } else {
            currentPlayer = p2;
        }

        return currentPlayer;
    }

    private Board setupBoard(int boardSize, ArrayList<Player> players){

        Board board = new Board(boardSize, boardSize);
        board.resetBoard(players.get(0), players.get(1));
        board.printBoard();

        return board;
    }


    private void updateBoard(Move nextMove, Board board, boolean printBoard){

        board.setSquarePiece(nextMove.getEndPosition().getX(), nextMove.getEndPosition().getY(), nextMove.getPiece());
        board.burnSquare(nextMove.getBurnedSquare().getX(), nextMove.getBurnedSquare().getY());

        if(printBoard){
            board.printBoard();
        }
    }

    private Player swapPlayers(ArrayList<Player> players, Player currentPlayer){

        if(players.indexOf(currentPlayer) == 0){
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    private void outputMove(Move move, Player currentPlayer, ArrayList<Player> players){

        String colour = "";
        if(currentPlayer.isWhite()){
            colour = "white";
        } else {
            colour = "black";
        }

        System.out.println("player " + players.indexOf(currentPlayer) + " is next to move (" + colour + ")");
        System.out.println(move.toString());
    }

    private void finishGame(ArrayList<Player> players, Player currentPlayer, ArrayList<Move> movesPlayed, Board board){

        System.out.println("");
        System.out.println("player " + players.indexOf(currentPlayer) + " is unable to move, and therefore has lost");
        System.out.println("This game lasted " + movesPlayed.size() + " moves");
        outputGameFile(movesPlayed, board);
        System.exit(0);
    }

    public void outputGameFile(ArrayList<Move> movesPlayed, Board board){

        GameFile gameFile = new GameFile(movesPlayed, board.getRowBoardSize());

        GameFileWriterReader gfrw = new GameFileWriterReader();

        gfrw.outputGameFile(gameFile);
    }

    public GameFile inputGameFile(){

        GameFileWriterReader gfrw = new GameFileWriterReader();
        GameFile gameFile = gfrw.getGameFile();
        return gameFile;

    }

    public void printGameFile(GameFile gf){

        System.out.println("previous games board size was " + gf.getBoardSize());

        ArrayList<Move> previousGamesMoves = gf.getMovesPlayed();

        for(Move move: previousGamesMoves){
            System.out.println(move);
        }
    }

    private void startGame(Board board, ArrayList<Move> movesPlayed, Player currentPlayer, ArrayList<Player> players){

        while(true){

            Move nextMove = currentPlayer.getMove(board);
            movesPlayed.add(nextMove);

            if(nextMove == null){
                finishGame(players, currentPlayer, movesPlayed, board);
            }

            updateBoard(nextMove, board, true);
            outputMove(nextMove, currentPlayer, players);
            currentPlayer = swapPlayers(players, currentPlayer);
        }

    }

    public static ArrayList<Player> setupPlayers(int noOfHumanPlayers){

        ArrayList<Player> players = new ArrayList<Player>();

        if(noOfHumanPlayers == 0){

            players.add(new AIPlayer(true));
            players.add(new AIPlayer(false));
        } else if(noOfHumanPlayers == 1){
            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false));
        } else {
            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false));
        }

        return players;
    }

    // simulates the game board that is passed in
    // returns true if white wins, false if black wins
    private Boolean simulateGame(Board board, Player currentPlayer, ArrayList<Player> players){

        while(true){

            Move nextMove = currentPlayer.getMove(board);

            if(nextMove == null){
                return currentPlayer.isWhite();
            }

            updateBoard(nextMove, board, false);
            currentPlayer = swapPlayers(players, currentPlayer);
        }
    }

    // simulates a partition game a number of times, returns the amount of wins white gets
    public int simulateGames(int noOfSimulations, Board partition, int firstToMove, BoardPartitionSetup setup){

        int whiteWins = 0;

        for(int i = 0; i < noOfSimulations; i++){

            ArrayList<Player> partitionPlayers = setupPlayers(0); // both AI players

            partition.setupPartitionPieces(setup, partitionPlayers); // uses setup object to burn squares / place pieces

            // on first iteration, print board
            if(i == 0){
                partition.printBoard();
            }

            Player partitionCurrentPlayer;

            if(firstToMove == 0){
                partitionCurrentPlayer = partitionPlayers.get(0);
            } else {
                partitionCurrentPlayer = partitionPlayers.get(1);
            }

            if(simulateGame(partition, partitionCurrentPlayer, partitionPlayers)){
                whiteWins++;
            }

            // clearing the partition board, for the next iteration
            partition.setupBoard();

        }

        return whiteWins;

    }


    // testing that a board partition is set up correctly
    public void testBoardPartition(){

        IO io = new IO();
        int noOfSimulations = 100;

        // getting partition setup information from IO

        BoardPartitionSetup setup = new BoardPartitionSetup();
        io.getPartitionBoardSize(setup);
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        io.getPartitionBurntSquares(partition, setup);
        io.getPartitionPieces(partition, setup);

        // 0 = white to move, 1 = black to move
        int firstToMove = io.getPartitionFirstToMove();

        // running through simulations
        int totalWhiteWins = simulateGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }

    // The first board partition example, L shape 4 by 4 board
    private void testBoardPartitionExample1(){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(4);
        setup.setNoOfRows(4);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        int burntSquareSize = 3;

        for(int i = 1; i <= burntSquareSize; i++) {
            for (int j = 1; j <= burntSquareSize; j++) {
                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(3);
        blackXCoordinates.add(3);
        blackYCoordinates.add(0);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulateGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }

    // The second board partition example, U shape 4 by 4 board
    private void testBoardPartitionExample2(){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(4);
        setup.setNoOfRows(4);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        for(int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(3);
        blackXCoordinates.add(3);
        blackYCoordinates.add(3);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulateGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }

    // The third board partition example, line of 4 squares
    private void testBoardPartitionExample3(){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(4);
        setup.setNoOfRows(1);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(0);
        blackXCoordinates.add(3);
        blackYCoordinates.add(0);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulateGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }


    public static void main(String Args[]){

        Board board;
        Player currentPlayer;
        ArrayList<Move> movesPlayed = new ArrayList<>();

        GameEngine engine = new GameEngine();

        //GameFile gf = engine.inputGameFile();
        //engine.printGameFile(gf);

        /*
        IO io = new IO();

        ArrayList<Player> players = setupPlayers(io.getNoOfPlayers());

        board = engine.setupBoard(io.getBoardSize(), players);

        currentPlayer = engine.setupPlayers(players.get(0), players.get(1));

        engine.startGame(board, movesPlayed, currentPlayer, players);

        engine.outputGameFile(movesPlayed, board);

        */

        //engine.testBoardPartition();
        engine.testBoardPartitionExample3();

    }

}