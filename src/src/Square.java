import java.io.Serializable;

public class Square implements Serializable {

    private Piece amazon;
    private boolean burnt;
    private int x, y;

    public Square(int x, int y, Piece amazon, boolean burnt) {
        this.amazon = amazon;
        this.burnt = burnt;
        this.x = x;
        this.y = y;
    }

    public Piece getAmazon(){
        return this.amazon;
    }

    public void setAmazon(Piece amazon){
        this.amazon = amazon;
    }

    public boolean isBurnt(){
        return this.burnt;
    }

    public void burnSquare(){
        this.burnt = true;
    }

    public void removeAmazon(){ this.amazon = null;}

    public int getX(){
        return this.x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public void setY(int y){
        this.y = y;
    }

    

}
  
