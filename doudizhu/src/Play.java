import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Play {
    private static List<Card> player;     //储存玩家的牌
    private static List<Card> bot;
    private static CardProduct last = CardProduct.createNullType();

    /**
     * 发牌
     *
     * @param sum         多少副牌
     * @param contains345 是否包含345
     */
    public static void licensing(int sum, boolean contains345) {
        player = new ArrayList<>();
        bot = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            for (int j = contains345 ? GameConstants.ID_3_1 : GameConstants.ID_6_1; j < 54; j++) {
                list.add(j);
            }
        }
        while (list.size() > 0) {
            player.add(new Card(list.remove((int) (Math.random() * list.size()))));
            bot.add(new Card(list.remove((int) (Math.random() * list.size()))));
        }
        player.sort((o1, o2) -> o2.id - o1.id);
        bot.sort((o1, o2) -> o2.id - o1.id);
    }

    public static List<Integer> searchIndexByNum(List<Card> list, List<Card> card) {
        if (card.size() > list.size()) return null;
        int[] cardNum = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cardNum[i] = list.get(i).num;
        }
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < card.size(); i++) {
            if (card.get(i).num > GameConstants.NUM_2) {
                final int id = card.get(i).id;
                for (int j = 0; j < list.size(); j++) {
                    if (card.get(i).id == list.get(j).id && !indexes.contains(j)) {
                        indexes.add(j);
                        break;
                    }
                }
            } else {
                for (int j = 0; j < cardNum.length; j++) {
                    if (card.get(i).num == cardNum[j] && !indexes.contains(j)) {
                        indexes.add(j);
                        break;
                    }
                }
            }
        }
        if (indexes.size() != card.size()) return null;
        return indexes;
    }

    public static void main(String[] args) {
        licensing(1, false);
        System.out.println("您的扑克牌:" + player);
        System.out.println("电脑扑克牌:" + bot);
        boolean playing = true;
        boolean toMe = Math.random() < 0.5;
        Scanner scanner = new Scanner(System.in);
        while (playing) {
            System.out.println("=======================================================================");
            if (toMe) {
                System.out.println("上家出牌:" + last);
                System.out.println("您当前的扑克牌:" + player);
                System.out.println("请您出牌：(输入.不出 输入?提示 输入,偷看电脑的牌)");
                String line = scanner.nextLine();
                if (line == null || line.length() == 0) continue;
                else if (".".equals(line)) {
//					if (last.type==GameConstants.TYPE_NULL)
                    toMe = !toMe;
                    last = CardProduct.createNullType();
                    continue;
                } else if ("?".equals(line)) {
                    System.out.println("提示:" + new Prompt(player, last).prompt);
                    continue;
                } else if (",".equals(line)) {
                    System.out.println("电脑当前扑克牌:" + bot);
                    continue;
                }
                List<Integer> indexes = searchIndexByNum(player, CardUtils.CreateCards(line));
                if (indexes == null) {
                    System.out.println("您输入的扑克牌无效请重新输入");
                    continue;
                }
                CardProducts cardProducts = CardsFactory.builder(CardUtils.getCardsByIndexes(player, indexes));
                int index = cardProducts.isGreaterThan(last);
                if (index >= 0) {
                    CardProduct newCardProduct = cardProducts.list.get(index);
                    last = newCardProduct;
                    player.removeAll(last.list);
                    System.out.println("出牌成功:" + last);
                    if (player.size() == 0) {
                        System.out.println("你赢啦!");
                        playing = false;
                    }
                    toMe = !toMe;
                } else {
                    System.out.println("不符合游戏规则:" + cardProducts);
                    continue;
                }
            } else {
                System.out.println("电脑当前扑克牌:" + bot);
                Prompt prompt = new Prompt(bot, last);
                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardsFactory.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        bot.removeAll(last.list);
                        System.out.println("电脑出牌:" + last.list);
                        if (bot.size() == 0) {
                            System.out.println("你输啦!");
                            playing = false;
                        }
                        toMe = !toMe;
                        continue;
                    }
                    System.out.println("异常:prompt.prompt.size() > 0且cardProducts.isGreaterThan(last) < 0");
                    System.out.println(prompt);
                    System.out.println(cardProducts);
                    return;
                } else {
                    System.out.println("电脑不要");
                    last = CardProduct.createNullType();
                    toMe = !toMe;
                }

            }
        }
    }


}
