import java.util.*;
import java.util.stream.Collectors;

public class CardUtils {
    public static List<Card> CreateCards(String cards) {
        List<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.length(); i++) {
            String s = cards.substring(i, i + 1);
            if (!" ".equals(s)) list.add(new Card(s));
        }
        return list;
    }

    /**
     * 这个方法的作用是统计list中每张牌出现的次数
     *
     * @param list
     * @return
     */
    public static List<CardGroup> createGroup(List<Card> list) {
        List<CardGroup> groups = new ArrayList<>();
        LinkedList<Card> temp = new LinkedList<>(list);
        Map<Integer, Integer> map = new HashMap<>();// key=cardNum value=sum
        while (temp.size() > 0) {
            final Card card = temp.removeLast();
            final Integer sum = map.get(card.num);
            if (sum == null)
                map.put(card.num, 1);
            else
                map.put(card.num, sum.intValue() + 1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            groups.add(new CardGroup(entry.getKey(), entry.getValue()));
        }
        return groups;
    }

    /**
     * 删除大小王 返回新的List<CardGroup>
     *
     * @param list
     * @return
     */
    public static List<CardGroup> removeJokers(List<CardGroup> list) {
        return list.stream().filter(cardGroup -> cardGroup.num != GameConstants.NUM_JOKER).collect(Collectors.toList());
    }


    public static int indexOfById(List<Card> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) return i;
        }
        return -1;
    }

    public static int indexOfByNum(List<Card> list, int num) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num) return i;
        }
        return -1;
    }

    /**
     * 获取list中第一张为num的牌
     *
     * @param list
     * @param num
     * @return
     */
    public static Card getCardByNum(List<Card> list, int num) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num)
                return list.get(i);
        }
        return null;
    }

    /**
     * 从list头部开始搜索 返回len张为num的牌（list必须是排好序的）
     *
     * @param list
     * @param num
     * @param len  长度
     * @return
     */
    public static List<Card> getCardByNum(List<Card> list, int num, int len) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).num == num)
                return list.subList(i, i + len);// subList后的list不能添加或删除
        }
        return new ArrayList<>();
    }

    /**
     * 获取list中最后一张为num的牌
     *
     * @param list
     * @param num
     * @return
     */
    public static Card getLastCardByNum(List<Card> list, int num) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).num == num)
                return list.get(i);
        }
        return null;
    }

    /**
     * 从list尾部开始搜索 返回len张为num的牌（list必须是排好序的）
     *
     * @param list
     * @param num
     * @param len  长度
     * @return
     */
    public static List<Card> getLastCardByNum(List<Card> list, int num, int len) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).num == num)
                return list.subList(i - len + 1, i + 1);// subList后的list不能添加或删除
        }
        return new ArrayList<>();
    }

    /**
     * 根据索引返回一个新的list
     *
     * @param list
     * @param indexes
     */
    public static List<Card> getCardsByIndexes(List<Card> list, List<Integer> indexes) {
        List<Card> newList = new ArrayList<>(indexes.size());
        for (int i = 0; i < indexes.size(); i++) {
            newList.add(list.get(indexes.get(i)));
        }
        return newList;
    }

    /**
     * 根据索引从list删除 返回一个新的list
     *
     * @param list
     * @param indexes
     */
    public static List<Card> removeByIndex(List<Card> list, List<Integer> indexes) {
        List<Card> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!indexes.contains(i)) newList.add(list.get(i));
        }
        return newList;
    }

    /**
     * 把牌号为num的牌全部移动到前面（list必须是排好序的）
     *
     * @param num
     * @param len 移动的牌数
     */
    public static void moveToHead(List<Card> list, int num, int len) {
        ArrayList<Card> temp = new ArrayList<>();
        int i = 0;
        while (i < list.size() && temp.size() < len) {
            if (list.get(i).num == num) temp.add(list.remove(i));
            else i++;
        }
        list.addAll(0, temp);
    }

    /**
     * 把牌号为num的牌全部移动到后面（list必须是排好序的）
     *
     * @param num
     * @param len 移动的牌数
     */
    public static void moveToEnd(List<Card> list, int num, int len) {
        ArrayList<Card> temp = new ArrayList<>();
        int i = 0;
        while (i < list.size() && temp.size() < len) {
            if (list.get(i).num == num) temp.add(list.remove(i));
            else i++;
        }
        list.addAll(temp);
    }
}
