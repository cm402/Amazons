import java.util.ArrayList;

public class GameValue {

    ArrayList<GameValue> left;
    ArrayList<GameValue> right;

    public GameValue(){

        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
    }

    public String toString(){
        return "{" + this.left.toString() + " | " + this.right.toString() + "}";
    }

    public boolean lessThan(GameValue x, GameValue y){

        for(GameValue xleft: x.left){

            if(lessThan(y, xleft)){
                return false;
            }
        }

        return true;
    }

    public GameValue plus(GameValue x, GameValue y){

        // x.left + y.left

        GameValue newLeft = new GameValue();

        return x;
    }
}
