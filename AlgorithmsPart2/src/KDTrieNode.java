
public class KDTrieNode {
    private KDTST<Boolean> tree;

    public KDTrieNode(KDStringArray s) {
        tree = new KDTST<Boolean>(s);
    }
    public KDTST<Boolean> getTree() {
        return tree;
    }
}