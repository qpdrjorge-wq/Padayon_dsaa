package entity;

import card.Buff;
import main.GamePanel;
import main.Sound;

import java.awt.*;
import java.io.IOException;

public class NPC_Girlie extends Entity {

    public NPC_Girlie(GamePanel gp) {
        super(gp);

        direction = "idle";
        speed     = 1;

        solidArea         = new Rectangle(3, 34, 28, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getNPCImage();
        setDialogue();
    }

    public void getNPCImage() {
        try {
            label = "Girlie";
            idle  = uTool.playerAnimation("/npc/NPC_Girlie.png", playerFrameWidth, npcFrameHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        direction = "idle";
        moving    = false;
    }

    public void setDialogue() {

        DialogueSequence scold = new DialogueSequence("scold",
                new DialogueLine("Goodness gracious"),
                new DialogueLine("Ikaw you nag-shoplift."),
                new DialogueLine("..."),
                new DialogueLine("You were the one caught stealing on CCTV!"),
                new DialogueLine("..."),
                new DialogueLine(" Homayghad, no way, I cannot take this anymore, no way!"),

                new DialogueLine(
                        "What is the next line?",
                        new String[]{
                                "My twin brother is a criminal!",
                                "Bakit kasalanan ko?"
                        },
                        new int[]{ -1, -1 }, // both end dialogue
                        new Buff[]{
                                new Buff(40, 30, 0, 0,
                                        "Correct! \n +40 happiness, +30 reputation."),
                                new Buff(-20, -30, 0, 0,
                                        "I think someone owns that dialogue..\n-20 happiness, -30 reputation.")
                        }
                )



        );

        dialogueVariants = new DialogueSequence[]{ scold }; // index 0
    }

    @Override
    public void assignVariant(int playerIndex) {
        if (dialogueVariants == null || dialogueVariants.length == 0) return;

        // Always play the single scold sequence regardless of which player lands here
        currentVariantIndex  = 0;
        currentSequenceIndex = 0;
        currentLineIndex     = 0;

        choiceTimerActive  = false;
        choiceTimeoutTimer = 0;
        choiceSecondsLeft  = 0;
        choiceTimeoutFired = false;
    }

    @Override
    public void speak() {
        if (currentSequenceIndex == 0 && currentLineIndex == 0) {
            gp.sound.playSE(Sound.SE_GIRLIE);
        }

        super.speak();
    }
}