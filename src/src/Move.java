import java.io.Serializable;

public class Move implements Serializable {

    private Player player;
    private Square startPosition;
    private Square endPosition;
    private Piece pieceUsed;
    private Square squareBurned;

    public Move(Player player, Square start, Square end, Square squareBurned){

        this.player = player;
        this.startPosition = start;
        this.endPosition = end;
        this.pieceUsed = start.getAmazon();
        this.squareBurned = squareBurned;
    }

    public String toString(){
        return "(" + startPosition.getX() + ", " + startPosition.getY() + ") -> ("
                + endPosition.getX() + ", " + endPosition.getY() + ")" +
                " and arrow shot at (" + squareBurned.getX() + ", " + squareBurned.getY() + ")";
    }

    public Player getPlayer(){
        return this.player;
    }

    public Square getStartPosition(){
        return this.startPosition;
    }

    public Square getEndPosition(){
        return this.endPosition;
    }

    public Piece getPiece(){
        return this.pieceUsed;
    }

    public Square getBurnedSquare(){
        return this.squareBurned;
    }

}