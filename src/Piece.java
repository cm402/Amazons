import java.io.Serializable;

/**
 * Represents a piece in the Amazons game, storing
 * both the colour and the position on the board
 * that the piece is currently located.
 */
public class Piece implements Serializable {

    private boolean white;
    private Square position;

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square square){
        this.position = square;
    }

    public Piece(boolean white){
        this.setWhite(white);
    }

    public boolean isWhite(){
        return this.white;
    }

    public void setWhite(boolean white){
        this.white = white;
    }

}