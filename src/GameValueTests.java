public class GameValueTests {

    GameValue zero = new GameValue();
    GameValue one = new GameValue();
    GameValue half = new GameValue();
    GameValue star = new GameValue();

    public GameValueTests(){

        one.left.add(zero);

        half.left.add(zero);
        half.right.add(one);

        star.left.add(zero);
        star.right.add(zero);

    }

    public void testGameValueEquals() {

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

    public void testIsSimpleFraction(){

        GameValue gameValue = new GameValue();

        System.out.println();

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
}
