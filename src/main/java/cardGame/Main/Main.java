package cardGame.Main;

import cardGame.Players.Player;
import cardGame.utils.Piles;
import cardGame.utils.initialHands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
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

        try {
            File myObj = new File("src/main/java/cardGame/input");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                arrayList.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

//        Queue<Integer> pile1 = new LinkedList<>();
//        Queue<Integer> pile2 = new LinkedList<>();
//        Queue<Integer> pile3 = new LinkedList<>();
//        Queue<Integer> pile4 = new LinkedList<>();

        String[] piles;
        ArrayList<Integer> cards = new ArrayList<>();
        piles = arrayList.get(0).split(" ");
        for (String i : piles) {
            cards.add(Integer.valueOf(i));
//            pile1.add(Integer.valueOf(i));
        }
        piles = arrayList.get(1).split(" ");
        for (String i : piles) {
            cards.add(Integer.valueOf(i));
//            pile2.add(Integer.valueOf(i));
        }
        piles = arrayList.get(2).split(" ");
        for (String i : piles) {
            cards.add(Integer.valueOf(i));
//            pile3.add(Integer.valueOf(i));
        }
        piles = arrayList.get(3).split(" ");
        for (String i : piles) {
            cards.add(Integer.valueOf(i));
//            pile4.add(Integer.valueOf(i));
        }
        System.out.println("deck " + cards);


        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Pile[i][j] = cards.get(count);
                count++;
            }
        }
//        int pile_no = 1;
//        for (int i = 0; i < row; i++) {
//            System.out.print("Pile " + pile_no + " : ");
//            for (int j = 0; j < col; j++) {
//                System.out.print(Pile[i][j] + " ");
//            }
//            System.out.println();
//            pile_no++;
//        }
    }

}
