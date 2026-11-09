# Lesson 16 - Modular Programs

Your project will have hundreds of lines. Written as one long `main`, nobody can find anything in it, nothing can be tested on its own, and the same loop gets copied wherever it's needed.

A **modular** program is split into files that each do **one job**. You've already done half of this: in Lesson 14, `Enemy`, `Boss`, and `Spider` each had their own file. This lesson adds **helper classes** and a rule for deciding where each piece of code goes.

---

## 1. Three Kinds of Class

| Kind | What's in it | Example | How you use it |
|---|---|---|---|
| **Object class** | private attributes, constructor, instance methods | `Enemy` | `goblin.takeDamage(5)` (on an object) |
| **Helper class** | only `static` methods and constants, no attributes | `Dice`, `GameText`, Java's `Math` | `Dice.roll(6)` (on the class name) |
| **Driver** | `main`: input, output, and calls to the others | `ModularDemo` | you run it |

The demo has one of each, plus a second helper:

```
ModularDemo.java   driver: creates objects, calls helpers, prints (where we run our code)
Enemy.java         object class: each Enemy has its own name and health
Dice.java          helper: roll, rollTotal
GameText.java      helper: healthBar, status
```

---

## 2. Writing a Helper Class

A helper class groups related `static` methods, like `Math` groups maths:

```java
public class Dice {

    public static final int STANDARD_SIDES = 6;

    public static int roll(int sides) {
        return (int) (Math.random() * sides) + 1;
    }

    public static int rollTotal(int count, int sides) {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total = total + roll(sides);      // same class: no "Dice." needed
        }
        return total;
    }
}
```

From any other file, call them on the **class name**:

```java
int hit = Dice.rollTotal(2, Dice.STANDARD_SIDES);
String bar = GameText.healthBar(18, 30);       // [######----]
```

Rules for helper methods:

- **Everything comes in through parameters, and the answer goes out with `return`.** No `Scanner`, no attributes.
- **Don't print.** `healthBar` returns the bar. The driver decides whether to print it, save it to a file, or use it in a longer line. A method that prints can only ever be used one way.
- **Group by job.** Dice rolls in `Dice`, screen text in `GameText`. Not one giant `Helpers` class.

Since a helper class has no attributes, there's no reason to make an object of it. `Dice` has a **private constructor**, so `new Dice()` doesn't compile (`Math` does the same).

---

## 3. Instance or Static?

Ask: **does this method need one particular object's attributes?**

- **Yes** → instance method in that object's class. `takeDamage` changes **this** enemy's health, so it's in `Enemy`, with no `static`.
- **No, it only uses its parameters** → `static` method in a helper class. `roll(6)` doesn't belong to any enemy.

`static` isn't "the normal way to write a method". Lesson 6's methods were all `static` because there were no objects yet. Most methods in a real program are instance methods.

A static method can still **use** an object. It just has to be given one as a parameter:

```java
public static String status(Enemy enemy, int maxHealth) {
    return enemy.getName() + " " + healthBar(enemy.getHealth(), maxHealth) + ...;
}
```

Calling an instance method on the class name doesn't work, because Java doesn't know which enemy you mean:

```java
Enemy.attack();     // error: non-static method attack() cannot be referenced from a static context
```

---

## 4. Functional Decomposition

To design the methods, break the task into steps, then each step into smaller steps, until each one is a single method:

```
Battle round
├── roll the damage       Dice.rollTotal(2, 6)
├── apply it              goblin.takeDamage(hit)
└── show the result       GameText.status(goblin, 30)
    └── draw the bar      GameText.healthBar(health, max)
```

Then `main` reads like the list:

```java
int hit = Dice.rollTotal(2, Dice.STANDARD_SIDES);
goblin.takeDamage(hit);
System.out.println(GameText.status(goblin, maxHealth));
```

Two signs a step should be its own method: you've written the same code twice, or you need a `//` comment to say what a block of `main` is doing. The method's name **is** that comment.

---

## 5. How Java Finds the Other Files

All the `.java` files are in `src/`, so they can use each other with **no `import`**. Java looks for a class called `Dice` in a file called `Dice.java`:

- One `public class` per file, and the file name must match it exactly (`Dice.java`, not `dice.java`).
- VS Code compiles every file in `src/` when you press Run. From a terminal, compile them together: `javac *.java`, then `java ModularDemo`.
- `out/` gets one `.class` file per class.

If the file is missing or misnamed, every use of it fails with `cannot find symbol ... class Dice`.

`import` is only for classes in a different **package**, like `java.util.Scanner`. Bigger programs put their own classes into packages too. Ours don't need that.

---

## 6. Changing the Inside, Not the Outside

Because every program only **calls** `Dice.roll(sides)`, its insides can change without anything else changing. Switching from `Math.random()` to a `Random` object, or loading dice from a file, only touches `Dice.java`. Every caller still works, as long as the **header** and what the method **does** (its Javadoc) stay the same.

Adding a feature works the same way: write a new method in the right class, then call it. That's this lesson's exercise: `WeatherReport` gets two new lines of output by adding methods to a helper, not by making `main` longer.

---

## 7. Common Modular Bugs

**Calling a helper without the class name** from another file (`roll(6)` in `ModularDemo`). `cannot find symbol: method roll(int)`. Write `Dice.roll(6)`.

**Calling an instance method on the class name** (`Enemy.attack()`). See Section 3.

**File name doesn't match the class** (`class Dice` in `Helpers.java`). `class Dice is public, should be declared in a file named Dice.java`.

**A helper that prints instead of returning.** It works once, then can't be reused.

**Moving code out of `main` and leaving the old copy behind.** Now there are two versions to keep in sync. Delete the original.

---

## Try It Yourself

Run the companion file from the `src` folder:

```bash
javac *.java
java ModularDemo
```

`ModularDemo` uses `Enemy.java`, `Dice.java`, and `GameText.java`. Open all four. The rolls are random, so run it a few times.

Then work on the exercise in `WeatherStats.java` and `WeatherReport.java`.

---

## Course Expectations

### ICS 4U

"I can split a program into multiple files (object classes, helper classes, and a driver), decide whether a method should be static or an instance method, and break a task into methods" is a requirement of the course (A2.1, A2.2, A2.3, C1.3, C1.4) and will be expected in your programming project.

### AP Expectations

The following are expectations of the AP exam and will show up on the final exam:

- Class methods are associated with the class, not an instance; they're marked `static` and called with the class name and the dot operator, which is optional inside the defining class (Topic 1.10, 1.10.A.1, 1.10.A.2)
- Class methods can't access instance variables or call instance methods unless an instance is passed in as a parameter (Topic 3.7, 3.7.A.1)
- Class methods can use class variables and call other class methods (3.7.A.2)
- Procedural abstraction: method decomposition breaks a larger behaviour into smaller methods, and pulling shared code into one method avoids duplication (Topic 3.1, 3.1.A.4)
- Parameters let a method be reused with a range of values (3.1.A.5)
- A method's internals can change without affecting its callers, as long as its signature and what it does stay the same (3.1.A.6)
- Libraries are collections of classes; `Math` contains only class methods (Topic 1.7, 1.7.A.1; Topic 1.11, 1.11.A.2)
