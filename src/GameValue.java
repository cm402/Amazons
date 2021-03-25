import java.util.ArrayList;
import java.util.*;
import java.io.Serializable;

/**
 * Represents the value of a particular position in
 * a game. In the context of Amazons, each GameValue
 * stores a list of move options for both black and
 * white, left and right respectively, for a given
 * Board object.
 */
public class GameValue implements Serializable{

    ArrayList<GameValue> left; // List of move options for black
    ArrayList<GameValue> right; // List of move options for white
    Move move; // The move associated with this GameValue
    boolean simplified; // Indicates if this GameValue has already been simplified

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

    /**
     * Invert a GameValue object, swapping left and rights options recursively
     */
    public void invert(){

        // Swapping left and right
        final ArrayList<GameValue> temp = this.right;
        this.right = this.left;
        this.left = temp;

        for(GameValue leftGV: this.left){

            leftGV.invert();
        }

        for(GameValue rightGV: this.right){

            rightGV.invert();
        }

    }

    /**
     * Checks if "this" GameValue object is stored in a list of GameValue objects
     * Note- Mutually-Recursive with equals()
     * @param gameValues List of GameValue objects
     * @return true if "this" object is in list, false otherwise
     */
    public boolean isIn(ArrayList<GameValue> gameValues){

        for(GameValue gameValue: gameValues){

            if(gameValue.equals(this)){
                return true;
            }

        }
        return false;
    }

    /**
     * Checking the equality of "this" GameValue, with another.
     * This relates to the toString() content of both, and accepts
     * lists on the left and right side which are not in the same order.
     * The Moves stored also don't matter.
     * @param game GameValue object to compare "this" object with
     * @return true if both GameValue objects are the same, false otherwise
     */
    public boolean equals(GameValue game){

        // 1. check sizes match for both GameValue objects
        if(this.left.size() != game.left.size()
            || this.right.size() != game.right.size()){

            return false;
        }

        // 2. if either side is a single GameValue object, check that
        // its toString() notation matches the other game
        if(this.left.size() == 1) {

            if (!this.left.toString().equals(game.left.toString())) {
                return false;
            }
        }

        if(this.right.size() == 1){

            if(!this.right.toString().equals(game.right.toString())){
                return false;
            }
        }

        // 3. if either side is an list of GameValue objects, check
        // that the lists are equal, via ensuring that each GameValue
        // object in the first list is found in the second list

        if(this.left.size() > 1){

            for(GameValue gameValue: this.left){

                if(!gameValue.isIn(game.left)){
                    return false;
                }
            }

        }

        if(this.right.size() > 1){

            for(GameValue gameValue: this.right){

                if(!gameValue.isIn(game.right)){
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Finding the position of a GameValue string representation, in a
     * list of GameValue objects.
     * Helper method for toString()
     * @param gameValues list of GameValues to search
     * @param value GameValue string
     * @return index of GameValue, or -1 of not found in list
     */
    public int findPosition(ArrayList<GameValue> gameValues, String value){

        int counter = 0;

        for(GameValue gameValue: gameValues){

            if(gameValue.toString().equals(value)){
                return counter;
            }

            counter++;
        }
        return -1;
    }

    /**
     * Finding the maximum recursive depth of possible moves, for a given side.
     * Algorithm adapted from:
     * https://www.educative.io/edpresso/finding-the-maximum-depth-of-a-binary-tree
     * @param side Side to search
     * @param game
     * @return Maximum depth for the side chosen
     */
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

    /**
     * Getting the simplest form of a fraction,
     * where top and bottom cannot be any smaller,
     * while still being whole numbers.
     * @param a
     * @param b
     * @return
     */
    public double getSimplestForm(double a, double b){

        if(Math.ceil(a) < b){
            return Math.ceil(a);
        } else {
            return 0.5 * getSimplestForm(2*a, 2*b);
        }
    }

    /**
     * Checks if a string contains a number.
     * @param strNum String to check
     * @return true if string is numeric, false otherwise
     */
    public static boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }

        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checking if the left and right sides of a GameValue are a "simple" fraction
     * @param left Numerical representation of left side
     * @param right Numerical representation of right side
     * @return true if GameValue is a simple fraction, false otherwise
     */
    public static boolean isSimpleFraction(double left, double right){

        // 1. Find the difference between the left and right values
        double difference = Math.abs(right - left);

        // 2. If the difference is less than 1, then get the inverse, otherwise return false
        if(difference > 1){
            return false;
        }

        // 3. Check if the inverse is a power of 2
        double meanDifference = difference / 2;

        // get inverse so we can manipulate the denominator more easily
        double inverse = Math.pow(meanDifference, -1);

        // use ceiling of value so it will always round up
        int inverseInt = (int) Math.ceil(inverse);

        // returns if a value is a positive power of 2
        return inverseInt > 0 && ((inverseInt & (inverseInt - 1)) == 0);
    }

    /**
     * Getting the human-readable form of "this" GameValue object.
     * Using the same notation as "Winning Ways".
     * Positive Values indicate a number of moves for left (black).
     * Negative Values indicate a number of moves for right (white).
     * Special notations include:
     * * = < 0 | 0 >
     * -n* = < -n | -n >
     * ± n = < n | -n >
     * @return String form of "this" GameValue
     */
    public String toString() {

        if (left.isEmpty() && right.isEmpty()) {

            return "0";

        } else if (left.isEmpty()) {

            int max = maxDepth("right", this);
            return "-" + Integer.toString(max);

        } else if (right.isEmpty()) {

            int max = maxDepth("left", this);
            return Integer.toString(max);

        } else {

            String leftSide = left.toString().replace("[", "").replace("]", "");
            String rightSide = right.toString().replace("[", "").replace("]", "");

            // Both sides are single, numerical values
            if (isNumeric(leftSide) && isNumeric(rightSide)) {

                double leftValue = Double.parseDouble(leftSide);
                double rightValue = Double.parseDouble(rightSide);

                // < 0 | 0 > = *
                if (leftValue == 0 && rightValue == 0) {
                    return "*";
                }

                if(isSimpleFraction(leftValue, rightValue)) {
                    return String.valueOf((leftValue + rightValue) / 2);
                }

                // TODO- check if this should come first, or if statement above
                if(0 < leftValue && leftValue < rightValue){
                    return Double.toString(getSimplestForm(leftValue, rightValue));
                }

                // < -n | -n > = -n*
                if (leftValue == rightValue) {
                    return leftSide + "*";
                }

                // < a | b > = 0, where a < 0 and b > 0
                if (leftValue < 0 && rightValue > 0) {
                    return "0";
                }

                // switch games, {x|y}, x & y are numbers and x >= y, chapter 5 of "Winning Ways"
                // both players keen to move first as these are "hot" positions
                if (leftValue >= rightValue) {

                    // < n | -n > = ± n (page 123 winning ways)
                    if (leftValue == Math.abs(rightValue)) {
                        return "\u00B1" + leftSide;
                    }
                }

            }

            // Neither player has a winning move, so its a "zero" position
            if (leftSide.equals("*") && rightSide.equals("*")) {
                return "0";
            }

            // If both "*" and "0" positions are options,
            // remove the "*" as it is fuzzy with "0"
            if (findPosition(left, "*") != -1 &&
                    findPosition(left, "0") != -1) {

                left.remove(findPosition(left, "*"));
            }

            if (findPosition(right, "*") != -1 &&
                    findPosition(right, "0") != -1) {

                right.remove(findPosition(right, "*"));
            }

            return "<" + this.left.toString().replace("[", " ").replace("]", " ")
                    + " | " + this.right.toString().replace("[", " ").replace("]", " ") + ">";
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

    /**
     * Finding all duplicate GameValues, in a list of GameValue objects
     * @param gameValues list of GameValues to search
     * @return A list of the duplicate GameValues
     */
    public ArrayList<GameValue> findDuplicates(ArrayList<GameValue> gameValues){

        ArrayList<GameValue> duplicates = new ArrayList<GameValue>();

        for(int i = 0; i < gameValues.size(); i++){
            for(int j = i + 1; j < gameValues.size(); j++){

                if(gameValues.get(i).toString().equals(gameValues.get(j).toString())){
                    duplicates.add(gameValues.get(i));

                } else if(gameValues.get(i).equals(gameValues.get(j))){
                    duplicates.add(gameValues.get(i));
                }
            }

        }
        return duplicates;
    }

    // Removes the GameValue objects that are dominated by others, as well as duplicates
    // { A, B, C : D, E, F }
    // if A > B > C, remove B & C
    public void simplify(){

        // if "this" already simplified, we just return
        if(this.simplified){
            return;
        }

        GameValue maxLeft = findMax(this.left, "left");
        ArrayList<GameValue> toRemove = new ArrayList<GameValue>();

        // 1. finding dominated GameValue objects to remove
        if(maxLeft != null){

            // G > H means G is better for left, so remove H
            for(GameValue leftGame: this.left){

                if(compare(maxLeft, leftGame).equals("x")){
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
                    toRemove.add(rightGame);
                }

            }

            for(GameValue remove: toRemove) {
                this.right.remove(remove);
            }

        }

        // 2. Recursively simplifying children GameValue objects
        for(GameValue leftGame: this.left){
            leftGame.simplify();
        }

        for(GameValue rightGame: this.right){
            rightGame.simplify();
        }

        // 3. finding duplicated GameValue objects to remove
        // Must be done last, after all other simplifications
        toRemove = findDuplicates(this.left);

        for(GameValue remove: toRemove){
            this.left.remove(remove);
        }

        toRemove = findDuplicates(this.right);

        for(GameValue remove: toRemove){
            this.right.remove(remove);
        }

        this.simplified = true;


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
            newRight.add(plus(xr, y));
        }

        for(GameValue yr: y.right){
            newRight.add(plus(x, yr));
        }

        gameValue.left = newLeft;
        gameValue.right = newRight;

        return gameValue;
    }
}
