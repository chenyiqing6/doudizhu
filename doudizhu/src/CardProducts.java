import java.util.ArrayList;
import java.util.List;

public class CardProducts {
    public final List<CardProduct> list;

    public CardProducts() {
        list = new ArrayList<>();
    }

    public boolean add(CardProduct product) {
        return list.add(product);
    }

    public boolean addAll(List<CardProduct> list) {
        return this.list.addAll(list);
    }

    /**
     * @param product
     * @return 打得过则返回CardProduct在list里的索引 否则返回-1
     */
    public int isGreaterThan(CardProduct product) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).isGreaterThan(product))
                return i;
        return -1;
    }

    @Override
    public String toString() {
        return list.size() + " " + list;
    }

}
