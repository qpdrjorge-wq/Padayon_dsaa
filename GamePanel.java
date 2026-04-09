package main;

import card.*;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import object.Wheel;
import tile.BoardNode;
import tile.DecisionManager;
import tile.PathLoader;
import tile.TileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    public final int originalTileSize = 32;
    final int scale = 2;

    //screen without much scaling
    public int tileSize = originalTileSize * scale;
    public int maxScreenCol = 20;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; //1024
    public int screenHeight = tileSize * maxScreenRow; // 768

    //Temporary screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

    //Index Sizes
    int objSize = 10;
    public int numberOfPlayers;
    public boolean[] hasSelected;
    public int currentSelectingPlayer = 0;

    //world settings
    public int maxWorldCol = 90;
    public int maxWorldRow = 47;

    //SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public MouseHandler mouseH = new MouseHandler(this);
    DecisionManager decisionManager = new DecisionManager(this);
    public PropertyTab propertyTab;
    public PropertySelectionOverlay propertyOverlay;
    public Sound sound = new Sound();

    //ENTITY AND OBJECT
    public Player[] players;
    public Player currentPlayer;
    public SuperObject[] obj = new SuperObject[objSize];
    public Entity[] npc = new Entity[10];
    public Wheel wheel;
    public int maxPlayers = 4;
    public int currentPlayerTurn = 0;
    public Entity activeNPC;
    public int currentDialogueTimer = 0;
    public int[] selectedCharacterOrder;
    public int selectionIndex = 0;
    public String[] playerNames;
    public int namingForPlayer = -1;
    public boolean showNameInput = false;

    //GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterSelectState = 4;
    public final int playerCountState = 5;
    public final int buffState = 6;
    public final int endWaitState = 7;
    public final int chapterTransitionState = 8;
    public final int settingsState = 9;
    public final int gameModeSelectState = 10;
    public final int chapterSelectState = 11;
    public final int leaderboardState = 12;

    //camera stuffs
    public int cameraWorldX;
    public int cameraWorldY;
    public final int CAMERA_PLAYER = 0;
    public final int CAMERA_WHEEL = 1;
    public final int CAMERA_VIEW = 2;
    public int cameraMode = CAMERA_PLAYER;
    int cameraTimer = 0;
    int cameraDelay = 120;
    public boolean waitingAfterSpin = false;
    public double currentCamX, currentCamY;
    public float lerpAmount = 0.1f;

    //chapter tracking
    public int currentChapter = 1;
    public int playersAtEnd = 0;
    public float transitionAlpha = 0f;  // for fade effect
    public int transitionTimer = 0;
    public static final int TRANSITION_DURATION = 180;
    public boolean endWaitHandled = false;

    //cards
    public EventCard currentCard;
    List<EventCard> chapter1Deck;
    public boolean cardTriggeredThisTurn = false;
    public boolean pendingCardAfterBuff = false;
    public int globalTurnCount = 0;
    public static final int INCOME_INTERVAL = 5;

    //settings
    public int previousState;

    //game mode and chapter select
    public boolean isFullGame = true;
    public int selectedSingleChapter = 1;
    public static final int TOTAL_CHAPTERS = 4;

    //curse card
    public CurseCard currentCurseCard;
    List<card.CurseCard> chapter1CurseDeck;
    List<card.CurseCard> chapter2CurseDeck;

    public static final int[] CHAPTER_START_MONEY = {
            0, 0, 2000, 5000, 8000
    };

    //leaderboard
    public LeaderboardEntry[] leaderboard;

    public static class LeaderboardEntry {

        public String name;
        public int happiness;
        public int reputation;
        public int money;
        public int score;
        public int rank;

        public LeaderboardEntry(String name, int happiness, int reputation, int money) {
            this.name = name;
            this.happiness = happiness;
            this.reputation = reputation;
            this.money = money;

            this.score = happiness + reputation + money;
        }
    }

    public void buildLeaderboard() {
        leaderboard = new LeaderboardEntry[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            Player p = players[i];
            leaderboard[i] = new LeaderboardEntry(
                    p.playerName, p.happiness, p.reputation, p.money);
        }
        insertionSortLeaderboard(leaderboard);
        for (int i = 0; i < leaderboard.length; i++) {
            leaderboard[i].rank = i + 1;
        }
    }

    private void insertionSortLeaderboard(LeaderboardEntry[] arr) {
        for (int i = 1; i < arr.length; i++) {
            LeaderboardEntry key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j].score < key.score) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    //constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouseH);
        this.setFocusable(true); //the gamePanel can be focused to receive key input
        this.requestFocusInWindow();
    }

    public void startGame() {
        createPlayers();

        if (!isFullGame) {
            startingSingleChapter(selectedSingleChapter);
            return;
        }

        currentChapter = 1;
        playersAtEnd = 0;
        currentPlayerTurn = 0;
        wheel.hasSpun = false;
        cardTriggeredThisTurn = false;
        gameState = playState;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setupGame() {

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/elements/random/colorWheel2.png"));
            wheel = new Wheel(this, 38 * tileSize, 14 * tileSize, tileSize * 6, img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        playersAtEnd = 0;
        currentChapter = 1;

        aSetter.setObject();
        sound.playMusic(Sound.MUSIC_TITLE);
        aSetter.setNPC();
        gameState = titleState;
        chapter1Deck = Chapter1Cards.createChapter1Deck(this);
        chapter1CurseDeck = CurseCardDeck.createChapter1Deck(this);
        currentCamX = 8 * tileSize;
        currentCamY = 33 * tileSize;
        propertyTab = new PropertyTab(this);
        propertyOverlay = new PropertySelectionOverlay(this, null);

        setFullScreen();
    }

    //game loop
    @Override
    public void run() {

        double drawInterval = 1000000000 / 60.0; //the budget of 16, 666, 666 nanoseconds to complete one cycle in 60fps
        double delta = 0; // accumulator, tracks how much a frame has passed. when its reaches 1(100% of frame interval) it's time to update and draw
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                update(); //update info such as position
                repaint();
                delta--;
            }
        }

    }

    public void setFullScreen() {

//        get local screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        gd.setFullScreenWindow(Main.window);

//        get full screen width and height
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void update() { //handles all game logic and state changes

        if (gameState == playState) {

            updateCamera();

            currentPlayer = players[currentPlayerTurn];
            if (!waitingAfterSpin) {
                currentPlayer.update();
            }
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            wheel.update();

            if (wheel.justStopped) {
                waitingAfterSpin = true;
                cameraTimer = 0;
                wheel.justStopped = false;
            }

            if (waitingAfterSpin) {
                cameraTimer++;

                cameraMode = CAMERA_PLAYER;

                if (cameraTimer > cameraDelay) {
                    int result = wheel.getResultSegment() + 1;

                    currentPlayer.startBoardMove(result);

                    waitingAfterSpin = false;
                    cameraTimer = 0;
                }
            }

            if (propertyOverlay != null) propertyOverlay.update();
            if (propertyTab != null) propertyTab.update();

            if (currentPlayer.knockbackJustEnded) {
                currentPlayer.knockbackJustEnded = false;
                cardTriggeredThisTurn = false;
                return;
            }

            if (currentPlayer.hasFinishedMovement() &&
                    !currentPlayer.waitingForBranchChoice && !wheel.spinning && wheel.hasSpun && !cardTriggeredThisTurn && !waitingAfterSpin && !currentPlayer.knockingBack) {

                checkEndCondition();

                if (propertyOverlay != null && propertyOverlay.isActive()) {
                    return;
                }

                if (!currentPlayer.hasReachedEnd) {
                    triggerRandomCard();
                    cardTriggeredThisTurn = true;
                }
                return;
            }

            if (currentCard != null) {
                currentCard.update();
            }

            if (currentCurseCard != null) {
                currentCurseCard.update();
            }

            if (currentCurseCard != null && currentCurseCard.state == Card.CardState.HIDDEN) {
                currentCurseCard = null;
                ui.checkStatChanges();
                nextTurn();
            }

            if (currentCard != null && currentCard.state == Card.CardState.SHOWING) {
                if (keyH.enterPressed) {

                    if (currentCard.hasOptions() && mouseH.chosenOption == -1) {
                        keyH.enterPressed = false;
                        return;
                    }

                    currentCard.handleOptionSelection(mouseH.chosenOption, currentPlayer);

                    currentCard.state = Card.CardState.CLOSING;
                    keyH.enterPressed = false;
                }
            }

            if (currentCard != null && currentCard.state == Card.CardState.HIDDEN) {

                if (propertyOverlay != null && propertyOverlay.isActive()) {
                    return;
                }

                currentCard = null;
                mouseH.chosenOption = -1;
                ui.checkStatChanges();
                nextTurn();
            }

            if (keyH.spacePressed && !wheel.spinning && !currentPlayer.boardMoving) {
                cameraMode = CAMERA_WHEEL;
                wheel.spin();
                keyH.spacePressed = false;
            }
        }

        if (gameState == dialogueState) {

            if (activeNPC != null && activeNPC.choiceTimerActive) {
                activeNPC.tickChoiceTimer();
            }

            if (activeNPC != null && activeNPC.choiceTimeoutFired) {
                System.out.println("TIMEOUT FIRED - calling speak()");
                currentDialogueTimer = 0;
                activeNPC.speak();
                return;
            }

            currentDialogueTimer++;

            if (currentDialogueTimer > 110) {
                activeNPC.speak();
                currentDialogueTimer = 0;
            }
        }

        if (gameState == buffState) {

            if (keyH.enterPressed) {
                ui.buffActive = false;
                keyH.enterPressed = false;
                gameState = playState;

                if (currentPlayer.stepsRemaining > 0) {
                    currentPlayer.boardMoving = true;
                    return;
                }

                if (pendingCardAfterBuff) {
                    triggerRandomCard();
                    cardTriggeredThisTurn = true;
                    pendingCardAfterBuff = false;
                }

            }
        }

        if (gameState == pauseState) {
            //nothing
        }

        if (gameState == endWaitState) {
            if (!endWaitHandled) {
                endWaitHandled = true;
            } else {
                // Wait one extra frame so the banner renders at least once then handit off to the next player
                gameState = playState;
                nextTurn();
            }
        }

        if (gameState == chapterTransitionState) {
            transitionTimer++;
            transitionAlpha = Math.min(1f, transitionTimer / (float) (TRANSITION_DURATION / 2));

            if (transitionTimer == TRANSITION_DURATION / 2) {
                int nextChapter = currentChapter + 1;

                //single chapter mode
                if (!isFullGame) {
                    buildLeaderboard();
                    transitionAlpha = 0f;
                    transitionTimer = 0;
                    gameState = leaderboardState;
                    return;
                }

                //full game
                if (nextChapter > TOTAL_CHAPTERS) {
                    buildLeaderboard();
                    transitionAlpha = 0f;
                    transitionTimer = 0;
                    gameState = leaderboardState;
                    return;
                }

                loadChapter(nextChapter);
            }

            if (transitionTimer >= TRANSITION_DURATION) {
                transitionAlpha = 0f;
                transitionTimer = 0;
                gameState = playState;
                currentPlayerTurn = 0;
                wheel.hasSpun = false;
                cardTriggeredThisTurn = false;
                playersAtEnd = 0;
            }
        }

    }

    public void updateCamera() {

        int targetX = 0;
        int targetY = 0;

        if (cameraMode == CAMERA_PLAYER) {
            Player p = players[currentPlayerTurn];

            targetX = p.worldX + p.solidArea.width / 2;
            targetY = p.worldY + p.solidArea.height / 2;
        } else if (cameraMode == CAMERA_WHEEL) {
            targetX = wheel.x + wheel.size / 2;
            targetY = wheel.y + wheel.size / 2;
        } else if (cameraMode == CAMERA_VIEW) {
            targetX = maxWorldCol * tileSize / 2;
            targetY = maxWorldRow * tileSize / 2;
        }

        currentCamX += (targetX - currentCamX) * lerpAmount;
        currentCamY += (targetY - currentCamY) * lerpAmount;

        // Apply the smoothed coordinates to the final camera variables used by draw()
        cameraWorldX = (int) currentCamX;
        cameraWorldY = (int) currentCamY;
    }

    public void nextTurn() {
        ui.transitionAlpha = 1.0f;

        if (players != null) {
            for (Player p : players) {
                if (p != null & p.blockedTurns > 0) {

                }
            }
        }

        if (players != null && players[currentPlayerTurn] != null) {
            players[currentPlayerTurn].updateBuffDurations();
        }

        globalTurnCount++;
        if (globalTurnCount % INCOME_INTERVAL == 0) {
            for (Player p : players) {
                if (p != null) p.applyPropertyIncome();
            }
        }

        int startTurn = currentPlayerTurn;
        // move to the next player

        do {
            currentPlayerTurn++;
            if (currentPlayerTurn >= numberOfPlayers) {
                currentPlayerTurn = 0;
            }

            if (currentPlayerTurn == startTurn) break;

            Player next = players[currentPlayerTurn];
            if (next.blockedTurns > 0) {
                next.blockedTurns--;
                continue;
            }
        } while (players[currentPlayerTurn].hasReachedEnd
                && this.gameState != this.chapterTransitionState);

        currentPlayer = players[currentPlayerTurn];

        wheel.hasSpun = false;
        cardTriggeredThisTurn = false;
        endWaitHandled = false;
        ui.checkStatChanges();
    }

    public void createPlayers() {

        players = new Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {

            int selectedCharacterNum = selectedCharacterOrder[i] + 1;
            players[i] = new Player(this, keyH, selectedCharacterNum);

            if (isFullGame) {
                BoardNode start = players[i].getStartPoint();

                players[i].worldX = start.col * tileSize;
                players[i].worldY = start.row * tileSize;
            }
            players[i].currentPathIndex = 0;   // THIS is your board index
        }

        for (int i = 0; i < numberOfPlayers; i++) {
            if (playerNames != null) {
                int characterSlot = selectedCharacterOrder[i]; // which character box they picked
                if (characterSlot < playerNames.length && playerNames[characterSlot] != null) {
                    players[i].playerName = playerNames[characterSlot];
                }
            }
        }

        if (!isFullGame) {
            int startMoney = (selectedSingleChapter < CHAPTER_START_MONEY.length)
                    ? CHAPTER_START_MONEY[selectedSingleChapter] : 0;
            for (Player p : players) {
                p.setBaseMoney(startMoney);
            }
        }

        ui.initStatSnapshots();
    }

    public void drawToTempScreen() {

        if (gameState == titleState) {
            ui.draw(g2);
        } else if (gameState == playerCountState) {
            ui.draw(g2);
        } else if (gameState == gameModeSelectState) {
            ui.draw(g2);
        } else if (gameState == chapterSelectState) {
            ui.draw(g2);
        } else if (gameState == characterSelectState) {
            ui.draw(g2);
        } else if (gameState == leaderboardState) {
            ui.draw(g2);
        } else {
            tileM.drawGround(g2);
            tileM.drawDecor(g2);

            wheel.draw(g2);

            for (Player p : players) {
                p.draw(g2);
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].drawNpc(g2);
                }
            }

            ui.playerStatus(g2);
            ui.draw(g2);

            if (currentCard != null) {
                currentCard.draw(g2);
            }

            if (currentCurseCard != null) {
                currentCurseCard.draw(g2);
            }

            if (propertyTab != null) {
                propertyTab.draw(g2);
            }

            if (propertyOverlay != null && propertyOverlay.isActive()) {
                propertyOverlay.draw(g2);
            }

            if (gameState == settingsState) {
                ui.draw(g2);
            }
        }
    }

    public void setupCharacterSelection() {

        players = new Player[numberOfPlayers];
        hasSelected = new boolean[numberOfPlayers];
        selectedCharacterOrder = new int[numberOfPlayers];
        playerNames = new String[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            playerNames[i] = "Player " + (i + 1);
        }

        selectionIndex = 0;
        currentSelectingPlayer = 0;
        namingForPlayer = -1;
        showNameInput = false;
        ui.showConfirmPopup = false;
        ui.nameInputBuffer = "";
    }

    public void triggerRandomCard() {

        Collections.shuffle(chapter1Deck);

        currentCard = chapter1Deck.get(0);
        currentCard.resetCard();
        currentCard.spawnCard();
    }

    public void triggerRandomCurseCard() {
        List<card.CurseCard> deck = (currentChapter == 2) ? chapter2CurseDeck : chapter1CurseDeck;
        if (deck == null || deck.isEmpty()) {
            return;
        }

        Collections.shuffle(deck);
        currentCurseCard = deck.get(0);
        currentCurseCard.resetCard();
        currentCurseCard.spawnCard();
    }


    public void paintComponent(Graphics g) { //builtin in java

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        drawToTempScreen();   // draw everything to temp image
        g2.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
    }

    public void checkEndCondition() {
        BoardNode node = currentPlayer.getCurrentNode();
        if (node != null && node.isEndNode && !currentPlayer.hasReachedEnd) {
            currentPlayer.hasReachedEnd = true;
            currentPlayer.boardMoving = false;
            playersAtEnd++;
            cardTriggeredThisTurn = true;
            endWaitHandled = false;

            if (playersAtEnd >= numberOfPlayers) {
                // All players done start transition
                gameState = chapterTransitionState;
                transitionTimer = 0;
                transitionAlpha = 0f;
            } else {
                gameState = endWaitState;
            }
        }
    }

    //chapter

    public void loadChapter(int chapter) {
        currentChapter = chapter;
        ui.initStatSnapshots();

        // load chapter specifics
        switch (chapter) {
            case 2 -> {
                sound.playMusic(Sound.MUSIC_CHAPTER2);
                tileM.loadChapter2();
                aSetter.setNPCChapter2();
                chapter1Deck = Chapter2Cards.createChapter2Deck(this);
                chapter2CurseDeck = CurseCardDeck.createChapter2Deck(this);
            }
            case 3 -> {
                sound.playMusic(Sound.MUSIC_CHAPTER3);
                tileM.loadChapter3();
                aSetter.setNPCChapter3();
                aSetter.setWheelForChapter3();
                chapter1Deck = Chapter3Cards.createChapter3Deck(this);
            }
//            case 4 -> {
//                sound.playMusic(Sound.MUSIC_CHAPTER4);
//                tileM.loadChapter4();
//                aSetter.setNPCChapter4();
//                chapter1Deck = Chapter4Cards.createChapter4Deck(this);
//            }
            default -> {
                System.out.println("No chapter " + chapter + " defined.");
                gameState = titleState; // or a game-over/credits state
                return;
            }
        }

        // This part is identical for every chapter
        for (Player p : players) {
            switch (chapter) {
                case 2 -> p.pathLoader.loadChapter2Path();
                case 3 -> p.pathLoader.loadChapter3Path();
                case 4 -> p.pathLoader.loadChapter4Path();
            }

            BoardNode start = p.pathLoader.pathPoints.get(0);
            p.worldX = start.col * tileSize;
            p.worldY = start.row * tileSize;
            p.currentPathIndex = 0;
            p.hasReachedEnd = false;
            p.boardMoving = false;
        }

        currentCamX = players[0].worldX;
        currentCamY = players[0].worldY;
    }

    public void startingSingleChapter(int chapter) {
        selectedSingleChapter = chapter;
        currentChapter = chapter;

        switch (chapter) {
            case 1 -> {
                sound.playMusic(Sound.MUSIC_CHAPTER1);
                aSetter.setNPC();
                chapter1Deck = Chapter1Cards.createChapter1Deck(this);
            }
            case 2 -> {
                sound.playMusic(Sound.MUSIC_CHAPTER2);
                tileM.loadChapter2();
                aSetter.setNPC();
                chapter1Deck = Chapter2Cards.createChapter2Deck(this);
                chapter2CurseDeck = CurseCardDeck.createChapter2Deck(this);
            }
            case 3 -> {
                sound.playMusic(Sound.MUSIC_CHAPTER3);
                tileM.loadChapter3();
                aSetter.setNPC();
                chapter1Deck = Chapter3Cards.createChapter3Deck(this);
                chapter2CurseDeck = CurseCardDeck.createChapter3Deck(this);
            }
            default -> {
                System.out.println("Chapter " + chapter + " not yet implemented");
                gameState = chapterSelectState;
                return;
            }
        }

        if (players != null) {

            int startMoney = (chapter < CHAPTER_START_MONEY.length) ? CHAPTER_START_MONEY[chapter] : 0;
            for (Player p : players) {

                switch (chapter) {
                    case 2 -> p.pathLoader.loadChapter2Path();
                }

                BoardNode start = p.pathLoader.pathPoints.get(0);
                p.worldX = start.col * tileSize;
                p.worldY = start.row * tileSize;
                p.currentPathIndex = 0;
                p.hasReachedEnd = false;
                p.boardMoving = false;

                p.setBaseMoney(startMoney);
            }

            if (players != null) {

                 startMoney = (chapter < CHAPTER_START_MONEY.length) ? CHAPTER_START_MONEY[chapter] : 0;
                for (Player p : players) {

                    switch (chapter) {
                        case 3 -> p.pathLoader.loadChapter3Path();
                    }

                    BoardNode start = p.pathLoader.pathPoints.get(0);
                    p.worldX = start.col * tileSize;
                    p.worldY = start.row * tileSize;
                    p.currentPathIndex = 0;
                    p.hasReachedEnd = false;
                    p.boardMoving = false;

                    p.setBaseMoney(startMoney);
                }
                currentCamX = players[0].worldX;
                currentCamY = players[0].worldY;
            }

            playersAtEnd = 0;
            currentPlayerTurn = 0;
            wheel.hasSpun = false;
            cardTriggeredThisTurn = false;
            gameState = playState;
        }
    }
}