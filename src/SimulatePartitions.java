import java.util.ArrayList;
import java.util.Random;

/**
 * Used throughout development to test how different
 * partitions favour black or white, via simulations.
 * Includes methods to generate and simulate partitions
 * which are specified through I/O, various shapes such
 * as L, O, U, lines and staircase, as well as a random
 * partition generator method.
 */
public class SimulatePartitions {

    GameEngine engine = new GameEngine();

    /**
     * Simulates a game, for a given board object
     * @param board Board object we want to simulate a game for
     * @param currentPlayer The first player to move
     * @param players List of both player objects
     * @return Result of simulated game, true if white wins, false otherwise
     */
    public Boolean simulateGame(Board board, Player currentPlayer, ArrayList<Player> players){

        while(true){

            Move nextMove = currentPlayer.getMove(board);

            board.printBoard();

            if(nextMove == null){
                return currentPlayer.isWhite();
            }

            engine.updateBoard(nextMove, board, false);
            currentPlayer = engine.swapPlayers(players, currentPlayer);
        }
    }

    /**
     * Simulates a partition game a number of times, was used during development to test specific partitions
     * @param noOfSimulations The number of simulations games that will be run
     * @param partition The partition board shape used for the simulation games
     * @param firstToMove Indicates which player will move first, 0 or 1
     * @param setup Stores the pieces and burnt squares information for the partition
     * @return The number of times white wins
     */
    public int simulatePartitionGames(int noOfSimulations, Board partition, int firstToMove, BoardPartitionSetup setup){

        int whiteWins = 0;
        ArrayList<Player> partitionPlayers = engine.setupPlayers(0);

        for(int i = 0; i < noOfSimulations; i++){

            // uses setup object to burn squares / place pieces
            partition.setupPartitionPieces(setup, partitionPlayers);

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

    /**
     * Asks the user to specify type of partition and then
     * simulates it.
     * @param noOfSimulations The number of simulation games
     */
    public void testIO(int noOfSimulations){

        IO io = new IO();

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
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Simulates an L shaped partition
     * @param size Size of the L shape
     * @param noOfSimulations The number of simulation games
     */
    public void testL(int size, int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        for(int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(size - 1);
        blackXCoordinates.add(size - 1);
        blackYCoordinates.add(0);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Simulates an U shaped partition
     * @param size Size of the U shape
     * @param noOfSimulations The number of simulation games
     */
    public void testU(int size, int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        for(int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 1; j++) {
                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(size - 1);
        blackXCoordinates.add(size - 1);
        blackYCoordinates.add(size - 1);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Simulates an O shaped partition
     * @param size Size of the O shape
     * @param noOfSimulations The number of simulation games
     */
    public void testO(int size, int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        for(int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 2; j++) {
                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(size - 1);
        blackXCoordinates.add(size - 1);
        blackYCoordinates.add(size - 1);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Simulates an line partition
     * @param length length of the line
     * @param noOfSimulations The number of simulation games
     */
    public void testLine(int length, int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(length);
        setup.setNoOfRows(1);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(0);
        blackXCoordinates.add(length - 1);
        blackYCoordinates.add(0);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Simulates an staircase shaped partition
     * @param size Size of the staircase shape
     * @param noOfSimulations The number of simulation games
     */
    public void testStaircase(int size, int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        int minCounter = 0;

        for(int i = size - 1; i >= 1; i--) {

            for (int j = size - 1; j >= minCounter; j--) {

                xBurntCoordinates.add(i);
                yBurntCoordinates.add(j);
            }
            minCounter++;
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        whiteXCoordinates.add(0);
        whiteYCoordinates.add(0);
        blackXCoordinates.add(size - 2);
        blackYCoordinates.add(0);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }

    /**
     * Gets a random number, between 1 and max
     * @param max maximum value in range
     * @return random number
     */
    private int getRandomNumber(int max){
        Random rand = new Random();
        return rand.nextInt(max) + 1;
    }

    /**
     * Returns the X co-ordinate for a given square value, based on the
     * number of columns in the associated board
     * @param squareValue value associated with the square
     * @param noOfColumns number of columns on the board
     * @return integer indicating the x co-ordinate value
     */
    private int getXCoordinate(int squareValue, int noOfColumns){

        int x = squareValue % noOfColumns - 1;// x = Remainder

        // last in row case for x value
        if(x == -1){
            x = noOfColumns - 1;
        }

        return x;
    }

    /**
     * Returns the Y co-ordinate for a given square value, based on the
     * number of columns in the associated board
     * @param squareValue value associated with the square
     * @param noOfColumns number of columns on the board
     * @return integer indicating the y co-ordinate value
     */
    private int getYCoordinate(int squareValue, int noOfColumns){

        int y = squareValue / noOfColumns; // y = Quotient

        // last in row case for y value
        if(squareValue % noOfColumns == 0){
            y = y - 1;
        }

        return y;
    }

    /**
     * Checks if co-ordinates are already stored in co-ordinate lists
     * @param xCoordinates x co-ordinate list
     * @param yCoordinates y co-ordinate list
     * @param x x co-ordinate to check
     * @param y y co-ordinate to check
     * @return true if co-ordinates found in lists, false otherwise
     */
    public boolean checkCoordinates(ArrayList<Integer> xCoordinates, ArrayList<Integer> yCoordinates, int x, int y){

        // check that coordinates aren't already stored
        for (int j = 0; j < xCoordinates.size(); j++) {

            // if coordinates already stored in lists
            if (xCoordinates.get(j) == x && yCoordinates.get(j) == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a pair of co-ordinates, that aren't already being used.
     * @param setup Partition setup information
     * @param oldX list of x co-ordinates being used
     * @param oldY list of y co-ordinates being used
     * @param newX unused square x co-ordinate
     * @param newY unused square y co-ordinate
     */
    public void generateCoordinates(BoardPartitionSetup setup, ArrayList<Integer> oldX, ArrayList<Integer> oldY, ArrayList<Integer> newX, ArrayList<Integer> newY){

        while (true) { // looping until we get a square that isn't already being used

            // generating a random number, from 1 to the number of squares in the partition
            int squareValue = getRandomNumber(setup.getNoOfColumns() * setup.getNoOfRows());

            // using the number generated to get the x and y coordinates associated
            int x = getXCoordinate(squareValue, setup.getNoOfColumns());
            int y = getYCoordinate(squareValue, setup.getNoOfColumns());

            // checking that the square isn't already being used
            if(checkCoordinates(oldX, oldY, x, y)){
                continue;
            }

            // storing the new square
            newX.add(x);
            newY.add(y);
            break;
        }

    }

    /**
     * Simulates a randomly-generated partition
     * @param noOfSimulations The number of simulation games
     */
    public void testRandom(int noOfSimulations){

        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size, random number between 1 and 10
        setup.setNoOfColumns(getRandomNumber(10));
        setup.setNoOfRows(getRandomNumber(10));

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<>();

        // choose a random number of squares to burn
        int noOfBurntSquares = getRandomNumber(setup.getNoOfColumns() * setup.getNoOfRows());

        for(int i = 0; i < noOfBurntSquares; i++){

            generateCoordinates(setup, xBurntCoordinates, yBurntCoordinates, xBurntCoordinates, yBurntCoordinates);
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<>();

        generateCoordinates(setup, xBurntCoordinates, yBurntCoordinates, whiteXCoordinates, whiteYCoordinates);

        // storing all the coordinates used for both burnt squares and white pieces in 2 arraylists
        ArrayList<Integer> usedXCoordinates = new ArrayList<>();
        ArrayList<Integer> usedYCoordinates = new ArrayList<>();
        usedXCoordinates.addAll(xBurntCoordinates);
        usedXCoordinates.addAll(whiteXCoordinates);
        usedYCoordinates.addAll(yBurntCoordinates);
        usedYCoordinates.addAll(whiteYCoordinates);

        generateCoordinates(setup, usedXCoordinates, usedYCoordinates, blackXCoordinates, blackYCoordinates);

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);
        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

        // 5. simulating the games
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");
    }
}
