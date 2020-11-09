import java.util.ArrayList;
import java.util.Scanner;

public class GameEngine {

    private ArrayList<Player> players;
    private Board board;
    private Player currentPlayer;
    private String gameStatus;
    private ArrayList<Move> movesPlayed;

    private void setupGame(Player p1, Player p2, int boardSize){

        players = new ArrayList<Player>();
        board = new Board(boardSize);
        gameStatus = "Setup";
        movesPlayed = new ArrayList<Move>();

        this.players.add(p1);
        this.players.add(p2);

        // white goes first, so whoever is white to being is currentPlayer
        if(p1.isWhite()){
            this.currentPlayer = p1;
        } else {
            this.currentPlayer = p2;
        }

        this.board.resetBoard(p1, p2);
        this.movesPlayed.clear();

        this.board.printBoard();
    }


    private static int getUserInput(String inputPrompt){
        Scanner sc = new Scanner(System.in);
        System.out.println(inputPrompt);
        String in = sc.nextLine().trim().toLowerCase();
        return Integer.parseInt(in);
    }

    private Move getPlayerMove() {

        return currentPlayer.getMove(board, gameStatus);

    }

    private void updateBoard(Move nextMove){
        board.setSquarePiece(nextMove.getEndPosition().getX(), nextMove.getEndPosition().getY(), nextMove.getPiece());
        board.burnSquare(nextMove.getBurnedSquare().getX(), nextMove.getBurnedSquare().getY());
        board.printBoard();
    }

    private void swapPlayers(){

        if(players.indexOf(currentPlayer) == 0){
            currentPlayer = players.get(1);
        } else {
            currentPlayer = players.get(0);
        }
    }

    private void outputMove(Move move){

        String colour = "";
        if(currentPlayer.isWhite()){
            colour = "white";
        } else {
            colour = "black";
        }

        System.out.println("player " + players.indexOf(currentPlayer) + " is next to move (" + colour + ")");
        System.out.println(move.toString());
    }

    private void finishGame(){
        System.out.println("");
        System.out.println("player " + players.indexOf(currentPlayer) + " is unable to move, and therefore has lost");
        System.out.println("This game lasted " + movesPlayed.size() + " moves");
        outputGameFile();
        System.exit(0);
    }

    public void outputGameFile(){

        GameFile gameFile = new GameFile(movesPlayed, board.getBoardSize());

        GameFileWriterReader gfrw = new GameFileWriterReader();

        gfrw.outputGameFile(gameFile);
    }

    public GameFile inputGameFile(){

        GameFileWriterReader gfrw = new GameFileWriterReader();
        GameFile gameFile = gfrw.getGameFile();
        return gameFile;

    }

    public void printGameFile(GameFile gf){

        System.out.println("previous games board size was " + gf.getBoardSize());

        ArrayList<Move> previousGamesMoves = gf.getMovesPlayed();

        for(Move move: previousGamesMoves){
            System.out.println(move);
        }
    }



    private void startGame(){

        gameStatus = "started";

        while(!gameStatus.equals("finished")){

            Move nextMove = getPlayerMove();
            movesPlayed.add(nextMove);

            if(nextMove == null){
                gameStatus = "finished";
                finishGame();
            }

            updateBoard(nextMove);
            outputMove(nextMove);
            swapPlayers();
        }

    }

    public static ArrayList<Player> setupPlayers(int noOfHumanPlayers){

        ArrayList<Player> players = new ArrayList<Player>();

        if(noOfHumanPlayers == 0){

            players.add(new AIPlayer(true));
            players.add(new AIPlayer(false));
        } else if(noOfHumanPlayers == 1){
            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false));
        } else {
            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false));
        }

        return players;
    }


    public ArrayList<Player> getNoOfPlayers(){

        int noOfHumanPlayers = - 1;

        while(noOfHumanPlayers < 0 || noOfHumanPlayers > 2){

            noOfHumanPlayers = getUserInput("How many human players would you like for this game? (0, 1 or 2)");

            if(noOfHumanPlayers < 0 || noOfHumanPlayers > 2) {
                System.out.println("Error, please enter a valid number of human players (0, 1 or 2)");
            }
        }

        ArrayList<Player> players = setupPlayers(noOfHumanPlayers);

        return players;

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

    public void getSquaresToBurn(BoardPartition partition){

        // looping to ensure that the user can burn as many squares as they want
        while(true) {

            int xCoord = -2;
            int yCoord = -2;

            // validating that the x co-ordinate entered is valid, for the board size
            while (xCoord < 0 || xCoord >= partition.getColumnBoardSize()) {

                xCoord = getUserInput("Enter the x co-ordinate of a square you would like to burn, or -1 if you are finished burning squares");

                if (xCoord == -1) {
                    return;
                }

                if(xCoord < 0 || xCoord >= partition.getColumnBoardSize()){
                    System.out.println("Error, please enter a valid x co-ordinate (0 to " + (partition.getColumnBoardSize() - 1) + ")");
                }
            }

            // validating that the y co-ordinate entered is valid, for the board size
            while (yCoord < 0 || yCoord >= partition.getRowBoardSize()) {

                yCoord = getUserInput("Enter the y co-ordinate of a square you would like to burn, or -1 if you are finished burning squares");

                if (yCoord == -1) {
                    return;
                }

                if(yCoord < 0 || yCoord >= partition.getRowBoardSize()){
                    System.out.println("Error, please enter a valid x co-ordinate (0 to " + (partition.getRowBoardSize() - 1) + ")");
                }
            }

            partition.burnSquare(xCoord, yCoord);
        }
    }

     // testing that a board partition is set up correctly
    public void testBoardPartition(){

        BoardPartition partition = getPartitionBoardSize();

        partition.setupBoard();

        getSquaresToBurn(partition);

        partition.printBoard();

    }

    public static void main(String Args[]){

        GameEngine engine = new GameEngine();

        //GameFile gf = engine.inputGameFile();
        //engine.printGameFile(gf);

        /*

        ArrayList<Player> players = engine.getNoOfPlayers();

        int boardSize = engine.getBoardSize();

        engine.setupGame(players.get(0), players.get(1), boardSize);

        engine.startGame();

        engine.outputGameFile();

        */

        engine.testBoardPartition();

    }

}