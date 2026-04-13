package entity;

import main.GamePanel;
import main.Sound;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC_Jumpscare extends Entity {

    /** 60 frames = 1 second at 60 fps */
    public static final int JUMPSCARE_DURATION = 60;

    private static final float FLASH_ALPHA = 0.55f;

    public int jumpscareTimer = 0;
    public BufferedImage jumpscareImage;
    private boolean soundPlayed = false;

    public NPC_Jumpscare(GamePanel gp) {
        super(gp);

        direction = "idle";
        speed     = 0;

        solidArea          = new Rectangle(3, 34, 28, 28);
        solidAreaDefaultX  = solidArea.x;
        solidAreaDefaultY  = solidArea.y;

        getNPCImage();
        setDialogue();
    }

    public void getNPCImage() {
        try {
            label = "???";

            idle = uTool.playerAnimation("/npc/dealer.png",
                    playerFrameWidth, npcFrameHeight);

            jumpscareImage = ImageIO.read(
                    getClass().getResourceAsStream("/npc/jumpscare_face.png"));

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("NPC_Jumpscare: could not load image – " + e.getMessage());
        }
    }

    public void setDialogue() {

        // Sequence 0: opener — lulls the player in with humor
        DialogueSequence opener = new DialogueSequence("opener",
                new DialogueLine("Psst. Hoy. Ikaw. Oo, ikaw."),
                new DialogueLine("Bagong empleyado ka no? Halata."),
                new DialogueLine("May sasabihin lang ako sa'yo..."),
                new DialogueLine("Choice mo:",
                        new String[]{"Ano yun?", "Ayoko, busy ako."},
                        new int[]{1, 2}
                )
        );

        // Sequence 1: player listens → gets the scare
        DialogueSequence listens = new DialogueSequence("listens",
                new DialogueLine("Okay lang naman dito sa opisina..."),
                new DialogueLine("...pero 'wag kang mag-overtime ng mag-isa."),
                new DialogueLine("Yung dating empleyado dito... 'di na nakauwi."),
                new DialogueLine("Tinanong ko siya bakit. Sabi niya..."),
                new DialogueLine("*stopped*")
        );

        // Sequence 2: player tries to leave → scare finds them anyway
        DialogueSequence leaves = new DialogueSequence("leaves",
                new DialogueLine("Sige, sige. Mag-ingat ka sa CR ha."),
                new DialogueLine("Lalo na yung 3rd floor."),
                new DialogueLine("..."),
                new DialogueLine("*Lumingon ka. Wala na siya. Kailan pa siya umalis?*")
        );

        dialogueVariants = new DialogueSequence[]{
                opener,   // index 0
                listens,  // index 1
                leaves    // index 2
        };
    }

    @Override
    public void setAction() {
        direction = "idle";
        moving    = false;
    }

    @Override
    public void speak() {
        triggerJumpscare();
    }

    public void triggerJumpscare() {
        jumpscareTimer = JUMPSCARE_DURATION;
        soundPlayed    = false;
        gp.gameState   = gp.jumpscareState;
    }

    public void drawJumpscare(Graphics2D g2) {

        if (jumpscareTimer <= 0) return;

        int sw = gp.screenWidth;
        int sh = gp.screenHeight;

        // Red flash that fades out over the 1 second
        float flashFade = (float) jumpscareTimer / JUMPSCARE_DURATION;
        g2.setColor(new Color(1f, 0f, 0f, FLASH_ALPHA * flashFade));
        g2.fillRect(0, 0, sw, sh);

        // Jumpscare image — fully visible at start, fades with the flash
        if (jumpscareImage != null) {
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, Math.min(1f, flashFade * 2f)));
            g2.drawImage(jumpscareImage, 0, 0, sw, sh, null);
            g2.setComposite(old);
        }

        // Play jumpscare_scream.wav once (index 9 — add to Sound.java, see below)
        if (!soundPlayed) {
            gp.sound.playSE(Sound.SE_JUMPSCARE);
            soundPlayed = true;
        }

        jumpscareTimer--;
        if (jumpscareTimer <= 0) {
            gp.gameState = gp.playState;
        }

    }
    public boolean isOnLastLine() {
        if (dialogueVariants == null || currentSequenceIndex < 0) return false;
        DialogueSequence seq = dialogueVariants[currentSequenceIndex];
        return seq != null && currentLineIndex >= seq.lines.length - 1;
    }
}
