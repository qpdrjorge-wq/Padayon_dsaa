package tile;

import card.Buff;
import main.GamePanel;

import java.util.ArrayList;

public class PathLoader {

    GamePanel gp;
    // The player is looking for THIS variable name:
    public ArrayList<BoardNode> pathPoints = new ArrayList<>();

    public PathLoader(GamePanel gp) {
        this.gp = gp;
        loadStartPath();
    }

    public void loadStartPath() {
        pathPoints.clear();

        addPoint(16, 39); // Start
        addPoint(16, 36);
        addDialoguePoint(18, 36, 1);
        addPoint(21, 36);
        addCareerPoint(21, 34);
        addPropertyPoint(21, 31);
        addPoint(23, 31);
        addScenarioPoint(26, 31, 0);//choice based
    }

    public void addBranch1_Up() {
        addPoint(26, 29);
        addPaycheckPoint(26, 26);
        addPoint(23, 26);
        addPropertyPoint(23, 23);
        addPoint(25, 23);
        addBabyPoint(28, 23);
        addPoint(28, 20);
        addDialoguePoint(30, 20, 0);
        addBuffPoint(32, 20, new Buff(10, 15, 10, 3, "Your father is happy and proud of you for bringing his food. \nYou received father's respect buff.\n +10 happiness, +15 happiness, +15 money for 3 turns"));
        addPoint(35, 20);
        sharedPath1();
    }

    public void addBranch1_Down() {
        addPoint(26, 34);
        addDialoguePoint(28, 34, 3);
        addBuffPoint(30, 34, new Buff(-10, -5, 0, 5, "Your mother is fuming in anger.\n You received mother's wrath debuff.\n -10 happiness, -5 reputation for 5 turns"));
        addPoint(33, 34);
        addCursePoint(33,31);
        addPoint(35, 31);
        addPoint(38, 31);
        addPoint(38, 29);
        addPoint(38, 26);
        addPoint(35, 26);
        sharedPath1();
    }

    private void sharedPath1() {
        addPoint(35, 23);
        addPoint(37, 23);
        addDialoguePoint(39, 23, 2);
        addPoint(42, 23);
        addPoint(42, 25);
        addPoint(42, 28);
        addCursePoint(44, 28);
        addPoint(46, 28);
        addPoint(49, 28);
        addPoint(49, 31);
        addScenarioPoint(52, 31, 1); //choice based
    }

    public void addBranch2_Right() {
        addPoint(54, 31);
        addPoint(57, 31);
        addPoint(57, 34);
        addBuffPoint(60, 34, new Buff(10, 5, 10, 3, "You are fully energized for the day. Since you followed your mother, she gives you money. \n +10 happiness, +5 reputation, +10 money for 3 turns"));
        addPoint(60, 37);
        addCursePoint(57, 37);
        addPoint(57, 40);
        sharedPath2();

    }

    public void addBranch2_Down() {
        addPoint(52, 34);
        addPoint(50, 34);
        addBuffPoint(47, 34, new Buff(20, 10, 0, 3, "You and your friends played at the river, it was a fun day.\n +20 happiness, +10 reputation for 3 turns"));
        addPoint(47, 37);
        addPoint(49, 37);
        addCursePoint(51, 37);
        addPoint(54, 37);
        sharedPath2();
    }

    private void sharedPath2() {
        addPoint(54, 40);
        addPoint(54, 43);
        addPoint(56, 43);
        addPoint(58, 43);
        addPoint(60, 43);
        addPoint(63, 43);
        addCursePoint(63,40);
        addPoint(65,40);
        addPoint(68,40);
        addPoint(68,38);
        addPoint(68,36);
        addPoint(68,34);
        addPoint(68,31);
        addPoint(66,31);
        addPoint(63,31);
        addPoint(63,29);
        addPoint(63,27);
        addPoint(63,24);
        addScenarioPoint(60, 24, 2);
    }

    public void addBranch3_up(){
        addPoint(60, 21);
        addPropertyPoint(62, 21);
        addPoint(64, 21);
        addBuffPoint(67, 21, new Buff(-10, -5, -20, 3, "Your parents also attended the bazaar and caught you a midst the event.\n They are angry with you skipping class.\n -10 happiness, -5 reputation, -20 money for 3 turns"));
        addPoint(67, 18);
        addPoint(65, 18);
        addCursePoint(62, 18);
        addPoint(62, 15);
        addPoint(59, 15);
        addPoint(59, 12);
        sharedPath3();
    }

    public void addBranch3_left(){
        addPoint(58, 24);
        addPoint(55, 24);
        addPoint(55, 21);
        addPoint(53, 21);
        addPoint(51, 21);
        addPoint(48, 21);
        addCursePoint(48, 18);
        addPoint(50, 18);
        addPoint(53, 18);
        addBuffPoint(53, 15, new Buff(15, 10, 15, 3, "A midst the gameplay, your grandfather caught you skipped class\n He was cool though and both of you enjoyed the event\n +15 happiness, +10 reputation, +15 money for 3 turns"));
        addPoint(56, 15);
        sharedPath3();
    }

    private void sharedPath3(){
        addPoint(56, 12);
        addPoint(56, 9);
        addPoint(58, 9);
        addPoint(61, 9);
        addPoint(61, 6);
        addPoint(63, 6);
        addEndPoint(66, 6);
    }

    public void addPoint(int col, int row) {
        pathPoints.add(new BoardNode(col, row));
    }

    public void addEndPoint(int col, int row) {
        BoardNode node = new BoardNode(col, row);
        node.isEndNode = true;
        pathPoints.add(node);
    }

    public void addDialoguePoint(int col, int row, int npcIndex){pathPoints.add(new BoardNode(col, row, npcIndex));}
    public void addBuffPoint(int col, int row, Buff buff){pathPoints.add(new BoardNode(col, row, buff));}
    public void addScenarioPoint(int col, int row, int scenarioIndex){pathPoints.add(new BoardNode(col, row, scenarioIndex, true));}

    public void addPropertyPoint(int col, int row) {
        pathPoints.add(BoardNode.propertyNode(col, row));
    }
    public void addCursePoint(int col, int row){
        pathPoints.add(BoardNode.curseNode(col, row));
    }

    public void addCareerPoint(int col, int row) {pathPoints.add(BoardNode.careerNode(col, row));}
    public void addPaycheckPoint(int col, int row) {pathPoints.add(BoardNode.paycheckNode(col, row));}
    public void addBabyPoint(int col, int row){pathPoints.add(BoardNode.babyTileNode(col, row));}
    public void addBabyExpensePoint(int col, int row){pathPoints.add(BoardNode.babyExpenseNode(col, row));}

    public BoardNode getCurrentNode(int index) {
        if (index >= 0 && index < pathPoints.size()) {
            return pathPoints.get(index);
        }
        return null;
    }

    public void loadChapter2Path() {
        pathPoints.clear();

        addPoint(6, 13);
        addPoint(8, 13);
        addPoint(10, 13);
        addPoint(12, 13);
        addScenarioPoint(14, 13, 3); //addScenario
    }

    public void c2AddBranch_Up() {
        addPoint(14,10);
        addBuffPoint(17,10, new Buff(10, 15, 0, 4, "Maam Janice is proud of your decision, keep it up \n Pinasa ka ni maam Janice sa Project\n +10 happiness, +15 reputation for 4 turns"));
        addPoint(19,10);
        addPoint(21,10);
        addPoint(23,10);
        addPoint(25,10);
        c2SharedPath1();
    }

    public void c2AddBranch_Down() {
        addPoint(14,16);
        addDialoguePoint(17,16,1); //witch
        addPoint(19,16);
        addPoint(21,16);
        addPoint(23,16);
        addPoint(25,16);
        c2SharedPath1();
    }

    private void c2SharedPath1() {
        addCareerPoint(25,13);
        addPoint(28,13);
        addPoint(30,13);
        addPoint(32,13);
        addPoint(32,16);
        addPoint(35,16);
        addPoint(37,16);
        addScenarioPoint(39,16,1); //addScenario
    }

    public void c2AddBranch_Up2() {
        addPoint(39,13);
        addPoint(42,13);
        addPoint(44,13);
        addPoint(46,13);
        c2SharedPath2();
    }

    public void c2AddBranch_Down2() {
        addPoint(39,19);
        addPoint(42,19);
        addPoint(44,19);
        addPoint(46,19);
        c2SharedPath2();
    }

    private void c2SharedPath2() {
        addPoint(46,16);
        addPoint(49,16);
        addPoint(51,16);
        addPoint(53,16);
        addPoint(55,16);
        addPoint(57,16);
        addPoint(59,16);
        addPoint(59,13);
        addPoint(62,13);
        addPoint(64,13);
        addPoint(64,10);
        addPoint(67,10);
        addPoint(69,10);
        addPoint(69,13);
        addPoint(72,13);
        addPoint(72,16);
        addPoint(72,18);
        addPoint(72,20);
        addPoint(72,22);
        addPoint(72,24);
        addPoint(72,26);
        addPoint(72,28);
        addPoint(69,28);
        addPoint(69,31);
        addPoint(66,31);
        addPoint(64,31);
        addPoint(64,28);
        addPoint(61,28);
        addPoint(59,28);
        addPoint(59,25);
        addPoint(56,25);
        addPoint(54,25);
        addPoint(52,25);
        addPoint(50,25);
        addPoint(48,25);
        addPoint(46,25);
        addPoint(44,25);
        addScenarioPoint(42,25, 2); // addScenario
    }

    public void c2AddBranch_Up3() {
        addPoint(42,22);
        addPoint(39,22);
        addPoint(37,22);
        addPoint(35,22);
        addPoint(35,19);
        addPoint(32,19);
        addPoint(30,19);
        addPoint(28,19);
        addPoint(28,22);
        addPoint(25,22);
        addPoint(23,22);
        addPoint(21,22);
        c2SharedPath3();
    }

    public void c2AddBranch_Down3() {
        addPoint(42,28);
        addPoint(39,28);
        addPoint(37,28);
        addPoint(35,28);
        addPoint(35,31);
        addPoint(32,31);
        addPoint(30,31);
        addPoint(28,31);
        addPoint(28,28);
        addPoint(25,28);
        addPoint(23,28);
        addPoint(21,28);
        c2SharedPath3();
    }

    private void c2SharedPath3() {
        addPoint(21,25);
        addPoint(18,25);
        addPoint(16,25);
        addPoint(14,25);
        addPoint(12,25);
        addPoint(12,28);
        addPoint(12,30);
        addPoint(12,32);
        addPoint(12,34);
        addScenarioPoint(12,36,1); //addScenarioPoint
    }

    public void c2AddBranch_Right() {
        addPoint(15,36);
        addPoint(17,36);
        addPoint(17,39);
        addPoint(17,41);
        addPoint(17,43);
        addPoint(14,43);
        c2SharedPath4();
    }

    public void c2AddBranch_Left() {
        addPoint(9,38);
        addPoint(7,38);
        addPoint(7,39);
        addPoint(7,41);
        addPoint(7,43);
        addPoint(10,43);
        c2SharedPath4();
    }

    private void c2SharedPath4() {
        addPoint(12,43);
        addPoint(12,46);
        addPoint(12,48);
        addPoint(15,48);
        addPoint(17,48);
        addPoint(19,48);
        addPoint(21,48);
        addPoint(21,51);
        addEndPoint(21,53);
    }

    //Chapter 3
    public void loadChapter3Path() {
        pathPoints.clear();
        addPoint(13, 40); // Start
        addCareerPoint(13, 43);
        addPoint(16, 43);
        addCursePoint(18, 43);
        addPoint(20, 43);
        addPropertyPoint(22, 43);
        addPoint(22, 40);
        addDialoguePoint(22, 38, 0);
        addCursePoint(25, 38);
        addPoint(27, 38);
        addCursePoint(29, 38);
        addScenarioPoint(29, 35, 7); //scenario point
    }

    public void c3AddBranch_Left1() {
        addPoint(26, 35);
        addPropertyPoint(24, 35);
        addDialoguePoint(24, 32,1);//jumpscare
        addCursePoint(21, 32);
        addCursePoint(21, 32);
        addPoint(21, 29);
        addPoint(18, 26);
        addPropertyPoint(15, 26);
        addCursePoint(15, 23);
        addPoint(18, 23);
        c3SharedPath1();
    }

    public void c3AddBranch_Right1() {
        addPoint(32, 35);
        addPoint(34, 35);
        addPoint(34, 32);
        addPropertyPoint(31, 32);
        addCursePoint(31, 29);
        addPoint(28, 29);
        addPoint(28, 26);
        addPropertyPoint(25, 26);
        addPoint(23, 26);
        addCursePoint(23, 23);
        c3SharedPath1();
    }

    private void c3SharedPath1() {
        addPropertyPoint(20, 23);
        addPoint(20, 20);
        addPoint(23, 20);
        addCursePoint(25, 20);
        addPoint(27, 20);
        addPropertyPoint(29, 20);
        addPoint(29, 17);
        addPropertyPoint(29, 15);
        addPoint(32, 15);
        addCursePoint(34, 15);
        addPoint(34, 12);
        addPoint(37, 12);
        addPropertyPoint(39, 12);
        addPoint(39, 15);
        addPoint(42, 15);
        addCursePoint(44, 15);
        addPoint(46, 15);
        addPoint(48, 15);
        addPropertyPoint(50, 15);
        addScenarioPoint(50, 18, 8);//addScenario
    }

    public void c3AddBranch_Right2() {
        addPoint(53, 18);
        addPoint(53, 21);
        addDialoguePoint(56, 21,2);//jo
        addCursePoint(58, 21);
        addPoint(58, 24);
        addPropertyPoint(55, 24);
        addPoint(53, 24);
        addPropertyPoint(53, 27);
        addPoint(51, 27);
        c3SharedPath2();
    }

    public void c3AddBranch_Left2() {
        addPoint(47, 18);
        addCursePoint(45, 18);
        addPoint(45, 21);
        addPoint(42, 21);
        addCursePoint(40, 21);
        addPoint(40, 24);
        addPropertyPoint(43, 24);
        addPoint(45, 24);
        addPoint(45, 27);
        c3SharedPath2();
    }

    private void c3SharedPath2() {
        addBabyPoint(48, 27); // wedding tile
        addPoint(48, 30);
        addDialoguePoint(51, 30, 3);//girlie
        addPoint(53, 30);
        addPoint(55, 30);//pink
        addPoint(55, 33);
        addPoint(55, 35);
        addScenarioPoint(58, 35, 9);//ADD SCENARIO
    }


    public void c3AddBranch_Up1() {
        addBuffPoint(58, 32,new Buff(15, 10, 60, 5,"The government services in your hometown improved, \ncongrats, you voted for the right person. \n+15 happiness, +10 reputation for 5 turns"));//humanitarian lawyer
        addPoint(58, 30);
        addCursePoint(61, 30);
        addPoint(61, 27);
        addPropertyPoint(64, 27);
        addPoint(64, 30);
        addPoint(67, 30);
        addCursePoint(69, 30);
        addPoint(71, 30);
        c3SharedPath3();
    }

    public void c3AddBranch_Down1() {
        addBuffPoint(61, 35, new Buff(-20, -15, -90, 5,"Taxes were raised, budgets were allocated on \nunrelated things, what did you expect? \nyou voted a businesswoman and not a public servant afterall. \n-20 happiness, -15 reputation, -90 money for 5 turns"));//humanitarian lawyer);
        addPoint(61, 38);
        addBabyExpensePoint(64, 38);//pink
        addPoint(66, 38);
        addPoint(66, 35);
        addPropertyPoint(66, 33);
        addPoint(69, 33);
        c3SharedPath3();
    }

    private void c3SharedPath3() {
        addBabyExpensePoint(71,33); //pink
        addPoint(71, 36);
        addPoint(71, 38);
        addCursePoint(71, 40);
        addPoint(71, 42);
        addBabyExpensePoint(68, 42);//pink
        addPoint(66, 42);
        addCursePoint(66, 45);
        addPoint(63, 45);
        addPropertyPoint(61, 45);
        addPoint(59, 45);
        addScenarioPoint(57, 45, 10); //add scenario
    }

    public void c3AddBranch_Up2() {
        addPoint(57, 42);
        addPoint(54, 42);
        addCursePoint(54, 39);
        addBabyExpensePoint(51, 39);//pink
        addPoint(49, 39);
        addBabyExpensePoint(49, 42);//pink
        addPoint(46, 42);
        c3SharedPath4();
    }


    public void c3AddBranch_Down2() {
        addPoint(57, 48);
        addCursePoint(54, 48);
        addPropertyPoint(54, 51);
        addPoint(51, 51);
        addBabyExpensePoint(49, 51);//pink
        addPoint(49, 48);
        addPoint(46, 48);
        c3SharedPath4();
    }

    private void c3SharedPath4() {
        addPropertyPoint(46,45);
        addBabyExpensePoint(43,45);//pink
        addPoint(41, 45);
        addEndPoint(41, 48);//endpoint
    }

    //chapter 4 stuffs

    public void loadChapter4Path() {
        pathPoints.clear();

        addPoint(85, 7); // Start
        addPoint(83, 7);
        addPoint(81, 7);
        addPoint(78, 7);
        addPoint(78, 9);
        addPoint(78, 12);
        addPoint(76,12);
        addPoint(74,12);
        addPoint(72, 12);
        addScenarioPoint(69, 12, 3);
    }

    public void c4AddBranch_Up1() {
        addPoint(68, 10);
        addPoint(68, 7);
        addPoint(65,7);
        addPoint(65, 4);
        addPoint(63 , 4);
        addPoint(61, 4);
        addPoint(58, 4);
        addPoint(58, 7);
        addPoint(55, 7);
        addPoint(55, 9);
    }

    public void c4AddBranch_Down1() {
        addPoint(68, 14);
        addPoint(68, 17);
        addPoint(65, 17);
        addPoint(65, 20);
        addPoint(63, 20);
        addPoint(61, 20);
        addPoint(58, 20);
        addPoint(58, 17);
        addPoint(55, 17);
        addPoint(55, 15);
    }

    public void c4SharedBranch1() {
        addPoint(55, 12);
        addScenarioPoint(52, 12, 4);
    }

    public void c4AddBranchUp2(){
        addPoint(52, 10);
        addPoint(52, 7);
        addPoint(49,7);
        addPoint(49, 4);
        addPoint(47,4);
        addPoint(45,4);
        addPoint(42,4);
        addPoint(42, 7);
        addPoint(39,7);
        addPoint(39, 9);
    }

    public void c4AddBranchDown2(){
        addPoint(52, 14);
        addPoint(52, 17);
        addPoint(49,17);
        addPoint(49, 20);
        addPoint(47,20);
        addPoint(45,20);
        addPoint(42,20);
        addPoint(42, 17);
        addPoint( 39,17);
        addPoint(39, 15);
    }

    public void c4SharedBranch2(){
        addPoint(39, 12);
        addPoint(37, 12);
        addPoint(35, 12);
        addPoint(33, 12);
        addPoint(30, 12);
        addPoint(30, 14);
        addPoint(30, 17);
        addPoint(28, 17);
        addScenarioPoint(25, 17, 5);
    }

    public void c4AddBranchUp3(){
        addPoint(25, 15);
        addPoint(25, 13);
        addPoint(25, 10);
        addPoint(23, 10);
        addPoint(21, 10);
        addPoint(19, 10);
        addPoint(16, 10);
        addPoint(16, 12);
        addPoint(16, 14);
    }

    public void c4AddBranchDown3(){
        addPoint(25, 19);
        addPoint(25, 21);
        addPoint(25, 24);
        addPoint(23, 24);
        addPoint(21, 24);
        addPoint(19, 24);
        addPoint(16, 24);
        addPoint(16, 22);
        addPoint(16, 20);
    }

    public void c4SharedBranch3(){
        addPoint(16, 17);
        addPoint(14, 17);
        addPoint(12, 17);
        addPoint(9, 17);
        addPoint(9, 19);
        addPoint(9, 21);
        addPoint(9, 23);
        addPoint(9, 25);
        addPoint(9, 27);
        addPoint(9, 29);
        addPoint(9, 32);
        addPoint(11, 32);
        addPoint(13, 32);
        addPoint(15, 32);
        addPoint(17, 32);
        addPoint(19, 32);
        addPoint(22, 32);
        addPoint(22, 34);
        addPoint(22, 37);
        addPoint(24, 37);
        addPoint(26, 37);
        addPoint(28, 37);
        addScenarioPoint(31, 37, 6);
    }

    public void c4AddBranchUp4(){
        addPoint(31, 35);
        addPoint(31, 33);
        addPoint(31, 30);
        addPoint(33, 30);
        addPoint(36, 30);
        addPoint(36, 27);
        addPoint(38, 27);
        addPoint(40, 27);
        addPoint(43, 27);
        addPoint(43, 29);
        addPoint(43, 31);
    }

    public void c4AddBranchDown4(){
        addPoint(31, 40);
        addPoint(34, 40);
        addPoint(34, 43);
        addPoint(36, 43);
        addPoint(38, 43);
        addPoint(40, 43);
        addPoint(43, 43);
        addPoint(43, 41);
        addPoint(43, 39);
        addPoint(43, 37);
    }

    public void c4SharedBranch4(){
        addPoint(43, 34);
        addPoint(45, 34);
        addPoint(47, 34);
        addPoint(49, 34);
        addPoint(51, 34);
        addPoint(53, 34);
        addPoint(55, 34);
        addPoint(58, 34);
        addPoint(58,32);
        addPoint(58,29);
        addPoint(58,29);
        addPoint(58,29);
    }
}
