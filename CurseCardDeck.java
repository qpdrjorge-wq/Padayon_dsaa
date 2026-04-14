package card;

import main.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the curse-card decks for each chapter.
 * Add more chapters by following the same pattern.
 */
public class CurseCardDeck {

    public static List<CurseCard> createChapter1Deck(GamePanel gp) {
        List<CurseCard> deck = new ArrayList<>();

        deck.add(new CurseCard(gp,
                "Stumble & Fall",
                "Your rival trips on a loose stone and tumbles backwards down the path.",
                CurseCard.CurseEffect.KNOCKBACK, 3));

        deck.add(new CurseCard(gp,
                "Pick-Pocket",
                "Quick fingers, empty pockets. You swipe some cash when no one's looking.",
                CurseCard.CurseEffect.STEAL_MONEY, 20));

        deck.add(new CurseCard(gp,
                "Gossip Spreads",
                "A nasty rumor about your rival makes the rounds. Their reputation takes a hit.",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 15));

        deck.add(new CurseCard(gp,
                "Bad News",
                "You deliver devastating news to a rival. Their day is ruined.",
                CurseCard.CurseEffect.DECREASE_HAPPINESS, 20));

        deck.add(new CurseCard(gp,
                "Money Showdown",
                "You challenge a rival to a financial face-off. Whoever has more money wins ₱100!",
                CurseCard.CurseEffect.CHALLENGE_MONEY, 100));

        deck.add(new CurseCard(gp,
                "Reputation Duel",
                "Who's more respected in the barangay? The community decides — winner gets +100 rep!",
                CurseCard.CurseEffect.CHALLENGE_REP, 100));

        deck.add(new CurseCard(gp,
                "Happiness Contest",
                "Smile-off! The happier person wins +100 happiness. Fake it till you make it!",
                CurseCard.CurseEffect.CHALLENGE_HAPPY, 100));

        deck.add(new CurseCard(gp,
                "Road Block",
                "You bribe the barangay tanod to put up a detour sign on your rival's path.",
                CurseCard.CurseEffect.BLOCK_TURNS, 2));

        deck.add(new CurseCard(gp,
                "Property Grab",
                "You find a loophole in the land title. One of your rival's properties is now yours.",
                CurseCard.CurseEffect.STEAL_PROPERTY, 0));

        deck.add(new CurseCard(gp,
                "Heavy Step Back",
                "A sudden flood washes your rival far back down the road.",
                CurseCard.CurseEffect.KNOCKBACK, 5));

        return deck;
    }

    public static List<CurseCard> createChapter2Deck(GamePanel gp) {
        List<CurseCard> deck = new ArrayList<>();

        deck.add(new CurseCard(gp,
                "Academic Setback",
                "You tip off a professor about your rival's shortcut. They get sent back.",
                CurseCard.CurseEffect.KNOCKBACK, 3));

        deck.add(new CurseCard(gp,
                "Tuition Heist",
                "You found their ATM pin. A small withdrawal — for academic purposes.",
                CurseCard.CurseEffect.STEAL_MONEY, 80));

        deck.add(new CurseCard(gp,
                "Failed Recitation",
                "Your rival blanked during a recitation. Their academic rep crumbles.",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 20));

        deck.add(new CurseCard(gp,
                "Thesis Panic",
                "You deleted their intro chapter. They spiral into despair.",
                CurseCard.CurseEffect.DECREASE_HAPPINESS, 25));

        deck.add(new CurseCard(gp,
                "Allowance Duel",
                "Who got the bigger allowance this month? Higher money wins ₱100!",
                CurseCard.CurseEffect.CHALLENGE_MONEY, 100));

        deck.add(new CurseCard(gp,
                "Org Popularity Vote",
                "The student council votes on who's more respected. Winner gets +100 rep!",
                CurseCard.CurseEffect.CHALLENGE_REP, 100));

        deck.add(new CurseCard(gp,
                "Campus Vibes Check",
                "A wellness survey ranks who's actually thriving. Winner gets +100 happiness!",
                CurseCard.CurseEffect.CHALLENGE_HAPPY, 100));

        deck.add(new CurseCard(gp,
                "Suspended!",
                "You filed a bogus complaint. Your rival is suspended for 2 turns.",
                CurseCard.CurseEffect.BLOCK_TURNS, 2));

        deck.add(new CurseCard(gp,
                "Business Takeover",
                "You undercut their tindahan prices until they had to sell. Property acquired.",
                CurseCard.CurseEffect.STEAL_PROPERTY, 0));

        deck.add(new CurseCard(gp,
                "Hallway Ambush",
                "You corner them with bad news right before finals. They're sent reeling.",
                CurseCard.CurseEffect.KNOCKBACK, 4));

        return deck;
    }

    public static List<CurseCard> createChapter3Deck(GamePanel gp) {
        List<CurseCard> deck = new ArrayList<>();

        deck.add(new CurseCard(gp,
                "Crab Mentality",
                "Walang aangat, send a player back 5 spaces.",
                CurseCard.CurseEffect.KNOCKBACK, 5));

        deck.add(new CurseCard(gp,
                "Abangers",
                "You robbed them near recto station",
                CurseCard.CurseEffect.STEAL_MONEY, 1500));

        deck.add(new CurseCard(gp,
                "GCash Scam",
                "You \"borrowed\" 500 pesos from a player and conveniently forgot about it",
                CurseCard.CurseEffect.STEAL_MONEY, 20));

        deck.add(new CurseCard(gp,
                "The Wifi breakdown ",
                "Cut-off any player's wifi connection and block them for 2 turns\n",
                CurseCard.CurseEffect.BLOCK_TURNS, 2));

        deck.add(new CurseCard(gp,
                "Car Tow",
                "You reported a player for illegal parking in front of your gate.",
                CurseCard.CurseEffect.BLOCK_TURNS, 1));

        deck.add(new CurseCard(gp,
                "Neighborhood Gossip",
                "You spread a rumor about a player that they were once a part of the akyat bahay gang.",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 35));

        deck.add(new CurseCard(gp,
                "Salary Flex",
                "Whose got a bigger salary bonus this year? Higher money wins ₱300.",
                CurseCard.CurseEffect.CHALLENGE_MONEY, 300));

        deck.add(new CurseCard(gp,
                "Pamana ni Lola",
                "You found a loophole with your opponents family inheritance and stole what was rightfully theirs.",
                CurseCard.CurseEffect.STEAL_PROPERTY, 0));

        deck.add(new CurseCard(gp,
                "Ipapa-tulfo kita",
                "You submit a false complaint that a player stole your partner to Raffy Tulfo in Action.",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 40));

        deck.add(new CurseCard(gp,
                "Kwek-kwek Poisoning",
                "You dipped your opponents's kwek-kwek sa linisan ng sandok. Their stomach suffered the consequences.",
                CurseCard.CurseEffect.KNOCKBACK, 4));

        deck.add(new CurseCard(gp,
                "Jumper",
                "Snitch on any player to Meralco na isa silang jumper",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 7));

        deck.add(new CurseCard(gp,
                "Jeepney Strike",
                "Bring any player back 3 spaces due to the strike",
                CurseCard.CurseEffect.KNOCKBACK, 3));

        deck.add(new CurseCard(gp,
                "Declined and VL",
                "You told the manager that your opponent is just going to Boracay, not attending a family emergency. Their vacation leave gets cancelled.",
                CurseCard.CurseEffect.BLOCK_TURNS, 1));

        deck.add(new CurseCard(gp,
                "KKB",
                "You ordered the most expensive steak and wine at the restaurant, then loudly declared, \"KKB (Kanya-Kanyang Bayad) tayo guys ha!\" Sinong mas may budget?",
                CurseCard.CurseEffect.CHALLENGE_MONEY, 450));

        deck.add(new CurseCard(gp,
                "Reply-All Error",
                "You tricked your opponent into accidentally hitting \"Reply-All\" on " +
                        "a company-wide email chain while gossiping about the management.",
                CurseCard.CurseEffect.DECREASE_REPUTATION, 30));

        deck.add(new CurseCard(gp,
                "Walang Bidet!",
                "They rushed to the office restroom after eating spicy food, " +
                        "and you secretly turned off the water supply to their cubicle's bidet.",
                CurseCard.CurseEffect.DECREASE_HAPPINESS, 25));

        deck.add(new CurseCard(gp,
                "The Fatigue Olympics",
                "They complain they only had 5 hours of sleep. " +
                        "You instantly counter with, \"Wow, buti ka nga 5 hours eh, ako 2 hours lang!\"",
                CurseCard.CurseEffect.CHALLENGE_HAPPY, 30));
        
        return deck;
    }

}
