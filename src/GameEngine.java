import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GameEngine {

    private Player setupPlayers(Player p1, Player p2){

        Player currentPlayer;

        // white goes first, so whoever is white to being is currentPlayer
        if(p1.isWhite()){
            currentPlayer = p1;
        } else {
            currentPlayer = p2;
        }

        return currentPlayer;
    }

    private Board setupBoard(int boardSize, ArrayList<Player> players){

        Board board = new Board(boardSize);
        board.resetBoard(players.get(0), players.get(1));
        board.printBoard();

        return board;
    }


    private void updateBoard(Move nextMove, Board board){

        board.setSquarePiece(nextMove.getEndPosition().getX(), nextMove.getEndPosition().getY(), nextMove.getPiece());
        board.burnSquare(nextMove.getBurnedSquare().getX(), nextMove.getBurnedSquare().getY());
        board.printBoard();
    }

    private void swapPlayers(ArrayList<Player> players, Player currentPlayer){

        if(players.indexOf(currentPlayer) == 0){
            currentPlayer = players.get(1);
        } else {
            currentPlayer = players.get(0);
        }
    }

    private void outputMove(Move move, Player currentPlayer, ArrayList<Player> players){

        String colour = "";
        if(currentPlayer.isWhite()){
            colour = "white";
        } else {
            colour = "black";
        }

        System.out.println("player " + players.indexOf(currentPlayer) + " is next to move (" + colour + ")");
        System.out.println(move.toString());
    }

    private void finishGame(ArrayList<Player> players, Player currentPlayer, ArrayList<Move> movesPlayed, Board board){
        System.out.println("");
        System.out.println("player " + players.indexOf(currentPlayer) + " is unable to move, and therefore has lost");
        System.out.println("This game lasted " + movesPlayed.size() + " moves");
        outputGameFile(movesPlayed, board);
        System.exit(0);
    }

    public void outputGameFile(ArrayList<Move> movesPlayed, Board board){

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

    private void startGame(String gameStatus, Board board, ArrayList<Move> movesPlayed, Player currentPlayer, ArrayList<Player> players){

        gameStatus = "started";

        while(!gameStatus.equals("finished")){

            Move nextMove = currentPlayer.getMove(board, gameStatus);
            movesPlayed.add(nextMove);

            if(nextMove == null){
                gameStatus = "finished";
                finishGame(players, currentPlayer, movesPlayed, board);
            }

            updateBoard(nextMove, board);
            outputMove(nextMove, currentPlayer, players);
            swapPlayers(players, currentPlayer);
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

    // testing that a board partition is set up correctly
    public void testBoardPartition(){

        IO io = new IO();

        BoardPartition partition = io.getPartitionBoardSize();

        partition.setupBoard();

        io.getPartitionBurntSquares(partition);

        ArrayList<Player> partitionPlayers = setupPlayers(0); // both AI players

        io.getPartitionPieces(partition, partitionPlayers);

        // 0 = white to move, 1 = black to move
        int firstToMove = io.getPartitionFirstToMove(partition);

        partition.printBoard();


        // now, to play the game through 100 times
        /*
        Player partitionCurrentPlayer;

        if(firstToMove == 0){
            partitionCurrentPlayer = partitionPlayers.get(0);
        } else {
            partitionCurrentPlayer = partitionPlayers.get(1);
        }

        String partitionGameStatus = "started";

        while(!partitionGameStatus.equals("finished")){

            Move nextMove = partitionCurrentPlayer.getPartitionMove(partition, partitionGameStatus);

            if(nextMove == null){
                gameStatus = "finished";
                finishGame();
            }

            // TODO: update these to so they don't work on the current board, but the
            //updateBoard(nextMove);
            //outputMove(nextMove);
            //swapPlayers();
        }
        */


    }

    public static void main(String Args[]){

        Board board;
        Player currentPlayer;
        String gameStatus = "starting";
        ArrayList<Move> movesPlayed = new ArrayList<>();

        GameEngine engine = new GameEngine();

        //GameFile gf = engine.inputGameFile();
        //engine.printGameFile(gf);

        IO io = new IO();

        ArrayList<Player> players = setupPlayers(io.getNoOfPlayers());

        board = engine.setupBoard(io.getBoardSize(), players);

        currentPlayer = engine.setupPlayers(players.get(0), players.get(1));

        engine.startGame(gameStatus, board, movesPlayed, currentPlayer, players);

        engine.outputGameFile(movesPlayed, board);

        //engine.testBoardPartition();

    }

}