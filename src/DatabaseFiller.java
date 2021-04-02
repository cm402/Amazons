import java.util.ArrayList;

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
     * Generating a board object, given a list of integers
     * which store information about the squares states.
     * Returns null if we not a board that we want to evaluate
     * e.g. over 2 pieces of 1 colour, only 1 square etc.
     * @param combination list of integers indicating square states.
     * @return Generated Board object, or null
     */
    public Board generateBoard(int[] combination, int rows, int columns, int maxEmptySquares){

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
        // - no pieces of any colour
        // - only 1 square
        // - more than specified maximum number of empty squares
        if(whitePieces.size() > 2 || blackPieces.size() > 2
                || (whitePieces.size() == 0 && blackPieces.size() == 0)
                || board.getRowBoardSize() == 1 && board.getColumnBoardSize() == 1
                || board.getNumberOfEmptySquares() > maxEmptySquares){
            return null;
        }

        return board;
    }

    /**
     * Helper method for generateCombinations(), recursively generating the combinations.
     * @param n size of the list
     * @param position index to add a number to the list
     * @param value integer value to add to the list, at given position
     * @param maxValue maximum value that an integer in the list can take
     * @param combination list to add the value to
     */
    public void combinationsHelper(int n, int position, int value, int maxValue, int[] combination,
                                   int rows, int columns, int maxEmptySquares, long finishTime){

        combination[position] = value;

        // Base case: we can now have enough values to generate & evaluate the board
        if(position == n - 1){


            // once we reach finish time, output all game values to database
            if(System.currentTimeMillis() >= finishTime){

                FileInputOutput fio = new FileInputOutput();
                int sizeAfter = fio.getEndgameDatabaseSize();
                System.out.println("Database now has " + sizeAfter + " entries");
                System.exit(0);
            }

            // Generating current board
            Board board = generateBoard(combination, rows, columns, maxEmptySquares);

            // Checking the board meets the specified criteria to evaluate
            if(board != null){

                // Evaluating current board, adding it to endgame database
                board.printBoard();

                // If board not already stored in database, evaluate and store it
                if(!board.isInDatabase()){

                    board.addToDatabase();
                }
            }

        } else {

            for(int i = 0; i <= maxValue; i++){
                combinationsHelper(n, position + 1, i, maxValue, combination, rows, columns, maxEmptySquares, finishTime);
            }
        }

    }

    /**
     * Given a range of values (0 to maxValue), returns all possible combinations
     * of a list of values of size n.
     * @param n size of the list of values
     * @param maxValue maximum value that an integer in the list can take
     * @return ArrayList of lists of integers, Size = (maxValue + 1) ^ n
     */
    public void evaluateCombinations(int n, int maxValue, int rows, int columns, int maxEmptySquares, long finishTime){

        int[] combination = new int[n];

        for(int i = 0; i <= maxValue; i++){
            combinationsHelper(n, 0, i, maxValue, combination, rows, columns, maxEmptySquares, finishTime);
        }

    }

    /**
     * Filling the endgame Database, generating all the combinations
     * and evaluating them one by one, rather than finding a list
     * of all possible boards.
     * @param maxSize maximum board size
     * @param runTime number of minutes to evaluate for
     * @param maxEmptySquares
     */
    public void fillEndgameDatabase(int maxSize, int runTime, int maxEmptySquares){

        long start = System.currentTimeMillis();
        long finishTime = start + 1000 * 60 * runTime;

        // Looping through all board shapes, up to maximum size
        for(int rows = 1; rows <= maxSize; rows++){
            for(int columns = 1; columns <= maxSize; columns++){

                // Generating all the combinations of values 0-3 for the number of squares
                // in order to use these combinations to generate a board to evaluate
                int numberOfSquares = rows * columns;
                evaluateCombinations(numberOfSquares, 3, rows, columns, maxEmptySquares, finishTime);

            }
        }
    }
}
