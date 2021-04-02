import java.util.ArrayList;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Used to run the Amazons program, containing methods to play the game,
 * simulate games, as well the tutorial and review game options.
 */
public class GameEngine {

    /**
     * Defining and initialising 2 player objects, either AI or human,
     * depending on how many are specified
     * @param noOfHumanPlayers The number of humanPlayer objects required
     * @return A list of 2 Player objects
     */
    public static ArrayList<Player> setupPlayers(int noOfHumanPlayers){

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> AITypes = new ArrayList<>();
        IO io = new IO();

        // if we have AI players, ask what type of AI to use
        if(noOfHumanPlayers < 2){

            // Passing the number of AI players
            AITypes = io.getAITypes(2 - noOfHumanPlayers);

        }

        if(noOfHumanPlayers == 0){

            players.add(new AIPlayer(true, AITypes.get(0)));
            players.add(new AIPlayer(false, AITypes.get(1)));

        } else if(noOfHumanPlayers == 1){

            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false, AITypes.get(0)));

        } else {

            players.add(new HumanPlayer(true));
            players.add(new AIPlayer(false));
        }
        return players;
    }

    /**
     * Defining and initialising a new Board object, filling it with pieces and giving
     * the pieces to the correct player objects
     * @param boardSize Size of the board required
     * @param players List of players to give the pieces to
     * @return a Board object, ready to be used for a game
     */
    private Board setupBoard(int boardSize, ArrayList<Player> players){

        Board board = new Board(boardSize, boardSize);
        board.resetBoard(players.get(0), players.get(1));
        board.printBoard();

        return board;
    }

    /**
     * Chooses the first player, depending on who has the white pieces.
     * @param p1 First player object
     * @param p2 Second player object
     * @return The player with the white pieces, to play first.
     */
    private Player chooseFirstPlayer(Player p1, Player p2){

        Player currentPlayer;

        if(p1.isWhite()){
            currentPlayer = p1;
        } else {
            currentPlayer = p2;
        }

        return currentPlayer;
    }

    /**
     * Updating a board, with move that has been selected by a player.
     * @param nextMove Move to be played
     * @param board Board to update
     * @param printBoard boolean used to indicate if the board should be printed or not
     */
    public void updateBoard(Move nextMove, Board board, boolean printBoard){

        board.setSquarePiece(nextMove.getEndPosition().getX(), nextMove.getEndPosition().getY(), nextMove.getPiece());
        board.burnSquare(nextMove.getBurnedSquare().getX(), nextMove.getBurnedSquare().getY());

        if(printBoard){
            board.printBoard();
        }
    }

    /**
     * Gets the other player, given the player who was the last "currentPlayer"
     * @param players List of 2 player objects
     * @param currentPlayer Player who played last
     * @return Player object indicating who will play next
     */
    public Player swapPlayers(ArrayList<Player> players, Player currentPlayer){

        if(players.indexOf(currentPlayer) == 0){
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    /**
     * Writing a finished games gameFile to disk.
     * @param movesPlayed A list of the moves played in the completed game.
     * @param board The board that the game was played on.
     */
    public void outputGameFile(ArrayList<Move> movesPlayed, Board board){

        GameFile gameFile = new GameFile(movesPlayed, board.getRowBoardSize());

        FileInputOutput fio = new FileInputOutput();

        fio.outputGameFile(gameFile);
    }

    /**
     * Outputting the winner of the game, and exiting the program.
     * @param players List of the 2 player objects
     * @param currentPlayer The player who lost
     * @param movesPlayed The number of moves made
     * @param board The board played on
     */
    private void finishGame(ArrayList<Player> players, Player currentPlayer, ArrayList<Move> movesPlayed, Board board){

        System.out.println("");
        System.out.println("player " + players.indexOf(currentPlayer) + " is unable to move, and therefore has lost");
        System.out.println("This game lasted " + movesPlayed.size() + " moves");
        outputGameFile(movesPlayed, board);
        System.exit(0);
    }

    /**
     * Playing through a game, once the board and players have been initialised
     * @param board The board that will be used for the game
     * @param movesPlayed A list to store the moves that have been played
     * @param currentPlayer Stores the player who is next to move
     * @param players List of the 2 players for the game
     */
    public void startGame(Board board, ArrayList<Move> movesPlayed, Player currentPlayer, ArrayList<Player> players){

        while(true){

            // Getting the next move from the current player
            Move nextMove = currentPlayer.getMove(board);
            movesPlayed.add(nextMove);

            // Checking if the game is over, as the current player can't make a move
            if(nextMove == null){
                finishGame(players, currentPlayer, movesPlayed, board);
            }

            // Updating the board, outputting the move player to console, and swapping the current player
            updateBoard(nextMove, board, true);
            System.out.println(nextMove.toString());
            currentPlayer = swapPlayers(players, currentPlayer);
        }
    }

    /**
     * Setup the game, play through it and output the gameFile to file
     * @param io Input/Output object, used for console I/O
     * @param engine GameEngine, providing essential game methods
     */
    public void playGame(IO io, GameEngine engine){

        Board board;
        Player currentPlayer;
        ArrayList<Move> movesPlayed = new ArrayList<>();

        // game setup
        ArrayList<Player> players = setupPlayers(io.getNoOfPlayers());
        board = engine.setupBoard(io.getBoardSize(), players);
        currentPlayer = engine.chooseFirstPlayer(players.get(0), players.get(1));

        engine.startGame(board, movesPlayed, currentPlayer, players);

        engine.outputGameFile(movesPlayed, board);
    }

    /**
     * Retrieving a gameFile from disk, allowing it to be reviewed.
     * @return A GameFile object for the most recently played game
     */
    public GameFile inputGameFile(){

        FileInputOutput fio = new FileInputOutput();
        GameFile gameFile = fio.getGameFile();
        return gameFile;
    }

    /**
     * Reviewing a game, allowing the user to go through the moves
     * in the most recent game, navigating forwards and  backwards
     * through moves or selecting a specific move number.
     * @param io Input/Output object, used for console I/O
     * @param engine GameEngine, providing essential game methods
     */
    public void reviewGame(IO io, GameEngine engine){

        GameFile gf = engine.inputGameFile();

        if(gf == null){

            System.out.println("Error, previous game file not found, please play a game and then try again");
            System.exit(0);
        }

        ArrayList<Move> moves = gf.getMovesPlayed();
        int boardSize = gf.getBoardSize();

        io.reviewIntroduction(boardSize);

        Board board = new Board(boardSize, boardSize);
        board.resetBoard(new Player(true, false), new Player(false, false));

        // Storing a list of boards, one for each move played, to allow for quick
        // traversal between the moves
        ArrayList<Board> boards = new ArrayList<>();

        boards.add(board.newBoard(0, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1, -1));

        for(Move move: moves){

            if(move != null){

                updateBoard(move, board, false);

                // removing amazon from old square
                board.getSquare(move.getStartPosition().getX(), move.getStartPosition().getY()).removeAmazon();
                boards.add(board.newBoard(0, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1, -1));
            }
        }

        int moveCounter = 0;

        while(true) {

            String userInput = io.getReviewInput(moves.size());

            if (userInput.equals("m")) {

                int moveNumber = 1;

                for (Move move : moves) {
                    System.out.println(moveNumber + ". " + move);
                    moveNumber++;
                }

            } else if (userInput.equals("n")) {

                if (moveCounter == moves.size() - 1) {
                    System.out.println("Error- can't go forward any more, game is finished");
                    continue;
                }

                moveCounter++;
                boards.get(moveCounter).printBoard();
                System.out.println("move " + moveCounter + ": " + moves.get(moveCounter - 1).toString());

            } else if (userInput.equals("b")) {

                if (moveCounter == 0) {
                    System.out.println("Error- can't go back any more, this is the starting board");
                    continue;
                }

                moveCounter--;
                boards.get(moveCounter).printBoard();

                if(moveCounter == 0){
                    System.out.println("Starting board (move 0)");
                } else {
                    System.out.println("move " + moveCounter + ": " + moves.get(moveCounter - 1).toString());
                }

            } else if (userInput.equals("help")) {

                io.reviewIntroduction(boardSize);

            } else {

                int moveValue = Integer.parseInt(userInput);

                if(moveValue < 0 || moveValue >= moves.size()){
                    System.out.println("Error- please enter a valid move number");
                    continue;
                }

                moveCounter = moveValue;
                boards.get(moveCounter).printBoard();

                if(moveCounter == 0){
                    System.out.println("Starting board (move 0)");
                } else {
                    System.out.println("move " + moveCounter + ": " + moves.get(moveCounter - 1).toString());
                }
            }
        }
    }

    /**
     * A quick tutorial allowing a user to learn how to play the Amazons game.
     * @param io Input/Output object, used for console I/O
     * @param Args List of command-line arguments
     */
    public void tutorial(IO io, String Args[]){

        System.out.println("Welcome to the tutorial, enter \"n\" for the next part, or \"b\" to return to the first menu");

        String tutorialInput = io.getTutorialInput();

        // part 1 = background & rules explanation

        if(tutorialInput.equals("n")){
            io.tutorialIntroduction();
        } else {
            main(Args);
        }

        tutorialInput = io.getTutorialInput();

        // part 2 = algebraic notation & board shown

        if(tutorialInput.equals("n")){
            io.tutorialNotation();
        } else {
            main(Args);
        }

        tutorialInput = io.getTutorialInput();

        // part 3 = how to enter a move & move shown on board

        if(tutorialInput.equals("n")){
            io.tutorialExample();
        } else {
            main(Args);
        }

        main(Args);
    }

    /**
     * Simulates experiment games, with 2 types of AI, to compare their performance.
     * @param noOfSimulations The number of games that will be simulated
     * @param AIType1 The first AI strategy used
     * @param AIType2 The second AI strategy used
     * @return The number of times the first AI strategy wins
     */
    public int simulateGames(int noOfSimulations, String AIType1, String AIType2){

        int AIType1Wins = 0;

        for(int i = 0; i < noOfSimulations; i++){

            Board board = new Board(6, 6);

            AIPlayer p1, p2;

            // for the first half of simulations, give player 1 white, then swap
            if(i < noOfSimulations / 2){

                p1 = new AIPlayer(true);
                p2 = new AIPlayer(false);

            } else {

                p1 = new AIPlayer(false);
                p2 = new AIPlayer(true);

            }

            // Giving both AI players their correct types
            p1.AIType = AIType1;
            p2.AIType = AIType2;

            board.resetBoard(p1, p2);
            AIPlayer currentPlayer = p1;

            // Simulating a single game
            while(true){

                Move nextMove = currentPlayer.getMove(board);

                // when game is over
                if(nextMove == null){

                    System.out.println("game " + i + " finished");

                    // if 2nd player can't move, add 1 to first players wins
                    if(currentPlayer.equals(p2)){

                        AIType1Wins++;

                    }
                    break;
                }

                updateBoard(nextMove, board, true);

                // Swapping players
                if(currentPlayer.equals(p1)){
                    currentPlayer = p2;
                } else {
                    currentPlayer = p1;
                }

            }
        }
        return AIType1Wins;
    }

    public static void main(String Args[]){

        GameEngine engine = new GameEngine();
        IO io = new IO();

        // if no arguments are given, we just run the main program
        if(Args.length == 0){

            int introValue = io.getIntroduction();

            if(introValue == 1){

                engine.playGame(io, engine);

            } else if(introValue == 2){

                engine.reviewGame(io, engine);

            } else {

                engine.tutorial(io, Args);
            }

        } else {

            DatabaseFiller databaseFiller = new DatabaseFiller();

            if(Args[0].equals("experiments")){

                ArrayList<String> AITypes = io.getAITypes(2);
                int noOfSimulations = io.getNoOfSimulations();

                int firstPlayerWins = engine.simulateGames(noOfSimulations, AITypes.get(0), AITypes.get(1));
                System.out.println(AITypes.get(0) + " won " + firstPlayerWins + " games out of " + noOfSimulations + " against " + AITypes.get(1));

            } else if(Args[0].equals("unitTests")){

                Result result = JUnitCore.runClasses(BoardTests.class);

                for(Failure failure: result.getFailures()){

                    System.out.println(failure.toString());
                }

            } else if(Args[0].equals("fillDatabase")){

                int runTime = io.getRunTime();
                int maxBoardSize = io.getMaxBoardSize();
                int maxEmptySquares = io.getMaxEmptySquares();

                databaseFiller.fillEndgameDatabase(maxBoardSize, runTime, maxEmptySquares);

            }  else if(Args[0].equals("resetDatabase")){

                FileInputOutput fio = new FileInputOutput();
                fio.resetEndgameDatabase();

            } else if(Args[0].equals("databaseSize")){

                FileInputOutput fio = new FileInputOutput();

                int noOfEntries = fio.getEndgameDatabaseSize();
                System.out.println("Endgame database has " + noOfEntries + " entries");

            } else if(Args[0].equals("reportExamples")){

                ReportExamples reportExamples = new ReportExamples();
                reportExamples.boardTransformationsExamples();
            }
        }
    }
}