import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Cc代表大小王，0代表10
 * 大小写jqk都可以代表JQK
 * Aa1都代表A
 * <p>
 * 多少副牌sum、是否包含345contains345可以在main方法中更改
 * <p>
 * 四炸<双王炸<五炸<六炸<三王炸<七炸<八炸<四王炸
 * 并且[大王, 大王]是可以打赢[大王, 小王]和[小王, 小王]的
 */
public class Play1 {

    private static List<Card> player;//玩家
    private static List<Card> left;//上家
    private static List<Card> right;//下家
    private static CardProduct last = CardProduct.createNullType();

    //Cc代表大小王，0代表10

    /**
     * 发牌
     *
     * @param sum         多少副牌
     * @param contains345 是否包含345
     */
    public static void licensing(int sum, boolean contains345) {

        player = new ArrayList<>();
        left = new ArrayList<>();
        right = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();//牌堆
        for (int i = 0; i < sum; i++) {
            for (int j = contains345 ? GameConstants.ID_3_1 : GameConstants.ID_6_1; j < 54; j++) {
                list.add(j);
            }
        }
        //发牌
        while (list.size() > 0) {
            player.add(new Card(list.remove((int) (Math.random() * list.size()))));
            left.add(new Card(list.remove((int) (Math.random() * list.size()))));
            right.add(new Card(list.remove((int) (Math.random() * list.size()))));
        }
        player.sort((o1, o2) -> o2.id - o1.id);
        left.sort((o1, o2) -> o2.id - o1.id);
        right.sort((o1, o2) -> o2.id - o1.id);
    }

    public static List<Integer> searchIndexByNum(List<Card> list, List<Card> card) {
        if (card.size() > list.size()) return null;
        //在list中搜索card
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
        int sum = 1;//多少副牌
        boolean contains345 = true;//是否包含345
        licensing(sum, contains345);
        System.out.println("您的扑克牌:" + player);
        System.out.println("上家扑克牌:" + left);
        System.out.println("下家扑克牌:" + right);
        boolean playing = true;
        boolean toLeft = false;//出牌
        boolean toMe = false;//Math.random() < 0.5;//我出
        boolean toRight = false;
        boolean l = true;//主动出牌
        boolean m = false;
        boolean r = false;
        boolean isContinue = false;
        boolean isMe = false;
        Scanner scanner = new Scanner(System.in);
        while (playing) {

            //其作用就是让玩家在已经进入过toMe的情况下再重新进入一次toMe
            if (isMe) {//输入?(提示),或输入(查看），或出牌不符合规则--》还该自己出牌
                isMe = false;
                toLeft = false;
                toMe = true;
                toRight = false;
                l = false;
                m = false;
                r = false;

            }

            if (l) {
                System.out.println("=====================================上家主动出牌==================================");
                System.out.println("上家当前扑克牌:" + left);
                last = CardProduct.createNullType();
                Prompt prompt = new Prompt(left, last);

                //如果上家有可以出的牌
                if (prompt.prompt.size() > 0) {
                    //将其中最小的一组牌牌分牌型整理好
                    CardProducts cardProducts = CardsFactory.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);//打得过则返回CardProduct在list里的索引 否则返回-1
                    if (index >= 0) {
                        last = cardProducts.list.get(index);  //将left出的这组牌作为上家的牌（对于下一个出牌的我来说left就是上家）
                        left.removeAll(last.list);//将出完的牌移除
                        System.out.println("上家主动出牌:" + last.list);
                        //如果left牌出完了
                        if (left.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("上家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        toLeft = false;
                        toMe = true;//轮到我来接牌

                    }

                }


            }

            if (toLeft) {
                System.out.println("=============上家=================");
                System.out.println("上家当前扑克牌:" + left);

                Prompt prompt = new Prompt(left, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardsFactory.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        left.removeAll(last.list);
                        System.out.println("上家出牌:" + last.list);
                        if (left.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("上家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        m = false;
                        r = false;
                        l = true;

                        toLeft = false;
                        toMe = true;
                    }

                } else {
                    System.out.println("上家不要");

                    toLeft = false;
                    toMe = true;
                }

            }

            if (m) {
                System.out.println("================================玩家主动出牌======================================");

                last = CardProduct.createNullType();

                System.out.println("上一次出牌:" + last);
                System.out.println("您当前的扑克牌:" + player);
                System.out.println("请您出牌：(输入?提示 输入,偷看电脑的牌)");
                String line = scanner.nextLine();
                if (line == null || line.length() == 0) {
                    isMe = true;
                    continue;
                } else if ("?".equals(line)) {
                    System.out.println("提示:" + new Prompt(player, last).prompt);
                    isMe = true;
                    continue;
                } else if (",".equals(line)) {
                    System.out.println("上家当前扑克牌:" + left);
                    System.out.println("下家当前扑克牌:" + right);
                    isMe = true;
                    continue;
                }
                List<Integer> indexes = searchIndexByNum(player, CardUtils.CreateCards(line));
                if (indexes == null) {
                    System.out.println("您输入的扑克牌无效请重新输入");
                    isMe = true;
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
                        System.out.println("=======================================================================");
                        System.out.println("你赢啦!");
                        break;
                    }

                    toMe = false;
                    toRight = !toRight;
                } else {
                    System.out.println("不符合游戏规则:" + cardProducts);
                    isMe = true;
                    continue;
                }
            }

            if (toMe) {
                System.out.println("=============玩家=================");

                System.out.println("上一次出牌:" + last);
                System.out.println("您当前的扑克牌:" + player);
                //System.out.println("请您出牌：(输入.不出 输入?提示 输入,偷看电脑的牌)");
                System.out.println("请您出牌：(输入.不出 输入?提示 )");
                String line = scanner.nextLine();
                //如果没输入东西就按了Enter键，系统会重新回到toMe
                if (line == null || line.length() == 0) {
                    isMe = true;
                    continue;
                } else if (".".equals(line)) {
                    toMe = false;
                    toRight = true;  //如果我不出牌，就轮到下家出牌

                } else if ("?".equals(line)) {
                    System.out.println("提示:" + new Prompt(player, last).prompt);
                    isMe = true; //重启toMe
                    continue;
                }
//                else if (",".equals(line)) {
//                    System.out.println("上家当前扑克牌:" + left);
//                    System.out.println("下家当前扑克牌:" + right);
//                    isMe = true;  //偷看完电脑的牌之后再来一次
//                    continue;
//            }
                //一切正常，我开始出牌
                else {
                    List<Integer> indexes = searchIndexByNum(player, CardUtils.CreateCards(line));
                    if (indexes == null) {
                        System.out.println("您输入的扑克牌无效请重新输入");
                        isMe = true;
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
                            System.out.println("=======================================================================");
                            System.out.println("你赢啦!");
                            break;
                        }
                        l = false;
                        r = false;
                        m = true;

                        toMe = false;
                        toRight = true;
                    } else {
                        System.out.println("不符合游戏规则:" + cardProducts);
                        isMe = true;
                        continue;
                    }
                }
            }

            if (r) {
                System.out.println("================================下家主动出牌=======================================");

                System.out.println("下家当前扑克牌:" + right);
                last = CardProduct.createNullType();

                Prompt prompt = new Prompt(right, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardsFactory.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        right.removeAll(last.list);
                        System.out.println("下家主动出牌:" + last.list);
                        if (right.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("下家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        toRight = false;
                        toLeft = true;
                    }

                }
            }

            if (toRight) {
                System.out.println("=============下家=================");

                System.out.println("下家当前扑克牌:" + right);

                Prompt prompt = new Prompt(right, last);

                if (prompt.prompt.size() > 0) {
                    CardProducts cardProducts = CardsFactory.builder(prompt.prompt.get(0));
                    int index = cardProducts.isGreaterThan(last);
                    if (index >= 0) {
                        last = cardProducts.list.get(index);
                        right.removeAll(last.list);
                        System.out.println("下家出牌:" + last.list);
                        if (right.size() == 0) {
                            System.out.println("=======================================================================");
                            System.out.println("下家赢了!");
                            System.out.println("你输啦!");
                            break;
                        }
                        l = false;
                        m = false;
                        r = true;

                        toRight = false;
                        toLeft = true;

                    }

                } else {
                    System.out.println("下家不要");

                    toRight = false;
                    toLeft = true;
                }


            }

        }
    }

}
