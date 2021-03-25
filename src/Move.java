import java.io.Serializable;

/**
 * Represents a move that is played in the Amazons game,
 * containing information about the player who played the
 * move, as well as the starting square, finishing square,
 * square that was shot, and the piece that was used.
 */
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

    /**
     * Translates coordinates into their algebraic notation version.
     * @param x X co-ordinate
     * @param y Y co-ordinate
     * @return Algebraic notation version of co-ordinates
     */
    private String toAlgebraic(int x, int y){

        x += 97;
        char letter = (char) x;
        return letter + "" + y;
    }

    public String toString(){
        return toAlgebraic(startPosition.getX(), startPosition.getY()) + " -> "
                + toAlgebraic(endPosition.getX(), endPosition.getY()) +
                " and arrow shot at " + toAlgebraic(squareBurned.getX(), squareBurned.getY());
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