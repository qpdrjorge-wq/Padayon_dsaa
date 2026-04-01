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
        addDialoguePoint(16, 36, 0);
        addPoint(18, 36);
        addPoint(21, 36);
        addPoint(21, 34);
        addPoint(21, 31);
        addPoint(23, 31);
        addScenarioPoint(26, 31, 0);//choice based
    }

    public void addBranch1_Up() {
        addPoint(26, 29);
        addPoint(26, 26);
        addPoint(23, 26);
        addPoint(23, 23);
        addPoint(25, 23);
        addPoint(28, 23);
        addPoint(28, 20);
        addPoint(30, 20);
        addPoint(32, 20);
        addPoint(35, 20);
        sharedPath1();
    }

    public void addBranch1_Down() {
        addPoint(26, 34);
        addPoint(28, 34);
        addPoint(30, 34);
        addPoint(33, 34);
        addPoint(33,31);
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
        addPoint(44, 28);
        addPoint(46, 28);
        addPoint(49, 28);
        addPoint(49, 31);
        addScenarioPoint(52, 31, 1); //choice based
    }

    public void addBranch2_Right() {
        addPoint(54, 31);
        addPoint(57, 31);
        addPoint(57, 34);
        addPoint(60, 34);
        addPoint(60, 37);
        addPoint(57, 37);
        addPoint(57, 40);
        sharedPath2();

    }

    public void addBranch2_Down() {
        addPoint(52, 34);
        addPoint(50, 34);
        addPoint(47, 34);
        addPoint(47, 37);
        addPoint(49, 37);
        addPoint(51, 37);
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
        addPoint(63,40);
        addPoint(65,40);
        addPoint(68,40);
        addPoint(68,38);
        addPoint(68,36);
        addPoint(68,34);
        addPoint(68,31);
        addPoint(66,31);
        addPoint(63,31);
        addPoint(63,29);
        addPoint(63,29);
        addPoint(63,24);
        addScenarioPoint(60, 24, 2);
    }

    public void addBranch3_up(){
        addPoint(60, 21);
        addPoint(62, 21);
        addPoint(64, 21);
        addPoint(67, 21);
        addPoint(67, 18);
        addPoint(65, 18);
        addPoint(62, 18);
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
        addPoint(48, 18);
        addPoint(50, 18);
        addPoint(53, 18);
        addPoint(53, 15);
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
        addScenarioPoint(14, 13, 0); //addScenario
    }

    public void c2AddBranch_Up() {
        addPoint(14,10);
        addPoint(17,10);
        addPoint(19,10);
        addPoint(21,10);
        addPoint(23,10);
        addPoint(25,10);
        c2SharedPath1();
    }

    public void c2AddBranch_Down() {
        addPoint(14,17);
        addPoint(17,17);
        addPoint(19,17);
        addPoint(21,17);
        addPoint(23,17);
        addPoint(25,17);
        c2SharedPath1();
    }

    private void c2SharedPath1() {
        addPoint(25,13);
        addPoint(28,13);
        addPoint(30,13);
        addPoint(32,13);
        addPoint(32,17);
        addPoint(35,17);
        addPoint(37,17);
        addScenarioPoint(39,17,1); //addScenario
    }

    public void c2AddBranch_Up2() {
        addPoint(39,13);
        addPoint(42,13);
        addPoint(44,13);
        addPoint(46,13);
        c2SharedPath2();
    }

    public void c2AddBranch_Down2() {
        addPoint(39,20);
        addPoint(42,20);
        addPoint(44,20);
        addPoint(46,20);
        c2SharedPath2();
    }

    private void c2SharedPath2() {
        addPoint(46,17);
        addPoint(49,17);
        addPoint(51,17);
        addPoint(53,17);
        addPoint(55,17);
        addPoint(57,17);
        addPoint(59,17);
        addPoint(59,13);
        addPoint(62,13);
        addPoint(64,13);
        addPoint(64,10);
        addPoint(67,10);
        addPoint(69,10);
        addPoint(69,13);
        addPoint(72,13);
        addPoint(72,17);
        addPoint(72,19);
        addPoint(72,21);
        addPoint(72,23);
        addPoint(72,25);
        addPoint(72,27);
        addPoint(72,29);
        addPoint(69,29);
        addPoint(69,32);
        addPoint(66,32);
        addPoint(64,32);
        addPoint(64,29);
        addPoint(61,29);
        addPoint(59,29);
        addPoint(59,26);
        addPoint(56,26);
        addPoint(54,26);
        addPoint(52,26);
        addPoint(50,26);
        addPoint(48,26);
        addPoint(46,26);
        addPoint(44,26);
        addScenarioPoint(42,26, 2); // addScenario
    }

    public void c2AddBranch_Up3() {
        addPoint(42,23);
        addPoint(39,23);
        addPoint(37,23);
        addPoint(35,23);
        addPoint(35,20);
        addPoint(32,20);
        addPoint(30,20);
        addPoint(28,20);
        addPoint(28,23);
        addPoint(25,23);
        addPoint(23,23);
        addPoint(21,23);
        c2SharedPath3();
    }

    public void c2AddBranch_Down3() {
        addPoint(42,29);
        addPoint(39,29);
        addPoint(37,29);
        addPoint(35,29);
        addPoint(35,32);
        addPoint(32,32);
        addPoint(30,32);
        addPoint(28,32);
        addPoint(28,29);
        addPoint(25,29);
        addPoint(23,29);
        addPoint(21,29);
        c2SharedPath3();
    }

    private void c2SharedPath3() {
        addPoint(21,26);
        addPoint(18,26);
        addPoint(16,26);
        addPoint(14,26);
        addPoint(12,26);
        addPoint(12,29);
        addPoint(12,31);
        addPoint(12,33);
        addPoint(12,35);
        addScenarioPoint(12,37,1); //addScenarioPoint
    }

    public void c2AddBranch_Right() {
        addPoint(15,37);
        addPoint(17,37);
        addPoint(17,40);
        addPoint(17,42);
        addPoint(17,44);
        addPoint(14,44);
        c2SharedPath4();
    }

    public void c2AddBranch_Left() {
        addPoint(9,37);
        addPoint(7,37);
        addPoint(7,40);
        addPoint(7,42);
        addPoint(7,44);
        addPoint(10,44);
        c2SharedPath4();
    }

    private void c2SharedPath4() {
        addPoint(12,44);
        addPoint(12,47);
        addPoint(12,49);
        addPoint(15,49);
        addPoint(17,49);
        addPoint(19,49);
        addPoint(21,49);
        addPoint(21,52);
        addEndPoint(21,54);
    }



}
