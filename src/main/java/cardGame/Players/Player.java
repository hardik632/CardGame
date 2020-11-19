package cardGame.Players;

import cardGame.utils.Piles;


import java.util.*;

public class Player extends Piles implements Runnable {
    final Piles playerCards;
    static int count_players = 0;
    static boolean flag = true;
    int index = 0;
    int player_no;
    int[] cards = new int[4];
    HashMap<Integer, Integer> preferredCardsMap = new HashMap<>();

    public Player(Piles playerCards) {
        count_players++;
        this.playerCards = playerCards;
    }
    public void playerNumber(int i) {
        player_no = i;
    }

    public void SetCards(int i) {
        cards[index]=i;
        if (preferredCardsMap.containsKey(i))
            preferredCardsMap.put(i, preferredCardsMap.get(cards[index]) + 1);
        index+=1;
    }
    @Override
    public void run() {
        try {
            synchronized (playerCards) {
                while (flag) {
                    while (playerCards.spinr_current != this.player_no) {
                            playerCards.wait();
                    }
                    if (flag) {
                        turn();
                        System.out.println("-------------------");
                        if (check()) {

                            System.out.println("Player " + player_no + " wins ");
                            flag = false;


                        }
                    }
                    Thread.sleep(100);
                    if(player_no ==4)
                        playerCards.spinr_current = 1;
                    else
                        playerCards.spinr_current = player_no+1;

                    playerCards.notifyAll();
                }
                exitPlayers();
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean check() {
        for (Map.Entry i : preferredCardsMap.entrySet())
            if ((int) i.getValue() == 5)
                return true;
        return false;
    }

    private void exitPlayers() {
        System.out.println("Player"+ player_no + " exits ");
    }

    public void cards_in_hand() {
        System.out.print("Player"+player_no + " hand ");
        for (int i = 0; i < 4; i++)
            System.out.print(" " + cards[i] + " ");
        System.out.println();
//        for(ArrayDeque<Integer> i : deck)
//{System.out.println(i);}
//        System.out.println(preferredCardsMap);
    }

    public  void turn() {
        int draw = draw_card_from_pile(player_no);
        System.out.println("Player" + player_no + " draws " + draw);
        int discard = draw;
        if (preferredCardsMap.containsKey(draw)) {
            preferredCardsMap.put(draw, preferredCardsMap.get(draw) + 1);
            discard = replace(draw);
        }
        discard_card_from_pile(discard, player_no);
        System.out.println("Player" + player_no + " discards " + discard);
        cards_in_hand();
    }
    public int replace(int cardDrawn)
    {

        int discard = 0;
//        for(int i : cards)
//        System.out.println(i);
        boolean flag = true;
        int loc = 0;
        for (int i = 0; i < this.cards.length; i++) {

            if (!preferredCardsMap.containsKey(cards[i])) {
                flag = false;
                loc = i;
                break;
            }
        }
        if (!flag) {
            discard = cards[loc];
            cards[loc] = cardDrawn;
//            preferredCardsMap.put(cardDrawn, preferredCardsMap.get(cardDrawn) + 1);
        }
        else
        {
            int checkflag =0;
            int a = remove_preferred_card(cardDrawn);

            int loc_index = 0;
            for(int i=0;i<cards.length;i++)
            {
                if(cards[i]==a)
                    loc_index =i;
                checkflag=1;
            }
            if(checkflag ==1)
            discard = cards[loc_index];
            cards[loc_index] = cardDrawn;
        }
        return discard;
    }
    public int remove_preferred_card(int cardDrawn) {
        if (preferredCardsMap.get(cardDrawn) > 2) // we have to remove the other prefered card
        {


            int key = 0;
            for (Map.Entry i : preferredCardsMap.entrySet()) {
                int mapkey = (int) i.getKey();

                if (mapkey != cardDrawn) {

                    key = mapkey;
                }
            }
            preferredCardsMap.put(key, preferredCardsMap.get(key) - 1);
            return key;
        } else {
            return 0;
        }
    }

    public void preferdenominations(int x, int y) {
        preferredCardsMap.put(x,0);
        preferredCardsMap.put(y,0);
    }

}