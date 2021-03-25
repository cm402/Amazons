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
     * Testing that < 5 | -2 > inverted equals < 2 | -5 >
     */
    @Test
    public void invertGameValue(){

        GameValue five = getValue(5);
        GameValue negativeTwo = getValue(-2);
        GameValue fiveNegativeTwo = new GameValue();
        fiveNegativeTwo.left.add(five);
        fiveNegativeTwo.right.add(negativeTwo);

        fiveNegativeTwo.invert();

        GameValue negativeFive = getValue(-5);
        GameValue two = getValue(2);
        GameValue twoNegativeFive = new GameValue();

        twoNegativeFive.left.add(two);
        twoNegativeFive.right.add(negativeFive);

        assertTrue(fiveNegativeTwo.equals(twoNegativeFive));
    }

    // This clearly simplifies to 0.25, and this
    // test shows this, using simplify() and
    // then the equals() method.
    public void testSimplify(){

        // game 1 = < 0 | < *, 0 | 1 >, 0.5 >
        // game 2 = < 0 | 0.5 >

        GameValue game1_1 = new GameValue();
        game1_1.left.add(star);
        game1_1.left.add(zero);
        game1_1.right.add(one);

        GameValue game1 = new GameValue();
        game1.left.add(zero);
        game1.right.add(game1_1);
        game1.right.add(half);

        game1.simplify();

        System.out.println(game1.toString());
        System.out.println(quarter);

        System.out.println(game1.equals(quarter));

    }

    // This test shows that equals() will return true
    // when 2 games have the same objects on one side,
    // but in a different order.
    public void testGameValueEquals1() {

        // game 1 = < 0  |  < 1  |  * >, 0.5 >
        // game 2 = < 0  |  0.5, < 1  |  * > >

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
        game2.right.add(game1_1);

        System.out.println(game1.toString());
        System.out.println(game2.toString());

        System.out.println(game1.equals(game2));
    }

    public void testGameValueEquals2() {

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
        game2.right.add(zero);

        System.out.println(game1.toString());
        System.out.println(game2.toString());

        System.out.println(game1.equals(game2));
    }

    public void testGameValueEquals3() {

        // true
        System.out.println(star.toString() + " = " + star.toString() + ", " + star.equals(star));

        // false
        System.out.println(star.toString() + " = " + zero.toString() + ", " + star.equals(zero));

        // false
        System.out.println(one.toString() + " = " + minusOne.toString() + ", " + one.equals(minusOne));

        // false
        System.out.println(one.toString() + " = " + zero.toString() + ", " + one.equals(zero));

        // false
        System.out.println(quarter.toString() + " = " + half.toString() + ", " + quarter.equals(half));

        // true
        System.out.println(zero.toString() + " = " + zero.toString() + ", " + zero.equals(zero));

    }

    public void testIsSimpleFraction(){

        GameValue gameValue = new GameValue();

        // true (1/2)
        System.out.println(gameValue.isSimpleFraction(0, 1));

        // true (1/4)
        System.out.println(gameValue.isSimpleFraction(0, 0.5));

        // true (1/8)
        System.out.println(gameValue.isSimpleFraction(0, 0.25));

        // true (1/8)
        System.out.println(gameValue.isSimpleFraction(-1.75, -1.5));

        // false (3/8)
        System.out.println(gameValue.isSimpleFraction(1.25, 2));

        // true (-1 1/2)
        System.out.println(gameValue.isSimpleFraction(-2, -1));
    }

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

        System.out.println("The simplest form of < 2.375 | 5 > is " + test.toString());

        GameValue test2 = new GameValue();
        test2.left.add(twoAndHalf);
        test2.right.add(three);

        System.out.println("The simplest form of < 2.5 | 3 > is " + test.toString());
    }

    /**
     * Testing a board which is "fuzzy" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassFuzzy(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate(null);
        assertEquals(gameValue.getOutcomeClass(), "First");
    }

    /**
     * Testing a board which is "Positive" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassPositive(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate(null);
        assertEquals(gameValue.getOutcomeClass(), "Left");
    }

    /**
     * Testing a board which is "Negative" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassNegative(){

        Board board1 = new Board(3, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(0,0));
        board1.addPiece(0, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate(null);
        assertEquals(gameValue.getOutcomeClass(), "Right");
    }

    /**
     * Testing a board which is "Zero" produces the correct outcome class
     */
    @Test
    public void testOutcomeClassZero(){

        Board board1 = new Board(4, 1);
        board1.setupBoard();

        ArrayList<Piece> blackPieces = new ArrayList<Piece>();
        blackPieces.add(new Piece(false));
        blackPieces.get(0).setPosition(board1.getSquare(1,0));
        board1.addPiece(1, 0, blackPieces.get(0));

        ArrayList<Piece> whitePieces = new ArrayList<Piece>();
        whitePieces.add(new Piece(true));
        whitePieces.get(0).setPosition(board1.getSquare(2,0));
        board1.addPiece(2, 0, whitePieces.get(0));

        GameValue gameValue = board1.evaluate(null);
        assertEquals(gameValue.getOutcomeClass(), "Second");
    }
}
