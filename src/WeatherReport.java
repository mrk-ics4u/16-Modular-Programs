/*
 * Name:        Lesson 16: Modular Programs (WeatherReport.java)
 * Description: Reads a week of daily temperatures and prints a report.
 *              It works, but every calculation is written out inside main.
 * Created by:  Mr Kowalczewski
 * Last edited: 2026-11-03
 */
import java.util.Scanner;

/**
 * Prints a temperature report.
 *
 * @author Mr Kowalczewski
 */
public class WeatherReport {

    /**
     * Reads the temperatures and prints the report.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int days = Integer.parseInt(input.nextLine().trim());
        double[] temps = new double[days];
        for (int i = 0; i < days; i++) {
            temps[i] = Double.parseDouble(input.nextLine().trim());
        }

        double sum = 0;
        for (int i = 0; i < temps.length; i++) {
            sum = sum + temps[i];
        }
        double average = sum / temps.length;

        double highest = temps[0];
        for (int i = 1; i < temps.length; i++) {
            if (temps[i] > highest) {
                highest = temps[i];
            }
        }

        double lowest = temps[0];
        for (int i = 1; i < temps.length; i++) {
            if (temps[i] < lowest) {
                lowest = temps[i];
            }
        }

        System.out.println("Days recorded: " + days);
        System.out.printf("Average: %.1f C%n", average);
        System.out.printf("Highest: %.1f C%n", highest);
        System.out.printf("Lowest: %.1f C%n", lowest);
    }
}
