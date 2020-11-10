import java.util.ArrayList;
import java.util.Scanner;

public class IO {

    private static int getUserInput(String inputPrompt){

        Scanner sc = new Scanner(System.in);
        System.out.println(inputPrompt);
        String in = sc.nextLine().trim().toLowerCase();
        return Integer.parseInt(in);
    }

    public int getNoOfPlayers(){

        int noOfHumanPlayers = - 1;

        while(noOfHumanPlayers < 0 || noOfHumanPlayers > 2){

            noOfHumanPlayers = getUserInput("How many human players would you like for this game? (0, 1 or 2)");

            if(noOfHumanPlayers < 0 || noOfHumanPlayers > 2) {
                System.out.println("Error, please enter a valid number of human players (0, 1 or 2)");
            }
        }

        return noOfHumanPlayers;

    }

    public int getBoardSize(){

        int boardSize = -1;

        while(boardSize != 6 && boardSize != 10){
            boardSize = getUserInput("What size of board would you like? (6 or 10)");

            if(boardSize != 6 && boardSize != 10) {
                System.out.println("Error, please enter a valid board size (6 or 10)");
            }
        }
        return boardSize;
    }

    // getting the dimensions of the partition board
    public BoardPartition getPartitionBoardSize(){

        int noOfColumns = -1;
        int noOfRows = -1;

        // maximum for a partition is 10, as that is the maximum board size
        while(noOfColumns < 0 || noOfColumns > 10){

            noOfColumns = getUserInput("Enter the number of columns you would like the partition to have");

            if(noOfColumns < 0 || noOfColumns > 10){
                System.out.println("Error, please enter a valid number of columns (0-10)");
            }
        }

        while(noOfRows < 0 || noOfRows > 10){

            noOfRows = getUserInput("Enter the number of rows you would like the partition to have");

            if(noOfRows < 0 || noOfRows > 10){
                System.out.println("Error, please enter a valid number of rows (0-10)");
            }
        }

        return new BoardPartition(noOfColumns, noOfRows);

    }

    // Ask the user for co-ordinates, returns either an ArrayList of the 2 coordinates,
    // or null if the user is finished
    // xcoord = index 0
    // ycoord = index 1
    // TODO: validate that the sqaure is also empty (doesn't have a piece already or is burnt)
    public ArrayList<Integer> getCoordinates(BoardPartition partition, String outputType){

        int xCoord = -2;
        int yCoord = -2;

        ArrayList<Integer> coordinates = new ArrayList<Integer>();

        // validating that the x co-ordinate entered is valid, for the board size
        while (xCoord < 0 || xCoord >= partition.getColumnBoardSize()) {

            xCoord = getUserInput("Enter the x co-ordinate " +  outputType + ", or -1 if you are finished");

            if (xCoord == -1) {
                return null;
            } else if(xCoord < 0 || xCoord >= partition.getColumnBoardSize()){
                System.out.println("Error, please enter a valid x co-ordinate (0 to " + (partition.getColumnBoardSize() - 1) + ")");
            } else {
                coordinates.add(xCoord);
            }
        }

        // validating that the y co-ordinate entered is valid, for the board size
        while (yCoord < 0 || yCoord >= partition.getRowBoardSize()) {

            yCoord = getUserInput("Enter the y co-ordinate " + outputType + ", or -1 if you are finished");

            if (yCoord == -1) {
                return null;
            } else if(yCoord < 0 || yCoord >= partition.getRowBoardSize()){
                System.out.println("Error, please enter a valid x co-ordinate (0 to " + (partition.getRowBoardSize() - 1) + ")");
            } else {
                coordinates.add(yCoord);
            }
        }

        return coordinates;

    }

    // getting the pieces information for the partition
    public void getPartitionPieces(BoardPartition partition, ArrayList<Player> players){

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        ArrayList<Piece> blackPieces = new ArrayList<Piece>();

        // asking for white pieces
        while(true){

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a white piece");

            // break used to allow black pieces to also be given
            if(coordinates == null){
                break;
            }

            // adding a new white piece at the given coordinates
            partition.addPiece(coordinates.get(0), coordinates.get(1), new Piece(true));

            Piece whitePiece = new Piece(true);
            whitePiece.setPosition(partition.getSquare(coordinates.get(0), coordinates.get(1)));
            whitePieces.add(whitePiece);
            players.get(0).addPieces(whitePieces);

        }

        // asking for black pieces
        while(true){

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a black piece");

            if(coordinates == null){
                return;
            }

            // adding a new white piece at the given coordinates
            partition.addPiece(coordinates.get(0), coordinates.get(1), new Piece(false));


            Piece blackPiece = new Piece(false);
            blackPiece.setPosition(partition.getSquare(coordinates.get(0), coordinates.get(1)));
            blackPieces.add(blackPiece);
            players.get(1).addPieces(blackPieces);
        }
    }


    // getting the burnt squares information for the partition
    public void getPartitionBurntSquares(BoardPartition partition){

        // looping to ensure that the user can burn as many squares as they want
        while(true) {

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a square you would like to burn");

            if(coordinates == null){
                return;
            }

            partition.burnSquare(coordinates.get(0), coordinates.get(1));
        }
    }

    public int getPartitionFirstToMove(BoardPartition partition){

        int firstToMove = -1;

        while(firstToMove < 0 || firstToMove > 1){
            firstToMove = getUserInput("Please enter who is first to move, 0 for white, 1 for black");

            if(firstToMove < 0 || firstToMove > 1) {
                System.out.println("Error, you must enter a value in the range (0-1)");
            }
        }
        return firstToMove;
    }
}
