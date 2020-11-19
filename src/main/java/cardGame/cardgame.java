package cardGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Piles {

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

class initialHands {
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

class Player extends Piles implements Runnable {
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
//                    Thread.sleep(100);
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

public class cardgame {
    static int players = 4;
    static int row = 4;
    static int col = 8;
    static int count = 0;
    static int prior = 0;
    static int[][] Pile = new int[row][col];

    public static void main(String[] args) {

        CardDeck();

        Piles playerCards = new Piles();
        Player[] player = new Player[4];
        for (int i = 0; i < players; i++) {
            player[i] = new Player(playerCards);
            player[i].playerNumber(i + 1);
            player[i].preferdenominations(prior, prior + 1);
            prior += 2;
        }
        ArrayDeque<Integer>[] card_in_deck = new initialHands().initialcards(Pile, player);

        Player.piledeck(card_in_deck);
        for(ArrayDeque<Integer> i : card_in_deck)
            System.out.println(i);
        playerThreads(player);
    }


    private static void playerThreads(Player[] player) {
        Thread[] playerThread = new Thread[players];
        for (int i = 0; i < row; i++) {
            playerThread[i] = new Thread(player[i]);
            playerThread[i].start();

        }
    }

    private static void CardDeck() {
        ArrayList<String> arrayList = new ArrayList<>();

//        try {
//            File myObj = new File("src/main/java/cardGame/input");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine();
//                arrayList.add(data);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//
//        String[] piles;
//        ArrayList<Integer> cards = new ArrayList<>();
//        piles = arrayList.get(0).split(" ");
//        for (String i : piles) {
//            cards.add(Integer.valueOf(i));
//        }
//        piles = arrayList.get(1).split(" ");
//        for (String i : piles) {
//            cards.add(Integer.valueOf(i));
//        }
//        piles = arrayList.get(2).split(" ");
//        for (String i : piles) {
//            cards.add(Integer.valueOf(i));
//        }
//        piles = arrayList.get(3).split(" ");
//        for (String i : piles) {
//            cards.add(Integer.valueOf(i));
//        }
//        System.out.println("deck " + cards);
//
//
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//                Pile[i][j] = cards.get(count);
//                count++;
//            }
//        }
        System.out.println("Enter the input (4*8 matrix)");
        Scanner sc =  new Scanner(System.in);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Pile[i][j] = sc.nextInt();
            }
        }
    }

}
