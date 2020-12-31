import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class PartitionTests {

    GameEngine engine = new GameEngine();

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

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
        int totalWhiteWins = engine.simulateGames(noOfSimulations, partition, firstToMove, setup);

        System.out.println("From " + noOfSimulations + " simulations, white wins " + totalWhiteWins + " times");

    }
}
