import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.HashMap;

public class Board {

    private Square[][] squares;
    private int columnBoardSize, rowBoardSize;

    // columnBoardSize = no of columns, so its the x co-ordinate
    // rowBoardSize = no of rows, or the y co-ordinate

    public Board(int columnBoardSize, int rowBoardSize){
        squares = new Square[columnBoardSize][rowBoardSize];
        this.columnBoardSize = columnBoardSize;
        this.rowBoardSize = rowBoardSize;
    }


    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        for(int x = 0; x < columnBoardSize; x++){
            for(int y = 0; y < rowBoardSize; y++){

                // Each square has 4 possible states, so using that
                // information to generate a hash code value
                if(squares[x][y].isBurnt()){
                    result = prime * result + 0;
                } else if(squares[x][y].getAmazon() == null){
                    result = prime * result + 1;
                } else if(squares[x][y].getAmazon().isWhite()){
                    result = prime * result + 2;
                } else if(squares[x][y].getAmazon().isWhite()){
                    result = prime * result + 3;
                }

            }
        }

        result = prime * result + columnBoardSize;
        result = prime * result + rowBoardSize;

        return result;
    }

    private boolean isColumnBurnt(Board board, int columnIndex) {

        // looping through each row
        for (int i = 0; i < board.getRowBoardSize(); i++) {

            // if square isn't burnt, then we stop checking
            if (!board.getSquare(columnIndex, i).isBurnt()) {
                return false;

            }
        }

        return true;

    }

    private boolean isRowBurnt(Board board, int rowIndex){

        // looping through each column
        for (int i = 0; i < board.getColumnBoardSize(); i++) {

            // if square isn't burnt, then we stop checking
            if (!board.getSquare(i, rowIndex).isBurnt()) {
                return false;

            }
        }

        return true;

    }

    // generates a partition of a board, given an original board and coordinates to start/end at
    // if component Counter = -1, then we don't consider components
    // any other value for a component counter, we only return squares that are part of component
    public Board newBoard(Board board, int startX, int startY, int endX, int endY, int componentCounter){

        // creating new board, with new size
        int newColumnSize = endX - startX + 1;
        int newRowSize = endY - startY + 1;

        Board newBoard = new Board(newColumnSize, newRowSize);
        newBoard.setupBoard();

        int newX = 0;
        int newY = 0;

        for(int x = startX; x <= endX; x++){

            if(x == startX){
                newX = 0;
            } else {
                newX++;
            }

            for(int y = startY; y <= endY; y++){

                if(y == startY){
                    newY = 0;
                } else {
                    newY++;
                }

                Square oldSquare = board.getSquare(x, y);

                // We aren't dealing with components
                if(componentCounter == -1){

                    copySquare(newBoard, oldSquare, newX, newY);

                // We are dealing with components
                } else {

                    // not part of current component, then we just burn the square
                    if(oldSquare.getX() != componentCounter){
                        newBoard.burnSquare(newX, newY);

                    // part of current component
                    } else {

                        copySquare(newBoard, oldSquare, newX, newY);
                    }

                }
            }
        }

        return newBoard;

    }

    // removes outermost burnt rows from the board
    private Board simplify(){

        // first column
        if(isColumnBurnt(this, 0)){

            return newBoard(this, 1, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();
        }

        // last column
        if(isColumnBurnt(this, this.getColumnBoardSize() - 1)){

            return newBoard(this, 0, 0, this.getColumnBoardSize() - 2, this.getRowBoardSize() - 1, -1).simplify();
        }

        // first row
        if (isRowBurnt(this, 0)) {

            return newBoard(this, 0, 1, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();
        }

        // last row
        if (isRowBurnt(this, this.getRowBoardSize() - 1)) {

            return newBoard(this, 0, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 2, -1).simplify();
        }

        return this;

    }

    private boolean boardsEqual(Board board1, Board board2){

        // now, we check if the 2 boards are equal, by checking each square is the same on both
        for(int x = 0; x < board1.getColumnBoardSize(); x++){
            for(int y = 0; y < board1.getRowBoardSize(); y++){

                if(board1.getSquare(x, y).isBurnt() != board2.getSquare(x, y).isBurnt()){
                    return false;
                }

                // if the first board has an amazon on the current square
                if(board1.getSquare(x, y).getAmazon() != null){

                    // checking that there is an also an amazon on the same square on the 2nd board
                    // to prevent a seg fault error
                    if(board2.getSquare(x, y).getAmazon() == null){
                        return false;
                    }

                    // now, checking that the piece is the correct colour
                    if(board1.getSquare(x, y).getAmazon().isWhite() != board2.getSquare(x, y).getAmazon().isWhite()){
                        return false;
                    }

                }
            }
        }

        return true;
    }

    // rotates a board 90 degrees clockwise, using a transposition
    private Board rotate(){

        Board newBoard = new Board(this.getRowBoardSize(), this.getColumnBoardSize());
        newBoard.setupBoard();

        int newX = 0;
        int newY = 0;

        for(int x = 0; x < this.getColumnBoardSize(); x++) {

            if (x == 0) {
                newY = 0;
            } else {
                newY++;
            }

            for (int y = this.getRowBoardSize() - 1; y >= 0; y--) {

                if (y == this.getRowBoardSize() - 1) {
                    newX = 0;
                } else {
                    newX++;
                }

                Square oldSquare = this.getSquare(x, y);

                //System.out.println("(" + x + ", " + y + ") ---->" + "(" + newX + ", " + newY + ")");

                copySquare(newBoard, oldSquare, newX, newY);
            }
        }

        return newBoard;
    }

    public ArrayList<Board> split() {

        ArrayList<Board> partitions = new ArrayList<Board>();

        // making a copy of the board that we can manipulate
        // simplify board first, to remove any edge rows or columns that aren't needed
        Board boardCopy = newBoard(this, 0, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();

        int componentCounter = 0;

        // 1. Set all squares x-coordinate to -1 initially
        for(int x = 0; x < boardCopy.getColumnBoardSize(); x++){
            for(int y = 0; y < boardCopy.getRowBoardSize(); y++){
                boardCopy.getSquare(x, y).setX(-1);
            }
        }

        // 2. Group all unburnt squares into their connected components
        for(int x = 0; x < boardCopy.getColumnBoardSize(); x++){
            for(int y = 0; y < boardCopy.getRowBoardSize(); y++){

                Square square = boardCopy.getSquare(x, y);

                // find a square that hasn't been visited yet, and isn't burnt
                if(square.getX() == -1 && !square.isBurnt()){

                    DFS(boardCopy, square, componentCounter);
                    componentCounter++;
                }
            }
        }


        // 3. Create a partition board for each component
        for(int i = 0; i < componentCounter; i++){

            // 3.1. Extract dimensions information for each component
            boolean firstSquare = true;
            int minX, minY, maxX, maxY;
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;

            for(int x = 0; x < boardCopy.getColumnBoardSize(); x++) {
                for (int y = 0; y < boardCopy.getRowBoardSize(); y++) {

                    Square square = boardCopy.getSquare(x, y);

                    // found a square in the current component
                    if(square.getX() == i){

                        if(firstSquare){
                            minX = x;
                            maxX = x;
                            minY = y;
                            maxY = y;
                            firstSquare = false;
                        }

                        if(x < minX){
                            minX = x;
                        }
                        if(x > maxX){
                            maxX = x;
                        }
                        if(y < minY){
                            minY = y;
                        }
                        if(y > maxY){
                            maxY = y;
                        }

                    }

                }
            }

            // 3.2. Use dimensions to create a new board partition
            partitions.add(newBoard(boardCopy, minX, minY, maxX, maxY, i));

        }

        return partitions;

    }

    // https://www.baeldung.com/cs/graph-connected-components
    private void DFS(Board board, Square square, int componentCounter){

        for(int x = 0; x < board.getColumnBoardSize(); x++){
            for(int y = 0; y < board.getRowBoardSize(); y++){

                // We have located the correct square
                if(board.getSquare(x, y).equals(square)){

                    // add square to current component
                    board.getSquare(x, y).setX(componentCounter);

                    // get list of adjacent squares that are also unburnt
                    ArrayList<Square> unburntAdjacentSquares = getSurroundingUnBurntSquares(board, square);

                    for(Square adjacentSquare: unburntAdjacentSquares){

                        // if adjacent unburnt square is also unvisited, visit that square recursively
                        if(adjacentSquare.getX() == -1){
                            DFS(board, adjacentSquare, componentCounter);
                        }

                    }

                }
            }
        }
    }

    // returns an ArrayList of the squares that are surrounding the current square, and aren't burnt
    private ArrayList<Square> getSurroundingUnBurntSquares(Board board, Square originalSquare){

        ArrayList<Square> unburntSquares = new ArrayList<Square>();

        for(int x = 0; x < board.getColumnBoardSize(); x++) {
            for (int y = 0; y < board.getRowBoardSize(); y++) {

                // We have located the correct square
                if (board.getSquare(x, y).equals(originalSquare)) {

                    ArrayList<Square> squares = new ArrayList<Square>();

                    squares.add(board.getSquare(x + 1, y));
                    squares.add(board.getSquare(x - 1, y));
                    squares.add(board.getSquare(x, y + 1));
                    squares.add(board.getSquare(x, y - 1));

                    // diagonals also count
                    squares.add(board.getSquare(x - 1, y - 1));
                    squares.add(board.getSquare(x - 1, y + 1));
                    squares.add(board.getSquare(x + 1, y - 1));
                    squares.add(board.getSquare(x + 1, y + 1));



                    for(Square square: squares){

                        if(square != null && !square.isBurnt()){
                            unburntSquares.add(square);
                        }
                    }

                }
            }
        }

        return unburntSquares;

    }

    // Flips a board on a horizontal axis
    private Board flipHorizontal(){

        Board newBoard = new Board(this.getColumnBoardSize(), this.getRowBoardSize());
        newBoard.setupBoard();

        int newX = 0;
        int newY = 0;

        for(int x = this.getColumnBoardSize() - 1; x >= 0; x--) {

            if (x == this.getColumnBoardSize() - 1) {
                newX = 0;
            } else {
                newX++;
            }

            for (int y = 0; y < this.getRowBoardSize(); y++) {

                if (y == 0) {
                    newY = 0;
                } else {
                    newY++;
                }

                Square oldSquare = this.getSquare(x, y);

                //System.out.println("(" + x + ", " + y + ") ---->" + "(" + newX + ", " + newY + ")");

                copySquare(newBoard, oldSquare, newX, newY);
            }
        }

        return newBoard;
    }

    // Flips a board on a vertical axis
    private Board flipVertical(){

        Board newBoard = new Board(this.getColumnBoardSize(), this.getRowBoardSize());
        newBoard.setupBoard();

        int newX = 0;
        int newY = 0;

        for(int x = 0; x < this.getColumnBoardSize(); x++) {

            if (x == 0) {
                newX = 0;
            } else {
                newX++;
            }

            for (int y = this.getRowBoardSize() - 1; y >= 0; y--) {

                if (y == this.getRowBoardSize() - 1) {
                    newY = 0;
                } else {
                    newY++;
                }

                Square oldSquare = this.getSquare(x, y);

                //System.out.println("(" + x + ", " + y + ") ---->" + "(" + newX + ", " + newY + ")");

                copySquare(newBoard, oldSquare, newX, newY);
            }
        }

        return newBoard;
    }

    // copies a square to a new board, given the old square and the coordinates to place it on new board
    private void copySquare(Board board, Square oldSquare, int x, int y){

        // if any square on old board is burnt or contains a piece, replicate on new board
        if (oldSquare.isBurnt()) {

            board.burnSquare(x, y);

        } else if (oldSquare.getAmazon() != null) {

            if (oldSquare.getAmazon().isWhite()) {

                board.addPiece(x, y, new Piece(true));
            } else {

                board.addPiece(x, y, new Piece(false));
            }
        }
    }

    // Returns all the pieces of one colour on the current board
    // true = white pieces
    // false = black pieces
    public ArrayList<Piece> getPieces(boolean white){

        ArrayList<Piece> pieces = new ArrayList<>();

        for(int x = 0; x < this.getColumnBoardSize(); x++){
            for(int y = 0; y < this.getRowBoardSize(); y++){

                Piece piece = this.getSquare(x, y).getAmazon();

                // if there is an amazon on the current square, and it matches the colour we are looking for
                if(piece != null && piece.isWhite() == white){

                    pieces.add(piece);
                }
            }
        }

        return pieces;

    }

    // Returns an ArrayList of all possible moves for one colour
    // true = white pieces
    // false = black pieces
    public ArrayList<Move> getAllPossibleMoves(boolean white) {

        // create an AI player who uses one colours pieces
        AIPlayer player = new AIPlayer(white);
        player.addPieces(this.getPieces(white));

        return player.getValidMoves(this);

    }

    // Given a board and a move, returns a new board, with the move played
    public Board playMove(Board board, Move move){

        Board newBoard = this.newBoard(board, 0, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1, -1);

        // storing the piece we will move, on the new board
        Piece oldPiece = move.getPiece();
        Piece newPiece = newBoard.getSquare(oldPiece.getPosition().getX(), oldPiece.getPosition().getY()).getAmazon();

        int x = move.getEndPosition().getX();
        int y = move.getEndPosition().getY();

        // moving piece to new square and burning square that is shot at
        newBoard.setSquarePiece(x, y, newPiece);
        newBoard.burnSquare(move.getBurnedSquare().getX(), move.getBurnedSquare().getY());

        // removing amazon from original square
        if(newBoard.getSquare(move.getStartPosition().getX(), move.getStartPosition().getY()).getAmazon() != null){
            newBoard.getSquare(move.getStartPosition().getX(), move.getStartPosition().getY()).removeAmazon();
        }

        return newBoard;
    }

    // Inverts the board, by swapping the players pieces
    public Board invert(){

        Board invertedBoard = this.newBoard(this, 0, 0,
                this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1);

        for(int x = 0; x < this.getColumnBoardSize(); x++){
            for(int y = 0; y < this.getRowBoardSize(); y++){

                Piece piece = this.getSquare(x, y).getAmazon();

                // if there is an amazon on the current square, swap its colour
                if(piece != null){

                    if(piece.isWhite()){
                        invertedBoard.getSquare(x, y).setAmazon(new Piece(false));
                    } else {
                        invertedBoard.getSquare(x, y).setAmazon(new Piece(true));
                    }

                }
            }
        }

        return invertedBoard;
    }

    // returns the smallest hash code value from all the variants of the current board
    public Integer getSmallestHashValue(){

        ArrayList<Board> variants = new ArrayList<>();

        variants.add(this);
        variants.add(this.flipHorizontal());
        variants.add(this.flipVertical());
        variants.add(this.rotate().rotate());

        Board invertedBoard = this.invert();

        variants.add(invertedBoard);
        variants.add(invertedBoard.flipHorizontal());
        variants.add(invertedBoard.flipVertical());
        variants.add(invertedBoard.rotate().rotate());

        // if board is a square, 8 more possible variants
        if(this.getColumnBoardSize() == this.getRowBoardSize()){

            variants.add(this.rotate());
            variants.add(this.rotate().rotate().rotate());
            variants.add(this.rotate().flipHorizontal());
            variants.add(this.rotate().rotate().rotate().flipHorizontal());

            variants.add(invertedBoard.rotate());
            variants.add(invertedBoard.rotate().rotate().rotate());
            variants.add(invertedBoard.rotate().flipHorizontal());
            variants.add(invertedBoard.rotate().rotate().rotate().flipHorizontal());
        }

        Integer minValue = this.hashCode();

        for(Board board: variants){

            Integer hashValue = board.hashCode();

            if(hashValue < minValue){
                minValue = hashValue;
            }
        }

        return minValue;
    }

    // returns the current boards GameValue object if stored in the partitions database, or null otherwise
    public GameValue getGameValue(HashMap<Integer, GameValue> partitionsDB){

        Integer hashValue = getSmallestHashValue();

        // will either return gameValue object, or null
        return partitionsDB.get(hashValue);

    }

    private GameValue evaluate(Board board, int depth){

        ArrayList<Move> blackMoves = board.getAllPossibleMoves(false);
        ArrayList<Move> whiteMoves = board.getAllPossibleMoves(true);

        ArrayList<GameValue> left = new ArrayList<>();
        ArrayList<GameValue> right = new ArrayList<>();

        //System.out.println("Depth = " + depth);
        //System.out.println("Black now has " + blackMoves.size() + " moves");
        //System.out.println("White now has " + whiteMoves.size() + " moves");

        int blackMoveCounter = 0;

        for(Move move: blackMoves){

            Board newBoard = playMove(board, move);

            if(depth == 0){
                //System.out.println("At top evaluate() call, black move number " + blackMoveCounter++);
            }

            GameValue leftGame = evaluate(newBoard, depth + 1);

            // checking this isn't a duplicate
            if(!leftGame.isIn(left)){

                leftGame.move = move;
                // TODO- maybe simplify GameValue before adding it to list
                leftGame.simplify();
                left.add(leftGame);
            }


        }

        int whiteMoveCounter = 0;

        for(Move move: whiteMoves){

            Board newBoard = playMove(board, move);

            if(depth == 0){
                //System.out.println("At top evaluate() call, white move number " + whiteMoveCounter++);
            }

            GameValue rightGame = evaluate(newBoard, depth + 1);

            // checking this isn't a duplicate
            if(!rightGame.isIn(right)){

                rightGame.move = move;
                rightGame.simplify();
                right.add(rightGame);
            }
        }

        GameValue gameValue = new GameValue();
        gameValue.left = left;
        gameValue.right = right;

        return gameValue;

    }

    // Returns a GameValue object for the current Board
    public GameValue evaluate(HashMap<Integer, GameValue> partitionsDB){

        // TODO- Can quickly evaluate much bigger boards with either 1 players pieces, or no pieces, by counting squares

        GameValue gameValue;

        // if partitionsDB is null, we don't check it
        if(partitionsDB != null){

            gameValue = getGameValue(partitionsDB);

            // 1. Check if GameValue already stored in partitions DB
            if(gameValue != null){
                return gameValue;
            }
        }


        ArrayList<Board> partitions = this.split();

        // game isn't split into partitions, just evaluate the board
        if(partitions.size() == 1){

            gameValue = evaluate(this, 0);

            if(partitionsDB != null){

                // storing the GameValue in the database, to save evaluating it next time

                gameValue.simplify();
                partitionsDB.put(this.getSmallestHashValue(), gameValue);
            }


            return gameValue;

        } else {

            ArrayList<GameValue> gameValues = new ArrayList<>();

            for(Board partition: partitions){

                gameValues.add(evaluate(partition, 0));

            }

            GameValue gameValueTotal = new GameValue();
            gameValueTotal = gameValueTotal.plus(gameValues.get(0), gameValues.get(1));

            // if more than 2 partitions to add together
            if(gameValues.size() > 2){

                for(int i = 2; i < gameValues.size(); i++){

                    gameValueTotal = gameValueTotal.plus(gameValueTotal, gameValues.get(i));

                }

            }

            if(partitionsDB != null){

                partitionsDB.put(this.getSmallestHashValue(), gameValueTotal);
            }

            gameValueTotal.simplify();
            return gameValueTotal;
        }

    }


    @Override
    public boolean equals(Object o) {

        // self check
        if (this == o) {
            return true;
        }

        // first, we must simplify both boards fully
        Board board1 = this.simplify();
        Board board2 = ((Board) o).simplify();

        // both simplified boards are the not same dimensions
        if(board1.getColumnBoardSize() != board2.getColumnBoardSize() ||
        board1.getRowBoardSize() != board2.getRowBoardSize()){


            // meaning a rotation of 90 or 270 can show an equality of both boards
            if(board1.getColumnBoardSize() == board2.getRowBoardSize() &&
            board1.getRowBoardSize() == board2.getColumnBoardSize()){

                Board rotatedBoard90 = board1.rotate();
                Board rotatedBoard270 = rotatedBoard90.rotate().rotate();
                Board diagonalFlipBoard1 = rotatedBoard90.flipHorizontal();
                Board diagonalFlipBoard2 = rotatedBoard270.flipHorizontal();

                return boardsEqual(rotatedBoard90, board2) || boardsEqual(rotatedBoard270, board2)
                        || boardsEqual(diagonalFlipBoard1, board2) || boardsEqual(diagonalFlipBoard2, board2);

            }

            return false; // case where the sizes don't match, even after rotating, so 2 board cant be equal

        //  both boards are the same dimensions
        } else {

            Board horizontalFlippedBoard = board1.flipHorizontal();
            Board verticalFlippedBoard = board1.flipVertical();
            Board rotatedBoard180 = board1.rotate().rotate();

            // board is a square, so check all 8 cases
            if(board1.getColumnBoardSize() == board1.getRowBoardSize()){


                Board rotatedBoard90 = board1.rotate();
                Board rotatedBoard270 = rotatedBoard90.rotate().rotate();
                Board diagonalFlipBoard1 = rotatedBoard90.flipHorizontal();
                Board diagonalFlipBoard2 = rotatedBoard270.flipHorizontal();

                return boardsEqual(board1, board2) || boardsEqual(rotatedBoard180, board2)
                        || boardsEqual(horizontalFlippedBoard, board2) || boardsEqual(verticalFlippedBoard, board2)
                        || boardsEqual(rotatedBoard90, board2) || boardsEqual(rotatedBoard270, board2)
                        || boardsEqual(diagonalFlipBoard1, board2) || boardsEqual(diagonalFlipBoard2, board2);

            // board is a rectangle, so only check first 4 cases
            } else {

                return boardsEqual(board1, board2) || boardsEqual(rotatedBoard180, board2)
                        || boardsEqual(horizontalFlippedBoard, board2) || boardsEqual(verticalFlippedBoard, board2);
            }


        }
    }

    public int getColumnBoardSize(){
        return this.columnBoardSize;
    }

    public int getRowBoardSize(){
        return this.rowBoardSize;
    }

    public Square getSquare(int x, int y){
        
        if (x >= columnBoardSize || y >= rowBoardSize || x < 0 || y < 0){
            //System.out.println("Square out of range");
            return null;
        }

        return squares[x][y];
    }

    public void createSquare(int x, int y){
        this.squares[x][y] = new Square(x, y, null, false);
    }

    public void burnSquare(int x, int y){
        this.squares[x][y].burnSquare();
    }

    // adding a piece to a square
    public void addPiece(int x, int y, Piece piece){

        this.squares[x][y].setAmazon(piece);
        piece.setPosition(squares[x][y]);
    }

    public void setSquarePiece(int x, int y, Piece piece){

        // remove amazon from old square, place it on new square
        piece.getPosition().removeAmazon(); // removes amazon from square
        this.squares[x][y].setAmazon(piece); // places amazon in new square
        piece.setPosition(squares[x][y]); // updates new position within piece object
    }

    // returns if a square is empty or not
    public boolean isSquareEmpty(Square square){

        if (square.isBurnt() || square.getAmazon() != null){
            return false;
        } else {
            return true;
        }
    }

    // Gets all valid squares that can be shot at from the startSquare
    // If passed a non-null oldSquare, we also add this square even if its not empty
    public ArrayList<Square> getValidSquares(Square startSquare, int skipX, int skipY){

        ArrayList<Square> validSquares = new ArrayList<Square>();

        int startX = startSquare.getX();
        int startY = startSquare.getY();


        for(int x = startX - 1; x >= 0; x--) {

            // checking left
            if (isSquareEmpty(squares[x][startY]) || x == skipX && startY == skipY) {
                validSquares.add(squares[x][startY]);
            } else {
                break;
            }
        }

        for(int x = startX + 1; x < columnBoardSize; x++) {

            // checking right
            if (isSquareEmpty(squares[x][startY]) || x == skipX && startY == skipY) {
                validSquares.add(squares[x][startY]);
            } else {
                break;
            }
        }

        for (int y = startY + 1; y < rowBoardSize; y++) {
            // checking up
            if(isSquareEmpty(squares[startX][y]) || startX == skipX && y == skipY){
                validSquares.add(squares[startX][y]);
            } else {
                break;
            }
        }


        for(int y = startY - 1; y >= 0; y--) {
            // checking down
            if(isSquareEmpty(squares[startX][y]) || startX == skipX && y == skipY){
                validSquares.add(squares[startX][y]);
            } else {
                break;
            }

        }

        outerloop:
        for(int x = startX - 1; x >= 0; x--) {
            for (int y = startY + 1; y < rowBoardSize; y++) {

                if (Math.abs(startX - x) == Math.abs(startY - y)) {
                    // checking left/up
                    if (isSquareEmpty(squares[x][y]) || x == skipX && y == skipY) {
                        validSquares.add(squares[x][y]);
                    } else {
                        break outerloop;
                    }
                }
            }
        }

        outerloop:
        for(int x = startX - 1; x >= 0; x--) {
            for(int y = startY - 1; y >= 0; y--) {

                if (Math.abs(startX - x) == Math.abs(startY - y)) {
                    // checking left/down
                    if (isSquareEmpty(squares[x][y]) || x == skipX && y == skipY) {
                        validSquares.add(squares[x][y]);
                    } else {
                        break outerloop;
                    }
                }
            }
        }

        outerloop:
        for(int x = startX + 1; x < columnBoardSize; x++) {
            for (int y = startY + 1; y < rowBoardSize; y++) {

                if (Math.abs(startX - x) == Math.abs(startY - y)) {

                    // checking right/up
                    if (isSquareEmpty(squares[x][y]) || x == skipX && y == skipY) {
                        validSquares.add(squares[x][y]);
                    } else {
                        break outerloop;
                    }
                }

            }
        }

        outerloop:
        for(int x = startX + 1; x < columnBoardSize; x++) {
            for(int y = startY - 1; y >= 0; y--){

                if(Math.abs(startX - x) == Math.abs(startY - y)) {
                    // checking right/down
                    if (isSquareEmpty(squares[x][y]) || x == skipX && y == skipY) {
                        validSquares.add(squares[x][y]);
                    } else {
                        break outerloop;
                    }
                }
            }
        }

        return validSquares;

    }

    // used for board partitions
    public void setupBoard(){

        // create squares for board
        for(int i = 0; i < columnBoardSize; i++){
            for (int j = 0; j < rowBoardSize; j++){
                createSquare(i, j);
            }
        }
        // pieces and burnt squares added later
    }

    // burns all the squares and places the pieces in the correct places, given a setup object
    public void setupPartitionPieces(BoardPartitionSetup setup, ArrayList<Player> partitionPlayers){

        for(int i = 0; i < setup.getXBurntSquareCoordinates().size(); i++){
            this.burnSquare(setup.getXBurntSquareCoordinates().get(i), setup.getYBurntSquareCoordinates().get(i));
        }

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        ArrayList<Piece> blackPieces = new ArrayList<Piece>();

        for(int i = 0; i < setup.getXWhitePieceCoordinates().size(); i++){

            int xCoordinate = setup.getXWhitePieceCoordinates().get(i);
            int yCoordinate = setup.getYWhitePieceCoordinates().get(i);

            // creating a new white piece & setting its position
            Piece whitePiece = new Piece(true);
            whitePiece.setPosition(this.getSquare(xCoordinate, yCoordinate));

            // adding the new white piece at the given coordinates, in the board and to the pieces arraylist
            this.addPiece(xCoordinate, yCoordinate, whitePiece);
            whitePieces.add(whitePiece);
        }

        for(int i = 0; i < setup.getXBlackPieceCoordinates().size(); i++){

            int xCoordinate = setup.getXBlackPieceCoordinates().get(i);
            int yCoordinate = setup.getYBlackPieceCoordinates().get(i);

            // creating a new white piece & setting its position
            Piece blackPiece = new Piece(false);
            blackPiece.setPosition(this.getSquare(xCoordinate, yCoordinate));

            // adding the new white piece at the given coordinates, in the board and to the pieces arraylist
            this.addPiece(xCoordinate, yCoordinate, blackPiece);
            blackPieces.add(blackPiece);
        }

        partitionPlayers.get(0).addPieces(whitePieces);
        partitionPlayers.get(1).addPieces(blackPieces);
    }

    // used for regular game board
    public void resetBoard(Player p1, Player p2){

        // create squares for board
        for(int i = 0; i < columnBoardSize; i++){
            for (int j = 0; j < rowBoardSize; j++){
                createSquare(i, j);
            }   
        }

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        ArrayList<Piece> blackPieces = new ArrayList<Piece>();

        if(rowBoardSize == 6 && columnBoardSize == 6){

            // generating the amazon pieces
            for(int i = 0; i < 2; i++){
                whitePieces.add(new Piece(true));
                blackPieces.add(new Piece(false));
            }

            // adding the amazon pieces to the correct player object
            if(p1.isWhite()){
                p1.addPieces(whitePieces);
                p2.addPieces(blackPieces);
            } else {
                p1.addPieces(blackPieces);
                p2.addPieces(whitePieces);
            }

            // storing the position of the amazons
            whitePieces.get(0).setPosition(squares[0][3]);
            whitePieces.get(1).setPosition(squares[5][2]);

            // putting the amazons in their positions
            squares[0][3].setAmazon(whitePieces.get(0));
            squares[5][2].setAmazon(whitePieces.get(1));

            blackPieces.get(0).setPosition(squares[2][0]);
            blackPieces.get(1).setPosition(squares[3][5]);

            // set black amazons starting positions
            squares[2][0].setAmazon(blackPieces.get(0));
            squares[3][5].setAmazon(blackPieces.get(1));
        }

        if(rowBoardSize == 10 && columnBoardSize == 10) {

            // generating the amazon pieces
            for(int i = 0; i < 4; i++){
                whitePieces.add(new Piece(true));
                blackPieces.add(new Piece(false));
            }

            // adding the amazon pieces to the correct player object
            if(p1.isWhite()){
                p1.addPieces(whitePieces);
                p2.addPieces(blackPieces);
            } else {
                p1.addPieces(blackPieces);
                p2.addPieces(whitePieces);
            }

            // storing the position of the white amazons
            whitePieces.get(0).setPosition(squares[0][3]);
            whitePieces.get(1).setPosition(squares[3][0]);
            whitePieces.get(2).setPosition(squares[6][0]);
            whitePieces.get(3).setPosition(squares[9][3]);

            // putting the white amazons in their positions
            squares[0][3].setAmazon(whitePieces.get(0));
            squares[3][0].setAmazon(whitePieces.get(1));
            squares[6][0].setAmazon(whitePieces.get(2));
            squares[9][3].setAmazon(whitePieces.get(3));

            // storing the position of the white amazons
            blackPieces.get(0).setPosition(squares[0][6]);
            blackPieces.get(1).setPosition(squares[3][9]);
            blackPieces.get(2).setPosition(squares[6][9]);
            blackPieces.get(3).setPosition(squares[9][6]);

            // set black amazons starting positions
            squares[0][6].setAmazon(blackPieces.get(0));
            squares[3][9].setAmazon(blackPieces.get(1));
            squares[6][9].setAmazon(blackPieces.get(2));
            squares[9][6].setAmazon(blackPieces.get(3));
        }
    }

    public void printSquare(int x, int y){

        System.out.print("| ");

        if(squares[x][y].isBurnt()){
            System.out.print("X ");
        } else if(squares[x][y].getAmazon() != null){

            if(squares[x][y].getAmazon().isWhite()){

                System.out.print("W ");
            } else {
                System.out.print("B ");
            }

        } else {
            System.out.print("  ");
        }
    }

    public void printBoard(){

        StringBuilder rowLine = new StringBuilder();
        rowLine.append("  ");

        for(int i = 0; i < (columnBoardSize * 4) + 1; i++){
            rowLine.append("-");
        }

        int maxNumber = rowBoardSize - 1;

        for(int row = rowBoardSize - 1; row >= 0; row--){
            System.out.println("");
            System.out.println(rowLine);

            System.out.print(maxNumber + " ");
            maxNumber--;

            for(int column = 0; column < columnBoardSize; column++){
                printSquare(column, row);
            }
            System.out.print("|");
        }

        System.out.println("");
        System.out.println(rowLine);

        StringBuilder numbersLine = new StringBuilder();
        numbersLine.append("   ");

        char letter = 'A';

        for(int i = 0; i < columnBoardSize; i++){
            numbersLine.append(" " + letter + "  ");
            letter++;
        }

        System.out.println(numbersLine);
        System.out.println("");



    }

}