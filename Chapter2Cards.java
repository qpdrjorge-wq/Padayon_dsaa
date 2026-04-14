package card;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class Chapter2Cards {

    public static List<EventCard> createChapter2Deck(GamePanel gp){

        List<EventCard> deck = new ArrayList<>();

        deck.add(
                new EventCard(gp,"You were found smoking in the school bathroom", null,
                        null,new int[]{-10}, new int[]{-5}, new int[]{0}));

        deck.add(
                new EventCard(gp, "Your class starts at 8am, it's 7:40am, what should you do?",
                        new String[]{"Get up", "accept fate"}, new String[] {"You realized it's Sunday, there is no school", "Luckily you stayed home, it's Sunday!"}, new int[]{-10, 15}, new int[]{0, 0}, new int[]{-35, 0})
        );

        deck.add(
                new EventCard(gp, "You are hungry and you wanted lunch, what lunch would you buy?",
                        new String[]{"Hatdog and rice", "Beef and Vegetables"}, new String[]{"It was good, but it's not enough", "You where full!"}, new int[]{10, 5}, new int[]{0, 0}, new int[]{-20, -10})
        );

        deck.add(
                new EventCard(gp, "Maam Janice gave you a perfect score on your game project", null,
                        null, new int[] {30}, new int[]{30}, new int[]{0})
        );

        deck.add(
                new EventCard(gp, "You and your friends are planning a getaway for the summer. Where will you go?",
                        new String[]{"Binondo", "Star City"}, new String[]{"Hanggang Plano lang talaga, di na tuloy balak nyo",
                        "Too broke to continue"}, new int[]{10, 10}, new int[]{0, 0}, new int[]{0, 0})
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

        deck.add(
                new EventCard(gp, "Your terror professor asks a ridiculously hard question. " +
                        "The whole class goes silent, and he starts looking at your row.",
                        new String[]{"Stare at them", "Tingin sa Sahig"},
                        new String[]{"He calls on you! You guessed the answer correctly by " +
                                "some miracle. Your classmates think you're a hero.",
                                "You stare intensely at your notebook pretending to read. " +
                                        "He calls your seatmate instead. You are safe"},
                        new int[]{-5, 30}, new int[]{30, 0}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "It is strictly P.E. day, but you completely forgot your jogging pants on your bed. " +
                        "Your instructor won't let you do the activity if you're not wearing a PE uniform.",
                        new String[]{"Borrow", "Go home"},
                        new String[]{"You borrow an unwashed uniform from a friend. It smells, but you got in.",
                                "You went back home to get your uniform but you missed the first subject and paid double fare"},
                        new int[]{-10, -5}, new int[]{10, 0}, new int[]{0, -50})
        );



        deck.add(
                new EventCard(gp,"Dropped Out:\n You failed a major prerequisite subject and have to retake it during the summer.",
                        null,
                        null,new int[]{-25}, new int[]{-15}, new int[]{-1000}));

        deck.add(
                new EventCard(gp, "Class dismissed. Your friends invites you to go to a comshop, but you have a major quiz tomorrow.",
                        new String[]{"G! Laro", "Study"},
                        new String[]{"It wasn't just one game. You stayed until 8 PM and failed the quiz",
                                "You went home to review. You aced the quiz, but got FOMO"},
                        new int[]{30, -25}, new int[]{-20, 30}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "Your school was having a fieldtrip and gave students a choice of either",
                        new String[]{"Fieldtip", "Project"},
                        new String[]{"The fieldtrip was mediocre but the cost of ticket was so expensive despite the boring places you went to",
                                "You successfully finished the project despite the time constraints"},
                        new int[]{5, -5}, new int[]{0, 15}, new int[]{-870, 0})
        );

        deck.add(
                new EventCard(gp, "Your group needs to print and hardbound your final thesis at a shop near Recto, but nobody brought enough cash.",
                        new String[]{"Mag-Abono", "Compute sa GC"},
                        new String[]{"You paid for everything upfront. The paper is submitted on time, " +
                                "but getting paid back by your groupmates will take weeks",
                                "You refuse to front the cash and force everyone to calculate shares on the spot. The shop closes, and you submitted it late."},
                        new int[]{0, -10}, new int[]{40, -30}, new int[]{-750, 0})
        );

        deck.add(
                new EventCard(gp,"Di ka si Y/N:\n You zoned out imagining a fictional " +
                        "romance with the campus crush, then you bumped into a post.", null,
                        null,new int[]{-20}, new int[]{-10}, new int[]{0}));

        deck.add(
                new EventCard(gp,"\"Get 1/4 Sheet of Paper\" \nThe terror prof announces a Surprise Quiz!",
                        null,
                        null,new int[]{-20}, new int[]{0}, new int[]{0}));


        deck.add(
                new EventCard(gp,"Na-Post sa Freedom Wall!:\n Someone anonymously posted something good about you on the University Freedom Wall!",
                        null,
                        null,new int[]{20}, new int[]{40}, new int[]{0}));


        deck.add(
                new EventCard(gp, "You have a friend that is considered as a \"dumbbell\" or AKA pabigat sa group. Will you?",
                        new String[]{"Ignore them", "Accept them"},
                        new String[]{"They felt invisible but you had less weight to carry",
                                "You still suffer from the consequences of having a \"pabigat\" friend"},
                        new int[]{15, -15}, new int[]{10, -5}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp,"Sportsfest Week:\n No classes! Just cheering, eating street food, and watching sports.",
                        null,
                        null,new int[]{25}, new int[]{0}, new int[]{0}));

        deck.add(
                new EventCard(gp, "Ambag sa Groupwork:\n The deadline is tomorrow. Your groupmates have contributed zero research.",
                        new String[]{"DIY", "Remove them"},
                        new String[]{"You carry the entire team to a passing grade, but your soul is drained.",
                                "You delete their names from the title page. You feel vindicated, but they hate you now."},
                        new int[]{-10, 30}, new int[]{35, -10}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp,"Academic Burnout:\n You are tired. You question why you even chose this degree..",
                        null,
                        null,new int[]{-15}, new int[]{0}, new int[]{0}));


        return deck;
    }
}
