import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Used to fill the Endgame Database with GameValues for all possible boards
 *  up to a specified size, in a systematic way to ensure that we have as
 *  quick an AI as possible at run-time.
 */
public class DatabaseFiller{

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
     * Given a range of values (0 to maxValue), returns all possible combinations
     * of a list of values of size n.
     * @param n size of the list of values
     * @param maxValue maximum value that an integer in the list can take
     * @return ArrayList of lists of integers, Size = (maxValue + 1) ^ n
     */
    public ArrayList<int[]> generateCombinations(int n, int maxValue){

        ArrayList<int[]> combinations = new ArrayList<>();

        int[] combination = new int[n];

        for(int i = 0; i <= maxValue; i++){
            generatorHelper(n, 0, i, maxValue, combination, combinations);
        }
        return combinations;
    }

    /**
     * Helper method for generateCombinations(), recursively generating the combinations.
     * @param n size of the list
     * @param position index to add a number to the list
     * @param value integer value to add to the list, at given position
     * @param maxValue maximum value that an integer in the list can take
     * @param combination list to add the value to
     * @param combinations ArrayList of integer lists
     */
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

    /**
     * Generating all possible board combinations, up to a specified size
     * @param maxSize the maximum size of board
     * @return ArrayList of board objects
     */
    public ArrayList<Board> generateAllBoardCombinations(int maxSize){

        ArrayList<Board> boards = new ArrayList<Board>();

        // looping through all board size combinations, up to maximum size
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

    /**
     * Filling the Endgame database with all possible GameValue objects for boards
     * up to a specified size
     * @param maxSize maximum board size
     */
    public void fillPartitionsDatabase(int maxSize){

        // 1. Getting all possible board variations, up to specified size
        ArrayList<Board> boards = generateAllBoardCombinations(maxSize);

        // 2. Creating a new HashMap to store the GameValues
        FileInputOutput fio = new FileInputOutput();
        HashMap<Integer, GameValue> partitionsDB = new HashMap<>();

        // 3. Evaluating each board, storing the GameValues in the HashMap
        for(Board board: boards){

            board.printBoard();
            board.evaluate(partitionsDB);
        }

        // 4. Writing the HashMap of GameValues to a file, to act as the Endgame Database
        fio.outputDB(partitionsDB);
    }
}
