import java.util.ArrayList;

public class BoardPartitionSetup {

    private int noOfColumns, noOfRows;

    private ArrayList<Integer> xBurntSquareCoordinates;
    private ArrayList<Integer> yBurntSquareCoordinates;

    private ArrayList<Integer> xWhitePieceCoordinates;
    private ArrayList<Integer> yWhitePieceCoordinates;

    private ArrayList<Integer> xBlackPieceCoordinates;
    private ArrayList<Integer> yBlackPieceCoordinates;

    public BoardPartitionSetup(){

        this.xBurntSquareCoordinates = new ArrayList<Integer>();
        this.yBurntSquareCoordinates = new ArrayList<Integer>();

        this.xWhitePieceCoordinates = new ArrayList<Integer>();
        this.yWhitePieceCoordinates = new ArrayList<Integer>();

        this.xBlackPieceCoordinates = new ArrayList<Integer>();
        this.yBlackPieceCoordinates = new ArrayList<Integer>();
    }

    // getters and setters

    public int getNoOfColumns() {
        return this.noOfColumns;
    }

    public void setNoOfColumns(int noOfColumns) {
        this.noOfColumns = noOfColumns;
    }

    public int getNoOfRows() {
        return this.noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public ArrayList<Integer> getXBurntSquareCoordinates() {
        return this.xBurntSquareCoordinates;
    }

    public void setXBurntSquareCoordinates(ArrayList<Integer> xBurntSquareCoordinates){
        this.xBurntSquareCoordinates = xBurntSquareCoordinates;
    }

    public ArrayList<Integer> getYBurntSquareCoordinates() {
        return this.yBurntSquareCoordinates;
    }

    public void setYBurntSquareCoordinates(ArrayList<Integer> yBurntSquareCoordinates){
        this.yBurntSquareCoordinates = yBurntSquareCoordinates;
    }

    public ArrayList<Integer> getXWhitePieceCoordinates() {
        return this.xWhitePieceCoordinates;
    }

    public void setXWhitePieceCoordinates(ArrayList<Integer> xWhitePieceCoordinates){
        this.xWhitePieceCoordinates = xWhitePieceCoordinates;
    }

    public ArrayList<Integer> getYWhitePieceCoordinates() {
        return this.yWhitePieceCoordinates;
    }

    public void setYWhitePieceCoordinates(ArrayList<Integer> yWhitePieceCoordinates){
        this.yWhitePieceCoordinates = yWhitePieceCoordinates;
    }

    public ArrayList<Integer> getXBlackPieceCoordinates() {
        return this.xBlackPieceCoordinates;
    }

    public void setXBlackPieceCoordinates(ArrayList<Integer> xBlackPieceCoordinates){
        this.xBlackPieceCoordinates = xBlackPieceCoordinates;
    }

    public ArrayList<Integer> getYBlackPieceCoordinates() {
        return this.yBlackPieceCoordinates;
    }

    public void setYBlackPieceCoordinates(ArrayList<Integer> yBlackPieceCoordinates){
        this.yBlackPieceCoordinates = yBlackPieceCoordinates;
    }



}
