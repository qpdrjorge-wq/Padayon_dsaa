package card;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class Chapter2Cards {

    public static List<EventCard> createChapter2Deck(GamePanel gp){

        List<EventCard> deck = new ArrayList<>();

        deck.add(
                new EventCard(gp,"You were found smoking in the school bathroom", null,  null,new int[]{-10}, new int[]{-5}, new int[]{0}));

        deck.add(
                new EventCard(gp, "Your class starts at 8am, it's 7:40am, what should you do?", new String[]{"Get up", "accept fate"}, new String[] {"You realized it's Sunday, there is no school", "Luckily you stayed home, it's Sunday!"}, new int[]{-10, 15}, new int[]{0, 0}, new int[]{-35, 0})
        );

        deck.add(
                new EventCard(gp, "You are hungry and you wanted lunch, what lunch would you buy?", new String[]{"Hatdog and rice", "Beef and Vegetables"}, new String[]{"It was good, but it's not enough", "You where full!"}, new int[]{10, 5}, new int[]{0, 0}, new int[]{-20, -10})
        );

        deck.add(
                new EventCard(gp, "Maam Janice gave you a perfect score on your game project", null, null, new int[] {30}, new int[]{30}, new int[]{0})
        );

        deck.add(
                new EventCard(gp, "You and your friends are planning a getaway for the summer. Where will you go?", new String[]{"Binondo", "Star City"}, new String[]{"Hanggang Plano lang talaga, di na tuloy balak nyo", "Too broke to continue"}, new int[]{10, 10}, new int[]{0, 0}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "It’s the night before your final system defense. Your groupmate hasn't written " +
                        "a single line of code, but they did buy three boxes of pizza and two liters of Coke for the group.",
                        new String[]{"Let them stay", "Remove them from the group"},
                        new String[]{"They fed you, so they pass. You are tired but well-fed",
                                "\"Thesis 'to, hindi potluck\""},
                        new int[]{-35, 20}, new int[]{20, -10}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "You’re at a reunion and your Marites Auntie asks, \"O, bakit wala ka pa ring boyfriend/girlfriend?\"\n",
                        new String[]{"The Academic Shield", "The Reverse Card"},
                        new String[]{" \"Focus po muna sa career/boards, Tita.\"",
                                "\"Kayo po ba Tita, pang-ilang asawa niyo na po ba yan\""},
                        new int[]{-10, 30}, new int[]{0, 0}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "You’re studying for the Licensure Exam. Your barkada invites you to a 3-day beach trip in La Union.\n",
                        new String[]{"The \"St. Jude\" Route", "The \"YOLO\" Route"},
                        new String[]{"You stayed home, study, and light a candle at the church",
                                "You went to the beach and study on the bus"},
                        new int[]{0, 30}, new int[]{0, 0}, new int[]{0, 0})
        );



        return deck;
    }
}
