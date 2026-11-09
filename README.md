# ICS 4U0 — Lesson 16: Modular Programs

## Exercise

A weather station has a program, `src/WeatherReport.java`, that reads a week of daily temperatures and prints a report. It works, but every calculation is a loop written out inside `main`. The station now wants two more things in the report: the highest and lowest in Fahrenheit, and the number of days below freezing.

Instead of making `main` longer, split the program:

1. **Write the helper class `src/WeatherStats.java`.** Every method already has its header and Javadoc. Write each body from its Javadoc. These are `static` methods: they get everything from their parameters and **return** the answer. **No printing, no `Scanner`.**
2. **Refactor `WeatherReport`'s `main`.** Replace each calculation loop with a call to `WeatherStats`, and delete the old loops. The only loop left in `main` is the one that reads the temperatures.
3. **Add the new output** using `WeatherStats.toFahrenheit` and `WeatherStats.countBelow` (see Output). "Below freezing" means below `0`.

```
┌──────────────────────────────────────────────────┐
│ WeatherStats                                     │
├──────────────────────────────────────────────────┤
│ (no attributes)                                  │
├──────────────────────────────────────────────────┤
│ + average(temps : double[]) : double  (static)   │
│ + highest(temps : double[]) : double  (static)   │
│ + lowest(temps : double[]) : double  (static)    │
│ + countBelow(temps : double[],                   │
│              limit : double) : int  (static)     │
│ + toFahrenheit(celsius : double) : double        │
│                                       (static)   │
└──────────────────────────────────────────────────┘
```

Everything you need is from Lesson 16:

- a **helper class** of **`static` methods** called with the **class name** (`WeatherStats.average(temps)`),
- methods that **return** instead of print,
- **functional decomposition**: `main` becomes read, calculate (by calling), print.

- **Don't change the `WeatherStats` method headers.**
- Run the program before you start. The first four lines of the report are already right, so they should stay right after you refactor. If one changes, the refactor broke something.

---

## Input

| Line | Value | Type |
|------|-------|------|
| 1 | Number of days, `n` | whole number |
| next `n` | One temperature per line, in degrees Celsius | decimal number |

Example input:

```
7
4.5
-2.0
9.5
7.0
-6.0
3.5
7.5
```

---

## Output

```
Days recorded: <n>
Average: <average> C
Highest: <highest> C (<highest in Fahrenheit> F)
Lowest: <lowest> C (<lowest in Fahrenheit> F)
Days below freezing: <count>
```

Every temperature is printed with one decimal place (`%.1f`).

For the example input above, the program must print **exactly**:

```
Days recorded: 7
Average: 3.4 C
Highest: 9.5 C (49.1 F)
Lowest: -6.0 C (21.2 F)
Days below freezing: 2
```

Every space, bracket, and decimal place must match.

---

## Testing

- Test your code yourself first.
- Open the **Testing** panel from the sidebar (flask icon) and click ▶ **Run Tests**.

A green check next to a test means it passed; a red X means it failed and will show you the expected vs. actual output.
