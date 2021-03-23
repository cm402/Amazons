import java.util.ArrayList;
import java.util.HashMap;

/** Represents an Amazons board, storing its shape, as well
 *  as each of the squares associated with it. Can also be used to
 *  store a "partition", which is a section of a a larger board object
 */
public class Board {

    private Square[][] squares; // 2-D array of square objects
    private int columnBoardSize, rowBoardSize; // number of columns and number of rows

    /**
     * Constructor for a board object, number of columns and rows must be given
     * @param columnBoardSize
     * @param rowBoardSize
     */
    public Board(int columnBoardSize, int rowBoardSize){
        squares = new Square[columnBoardSize][rowBoardSize];
        this.columnBoardSize = columnBoardSize;
        this.rowBoardSize = rowBoardSize;
    }

    /**
     * Generates a hashcode value, using the current board object
     * @return The generated hashcode
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        for(int x = 0; x < columnBoardSize; x++){
            for(int y = 0; y < rowBoardSize; y++){

                Piece piece = squares[x][y].getAmazon();

                // Each square has 4 possible states, so using that
                // information to generate a hash code value
                if(squares[x][y].isBurnt()){
                    result = prime * result + 0;
                } else if(piece == null){
                    result = prime * result + 1;
                } else if(piece.isWhite()){
                    result = prime * result + 2;
                } else {
                    result = prime * result + 3;
                }

            }
        }

        result = prime * result + columnBoardSize;
        result = prime * result + rowBoardSize;

        return result;
    }

    /**
     * For this board object and a specific column, checks if the whole column is burnt
     * @param columnIndex Index of column to check
     * @return true if column is burnt, false otherwise
     */
    private boolean isColumnBurnt(int columnIndex) {

        // looping through each row
        for (int i = 0; i < this.getRowBoardSize(); i++) {

            // if square isn't burnt, then we stop checking
            if (!this.getSquare(columnIndex, i).isBurnt()) {
                return false;

            }
        }
        return true;
    }

    /**
     * For this board object and a specific row, checks if whole row is burnt
     * @param rowIndex Index of row to check
     * @return true if row is burnt, false otherwise
     */
    private boolean isRowBurnt(int rowIndex){

        // looping through each column
        for (int i = 0; i < this.getColumnBoardSize(); i++) {

            // if square isn't burnt, then we stop checking
            if (!this.getSquare(i, rowIndex).isBurnt()) {
                return false;

            }
        }
        return true;
    }

    /**
     * Given a board object, and a partition to select, returns a new board which is a copy of that partition
     * @param startX X co-ordinate on the original board that the partition will start from
     * @param startY Y co-ordinate on the original board that the partition will start from
     * @param endX X co-ordinate on the original board that the partition will end at
     * @param endY Y co-ordinate on the original board that the partition will end at
     * @param componentCounter Used when considering components, for splitting a board. -1 means we don't consider components
     * @return The new board object, a partition of the original board
     */
    public Board newBoard(int startX, int startY, int endX, int endY, int componentCounter){

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

                Square oldSquare = this.getSquare(x, y);

                // We aren't dealing with components
                if(componentCounter == -1){

                    newBoard.copySquare(oldSquare, newX, newY);

                // We are dealing with components
                } else {

                    // not part of current component, then we just burn the square
                    if(oldSquare.getX() != componentCounter){
                        newBoard.burnSquare(newX, newY);

                    // part of current component
                    } else {

                        newBoard.copySquare(oldSquare, newX, newY);
                    }

                }
            }
        }
        return newBoard;
    }

    /**
     * Simplifying the current board object, by recursively removing outer-most burnt rows and columns
     * @return "simplified" board object
     */
    public Board simplify(){

        // first column
        if(this.isColumnBurnt( 0)){

            return this.newBoard(1, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();
        }

        // last column
        if(this.isColumnBurnt(this.getColumnBoardSize() - 1)){

            return this.newBoard(0, 0, this.getColumnBoardSize() - 2, this.getRowBoardSize() - 1, -1).simplify();
        }

        // first row
        if (this.isRowBurnt( 0)) {

            return this.newBoard( 0, 1, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();
        }

        // last row
        if (this.isRowBurnt(this.getRowBoardSize() - 1)) {

            return this.newBoard(0, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 2, -1).simplify();
        }
        return this;
    }

    /**
     * Checks the equality of 2 board objects, by checking that each square has the same state on both boards
     * @param board1 First board we want to check equality of
     * @param board2 Second board we want to check equality of
     * @return true if both boards equal, false otherwise
     */
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

    /**
     * Checking equality of "this" board object, with another board object, for all possible transformations.
     * @param o board object we are checking "this" board against
     * @return true if both boards equal, false otherwise
     */
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
        if (board1.getColumnBoardSize() != board2.getColumnBoardSize() ||
                board1.getRowBoardSize() != board2.getRowBoardSize()) {


            // meaning a rotation of 90 or 270 can show an equality of both boards
            if (board1.getColumnBoardSize() == board2.getRowBoardSize() &&
                    board1.getRowBoardSize() == board2.getColumnBoardSize()) {

                Board rotatedBoard90 = board1.rotate();
                Board rotatedBoard270 = rotatedBoard90.rotate().rotate();
                Board diagonalFlipBoard1 = rotatedBoard90.flipHorizontal();
                Board diagonalFlipBoard2 = rotatedBoard270.flipHorizontal();

                return boardsEqual(rotatedBoard90, board2) || boardsEqual(rotatedBoard270, board2)
                        || boardsEqual(diagonalFlipBoard1, board2) || boardsEqual(diagonalFlipBoard2, board2);

            }

            return false; // case where the sizes don't match, even after rotating, so 2 boards can't be equal

            //  both boards are the same dimensions
        } else {

            Board horizontalFlippedBoard = board1.flipHorizontal();
            Board verticalFlippedBoard = board1.flipVertical();
            Board rotatedBoard180 = board1.rotate().rotate();

            // board is a square, so check all 8 cases
            if (board1.getColumnBoardSize() == board1.getRowBoardSize()) {


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

    /**
     * Rotates current board object 90 degrees clockwise, using a transposition
     * @return Rotated board object
     */
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
                newBoard.copySquare(oldSquare, newX, newY);
            }
        }
        return newBoard;
    }

    /**
     * Splits the current board object into partitions, if possible due to burnt rows or columns.
     * Utilises Connected-Components Graph Algorithm
     * @return ArrayList of board partitions, or our original board
     */
    public ArrayList<Board> split() {

        ArrayList<Board> partitions = new ArrayList<Board>();

        // making a copy of the board that we can manipulate
        // simplify board first, to remove any edge rows or columns that aren't needed
        Board boardCopy = this.newBoard( 0, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1).simplify();

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

                    boardCopy.connectedComponentsDFS(square, componentCounter);
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
            partitions.add(boardCopy.newBoard(minX, minY, maxX, maxY, i));
        }
        return partitions;
    }

    /**
     * DFS-based algorithm to give all nodes in a given squares component the same value.
     * @param square unburnt square, with a current value of -1
     * @param componentCounter next component value to be used
     */
    private void connectedComponentsDFS(Square square, int componentCounter){

        for(int x = 0; x < this.getColumnBoardSize(); x++){
            for(int y = 0; y < this.getRowBoardSize(); y++){

                // We have located the correct square
                if(this.getSquare(x, y).equals(square)){

                    // add square to current component
                    this.getSquare(x, y).setX(componentCounter);

                    // get list of adjacent squares that are also unburnt
                    ArrayList<Square> unburntAdjacentSquares = this.getSurroundingUnBurntSquares(square);

                    for(Square adjacentSquare: unburntAdjacentSquares){

                        // if adjacent unburnt square is also unvisited, visit that square recursively
                        if(adjacentSquare.getX() == -1){
                            this.connectedComponentsDFS(adjacentSquare, componentCounter);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets a list of the squares that surround a given square, and aren't burnt.
     * @param originalSquare The given square
     * @return An ArrayList of the surrounding unburnt squares
     */
    private ArrayList<Square> getSurroundingUnBurntSquares(Square originalSquare){

        ArrayList<Square> unburntSquares = new ArrayList<Square>();

        for(int x = 0; x < this.getColumnBoardSize(); x++) {
            for (int y = 0; y < this.getRowBoardSize(); y++) {

                // We have located the correct square
                if (this.getSquare(x, y).equals(originalSquare)) {

                    ArrayList<Square> squares = new ArrayList<Square>();

                    squares.add(this.getSquare(x + 1, y));
                    squares.add(this.getSquare(x - 1, y));
                    squares.add(this.getSquare(x, y + 1));
                    squares.add(this.getSquare(x, y - 1));

                    // diagonals also count
                    squares.add(this.getSquare(x - 1, y - 1));
                    squares.add(this.getSquare(x - 1, y + 1));
                    squares.add(this.getSquare(x + 1, y - 1));
                    squares.add(this.getSquare(x + 1, y + 1));

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

    /**
     * Applying a reflection in the horizontal perpendicular bisector of the current board object.
     * @return Horizontally "flipped" board object
     */
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
                newBoard.copySquare(oldSquare, newX, newY);
            }
        }
        return newBoard;
    }

    /**
     * Applying a reflection in the vertical perpendicular bisector of the current board object.
     * @return Vertically "flipped" board object
     */
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
                newBoard.copySquare(oldSquare, newX, newY);
            }
        }
        return newBoard;
    }

    /**
     * Copying a square to a position in the current board object
     * @param oldSquare Square we want to copy
     * @param x X co-ordinate on current board that we want to copy square onto
     * @param y Y co-ordinate on current board that we want to copy square onto
     */
    private void copySquare(Square oldSquare, int x, int y){

        // if any square on old board is burnt or contains a piece, replicate on new board
        if (oldSquare.isBurnt()) {

            this.burnSquare(x, y);

        } else if (oldSquare.getAmazon() != null) {

            if (oldSquare.getAmazon().isWhite()) {

                this.addPiece(x, y, new Piece(true));

            } else {

                this.addPiece(x, y, new Piece(false));
            }
        }
    }

    /**
     * Gets the number of burnt squares on the current board object
     * @return The number of burnt squares
     */
    public int getNumberOfBurntSquares(){

        int burntSquares = 0;

        for(int x = 0; x < this.getColumnBoardSize(); x++){
            for(int y = 0; y < this.getRowBoardSize(); y++){

                if(this.getSquare(x, y).isBurnt()){

                    burntSquares++;
                }
            }
        }
        return burntSquares;
    }

    /**
     * Gets all the pieces of a given colour, on the current board
     * @param isWhite indicates what colour to check, true for white, false for black
     * @return The number of pieces of the colour given, on the current board
     */
    public ArrayList<Piece> getPieces(boolean isWhite){

        ArrayList<Piece> pieces = new ArrayList<>();

        for(int x = 0; x < this.getColumnBoardSize(); x++){
            for(int y = 0; y < this.getRowBoardSize(); y++){

                Piece piece = this.getSquare(x, y).getAmazon();

                // if there is an amazon on the current square, and it matches the colour we are looking for
                if(piece != null && piece.isWhite() == isWhite){
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Gets a list of all the possible moves for a given colour, on the current board
     * @param isWhite indicates what colour to check, true for white, false for black
     * @return The number of possible moves for the given colour, on the current board
     */
    public ArrayList<Move> getAllPossibleMoves(boolean isWhite) {

        // create an AI player who uses one colours pieces
        AIPlayer player = new AIPlayer(isWhite);
        player.addPieces(this.getPieces(isWhite));

        return player.getValidMoves(this);

    }

    /**
     * Plays a move on a copy of the current board, returning the newly updated board
     * @param move Move to be played
     * @return New board object, with given move played
     */
    public Board playMove(Move move){

        Board newBoard = this.newBoard(0, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1, -1);

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

    /**
     * Inverting the current board, meaning swapping the players pieces
     * @return Inverted board object
     */
    public Board invert(){

        Board invertedBoard = this.newBoard(0, 0,
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

    /**
     * Returns a SmallestHashValue object for the current board, containing the smallest
     * hash value board, its associated hash value, and the transformation applied to the
     * current board to get to the smallest hash value board
     * @return SmallestHashValue object for the current board object
     */
    public SmallestHashValue getSmallestHashValue(){

        // 1. Adding all board variants to a list
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

        // Special case- if board is a square, 8 more possible variants
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

        // 2. Standard find min algorithm to retrieve the smallest hash value from list

        Integer minValue = this.hashCode();

        // Indicates what variation was applied to get the minimum Hash value
        int boardVariationType = 0;

        for(int i = 0; i < variants.size(); i++){

            Integer hashValue = variants.get(i).hashCode();

            if(hashValue < minValue){
                minValue = hashValue;
                boardVariationType = i;
            }

        }

        // 3. Storing smallest hash board, value & transformation from current board
        SmallestHashValue smallestHashValue = new SmallestHashValue();

        smallestHashValue.hashValue = minValue;
        smallestHashValue.transformation = boardVariationType;
        smallestHashValue.board = variants.get(boardVariationType);

        return smallestHashValue;
    }

    /**
     * Rotates a squares co-ordinates 90 degrees clockwise
     * @param square Square to be rotated
     * @return Rotated Square object
     */
    public Square rotatePoint(Square square){

        // x' = y
        int newX = square.getY();

        // y' = maximum y index - x
        int newY = this.getRowBoardSize() - 1 - square.getX();

        return new Square(newX, newY, null, false);
    }

    /**
     * Rotates a squares co-ordinates 90 degrees anti-clockwise
     * @param square Square to be rotated
     * @return Rotated Square object
     */
    public Square rotatePointAnti(Square square){

        // x' = maximum x index - y
        int newX = this.getColumnBoardSize() - 1 - square.getY();

        // y' = x
        int newY = square.getX();

        return new Square(newX, newY, null, false);
    }

    /**
     * Reflects a squares co-ordinates in a horizontal axis
     * @param square Square to be reflected
     * @return Horizontally reflected Square object
     */
    public Square flipPointHorizontal(Square square){

        // x' = maximum x index - x
        int newX = this.getColumnBoardSize() - 1 - square.getX();

        // y' = y
        int newY = square.getY();

        return new Square(newX, newY, null, false);
    }

    /**
     * Reflects a squares co-ordinates in a vertical axis
     * @param square Square to be reflected
     * @return Vertically reflected Square object
     */
    public Square flipPointVertical(Square square) {

        // x' = x
        int newX = square.getX();

        // y' = maximum y index - y
        int newY = this.getRowBoardSize() - 1 - square.getY();

        return new Square(newX, newY, null, false);
    }

    /**
     * Stores the smallest hash value board, its hash value, and the
     * transformation from the current board, to the smallest hash value board
     */
    public class SmallestHashValue {

        int hashValue;
        int transformation;
        Board board;
    }

    // TODO- after set up JUNIT tests, check if we can remove player parameter, replaced with oldMove.getPlayer()
    /**
     * Transforms a Move objects Squares, given a specific transformation value
     * @param oldMove The Move we want to transform
     * @param transformation An integer indicating the type of transformation required
     * @param player Player we want to add to the new move
     * @return new Move object, with Squares transformed
     */
    public Move transformMove(Move oldMove, int transformation, Player player){

        Square start = oldMove.getStartPosition();
        Square end = oldMove.getEndPosition();
        Square shoot = oldMove.getBurnedSquare();

        Square newStart = start;
        Square newEnd = end;
        Square newShoot = shoot;

        if(transformation == 0 || transformation == 4){

            return oldMove;

        } else if(transformation == 1 || transformation == 5){

            newStart = flipPointHorizontal(start);
            newEnd = flipPointHorizontal(end);
            newShoot = flipPointHorizontal(shoot);

        } else if(transformation == 2 || transformation == 6){

            newStart = flipPointVertical(start);
            newEnd = flipPointVertical(end);
            newShoot = flipPointVertical(shoot);

        } else if(transformation == 3 || transformation == 7){

            newStart = rotatePoint(rotatePoint(start));
            newEnd = rotatePoint(rotatePoint(end));
            newShoot = rotatePoint(rotatePoint(shoot));

        } else if(transformation == 8 || transformation == 12){

            newStart = rotatePointAnti(start);
            newEnd = rotatePointAnti(end);
            newShoot = rotatePointAnti(shoot);

        } else if(transformation == 9 || transformation == 13){

            newStart = rotatePoint(start);
            newEnd = rotatePoint(end);
            newShoot = rotatePoint(shoot);

        } else if(transformation == 10 || transformation == 14){

            newStart = rotatePointAnti(flipPointHorizontal(start));
            newEnd = rotatePointAnti(flipPointHorizontal(end));
            newShoot = rotatePointAnti(flipPointHorizontal(shoot));

        } else if(transformation == 11 || transformation == 15){

            newStart = rotatePoint(flipPointHorizontal(start));
            newEnd = rotatePoint(flipPointHorizontal(end));
            newShoot = rotatePoint(flipPointHorizontal(shoot));

        }

        return new Move(player, newStart, newEnd, newShoot);
    }

    // transforms a GameValue from the smallestHashBoard, to the current board

    /**
     * Transforms the smallest hash GameValue, into the current board GameValue object
     * @param smallestHash Contains the smallest hash board, hash value, and transformation from current board
     * @param smallestHashGameValue The GameValue of the smallest hash board
     * @return The GameValue of the current board
     */
    public GameValue transformGameValue(SmallestHashValue smallestHash, GameValue smallestHashGameValue){

        int transform = smallestHash.transformation;

        GameValue transformedGameValue = new GameValue();

        // if transformation in this range of values, it requires an inversion
        if(transform >= 4 && transform <= 7 || transform >= 12 && transform <= 15){

            // swapping left and right sides, for inversion
            transformedGameValue.left = smallestHashGameValue.right;
            transformedGameValue.right = smallestHashGameValue.left;

        } else {

            transformedGameValue.left = smallestHashGameValue.left;
            transformedGameValue.right = smallestHashGameValue.right;

        }

        // Getting players objects and giving them their correct colour pieces
        Player left = smallestHashGameValue.left.get(0).move.getPlayer();
        Player right = smallestHashGameValue.right.get(0).move.getPlayer();

        left.addPieces(this.getPieces(false));
        right.addPieces(this.getPieces(true));

        // transforming each of the moves
        for(GameValue leftOption: transformedGameValue.left){

            Move newMove = transformMove(leftOption.move, transform, left);

            leftOption.move = newMove;
        }

        for(GameValue rightOption: transformedGameValue.right){

            Move newMove = transformMove(rightOption.move, transform, right);

            rightOption.move = newMove;
        }

        return transformedGameValue;
    }

    /**
     * Queries the partitions DB for the current boards GameValue object
     * @param partitionsDB HashMap of boards hashcodes to their associated GameValue objects
     * @return Either the current boards GameValue object, or null
     */
    public GameValue getGameValue(HashMap<Integer, GameValue> partitionsDB){

        SmallestHashValue smallestHash = getSmallestHashValue();
        GameValue smallestHashGameValue = partitionsDB.get(smallestHash.hashValue);

        // if some variation of the board has been evaluated before
        if(smallestHashGameValue != null){

            // Transforming the smallest hash GameValue, to the current boards GameValue
            return transformGameValue(smallestHash, smallestHashGameValue);
        }

        // if board not in database, return null
        return null;
    }

    /**
     * Used to evaluate the current board object into a GameValue object, at run-time
     * @param depth Depth of current recursive call, used for testing purposes
     * @return GameValue object for the current board object
     */
    private GameValue evaluate(int depth){

        ArrayList<Move> blackMoves = this.getAllPossibleMoves(false);
        ArrayList<Move> whiteMoves = this.getAllPossibleMoves(true);

        ArrayList<GameValue> left = new ArrayList<>();
        ArrayList<GameValue> right = new ArrayList<>();

        for(Move move: blackMoves){

            Board newBoard = this.playMove(move);

            GameValue leftGame = newBoard.evaluate(depth + 1);

            // checking this isn't a duplicate
            if(!leftGame.isIn(left)){

                leftGame.move = move;
                leftGame.simplify();
                left.add(leftGame);
            }
        }

        for(Move move: whiteMoves){

            Board newBoard = this.playMove(move);

            GameValue rightGame = newBoard.evaluate(depth + 1);

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

    /**
     * Returns a GameValue object for the current board object, possibly using the endgame database
     * @param partitionsDB HashMap used as the Endgame Database
     * @return GameValue object for the current board object
     */
    public GameValue evaluate(HashMap<Integer, GameValue> partitionsDB){

        GameValue gameValue;

        // if partitionsDB is null, we don't check it
        if(partitionsDB != null){

            gameValue = getGameValue(partitionsDB);

            // Check if GameValue already stored in partitions DB
            if(gameValue != null){
                return gameValue;
            }
        }

        ArrayList<Board> partitions = this.split();

        // game isn't split into partitions, just evaluate the board
        if(partitions.size() == 1){

            SmallestHashValue smallestHash = getSmallestHashValue();

            // Storing the GameValue object for the smallest hash variation of our current Board
            gameValue = smallestHash.board.evaluate(0);
            gameValue.simplify();

            if(partitionsDB != null){

                // storing the GameValue in the database, so don't have to evaluate it next time
                partitionsDB.put(smallestHash.hashValue, gameValue);
            }

            // Transforming our smallestHash GameValue, to the current board GameValue
            GameValue currentBoardGameValue = transformGameValue(smallestHash, gameValue);

            return currentBoardGameValue;

        // board can be split into partitions, must evaluate partitions separately, and add them
        } else {

            // TODO- Setup a board into 3 partitions, and update the AIPlayer to decide which subgame to
            // TODO- play in, based on the GameValue objects of each subgame.
            // TODO- Change from adding partitions, to selecting correct one to move in?
            ArrayList<GameValue> gameValues = new ArrayList<>();

            for(Board partition: partitions){

                gameValues.add(partition.evaluate(0));
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

                //partitionsDB.put(this.hashCode(), gameValueTotal);
                //partitionsDB.put(this.getSmallestHashValue().get(0), gameValueTotal);
            }

            gameValueTotal.simplify();
            return gameValueTotal;
        }
    }

    /**
     * Getter for the columnBoardSize
     * @return this boards objects columnBoardSize
     */
    public int getColumnBoardSize(){
        return this.columnBoardSize;
    }

    /**
     * Getter for the rowBoardSize
     * @return this boards objects rowBoardSize
     */
    public int getRowBoardSize(){
        return this.rowBoardSize;
    }

    /**
     * Gets the square at a particular x & y co-ordinate
     * @return Square object at given co-ordinates
     */
    public Square getSquare(int x, int y){
        
        if (x >= columnBoardSize || y >= rowBoardSize || x < 0 || y < 0){
            return null;
        }
        return squares[x][y];
    }

    /**
     * Creates a new square at a particular x & y co-ordinate
     * @param x square x co-ordinate
     * @param y square y co-ordinate
     */
    public void createSquare(int x, int y){
        this.squares[x][y] = new Square(x, y, null, false);
    }

    /**
     * Burns the square at a particular x & y co-ordinate
     * @param x square x co-ordinate
     * @param y square y co-ordinate
     */
    public void burnSquare(int x, int y){
        this.squares[x][y].burnSquare();
    }

    /**
     * Adds a piece to a particular x and y co-ordinate
     * @param x square x co-ordinate
     * @param y square y co-ordinate
     */
    public void addPiece(int x, int y, Piece piece){

        this.squares[x][y].setAmazon(piece);
        piece.setPosition(squares[x][y]);
    }

    /**
     * Removes an amazon from its old position, and moves it to a new square
     * @param x new square x co-ordinate
     * @param y new square y co-ordinate
     * @param piece piece to be moved
     */
    public void setSquarePiece(int x, int y, Piece piece){

        piece.getPosition().removeAmazon(); // removes amazon from square
        this.squares[x][y].setAmazon(piece); // places amazon in new square
        piece.setPosition(squares[x][y]); // updates new position within piece object
    }

    /**
     * Checks if a square is empty
     * @param square Square to be checked
     * @return true if empty, false otherwise
     */
    public boolean isSquareEmpty(Square square){

        if (square.isBurnt() || square.getAmazon() != null){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets all squares that can be moved to according to amazons rules, from a starting square
     * @param startSquare Square we are starting from
     * @param skipX X co-ordinate that should be skipped
     * @param skipY Y co-ordinate that should be skipped
     * @return ArrayList of squares that can be moved to
     */
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

    /**
     * Used to reset a 6x6 or 10x10 board, placing pieces in starting positions
     * @param p1 First player object, white pieces
     * @param p2 Second player object, black pieces
     */
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

    /**
     * Prints a given square to the console, used for printing the current board object
     * @param x x co-ordinate for the square to be printed
     * @param y y co-ordinate for the square to be printed
     */
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

    /**
     * Prints the current board object to the console, with algebraic notation
     */
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