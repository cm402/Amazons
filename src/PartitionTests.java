import java.util.ArrayList;

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

    // The second board partition example, U shape 4 by 4 board
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

    // The third board partition example, line of 4 squares
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
}
