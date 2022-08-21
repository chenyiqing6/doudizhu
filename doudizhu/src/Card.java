public class Card {

    public final int id;
    public final int num;

    public Card(int id) {
        this.id = id;
        num = id / 4;
    }

    /**
     * 这个构造方法是用来做测试用的
     *
     * @param s
     */
    public Card(String s) {
        if ("C".equals(s)) {
            id = GameConstants.ID_JOKER_2;
            num = id / 4;
            return;
        }
        switch (s) {
            case "3":
                num = GameConstants.NUM_3;
                break;
            case "4":
                num = GameConstants.NUM_4;
                break;
            case "5":
                num = GameConstants.NUM_5;
                break;
            case "6":
                num = GameConstants.NUM_6;
                break;
            case "7":
                num = GameConstants.NUM_7;
                break;
            case "8":
                num = GameConstants.NUM_8;
                break;
            case "9":
                num = GameConstants.NUM_9;
                break;
            case "0":
                num = GameConstants.NUM_10;
                break;
            case "J":
            case "j":
                num = GameConstants.NUM_J;
                break;
            case "Q":
            case "q":
                num = GameConstants.NUM_Q;
                break;
            case "K":
            case "k":
                num = GameConstants.NUM_K;
                break;
            case "1":
            case "A":
            case "a":
                num = GameConstants.NUM_A;
                break;
            case "2":
                num = GameConstants.NUM_2;
                break;
            case "c":
                num = GameConstants.NUM_JOKER;
                break;
            default:
                throw new RuntimeException();
        }
        id = num * 4;
    }

    public boolean is2() {
        return num == GameConstants.NUM_2;
    }

    public boolean isJoker() {
        return num == GameConstants.NUM_JOKER;
    }

    @Override
    public String toString() {
        switch (num) {
            case GameConstants.NUM_3:
                return "3";
            case GameConstants.NUM_4:
                return "4";
            case GameConstants.NUM_5:
                return "5";
            case GameConstants.NUM_6:
                return "6";
            case GameConstants.NUM_7:
                return "7";
            case GameConstants.NUM_8:
                return "8";
            case GameConstants.NUM_9:
                return "9";
            case GameConstants.NUM_10:
                return "10";
            case GameConstants.NUM_J:
                return "J";
            case GameConstants.NUM_Q:
                return "Q";
            case GameConstants.NUM_K:
                return "K";
            case GameConstants.NUM_A:
                return "A";
            case GameConstants.NUM_2:
                return "2";
            case GameConstants.NUM_JOKER:
                if (id == GameConstants.ID_JOKER_1) return "小王";
                else return "大王";
        }
        return null;
    }

}

