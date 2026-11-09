/*
 * Name:        Lesson 16: Modular Programs (GameText.java)
 * Description: A second helper class, for building the text the game
 *              shows. It returns Strings and never prints: the program
 *              that calls it decides what to print. Used by ModularDemo.java.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */

/**
 * Builds the text the game shows on screen.
 *
 * @author Mr Kowalczewski
 */
public class GameText {

    /** How many characters wide a health bar is, not counting the brackets. */
    public static final int BAR_WIDTH = 10;

    /**
     * Builds a health bar, for example [######----] for 18 out of 30.
     *
     * Precondition: maxHealth > 0 and health is from 0 to maxHealth.
     *
     * @param health    the current health
     * @param maxHealth the health when full
     * @return the bar, BAR_WIDTH characters between square brackets
     */
    public static String healthBar(int health, int maxHealth) {
        int filled = health * BAR_WIDTH / maxHealth;
        String bar = "[";
        for (int i = 0; i < BAR_WIDTH; i++) {
            if (i < filled) {
                bar = bar + "#";
            } else {
                bar = bar + "-";
            }
        }
        return bar + "]";
    }

    /**
     * Describes an enemy's health in one line, for example
     * Goblin [######----] 18/30.
     *
     * Precondition: enemy is not null and maxHealth > 0.
     *
     * @param enemy     the enemy to describe
     * @param maxHealth the enemy's health when full
     * @return the enemy's name, health bar, and health out of maxHealth
     */
    public static String status(Enemy enemy, int maxHealth) {
        // a static method has no object of its own, so the Enemy comes in as a parameter
        int health = enemy.getHealth();
        return enemy.getName() + " " + healthBar(health, maxHealth) + " " + health + "/" + maxHealth;
    }
}
