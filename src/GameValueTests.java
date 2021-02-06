public class GameValueTests {

    GameValue zero = new GameValue();
    GameValue one = new GameValue();
    GameValue half = new GameValue();
    GameValue star = new GameValue();
    GameValue quarter = new GameValue();
    GameValue minusOne = new GameValue();

    public GameValueTests(){

        one.left.add(zero);

        half.left.add(zero);
        half.right.add(one);

        star.left.add(zero);
        star.right.add(zero);

        quarter.left.add(zero);
        quarter.right.add(half);

        minusOne.right.add(zero);

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
}