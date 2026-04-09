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
                new CriticalDecision(gp, "You're finally going to college! Which undergraduate degree are you going to take?", new String[]{"CS", "BA"})
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
            case 3:
                if (choiceIndex == 0){
                    player.getPathLoader().c2AddBranch_Up();
                } else {
                    player.getPathLoader().c2AddBranch_Down();
                }
            case 4:
                if (choiceIndex == 0){
                    player.getPathLoader().c3AddBranch_Left1();
                }else {
                    player.getPathLoader().c3AddBranch_Right1();
                }
        }

        player.waitingForBranchChoice = false;
        player.boardMoving = true;
    }
}
