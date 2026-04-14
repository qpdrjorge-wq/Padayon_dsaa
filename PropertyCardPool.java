package card;

import main.GamePanel;

import java.util.*;

public class PropertyCardPool {

    public static List<PropertyCard> buildChapter1Pool(GamePanel gp) {
        List<PropertyCard> pool = new ArrayList<>();

        pool.add(new PropertyCard(gp,
                "Limited Edition Pokemon Card",
                "A very rare card with only 3 copies worldwide. Future collector's gold.",
                2000, 0));

        pool.add(new PropertyCard(gp,
                "Antique Necklace",
                "An old necklace given to you by your grandmother. Priceless to the family.",
                850, 0));

        pool.add(new PropertyCard(gp,
                "Baby Alive Toy",
                "A toy that acts like a baby. You kept it in perfect condition.",
                20, 0));

        pool.add(new PropertyCard(gp,
                "Mango Tree",
                "The tallest mango tree in the barangay. Tourists have offered to buy it.",
                200, 15));

        pool.add(new PropertyCard(gp,
                "Piggybank Collection",
                "Dozens of ceramic pigs stuffed with coins. One day you'll break them.",
                80, 0));

        pool.add(new PropertyCard(gp,
                "Lemonade Stand",
                "A small family store in front of the house. Always running out of ice but earns.",
                200, 50));

        pool.add(new PropertyCard(gp,
                "Nokia",
                "The unbreakable beast of the 90s",
                1000, 0));

        pool.add(new PropertyCard(gp,
                "Bouncy Horsey",
                "The wild west can't handle this",
                30, 0));

        pool.add(new PropertyCard(gp,
                "Bicycle",
                "Speed like lightnin mcqueen",
                1500, 0));

        pool.add(new PropertyCard(gp,
                "Teddy",
                "Reminds you of mr bean",
                100, 0));

        return pool;
    }

    public static List<PropertyCard> buildChapter2Pool(GamePanel gp) {
        List<PropertyCard> pool = new ArrayList<>();

        pool.add(new PropertyCard(gp,
                "Tindahan sa Eskwelahan",
                "A small canteen stall beside the school gate. Pandesal sells out every morning.",
                400, 30));

        pool.add(new PropertyCard(gp,
                "Boarding House Room",
                "A tiny room you sub-let to a classmate. Shower is shared but the rent is real.",
                350, 25));

        pool.add(new PropertyCard(gp,
                "Secondhand Bike",
                "Bought for 500, re-sold for 800 after spray-painting it. Business is business.",
                100, 0));

        pool.add(new PropertyCard(gp,
                "USB Printing Stall",
                "A laptop and printer in a small kiosk. Thesis season equals gold rush.",
                450, 35));

        pool.add(new PropertyCard(gp,
                "Bahay Kubo",
                "A nipa hut passed down from lolo. Small but yours.",
                300, 5));

        return pool;
    }

    public static List<PropertyCard> buildChapter3Pool(GamePanel gp) {
        List<PropertyCard> pool = new ArrayList<>();

        pool.add(new PropertyCard(gp,
                "Studio Apartment",
                "A 20sqm unit near the office. Loud neighbors but walking distance to work.",
                1200, 60));

        pool.add(new PropertyCard(gp,
                "Vicks VapoRub",
                "Used best whenever you have a cold",
                130, 0));

        pool.add(new PropertyCard(gp,
                "Diatabs",
                "A single concrete lot near the mall. Passive income while you sleep.",
                80, 0));

        pool.add(new PropertyCard(gp,
                "Bioflu",
                "Ingat! For when the stress of adulting and capitalism gives you a literal headache.",
                130, 0));

        pool.add(new PropertyCard(gp,
                "Rolls-Royce ni Sarah",
                "Such an expensive car. I wonder whose wallet did it come from? Spoiler alert, mula ito sa kaban ng bayan",
                5000, 0));

        pool.add(new PropertyCard(gp,
                "Tumbler",
                "Your trusty steel bottle in this hot weather.",
                5000, 0));

        pool.add(new PropertyCard(gp,
                "Nespresso Machine",
                "Corporate worker's fuel. You save money on Starbucks, but the pods are making you broke.",
                550, 0));

        pool.add(new PropertyCard(gp,
                "Katinko Roll-on",
                "Directly applied to the forehead during stressful Zoom calls with the toxic boss.",
                80, 0));

        pool.add(new PropertyCard(gp,
                "St. Peter Life Plan",
                "The ultimate adulting flex. Ready ka na for the afterlife, at least bayad na.",
                900, 0));

        pool.add(new PropertyCard(gp,
                "1-Year Gym Membership",
                "Binayaran mo in advance para ma-motivate ka pero tatlong beses ka lang pumunta. Sayang pera.",
                1500, 0));

        pool.add(new PropertyCard(gp,
                "Beep Card (Fully Loaded)",
                "Loaded with ₱1,000. Ang yaman mo tingnan pag-tap sa LRT, pero ubos din agad kase RTO ka araw-araw.",
                1000, 0));





        return pool;
    }

    public static List<PropertyCard> buildChapter4Pool(GamePanel gp) {
        List<PropertyCard> pool = new ArrayList<>();

        pool.add(new PropertyCard(gp,
                "Apartment Building",
                "A 3-floor walk-up in Quezon City. 6 units, 5 occupied. Roof leaks in July.",
                3000, 150));

        pool.add(new PropertyCard(gp,
                "Commercial Lot",
                "A corner lot along the national road. Zoned commercial. Primed for development.",
                4000, 0));

        pool.add(new PropertyCard(gp,
                "Fishing Boat",
                "A 12-footer with a secondhand motor. Goes out every dawn, returns with bangus.",
                700, 55));

        pool.add(new PropertyCard(gp,
                "Funeral Parlor",
                "Morbid but profitable. Demand is literally guaranteed.",
                2500, 120));

        pool.add(new PropertyCard(gp,
                "Hot Spring Resort",
                "Discovered while hiking Mt. Banahaw. Rough facilities but packed every weekend.",
                3500, 200));

        return pool;
    }

    private static Set<String> allOwnedNames(GamePanel gp) {
        Set<String> owned = new HashSet<>();
        if (gp.players == null) return owned;
        for (entity.Player p : gp.players) {
            if (p == null) continue;
            for (PropertyCard pc : p.getProperties()) {
                owned.add(pc.propertyName);
            }
        }
        return owned;
    }

    //picks three random property cards from each chapter and ensures that no taken prop
    public static List<PropertyCard> pickRandom(GamePanel gp, int count) {
        List<PropertyCard> pool;
        switch (gp.currentChapter) {
            case 1  -> pool = buildChapter1Pool(gp);
            case 2  -> pool = buildChapter2Pool(gp);
            case 3  -> pool = buildChapter3Pool(gp);
            case 4  -> pool = buildChapter4Pool(gp);
            default -> pool = buildChapter1Pool(gp);
        }

        Set<String> owned = allOwnedNames(gp);

        List<PropertyCard> available = new ArrayList<>();
        for (PropertyCard pc : pool) {
            if (!owned.contains(pc.propertyName)) available.add(pc);
        }

        if (available.isEmpty()) available = pool;

        Collections.shuffle(available);
        return new ArrayList<>(available.subList(0, Math.min(count, available.size())));
    }
}
