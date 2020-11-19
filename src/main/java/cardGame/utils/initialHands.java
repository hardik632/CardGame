package cardGame.utils;

import cardGame.Players.Player;

import java.util.ArrayDeque;

public class initialHands {
    int row =4;
    int col =8;
    int spin;

    public ArrayDeque<Integer>[] initialcards(int[][] Pile , Player[] player)
    {
      // for cards in hands
        for (int i = 0; i < row/2; i++) {
            spin = 0;
            for (int j = 0; j < col; j++) {
                if (spin == 4)
                    spin = 0;
                player[spin].SetCards(Pile[i][j]);
                spin += 1;
            }
        }
        // for cards in pile
        ArrayDeque<Integer>[] piledeck = new ArrayDeque[4];
        for (int i = 0; i < 4; i++) {
            piledeck[i] = new ArrayDeque<>();
        }

        for (int i = 2; i < 4; i++) {
            spin = 0;
            for (int j = 0; j < col; j++) {
                if (spin == 4)
                    spin = 0;
                piledeck[spin].add(Pile[i][j]);
                spin += 1;
            }
        }

        return piledeck;
    }

}
