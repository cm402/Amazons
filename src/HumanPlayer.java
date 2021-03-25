import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a human player, allowing a program user
 * to select moves, as well as validating the positions
 * selected are valid.
 */
public class HumanPlayer extends Player implements Serializable {

    IO io;

    public HumanPlayer(boolean white){
        super(white, true);
        io = new IO();
    }

    /**
     * Getting a move to play, from the user, via the console
     * @param board Current state of the board
     * @return Move selected by the user
     */
    @Override
    public Move getMove(Board board){

        ArrayList<Move> validMoves = getValidMoves(board);

        // if no valid moves, don't ask user for move
        if(validMoves.isEmpty()){
            return null;
        }

        // Looping until we get a "valid" move
        while(true) {

            Move move = getUserMove(board);

            if (!isMoveValid(move, board)) {
                System.out.println("please retry");
            } else {
                return move;
            }
        }
    }

    /**
     * Checking a move is valid, meaning the start position has a valid
     * piece, the end position can be moved to, and the shoot position can
     * be shot at, according to the moving and shooting rules of Amazons.
     * @param move Move to validate
     * @param board Board we are checking with
     * @return true if move is valid, false otherwise.
     */
    private boolean isMoveValid(Move move, Board board){

        // Checking player has amazon in correct start position
        if(!this.getPieces().contains(move.getPiece())){
            System.out.println("Amazon start location co-ordinates are invalid");
            return false;
        }

        // Checking end position square is in valid squares for start position
        if(!board.getValidSquares(move.getStartPosition(), -1, -1).contains(move.getEndPosition())){
            System.out.println("Amazon end location co-ordinates are invalid");
            return false;
        }

        // Checking shoot position square is in valid squares for end position
        if(!board.getValidSquares(move.getEndPosition(), move.getPiece().getPosition().getX(), move.getPiece().getPosition().getY()).contains(move.getBurnedSquare())){
            System.out.println("Amazons arrow location co-ordinates are invalid");
            return false;
        }
        return true;
    }

    /**
     * Checking if a string co-ordinate is valid, for the current board size.
     * @param coordinate String representation of a co-ordinate
     * @param columns Number of columns on the board
     * @param rows Number of rows on the board
     * @return true if co-ordinate is valid, false otherwise
     */
    private boolean validateCoordinate(String coordinate, int columns, int rows){

        if(coordinate.length() != 2){
            return false;
        }

        int x = coordinate.charAt(0);
        int y = coordinate.charAt(1);

        // 'a' = 97
        if(x < 97 || x > 97 + columns){
            return false;
        }

        // '0' = 48
        if(y < 48 || y > 48 + rows){
            return false;
        }
        return true;
    }

    /**
     * Asking for a move from the user, validating all the
     * co-ordinates given, before returning the move.
     * @param board The current state of the board
     * @return The validated move
     */
    public Move getUserMove(Board board){

        while(true){

            String input = io.askUser("Please enter your move");

            boolean valid = true;
            String[] positions = input.split(",");

            for(int i = 0; i < 3; i++) {

                positions[i] = positions[i].trim();
                if (!validateCoordinate(positions[i], board.getColumnBoardSize(), board.getRowBoardSize())) {
                    System.out.println("Error, please enter your move in the correct format");
                    System.out.println("e.g. a3, b2, d3");
                    System.out.println("");
                    valid = false;
                    break;
                }
            }

            // meaning coordinates are valid for the current board size
            if(valid){

                int startX = positions[0].charAt(0) - 97;
                int startY = positions[0].charAt(1) - 48;

                int endX = positions[1].charAt(0) - 97;
                int endY = positions[1].charAt(1) - 48;

                int shootX = positions[2].charAt(0) - 97;
                int shootY = positions[2].charAt(1) - 48;

                return new Move(this, board.getSquare(startX, startY), board.getSquare(endX, endY), board.getSquare(shootX, shootY));
            }
        }
    }
}