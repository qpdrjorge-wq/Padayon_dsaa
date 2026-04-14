package tile;

import card.CriticalDecision;
import entity.Player;
import main.GamePanel;

public class DecisionManager {
    public CriticalDecision[] chapter1Decisions;
    public CriticalDecision[] chapter2Decisions;
    GamePanel gp;

    public DecisionManager(GamePanel gp){
        this.gp = gp;

        chapter1Decisions = new CriticalDecision[]{
                new CriticalDecision(gp, "Your mother ask you to deliver your father's forgotten lunch\n but your friend invited you to check the base they have created down the forest park.\n What should you do?",
                        new String[]{"Bring the lunch to father", "Play with your friends"}),
                new CriticalDecision(gp, "Your mother has instructed you to sleep for the afternoon before playing outside\nWhat will you do?", new String[]{"Sleep", "Escape from mother's eyes"}),
                new CriticalDecision(gp, "You don't feel like going to school today\nSo, you decided to skip school for today\n Where will you go?", new String[]{"Bazaar", "Manood ng Liga"}),
                new CriticalDecision(gp, "You're finally going to college! Which undergraduate degree are you going to take?", new String[]{"CS", "BA"}),
                new CriticalDecision(gp, "You don’t know much people in college. How would you handle final projects?", new String[]{"Have Courage", "Do it Solo"}),
                new CriticalDecision(gp, "Finals week is approaching! You’ve been procrastinating for a while and you’re nervous for the exams", new String[]{"Study", "Cram it"}),
                new CriticalDecision(gp, "Oh no! Your professor asked you to present the current lesson in class this week.", new String[]{"Absent", "Discuss"}),
                new CriticalDecision(gp, "Choose", new String[]{"Up", "Down"}),
                new CriticalDecision(gp, "A random person comes knocking on you door asking you " +
                        "to join their \"peace organization\". ", new String[]{"Join them ", "Ignore them"}),
                new CriticalDecision(gp, "A breadwinner's dillema of choosing from either:", new String[]{"Career", "Love life"}),
                new CriticalDecision(gp, "Your rapidly urbanizing hometown is holding a mayoral election. The outcome will permanently reshape the city’s economy and your own daily life. Do you vote for: ",
                        new String[]{"Sarah Villarluna, a humanitarian lawyer", "Sheila Garcia, a local businesswoman and capitalist"}),
                new CriticalDecision(gp, "Your immigration agency calls—your working visa for Canada was finally approved. Do you:",
                        new String[]{"Leave the Philippines", "Stay in the Philippines"}),

        };
    }


    public void handleScenarioChoice(int scenarioIndex, int choiceIndex, Player player){

        switch(scenarioIndex){
            case 0:
                if(choiceIndex == 0){
                    player.getPathLoader().addBranch1_Up();
                } else {
                    player.getPathLoader().addBranch1_Down();
                }
                break;
            case 1:
                if(choiceIndex == 0){
                    player.getPathLoader().addBranch2_Right();
                } else {
                    player.getPathLoader().addBranch2_Down();
                }
                break;
            case 2:
                if(choiceIndex == 0){
                    player.getPathLoader().addBranch3_up();
                } else {
                    player.getPathLoader().addBranch3_left();
                }
                break;
            case 3:
                if (choiceIndex == 0){
                    player.getPathLoader().c2AddBranch_Up();
                } else {
                    player.getPathLoader().c2AddBranch_Down();
                }
                break;
            case 4:
                if (choiceIndex == 0){
                    player.getPathLoader().c2AddBranch_Up2();
                } else {
                    player.getPathLoader().c2AddBranch_Down2();
                }
                break;
            case 5:
                if (choiceIndex == 0){
                    player.getPathLoader().c2AddBranch_Up3();
                } else {
                    player.getPathLoader().c2AddBranch_Down3();
                }
                break;
            case 6:
                if (choiceIndex == 0){
                    player.getPathLoader().c2AddBranch_Right();
                } else {
                    player.getPathLoader().c2AddBranch_Left();
                }
                break;
            case 7:
                if (choiceIndex == 0){
                    player.getPathLoader().c4AddBranch_Up1();
                } else {
                    player.getPathLoader().c4AddBranch_Down1();
                }
                break;
            case 8:
                if (choiceIndex == 0){
                    player.getPathLoader().c3AddBranch_Left1();
                } else {
                    player.getPathLoader().c3AddBranch_Right1();
                }
                break;
            case 9:
                if (choiceIndex == 0){
                    player.getPathLoader().c3AddBranch_Left2();
                } else {
                    player.getPathLoader().c3AddBranch_Right2();
                }
                break;
            case 10:
                if (choiceIndex == 0){
                    player.getPathLoader().c3AddBranch_Up1();
                } else {
                    player.getPathLoader().c3AddBranch_Down1();
                }
                break;
            case 11:
                if (choiceIndex == 0){
                    player.getPathLoader().c3AddBranch_Up2();
                } else {
                    player.getPathLoader().c3AddBranch_Down2();
                }
                break;
        }

        player.waitingForBranchChoice = false;
        player.boardMoving = true;
    }
}
