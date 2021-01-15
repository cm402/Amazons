import java.util.ArrayList;
import java.util.*;

public class GameValue {

    ArrayList<GameValue> left;
    ArrayList<GameValue> right;

    public GameValue(){

        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
    }

    // returns maximum depth of possible moves for a given side
    // side false = left, true = right
    // algorithm adapted from: https://www.educative.io/edpresso/finding-the-maximum-depth-of-a-binary-tree
    public int maxDepth(String side, GameValue game){

        if (side.equals("left")) {

            if(game.left.isEmpty()){
                return 0;
            }

            ArrayList<Integer> leftGames = new ArrayList<>();

            for(GameValue leftGame: game.left){

                leftGames.add(maxDepth("left", leftGame));
            }

            return Collections.max(leftGames) + 1;

        } else {

            if(game.right.isEmpty()){
                return 0;
            }

            ArrayList<Integer> rightGames = new ArrayList<>();

            for(GameValue rightGame: game.right){

                rightGames.add(maxDepth("right", rightGame));
            }

            return Collections.max(rightGames) + 1;

        }

    }

    public String toString(){

        if(left.isEmpty() && right.isEmpty()){
            return "0";
        } else if(left.isEmpty()){

            int max = maxDepth("right", this);
            return "-" + Integer.toString(max);
        } else if(right.isEmpty()){

            int max = maxDepth("left", this);
            return Integer.toString(max);
        } else {
            return "{" + this.left.toString() + " | " + this.right.toString() + "}";
        }

    }

    // Given 2 GameValue Objects, X & Y, returns true if X <= Y
    // Definition from winning ways book:
    // X <= Y unless some Y <= XL or some YR <= X
    public boolean lessThanOrEqualTo(GameValue x, GameValue y){

        for(GameValue xleft: x.left){

            if(lessThanOrEqualTo(y, xleft)){
                return false;
            }
        }

        for(GameValue yRight: y.right){

            if(lessThanOrEqualTo(yRight, x)){
                return false;
            }
        }

        return true;
    }

    // Definition from winning ways book:
    // X + Y = { XL + Y, X + YL | XR + Y, X + YR }
    public GameValue plus(GameValue x, GameValue y){

        GameValue gameValue = new GameValue();

        ArrayList<GameValue> newLeft = new ArrayList<>();

        for(GameValue xl: x.left){
            newLeft.add(plus(xl, y));
        }

        for(GameValue yl: y.left){
            newLeft.add(plus(x, yl));
        }

        ArrayList<GameValue> newRight = new ArrayList<>();

        for(GameValue xr: x.right){
            newLeft.add(plus(xr, y));
        }

        for(GameValue yr: y.right){
            newLeft.add(plus(x, yr));
        }

        gameValue.left = newLeft;
        gameValue.right = newRight;

        return gameValue;
    }
}
