import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class PartitionTests {

    GameEngine engine = new GameEngine();

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

        for(int i = 0; i < noOfSimulations; i++){

            ArrayList<Player> partitionPlayers = engine.setupPlayers(0); // both AI players

            partition.setupPartitionPieces(setup, partitionPlayers); // uses setup object to burn squares / place pieces

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
     * Simulates a game, for a given board object
     * @param board Board object we want to simulate a game for
     * @param currentPlayer The first player to move
     * @param players List of both player objects
     * @return Result of simulated game, true if white wins, false otherwise
     */
    public Boolean simulateGame(Board board, Player currentPlayer, ArrayList<Player> players){

        while(true){

            Move nextMove = currentPlayer.getMove(board);

            if(nextMove == null){
                return currentPlayer.isWhite();
            }

            engine.updateBoard(nextMove, board, false);
            currentPlayer = engine.swapPlayers(players, currentPlayer);
        }
    }

    // Asks the user for partition before simulating it
    public void testIO(){

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
        int totalWhiteWins = simulatePartitionGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }


    // The first board partition example, L shape
    public void testL(int size){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        for(int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
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

    // The second board partition example, U shape
    public void testU(int size){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        for(int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 1; j++) {
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

    // The third board partition example, O shape
    public void testO(int size){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        for(int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 2; j++) {
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

    // The fourth board partition example, line of 4 squares
    public void testLine(int length){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(length);
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

    // The fifth board partition example, staircase shape
    public void testStaircase(int size){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size
        setup.setNoOfColumns(size);
        setup.setNoOfRows(size);

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

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
        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

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

    // returns a random number, between 1 and max
    private int getRandomNumber(int max){
        Random rand = new Random();
        return rand.nextInt(max) + 1;
    }

    private int getXCoordinate(int squareValue, int noOfColumns){

        int x = squareValue % noOfColumns - 1;// x = Remainder

        // last in row case for x value
        if(x == -1){
            x = noOfColumns - 1;
        }

        return x;
    }

    private int getYCoordinate(int squareValue, int noOfColumns){

        int y = squareValue / noOfColumns; // y = Quotient

        // last in row case for y value
        if(squareValue % noOfColumns == 0){
            y = y - 1;
        }

        return y;
    }

    // checks if coordinates already stored in lists
    // returns true if coordinates already stored
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

    // generates coordinates that haven't been used already
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

    // The final board partition example, a random partition generator
    public void testRandom(){

        int noOfSimulations = 100;
        int firstToMove = 0; // white first to move

        BoardPartitionSetup setup = new BoardPartitionSetup();

        // 1. store board size, random number between 1 and 10
        setup.setNoOfColumns(getRandomNumber(10));
        setup.setNoOfRows(getRandomNumber(10));

        // 2. generate board partition, using board size
        Board partition = new Board(setup.getNoOfColumns(), setup.getNoOfRows());
        partition.setupBoard();

        // 3. store burnt squares
        ArrayList<Integer> xBurntCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yBurntCoordinates = new ArrayList<Integer>();

        // choose a random number of squares to burn
        int noOfBurntSquares = getRandomNumber(setup.getNoOfColumns() * setup.getNoOfRows());

        for(int i = 0; i < noOfBurntSquares; i++){

            generateCoordinates(setup, xBurntCoordinates, yBurntCoordinates, xBurntCoordinates, yBurntCoordinates);
        }

        setup.setXBurntSquareCoordinates(xBurntCoordinates);
        setup.setYBurntSquareCoordinates(yBurntCoordinates);

        // 4. store black and white pieces
        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

        generateCoordinates(setup, xBurntCoordinates, yBurntCoordinates, whiteXCoordinates, whiteYCoordinates);

        // storing all the coordinates used for both burnt squares and white pieces in 2 arraylists
        ArrayList<Integer> usedXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> usedYCoordinates = new ArrayList<Integer>();
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

    // Given a range of values (0 to maxValue), returns all possible combinations
    // of a list of values of size n
    // Size = (maxValue + 1)^n
    public ArrayList<int[]> generateCombinations(int n, int maxValue){

        ArrayList<int[]> combinations = new ArrayList<>();

        int[] combination = new int[n];

        for(int i = 0; i <= maxValue; i++){
            generatorHelper(n, 0, i, maxValue, combination, combinations);
        }

        return combinations;
    }

    // helper method for the generateCombinations() method, recursively generating the combinations
    public void generatorHelper(int n, int position, int value, int maxValue, int[] combination, ArrayList<int[]> combinations){

        combination[position] = value;

        if(position == n - 1){
            combinations.add(combination.clone());

        } else {

            for(int i = 0; i <= maxValue; i++){
                generatorHelper(n, position + 1, i, maxValue, combination, combinations);
            }
        }

    }


    public ArrayList<Board> generateAllBoardCombinations(int maxSize){

        ArrayList<Board> boards = new ArrayList<Board>();

        // looping through all board size combinations, up to 3 by 3
        for(int rows = 1; rows <= maxSize; rows++){
            for(int columns = 1; columns <= maxSize; columns++){

                // 1. Generate all the combinations of values 0-3 for the number of squares
                int numberOfSquares = rows * columns;
                ArrayList<int[]> combinations = generateCombinations(numberOfSquares, 3);

                // 2. Use these values to set squares for boards

                // looping through each of the square state combinations possible
                for(int[] combination: combinations){

                    Board board = new Board(columns, rows);
                    board.setupBoard();

                    // looping through each of the squares individual state values
                    for(int i = 0; i < combination.length; i++) {

                        // using the array index, generating associated x & y co-ordinate values
                        int x = getXCoordinate(i + 1, columns);
                        int y = getYCoordinate(i + 1, columns);

                        int squareState = combination[i];

                        // using square state value to set squares
                        if (squareState == 1) {
                            board.getSquare(x, y).burnSquare();
                        } else if (squareState == 2) {
                            Piece whitePiece = new Piece(true);
                            whitePiece.setPosition(board.getSquare(x, y));
                            board.getSquare(x, y).setAmazon(whitePiece);
                        } else if (squareState == 3) {
                            Piece blackPiece = new Piece(false);
                            blackPiece.setPosition(board.getSquare(x, y));
                            board.getSquare(x, y).setAmazon(blackPiece);


                        }

                    }

                    ArrayList<Piece> whitePieces = board.getPieces(true);
                    ArrayList<Piece> blackPieces = board.getPieces(false);

                    // not storing partitions with
                    // - over 2 pieces of either colour
                    // - only 1 square
                    // - no pieces of any colour
                    // - all squares burnt
                    if(whitePieces.size() > 2 || blackPieces.size() > 2
                        || board.getRowBoardSize() == 1 && board.getColumnBoardSize() == 1
                        || board.getNumberOfBurntSquares() == numberOfSquares){
                        continue;
                    }
                    boards.add(board);
                }
            }

        }

        return boards;
    }

    // fills the partition database with GameValue objects for each of the possible
    // Board variations, up to a specified max size
    public void fillPartitionsDatabase(){

        ArrayList<Board> boards = generateAllBoardCombinations(2);
        // getting partitions Database from file
        FileInputOutput fio = new FileInputOutput();
        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        for(Board board: boards){

            board.printBoard();
            GameValue gameValue = board.evaluate(partitionsDB);

            fio.outputDB(partitionsDB);
        }

        partitionsDB.size();

    }

}
