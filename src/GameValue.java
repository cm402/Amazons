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
