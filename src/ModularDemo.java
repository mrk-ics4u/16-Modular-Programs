/*
 * Name:        Lesson 16: Modular Programs (ModularDemo.java)
 * Description: The driver for a small battle. It reads nothing and does no
 *              maths of its own: it calls Enemy (an object class), Dice and
 *              GameText (helper classes), and prints the results.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */

/**
 * Lesson 16 demo. Open Enemy.java, Dice.java, and GameText.java beside
 * this file. All four are in the same folder, so no imports are needed.
 *
 * @author Mr Kowalczewski
 */
public class ModularDemo {

    /**
     * Runs a battle against one goblin.
     *
     * @param args not used
     */
    public static void main(String[] args) {

        // static (class) methods: called on the CLASS name, like Math.sqrt
        System.out.println(Dice.STANDARD_SIDES);
        int roll = Dice.roll(Dice.STANDARD_SIDES);
        System.out.println("Rolled a " + roll);
        System.out.println("Two dice: " + Dice.rollTotal(2, Dice.STANDARD_SIDES));
        System.out.println(GameText.healthBar(7, 10));

        // instance methods: called on an OBJECT, because they use its attributes
        int maxHealth = 30;
        Enemy goblin = new Enemy("Goblin", maxHealth, 4);
        System.out.println(GameText.status(goblin, maxHealth));

        // one battle round, one call per step:
        // roll damage (Dice), apply it (Enemy), show it (GameText)
        int hit = Dice.rollTotal(2, Dice.STANDARD_SIDES);
        goblin.takeDamage(hit);
        System.out.println("Hit for " + hit + ": " + GameText.status(goblin, maxHealth));

        // the same steps in a loop until the goblin is defeated
        int round = 1;
        while (!goblin.isDefeated()) {
            hit = Dice.rollTotal(2, Dice.STANDARD_SIDES);
            goblin.takeDamage(hit);
            System.out.println("Round " + round + ", hit for " + hit + ": " + GameText.status(goblin, maxHealth));
            round++;
        }

        // these don't compile:
        // Enemy.attack();     // non-static method attack() cannot be referenced from a static context
        // roll(6);            // cannot find symbol: roll is in Dice, so call Dice.roll(6)
        // new Dice();         // Dice() has private access in Dice
    }

}
