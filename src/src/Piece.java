import java.io.Serializable;

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