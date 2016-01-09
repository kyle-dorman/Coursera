import edu.princeton.cs.algs4.Queue;

public class KDTrie {
    private static final int R = 26;
    private KDTrieNode[] nodes = new KDTrieNode[KDTrie.R * KDTrie.R];
    private boolean[] firstLetter = new boolean[KDTrie.R];

    public KDTrie() { }

    public KDTSTNode<Boolean> get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() < 2) { throw new IllegalArgumentException("key must have length >= 1"); }
        KDTrieNode trieNode = nodes[getIndex(key.substring(0, 2))];
        if (trieNode == null) {
            return null;
        }
        KDTSTNode<Boolean> tstNode = trieNode.getTree().get(key.substring(2));
        if (tstNode == null) return null;
        return tstNode;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public int longestPrefixOf(KDStringArray query) {
        if (query == null || query.length() == 0) return 0;
        else if (query.length() == 1 && firstLetter[getValue(query.charAt(0))]) return 1;
        else if (!firstLetter[getValue(query.charAt(0))]) return 0;
        KDTrieNode trieNode = nodes[getIndex(query)];

        if (trieNode == null) return 0;
        else return trieNode.getTree().longestPrefixOf(query, 2);
    }

    public void put(KDStringArray key) {
        if (key.length() <= 2) { return; }
        int index = getIndex(key);
        KDTrieNode trieNode = nodes[index];
        if (trieNode == null) {
            nodes[index] = new KDTrieNode(key);
            trieNode = nodes[index];
        }
        trieNode.getTree().put(key, 2, true);
        firstLetter[getValue(key.charAt(0))] = true;
    }

    public Iterable<String> keys() {
        Queue<String> results = new Queue<String>();
        for (KDTrieNode n : nodes) {
            if (n != null) {
                for (String s : n.getTree().keys()) {
                    results.enqueue(s);
                }
            }
        }
        return results;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();

        for (KDTrieNode n : getTreesFromPrefix(prefix)) {
            if (n != null) {
                for (String s : n.getTree().keysWithPrefix(prefix.length() > 2 ? prefix.substring(2) : "")) {
                    results.enqueue(s);
                }
            }
        }
        return results;
    }

    private KDTrieNode[] getTreesFromPrefix(String prefix) {
        if (prefix.length() == 0) {
            return nodes;
        } else if (prefix.length() > 1) {
            return new KDTrieNode[]{ nodes[getIndex(prefix)] };
        } else {
            int index = getValue(prefix.charAt(0));
            if (index == -1) throw new IllegalArgumentException("Invalid character " + prefix);

            KDTrieNode[] result = new KDTrieNode[KDTrie.R];
            System.arraycopy(nodes, (index * KDTrie.R), result, 0, KDTrie.R);
            return result;
        }
    }

    private int getIndex(KDStringArray s) {
        int firstIndex = getValue(s.charAt(0));
        int secondIndex = getValue(s.charAt(1));

        if (firstIndex == -1 || secondIndex == -1) {
            throw new IllegalArgumentException("Invalid first or second letter " + s);
        }

        return (firstIndex * KDTrie.R) + secondIndex;
    }

    private int getIndex(String s) {
        int firstIndex = getValue(s.charAt(0));
        int secondIndex = getValue(s.charAt(1));

        if (firstIndex == -1 || secondIndex == -1) {
            throw new IllegalArgumentException("Invalid first or second letter " + s);
        }

        return (firstIndex * KDTrie.R) + secondIndex;
    }

    private int getValue(char c) {
        int cInt = (int) c;
        if (cInt > 90) { return -1; }
//        StdOut.println(c);
//        StdOut.println(cInt);
        return cInt - 65;
//        switch (c) {
//            case 'A': return 0;
//            case 'B': return 1;
//            case 'C': return 2;
//            case 'D': return 3;
//            case 'E': return 4;
//            case 'F': return 5;
//            case 'G': return 6;
//            case 'H': return 7;
//            case 'I': return 8;
//            case 'J': return 9;
//            case 'K': return 10;
//            case 'L': return 11;
//            case 'M': return 12;
//            case 'N': return 13;
//            case 'O': return 14;
//            case 'P': return 15;
//            case 'Q': return 16;
//            case 'R': return 17;
//            case 'S': return 18;
//            case 'T': return 19;
//            case 'U': return 20;
//            case 'V': return 21;
//            case 'W': return 22;
//            case 'X': return 23;
//            case 'Y': return 24;
//            case 'Z': return 25;
//            default: return -1;
//        }
    }
}
