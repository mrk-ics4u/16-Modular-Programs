/*
 * Name:        Lesson 16: Modular Programs (Dice.java)
 * Description: A helper class: only static methods and a constant, no
 *              attributes. Any program that needs dice rolls can use it.
 *              Used by ModularDemo.java.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */

/**
 * Dice rolls for a game. Every method is static, so there's no Dice
 * object: call them on the class name, like Math.
 *
 * @author Mr Kowalczewski
 */
public class Dice {

    /** The number of sides on a standard die. */
    public static final int STANDARD_SIDES = 6;

    /**
     * Nobody needs a Dice object, so the constructor is private.
     * new Dice() doesn't compile outside this file.
     */
    private Dice() {
    }

    /**
     * Rolls one die.
     *
     * Precondition: sides >= 1.
     *
     * @param sides the number of sides on the die
     * @return a whole number from 1 to sides
     */
    public static int roll(int sides) {
        return (int) (Math.random() * sides) + 1;
    }

    /**
     * Rolls several dice and adds them up.
     *
     * Precondition: count >= 0 and sides >= 1.
     *
     * @param count how many dice to roll
     * @param sides the number of sides on each die
     * @return the total of all the rolls
     */
    public static int rollTotal(int count, int sides) {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total = total + roll(sides);    // roll is in this class, so no "Dice." needed
        }
        return total;
    }
}
