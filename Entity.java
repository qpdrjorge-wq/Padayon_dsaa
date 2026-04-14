package entity;

import card.Buff;
import card.CurseCard;
import main.GamePanel;
import main.Sound;
import main.UtilityTool;
import object.Wheel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity { //parent class for players, npc, and monsters

    GamePanel gp;
    UtilityTool uTool = new UtilityTool();
    public int worldX, worldY; //position
    public int speed;
    public String label;

    //describes an image with an accessible buffer of image data for animation
    public BufferedImage[] leftFrames, rightFrames, upFrames, downFrames, idle;
    boolean moving;

    //player animation per frame
    int playerFrameWidth = 16;
    int playerFrameHeight = 23;
    int npcFrameHeight = 32;
    int animalFrameWidth = 32;

    //animation counters
    int frameIndex = 0;
    int animationCounter = 0;
    int animationSpeed = 8;

    public String direction;

    //collisions
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public int solidAreaDefaultX, solidAreaDefaultY;
    public int currentPathIndex = 0;
    public int drawWidth = 16 * 3;
    public int drawHeight = 32 * 2;

    //    Dialogues
    protected DialogueSequence[] dialogueVariants;
    protected int currentVariantIndex = 0;
    protected int currentLineIndex = 0;
    protected int currentSequenceIndex = 0;
    public int pendingChoiceResult = -1;
    public int choiceTimeoutTimer = 0;
    public boolean choiceTimerActive = false;
    public int choiceSecondsLeft = 0;
    public boolean choiceTimeoutFired = false;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){}

    public void speak(){

        if(dialogueVariants == null || dialogueVariants.length == 0){
            endDialogue();
            return;
        }

        DialogueSequence seq = dialogueVariants[currentSequenceIndex];

        if(currentLineIndex >= seq.lines.length){
            endDialogue();
            return;
        }

        DialogueLine line = seq.lines[currentLineIndex];

        //item request handling
        if (line.hasItemRequest() && pendingChoiceResult == -1){

            boolean playerHasItem = false;
            String itemName = line.requestedPropertyName;

            if (gp.currentPlayer != null){
                for (card.PropertyCard pc : gp.currentPlayer.getProperties()){
                    if (pc.propertyName.equals(itemName)){
                        playerHasItem = true;
                        break;
                    }
                }
            }

            String[] displayChoices;
            int[] displayNextSeq;

            if (playerHasItem && line.hasChoices()){

                displayChoices = new String[line.choices.length + 1];
                displayNextSeq = new int[line.nextSequenceIndex.length + 1];
                System.arraycopy(line.choices, 0, displayChoices, 0, line.choices.length);
                System.arraycopy(line.nextSequenceIndex, 0, displayNextSeq, 0, line.nextSequenceIndex.length);
                displayChoices[line.choices.length] = "Give " + itemName;
                displayNextSeq[line.choices.length] = line.giveItemSequenceIndex;
            } else if (playerHasItem){
                displayChoices = new String[] {"Keept it", "Give " + itemName};
                displayNextSeq = new int[] {-1, line.giveItemSequenceIndex};
            } else if(line.hasChoices()){
                displayChoices = line.choices;
                displayNextSeq = line.nextSequenceIndex;
            } else {
                gp.ui.currentDialogue = line.text;
                currentLineIndex++;
                return;
            }

            if (line.timeoutSeconds > 0 && !choiceTimerActive){
                choiceTimerActive = true;
                choiceTimeoutTimer = line.timeoutSeconds * 60;
                choiceSecondsLeft = line.timeoutSeconds;
            }

            gp.ui.currentDialogue = line.text;
            gp.ui.showDialogueChoices(displayChoices);
            return;
        }

        if (line.hasItemRequest() && pendingChoiceResult != -1){

            choiceTimerActive = false;
            choiceTimeoutTimer = 0;
            choiceSecondsLeft = 0;

            int chosen = pendingChoiceResult;
            pendingChoiceResult = -1;
            gp.ui.hideDialogueChoices();

            boolean playerHasItem = false;
            if (gp.currentPlayer != null){
                for (card.PropertyCard pc : gp.currentPlayer.getProperties()){
                    if (pc.propertyName.equals(line.requestedPropertyName)){
                        playerHasItem = true;
                        break;
                    }
                }
            }

            int giveSlot = (line.hasChoices() ? line.choices.length : 1);

            if (playerHasItem && chosen == giveSlot){
                handleItemGive(line);
                return;
            }

            card.Buff choiceBuff = line.getBuffForChoice(chosen);
            if (choiceBuff != null && gp.currentPlayer != null){
                applyBuffToCurrentPlayer(choiceBuff);
            }

            int nextSeq = line.hasChoices() ? line.nextSequenceIndex[chosen] : -1;

            if(nextSeq == -1){
                endDialogue();
            } else {
                currentSequenceIndex = nextSeq;
                currentLineIndex = 0;
            }
            return;
        }

        //if the line has choices and player has not chosen yet
        if(line.hasChoices() && pendingChoiceResult == -1){

            //handles timeout fire
            if (choiceTimeoutFired) {
                choiceTimeoutFired = false;
                choiceTimerActive = false;
                choiceTimeoutTimer = 0;
                choiceSecondsLeft = 0;
                gp.ui.hideDialogueChoices();

                int nextSeq = line.timeoutSequenceIndex;
                if (nextSeq == -1) {
                    endDialogue();
                } else {
                    currentSequenceIndex = nextSeq;
                    currentLineIndex = 0;
                }
                return;
            }

            // Arm the timer once when this choice line is first shown
            if (line.timeoutSeconds > 0 && !choiceTimerActive) {
                choiceTimerActive = true;
                choiceTimeoutTimer = line.timeoutSeconds * 60;
                choiceSecondsLeft = line.timeoutSeconds;
            }

            gp.ui.currentDialogue = line.text;
            gp.ui.showDialogueChoices(line.choices);
            return;
        }

        // if line had choices and player chose
        if(line.hasChoices() && pendingChoiceResult != -1){

            choiceTimerActive = false;
            choiceTimeoutTimer = 0;
            choiceSecondsLeft = 0;

            int chosen = pendingChoiceResult;
            pendingChoiceResult = -1;
            gp.ui.hideDialogueChoices();

            // --- CUSTOM WITCH LOGIC ---
            if (this.label != null && this.label.equals("Witch")) {
                card.CurseCard.CurseEffect choiceCurse = null;
                int curseValue = 0;

                if (chosen == 0) { choiceCurse = card.CurseCard.CurseEffect.STEAL_MONEY; curseValue = 1000; }
                else if (chosen == 1) { choiceCurse = card.CurseCard.CurseEffect.BLOCK_TURNS; curseValue = 1; }
                else if (chosen == 2) { choiceCurse = card.CurseCard.CurseEffect.DECREASE_HAPPINESS; curseValue = 30; }
                else if (chosen == 3) { choiceCurse = CurseCard.CurseEffect.KNOCKBACK; curseValue = 9; }
                else if (chosen == 4) { choiceCurse = CurseCard.CurseEffect.DECREASE_REPUTATION; curseValue = 25; }

                if (choiceCurse != null && gp.currentPlayer != null) {
                    gp.currentCurseCard = new card.CurseCard(
                            gp,
                            "Witch's Hex",
                            "A dark curse chosen by " + gp.currentPlayer.playerName + "!",
                            choiceCurse,
                            curseValue
                    );
                    gp.currentCurseCard.spawnCard();
                    gp.currentCurseCard.phase = card.CurseCard.CursePhase.PICKING_TARGET;
                    gp.cardTriggeredThisTurn = true;

                    endDialogue();
                    return;
                }
            }
            // --- END CUSTOM WITCH LOGIC ---

            Buff choiceBuff = line.getBuffForChoice(chosen);
            if (choiceBuff != null && gp.currentPlayer != null) {
                applyBuffToCurrentPlayer(choiceBuff);
            }

            int nextSeq = line.nextSequenceIndex[chosen];
            if(nextSeq == -1){
                endDialogue();
            } else {
                currentSequenceIndex = nextSeq;
                currentLineIndex = 0;
            }
            return;
        }

        gp.ui.currentDialogue = line.text;
        currentLineIndex++;
    };

    private void applyBuffToCurrentPlayer(Buff buff) {
        entity.Player player = gp.currentPlayer;
        if (player == null) return;

        player.applyExternalBuff(buff);

        // Show the buff popup so the player knows what happened
        if (buff.buffText != null && !buff.buffText.isBlank()) {
            String durationLabel = buff.isChapterLong()
                    ? "Duration: Rest of this chapter"
                    : "Duration: " + buff.duration + " turn" + (buff.duration == 1 ? "" : "s");

            gp.ui.currentBuffText = buff.buffText + "\n\n" + durationLabel;
            gp.ui.buffActive      = true;
            gp.pendingCardAfterBuff = false;   // buff came from dialogue, not a board node
            gp.gameState = gp.buffState;
        }
    }

    protected void endDialogue() {

        gp.ui.currentNPCName = "";

        DialogueSequence finishedSeq = dialogueVariants[currentSequenceIndex];
        boolean shouldKnockback = finishedSeq.triggerKnockback;

        currentLineIndex = 0;
        currentSequenceIndex = currentVariantIndex;

        choiceTimerActive = false;
        choiceTimeoutTimer = 0;
        choiceSecondsLeft = 0;
        choiceTimeoutFired = false;

        gp.activeNPC = null;
        gp.gameState = gp.playState;

        if (shouldKnockback && gp.currentPlayer != null) {
            gp.currentPlayer.knockbackCount++;

            if(gp.currentPlayer.knockbackCount > Player.MAX_KNOCKBACKS){
                gp.currentPlayer.knockbackCount = 0;

                if(gp.currentPlayer.stepsRemaining > 0){
                    gp.currentPlayer.boardMoving = true;
                }
            } else {
                gp.currentPlayer.stepsRemaining = 0;
                gp.currentPlayer.boardMoving = false;
                gp.currentPlayer.startKnockback(4);
            }
        } else if (gp.currentPlayer.stepsRemaining > 0) {
            gp.currentPlayer.boardMoving = true;
        }
    }

    public void assignVariant(int playerIndex) {
        if (dialogueVariants == null || dialogueVariants.length == 0) return;
        currentVariantIndex = playerIndex % dialogueVariants.length;
        currentSequenceIndex = currentVariantIndex;
        currentLineIndex = 0;

        //reset the timer state to refresh the dialogue
        choiceTimerActive = false;
        choiceTimeoutTimer = 0;
        choiceSecondsLeft = 0;
        choiceTimeoutFired = false;
    }

    public void update(){
        setAction();
        collisionOn = false;
        gp.cChecker.checkerTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);

        if (!collisionOn){
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        animationCounter++;

        if(animationCounter > animationSpeed){
            frameIndex++;

            if (frameIndex >= 4) {
                frameIndex = 0;
            }

            animationCounter = 0;
        }
    }

    public void drawNpc(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - gp.cameraWorldX + gp.screenWidth / 2;
        int screenY = worldY - gp.cameraWorldY + gp.screenHeight / 2;

        if(screenX + gp.tileSize > 0 &&
                screenX - gp.tileSize < gp.screenWidth &&
                screenY + gp.tileSize > 0 &&
                screenY - gp.tileSize < gp.screenHeight) {

            switch (direction) {
                case "idle":
                    image = idle[frameIndex];
                    break;
            }

            g2.drawImage(image, screenX, screenY, drawWidth, drawHeight, null);
        }
    }

    public void tickChoiceTimer() {
        if (!choiceTimerActive) return;
        choiceTimeoutTimer--;
        choiceSecondsLeft = Math.max(0, choiceTimeoutTimer / 60);

        if (choiceTimeoutTimer <= 0) {
            choiceTimerActive = false;
            choiceSecondsLeft = 0;
            choiceTimeoutFired = true; // GamePanel will call speak() next frame
        }
    }

    private void handleItemGive(DialogueLine line) {

        Player player = gp.currentPlayer;
        if (player == null) { endDialogue(); return; }

        // 1. Find and remove the property from the player's inventory
        card.PropertyCard given = null;
        for (card.PropertyCard pc : player.getProperties()) {
            if (pc.propertyName.equals(line.requestedPropertyName)) {
                given = pc;
                break;
            }
        }
        if (given != null) {
            player.removeProperty(given);
        }

        // 2. Apply the reward (if any)
        card.ItemGiftReward reward = line.itemGiftReward;
        if (reward != null) {

            if (reward.isBuff()) {
                applyBuffToCurrentPlayer(reward.buff);       // uses existing helper

            } else if (reward.isProperty()) {
                card.PropertyCard gift = new card.PropertyCard(
                        gp,
                        reward.giftPropertyName,
                        reward.giftPropertyDesc,
                        reward.giftAssetValue,
                        reward.giftMonthlyIncome
                );
                player.addProperty(gift);

                // Briefly show a notification in the buff popup
                gp.ui.currentBuffText = "You received: " + reward.giftPropertyName
                        + "\n\nAsset Value: ₱" + reward.giftAssetValue
                        + "   Monthly Income: ₱" + reward.giftMonthlyIncome;
                gp.ui.buffActive       = true;
                gp.pendingCardAfterBuff = false;
                gp.gameState            = gp.buffState;

            } else {                                         // raw stats
                player.addBaseStats(reward.happiness, reward.reputation, reward.money);

                if (reward.happiness != 0 || reward.reputation != 0 || reward.money != 0) {
                    gp.ui.currentBuffText =
                            "The trade paid off!\n\n"
                                    + (reward.happiness  != 0 ? "Happiness: "  + sign(reward.happiness)  + "\n" : "")
                                    + (reward.reputation != 0 ? "Reputation: " + sign(reward.reputation) + "\n" : "")
                                    + (reward.money      != 0 ? "Money: ₱"     + reward.money             + "\n" : "");
                    gp.ui.buffActive       = true;
                    gp.pendingCardAfterBuff = false;
                    gp.gameState            = gp.buffState;
                }
            }
        }

        // 3. Jump to the give-item success sequence
        int nextSeq = line.giveItemSequenceIndex;
        if (nextSeq == -1) {
            endDialogue();
        } else {
            currentSequenceIndex = nextSeq;
            currentLineIndex     = 0;
        }
    }

    private static String sign(int v) { return v >= 0 ? "+" + v : String.valueOf(v); }
}
