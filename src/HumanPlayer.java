import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player implements Serializable {

    // constructor 
    public HumanPlayer(boolean white){
        super(white, true);
    }

    @Override
    public Move getMove(Board board, String gameStatus){

        ArrayList<Move> validMoves = getValidMoves(board);

        // if no valid moves, don't ask user for move
        if(validMoves.isEmpty()){
            return null;
        }

        while(true){

            Move move = askForMove(board);

            if(!isMoveValid(move, board)){
                System.out.println("please retry");
            } else {
                return move;
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

        if(!board.getValidSquares(move.getStartPosition()).contains(move.getEndPosition())){
            System.out.println("Amazon end location co-ordinates are invalid");
            return false;
        }

        if(!board.getValidSquares(move.getEndPosition()).contains(move.getBurnedSquare())){
            System.out.println("Amazons arrow location co-ordinates are invalid");
            return false;
        }

        return true;
    }

    private static int getUserInput(String inputPrompt){
        Scanner sc = new Scanner(System.in);
        System.out.println(inputPrompt);
        String in = sc.nextLine().trim().toLowerCase();
        return Integer.parseInt(in);
    }

    // TODO: validation isn't working on moves
    private Move askForMove(Board board){

        int startX = getUserInput("Please enter the x co-ordinate of the amazon you wish to move");
        int startY = getUserInput("Please enter the y co-ordinate of the amazon you wish to move");
        int endX = getUserInput("Please enter the x co-ordinate where you wish to move your amazon to");
        int endY = getUserInput("Please enter the y co-ordinate where you wish to move your amazon to");
        int shootX = getUserInput("Please enter the x co-ordinate where you wish to shoot to");
        int shootY = getUserInput("Please enter the y co-ordinate where you wish to shoot to");

        return new Move(this, board.getSquare(startX, startY), board.getSquare(endX, endY), board.getSquare(shootX, shootY));
    }

}