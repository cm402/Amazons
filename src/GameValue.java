import java.util.ArrayList;
import java.util.*;

public class GameValue {

    ArrayList<GameValue> left;
    ArrayList<GameValue> right;
    Move move;
    boolean simplified;

    public GameValue(){

        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.move = null;
        simplified = false;
    }

    public GameValue(Move move){

        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.move = move;
        simplified = false;
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
        } else if(left.toString().equals("[0]") && right.toString().equals("[0]")){
            return "*";
        } else {
            return "{" + this.left.toString() + " | " + this.right.toString() + "}";
        }

    }

    // Given 2 GameValue Objects, X & Y, returns true if X <= Y
    // Definition from winning ways book:
    // G >= H means that G + (-H) >= 0
    // or X <= Y unless some Y <= XL or some YR <= X
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

    // Given a GameValue object, describes which player has a winning strategy
    // Can be "Left", "Right", "First" or "Second"

    public String getOutcomeClass(){

        // 0 = { : }
        GameValue zero = new GameValue();

        if(lessThanOrEqualTo(this, zero)){

            // x <= 0 and 0 <= 0 only true when x = 0
            // This is a zero position
            if(lessThanOrEqualTo(zero, this)){

                return "Second";

            // meaning x < 0, game is negative
            } else {

                return "Right";
            }
        }

        if(lessThanOrEqualTo(zero, this)){

            // meaning x > 0, this is a positive game
            if (!lessThanOrEqualTo(this, zero)) { // is this needed?

                return "Left";
            }
        }

        // x || 0 means the position is fuzzy
        // Meaning we can't specify a clear winner
        // and so the first player can always win
        return "First";
    }

    // Compares 2 GameValue objects to see which is better for left
    // X > Y
    public String compare(GameValue x, GameValue y){

        if(lessThanOrEqualTo(x, y)){

            // x and y are equally favourable for left
            if(lessThanOrEqualTo(y, x)){
                return "Equal";

            // x is worse than y for left
            } else {
                return "y";
            }
        }

        if(lessThanOrEqualTo(y, x)){

            // x is better than y for left
            if(!lessThanOrEqualTo(x, y)){ // is this check needed?
                return "x";
            }
        }

        return "Fuzzy";

    }

    // Given an ArrayList of GameValues, find the one that dominates the most
    // of the other GameValue objects
    public GameValue findMax(ArrayList<GameValue> games, String side){

        if(games.isEmpty()){
            return null;
        }

        GameValue max = games.get(0);

        if(side.equals("left")){

            for(GameValue game: games){
                if(compare(game, max).equals("x")){
                    max = game;
                }

            }

        } else {

            for(GameValue game: games){
                if(compare(game, max).equals("y")){
                    max = game;
                }

            }
        }

        return max;

    }

    // Removes the GameValue objects that are dominated by others
    // { A, B, C : D, E, F }
    // if A > B > C, remove B & C
    public void simplify(){

        // if "this" already simplified, we just return
        if(this.simplified){
            return;
        }

        GameValue maxLeft = findMax(this.left, "left");
        ArrayList<GameValue> toRemove = new ArrayList<GameValue>();

        if(maxLeft != null){

            // G > H means G is better for left, so remove H
            for(GameValue leftGame: this.left){

                if(compare(maxLeft, leftGame).equals("x")){
                    //this.left.remove(leftGame);
                    toRemove.add(leftGame);
                }
            }

            for(GameValue remove: toRemove){
                this.left.remove(remove);
            }
        }

        toRemove.clear();

        GameValue maxRight = findMax(this.right, "right");

        if(maxRight != null){

            for(GameValue rightGame: this.right){

                if(compare(maxRight, rightGame).equals("y")){
                    //this.right.remove(rightGame);
                    toRemove.add(rightGame);
                }

            }

            for(GameValue remove: toRemove) {
                this.right.remove(remove);
            }

        }


        this.simplified = true;

        for(GameValue leftGame: this.left){
            leftGame.simplify();
        }

        for(GameValue rightGame: this.right){
            rightGame.simplify();
        }


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
