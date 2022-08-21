public class CardGroup {
    public final int num;
    public int sum;

    public CardGroup(int num, int sum) {
        this.num = num;
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "(" + "num=" + num + ", sum=" + sum + ')';
    }
}
