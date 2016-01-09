/**
 * Created by kyledorman on 1/6/16.
 */
public class KDStringArray {
    private char[] wordList;
    private int[] lengthOfAddition;
    private int currentIndex = -1;

    public KDStringArray(int length) {
        wordList = new char[length * 2];
        lengthOfAddition = new int[length * 2];
    }

    public void add(String s) {
        if (currentIndex + s.length() >= wordList.length) {
            throw new IndexOutOfBoundsException("For word: " + this.toString());
        }
        for (int i = 0; i < s.length(); i++) {
            currentIndex++;
            wordList[currentIndex] = s.charAt(i);
            lengthOfAddition[currentIndex] = s.length();
        }
    }

    public void pop() {
        int stringLength = lengthOfAddition[currentIndex];
        currentIndex -= stringLength;
    }

    public char charAt(int i) {
        if (i > currentIndex) {
            throw new IndexOutOfBoundsException();
        }
        return wordList[i];
    }

    public int length() { return currentIndex + 1; }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < currentIndex + 1; i++) {
            sb.append(wordList[i]);
        }
        return sb.toString();
    }
}
