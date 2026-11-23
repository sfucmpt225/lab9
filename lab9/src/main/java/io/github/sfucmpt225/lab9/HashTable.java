package io.github.sfucmpt225.lab9;

/*
 * A hash table that maps String keys to int values. It uses separate
 * chaining: an array of buckets, where each bucket holds a linked list (a
 * "chain") of the nodes whose keys hash to that bucket. Complete the seven
 * methods in the YOUR CODE section at the bottom of this file. README.md
 * explains each method and how your work is marked.
 */
public class HashTable {

    // =========================================================================
    //  PROVIDED CODE: do not change anything in this section.
    //  I will build my test tables from these Node objects, by writing
    //  the buckets and size fields directly, and read them to check your
    //  results. If you rename or change them, your methods cannot be tested.
    // =========================================================================

    /** One key and its value. The nodes in the same bucket form a chain. */
    private static class Node {
        String key;
        int value;
        Node next;   // the next node in the same chain, or null if this is the last one

        Node(String key, int value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /** The number of buckets in a new table made with HashTable(). */
    public static final int DEFAULT_CAPACITY = 8;

    /** put() calls resize() when size > MAX_LOAD_FACTOR * (the number of buckets). */
    public static final double MAX_LOAD_FACTOR = 0.75;

    /**
     * The buckets. buckets[i] is the first node of the chain in bucket i, or
     * null if bucket i is empty. The number of buckets is buckets.length.
     */
    private Node[] buckets;

    /** The number of keys in the table. */
    private int size;

    /** Creates an empty table with DEFAULT_CAPACITY (8) buckets. */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /** Creates an empty table with the given number of buckets (at least 1). */
    public HashTable(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1, not " + capacity);
        }
        buckets = new Node[capacity];
        size = 0;
    }

    /** Returns the number of keys in the table. */
    public int size() {
        return size;
    }

    /** Returns true if the table holds no keys. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of buckets. */
    public int capacity() {
        return buckets.length;
    }

    /**
     * Returns the index of the bucket whose chain holds key, or -1 if no chain
     * holds it. It searches every bucket, so it is slow: it is here for
     * testing (Main.java uses it). Do not use it in your own methods.
     */
    public int bucketOf(String key) {
        for (int i = 0; i < buckets.length; i++) {
            int steps = 0;
            for (Node node = buckets[i]; node != null && steps < 100000; node = node.next) {
                if (key.equals(node.key)) {
                    return i;
                }
                steps++;
            }
        }
        return -1;
    }

    /**
     * Returns a picture of the table, one line per bucket, in the same style
     * as the diagrams in README.md:
     *
     *     buckets[0] -> null
     *     buckets[1] -> ["owl" | 1] -> ["dog" | 7] -> null
     *     ...
     *
     * System.out.println(table) prints it.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < buckets.length; i++) {
            s.append("buckets[").append(i).append("] -> ");
            int count = 0;
            for (Node node = buckets[i]; node != null; node = node.next) {
                if (count == 1000) {
                    s.append("... (more than 1000 nodes: does the chain loop back on itself?) -> ");
                    break;
                }
                String key = node.key == null ? "null" : "\"" + node.key + "\"";
                s.append("[").append(key).append(" | ").append(node.value).append("] -> ");
                count++;
            }
            s.append("null\n");
        }
        return s.toString();
    }

    // =========================================================================
    //  YOUR CODE: complete the seven methods below.
    //
    //  - Replace each "throw new UnsupportedOperationException(...)" line
    //    with your code.
    //  - You may add private helper methods.
    //  - Do not add fields (instance or static variables).
    //  - Do not use HashMap, Hashtable, HashSet or any other map or set class
    //    from the Java library, and do not use String.hashCode(): you are
    //    writing the hash table and the hash function yourself.
    // =========================================================================

    /**
     * Returns the bucket index of key in a table with the given number of
     * buckets: a number from 0 to capacity - 1. Uses the polynomial hash
     * function from README.md (method 1):
     *
     *     h = 0
     *     for each character c of key, from left to right:
     *         h = (31 * h + c) % capacity
     *     return h
     *
     * You may assume that key is not null and 1 <= capacity <= 1,000,000.
     */
    public static int hash(String key, int capacity) {
        // TODO
        throw new UnsupportedOperationException("hash() is not implemented yet");
    }

    /**
     * Adds key with the given value. If key is already in the table, replaces
     * its value instead (size does not change). After adding a new key, calls
     * resize() if size > MAX_LOAD_FACTOR * buckets.length.
     *
     * @throws IllegalArgumentException if key is null
     */
    public void put(String key, int value) {
        // TODO
        throw new UnsupportedOperationException("put() is not implemented yet");
    }

    /**
     * Returns the value stored with key, or null if key is not in the table.
     * Looks only at the chain in bucket hash(key, buckets.length). Compare
     * keys with equals, not ==. You may assume that key is not null.
     */
    public Integer get(String key) {
        // TODO
        throw new UnsupportedOperationException("get() is not implemented yet");
    }

    /**
     * Returns true if key is in the table, and false otherwise. You may
     * assume that key is not null.
     */
    public boolean containsKey(String key) {
        // TODO
        throw new UnsupportedOperationException("containsKey() is not implemented yet");
    }

    /**
     * Removes key from the table and returns its value. If key is not in the
     * table, returns null and changes nothing. You may assume that key is not
     * null.
     */
    public Integer remove(String key) {
        // TODO
        throw new UnsupportedOperationException("remove() is not implemented yet");
    }

    /**
     * Returns the load factor: the number of keys divided by the number of
     * buckets, as a double (for example, 3 keys in 8 buckets gives 0.375).
     */
    public double loadFactor() {
        // TODO
        throw new UnsupportedOperationException("loadFactor() is not implemented yet");
    }

    /**
     * Doubles the number of buckets, and moves every key (with its value)
     * into its bucket in the new array: bucket hash(key, new number of
     * buckets). size does not change.
     */
    public void resize() {
        // TODO
        throw new UnsupportedOperationException("resize() is not implemented yet");
    }
}
