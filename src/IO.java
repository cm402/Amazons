import java.util.ArrayList;
import java.util.Scanner;

public class IO {

    private int getUserInput(String inputPrompt){

        Scanner sc = new Scanner(System.in);
        System.out.println(inputPrompt);
        String in = sc.nextLine().trim().toLowerCase();
        return Integer.parseInt(in);
    }

    private String getUserInputString(){

        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine().trim().toLowerCase();
        return in;
    }

    public void reviewIntroduction(int boardSize){

        System.out.println("The previous game was played on a " + boardSize + " by " + boardSize + " board.");
        System.out.println("To see a list of all the moves played, enter \'m\'.");
        System.out.println("To move onto the next move, enter \'n\'.");
        System.out.println("To go back a move, enter \'b\'.");
        System.out.println("To jump to a specific move, enter the move number (e.g. 12)");
        System.out.println("Finally, to see this list of options again, enter \'help\'");
    }

    public String getReviewInput(int noOfMoves){

        String reviewInput = "";

        while(true){

            reviewInput = getUserInputString();

            // options- "m", "n", "b", "help", number between 1 & noOfMoves

            if(!reviewInput.equals("m") && !reviewInput.equals("n") &&
                    !reviewInput.equals("b") && !reviewInput.equals("help")){

                try {

                    int moveNumber = Integer.parseInt(reviewInput);

                    if(moveNumber < 0 | moveNumber > noOfMoves){

                        System.out.println("Error, please enter a valid move number (0 to " + noOfMoves + ")");
                        continue;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error, please enter a valid command");
                    continue;
                }
            }

            return reviewInput;
        }
    }

    public int getIntroduction(){

        System.out.println("Welcome, to \"Game of the Amazons\"");
        System.out.println("Would you like to play a game? (press 1)");
        System.out.println("Or review the previous game? (press 2)");
        System.out.println("Or have a quick tutorial on how to play? (press 3)");

        int introValue = -1;

        while(introValue < 1 || introValue > 3){

            introValue = getUserInput("");

            if(introValue < 1 || introValue > 3){
                System.out.println("Error, please retry");
            }
        }

        return introValue;

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
    public void getPartitionBoardSize(BoardPartitionSetup setup){

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

        setup.setNoOfColumns(noOfColumns);
        setup.setNoOfRows(noOfRows);

        return;

    }

    // Ask the user for co-ordinates, returns either an ArrayList of the 2 coordinates,
    // or null if the user is finished
    // xcoord = index 0
    // ycoord = index 1
    // TODO: validate that the square is also empty (doesn't have a piece already or is burnt)
    public ArrayList<Integer> getCoordinates(Board partition, String outputType){

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
    public void getPartitionPieces(Board partition, BoardPartitionSetup setup){

        ArrayList<Integer> whiteXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> whiteYCoordinates = new ArrayList<Integer>();

        ArrayList<Integer> blackXCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> blackYCoordinates = new ArrayList<Integer>();

        // asking for white pieces
        while(true){

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a white piece");

            // break used to allow black pieces to also be given
            if(coordinates == null){
                break;
            }

            whiteXCoordinates.add(coordinates.get(0));
            whiteYCoordinates.add(coordinates.get(1));

        }

        setup.setXWhitePieceCoordinates(whiteXCoordinates);
        setup.setYWhitePieceCoordinates(whiteYCoordinates);

        // asking for black pieces
        while(true){

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a black piece");

            if(coordinates == null){
                break;
            }

            blackXCoordinates.add(coordinates.get(0));
            blackYCoordinates.add(coordinates.get(1));
        }

        setup.setXBlackPieceCoordinates(blackXCoordinates);
        setup.setYBlackPieceCoordinates(blackYCoordinates);

    }


    // getting the burnt squares information for the partition
    public void getPartitionBurntSquares(Board partition, BoardPartitionSetup setup){

        ArrayList<Integer> xCoordinates = new ArrayList<Integer>();
        ArrayList<Integer> yCoordinates = new ArrayList<Integer>();

        // looping to ensure that the user can burn as many squares as they want
        while(true) {

            ArrayList<Integer> coordinates = getCoordinates(partition, "of a square you would like to burn");

            if(coordinates == null){
                break;
            }

            xCoordinates.add(coordinates.get(0));
            yCoordinates.add(coordinates.get(1));
        }

        setup.setXBurntSquareCoordinates(xCoordinates);
        setup.setYBurntSquareCoordinates(yCoordinates);
    }

    public int getPartitionFirstToMove(){

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
