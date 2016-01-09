/**
 * Wrapper class containing node and weather to increment number of children
 */
public class KDTSTNodeTransaction<Value> {
    private KDTSTNode<Value> node;
    private boolean increment;

    public KDTSTNodeTransaction(KDTSTNode<Value> n, boolean i) {
        node = n;
        increment = i;
    }

    public KDTSTNode<Value> getNode() {
        return node;
    }

    public boolean increment() {
        return increment;
    }
}
