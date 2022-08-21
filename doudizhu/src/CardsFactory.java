import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CardsFactory {

    private final List<Card> list;//排序方式为 order by id desc
    private final CardProducts products;
    private final List<CardGroup> groups;//排序方式为 order by sum desc,num desc

    private CardsFactory(List<Card> list) {
        this.list = list;

        this.list.sort((o1, o2) -> o2.id - o1.id);
        products = new CardProducts();
        groups = CardUtils.createGroup(list);
        Collections.sort(groups, (o1, o2) -> {
            //order by sum desc,num desc
            final int o = o2.sum - o1.sum;
            if (o != 0) return o;
            return o2.num - o1.num;
        });

        if (is1()) return;
        if (is22()) return;
        if (is333()) return;
        if (isBoom()) return;
        if (is3331()) return;
        if (is33322()) return;
        if (is34567()) return;
        if (is334455()) return;
        //33334444可被分为飞机和四带两对
        boolean flag = false;
        if (is444412()) flag = true;
        if (is44441122()) flag = true;
        if (is333444()) flag = true;
        if (is33344412()) flag = true;
        if (is3334441122()) flag = true;
        if (!flag) {
            List<Card> sorted = new ArrayList<>(list);
            products.add(new CardProduct(sorted, GameConstants.TYPE_ERROR, list.get(0), groups));
        }
    }

    /**
     * 是否全部牌的num都一样
     *
     * @return
     */
    public boolean isEqualsAll() {
        return groups.size() == 1;
    }

    public boolean is1() {
        if (list.size() == 1) {
            List<Card> sorted = new ArrayList<>(list);
            products.add(new CardProduct(sorted, GameConstants.TYPE_1, list.get(0), groups));
            return true;
        }
        return false;
    }

    public boolean is22() {
        if (list.size() == 2 && !list.get(0).isJoker() && isEqualsAll()) {
            List<Card> sorted = new ArrayList<>(list);
            products.add(new CardProduct(sorted, GameConstants.TYPE_22, list.get(0), groups));
            return true;
        }
        return false;
    }

    public boolean is333() {
        if (list.size() == 3 && !list.get(0).isJoker() && isEqualsAll()) {
            List<Card> sorted = new ArrayList<>(list);
            products.add(new CardProduct(sorted, GameConstants.TYPE_333, list.get(0), groups));
            return true;
        }
        return false;
    }

    public boolean isBoom() {
        if (list.size() >= 2) {
            if (groups.size() == 1) {
                if (groups.get(0).num == GameConstants.NUM_JOKER && groups.get(0).sum >= 2 || groups.get(0).sum >= 4) {
                    List<Card> sorted = new ArrayList<>(list);
                    products.add(new CardProduct(sorted, GameConstants.TYPE_BOOM, list.get(0), groups));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean is3331() {
        if (list.size() == 4) {
            if (groups.get(0).sum == 3 && groups.get(0).num != GameConstants.NUM_JOKER) {
                List<Card> sorted = new ArrayList<>(list);
                CardUtils.moveToEnd(sorted, groups.get(1).num, groups.get(1).sum);
                products.add(new CardProduct(sorted, GameConstants.TYPE_3331, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is33322() {
        if (list.size() == 5) {
            if (CardUtils.indexOfByNum(list, GameConstants.NUM_JOKER) < 0)
                if (groups.get(0).sum == 3 && groups.get(1).sum == 2 && groups.get(0).num != GameConstants.NUM_JOKER) {
                    List<Card> sorted = new ArrayList<>(list);
                    CardUtils.moveToEnd(sorted, groups.get(1).num, groups.get(1).sum);
                    products.add(new CardProduct(sorted, GameConstants.TYPE_33322, sorted.get(0), groups));
                    return true;
                }
        }
        return false;
    }

    public boolean is444412() {
        if (list.size() == 6) {
            if (groups.get(0).sum >= 4) {
                List<Card> sorted = new ArrayList<>(list);
                CardUtils.moveToHead(sorted, groups.get(0).num, 4);
                products.add(new CardProduct(sorted, GameConstants.TYPE_444412, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is44441122() {
        if (list.size() == 8) {
            List<CardGroup> temp = new ArrayList<>(groups);
            temp.sort((o1, o2) -> o2.num - o1.num);//按num逆序
            CardGroup ge4 = null;//找到num最大 且sum>=4的牌
            for (CardGroup cardGroup : temp) {
                if (cardGroup.sum >= 4) {
                    ge4 = cardGroup;
                    break;
                }
            }
            if (ge4 != null) {
                for (CardGroup cardGroup : temp) {
                    if (cardGroup.sum % 2 != 0) return false;
                }
                List<Card> sorted = new ArrayList<>(list);
                CardUtils.moveToHead(sorted, ge4.num, 4);
                products.add(new CardProduct(sorted, GameConstants.TYPE_44441122, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is34567() {
        if (list.size() >= 5) {
            if (list.get(0).id < GameConstants.ID_2_1) {
                for (int i = 0, len = list.size() - 1; i < len; i++) {
                    Card card1 = list.get(i);
                    Card card2 = list.get(i + 1);
                    if (card1.num - card2.num != 1) return false;
                }
                List<Card> sorted = new ArrayList<>(list);
                products.add(new CardProduct(sorted, GameConstants.TYPE_34567, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is334455() {
        if (list.size() >= 6 && list.get(0).id < GameConstants.ID_2_1) {
            if (groups.get(0).num < GameConstants.NUM_2 && groups.get(0).sum == 2) {
                for (int i = 0, len = groups.size() - 1; i < len; i++) {
                    if (groups.get(i).num - groups.get(i + 1).num != 1 || groups.get(i + 1).sum != 2)
                        return false;
                }
                List<Card> sorted = new ArrayList<>(list);
                products.add(new CardProduct(sorted, GameConstants.TYPE_334455, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is333444() {
        if (list.size() % 3 == 0 && list.size() >= 6 && groups.size() >= 2) {
            if (groups.get(0).num < GameConstants.NUM_2 && groups.get(0).sum == 3) {
                for (int i = 0, len = groups.size() - 1; i < len; i++) {
                    if (groups.get(i).num - groups.get(i + 1).num != 1 || groups.get(i + 1).sum != 3) return false;
                }
                List<Card> sorted = new ArrayList<>(list);
                products.add(new CardProduct(sorted, GameConstants.TYPE_333444, sorted.get(0), groups));
                return true;
            }
        }
        return false;
    }

    public boolean is33344412() {
        if (list.size() % 4 == 0 && list.size() >= 8) {
            List<CardGroup> ge3 = groups.stream().filter(cardGroup -> cardGroup.sum >= 3).sorted((o1, o2) -> o2.num - o1.num).collect(Collectors.toList());

            ArrayList<ArrayList<CardGroup>> base = new ArrayList<>();//可能不止一个三连 如333 444 666 777 888 999 000 JJJ
            ArrayList<CardGroup> continuous = new ArrayList<>();
            for (int i = 0, len = ge3.size() - 1; i < len; i++) {
                final CardGroup group1 = ge3.get(i);
                final CardGroup group2 = ge3.get(i + 1);
                if (group1.num - group2.num == 1 && group1.num < GameConstants.NUM_2) {
                    if (!continuous.contains(group1))
                        continuous.add(group1);
                    continuous.add(group2);
                    if (i == len - 1) base.add(continuous);
                } else {
                    base.add(continuous);
                    continuous = new ArrayList<>();
                }
            }
            for (ArrayList<CardGroup> plane : base) {
                while (plane.size() >= 2) {
                    if (plane.size() * 4 == list.size()) {
                        List<Card> sorted = new ArrayList<>(list);
                        for (int i = plane.size() - 1; i >= 0; i--) {
                            CardUtils.moveToHead(sorted, plane.get(i).num, 3);
                        }
                        products.add(new CardProduct(sorted, GameConstants.TYPE_33344412, sorted.get(0), groups));
                        return true;
                    } else {
                        plane.remove(plane.size() - 1);//切一节飞机尾
                    }
                }
            }
        }
        return false;
    }

    public boolean is3334441122() {
        if (list.size() % 5 == 0 && list.size() >= 10) {
            List<CardGroup> ge3 = groups.stream().filter(cardGroup -> cardGroup.sum >= 3).sorted((o1, o2) -> o2.num - o1.num).collect(Collectors.toList());

            ArrayList<ArrayList<CardGroup>> base = new ArrayList<>();//可能不止一个三连 如333 444 666 777 888 999 000 JJJ
            ArrayList<CardGroup> continuous = new ArrayList<>();
            for (int i = 0, len = ge3.size() - 1; i < len; i++) {
                final CardGroup group1 = ge3.get(i);
                final CardGroup group2 = ge3.get(i + 1);
                if (group1.num - group2.num == 1 && group1.num < GameConstants.NUM_2) {
                    if (!continuous.contains(group1))
                        continuous.add(group1);
                    continuous.add(group2);
                    if (i == len - 1) base.add(continuous);
                } else {
                    base.add(continuous);
                    continuous = new ArrayList<>();
                }
            }
            for (ArrayList<CardGroup> plane : base) {
                int cut = 0;
                while (plane.size() - cut >= 2) {
                    if ((plane.size() - cut) * 5 == list.size()) {
                        for (int i = 0; i < cut + 1; i++) {
                            List<CardGroup> temp = plane.subList(i, i + plane.size() - cut);
                            //在this.groups里找到temp里存在的元素 并让他的sum-3 意思大概就是把当机身的牌从this.groups取走3张
                            List<CardGroup> diff = this.groups.stream()
                                    .map(cardGroup -> {
                                        if (temp.stream().anyMatch(group -> group.num == cardGroup.num))
                                            return new CardGroup(cardGroup.num, cardGroup.sum - 3);//为了不影响到原来的集合 这里选择new一个
                                        else return cardGroup;
                                    })
                                    .collect(Collectors.toList());
                            //判断剩下的牌的sum是否都是偶数
                            if (diff.stream().allMatch(cardGroup -> cardGroup.sum % 2 == 0)) {
                                List<Card> sorted = new ArrayList<>(list);
                                for (int j = temp.size() - 1; j >= 0; j--) {
                                    CardUtils.moveToHead(sorted, temp.get(j).num, 3);
                                }
                                products.add(new CardProduct(sorted, GameConstants.TYPE_3334441122, sorted.get(0), groups));
                                return true;
                            }
                        }
                    }
                    cut++;
                }
            }
        }
        return false;
    }

    public static CardProducts builder(List<Card> list) {
        return new CardsFactory(list).products;
    }

    /**
     * 测试用
     *
     * @param cards
     * @return
     */
    public static CardProducts builder(String cards) {
        List<Card> list = new ArrayList<>();
        for (int i = 0; i < cards.length(); i++) {
            String s = cards.substring(i, i + 1);
            if (!" ".equals(s)) list.add(new Card(s));
        }
        return builder(list);
    }

}
