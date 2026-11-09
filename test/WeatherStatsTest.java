/*
 * WeatherStatsTest.java -- unit tests for the Lesson 16 exercise
 * ICS 4U0 - Lesson 16 Exercise: WeatherStats
 *
 * Three kinds of tests:
 *   - Method tests call each WeatherStats method directly, the same way
 *     WeatherReport (or any other program) would.
 *   - An output test runs WeatherReport.main(): it types the input lines on
 *     System.in and checks the report it prints.
 *   - A structure test reads src/WeatherReport.java as text to check that
 *     main calls WeatherStats instead of doing the calculations itself.
 * You don't need to change this file -- just run the tests (see README.md)
 * and fix WeatherStats.java and WeatherReport.java until they all pass.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeatherStatsTest {

    private static final double CLOSE_ENOUGH = 0.000001;

    private InputStream originalIn;
    private PrintStream originalOut;
    private Locale originalLocale;

    @BeforeEach
    public void redirectIoAndPinLocale() {
        originalIn = System.in;
        originalOut = System.out;
        originalLocale = Locale.getDefault();
        // printf follows the default locale -- pin it so "%.1f" prints
        // 3.4, not 3,4, on every machine.
        Locale.setDefault(Locale.CANADA);
    }

    @AfterEach
    public void restoreIoAndLocale() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        Locale.setDefault(originalLocale);
    }

    /**
     * Feeds {@code input} to WeatherReport.main() on System.in and returns
     * everything it printed to System.out.
     */
    private String run(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

        WeatherReport.main(new String[0]);

        return captured.toString(StandardCharsets.UTF_8);
    }

    /**
     * Compares output line by line, trimming trailing whitespace on each
     * line and ignoring one blank line at the very end (println's final
     * newline). Everything else -- including every space, colon, and bracket
     * -- is compared exactly.
     */
    private void assertOutputEquals(String expected, String actual) {
        String[] expectedLines = expected.stripTrailing().split("\n", -1);
        String[] actualLines = actual.stripTrailing().split("\n", -1);

        assertEquals(expectedLines.length, actualLines.length,
                "Expected " + expectedLines.length + " line(s) of output, got " + actualLines.length
                        + ".\n--- expected ---\n" + expected + "--- actual ---\n" + actual);

        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i].stripTrailing(), actualLines[i].stripTrailing(),
                    "Line " + (i + 1) + " didn't match.\n--- expected ---\n" + expected
                            + "--- actual ---\n" + actual);
        }
    }

    // ------------------------------------------------------------------
    // Output test: WeatherReport
    // ------------------------------------------------------------------

    @Test
    public void exampleFromReadme() {
        String input = "7\n4.5\n-2.0\n9.5\n7.0\n-6.0\n3.5\n7.5\n";
        String expected = "Days recorded: 7\n"
                + "Average: 3.4 C\n"
                + "Highest: 9.5 C (49.1 F)\n"
                + "Lowest: -6.0 C (21.2 F)\n"
                + "Days below freezing: 2\n";
        assertOutputEquals(expected, run(input));
    }

    // ------------------------------------------------------------------
    // Method tests: WeatherStats
    // ------------------------------------------------------------------

    @Test
    public void averageIncludesEveryTemperature() {
        // (2 + 4 + 9) / 3 = 5. Starting the loop at 1 skips the 2.0.
        double[] temps = {2.0, 4.0, 9.0};
        assertEquals(5.0, WeatherStats.average(temps), CLOSE_ENOUGH,
                "average({2.0, 4.0, 9.0})");
    }

    @Test
    public void highestWorksWhenEveryTemperatureIsBelowZero() {
        // Starting highest at 0 returns 0, which isn't in the list.
        // Start at temps[0] instead.
        double[] temps = {-8.5, -3.0, -12.0};
        assertEquals(-3.0, WeatherStats.highest(temps), CLOSE_ENOUGH,
                "highest({-8.5, -3.0, -12.0})");
    }

    @Test
    public void lowestWorksWhenEveryTemperatureIsAboveZero() {
        // Starting lowest at 0 returns 0, which isn't in the list.
        // Start at temps[0] instead.
        double[] temps = {14.0, 22.5, 18.0};
        assertEquals(14.0, WeatherStats.lowest(temps), CLOSE_ENOUGH,
                "lowest({14.0, 22.5, 18.0})");
    }

    @Test
    public void countBelowDoesNotCountTheLimitItself() {
        // Only -1.0 is below 0.0. The two days exactly at 0.0 don't count: use <, not <=.
        double[] temps = {-1.0, 0.0, 0.0, 3.0};
        assertEquals(1, WeatherStats.countBelow(temps, 0.0),
                "countBelow({-1.0, 0.0, 0.0, 3.0}, 0.0)");
    }

    @Test
    public void countBelowUsesTheLimitParameter() {
        // The limit is a parameter so the method works for any limit, not just freezing.
        // 5.0 and 9.0 are below 10.0.
        double[] temps = {5.0, 9.0, 12.0};
        assertEquals(2, WeatherStats.countBelow(temps, 10.0),
                "countBelow({5.0, 9.0, 12.0}, 10.0)");
    }

    @Test
    public void toFahrenheitUsesDecimalDivision() {
        // 100 C is 212 F. If 9 / 5 is worked out first as int division it's 1, giving 132.
        assertEquals(212.0, WeatherStats.toFahrenheit(100.0), CLOSE_ENOUGH, "toFahrenheit(100.0)");
        assertEquals(-40.0, WeatherStats.toFahrenheit(-40.0), CLOSE_ENOUGH, "toFahrenheit(-40.0)");
        assertEquals(98.6, WeatherStats.toFahrenheit(37.0), CLOSE_ENOUGH, "toFahrenheit(37.0)");
    }

    @Test
    public void statsMethodsDoNotPrint() {
        // A helper method returns its answer. Printing is the driver's job.
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

        double[] temps = {1.0, -2.0, 3.0};
        WeatherStats.average(temps);
        WeatherStats.highest(temps);
        WeatherStats.lowest(temps);
        WeatherStats.countBelow(temps, 0.0);
        WeatherStats.toFahrenheit(20.0);

        String printed = captured.toString(StandardCharsets.UTF_8);
        assertEquals("", printed, "WeatherStats methods printed something. Return the value "
                + "and let WeatherReport print it. Printed:\n" + printed);
    }

    // ------------------------------------------------------------------
    // Structure test: reads WeatherReport.java as text
    // ------------------------------------------------------------------

    /** WeatherReport.java with // and block comments removed. */
    private String reportCode() {
        Path path = Path.of("src", "WeatherReport.java");
        if (!Files.exists(path)) {
            path = Path.of("..", "src", "WeatherReport.java");
        }
        try {
            String code = Files.readString(path, StandardCharsets.UTF_8);
            code = code.replaceAll("(?s)/\\*.*?\\*/", "");
            code = code.replaceAll("//[^\n]*", "");
            return code;
        } catch (IOException e) {
            fail("Couldn't read src/WeatherReport.java: " + e.getMessage());
            return null;
        }
    }

    @Test
    public void reportCallsWeatherStatsInsteadOfItsOwnLoops() {
        String code = reportCode();
        String[] calls = {"average", "highest", "lowest", "countBelow", "toFahrenheit"};
        for (String name : calls) {
            assertTrue(code.contains("WeatherStats." + name + "("), "WeatherReport's main should call WeatherStats."
                    + name + "(...) instead of working it out itself.");
        }

        int loops = code.split("\\bfor\\s*\\(", -1).length - 1;
        loops = loops + code.split("\\bwhile\\s*\\(", -1).length - 1;
        assertTrue(loops <= 1, "WeatherReport has " + loops + " loops. It only needs one, to read the "
                + "temperatures. Move the calculation loops into WeatherStats and delete them from main.");
    }
}
