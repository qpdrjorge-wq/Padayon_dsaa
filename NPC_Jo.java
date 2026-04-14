package entity;

import main.GamePanel;
import main.Sound;
import card.Buff;

import java.awt.*;
import java.io.IOException;

public class NPC_Jo extends Entity {

    public NPC_Jo(GamePanel gp) {
        super(gp);

        direction = "idle";
        speed     = 1;

        solidArea          = new Rectangle(3, 34, 28, 28);
        solidAreaDefaultX  = solidArea.x;
        solidAreaDefaultY  = solidArea.y;

        getNPCImage();
        setDialogue();
    }

    public void getNPCImage() {
        try {
            label = "Narrator";
            idle  = uTool.playerAnimation("/npc/NPC_Jo.png", playerFrameWidth, npcFrameHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        direction = "idle";
        moving    = false;
    }

    public void setDialogue() {

        // ── Variant A ─────────────────────────────────────────────────────────
        DialogueSequence variantA = new DialogueSequence("variantA",
                new DialogueLine("You are rushing inside a jeepney with only 10 minutes left \nbefore the most important exam of your life."),
                new DialogueLine("Suddenly, you look out the window and spot your drunk \npartner falling flat on the sidewalk."),
                new DialogueLine(
                        "What do you do?",
                        new String[]{"Help them", "Dedmahin"},
                        new int[]{2, 3}
                )
        );

        // A → Help them
        DialogueSequence helpResponse = new DialogueSequence("helpResponse",
                new DialogueLine("Martyr ka teh?"),
                new DialogueLine("You saved them from the gutter, but you completely missed your exam."),
                new DialogueLine("Love wins... I guess?")
        );

        // A → Dedmahin
        DialogueSequence dedmaResponse = new DialogueSequence("dedmaResponse",
                new DialogueLine("Oops, access Denied."),
                new DialogueLine("Remember when you chose 'Love Life' over 'Career' at the earlier crossroads?"),
                new DialogueLine("You are bound to your foolish decisions!")
        );

        // ── Variant B ─────────────────────────────────────────────────────────
        DialogueSequence variantB = new DialogueSequence("variantB",
                new DialogueLine("So, your long-term partner—who has been waiting for their indie band's \n'big break' for 5 years—"),
                new DialogueLine("just used your Meralco budget to buy a vintage guitar pedal."),
                new DialogueLine("The house is completely dark, and it is currently pouring rain outside."),
                new DialogueLine(
                        "What's your script?",
                        new String[]{"PAGOD NA PAGOD NA AKO!", "Forgive them."},
                        new int[]{5, 6}
                )
        );

        // B → PAGOD NA (audio only, no buff)
        DialogueSequence pagodResponse = new DialogueSequence("pagodResponse",
                new DialogueLine("..."),
                new DialogueLine("Some things are better left unsaid.")
        );

        // B → Forgive them
        DialogueSequence forgiveResponse = new DialogueSequence("forgiveResponse",
                new DialogueLine("You really are a martyr aren't you?"),
                new DialogueLine("You paid the bill so your partner can continue with their \"passion\"."),
                new DialogueLine("Your relationship survives, but your wallet and sanity are barely hanging on.")
        );

        dialogueVariants = new DialogueSequence[]{
                variantA,       // index 0
                variantB,       // index 1
                helpResponse,   // index 2
                dedmaResponse,  // index 3
                variantB,       // index 4 (placeholder, assignVariant picks 0 or 1)
                pagodResponse,  // index 5
                forgiveResponse // index 6
        };
    }

    @Override
    public void assignVariant(int playerIndex) {
        if (dialogueVariants == null || dialogueVariants.length == 0) return;

        // Alternate between variantA and variantB per player
        int startSequence = playerIndex % 2; // 0 = variantA, 1 = variantB

        currentVariantIndex  = startSequence;
        currentSequenceIndex = startSequence;
        currentLineIndex     = 0;

        choiceTimerActive  = false;
        choiceTimeoutTimer = 0;
        choiceSecondsLeft  = 0;
        choiceTimeoutFired = false;
    }

    @Override
    public void speak() {
        // Play audio when entering specific response sequences
        if (currentSequenceIndex == 2 && currentLineIndex == 0) {
            // Help them → rant_jo.wav (index 10)
            gp.sound.playSE(Sound.SE_RANT_JO);
        } else if (currentSequenceIndex == 5 && currentLineIndex == 0) {
            // PAGOD NA → pagod_na.wav (index 11)
            gp.sound.playSE(Sound.SE_PAGOD_NA);
        } else if (currentSequenceIndex == 6 && currentLineIndex == 0) {
            // Forgive them → puro_passion.wav (index 12)
            gp.sound.playSE(Sound.SE_PURO_PASSION);
        }

        super.speak();
    }
}