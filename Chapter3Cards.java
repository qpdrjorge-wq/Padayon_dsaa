package card;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class  Chapter3Cards {

    public static List<EventCard> createChapter3Deck(GamePanel gp){

        List<EventCard> deck = new ArrayList<>(); //happiness, reputation, money

        deck.add(
                new EventCard(gp, "You are the breadwinner of your family, pay all bills and expenses\n" +
                        "\n\n Ano ba yan ilang taon ka pang nag-aral ng math, subtraction lang pala yung magagamit mo...",
                        null, new String[] {},
                        new int[]{0}, new int[]{0}, new int[]{-2500}));

        deck.add(
                new EventCard(gp, "You brought your lunch to work, but you left your mother's favorite tupperware in the office fridge. It is now missing \n Do you:",
                        new String[]{"Confess", "Buy a new one"},
                        new String[]{"She gave you a full 1-hr sermon about responsibility and even made you buy her a new one ",
                                "You bought the wrong shade of pink but it's fine since she hasn't noticed it.... not yet"},
                        new int[]{-30, 0}, new int[]{0, 0}, new int[]{-650, -650})
        );

        deck.add(
                new EventCard(gp, "You've been a minimum wage worker for three year already in your company " +
                        "and haven't been given a raise during ever since. Ano ang gagawin mo?",
                        new String[]{"Revenge", "File a complaint"},
                        new String[]{"Hinakot mo ang buong pantry and stuffed your bag with 2 months worth of 3-in-1 coffee, tissue, and toothpicks. Dasurb ",
                                "Your complaint was \"duly noted\"... meaning it was noted, but definitely not heard."},
                        new int[]{50, -60}, new int[]{0, -10}, new int[]{500, 0})
        );

        deck.add(
                new EventCard(gp, "You politely corrected a co-worker's formatting mistake, and they hit you with: \"Edi sorry, ikaw na ang magaling.\"\n",
                        new String[]{"Patulan pabalik", "Ignore"},
                        new String[]{"\"Huy ano ka ba, don't say sorry, be better.\" You established your boundaries and handled it well. Kudos!",
                                "You chose silence to keep the peace, but now they think you have no backbone. The disrespect continues. "},
                        new int[]{10, -15}, new int[]{20, -20}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "An officemate nudges you and told you \"Mukhang model yung boss natin 'no?\"\n",
                        new String[]{"\"Crush koyon..\"", "Dedma"},
                        new String[]{"Your comment immediately spreads to the whole department. Teh, anong crush-crush, trentahin ka na oh!",
                                "Ded is ma sila sayo. You completely avoided the office gossip trap and kept your professional image spotless."},
                        new int[]{0, 0}, new int[]{-20, 20}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "At the family reunion, your nosy Tita corners you: \"Huy inaanak, puro ka work! Wala ka bang balak mag-asawa?\"\n",
                        new String[]{"Patulan pabalik", "Dedmahin"},
                        new String[]{"\"Tita mas matimbang pa ba ang monthsary kesa sa mothly salary... in this economy??\"\n Kinabog mo ate!",
                                "You realize that she's not worth the time and energy."},
                        new int[]{10, 0}, new int[]{20, 0}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "Adulting Reality: Tax Deduction\n You looked at your payslip and realized mas malaki pa yung kaltas ng tax, SSS, at PhilHealth kesa sa naiuwi mong sahod. Welcome to the real world! ",
                        null, null, new int[]{-40}, new int[]{0}, new int[]{-2500}));



        deck.add(
                new EventCard(gp, "An old friend invited you for a \"drink\", but it turned out to be a networking seminar. " +
                        "\nYou were peer-pressured into buying their \"authentic\" and \"imported\" herbal creams na nakakagaling ng almoranas. ",
                        null, null, new int[]{-50}, new int[]{0}, new int[]{-450}));


        deck.add(
                new EventCard(gp,"You won 1000 pesos from the lotto!!",
                        null,  null,new int[]{60}, new int[]{0}, new int[]{1000}));

        deck.add(
                new EventCard(gp,"13th month pay!!",
                        null,  null,new int[]{0}, new int[]{0}, new int[]{2500}));

        deck.add(
                new EventCard(gp,"Christmas Bonus",
                        null,  new String[]{"Ibigay mona, ang aming Christmas bonus~"},new int[]{0}, new int[]{0}, new int[]{1700}));
        deck.add(
                new EventCard(gp, "It's that time of the year again, you owe your niece's and nephew's their pamasko!",
                        null, null, new int[]{-5}, new int[]{0}, new int[]{-500}));

        deck.add(
                new EventCard(gp, "You wanted to buy new shoes for yourself\n Where do you want to buy it?",
                        new String[]{"Online Shop", "Mall"},
                        new String[]{"You saved transportation cost but the product you bought online was a counterfeit",
                                "You bought it at a higher price but was satisfied "}, new int[]{-15, 30}, new int[]{0, 0}, new int[]{-1500, -1700})
        );

        deck.add(
                new EventCard(gp, "You attended a funeral near your home\n Would you:", new String[]{"Go Home", "Pass by 7-eleven"},
                        new String[]{"The spirit followed you home and caused some misfortunes in your life",
                                "Nagpagpag ka, congrats, no spirit followed you home that night"},
                        new int[]{-10, 15}, new int[]{-15, 20}, new int[]{0, 10})
        );

        deck.add(
                new EventCard(gp, "Someone from another department whom you are not close with suddenly drops " +
                        "a highly personal, insulting \"joke\" about you in front of everyone.",
                        new String[]{"Below the belt", "Confront"},
                        new String[]{"\"Ganyan talaga siguro kapag lumaking kulang sa aruga...\" \nYou chose violence, and it felt amazing",
                                "\"Wag ako ante, 7-eleven tayo, never tayong close..\""},
                        new int[]{15, 15}, new int[]{0, 0}, new int[]{0, 0})
        );

        deck.add(
                new EventCard(gp, "You are on the graveyard shift. While washing your hands you kept hearing muffled screams from an empty bathroom stall",
                        new String[]{"Ignore", "Run"},
                        new String[]{"You locked eyes with something you shouldn't have seen. You get a mysterious fever the next day and must pay a faith healer",
                                "You escaped whatever it is waiting for you in that room"},
                        new int[]{-20, 20}, new int[]{0, 0}, new int[]{-280, 0})
        );

        deck.add(
                new EventCard(gp, "Your college friend, Cassie, is getting married in Boracay and they invited you to be " +
                        "part of the entourage, meaning you pay for your own flight and hotel.",
                        new String[]{"Gora", "Pass Muna"},
                        new String[]{"You maxed out your credit card, but had a really good time",
                                "You passed the invitation. You saved money, but nagtampo si Cassie sa'yo"},
                        new int[]{100, -20}, new int[]{0, -30}, new int[]{-2000, 0})
        );

        deck.add(
                new EventCard(gp, "Your coworker just had a baby and randomly listed you as a Godparent without asking you first.",
                        new String[]{"Accept", "I-seen-zone"},
                        new String[]{"You buy a high-end stroller and officially enter your Tito/Tita era",
                                "\"'Di ko pa nga nakikita baby niyan eh..\""},
                        new int[]{10, 0}, new int[]{40, -20}, new int[]{-600, 0})
        );

        deck.add(
                new EventCard(gp, "Pay your taxes first!",
                        null, null, new int[]{-5}, new int[]{0}, new int[]{-1200}));

        deck.add(
                new EventCard(gp, "Happy na birthday mo pa \n nagpa-blowout ka",
                        null, null, new int[]{0}, new int[]{20}, new int[]{-750}));


        return deck;
    }
}
