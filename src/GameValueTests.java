import org.junit.Test;
import org.junit.Before;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Unit tests used throughout development of the
 * GameValue class, allowing for testing of its methods.
 */
public class GameValueTests {

    GameValue zero = new GameValue();
    GameValue one = new GameValue();
    GameValue half = new GameValue();
    GameValue star = new GameValue();
    GameValue quarter = new GameValue();
    GameValue minusOne = new GameValue();

    /**
     * Returns a GameValue object representation of a numeric value
     * @param value value to translate into a GameValue
     * @return GameValue representation of value
     */
    public GameValue getValue(int value){

        GameValue newGameValue = new GameValue();
        GameValue oldGameValue = new GameValue();

        while(value != 0){

            newGameValue = new GameValue();

            if(value > 0){
                newGameValue.left.add(oldGameValue);
                value--;
            } else {
                newGameValue.right.add(oldGameValue);
                value++;
            }

            oldGameValue = newGameValue;
        }

        return newGameValue;
    }

    /**
     * A set of GameValues that are used throughout the unit tests
     */
    @Before
    public void init(){

        one.left.add(zero);

        half.left.add(zero);
        half.right.add(one);

        star.left.add(zero);
        star.right.add(zero);

        quarter.left.add(zero);
        quarter.right.add(half);

        minusOne.right.add(zero);
    }

    /**
     * Testing that < 5 | -2 > is deep-copied
     */
    @Test
    public void deepCopy(){

        GameValue five = getValue(5);
        GameValue negativeTwo = getValue(-2);
        GameValue fiveNegativeTwo = new GameValue();
        fiveNegativeTwo.left.add(five);
        fiveNegativeTwo.right.add(negativeTwo);

        GameValue copy = fiveNegativeTwo.deepCopy();

        assertTrue(fiveNegativeTwo.equals(copy));

        copy.left.add(star);

        assertFalse(fiveNegativeTwo.equals(copy));
    }

    /**
     * Testing that < 5 | -2 > inverted equals < 2 | -5 >
     */
    @Test
    public void invertGameValue(){

        GameValue five = getValue(5);
        GameValue negativeTwo = getValue(-2);
        GameValue fiveNegativeTwo = new GameValue();
        fiveNegativeTwo.left.add(five);
        fiveNegativeTwo.right.add(negativeTwo);

        GameValue negativeFive = getValue(-5);
        GameValue two = getValue(2);
        GameValue twoNegativeFive = new GameValue();
        twoNegativeFive.left.add(two);
        twoNegativeFive.right.add(negativeFive);

        assertFalse(fiveNegativeTwo.equals(twoNegativeFive));

        fiveNegativeTwo.invert();

        assertTrue(fiveNegativeTwo.equals(twoNegativeFive));
    }

    /**
     * Testing simplify() removes redundant "0.5" GameValues,
     * as well as toString() simplifies < *, 0 | 1 > to 0.5
     */
    @Test
    public void testSimplify(){

        GameValue game1_1 = new GameValue();
        game1_1.left.add(star);
        game1_1.left.add(zero);
        game1_1.right.add(one);

        GameValue game1 = new GameValue();
        game1.left.add(zero);
        game1.right.add(game1_1);
        game1.right.add(half);

        assertFalse(game1.equals(quarter));

        game1.simplify();

        assertTrue(game1.equals(quarter));
    }

    /**
     * Testing the Equals method with a variety of
     * different GameValues
     */
    @Test
    public void testGameValueEquals() {

        assertTrue(star.equals(star));
        assertFalse(star.equals(zero));
        assertFalse(star.equals(minusOne));
        assertFalse(one.equals(zero));
        assertFalse(quarter.equals(half));
        assertTrue(zero.equals(zero));
    }

    /**
     * Testing equals works with lists that are not
     * in the same order.
     * game 1 = < 0  |  < 1  |  * >, 0.5 >
     * game 2 = < 0  |  0.5, < 1  |  * > >
     */
    @Test
    public void testEqualsOutOfOrder() {

        GameValue game1_1 = new GameValue();
        game1_1.left.add(one);
        game1_1.right.add(star);

        GameValue game1 = new GameValue();
        game1.left.add(zero);
        game1.right.add(game1_1);
        game1.right.add(half);

        GameValue game2 = new GameValue();
        game2.left.add(zero);
        game2.right.add(half);

        assertFalse(game1.equals(game2));

        game2.right.add(game1_1);

        assertTrue(game1.equals(game2));
    }

    /**
     * Testing equals works with lists that are not
     * in the same order, at a greater depth.
     * game 1 = < 0.5, < 0.5, 1  |  * >  |  0 >
     * game 2 = < < 1, 0.5  |  * >, 0.5  |  0 >
     */
    @Test
    public void testEqualsOutOfOrder2() {

        GameValue game1_1 = new GameValue();
        game1_1.left.add(half);
        game1_1.left.add(one);
        game1_1.right.add(star);

        GameValue game1 = new GameValue();
        game1.left.add(half);
        game1.left.add(game1_1);
        game1.right.add(zero);

        GameValue game2_1 = new GameValue();
        game2_1.left.add(one);
        game2_1.left.add(half);
        game2_1.right.add(star);

        GameValue game2 = new GameValue();
        game2.left.add(game2_1);
        game2.left.add(half);

        assertFalse(game1.equals(game2));

        game2.right.add(zero);

        assertTrue(game1.equals(game2));
    }

    /**
     * Testing the method to check the
     * isSimpleFraction() method works correctly.
     */
    @Test
    public void testIsSimpleFraction(){

        GameValue gameValue = new GameValue();

        // 1/2
        assertTrue(gameValue.isSimpleFraction(0, 1));

        // 1/4
        assertTrue(gameValue.isSimpleFraction(0, 0.5));

        // 1/8
        assertTrue(gameValue.isSimpleFraction(0, 0.25));

        // 1/4
        assertTrue(gameValue.isSimpleFraction(-1.75, -1.5));

        // 3/8
        assertFalse(gameValue.isSimpleFraction(1.25, 2));

        // 1/2
        assertTrue(gameValue.isSimpleFraction(-2, -1));
    }

    /**
     * Testing the getSimplestForm method, which
     * returns the simplest number that fits, according
     * to the "Simplicity Rule" from chapter 2 of 'Winning Ways".
     * < 2.375 | 5 >  = 3, example used in the book.
     */
    @Test
    public void testGetSimplestForm(){

        GameValue five = getValue(5);

        GameValue two = getValue(2);
        GameValue three = getValue(3);

        GameValue twoAndHalf = new GameValue();
        twoAndHalf.left.add(two);
        twoAndHalf.right.add(three);

        GameValue twoAndQuarter = new GameValue();
        twoAndQuarter.left.add(two);
        twoAndQuarter.right.add(twoAndHalf);

        GameValue twoAndThreeEighths = new GameValue();
        twoAndThreeEighths.left.add(twoAndQuarter);
        twoAndThreeEighths.right.add(twoAndHalf);

        GameValue test = new GameValue();
        test.left.add(twoAndThreeEighths);
        test.right.add(five);

        assertTrue(test.toString().equals("3.0"));
    }

    /**
     * Testing a board which is "fuzzy" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassFuzzy(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate();
        assertEquals(gameValue.getOutcomeClass(), "First");
    }

    /**
     * Testing a board which is "Positive" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassPositive(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate();
        assertEquals(gameValue.getOutcomeClass(), "Left");
    }

    /**
     * Testing a board which is "Negative" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassNegative(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate();
        assertEquals(gameValue.getOutcomeClass(), "Right");
    }

    /**
     * Testing a board which is "Zero" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassZero(){

        Board board1 = new Board(4, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate();
        assertEquals(gameValue.getOutcomeClass(), "Second");
    }
}
