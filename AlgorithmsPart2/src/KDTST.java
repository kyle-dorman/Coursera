import edu.princeton.cs.algs4.Queue;

public class KDTST<Value> {
    private String firstTwoLetters;
    private KDTSTNode<Value> root;   // root of TST

    /**
     * Initializes an empty string symbol table.
     */
    public KDTST(KDStringArray s) {
        firstTwoLetters = (new StringBuilder().append(s.charAt(0)).append(s.charAt(1))).toString();
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public KDTSTNode<Value> get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        KDTSTNode<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x;
    }

    // return subtrie corresponding to given key
    private KDTSTNode<Value> get(KDTSTNode<Value> x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.getChar())      return get(x.getLeft(),  key, d);
        else if (c > x.getChar())      return get(x.getRight(), key, d);
        else if (d < key.length() - 1) return get(x.getMid(),   key, d+1);
        else                           return x;
    }

    public int longestPrefixOf(KDStringArray query, int startIndex) {
        if (query == null) return 0;
        else if (query.length() <= startIndex) return 1;

        int length = startIndex;
        KDTSTNode<Value> x = root;
//        KDTSTNode<Value> previousNode = null;
        boolean hasChildren = false;
        int i = startIndex;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if (c < x.getChar()) {
                x = x.getLeft();
                hasChildren = x != null;
            }
            else if (c > x.getChar()) {
                x = x.getRight();
                hasChildren = x != null;
            }
            else {
                i++;
                if (x.getVal() != null) length = i;
                hasChildren = x.hasChildren();
                x = x.getMid();
            }
        }
//        hasChildren = previousNode != null && previousNode.hasChildren();
        if (query.length() == length && hasChildren) {
            return 3;
        } else if (query.length() == length && !hasChildren) {
            return 2;
        } else if (query.length() != length && hasChildren) {
            return 1;
        } else {
            return 0;
        }
        /*
            odd has child
            even no children
            greater than 1, match
            less than 1, no match
         */
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(KDStringArray key, int startIndex, Value val) {
        root = put(root, key, val, startIndex).getNode();
    }

    private KDTSTNodeTransaction<Value> put(KDTSTNode<Value> x, KDStringArray key, Value val, int keyIndex) {
        char c = key.charAt(keyIndex);
        boolean increment = false;
        if (x == null) {
            x = new KDTSTNode<Value>();
            x.setChar(c);
        }
        if (c < x.getChar()) {
            KDTSTNodeTransaction<Value> transaction = put(x.getLeft(), key, val, keyIndex);
            if (transaction.increment()) {
                x.incrementChildCount();
                increment = true;
            }
            x.setLeft(transaction.getNode());
        }
        else if (c > x.getChar()) {
            KDTSTNodeTransaction<Value> transaction = put(x.getRight(), key, val, keyIndex);
            if (transaction.increment()) {
                x.incrementChildCount();
                increment = true;
            }
            x.setRight(transaction.getNode());
        }
        else if (keyIndex < key.length() - 1) {
            KDTSTNodeTransaction<Value> transaction = put(x.getMid(), key, val, keyIndex + 1);
            if (transaction.increment()) {
                x.incrementChildCount();
                increment = true;
            }
            x.setMid(transaction.getNode());
        }
        else {
            if (x.getVal() == null) {
                increment = true;
            }
            x.setVal(val);
        }
        return new KDTSTNodeTransaction<Value>(x, increment);
    }

    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     * To iterate over all of the keys in the symbol table named <tt>st</tt>,
     * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
     * @return all keys in the sybol table as an <tt>Iterable</tt>
     */
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(firstTwoLetters), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     *     as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> queue = new Queue<String>();
        KDTSTNode<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.getVal() != null) queue.enqueue(prefix);
        collect(x.getMid(), new StringBuilder(firstTwoLetters + prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(KDTSTNode<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.getLeft(),  prefix, queue);
        if (x.getVal() != null) queue.enqueue(prefix.toString() + x.getChar());
        collect(x.getMid(),   prefix.append(x.getChar()), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.getRight(), prefix, queue);
    }
}

