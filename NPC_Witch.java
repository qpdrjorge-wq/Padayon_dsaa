package entity;

import card.CurseCard.CurseEffect;
import main.GamePanel;

import java.awt.*;
import java.io.IOException;

public class NPC_Witch extends Entity {

    public NPC_Witch(GamePanel gp) {
        super(gp);

        direction = "idle";
        speed = 1;

        solidArea = new Rectangle(3, 34, 28, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getNPCImage();
        setDialogue();
    }

    public void getNPCImage() {
        try {
            label = "Witch";
            // Ensure you have a witch.png in your resources
            idle = uTool.playerAnimation("/npc/witch.png", playerFrameWidth, npcFrameHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        direction = "idle";
        moving = false;
    }

    public void setDialogue() {

        DialogueSequence offerCurse = new DialogueSequence("offerCurse",
                new DialogueLine("Hehehe... I sense malice in your heart, traveler."),
                new DialogueLine(
                        "Would you like to cast misfortune upon your rivals? Choose your poison.",
                        new String[]{ "Steal ₱1000", "Block 1 Turn", "Emotional Damage", "9 spaces back", "Tarnish reputation", "No thanks" },
                        new int[]   { -1, -1, -1, -1, -1, -1 } // -1 ends the dialogue after choice
                ).withCurseReward(
                        new CurseEffect[] {
                                CurseEffect.STEAL_MONEY,
                                CurseEffect.BLOCK_TURNS,
                                CurseEffect.DECREASE_HAPPINESS,
                                CurseEffect.KNOCKBACK,
                                CurseEffect.DECREASE_REPUTATION,
                                null
                        },
                        new int[] { 1000, 1, 30, 9, 25,0 } // The magnitude/values for each curse
                )
        );

        dialogueVariants = new DialogueSequence[]{ offerCurse };
    }

    @Override
    public void speak() {
        super.speak();
    }
}