import java.util.ArrayList;
import java.util.List;

public class CardProduct {
    public final List<Card> list;
    public final int type;
    public final Card maxCard;
    public final List<CardGroup> group;

    public CardProduct(List<Card> list, int type, Card maxCard, List<CardGroup> group) {
        this.list = list;
        this.type = type;
        this.maxCard = maxCard;
        this.group = group;
    }

    public boolean isGreaterThan(CardProduct cardProduct) {
        if (type != GameConstants.TYPE_ERROR && type != GameConstants.TYPE_NULL
                && cardProduct.type == GameConstants.TYPE_NULL) {
            return true;
        } else if (type == GameConstants.TYPE_BOOM) {
            if (cardProduct.type == GameConstants.TYPE_BOOM) {
                if (list.get(0).isJoker() && cardProduct.list.get(0).isJoker()) {
                    //王炸 炸 王炸
                    if (list.size() == cardProduct.list.size()) {
                        int bigJokerSum1 = 0, bigJokerSum2 = 0;
                        for (Card card : list) {
                            if (card.id == GameConstants.ID_JOKER_2) bigJokerSum1++;
                        }
                        for (Card card : cardProduct.list) {
                            if (card.id == GameConstants.ID_JOKER_2) bigJokerSum2++;
                        }
                        return bigJokerSum1 > bigJokerSum2;
                    } else return list.size() > cardProduct.list.size();
                } else if (list.get(0).isJoker() && !cardProduct.list.get(0).isJoker()) {
                    //王炸 炸 炸弹
                    return list.size() * 2 >= cardProduct.list.size();
                } else if (!list.get(0).isJoker() && cardProduct.list.get(0).isJoker()) {
                    //炸弹 炸 王炸
                    return list.size() > cardProduct.list.size() * 2;
                } else if (!list.get(0).isJoker() && !cardProduct.list.get(0).isJoker()) {
                    //炸弹 炸 炸弹
                    if (list.size() > cardProduct.list.size()) return true;
                    else if (list.size() == cardProduct.list.size()) {
                        return list.get(0).num > cardProduct.list.get(0).num;
                    } else return false;
                }
            } else return true;
        } else if (type == cardProduct.type && list.size() == cardProduct.list.size()) {
            if (type == GameConstants.TYPE_1 && maxCard.num == GameConstants.NUM_JOKER && cardProduct.maxCard.num == GameConstants.NUM_JOKER) {
                //由于大小王的num都是相同的 所以比较大小王的话要对比id
                return maxCard.id > cardProduct.maxCard.id;
            } else {
                return maxCard.num > cardProduct.maxCard.num;
            }
        }
        return false;
    }

    private String typeToString() {
        switch (type) {
            case GameConstants.TYPE_ERROR:
                return "错误";
            case GameConstants.TYPE_NULL:
                return "无";
            case GameConstants.TYPE_1:
                return "单牌";
            case GameConstants.TYPE_22:
                return "对子";
            case GameConstants.TYPE_333:
                return "三个";
            case GameConstants.TYPE_BOOM:
                return "炸弹";
            case GameConstants.TYPE_3331:
                return "三带一";
            case GameConstants.TYPE_33322:
                return "三带一对";
            case GameConstants.TYPE_444412:
                return "四带二";
            case GameConstants.TYPE_44441122:
                return "四带两对";
            case GameConstants.TYPE_34567:
                return "顺子";
            case GameConstants.TYPE_334455:
                return "连对";
            case GameConstants.TYPE_333444:
                return "飞机";
            case GameConstants.TYPE_33344412:
                return "飞机带单牌";
            case GameConstants.TYPE_3334441122:
                return "飞机带对子";
        }
        return "未知";
    }

    @Override
    public String toString() {
        return "CardProduct{" +
                "list=" + list +
                ", type=" + typeToString() +
                ", maxCard=" + maxCard +
                ", len=" + list.size() +
                ", group=" + group +
                '}';
    }

    public static CardProduct createNullType() {
        return new CardProduct(new ArrayList<>(), GameConstants.TYPE_NULL, null, null);
    }


}
