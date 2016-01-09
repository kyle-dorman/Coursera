/**
 * Created by kyledorman on 12/30/15.
 */
public class KDTSTNode<Value> {
    private char c;                        // character
    private KDTSTNode<Value> left, mid, right;  // left, middle, and right subtries
    private Value val;                     // value associated with string
    private int childCount = 0;

    public KDTSTNode() { }

    public char getChar() { return c; }
    public KDTSTNode<Value> getRight() { return right; }
    public KDTSTNode<Value> getLeft() { return left; }
    public KDTSTNode<Value> getMid() { return mid; }
    public boolean hasChildren() { return childCount > 0; }
    public int getChildCount() { return childCount; }
    public Value getVal() { return val; }

    public void incrementChildCount() { childCount++; }

    public void setChar(char chr) { this.c = chr; }

    public void setLeft(KDTSTNode<Value> left) { this.left = left; }

    public void setMid(KDTSTNode<Value> mid) { this.mid = mid; }

    public void setRight(KDTSTNode<Value> right) { this.right = right; }

    public void setVal(Value val) { this.val = val; }
}