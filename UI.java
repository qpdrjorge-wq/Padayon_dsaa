package main;

import card.CriticalDecision;
import entity.Entity;
import entity.NPC_Jumpscare;
import entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    UtilityTool uTool = new UtilityTool();
    Font arial_40, arial_80;
    int creditsScrollY = 0;

    public String currentDialogue = "";
    BufferedImage backgroundImage;
    BufferedImage character;
    public int commandNum = 0;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    public boolean showConfirmPopup = false;

    public boolean scenarioActive = false;
    public int currentScenarioIndex = -1;
    public Rectangle[] scenarioOptionButtons;

    private BufferedImage[][] playerImages;
    private final Color[] colors = {
            new Color(125, 154, 129, 200),
            new Color(196, 92, 40, 200),
            new Color(206, 148, 89, 200),
            new Color(165, 111, 135, 200)};

    public Rectangle confirmButtonRect;
    public Rectangle[] selectButtons;
    public Rectangle[] playerStatus;
    private BufferedImage[] playerProfile;

    BufferedImage happyIcon;
    BufferedImage starIcon;
    BufferedImage moneyIcon;
    int iconSize = 20;
    int screenWidth, screenHeight;
    public float transitionAlpha = 0;

    public boolean buffActive = false;
    public String currentBuffText = "";
    public int titleMenuCommand = 0;
    public Rectangle[] titleMenuRects = new Rectangle[4];
    public int recordsScrollOffset = 0;

    //settings menu states
    public boolean settingsMenuOpen = false;
    public Rectangle settingsButtonRect;
    public Rectangle[] settingsSliderRects = new Rectangle[3];
    public Rectangle exitGameButtonRect;
    public int draggingSlider = -1;

    //dialogue field
    public boolean dialogueChoicesActive = false;
    public String[] currentChoices;
    public Rectangle[] choiceButtons;
    public String currentNPCName = "";

    //name selection
    public String nameInputBuffer = "";
    public Rectangle nameConfirmRect;
    public Rectangle nameCancelRect;

    //buff dropdown
    private boolean[] buffDropdownOpen;
    private Rectangle[] buffDropdownToggleRects;

    //leaderboard stuffs
    public Rectangle leaderboardMainMenuRect;
    public Rectangle leaderboardPlayAgainRect;

    //chapter selection
    public Rectangle[] chapterSelectButtons;

    //game mode selection stuffs
    public Rectangle gameModeFullRect;
    public Rectangle gameModeChapterRect;

    //item trading fields
    public boolean itemTradeUIActive = false;
    public String  itemTradeItemName  = "";
    public Rectangle itemTradeGiveBtn = new Rectangle();
    public Rectangle itemTradeKeepBtn = new Rectangle();

    //animated background fields
    BufferedImage spriteSheet;
    BufferedImage[] frames;
    int frameWidth = 540;
    int frameHeight = 338;
    int totalFrames = 11;
    int currentFrame = 0;
    int frameCounter = 0;
    int frameSpeed = 8;

    private static class StatPopup {
        int playerIndex;
        int slot;
        int dH, dR, dM;
        int life;
        static final int MAX_LIFE = 180; //player stats
        static final int FADE_IN  = 20;
        static final int FADE_OUT = 40;

        int spawnY;

        StatPopup(int playerIndex, int slot, int dH, int dR, int dM, int spawnY) {
            this.playerIndex = playerIndex;
            this.slot        = slot;
            this.dH = dH; this.dR = dR; this.dM = dM;
            this.life        = MAX_LIFE;
            this.spawnY      = spawnY;
        }

        float alpha() {
            if (life > MAX_LIFE - FADE_IN) return 1f - (life - (MAX_LIFE - FADE_IN)) / (float) FADE_IN;
            if (life < FADE_OUT)           return life / (float) FADE_OUT;
            return 1f;
        }

        // all popups for a player move up at the same rate
        float drift() {
            return (1f - life / (float) MAX_LIFE) * 30f;
        }
    }


    private final List<StatPopup> statPopups = new ArrayList<>();

    private int[] lastHappiness;
    private int[] lastReputation;
    private int[] lastMoney;

    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80 = new Font("Arial", Font.ITALIC, 80);

        playerImages = new BufferedImage[GamePanel.TOTAL_CHARACTERS][8];

        for (int i = 0; i < GamePanel.TOTAL_CHARACTERS; i++){
            BufferedImage fullSheet = uTool.getImage("/player/player" + (i+1) + "_frontjump.png");

            int frameWidth = fullSheet.getWidth() / 8;
            int frameHeight = fullSheet.getHeight();

            for (int f = 0; f < 8; f++){
                playerImages[i][f] = fullSheet.getSubimage(f * frameWidth, 0, frameWidth, frameHeight);
            }
        }

        try{
            happyIcon = uTool.loadIcons("happy.png", iconSize - 2, iconSize - 2);
            starIcon = uTool.loadIcons("star.png", iconSize, iconSize);
            moneyIcon = uTool.loadIcons("money.png", iconSize, iconSize);
        } catch (IOException e){
            e.printStackTrace();
        }

        loadBackgroundAnimation();
        character = uTool.getImage("/player/rac_down_1.png");
        screenWidth = gp.screenWidth;
        screenHeight = gp.screenHeight;
    }

    public void loadBackgroundAnimation() {
        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/backgrounds/openingScreen.png"));

            frames = new BufferedImage[totalFrames];

            for (int i = 0; i < totalFrames; i++) {
                frames[i] = spriteSheet.getSubimage(
                        i * frameWidth,
                        0,
                        frameWidth,
                        frameHeight
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initStatSnapshots() {
        if (gp.players == null) return;
        int n = gp.numberOfPlayers;
        lastHappiness  = new int[n];
        lastReputation = new int[n];
        lastMoney      = new int[n];
        for (int i = 0; i < n; i++) {
            lastHappiness[i]  = gp.players[i].happiness;
            lastReputation[i] = gp.players[i].reputation;
            lastMoney[i]      = gp.players[i].money;
        }
    }

    public void checkStatChanges() {
        if (gp.players == null || lastHappiness == null) return;

        final int CARD_H   = 115;
        final int CARD_PAD = 20;
        final int HUD_BASE = gp.tileSize * 2;
        final int HUD_X    = 25;
        final int CARD_W   = 180;
        final int POPUP_STEP = 36;

        for (int i = 0; i < gp.numberOfPlayers; i++) {
            Player p = gp.players[i];
            int dH = p.happiness  - lastHappiness[i];
            int dR = p.reputation - lastReputation[i];
            int dM = p.money      - lastMoney[i];

            if (dH != 0 || dR != 0 || dM != 0) {
                int usedSlots = 0;
                for (StatPopup existing : statPopups) {
                    if (existing.playerIndex == i) usedSlots++;
                }

                // Calculate spawn Y once, accounting for slot stacking
                int cardY  = HUD_BASE + i * (CARD_H + CARD_PAD);
                int spawnY = cardY + CARD_H / 2 + usedSlots * POPUP_STEP;

                statPopups.add(new StatPopup(i, usedSlots, dH, dR, dM, spawnY));
            }

            lastHappiness[i]  = p.happiness;
            lastReputation[i] = p.reputation;
            lastMoney[i]      = p.money;
        }
    }

    public void handleBuffDropdownClick(int vx, int vy) {
        if (buffDropdownToggleRects==null) return;
        for (int i=0; i<buffDropdownToggleRects.length; i++) {
            if (buffDropdownToggleRects[i]!=null && buffDropdownToggleRects[i].contains(vx,vy)) {
                buffDropdownOpen[i]=!buffDropdownOpen[i]; return;
            }
        }
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(arial_80);
        g2.setColor(Color.white);

        //states
        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        } else if(gp.gameState == gp.titleLeaderboardState){
            drawTitleLeaderboardScreen(g2);
        } else if(gp.gameState == gp.creditState){
            drawCreditsScreen(g2);
        }else if (gp.gameState == gp.playerCountState){
            drawPlayerCountScreen();
        } else if (gp.gameState == gp.gameModeSelectState){
            drawGameModeScreen();
        } else if (gp.gameState == gp.chapterSelectState){
            drawChapterSelectScreen();
        }
        else if (gp.gameState == gp.characterSelectState){
            drawCharacterSelection();
        }
        //Play state
        else if(gp.gameState == gp.playState){

        }
        //pause state
        else if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        //Dialogue state
        else if(gp.gameState == gp.dialogueState){
            drawDialogueScreen();
        }

        else if(gp.gameState == gp.buffState){
            drawBuffPopup(g2);
        } else if(gp.gameState == gp.settingsState){
            drawSettingsPanel(g2);
        } else if (gp.gameState == gp.leaderboardState){
            drawLeaderboardScreen(g2);
        }

        if(scenarioActive){
            drawScenarioScreen(g2);
        }

        if (gp.gameState != gp.titleState &&
                gp.gameState != gp.titleLeaderboardState &&
                gp.gameState != gp.creditState &&
                gp.gameState != gp.playerCountState &&
                gp.gameState != gp.gameModeSelectState &&
                gp.gameState != gp.chapterSelectState &&
                gp.gameState != gp.characterSelectState &&
                gp.gameState != gp.leaderboardState) {
            drawSettingsButton(g2);
        }

        if (gp.gameState == gp.endWaitState) {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(gp.screenWidth/2 - 200, gp.screenHeight/2 - 30, 400, 60, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(22f));
            String msg = "Waiting for other players... (" + gp.playersAtEnd + "/" + gp.numberOfPlayers + ")";
            int x = getXForCenteredText(msg);
            g2.drawString(msg, x, gp.screenHeight/2 + 8);
        }

        if (gp.gameState == gp.chapterTransitionState) {
            int alpha = (int)(gp.transitionAlpha * 255);
            g2.setColor(new Color(0, 0, 0, Math.min(255, alpha)));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

            if (gp.transitionAlpha >= 0.9f) {
                g2.setColor(new Color(255, 255, 255, Math.min(255, (int)((gp.transitionAlpha - 0.9f) * 10 * 255))));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32f));
                String chapterText = "Chapter 2";
                int x = getXForCenteredText(chapterText);
                g2.drawString(chapterText, x, gp.screenHeight/2);
            }
        }

        if (gp.gameState == gp.jumpscareState) {
            // find whichever npc slot holds your NPC_Jumpscare
            for (Entity e : gp.npc) {
                if (e instanceof NPC_Jumpscare js) {
                    js.drawJumpscare(g2);
                    break;
                }
            }
        }


        drawStatPopups(g2);


    }

    //dialogue helpers
    public void showDialogueChoices(String[] choices) {
        dialogueChoicesActive = true;
        currentChoices = choices;
    }

    public void hideDialogueChoices() {
        dialogueChoicesActive = false;
        currentChoices = null;
    }

    //game mode screen
    public void drawGameModeScreen() {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // --- Title ---
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 52f));
        g2.setColor(Color.WHITE);
        String title = "Select Game Mode";
        g2.drawString(title, getXForCenteredText(title), gp.tileSize * 3);

        // --- Card panel ---
        int cardW = 340, cardH = 260;
        int gap   = 60;
        int totalW = cardW * 2 + gap;
        int startX = gp.screenWidth / 2 - totalW / 2;
        int cardY  = gp.screenHeight / 2 - cardH / 2 - 20;

        int fgX = startX;
        boolean fgHover = (commandNum == 0);
        drawModeCard(g2, fgX, cardY, cardW, cardH,
                "🌐  Full Game",
                "Play all chapters from\nstart to finish.",
                "All chapters  |  Normal start money",
                fgHover);
        gameModeFullRect = new Rectangle(fgX, cardY, cardW, cardH);

        int csX = startX + cardW + gap;
        boolean csHover = (commandNum == 1);
        drawModeCard(g2, csX, cardY, cardW, cardH,
                "📖  Single Chapter",
                "Jump into any chapter\nwith adjusted starting money.",
                "One chapter  |  Chapter starting money",
                csHover);
        gameModeChapterRect = new Rectangle(csX, cardY, cardW, cardH);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18f));
        g2.setColor(new Color(200, 200, 200, 180));
        String hint = "← → to select   |   ENTER to confirm";
        g2.drawString(hint, getXForCenteredText(hint), cardY + cardH + 50);
    }

    private void drawModeCard(Graphics2D g2, int x, int y, int w, int h,
                              String heading, String body, String footer, boolean selected) {
        // Shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x + 6, y + 6, w, h, 24, 24);

        // Background
        Color bg = selected ? new Color(255, 215, 90, 230) : new Color(30, 28, 50, 220);
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, 24, 24);

        // Border
        g2.setColor(selected ? new Color(255, 200, 40) : new Color(100, 100, 140));
        g2.setStroke(new BasicStroke(selected ? 3f : 1.5f));
        g2.drawRoundRect(x, y, w, h, 24, 24);
        g2.setStroke(new BasicStroke(1f));

        Color textCol = selected ? new Color(40, 30, 0) : Color.WHITE;

        // Heading
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
        g2.setColor(textCol);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(heading, x + w/2 - fm.stringWidth(heading)/2, y + 56);

        // Body lines
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
        g2.setColor(selected ? new Color(60, 40, 0) : new Color(200, 200, 220));
        int lineY = y + 96;
        for (String line : body.split("\n")) {
            fm = g2.getFontMetrics();
            g2.drawString(line, x + w/2 - fm.stringWidth(line)/2, lineY);
            lineY += 26;
        }

        // Footer divider
        g2.setColor(selected ? new Color(200, 150, 0, 120) : new Color(100, 100, 140, 100));
        g2.drawLine(x + 20, y + h - 60, x + w - 20, y + h - 60);

        // Footer text
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 13f));
        g2.setColor(selected ? new Color(80, 60, 0) : new Color(150, 150, 180));
        fm = g2.getFontMetrics();
        g2.drawString(footer, x + w/2 - fm.stringWidth(footer)/2, y + h - 34);
    }

    //chapter selection
    public void drawChapterSelectScreen() {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50f));
        g2.setColor(Color.WHITE);
        String title = "Choose a Chapter";
        g2.drawString(title, getXForCenteredText(title), gp.tileSize * 3);

        // Chapter info — extend this array as you add chapters
        String[] chapterNames = { "Chapter 1", "Chapter 2", "chapter 3", "chapter 4"};
        String[] chapterDesc  = { "The Beginning", "A New Horizon", "Struggles", "Purgatory" };
        int[]    startMoney   = {
                GamePanel.CHAPTER_START_MONEY[1],
                GamePanel.CHAPTER_START_MONEY[2],
                GamePanel.CHAPTER_START_MONEY[3],
                GamePanel.CHAPTER_START_MONEY[4]
        };

        int cols    = chapterNames.length;
        int cardW   = 260, cardH = 200, gap = 40;
        int totalW  = cols * cardW + (cols - 1) * gap;
        int startX  = gp.screenWidth / 2 - totalW / 2;
        int cardY   = gp.screenHeight / 2 - cardH / 2;

        chapterSelectButtons = new Rectangle[cols];

        for (int i = 0; i < cols; i++) {
            int cx = startX + i * (cardW + gap);
            boolean sel = (commandNum == i);

            // Shadow
            g2.setColor(new Color(0, 0, 0, 110));
            g2.fillRoundRect(cx + 5, cardY + 5, cardW, cardH, 20, 20);

            // Background
            g2.setColor(sel ? new Color(255, 215, 90, 230) : new Color(35, 30, 55, 220));
            g2.fillRoundRect(cx, cardY, cardW, cardH, 20, 20);

            // Border
            g2.setColor(sel ? new Color(255, 195, 30) : new Color(100, 90, 140));
            g2.setStroke(new BasicStroke(sel ? 3f : 1.5f));
            g2.drawRoundRect(cx, cardY, cardW, cardH, 20, 20);
            g2.setStroke(new BasicStroke(1f));

            Color textCol = sel ? new Color(40, 30, 0) : Color.WHITE;

            // Chapter number badge
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26f));
            g2.setColor(textCol);
            FontMetrics fm = g2.getFontMetrics();
            String num = "Ch. " + (i + 1);
            g2.drawString(num, cx + cardW/2 - fm.stringWidth(num)/2, cardY + 48);

            // Chapter name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
            g2.setColor(sel ? new Color(60, 40, 0) : new Color(210, 200, 255));
            fm = g2.getFontMetrics();
            g2.drawString(chapterNames[i], cx + cardW/2 - fm.stringWidth(chapterNames[i])/2, cardY + 78);

            // Description
            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14f));
            g2.setColor(sel ? new Color(80, 60, 0) : new Color(170, 165, 200));
            fm = g2.getFontMetrics();
            g2.drawString(chapterDesc[i], cx + cardW/2 - fm.stringWidth(chapterDesc[i])/2, cardY + 104);

            // Divider
            g2.setColor(sel ? new Color(200, 150, 0, 100) : new Color(100, 90, 140, 80));
            g2.drawLine(cx + 20, cardY + cardH - 70, cx + cardW - 20, cardY + cardH - 70);

            // Starting money label
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
            g2.setColor(sel ? new Color(60, 100, 20) : new Color(120, 200, 100));
            String moneyStr = "Starting ₱: " + startMoney[i];
            fm = g2.getFontMetrics();
            g2.drawString(moneyStr, cx + cardW/2 - fm.stringWidth(moneyStr)/2, cardY + cardH - 42);

            chapterSelectButtons[i] = new Rectangle(cx, cardY, cardW, cardH);
        }

        // Hint
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 17f));
        g2.setColor(new Color(200, 200, 200, 180));
        String hint = "← → to select   |   ENTER to confirm   |   ESC to go back";
        g2.drawString(hint, getXForCenteredText(hint), cardY + cardH + 55);
    }

    //leaderboard screen
    public void drawLeaderboardScreen(Graphics2D g2) {
        // Background
        g2.drawImage(backgroundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // ---- Decorative header banner ----
        int bannerH = 90;
        GradientPaint gp2 = new GradientPaint(
                0, 0, new Color(30, 20, 60),
                0, bannerH, new Color(60, 40, 10));
        g2.setPaint(gp2);
        g2.fillRect(0, 0, gp.screenWidth, bannerH);
        g2.setColor(new Color(255, 200, 40, 80));
        g2.fillRect(0, bannerH - 3, gp.screenWidth, 3);

        // Title
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 46f));
        g2.setColor(new Color(255, 220, 60));
        String title = "🏆  Final Rankings";
        g2.drawString(title, getXForCenteredText(title), 62);

        // Mode sub-label
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 16f));
        g2.setColor(new Color(200, 180, 120));
        String modeLbl = gp.isFullGame
                ? "Full Game — All Chapters"
                : "Chapter " + gp.selectedSingleChapter + " — Single Chapter Run";
        g2.drawString(modeLbl, getXForCenteredText(modeLbl), 84);

        if (gp.leaderboard == null) return;

        // ---- Table layout ----
        int tableW  = 800;
        int rowH    = 72;
        int tableX  = gp.screenWidth / 2 - tableW / 2;
        int headerY = bannerH + 30;

        // Column X positions (left edge of each)
        int[] colX = {
                tableX,          // Rank
                tableX + 70,     // Name
                tableX + 330,    // Happiness
                tableX + 460,    // Reputation
                tableX + 600,    // Money
                tableX + 720,    // Score
        };
        String[] colHeaders = { "#", "Player", "😊", "⭐", "₱", "Score" };

        // Header row
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
        g2.setColor(new Color(255, 200, 60));
        FontMetrics hfm = g2.getFontMetrics();
        for (int c = 0; c < colHeaders.length; c++) {
            g2.drawString(colHeaders[c], colX[c], headerY);
        }

        // Divider under header
        g2.setColor(new Color(255, 200, 60, 80));
        g2.drawLine(tableX, headerY + 6, tableX + tableW, headerY + 6);

        // Rank medal colors
        Color[] medalColors = {
                new Color(255, 215, 0),   // Gold
                new Color(192, 192, 192), // Silver
                new Color(205, 127, 50),  // Bronze
                Color.WHITE,
        };

        for (int i = 0; i < gp.leaderboard.length; i++) {
            GamePanel.LeaderboardEntry e = gp.leaderboard[i];
            int rowY = headerY + 20 + i * (rowH + 8);

            // Row background
            Color rowBg = (i == 0)
                    ? new Color(255, 215, 0, 25)
                    : (i % 2 == 0) ? new Color(255,255,255,8) : new Color(0,0,0,0);
            g2.setColor(rowBg);
            g2.fillRoundRect(tableX - 10, rowY, tableW + 20, rowH, 12, 12);

            // Row border for top 3
            if (i < 3) {
                g2.setColor(new Color(medalColors[i].getRed(),
                        medalColors[i].getGreen(), medalColors[i].getBlue(), 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(tableX - 10, rowY, tableW + 20, rowH, 12, 12);
                g2.setStroke(new BasicStroke(1f));
            }

            Color col = i < medalColors.length ? medalColors[i] : Color.WHITE;
            int textY = rowY + rowH/2 + 6;

            // Rank
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 26f));
            g2.setColor(col);
            String rankStr = (i == 0) ? "🥇" : (i == 1) ? "🥈" : (i == 2) ? "🥉" : String.valueOf(e.rank);
            g2.drawString(rankStr, colX[0], textY);

            // Name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.setColor(Color.WHITE);
            g2.drawString(e.name, colX[1], textY);

            // Happiness
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18f));
            g2.setColor(new Color(100, 220, 140));
            g2.drawString(String.valueOf(e.happiness), colX[2], textY);

            // Reputation
            g2.setColor(new Color(240, 200, 60));
            g2.drawString(String.valueOf(e.reputation), colX[3], textY);

            // Money
            g2.setColor(new Color(80, 200, 255));
            g2.drawString(String.valueOf(e.money), colX[4], textY);

            // Score (composite)
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 19f));
            g2.setColor(col);
            g2.drawString(String.valueOf(e.score), colX[5], textY);
        }

        // ---- "Sorted with Insertion Sort" footnote ----
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 13f));
        g2.setColor(new Color(150, 150, 180, 160));
        String footnote = "Rankings sorted using Insertion Sort  •  Score = Happiness + Reputation + Money";
        g2.drawString(footnote, getXForCenteredText(footnote), gp.screenHeight - 80);

        // ---- Buttons ----
        int btnW = 190, btnH = 44;
        int gap  = 30;
        int totalBtnW = btnW * 2 + gap;
        int btnBaseX = gp.screenWidth / 2 - totalBtnW / 2;
        int btnY = gp.screenHeight - 55;

        // Main Menu button
        leaderboardMainMenuRect = new Rectangle(btnBaseX, btnY, btnW, btnH);
        drawLeaderboardButton(g2, btnBaseX, btnY, btnW, btnH, "Main Menu",
                new Color(70, 50, 140), new Color(130, 100, 220));

        // Play Again button
        int playAgainX = btnBaseX + btnW + gap;
        leaderboardPlayAgainRect = new Rectangle(playAgainX, btnY, btnW, btnH);
        drawLeaderboardButton(g2, playAgainX, btnY, btnW, btnH, "Play Again",
                new Color(40, 110, 50), new Color(70, 190, 90));
    }

    public void updateBackgroundAnimation() {
        frameCounter++;

        if (frameCounter >= frameSpeed) {
            currentFrame++;
            frameCounter = 0;

            if (currentFrame >= frames.length) {
                currentFrame = 0; // loop animation
            }
        }
    }

    //leaderbaord helper
    private void drawLeaderboardButton(Graphics2D g2, int x, int y, int w, int h,
                                       String label, Color bg, Color border) {
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(x + 3, y + 3, w, h, 12, 12);
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, 12, 12);
        g2.setColor(border);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, w, h, 12, 12);
        g2.setStroke(new BasicStroke(1f));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(label, x + w/2 - fm.stringWidth(label)/2,
                y + h/2 + fm.getAscent()/2 - 3);
    }

    public void drawStardewPanel(Graphics2D g2, int x, int y, int width, int height){

        // Shadow (offset)
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(x + 6, y + 6, width, height);

        // Outer dark border
        g2.setColor(new Color(70, 40, 20)); // dark brown
        g2.fillRect(x, y, width, height);

        // Inner border
        g2.setColor(new Color(120, 75, 35)); // mid brown
        g2.fillRect(x + 4, y + 4, width - 8, height - 8);

        // Main background
        g2.setColor(new Color(181, 140, 90)); // warm Stardew brown
        g2.fillRect(x + 8, y + 8, width - 16, height - 16);
    }

    public void drawScenarioScreen(Graphics2D g2){

        int width = screenWidth - gp.tileSize * 4;
        int height = screenHeight / 2;
        int x = gp.tileSize * 2;
        int y = screenHeight/2 - height/2;

        drawStardewPanel(g2, x, y, width, height);

        CriticalDecision decision =
                gp.decisionManager.chapter1Decisions[currentScenarioIndex];

        // Draw scenario text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 26F));
        g2.setColor(Color.white);

        int textX = x + 40;
        int textY = y + 60;

        for(String line : decision.text.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 35;
        }

        scenarioOptionButtons = new Rectangle[decision.options.length];

        for(int i = 0; i < decision.options.length; i++){

            int btnWidth = 400;
            int btnHeight = 50;

            int totalButtonHeight = decision.options.length * 65;
            int startY = y + height - totalButtonHeight - 40;

            int btnX = x + width/2 - btnWidth/2;
            int btnY = startY + (i * 65);

            scenarioOptionButtons[i] =
                    new Rectangle(btnX, btnY, btnWidth, btnHeight);

            // hover effect
            boolean hovered =
                    scenarioOptionButtons[i].contains(
                            gp.mouseH.mouseX,
                            gp.mouseH.mouseY
                    );

            // Button shadow
            g2.setColor(new Color(0,0,0,100));
            g2.fillRect(btnX + 3, btnY + 3, btnWidth, btnHeight);

            //outer border
            g2.setColor(new Color(60, 35, 15));
            g2.fillRect(btnX, btnY, btnWidth, btnHeight);

            // inner border
            g2.setColor(new Color(140, 95, 50));
            g2.fillRect(btnX + 3, btnY + 3, btnWidth - 6, btnHeight - 6);

            // main wood
            Color wood = new Color(201, 160, 95);

            if(hovered){
                wood = new Color(221, 180, 115); // brighter on hover
            }

            g2.setColor(wood);
            g2.fillRect(btnX + 6, btnY + 6, btnWidth - 12, btnHeight - 12);

            //text
            g2.setColor(new Color(40, 20, 10));
            g2.drawString(decision.options[i],
                    btnX + btnWidth/2 - g2.getFontMetrics().stringWidth(decision.options[i]) / 2,
                    btnY + 33);
        }

    }

    public void playAnimation(){
        spriteCounter++;
        if (spriteCounter > 10){
            spriteNum++;
            if (spriteNum >= 8){
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }

    public void drawTitleScreen() {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(0, 0, 0, 40)); // Much lighter
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        GradientPaint beachGlow = new GradientPaint(
                0, gp.screenHeight * 0.3f, new Color(255, 220, 150, 20),  // Sandy top
                gp.screenWidth, gp.screenHeight, new Color(20, 80, 120, 60)); // Ocean bottom
        g2.setPaint(beachGlow);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setPaint(null);

        float pulse = (float)(Math.sin(System.currentTimeMillis() * 0.002) * 0.5 + 0.5);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 86f));
        String line1 = "PADAYON";

        Color[] glowColors = {
                new Color(255, 140, 60, (int)(20 * pulse)),  // Orange glow
                new Color(255, 200, 80, (int)(16 * pulse)),  // Yellow glow
                new Color(255, 100, 140, (int)(12 * pulse))  // Pink glow
        };

        for (int i = 0; i < glowColors.length; i++) {
            g2.setColor(glowColors[i]);
            int offset = (i + 1) * 2;
            int gx = getXForRightAlignedText(line1, 60);
            g2.drawString(line1, gx - offset, gp.tileSize * 2 + 10 - offset);
            g2.drawString(line1, gx + offset, gp.tileSize * 2 + 10 + offset);
        }

        g2.setColor(new Color(255, 180, 80)); // Warm golden orange
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 86f));
        g2.drawString(line1, getXForRightAlignedText(line1, 60), gp.tileSize * 2 + 10);

        // Subtitle: sandy beige with subtle shadow
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 30f));
        g2.setColor(new Color(240, 220, 180)); // Sandy beige
        String line2 = "Changing of Fates";
        g2.drawString(line2, getXForRightAlignedText(line2, 60), gp.tileSize * 2 + 56);

        // Beach divider: seashell-inspired wave line
        int divW = 380;
        int divX = gp.screenWidth - divW - 60;
        int divY = gp.tileSize * 2 + 72;

        // Wave stroke
        g2.setColor(new Color(255, 210, 120, 140));
        g2.setStroke(new BasicStroke(2.5f));

        // Simple wave path
        int[] waveX = {divX, divX + 60, divX + 120, divX + 200, divX + 280, divX + divW};
        int[] waveY = {divY, divY - 8, divY + 8, divY - 6, divY + 6, divY};

        for (int i = 0; i < waveX.length - 1; i++) {
            g2.drawLine(waveX[i], waveY[i], waveX[i+1], waveY[i+1]);
        }
        g2.setStroke(new BasicStroke(1f));

        String[] menuItems = { "New Game", "Records", "Credits", "Quit" };
        Color[] itemColors = {
                new Color(80, 160, 60),      // Tropical green
                new Color(60, 120, 180),     // Ocean blue
                new Color(200, 140, 80),     // Coconut brown
                new Color(180, 80, 60)       // Coral red
        };

        int btnW = 300;  // Slightly wider
        int btnH = 56;
        int gap = 18;
        int totalH = menuItems.length * btnH + (menuItems.length - 1) * gap;

        int rightPad = 60;
        int bottomPad = 70;
        int menuStartX = gp.screenWidth - btnW - rightPad;
        int menuStartY = gp.screenHeight - bottomPad - totalH;

        titleMenuRects = new Rectangle[menuItems.length];

        for (int i = 0; i < menuItems.length; i++) {
            int btnX = menuStartX;
            int btnY = menuStartY + i * (btnH + gap);
            boolean sel = (commandNum == i);

            titleMenuRects[i] = new Rectangle(btnX, btnY, btnW, btnH);

            Color base = itemColors[i];

            // Subtle drop shadow
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(btnX + 3, btnY + 3, btnW, btnH, 20, 20);

            // Button background: wood/ocean texture feel
            if (sel) {
                // Selected: brighter, glowing
                g2.setColor(new Color(base.getRed() + 40, base.getGreen() + 40,
                        base.getBlue() + 40, 240));
            } else {
                // Normal: semi-transparent driftwood look
                g2.setColor(new Color(20, 25, 30, 200));
            }
            g2.fillRoundRect(btnX, btnY, btnW, btnH, 20, 20);

            // Tropical highlight bar (palm leaf style)
            if (sel) {
                g2.setColor(new Color(255, 255, 220, 200)); // Coconut cream
                g2.fillRoundRect(btnX + 8, btnY + 12, 6, btnH - 24, 6, 6);
            }

            // Subtle wood grain lines
            g2.setColor(new Color(60, 40, 20, sel ? 100 : 60));
            g2.setStroke(new BasicStroke(0.5f));
            for (int grain = 0; grain < 4; grain++) {
                int grainY = btnY + 15 + grain * 10;
                g2.drawLine(btnX + 20, grainY, btnX + btnW - 20, grainY);
            }
            g2.setStroke(new BasicStroke(1f));

            // Border with sandy highlight
            g2.setColor(sel
                    ? base.brighter().brighter()
                    : new Color(base.getRed(), base.getGreen(), base.getBlue(), 120));
            g2.setStroke(new BasicStroke(sel ? 2.5f : 1.5f));
            g2.drawRoundRect(btnX, btnY, btnW, btnH, 20, 20);
            g2.setStroke(new BasicStroke(1f));

            // Text with sandy drop shadow
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
            Color textColor = sel ? Color.WHITE : new Color(245, 235, 210);

            // Text shadow
            g2.setColor(new Color(100, 80, 60, 140));
            g2.drawString(menuItems[i], btnX + 28, btnY + btnH / 2 + g2.getFontMetrics().getAscent() / 2 - 2);

            // Main text
            g2.setColor(textColor);
            g2.drawString(menuItems[i], btnX + 26, btnY + btnH / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
        }

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 14f));
        g2.setColor(new Color(200, 180, 140, 200)); // Seashell pinkish
        String hint = "↑↓ navigate   ENTER / click select";

        // Seashell shadow
        g2.setColor(new Color(100, 70, 60, 120));
        g2.drawString(hint, 26, gp.screenHeight - 14);
        g2.setColor(new Color(200, 180, 140, 200));
        g2.drawString(hint, 24, gp.screenHeight - 16);

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 13f));
        g2.setColor(new Color(210, 190, 150, 180)); // Warm sand
        String ver = "v1.0  •  2025";

        // Sand shadow
        g2.setColor(new Color(120, 100, 80, 100));
        g2.drawString(ver, getXForCenteredText(ver) + 1, gp.screenHeight - 14);
        g2.setColor(new Color(210, 190, 150, 180));
        g2.drawString(ver, getXForCenteredText(ver), gp.screenHeight - 16);
    }

    public int getXForRightAlignedText(String text, int rightPadding) {
        int textWidth = g2.getFontMetrics().stringWidth(text);
        return gp.screenWidth - textWidth - rightPadding;
    }

    public void drawTitleLeaderboardScreen(Graphics2D g2) {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Header
        int bannerH = 80;
        GradientPaint grad = new GradientPaint(0, 0, new Color(20, 15, 50),
                0, bannerH, new Color(50, 35, 10));
        g2.setPaint(grad);
        g2.fillRect(0, 0, gp.screenWidth, bannerH);
        g2.setPaint(null);
        g2.setColor(new Color(255, 200, 40, 90));
        g2.fillRect(0, bannerH - 2, gp.screenWidth, 2);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40f));
        g2.setColor(new Color(255, 215, 60));
        String title = "🏆  All-Time Records";
        g2.drawString(title, getXForCenteredText(title), 54);

        List<LeaderboardSave.SavedEntry> entries = gp.savedLeaderboard;

        if (entries == null || entries.isEmpty()) {
            g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 22f));
            g2.setColor(new Color(180, 170, 200));
            String empty = "No records yet. Play a game to get on the board!";
            g2.drawString(empty, getXForCenteredText(empty), gp.screenHeight / 2);
        } else {

            // Column layout
            int tableW = 820;
            int tableX = gp.screenWidth / 2 - tableW / 2;
            int headerY = bannerH + 28;
            int rowH    = 52;

            int[] colX = { tableX, tableX + 50, tableX + 280,
                    tableX + 420, tableX + 530, tableX + 640, tableX + 730 };
            String[] headers = { "#", "Name", "Mode", "😊", "⭐", "₱", "Score" };

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
            g2.setColor(new Color(255, 200, 60));
            for (int c = 0; c < headers.length; c++) {
                g2.drawString(headers[c], colX[c], headerY);
            }
            g2.setColor(new Color(255, 200, 60, 60));
            g2.drawLine(tableX, headerY + 5, tableX + tableW, headerY + 5);

            Color[] medalColors = {
                    new Color(255, 215, 0),
                    new Color(192, 192, 192),
                    new Color(205, 127, 50),
                    Color.WHITE
            };

            int maxVisible = Math.min(entries.size(), 8);
            for (int i = 0; i < maxVisible; i++) {
                LeaderboardSave.SavedEntry e = entries.get(i);
                int rowY = headerY + 18 + i * (rowH + 4);

                // Row bg
                g2.setColor(i == 0
                        ? new Color(255, 215, 0, 18)
                        : i % 2 == 0 ? new Color(255, 255, 255, 6) : new Color(0, 0, 0, 0));
                g2.fillRoundRect(tableX - 8, rowY, tableW + 16, rowH, 10, 10);

                Color col = i < medalColors.length ? medalColors[i] : Color.WHITE;
                int ty = rowY + rowH / 2 + 6;

                // Rank
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
                g2.setColor(col);
                String rank = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : String.valueOf(i + 1);
                g2.drawString(rank, colX[0], ty);

                // Name
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.setColor(Color.WHITE);
                g2.drawString(truncate(e.name, 16), colX[1], ty);

                // Mode + date
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12f));
                g2.setColor(new Color(160, 155, 190));
                g2.drawString(e.chapter, colX[2], ty - 6);
                g2.drawString(e.date,    colX[2], ty + 10);

                // Stats
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15f));
                g2.setColor(new Color(100, 220, 140));
                g2.drawString(String.valueOf(e.happiness),  colX[3], ty);
                g2.setColor(new Color(240, 200, 60));
                g2.drawString(String.valueOf(e.reputation), colX[4], ty);
                g2.setColor(new Color(80, 200, 255));
                g2.drawString(String.valueOf(e.money),      colX[5], ty);

                // Score
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 17f));
                g2.setColor(col);
                g2.drawString(String.valueOf(e.score), colX[6], ty);
            }
        }

        // Back button
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        int backW = 160, backH = 38;
        int backX = gp.screenWidth / 2 - backW / 2;
        int backY = gp.screenHeight - 52;
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(backX + 3, backY + 3, backW, backH, 10, 10);
        g2.setColor(new Color(60, 55, 90));
        g2.fillRoundRect(backX, backY, backW, backH, 10, 10);
        g2.setColor(new Color(140, 130, 180));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(backX, backY, backW, backH, 10, 10);
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        String back = "← Back";
        g2.drawString(back, backX + backW / 2 - fm.stringWidth(back) / 2,
                backY + backH / 2 + fm.getAscent() / 2 - 3);
    }

    public void drawCreditsScreen(Graphics2D g2) {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setColor(new Color(0, 0, 0, 210));

        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42f));
        g2.setColor(new Color(220, 180, 255));
        String title = "✨ Credits";
        g2.drawString(title, getXForCenteredText(title), gp.tileSize * 2);

        g2.setColor(new Color(180, 140, 220, 80));
        g2.drawLine(gp.screenWidth / 2 - 220, gp.tileSize * 2 + 12,
                gp.screenWidth / 2 + 220, gp.tileSize * 2 + 12);

        String[][] credits = {
                { "Lead Developer", "" },
                { "", "" },
                { "", "@Jamela Ann Rayos" },
                { "", "Designed the core game mechanics" },
                { "", "Implemented the logic of the game" },
                { "", "Implemented chapter 1 and 4" },

                { "", "" },
                { "Assistant Developer", "" },
                { "", "" },
                { "", "@Paulina D. Jorge" },
                { "", "Designed the path navigation" },
                { "", "Implemented chapter 2 and 3 map" },

                { "", "" },
                { "Art & Visual Design", "" },
                { "", "" },
                { "", "@Angel D. Abella" },
                { "", "" },
                { "", " Designed character 2, 3 and 4" },
                { "", " Chapter 1 assets" },
                { "", " Designed Chapter 1 NPCs" },

                { "", "" },
                { "", "@Kyla Nicole Guevarra" },
                { "", "" },
                { "", " Designed Chapter 1 and 2 map" },
                { "", " Designed Character 1" },
                { "", " Designed Wheel and other elements from chapter 1 to 2" },
                { "", " Designed Cards" },

                { "", "" },
                { "", "@Paulina D. Jorge" },
                { "", "" },
                { "", " Designed Title Screen Opening" },
                { "", " Designed Chapter 3 map" },
                { "", " Designed Chapter 3 NPCs" },

                { "", "" },
                { "", "@Jamela Ann Rayos" },
                { "", "" },
                { "", " Designed Chapter 4 map" },
                { "", " Designed cat NPC and Chapter 4 NPCs" },
                { "", " Game UI and UX Designer" },

                { "", "" },
                { "Music", "" },
                { "", "" },
                { "", " @Kyla Nicole Guevarra" },
                { "", " Opening Screen " },
                { "", " Chapter 1 and 2 background music" },

                { "", "@Paulina D. Jorge" },
                { "", "" },
                { "", " Sound effects" },
                { "", " Chapter 3 background music" },

                { "", "@Jamela Ann Rayos" },
                { "", "" },
                { "", " Button Sound effect" },
                { "", " Chapter 4 background music" },

                { "", "" },
                { "Narrative And Writing", "" },
                { "", "" },
                { "", "@Jamela Ann Rayos" },
                { "", "" },
                { "", "Chapter 1 Main Concept" },
                { "", "Chapter 4 Main COncept" },

                { "", "@Paulina D. Jorge" },
                { "", "" },
                { "", "Chapter 1 Main Concept" },
                { "", "Chapter 3 Main Concept" },

                { "", "@Kyla Nicole Guevarra" },
                { "", "" },
                { "", "Chapter 2 Main Concept" },

                { "", "@Angel D. Abella" },
                { "", "" },
                { "", "Chapter 1 Main Concept" },

                { "", "" },

                { "Third Party Licenses", "" },
                { "", "" },
                { "", "@Libraries/Engines" },
                { "", "" },
                { "", "IntelliJ: JetBrains" },
                { "", "Java Swing: Sun Microsystems" },

                { "", "" },
                { "", "@Art Tools" },
                { "", "" },
                { "", "Asperite" },
                { "", "Pixelorama" },

                { "", "" },
                { "Game Assets", "" },
                { "", "" },
                { "", "Limezu" },
                { "", "Anasabdin" },

                { "", "" },
                { "Audio", "" },
                { "", "" },
                { "", "Tomodachi Life Theme: Nintendo" },

                { "", "" },
                { "Game Basis", "" },
                { "", "" },
                { "", "Game of Life: Marmalade Game Studio"},
                { "", "Stardew Valley: Concerned Ape" },
                { "", "Pokemon Leaf Green: Game Freak" },
                { "", "Monopoly: Hasbro" },

                { "", "" },
                { "Special Thanks", "" },
                { "", "" },
                { "", "We would like to thank our professor, Ms. Janice Capule for giving us the opportunity and support to explore and experiment with this project, learning to integrate what we have learned into a real life product. \n" },
                {"", ""},
                {"", ""},
                {"", "We would also like to thank our friends, Bagasbas, Sheilamae, Garcia, Krizha Cassandra, and Samudio, Renzo for supporting and helping us throughout the development of this game project."},

                { "", "" },
                {"", ""},
                {"", ""},
                {"", ""},
                { "PADAYON: Changing of Fates", "" },
                { "", "Version 1.0 • 2025" },
        };

        int lineH = 34;
        int startY = gp.tileSize * 3 + creditsScrollY;

        int contentHeight = credits.length * 40 + 400;
        int minScroll = Math.min(0, gp.screenHeight - contentHeight - 100);
        int maxScroll = 0;

        creditsScrollY = Math.max(minScroll, Math.min(maxScroll, creditsScrollY));

        Shape oldClip = g2.getClip();

        int clipY = gp.tileSize * 2 + 30;
        int clipHeight = gp.screenHeight - clipY - 100;

        g2.setClip(0, clipY, gp.screenWidth, clipHeight);

        int centerX = gp.screenWidth / 2;
        int maxWidth = 600; // wrapping width

        for (String[] row : credits) {

            String label = row[0];
            String value = row[1];

            if (!label.isEmpty() && value.isEmpty()) {

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
                g2.setColor(new Color(255, 200, 60));

                String text = label.toUpperCase();
                g2.drawString(text, getXForCenteredText(text), startY);

                startY += lineH;

                // underline
                FontMetrics fm = g2.getFontMetrics();
                int w = fm.stringWidth(text);
                g2.setColor(new Color(255, 200, 60, 80));
                g2.drawLine(centerX - w/2, startY - 10, centerX + w/2, startY - 10);

                startY += 10;
            }

            else if (value.startsWith("@")) {

                String name = value.substring(1);

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
                g2.setColor(new Color(230, 220, 255));

                g2.drawString(name, getXForCenteredText(name), startY);

                startY += lineH;
            }

            else if (!value.isEmpty()) {

                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
                g2.setColor(new Color(180, 170, 210));

                drawWrappedText(g2, "• " + value, centerX, startY, maxWidth);

                startY += g2.getFontMetrics().getHeight() + 4;
            }

            else {
                startY += lineH / 2;
            }
        }

        g2.setClip(oldClip);

        int backW = 160, backH = 38;
        int backX = gp.screenWidth / 2 - backW / 2;
        int backY = gp.screenHeight - 52;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(backX + 3, backY + 3, backW, backH, 10, 10);

        g2.setColor(new Color(50, 40, 80));
        g2.fillRoundRect(backX, backY, backW, backH, 10, 10);

        g2.setColor(new Color(160, 130, 210));
        g2.drawRoundRect(backX, backY, backW, backH, 10, 10);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        String back = "← Back";
        g2.drawString(back,
                backX + backW / 2 - fm.stringWidth(back) / 2,
                backY + backH / 2 + fm.getAscent() / 2 - 3);
    }

    private void drawWrappedText(Graphics2D g2, String text, int centerX, int y, int maxWidth) {

        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");

        String line = "";
        int lineHeight = fm.getHeight();

        for (String word : words) {

            String testLine = line.isEmpty() ? word : line + " " + word;

            if (fm.stringWidth(testLine) > maxWidth) {
                int drawX = centerX - fm.stringWidth(line) / 2;
                g2.drawString(line, drawX, y);
                y += lineHeight;
                line = word;
            } else {
                line = testLine;
            }
        }

        if (!line.isEmpty()) {
            int drawX = centerX - fm.stringWidth(line) / 2;
            g2.drawString(line, drawX, y);
        }
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    public void drawCharacterSelection() {

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        selectButtons = new Rectangle[gp.TOTAL_CHARACTERS];

        if (gp.selectedCharacterOrder == null || gp.hasSelected == null) {
            gp.setupCharacterSelection();
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 52F));
        g2.setColor(new Color(255, 220, 160, 240)); // Sandy sunset glow

        // Title drop shadow
        g2.setColor(new Color(100, 70, 40, 160));
        String title = "Character Selection";
        g2.drawString(title, getXForCenteredText(title) + 3, gp.tileSize * 2 + 3);

        // Main title
        g2.setColor(new Color(255, 230, 180, 255));
        g2.drawString(title, getXForCenteredText(title), gp.tileSize * 2);

        int totalBoxes = GamePanel.TOTAL_CHARACTERS;
        int boxWidth = gp.screenWidth / totalBoxes;
        int boxY = gp.tileSize * 4;
        int boxHeight = gp.screenHeight - boxY;
        int padding = 8; // Slightly more padding
        playAnimation();

        Color[] colors = { // Beach palette
                new Color(255, 200, 120, 200), // Sandy gold
                new Color(100, 160, 200, 200), // Ocean blue
                new Color(200, 140, 80, 200),  // Coconut brown
                new Color(120, 180, 100, 200)  // Palm green
        };

        for (int i = 0; i < totalBoxes; i++) {
            int boxX = (i * boxWidth) + padding;
            int adjW = boxWidth - padding * 2;

            boolean isAlreadySelected = false;
            String nameLabel = "Character " + (i + 1);

            if (gp.selectedCharacterOrder != null && gp.hasSelected != null) {
                for (int playerIndex = 0; playerIndex < gp.numberOfPlayers; playerIndex++) {
                    if (playerIndex < gp.selectedCharacterOrder.length &&
                            playerIndex < gp.hasSelected.length &&
                            gp.hasSelected[playerIndex] &&
                            gp.selectedCharacterOrder[playerIndex] == i) {
                        isAlreadySelected = true;
                        if (gp.playerNames != null && playerIndex < gp.playerNames.length &&
                                gp.playerNames[playerIndex] != null && !gp.playerNames[playerIndex].isEmpty()) {
                            nameLabel = gp.playerNames[playerIndex];
                        }
                        break;
                    }
                }
            }

            // outer glow
            g2.setColor(new Color(255, 200, 100, 60));
            g2.fillRoundRect(boxX - 10, boxY - 10, adjW + 20, boxHeight + 20, 50, 50);

            // Main character frame
            Color frameColor = isAlreadySelected
                    ? new Color(80, 140, 100, 240)  // Tropical green tint
                    : new Color(255, 240, 200, 200); // Sandy frosted glass

            g2.setColor(frameColor);
            g2.fillRoundRect(boxX + 4, boxY + 4, adjW - 8, boxHeight - 8, 45, 45);

            // inner shadow border
            g2.setColor(new Color(0, 0, 0, 120));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(boxX + 6, boxY + 6, adjW - 12, boxHeight - 12, 42, 42);
            g2.setStroke(new BasicStroke(1f));

            // character image
            int imgSize = gp.tileSize * 2;
            int imgX = boxX + adjW / 2 - imgSize / 2;
            int imgY = boxY + boxHeight / 2 - imgSize;

            //critical
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.drawImage(playerImages[i][spriteNum], imgX, imgY, imgSize, imgSize * 2, null);

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // 5. Name label with sandy drop shadow
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
            FontMetrics fm = g2.getFontMetrics();
            int textX = boxX + adjW / 2 - fm.stringWidth(nameLabel) / 2;
            int textY = boxY + 38;

            // Drop shadow
            g2.setColor(new Color(80, 60, 40, 180));
            g2.drawString(nameLabel, textX + 2, textY + 2);

            // Main text (seashell white)
            g2.setColor(new Color(245, 235, 210, 255));
            g2.drawString(nameLabel, textX, textY);

            // 6. SELECT BUTTON - Tropical leaf button
            String btnText = isAlreadySelected ? "✓ CHOSEN" : "SELECT";
            int btnW = 160;
            int btnH = 44;
            int btnX = boxX + adjW / 2 - btnW / 2;
            int btnY = boxY + boxHeight - 90;
            int hitPad = 10;

            selectButtons[i] = new Rectangle(btnX - hitPad, btnY - hitPad,
                    btnW + hitPad * 2, btnH + hitPad * 2);
            boolean hov = selectButtons[i].contains(gp.mouseH.mouseX, gp.mouseH.mouseY);

            // Button glow
            if (hov || isAlreadySelected) {
                g2.setColor(new Color(255, 255, 220, 80));
                g2.fillRoundRect(btnX - 6, btnY - 6, btnW + 12, btnH + 12, 20, 20);
            }

            // Button background
            Color btnBg = isAlreadySelected
                    ? new Color(100, 160, 80, 240)  // Success green
                    : hov
                    ? new Color(255, 220, 140, 240) // Sandy hover
                    : new Color(200, 180, 140, 200); // Driftwood

            g2.setColor(btnBg);
            g2.fillRoundRect(btnX, btnY, btnW, btnH, 18, 18);

            // Button border
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(isAlreadySelected ? 3f : 2f));
            g2.drawRoundRect(btnX, btnY, btnW, btnH, 18, 18);
            g2.setStroke(new BasicStroke(1f));

            // Button text with shadow
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            fm = g2.getFontMetrics();
            int btnTextX = btnX + btnW / 2 - fm.stringWidth(btnText) / 2;
            int btnTextY = btnY + btnH / 2 + fm.getAscent() / 2 - 2;

            g2.setColor(new Color(40, 30, 20, 180));
            g2.drawString(btnText, btnTextX + 1, btnTextY + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(btnText, btnTextX, btnTextY);
        }

        if (showConfirmPopup) { drawConfirmPopup(g2); }
        if (gp.showNameInput && gp.namingForPlayer >= 0) {
            drawNameInputModal(g2, gp.namingForPlayer);
        }
    }

    private void drawStatLine(Graphics2D g2, BufferedImage icon, String value, int x, int y) {
        if (icon != null) {
            g2.drawImage(icon, x, y - 14, null);
        }

        int text = x + (icon != null ? 22 : 0);
        g2.setColor(new Color(0, 0,0, 100));
        g2.drawString(value, text+1, y+1);
        g2.setColor(Color.WHITE);
        g2.drawString(value, text, y);
    }

    private void drawBuffDropdown(Graphics2D g2, List<card.Buff> buffs, int x, int y, int width){

        final int ROW_H = 28;
        final int PADDING = 8;
        int panelH = buffs.size() * ROW_H + PADDING * 2;
        g2.setColor(new Color(0, 0,0, 80));
        g2.fillRoundRect(x+3, y+3, width, panelH, 10, 10);
        g2.setColor(new Color(20,18,32,238));
        g2.fillRoundRect(x,y,width,panelH,10,10);
        g2.setColor(new Color(155,138,78,115));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x,y,width,panelH,10,10);
        int rowY=y+PADDING;

        for (card.Buff buff : buffs) {
            boolean pos=buff.changeHappiness>0||buff.changeReputation>0||buff.changeMoney>0;
            boolean neg=buff.changeHappiness<0||buff.changeReputation<0||buff.changeMoney<0;
            Color tc=pos&&!neg?new Color(38,158,88):neg&&!pos?new Color(178,48,48):new Color(168,128,18);

            g2.setColor(new Color(tc.getRed(),tc.getGreen(),tc.getBlue(),32));
            g2.fillRoundRect(x+4,rowY,width-8,ROW_H-2,6,6);
            g2.setColor(tc); g2.fillRoundRect(x+4,rowY+3,4,ROW_H-8,3,3);

            String durStr=buff.isChapterLong()?"CHAP":buff.duration+"T";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,9f)); FontMetrics fm=g2.getFontMetrics();

            int durW=fm.stringWidth(durStr)+8;
            int durX=x+width-durW-6;
            int durY=rowY+(ROW_H-14)/2;

            g2.setColor(new Color(tc.getRed(),tc.getGreen(),tc.getBlue(),175));
            g2.fillRoundRect(durX,durY,durW,14,4,4);
            g2.setColor(Color.WHITE);
            g2.drawString(durStr,durX+4,durY+11);

            StringBuilder sb=new StringBuilder();
            if(buff.changeHappiness!=0) {
                sb.append(buff.changeHappiness>0?"+":"").append(buff.changeHappiness).append("\uD83D\uDE0A ");
            }

            if(buff.changeReputation!=0) {
                sb.append(buff.changeReputation>0?"+":"").append(buff.changeReputation).append("\u2605 ");
            }

            if(buff.changeMoney!=0) {
                sb.append(buff.changeMoney>0?"+":"").append(buff.changeMoney).append("\u20b1");
            }

            String summary=sb.toString().trim();
            g2.setFont(g2.getFont().deriveFont(Font.BOLD,10f));
            fm=g2.getFontMetrics();
            while(fm.stringWidth(summary)>durX-x-18&&summary.length()>3) {
                summary=summary.substring(0,summary.length()-1);
            }

            g2.setColor(new Color(218,213,198)); g2.drawString(summary,x+13,rowY+ROW_H/2+fm.getAscent()/2-2);
            rowY+=ROW_H;
        }
    }

    public void drawConfirmPopup(Graphics2D g2){

        int width = 400;
        int height = 200;

        int x = gp.screenWidth / 2 - width / 2;
        int y = gp.screenHeight / 2 - height / 2;

        //popup background
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(x, y, width, height, 25, 25);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 25, 25);

        String message = "All players ready!";

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);

        int textX = x + (width / 2) - (textWidth / 2);
        int textY = y + 70;

        g2.drawString(message, textX, textY);

        // confirm button
        int btnWidth = 140;
        int btnHeight = 45;

        int btnX = x + (width / 2) - (btnWidth / 2);
        int btnY = y + 110;

        confirmButtonRect = new Rectangle(btnX, btnY, btnWidth, btnHeight);

        g2.setColor(new Color(255,255,255,180));
        g2.fillRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

        g2.setColor(Color.WHITE);
        g2.drawRoundRect(btnX, btnY, btnWidth, btnHeight, 15, 15);

        //button text
        String btnText = "CONFIRM";

        int btnTextWidth = fm.stringWidth(btnText);
        int btnTextX = btnX + (btnWidth / 2) - (btnTextWidth / 2);
        int btnTextY = btnY + (btnHeight / 2) + (fm.getAscent()/2) - 4;

        g2.drawString(btnText, btnTextX, btnTextY);
    }

    public void drawPauseScreen(){

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public void drawDialogueScreen(){

        // Window
        int boxX = gp.tileSize*2;
        int boxY = gp.tileSize/2;
        int boxW = gp.screenWidth - (gp.tileSize*4);

        boolean showTradeButtons = itemTradeUIActive && gp.activeNPC != null;
        String[] safeChoices = (dialogueChoicesActive) ? currentChoices : null; // snapshot
        boolean showChoiceButtons = safeChoices != null;
        int extraH = 0;
        if (showTradeButtons) extraH = 60;
        if (showChoiceButtons) extraH = safeChoices.length * 55 + 20;

        int boxH = gp.tileSize * 4 + extraH;

        // Background dim
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Dialogue panel
        drawSubWindow(boxX, boxY, boxW, boxH);

        //name plate
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        FontMetrics fm = g2.getFontMetrics();

        int namePaddingX = 14;
        int nameW = fm.stringWidth(currentNPCName) + namePaddingX * 2;
        int nameH = 28;
        int nameX = boxX + 20;
        int nameY = boxY - (nameH / 2);

        // Shadow
        g2.setColor(new Color(0,0,0,120));
        g2.fillRoundRect(nameX + 2, nameY + 2, nameW, nameH, 10, 10);

        // Background
        g2.setColor(new Color(41, 41, 59));
        g2.fillRoundRect(nameX, nameY, nameW, nameH, 10, 10);

        // Border
        g2.setColor(new Color(255, 243, 215));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(nameX, nameY, nameW, nameH, 10, 10);

        // Text
        g2.setColor(Color.WHITE);
        g2.drawString(currentNPCName, nameX + namePaddingX, nameY + 19);

        // ===== DIALOGUE TEXT =====
        int textX = boxX + gp.tileSize;
        int textY = boxY + gp.tileSize + 10;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 26F));
        g2.setColor(Color.WHITE);

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 36;
        }

        // ===== NORMAL CHOICE BUTTONS =====
        if (showChoiceButtons) {
            int btnWidth = 300;
            int btnHeight = 45;
            int startY = textY + 20;

            choiceButtons = new Rectangle[safeChoices.length];  // ← safeChoices

            for (int i = 0; i < safeChoices.length; i++) {     // ← safeChoices
                int btnX = gp.screenWidth / 2 - btnWidth / 2;
                int btnY = startY + i * (btnHeight + 10);

                choiceButtons[i] = new Rectangle(btnX, btnY, btnWidth, btnHeight);
                boolean hovered = choiceButtons[i].contains(gp.mouseH.mouseX, gp.mouseH.mouseY);

                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRoundRect(btnX + 3, btnY + 3, btnWidth, btnHeight, 12, 12);
                g2.setColor(hovered ? new Color(255, 255, 255, 220) : new Color(240, 240, 240, 180));
                g2.fillRoundRect(btnX, btnY, btnWidth, btnHeight, 12, 12);
                g2.setColor(new Color(80, 80, 90));
                g2.drawRoundRect(btnX, btnY, btnWidth, btnHeight, 12, 12);

                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20f));
                g2.setColor(new Color(30, 20, 10));
                fm = g2.getFontMetrics();
                g2.drawString(safeChoices[i],                   // ← safeChoices
                        btnX + btnWidth / 2 - fm.stringWidth(safeChoices[i]) / 2,  // ← safeChoices
                        btnY + btnHeight / 2 + fm.getAscent() / 2 - 4);
            }
        }

        // ===== ITEM TRADE BUTTONS (third button row) =====
        if (showTradeButtons) {
            int giveW = 260, keepW = 180, btnH = 44;
            int totalW = giveW + keepW + 20;
            int startX = gp.screenWidth / 2 - totalW / 2;
            int btnY   = boxY + boxH - btnH - 16;

            itemTradeGiveBtn.setBounds(startX, btnY, giveW, btnH);
            itemTradeKeepBtn.setBounds(startX + giveW + 20, btnY, keepW, btnH);

            // Give button (gold)
            boolean giveHov = itemTradeGiveBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(startX + 3, btnY + 3, giveW, btnH, 12, 12);
            g2.setColor(giveHov ? new Color(220, 170, 30) : new Color(180, 135, 20));
            g2.fillRoundRect(startX, btnY, giveW, btnH, 12, 12);
            g2.setColor(new Color(255, 230, 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(startX, btnY, giveW, btnH, 12, 12);
            g2.setStroke(new BasicStroke(1f));
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
            g2.setColor(Color.WHITE);
            fm = g2.getFontMetrics();
            String giveLabel = "🎁 Give: " + itemTradeItemName;
            g2.drawString(giveLabel,
                    startX + giveW / 2 - fm.stringWidth(giveLabel) / 2,
                    btnY + btnH / 2 + fm.getAscent() / 2 - 3);

            // Keep button (grey)
            int keepX = startX + giveW + 20;
            boolean keepHov = itemTradeKeepBtn.contains(gp.mouseH.mouseX, gp.mouseH.mouseY);
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect(keepX + 3, btnY + 3, keepW, btnH, 12, 12);
            g2.setColor(keepHov ? new Color(100, 100, 110) : new Color(70, 70, 80));
            g2.fillRoundRect(keepX, btnY, keepW, btnH, 12, 12);
            g2.setColor(new Color(150, 150, 170));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(keepX, btnY, keepW, btnH, 12, 12);
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(Color.WHITE);
            String keepLabel = "Keep Item";
            g2.drawString(keepLabel,
                    keepX + keepW / 2 - fm.stringWidth(keepLabel) / 2,
                    btnY + btnH / 2 + fm.getAscent() / 2 - 3);
        }
    }

    public void drawBuffPopup(Graphics2D g2){
        //window
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int height = gp.tileSize*4;
        int width = gp.screenWidth - (gp.tileSize*4);

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentBuffText.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0, 0, 0, 220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawPlayerCountScreen(){

        BufferedImage frame = frames[currentFrame];

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(frame, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
        String text = "Select Number of Players";
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(36f));
        FontMetrics fm = g2.getFontMetrics();
        String[] options = {"2 PLayers", "3 PLayers", "4 players"};

        for (int i = 0; i < options.length; i++){

            text = options[i];
            x = getXForCenteredText(text);
            y = gp.tileSize * (6 + i * 2);

            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int paddingX = 40;
            int paddingY = 20;

            int boxX = x - paddingX;
            int boxY = y - fm.getAscent() - paddingY;
            int boxWidth = textWidth + paddingX * 2;
            int boxHeight = textHeight + paddingY * 2;

            float floatOffset = 0;
            if(commandNum == i){
                floatOffset = (float)Math.sin(System.currentTimeMillis() * 0.003) * 3;

                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);

                // subtle glow border
                g2.setColor(new Color(255, 255, 255, 120));
                g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);
            }

            g2.setColor(Color.white);
            g2.drawString(text, x, (int)(y + floatOffset));
        }
    }

    private void drawNameInputModal(Graphics2D g2, int playerIndex) {

        g2.setColor(new Color(0,0,0,185));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        int mW=460;
        int mH=230;
        int mX = gp.screenWidth/2-mW/2;
        int mY = gp.screenHeight/2-mH/2;

        g2.setColor(new Color(28,26,42,248));
        g2.fillRoundRect(mX,mY,mW,mH,20,20);
        g2.setColor(new Color(200,180,120));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(mX+1,mY+1,mW-2,mH-2,20,20);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,22f));
        g2.setColor(new Color(240,215,140));
        String prompt="Enter name for Character "+(playerIndex+1);
        FontMetrics fm=g2.getFontMetrics();
        g2.drawString(prompt,mX+mW/2-fm.stringWidth(prompt)/2,mY+44);

        int fX = mX+30;
        int fY = mY+60;
        int fW = mW-60;
        int fH = 38;

        g2.setColor(new Color(14,12,22));
        g2.fillRoundRect(fX,fY,fW,fH,8,8);
        g2.setColor(new Color(160,150,200));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(fX,fY,fW,fH,8,8);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,20f));
        g2.setColor(Color.WHITE);
        String display = nameInputBuffer + ((System.currentTimeMillis()/500) % 2 == 0 ? "|" : "");
        fm=g2.getFontMetrics();
        g2.drawString(display,fX+10,fY+fH/2+fm.getAscent()/2-3);
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC,11f));
        g2.setColor(new Color(140,130,170));

        String hint=nameInputBuffer.length()+"/12 characters";
        fm = g2.getFontMetrics();
        g2.drawString(hint,fX+fW-fm.stringWidth(hint),fY+fH+14);

        int bW=130;
        int bH=38;
        int confirmX = mX + mW / 2 - bW - 10;
        int confirmY = mY + mH - 60;
        nameConfirmRect = new Rectangle(confirmX,confirmY,bW,bH);

        boolean canConfirm=!nameInputBuffer.trim().isEmpty();
        g2.setColor(canConfirm?new Color(55,135,85):new Color(48,48,58));
        g2.fillRoundRect(confirmX,confirmY,bW,bH,10,10);
        g2.setColor(canConfirm?new Color(110,215,145):new Color(95,95,115));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(confirmX,confirmY,bW,bH,10,10);
        g2.setStroke(new BasicStroke(1f));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,16f));
        g2.setColor(canConfirm?Color.WHITE:new Color(110,110,130));
        fm=g2.getFontMetrics();

        g2.drawString("CONFIRM",confirmX+bW/2-fm.stringWidth("CONFIRM")/2,confirmY+bH/2+fm.getAscent()/2-3);
        int cancelX=mX+mW/2+10;
        nameCancelRect=new Rectangle(cancelX,confirmY,bW,bH);
        g2.setColor(new Color(125,45,45));
        g2.fillRoundRect(cancelX,confirmY,bW,bH,10,10);
        g2.setColor(new Color(215,95,95));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(cancelX,confirmY,bW,bH,10,10);
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.WHITE);
        g2.drawString("CANCEL",cancelX+bW/2-fm.stringWidth("CANCEL")/2,confirmY+bH/2+fm.getAscent()/2-3);
    }

    public void playerStatus(Graphics2D g2) {

        playerStatus = new Rectangle[gp.numberOfPlayers];

        if (buffDropdownOpen == null || buffDropdownOpen.length != gp.numberOfPlayers) {
            buffDropdownOpen = new boolean[gp.numberOfPlayers];
            buffDropdownToggleRects = new Rectangle[gp.numberOfPlayers];
        }

        final int CARD_W  = 200;
        final int CARD_H  = 175;   // taller to fit career + baby row
        final int PAD     = 14;
        final int BASE_X  = 18;
        final int BASE_Y  = gp.tileSize * 2;

        for (int i = 0; i < gp.numberOfPlayers; i++) {

            entity.Player p = gp.players[i];
            int y = BASE_Y + i * (CARD_H + PAD);

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(BASE_X + 6, y + 6, CARD_W, CARD_H, 20, 20);

            g2.setColor(new Color(40, 25, 15));
            g2.fillRoundRect(BASE_X, y, CARD_W, CARD_H, 20, 20);
            g2.setColor(new Color(170, 130, 85));
            g2.fillRoundRect(BASE_X + 2, y + 2, CARD_W - 4, CARD_H - 4, 18, 18);
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(BASE_X + 2, y + 2, CARD_W - 4, 30, 18, 18);

            int portW = 50, portH = CARD_H - 14;
            int portX = BASE_X + 6, portY = y + 7;
            g2.setColor(new Color(120, 85, 50));
            g2.fillRoundRect(portX, portY, portW, portH, 12, 12);
            g2.setColor(new Color(0, 0, 0, 50));
            g2.drawRoundRect(portX, portY, portW, portH, 12, 12);

            getProfileImg();
            int ci = p.playerNum - 1;
            if (playerProfile != null && ci >= 0 && ci < playerProfile.length && playerProfile[ci] != null) {
                int imgX = portX + (portW - gp.tileSize) / 2;
                int imgY = portY + (portH - gp.tileSize) / 2;
                g2.drawImage(playerProfile[ci], imgX, imgY, gp.tileSize, gp.tileSize, null);
            }

            int x      = portX + portW + 10;
            int textY  = y + 28;

            // Player name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
            String displayName = p.playerName;
            FontMetrics nameFm = g2.getFontMetrics();
            int maxNameW = CARD_W - (portW + 22);
            while (nameFm.stringWidth(displayName) > maxNameW && displayName.length() > 1)
                displayName = displayName.substring(0, displayName.length() - 1);

            g2.setColor(new Color(0, 0, 0, 120));
            g2.drawString(displayName, x + 1, textY + 1);
            g2.setColor(new Color(255, 230, 180));
            g2.drawString(displayName, x, textY);

            textY += 20;

            // Stats
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
            drawStatLine(g2, happyIcon, String.valueOf(p.happiness),   x, textY); textY += 20;
            drawStatLine(g2, starIcon,  String.valueOf(p.reputation),  x, textY); textY += 20;
            drawStatLine(g2, moneyIcon, String.valueOf(p.money),       x, textY); textY += 20;

            if (p.career != null) {
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
                g2.setColor(new Color(60, 160, 255));

                String careerLabel = p.career.icon + " " + p.career.title;
                FontMetrics cfm = g2.getFontMetrics();
                while (cfm.stringWidth(careerLabel) > CARD_W - (portW + 22) && careerLabel.length() > 2)
                    careerLabel = careerLabel.substring(0, careerLabel.length() - 1);

                g2.setColor(new Color(0, 0, 0, 80));
                g2.drawString(careerLabel, x + 1, textY + 1);
                g2.setColor(new Color(130, 210, 255));
                g2.drawString(careerLabel, x, textY);
                textY += 16;

                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 10f));
                g2.setColor(new Color(100, 220, 130));
                g2.drawString("₱" + p.career.salary + "/paycheck", x, textY);
                textY += 14;

            } else {
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 10f));
                g2.setColor(new Color(150, 130, 100));
                g2.drawString("No career yet", x, textY);
                textY += 14;
            }

            if (p.babyCount > 0) {
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
                String babyStr = "🍼 × " + p.babyCount
                        + "  (-₱" + (card.BabyExpenseNode.EXPENSE_PER_BABY * p.babyCount) + "/exp)";
                // BABY_DISCOUNT perk colours it differently
                boolean discounted = p.hasPerk(card.PropertyPerk.BABY_DISCOUNT);
                g2.setColor(discounted ? new Color(120, 220, 180) : new Color(255, 160, 180));
                FontMetrics bfm = g2.getFontMetrics();
                while (bfm.stringWidth(babyStr) > CARD_W - (portW + 22) && babyStr.length() > 5)
                    babyStr = babyStr.substring(0, babyStr.length() - 1);
                g2.drawString(babyStr, x, textY);
            }

            java.util.List<card.Buff> buffs = p.getActiveBuffs();
            boolean hasBuff = !buffs.isEmpty();

            if (hasBuff) {
                int bx = BASE_X + CARD_W - 20;
                int by = y + 6;
                g2.setColor(new Color(200, 60, 60));
                g2.fillOval(bx, by, 14, 14);
                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 9f));
                String txt = String.valueOf(buffs.size());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt, bx + 7 - fm.stringWidth(txt) / 2, by + 10);
            }
            if (hasPerkBadge(p)) {
                int px = BASE_X + CARD_W - 36;
                int py = y + 6;
                g2.setColor(new Color(140, 60, 200));
                g2.fillOval(px, py, 14, 14);
                g2.setColor(new Color(220, 160, 255));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 9f));
                g2.drawString("P", px + 4, py + 10);
            }

            int tW = 22, tH = 14;
            int tX = BASE_X + CARD_W - tW - 6;
            int tY = y + CARD_H - tH - 6;
            buffDropdownToggleRects[i] = new Rectangle(tX, tY, tW, tH);

            g2.setColor(hasBuff ? new Color(70, 50, 20) : new Color(50, 50, 50, 120));
            g2.fillRoundRect(tX, tY, tW, tH, 6, 6);
            g2.setColor(hasBuff ? new Color(255, 210, 90) : new Color(150, 150, 150));
            String arrow = buffDropdownOpen[i] ? "▲" : "▼";
            FontMetrics afm = g2.getFontMetrics();
            g2.drawString(arrow,
                    tX + tW / 2 - afm.stringWidth(arrow) / 2,
                    tY + tH / 2 + afm.getAscent() / 2 - 2);

            buffDropdownOpen[i] = hasBuff && (i == gp.currentPlayerTurn);
            buffDropdownToggleRects[i] = new Rectangle(0, 0, 0, 0);

            playerStatus[i] = new Rectangle(BASE_X, y, CARD_W, CARD_H);
        }

        for (int i = 0; i < gp.numberOfPlayers; i++) {
            if (!buffDropdownOpen[i]) continue;
            java.util.List<card.Buff> buffs = gp.players[i].getActiveBuffs();
            if (buffs.isEmpty()) continue;
            int y = BASE_Y + i * (CARD_H + PAD);
            drawBuffDropdown(g2, buffs, BASE_X, y + CARD_H + 4, CARD_W);
        }
    }

    private boolean hasPerkBadge(entity.Player p) {
        for (card.PropertyCard pc : p.getProperties()) {
            if (pc.hasPerk()) return true;
        }
        return false;
    }

    public void getProfileImg(){
        playerProfile = new BufferedImage[GamePanel.TOTAL_CHARACTERS];
        for (int i = 0; i < GamePanel.TOTAL_CHARACTERS; i++){
            BufferedImage profile = uTool.getImage("/player/player" + (i + 1) + "Profile.png");
            playerProfile[i] = profile;
        }
    }

    public void showPerkNotice(entity.Player p, card.PropertyPerk perk) {
        System.out.println("[PERK] " + p.playerName + " – " + perk.displayName + " activated!");
    }

    public void showPaycheckNotice(entity.Player p, int amount) {
        System.out.println("[PAYCHECK] " + p.playerName + " received ₱" + amount
                + " salary (" + p.career.title + ")");}
    public void showBabyExpenseNotice(entity.Player p, int amount) {
        System.out.println("[BABY EXPENSE] " + p.playerName + " paid ₱" + amount
                + " for " + p.babyCount + " babies");}

    private void drawSettingsButton(Graphics2D g2) {
        int btnSize = 40;
        int margin = 12;
        int x = gp.screenWidth - btnSize - margin;
        int y = margin;
        settingsButtonRect = new Rectangle(x, y, btnSize, btnSize);

        // Rounded dark background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x, y, btnSize, btnSize, 10, 10);

        // gear symbol
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
        FontMetrics fm = g2.getFontMetrics();
        String gear = "\u2699";
        int gx = x + (btnSize - fm.stringWidth(gear)) / 2;
        int gy = y + (btnSize + fm.getAscent()) / 2 - 4;
        g2.drawString(gear, gx, gy);
    }

    private void drawSettingsPanel(Graphics2D g2) {
        // Semi-transparent full-screen dim
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Panel box
        int panelW = 420, panelH = 320;
        int panelX = (gp.screenWidth - panelW) / 2;
        int panelY = (gp.screenHeight - panelH) / 2;

        g2.setColor(new Color(30, 30, 40, 240));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(new Color(100, 100, 130));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // Title
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("Settings", panelX + 20, panelY + 40);

        // sliders
        String[] labels = {"Master Volume", "Music Volume", "SFX Volume"};
        float[] values  = {gp.sound.masterVolume, gp.sound.musicVolume, gp.sound.seVolume};
        int sliderX = panelX + 20;
        int sliderW = panelW - 40;

        for (int i = 0; i < 3; i++) {
            int rowY = panelY + 80 + i * 65;

            // Label
            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.setColor(new Color(200, 200, 220));
            g2.drawString(labels[i], sliderX, rowY);

            // Track
            int trackY = rowY + 12;
            g2.setColor(new Color(70, 70, 90));
            g2.fillRoundRect(sliderX, trackY, sliderW, 8, 8, 8);

            // Fill
            int fillW = (int)(sliderW * values[i]);
            g2.setColor(new Color(100, 160, 255));
            g2.fillRoundRect(sliderX, trackY, fillW, 8, 8, 8);

            // Thumb
            int thumbX = sliderX + fillW - 8;
            g2.setColor(Color.WHITE);
            g2.fillOval(thumbX, trackY - 4, 16, 16);

            // Store clickable area for mouse interaction
            settingsSliderRects[i] = new Rectangle(sliderX, trackY - 4, sliderW, 20);
        }

        // Exit button
        int exitBtnW = 120, exitBtnH = 36;
        int exitBtnX = panelX + (panelW - exitBtnW) / 2;
        int exitBtnY = panelY + panelH - 55;

        g2.setColor(new Color(200, 70, 70));
        g2.fillRoundRect(exitBtnX, exitBtnY, exitBtnW, exitBtnH, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 15));
        g2.drawString("Exit Game", exitBtnX + 22, exitBtnY + 24);

        exitGameButtonRect = new Rectangle(exitBtnX, exitBtnY, exitBtnW, exitBtnH);
    }

    private void drawStatPopups(Graphics2D g2) {
        if (gp.gameState == gp.buffState) return;   // hidden behind buff popup
        if (statPopups.isEmpty()) return;

        final int HUD_X  = 25;
        final int CARD_W = 180;
        final int popX   = HUD_X + CARD_W + 8;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));

        Iterator<StatPopup> it = statPopups.iterator();
        while (it.hasNext()) {
            StatPopup pop = it.next();
            pop.life--;
            if (pop.life <= 0) { it.remove(); continue; }

            int alpha = (int)(pop.alpha() * 215);
            if (alpha <= 0) continue;

            int popY = (int)(pop.spawnY - pop.drift());

            // Build stat lines
            List<String> lines   = new ArrayList<>();
            List<Color>  colours = new ArrayList<>();

            if (pop.dH != 0) {
                lines.add((pop.dH > 0 ? "+" : "") + pop.dH + " \uD83D\uDE0A");
                colours.add(pop.dH > 0 ? new Color(80, 210, 110, alpha) : new Color(225, 75, 75, alpha));
            }
            if (pop.dR != 0) {
                lines.add((pop.dR > 0 ? "+" : "") + pop.dR + " \u2605");
                colours.add(pop.dR > 0 ? new Color(245, 205, 40, alpha) : new Color(205, 95, 45, alpha));
            }
            if (pop.dM != 0) {
                lines.add((pop.dM > 0 ? "+" : "") + pop.dM + " \u20B1");
                colours.add(pop.dM > 0 ? new Color(75, 195, 245, alpha) : new Color(185, 70, 70, alpha));
            }

            if (lines.isEmpty()) { it.remove(); continue; }

            FontMetrics fm    = g2.getFontMetrics();
            int lineH         = fm.getHeight() + 2;
            int popH          = lines.size() * lineH + 10;
            int maxW          = 0;
            for (String l : lines) maxW = Math.max(maxW, fm.stringWidth(l));
            int popW = maxW + 16;

            // Shadow
            g2.setColor(new Color(0, 0, 0, alpha / 4));
            g2.fillRoundRect(popX + 2, popY + 2, popW, popH, 8, 8);
            // Background
            g2.setColor(new Color(15, 15, 25, (int)(alpha * 0.9f)));
            g2.fillRoundRect(popX, popY, popW, popH, 8, 8);
            // Border
            g2.setColor(new Color(200, 200, 225, alpha / 4));
            g2.setStroke(new BasicStroke(0.8f));
            g2.drawRoundRect(popX, popY, popW, popH, 8, 8);
            g2.setStroke(new BasicStroke(1f));

            int lineY = popY + fm.getAscent() + 5;
            for (int j = 0; j < lines.size(); j++) {
                g2.setColor(colours.get(j));
                g2.drawString(lines.get(j), popX + 8, lineY);
                lineY += lineH;
            }
        }
    }
}