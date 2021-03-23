import java.util.ArrayList;

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

        Board board = new Board(boardSize, boardSize);
        board.resetBoard(players.get(0), players.get(1));
        board.printBoard();

        return board;
    }


    public void updateBoard(Move nextMove, Board board, boolean printBoard){

        board.setSquarePiece(nextMove.getEndPosition().getX(), nextMove.getEndPosition().getY(), nextMove.getPiece());
        board.burnSquare(nextMove.getBurnedSquare().getX(), nextMove.getBurnedSquare().getY());

        if(printBoard){
            board.printBoard();
        }
    }

    public Player swapPlayers(ArrayList<Player> players, Player currentPlayer){

        if(players.indexOf(currentPlayer) == 0){
            return players.get(1);
        } else {
            return players.get(0);
        }
    }

    private void outputMove(Move move, Player currentPlayer, ArrayList<Player> players){

        String colour = "";
        if(currentPlayer.isWhite()){
            colour = "white";
        } else {
            colour = "black";
        }

        //System.out.println("player " + players.indexOf(currentPlayer) + " is next to move (" + colour + ")");
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

        GameFile gameFile = new GameFile(movesPlayed, board.getRowBoardSize());

        FileInputOutput fio = new FileInputOutput();

        fio.outputGameFile(gameFile);
    }

    public GameFile inputGameFile(){

        FileInputOutput fio = new FileInputOutput();
        GameFile gameFile = fio.getGameFile();
        return gameFile;

    }

    public void printGameFile(GameFile gf){

        System.out.println("previous games board size was " + gf.getBoardSize());

        ArrayList<Move> previousGamesMoves = gf.getMovesPlayed();

        for(Move move: previousGamesMoves){
            System.out.println(move);
        }
    }

    public void startGame(Board board, ArrayList<Move> movesPlayed, Player currentPlayer, ArrayList<Player> players){

        while(true){

            Move nextMove = currentPlayer.getMove(board);
            movesPlayed.add(nextMove);

            if(nextMove == null){
                finishGame(players, currentPlayer, movesPlayed, board);
            }

            updateBoard(nextMove, board, true);
            outputMove(nextMove, currentPlayer, players);
            currentPlayer = swapPlayers(players, currentPlayer);
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

                    // if 2nd player can't move, add 1 to first players wins
                    if(currentPlayer.equals(p2)){

                      AIType1Wins++;

                    }
                    break;
                }

                updateBoard(nextMove, board, false);

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

    public void playGame(IO io, GameEngine engine){

        Board board;
        Player currentPlayer;
        ArrayList<Move> movesPlayed = new ArrayList<>();

        ArrayList<Player> players = setupPlayers(io.getNoOfPlayers());

        board = engine.setupBoard(io.getBoardSize(), players);

        currentPlayer = engine.setupPlayers(players.get(0), players.get(1));

        engine.startGame(board, movesPlayed, currentPlayer, players);

        engine.outputGameFile(movesPlayed, board);

    }

    public void reviewGame(IO io, GameEngine engine){

        GameFile gf = engine.inputGameFile();

        ArrayList<Move> moves = gf.getMovesPlayed();
        int boardSize = gf.getBoardSize();

        io.reviewIntroduction(boardSize);

        Board board = new Board(boardSize, boardSize);
        board.resetBoard(new Player(true, false), new Player(false, false));

        ArrayList<Board> boards = new ArrayList<Board>();

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

    public void tutorial(IO io, String Args[]){

        System.out.println("Welcome to the tutorial, enter \"n\" for the next part, or \"b\" to return to the first menu");

        String tutorialInput = io.getTutorialInput();

        // part 1 = background & rules explanation

        if(tutorialInput.equals("n")){
            io.tutorialIntroducion();
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

    public void testing(){

        ReportExamples reportExamples = new ReportExamples();
        //reportExamples.boardPrintExample();
        //reportExamples.boardSimplifyExample();
        //reportExamples.boardEqualsExample();
        //reportExamples.boardSplitExample();
        //reportExamples.endgameDatabaseExample();
        //reportExamples.boardEvaluateExample();
        //reportExamples.boardHashCodeExample();


        PartitionTests partitionTests = new PartitionTests();
        //partitionTests.testRandom();
        //partitionTests.fillPartitionsDatabase();

        AITests aiTests = new AITests();
        //aiTests.testHeuristicMove();
        //aiTests.testMonteCarloMove();
        //aiTests.testAIWithPartitionsDB();
        //aiTests.testBasicAIGameBlackFirst();
        //aiTests.testBasicAIGameWhiteFirst();

        BoardTests boardTests = new BoardTests();
        //boardTests.testSplit1();
        //boardTests.testFlipVertical();
        //boardTests.testFlipHorizontal();
        //boardTests.testTransformingGameValues();
        //boardTests.testTransformingSquares();
        //boardTests.testGetGameValue();
        //boardTests.testBiggerAIGame2();
        //boardTests.testPartitionsDBLarge();
        //boardTests.testEvalutate3();
        //boardTests.testEvalutate4();
        //boardTests.testEvalutate2();
        //boardTests.testHashCode();
        //boardTests.testPartitionsDBSpeed();
        //boardTests.testPartitionsDBSaved();
        //boardTests.testEvaluateSplit();
        //boardTests.testEvaluateSplit2();

        GameValueTests gameValueTests = new GameValueTests();
        //gameValueTests.testGetSimplestForm();
        //gameValueTests.testIsSimpleFraction();
        //gameValueTests.testSimplify();
        //gameValueTests.testGameValueEquals1();
        //gameValueTests.testGameValueEquals2();
        //gameValueTests.testGameValueEquals3();
    }


    public static void main(String Args[]){

        GameEngine engine = new GameEngine();

        // if no arguments are given, we just run the main program
        if(Args.length == 0){

            IO io = new IO();

            int introValue = io.getIntroduction();

            if(introValue == 1){

                engine.playGame(io, engine);

            } else if(introValue == 2){

                engine.reviewGame(io, engine);

            } else {

                engine.tutorial(io, Args);
            }

        // if arguments given, in "testing" mode
        } else {

            if(Args[0].equals("experiments")){

                int firstPlayerWins = engine.simulateGames(50, "Heuristic", "MCTS");
                //System.out.println(firstPlayerWins);

            } else if(Args[0].equals("testing")){

                engine.testing();

            }

        }
    }

}