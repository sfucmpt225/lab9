package io.github.sfucmpt225.lab9;

import java.util.Objects;
import java.util.function.Supplier;

/*
 * Runs some checks on the methods in HashTable.java and prints PASS or FAIL
 * for each one. Compile and run it from the folder that holds both files:
 *
 * Main.java is NOT marked. I will my own tests, on other tables as
 * well, so passing every check here does not guarantee full marks. Add your
 * own checks: copy a check(...) line and change the call and the answer.
 *
 * Most checks use "the example table" from README.md: 8 buckets, after
 * put("cat", 3), put("dog", 7), put("bee", 5), put("owl", 1) and
 * put("pig", 9). To see a table while you debug, print it:
 *
 *     System.out.println(table);
 */
public class Main {

    public static void main(String[] args) {
        section("hash(String, int)");
        check("hash(\"cat\", 8)", 6, () -> HashTable.hash("cat", 8));
        check("hash(\"dog\", 8)", 4, () -> HashTable.hash("dog", 8));
        check("hash(\"\", 8) (the empty key)", 0, () -> HashTable.hash("", 8));
        check("hash(\"a\", 8)", 1, () -> HashTable.hash("a", 8));
        check("hash(\"cat\", 13)", 8, () -> HashTable.hash("cat", 13));
        check("hash(\"cat\", 1) (one bucket)", 0, () -> HashTable.hash("cat", 1));
        check("hash(\"Hello, World!\", 1000) (% at every step)", 389, () -> HashTable.hash("Hello, World!", 1000));

        // From here on, the tables are built with put(), so do put() first.
        section("put(String, int)");
        check("put(\"cat\", 3) into an empty table: size()", 1, () -> tableOf("cat", 3).size());
        check("put(\"cat\", 3) into an empty table: \"cat\" is in bucket", 6, () -> tableOf("cat", 3).bucketOf("cat"));
        check("the example table: size()", 5, () -> exampleTable().size());
        check("the example table: \"dog\" is in bucket", 4, () -> exampleTable().bucketOf("dog"));
        check("the example table: \"owl\" is in bucket (with \"dog\")", 4, () -> exampleTable().bucketOf("owl"));
        check("the example table: \"pig\" is in bucket (with \"cat\")", 6, () -> exampleTable().bucketOf("pig"));
        check("put(\"cat\", 3), then put(\"cat\", 10): size() (still 1)", 1, () -> tableOf("cat", 3, "cat", 10).size());
        checkThrows("put(null, 1)", IllegalArgumentException.class, () -> {
            new HashTable().put(null, 1);
            return null;
        });

        section("get(String)");
        check("get(\"dog\") on the example table", 7, () -> exampleTable().get("dog"));
        check("get(\"owl\") on the example table", 1, () -> exampleTable().get("owl"));
        check("get(\"bee\") on the example table", 5, () -> exampleTable().get("bee"));
        check("get(\"ant\") on the example table (not there)", null, () -> exampleTable().get("ant"));
        check("get(\"Cat\") on the example table (not there: \"Cat\" is not \"cat\")", null, () -> exampleTable().get("Cat"));
        check("put(\"cat\", 3), then put(\"cat\", 10): get(\"cat\")", 10, () -> tableOf("cat", 3, "cat", 10).get("cat"));

        section("containsKey(String)");
        check("containsKey(\"bee\") on the example table", true, () -> exampleTable().containsKey("bee"));
        check("containsKey(\"pig\") on the example table", true, () -> exampleTable().containsKey("pig"));
        check("containsKey(\"emu\") on the example table", false, () -> exampleTable().containsKey("emu"));

        section("remove(String)");
        check("remove(\"owl\") on the example table returns", 1, () -> exampleTable().remove("owl"));
        check("the example table after remove(\"owl\"): get(\"dog\") (same chain)", 7,
                () -> afterRemove(exampleTable(), "owl").get("dog"));
        check("the example table after remove(\"owl\"): size()", 4, () -> afterRemove(exampleTable(), "owl").size());
        check("the example table after remove(\"cat\"): get(\"pig\") (same chain)", 9,
                () -> afterRemove(exampleTable(), "cat").get("pig"));
        check("remove(\"ant\") on the example table (not there)", null, () -> exampleTable().remove("ant"));
        check("the example table after remove(\"ant\"): size() (still 5)", 5, () -> afterRemove(exampleTable(), "ant").size());

        section("loadFactor()");
        check("loadFactor() of an empty table", 0.0, () -> new HashTable().loadFactor());
        check("loadFactor() of the example table (5 keys in 8 buckets)", 0.625, () -> exampleTable().loadFactor());
        check("loadFactor() of 1 key in 3 buckets", 1.0 / 3, () -> tableOf(new HashTable(3), "cat", 1).loadFactor());

        section("resize()");
        check("the example table after resize(): capacity()", 16, () -> resized(exampleTable()).capacity());
        check("the example table after resize(): size() (still 5)", 5, () -> resized(exampleTable()).size());
        check("the example table after resize(): \"dog\" is in bucket", 12, () -> resized(exampleTable()).bucketOf("dog"));
        check("the example table after resize(): \"pig\" is in bucket", 14, () -> resized(exampleTable()).bucketOf("pig"));
        check("the example table after resize(): \"cat\" is in bucket", 6, () -> resized(exampleTable()).bucketOf("cat"));
        check("the example table after resize(): get(\"owl\")", 1, () -> resized(exampleTable()).get("owl"));
        check("put a 6th key into the example table: capacity() (6 keys: no resize)", 8,
                () -> tableOf(exampleTable(), "emu", 4).capacity());
        check("put a 7th key: capacity() (7 > 0.75 * 8, so put() resizes)", 16,
                () -> tableOf(exampleTable(), "emu", 4, "ant", 2).capacity());
        check("put a 7th key: get(\"dog\") after the resize", 7,
                () -> tableOf(exampleTable(), "emu", 4, "ant", 2).get("dog"));

        // Add your own checks here. For example:
        // check("hash(\"bee\", 8)", 2, () -> HashTable.hash("bee", 8));

        System.out.println();
        System.out.println("Passed " + passed + " of " + total + " checks.");
        if (errors > 0) {
            System.out.println("Careful: " + errors + " check(s) threw an exception. When your work is marked,"
                    + " a method that throws an exception the README does not allow scores 0.");
        }
    }

    // -------------------------------------------------------------------------
    //  Small helpers used by the checks above.
    // -------------------------------------------------------------------------

    /** A new table with 8 buckets, after put(key, value) for each key and value given, in this order. */
    static HashTable tableOf(Object... keysAndValues) {
        return tableOf(new HashTable(), keysAndValues);
    }

    /** The given table, after put(key, value) for each key and value given, in this order. */
    static HashTable tableOf(HashTable table, Object... keysAndValues) {
        for (int i = 0; i < keysAndValues.length; i += 2) {
            table.put((String) keysAndValues[i], (Integer) keysAndValues[i + 1]);
        }
        return table;
    }

    /** The example table from README.md: 8 buckets, after put cat=3, dog=7, bee=5, owl=1, pig=9. */
    static HashTable exampleTable() {
        return tableOf("cat", 3, "dog", 7, "bee", 5, "owl", 1, "pig", 9);
    }

    /** Calls remove(key) once, then returns the table. */
    static HashTable afterRemove(HashTable table, String key) {
        table.remove(key);
        return table;
    }

    /** Calls resize() once, then returns the table. */
    static HashTable resized(HashTable table) {
        table.resize();
        return table;
    }

    // -------------------------------------------------------------------------
    //  Helpers that run a check and print the result. You do not need to read
    //  them to use them.
    // -------------------------------------------------------------------------

    private static int passed = 0;
    private static int total = 0;
    private static int errors = 0;

    /** Prints a heading for the checks of one method. */
    static void section(String method) {
        System.out.println();
        System.out.println(method);
    }

    /** Runs code and checks that it returns expected. */
    static void check(String what, Object expected, Supplier<Object> code) {
        total++;
        Object actual;
        try {
            actual = code.get();
        } catch (RuntimeException | Error e) {
            reportException(what, e);
            return;
        }
        if (same(expected, actual)) {
            passed++;
            print("PASS", what + ": " + actual);
        } else {
            print("FAIL", what + ": expected " + expected + ", got " + actual);
        }
    }

    /** Runs code and checks that it throws an exception of the given type. */
    static void checkThrows(String what, Class<? extends Throwable> expected, Supplier<Object> code) {
        total++;
        try {
            code.get();
        } catch (RuntimeException | Error e) {
            if (expected.isInstance(e)) {
                passed++;
                print("PASS", what + ": threw " + e.getClass().getSimpleName());
            } else {
                reportException(what, e);
            }
            return;
        }
        print("FAIL", what + ": expected " + expected.getSimpleName() + " to be thrown, but nothing was thrown");
    }

    /** Equal answers; two doubles count as equal if they differ by less than 0.000000001. */
    private static boolean same(Object expected, Object actual) {
        if (expected instanceof Double && actual instanceof Double) {
            return Math.abs((Double) expected - (Double) actual) < 1e-9;
        }
        return Objects.equals(expected, actual);
    }

    private static void reportException(String what, Throwable e) {
        String message = e.getMessage() == null ? "" : e.getMessage();
        if (e instanceof UnsupportedOperationException && message.endsWith("not implemented yet")) {
            print("NOT DONE", what + ": " + message);
            return;
        }
        errors++;
        String text = what + ": threw " + e.getClass().getSimpleName();
        for (StackTraceElement frame : e.getStackTrace()) {
            if (frame.getClassName().startsWith("HashTable")) {
                text += " at line " + frame.getLineNumber() + " of HashTable.java";
                break;
            }
        }
        if (e instanceof StackOverflowError) {
            text += " (infinite recursion? Do put() and resize() call each other forever?)";
        } else if (!message.isEmpty()) {
            text += ": " + message;
        }
        print("ERROR", text);
    }

    private static void print(String status, String text) {
        System.out.printf("  %-9s %s%n", status, text);
    }
}
