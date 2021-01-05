import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private Board newBoard(Board board, int startX, int startY, int endX, int endY){

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

                // if any square on old board is burnt or contains a piece, replicate on new board
                if(oldSquare.isBurnt()){

                    newBoard.burnSquare(newX, newY);

                } else if(oldSquare.getAmazon() != null){

                    if (oldSquare.getAmazon().isWhite()){
                        newBoard.addPiece(newX, newY, new Piece(true));
                    } else {
                        newBoard.addPiece(newX, newY, new Piece(false));
                    }

                }

            }
        }

        return newBoard;

    }

    private Board simplify(Board board){

        // first column
        if(isColumnBurnt(board, 0)){

            // if burnt, create a new board, with the first column removed and all other squares shifted left
            board = newBoard(board, 1, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1);
            //board.printBoard();

            // recursively call in case more reductions possible
            return simplify(board);
        }

        // last column
        if(isColumnBurnt(board, board.getColumnBoardSize() - 1)){

            // if burnt, create a new board, with the last column removed and all other squares shifted right
            board = newBoard(board, 0, 0, board.getColumnBoardSize() - 2, board.getRowBoardSize() - 1);
            //board.printBoard();

            return simplify(board);
        }

        // first row
        if (isRowBurnt(board, 0)) {

            board = newBoard(board, 0, 1, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 1);
            //board.printBoard();

            return simplify(board);
        }

        // last row
        if (isRowBurnt(board, board.getRowBoardSize() - 1)) {

            board = newBoard(board, 0, 0, board.getColumnBoardSize() - 1, board.getRowBoardSize() - 2);
            //board.printBoard();

            return simplify(board);
        }

        return board;

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
    private Board rotateBoard(Board board){

        Board newBoard = new Board(board.getRowBoardSize(), board.getColumnBoardSize());
        newBoard.setupBoard();

        int newX = 0;
        int newY = 0;

        for(int x = 0; x < board.getColumnBoardSize(); x++) {

            if (x == 0) {
                newY = 0;
            } else {
                newY++;
            }

            for (int y = board.getRowBoardSize() - 1; y >= 0; y--) {

                if (y == board.getRowBoardSize() - 1) {
                    newX = 0;
                } else {
                    newX++;
                }

                Square oldSquare = board.getSquare(x, y);

                //System.out.println("(" + x + ", " + y + ") ---->" + "(" + newX + ", " + newY + ")");

                // if any square on old board is burnt or contains a piece, replicate on new board
                if (oldSquare.isBurnt()) {

                    newBoard.burnSquare(newX, newY);

                } else if (oldSquare.getAmazon() != null) {

                    if (oldSquare.getAmazon().isWhite()) {

                        newBoard.addPiece(newX, newY, new Piece(true));
                    } else {

                        newBoard.addPiece(newX, newY, new Piece(false));
                    }
                }
            }
        }

        return newBoard;
    }

    // if size() of returned array is 1, the board can't be split
    public ArrayList<Board> split(){

        ArrayList<Board> partitions = new ArrayList<Board>();

        // check if can simplify, by removing outermost burnt rows/columns
        Board board = simplify(this);

        // check if any columns can be split
        for(int x = 1; x < board.getColumnBoardSize() - 2; x++){

            if(isColumnBurnt(this, x)){

                Board board1 = newBoard(this, 0, 0, x - 1, this.getRowBoardSize() - 1);
                Board board2 = newBoard(this, x + 1, 0, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1);

                ArrayList<Board> boards1 = board1.split();
                ArrayList<Board> boards2 = board2.split();

                boards1.addAll(boards2);

                return boards1;

            }
        }

        // check if any rows can be split
        for(int y = 1; y < board.getRowBoardSize() - 2; y++){

            if(isRowBurnt(this, y)){

                Board board1 = newBoard(this, 0, 0, this.getColumnBoardSize() - 1, y - 1);
                Board board2 = newBoard(this, 0, y + 1, this.getColumnBoardSize() - 1, this.getRowBoardSize() - 1);

                ArrayList<Board> boards1 = board1.split();
                ArrayList<Board> boards2 = board2.split();

                boards1.addAll(boards2);

                return boards1;

            }
        }

        // checking bottom and top row for burnt squares
        for(int x = 0; x < board.getColumnBoardSize() - 1; x++){

            // bottom row
            if(board.getSquare(x, 0).isBurnt()){
                //System.out.println(x + " ," + 0);
                //need to see if there is a path of burnt squares from this edge square to another edge square
            }

            // top row
            if(board.getSquare(x, board.getRowBoardSize() - 1).isBurnt()){
                //System.out.println(x + "," + Integer.toString(board.getRowBoardSize() - 1));
            }

        }

        partitions.add(this); // base case, where board can't be split further, add to partitions

        return partitions;

    }

    // finds a path from a burnt edge square to another burnt edge square, via burnt squares
    public boolean isBurntLine(Board board){

        return false;
    }

    @Override
    public boolean equals(Object o) {

        // self check
        if (this == o) {
            return true;
        }

        // first, we must simplify both boards fully
        Board board1 = simplify(this);
        Board board2 = simplify((Board) o);

        // check that both simplified boards are the same size
        if(board1.getColumnBoardSize() != board2.getColumnBoardSize() ||
        board1.getRowBoardSize() != board2.getRowBoardSize()){


            // meaning a rotation of 90 or 270 can show an equality of both boards
            if(board1.getColumnBoardSize() == board2.getRowBoardSize() &&
            board1.getRowBoardSize() == board2.getColumnBoardSize()){

                Board rotatedBoard90 = rotateBoard(board1);
                Board rotatedBoard270 = rotateBoard(rotateBoard(rotatedBoard90));

                return boardsEqual(rotatedBoard90, board2) || boardsEqual(rotatedBoard270, board2);

            }

            return false; // case where the sizes don't match, even after rotating, so 2 board cant be equal
        }

        Board rotatedBoard180 = rotateBoard(rotateBoard(board1));

        return boardsEqual(board1, board2) || boardsEqual(rotatedBoard180, board2);
    }

    public int getColumnBoardSize(){
        return this.columnBoardSize;
    }

    public int getRowBoardSize(){
        return this.rowBoardSize;
    }

    public Square getSquare(int x, int y){
        
        if (x >= columnBoardSize || y >= rowBoardSize || x < 0 || y < 0){
            System.out.println("Square out of range");
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

    public ArrayList<Square> getValidSquares(Square startSquare){

        ArrayList<Square> validSquares = new ArrayList<Square>();

        int startX = startSquare.getX();
        int startY = startSquare.getY();


        for(int x = startX - 1; x >= 0; x--) {

            // checking left
            if (isSquareEmpty(squares[x][startY])) {
                validSquares.add(squares[x][startY]);
            } else {
                break;
            }
        }

        for(int x = startX + 1; x < columnBoardSize; x++) {

            // checking right
            if (isSquareEmpty(squares[x][startY])) {
                validSquares.add(squares[x][startY]);
            } else {
                break;
            }
        }

        for (int y = startY + 1; y < rowBoardSize; y++) {
            // checking up
            if(isSquareEmpty(squares[startX][y])){
                validSquares.add(squares[startX][y]);
            } else {
                break;
            }
        }


        for(int y = startY - 1; y >= 0; y--) {
            // checking down
            if(isSquareEmpty(squares[startX][y])){
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
                    if (isSquareEmpty(squares[x][y])) {
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
                    if (isSquareEmpty(squares[x][y])) {
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
                    if (isSquareEmpty(squares[x][y])) {
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
                    if (isSquareEmpty(squares[x][y])) {
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

        for(int i = 0; i < (columnBoardSize * 4) + 1; i++){
            rowLine.append("-");
        }

        for(int row = rowBoardSize - 1; row >= 0; row--){
            System.out.println("");
            System.out.println(rowLine);

            for (int column = 0; column < columnBoardSize; column++){
                printSquare(column, row);
            }
            System.out.print("|");
        }
        System.out.println("");
        System.out.println(rowLine);
    }

}