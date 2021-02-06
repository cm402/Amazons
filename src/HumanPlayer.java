import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player implements Serializable {

    // constructor 
    public HumanPlayer(boolean white){
        super(white, true);
    }

    @Override
    public Move getMove(Board board){

        ArrayList<Move> validMoves = getValidMoves(board);

        // if no valid moves, don't ask user for move
        if(validMoves.isEmpty()){
            return null;
        }

        while(true){

            Move move = getUserMove(board);
            //Move move = askForMove(board);

            if(!isMoveValid(move, board)){
                System.out.println("please retry");
            } else {
                return move;
            }

        }

    }

    private String askUser(String userPrompt){

        Scanner sc = new Scanner(System.in);
        System.out.println(userPrompt);
        return sc.nextLine().trim().toLowerCase();
    }

    // Given a string coordinate, will check that its valid for the current board size
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

    private Move getUserMove(Board board){

        while(true){

            String input = askUser("Please enter your move");

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

    private boolean isMoveValid(Move move, Board board){

        // 1. check player has amazon in correct start position
        // 2. check end position square is in valid squares for start position
        // 3. check shoot position square is in valid squares for end position

        if(!this.getPieces().contains(move.getPiece())){
            System.out.println("Amazon start location co-ordinates are invalid");
            return false;
        }

        if(!board.getValidSquares(move.getStartPosition(), -1, -1).contains(move.getEndPosition())){
            System.out.println("Amazon end location co-ordinates are invalid");
            return false;
        }

        if(!board.getValidSquares(move.getEndPosition(), move.getPiece().getPosition().getX(), move.getPiece().getPosition().getY()).contains(move.getBurnedSquare())){
            System.out.println("Amazons arrow location co-ordinates are invalid");
            return false;
        }

        return true;
    }

}