# Lab9 

For lab9, you will implement the `HashTable` class that maps `String` keys to `int` values, such as `"cat"` → 3. You will write the hash function that turns a key into a bucket number, the methods that add, find and remove keys, and the resizing that keeps the table fast as it fills up. There are seven methods you need to implement. 

## Objectivies

- Computeing a polynomial hash with Horner's rule, and turning it into a bucket index with `%` without overflowing an `int`.
- Storing a hash table with separate chaining: an array of linked lists, where keys that collide share a list.
- Adding, finding and removing a key by looking at one list only, comparing keys with `equals`.
- Keeping the table fast: measureing its load factor, and moving every key into a bigger array when it gets too full.

## Files

| File | What to do |
|:--|:--|
| `README.md` | These instructions. |
| `HashTable.java` | Complete the seven methods marked `TODO`. |
| `Main.java` | Example tests. Run it to check your work. |

Each of the seven methods currently throws an `UnsupportedOperationException`. Replace that line with your code. The files compile as they are, so you can work on one method at a time.

## Hash tables 

The table is an array of buckets. Each bucket holds a linked list, called a chain, of the nodes whose keys belong in that bucket. A node holds one key, its value, and a link to the next node in the same chain. This way of storing keys is called separate chaining.

Here is a table with 8 buckets after `put("cat", 3)`, `put("dog", 7)`, `put("bee", 5)`, `put("owl", 1)` and `put("pig", 9)`. The rest of this README calls it the example table.

```text
buckets[0] -> null
buckets[1] -> null
buckets[2] -> ["bee" | 5] -> null
buckets[3] -> null
buckets[4] -> ["owl" | 1] -> ["dog" | 7] -> null       "owl" and "dog" collide
buckets[5] -> null
buckets[6] -> ["pig" | 9] -> ["cat" | 3] -> null       "pig" and "cat" collide
buckets[7] -> null
```

A key's bucket is `hash(key, 8)`, the hash function that you write in method 1. `"cat"` and `"pig"` both hash to 6: they collide, so they share bucket 6's chain. To find a key, you work out its bucket and walk along that one chain. You never need to look in the other buckets.

The load factor is the number of keys divided by the number of buckets: here, 5 / 8 = 0.625. The higher it is, the longer the chains, and the slower the table. So when `put()` adds a new key and the number of keys goes above 0.75 times the number of buckets, the table resizes: it doubles its number of buckets and moves every key into its bucket in the new array. A 7th key would do that here, because 7 > 0.75 × 8 = 6. Method 7 shows the table after a resize.

`toString()` draws a table in the same way, so `System.out.println(table);` shows you yours.

## The hash function

The hash function turns a key into a bucket index from 0 to `capacity - 1`, where `capacity` is the number of buckets. It treats the key's characters as the digits of a number in base 31, and uses Horner's rule to work out that number one character at a time:

```text
h = 0
for each character c of key, from left to right:
    h = (31 * h + c) % capacity
return h
```

Here `c` is the character's code: `'a'` is 97, `'b'` is 98, `'A'` is 65 and a space is 32. In Java, a `char` used in arithmetic is its code, so `31 * h + key.charAt(i)` does the right thing.

For example, here is `hash("cat", 8)`:

| Character | Code | `h = (31 * h + code) % 8` |
|:--|:--|:--|
| (start) | | `0` |
| `c` | 99 | `(31 * 0 + 99) % 8 = 99 % 8 = 3` |
| `a` | 97 | `(31 * 3 + 97) % 8 = 190 % 8 = 6` |
| `t` | 116 | `(31 * 6 + 116) % 8 = 302 % 8 = 6` |

So `hash("cat", 8)` is 6.

Taking `% capacity` at every step keeps `h` smaller than `capacity`, so `31 * h + c` never gets too big for an `int`. The answer is the same as working out the whole number first, 99 × 31² + 97 × 31 + 116 = 98,262, and then taking `98,262 % 8 = 6`. But for a long key, that whole number has dozens of digits: far too many for an `int`, or even a `long`.

## The provided code

The `PROVIDED CODE` section at the top of `HashTable.java` gives you:

| Member | What it is |
|:--|:--|
| `Node` | One key with its value: the fields `key`, `value` and `next` (the next node in the same chain, or `null` for the last node), and the constructor `Node(key, value, next)`. |
| `Node[] buckets` and `int size` | The table's only two fields. `buckets[i]` is the first node of bucket `i`'s chain, or `null` if the bucket is empty. `size` is the number of keys. |
| `DEFAULT_CAPACITY`, `MAX_LOAD_FACTOR` | 8, the number of buckets in a new table, and 0.75. |
| `HashTable()`, `HashTable(int capacity)` | Create an empty table with 8 buckets, or with the number of buckets that you give. |
| `size()`, `isEmpty()`, `capacity()` | The number of keys, whether it is 0, and the number of buckets (`buckets.length`). |
| `bucketOf(key)` | The index of the bucket whose chain holds `key`, or -1 if there is none. It searches every bucket, so it is only for testing (`Main.java` uses it). Do not call it in your methods. |
| `toString()` | A picture of the table, like the one above. |

## Rules

1. **Keep the provided code.** Do not change anything in the `PROVIDED CODE` section of `HashTable.java`. Do not rename the class or the seven methods, change their parameters or return types, or replace the `package` line. I will build my test tables by creating `Node`s and writing the `buckets` and `size` fields directly, and check your results by reading them.
2. **Do not add fields.** Do not add instance variables or static variables: a table is exactly its `buckets` and its `size`. Use parameters, local variables and private helper methods instead.
3. **One file.** All of your code goes in `HashTable.java`. It must compile on its own, using only the standard Java library.
4. **Write the hash table yourself.** Do not use `HashMap`, `Hashtable`, `HashSet` or any other map or set from the Java library, and do not use `hashCode()`: you are writing the hash function too. I will flag any use of them.
5. **Exceptions.** The only exception your code may throw is an `IllegalArgumentException` from `put()` when the key is `null`. Do not call `System.exit`.


## Testing

`Main` runs the examples above and prints `PASS`, `FAIL`, `ERROR` (an exception was thrown) or `NOT DONE` for each one:

```text
hash(String, int)
  PASS      hash("cat", 8): 6
  PASS      hash("dog", 8): 4
  ...
  FAIL      hash("Hello, World!", 1000) (% at every step): expected 389, got 909
  ...
loadFactor()
  PASS      loadFactor() of an empty table: 0.0
  FAIL      loadFactor() of the example table (5 keys in 8 buckets): expected 0.625, got 0.0
  ...
resize()
  NOT DONE  the example table after resize(): capacity(): resize() is not implemented yet
  ...

Passed 31 of 42 checks.
```

The checks for `get`, `containsKey`, `remove`, `loadFactor` and `resize` build their tables with `put()`, so do `put()` first. To see a table while you debug, print it with `System.out.println(table);`.

`Main` is not marked. I will use my own tables (with 1, 4, 8, 13 and 32 buckets, long chains, keys that differ only in case, the empty key, a table that grows from 8 to 256 buckets, and more), so passing every check in `Main` does not guarantee full marks. Add your own checks: copy a `check(...)` line in `main` and change the call and the expected answer.

## Rubric

Your code is marked automatically: **40 test cases × 1 mark = 40 marks**. There are no marks for style or comments; only the results count.

- **Compiling.** If `HashTable.java` does not compile, the whole assignment scores **0**.
- **Test cases.** Each test case runs one method on one or more tables, each made fresh. A test case earns its mark only if every result in it is right.
- **Test tables.** I will build each table myself, by creating the `Node`s and writing `buckets` and `size`, so a bug in one method cannot spoil the tests of another. It puts each key in the bucket that **your** `hash()` gives, so a mistake in `hash()` costs marks in `hash()` only. The exceptions: `put`'s test cases 8 and 10 make the table resize, so they also need your `resize()`, and if your `resize()` uses `put()`, it needs your `put()`.
- **The whole table.** After `put`, `remove` and `resize`, I will read your `buckets` and `size` and check the number of buckets, `size`, every key and its value (each key exactly once), and that each key is in its own bucket, `hash(key, buckets.length)`. The order of the nodes in a chain does not matter. `get`, `containsKey` and `loadFactor` must leave the table exactly as it was.
- **Unexpected exceptions.** If a method throws an exception that this README does not allow, in **any** of its test cases, that method scores **0** for **all** of its test cases. This includes a `NullPointerException` (for example, from an empty bucket or from the end of a chain), an `ArrayIndexOutOfBoundsException`, a `StackOverflowError`, the `UnsupportedOperationException` of a method that you have not done, and any exception from `put(null, value)` other than an `IllegalArgumentException`. Calling `System.exit` counts the same way.
- **Exceptions caused by another method.** An exception caused by another of the seven methods does not count against the method being tested: one that `resize()` throws while `put()` is calling it, for example, or an `ArrayIndexOutOfBoundsException` because `hash()` gave an index outside the table. That test case scores 0, and the other method is marked by its own test cases. This does not apply to `put(null, value)`: there, `put()` itself must throw the `IllegalArgumentException`.
- **Time limit.** A test case that runs for more than 5 seconds (for example, because of a chain that loops back on itself) scores 0.

| Method | Marks | What its test cases check |
|:--|:--|:--|
| `hash(String, int)` | 6 | 1. the empty key and one-character keys · 2. short keys in 8 buckets · 3. other numbers of buckets, including prime numbers and 1 · 4. keys with capital letters, digits, spaces and punctuation · 5. long keys (% must be taken at every step) · 6. 200 random keys and numbers of buckets |
| `put(String, int)` | 10 | 1. into an empty table (8 and 13 buckets) · 2. several keys in different buckets (one of them is the empty string "") · 3. keys that collide share one chain · 4. an existing key: its value is replaced, and size does not change · 5. an existing key given as an equal, but different, String object (equals, not ==) · 6. put(null, value) throws an IllegalArgumentException and changes nothing · 7. no resize while size is at most 0.75 times the number of buckets · 8. a new key that takes size above 0.75 times the number of buckets doubles it · 9. replacing a value never resizes the table · 10. 100 different keys into an empty table, which grows from 8 to 256 buckets |
| `get(String)` | 5 | 1. keys alone in their buckets, and missing keys whose buckets are empty (8 and 13 buckets) · 2. keys in a chain: the first, a middle and the last node · 3. missing keys whose buckets hold other keys · 4. keys that differ only in case or by one character, and an empty table · 5. an equal, but different, String object (equals, not ==) |
| `containsKey(String)` | 4 | 1. keys alone in their buckets, and missing keys whose buckets are empty (8 and 13 buckets) · 2. keys in a chain, and missing keys whose buckets hold other keys · 3. keys that differ only in case or by one character, and an empty table · 4. an equal, but different, String object (equals, not ==) |
| `remove(String)` | 7 | 1. a key that is alone in its bucket · 2. the first node of a chain · 3. a middle node of a chain · 4. the last node of a chain · 5. missing keys return null and change nothing · 6. removing every key, one at a time, until the table is empty · 7. removing the same key twice, and an equal, but different, String object |
| `loadFactor()` | 3 | 1. an empty table, and tables with one key · 2. a few keys (the answer is not a whole number) · 3. more keys than buckets (a load factor above 1) |
| `resize()` | 5 | 1. an empty table: the number of buckets doubles · 2. keys alone in their buckets move to their buckets in the new table · 3. a chain whose keys go to different buckets in the new table · 4. resizing twice in a row · 5. larger tables (7 keys in 8 buckets, and 24 keys in 32 buckets) |

## Submission
Zip the project directory and submit the ZIP file to Canvas.
**Please do not remove any build artifacts or manifest files generated by NetBeans, and do not modify the directory structure. Zip the project directory exactly as it is.**

**Your program must compile and execute successfully. If the program fails to compile or crashes during execution, the submission will receive a grade of zero.**

## Deadline
Sunday, November 29, 2026, at 11:59 PM PDT



