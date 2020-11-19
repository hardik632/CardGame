package cardGame.utils;

import java.util.ArrayDeque;

public class Piles {

    public int spinr_current = 1;
    private static final ArrayDeque<Integer>[] deck = new ArrayDeque[4];

    public static void piledeck(ArrayDeque<Integer>[] piledeck) {
        for (int i = 0; i < 4; i++) {
            deck[i] = new ArrayDeque<>(piledeck[i]);
        }
    }

    public int draw_card_from_pile(int playerNum) {
        return deck[playerNum - 1].pollFirst();
    }

    public void discard_card_from_pile(int card, int playerNum) {
        int pilenumber;
        if (playerNum == 4)
            pilenumber = 0;
        else
            pilenumber = playerNum;
        deck[pilenumber].offerLast(card);
    }
}
