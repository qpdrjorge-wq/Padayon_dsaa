package entity;

import main.GamePanel;

import java.awt.*;
import java.io.IOException;

public class NPC_Chismosa  extends Entity{

    public NPC_Chismosa(GamePanel gp){
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
        try{
            label = "Aling Nena";
            idle = uTool.playerAnimation("/npc/chismosa.png", playerFrameWidth, npcFrameHeight);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setAction(){
        direction = "idle";
        moving = false;
    }
    public void setDialogue() {
        // Sequence 0: Variant A (opener) — leads to a choice
        DialogueSequence variantA = new DialogueSequence("variantA",
                new DialogueLine("Huyy, kamusta ka? balita ko nag-ibang bansa mama mo ahh.."),
                new DialogueLine("baka naman may extra ka petty cash, bayaran ko nalang sa katapusan.."),
                new DialogueLine(
                        "What is your response?",
                        new String[]{"Yung utang mo nga 'di mo pa rin nababayaran sakin e!!", "Ay mare sorry wala kong extra ngayon e"},
                        new int[]{2, 3}  // index 2 = angryResponse, index 3 = goodResponse
                )
        );

        // Sequence 1: Variant B (different opener for player 2+)
        DialogueSequence variantB = new DialogueSequence("variantB",
                new DialogueLine("May bago ka na raw trabahon ah? Pautang naman 500 bayaran kita sa katapusan..."),
                new DialogueLine("mamamo."),
                new DialogueLine(
                        "What do you think",
                        new String[]{"Kapal naman ho ng batok niyong mangutang", "Wala akong trabaho"},
                        new int[]{2, 3}
                )
        );

        // Sequence 2: Angry response branch (shared)
        DialogueSequence angryResponse = new DialogueSequence("angryResponse",
                new DialogueLine("Yabang mo"),
                new DialogueLine("Sayo nayang mga de-susing cornbeef niyo")
        );

        // Sequence 3: Sad response branch (shared)
        DialogueSequence goodResponse = new DialogueSequence("goodResponse",
                new DialogueLine("Ay, ganon, kaya pala di mo mapaayos ung bubong niyo"),
                new DialogueLine("hatdohg..")
        );

        dialogueVariants = new DialogueSequence[]{
                variantA,       // index 0
                variantB,       // index 1
                angryResponse,  // index 2
                goodResponse     // index 3
        };
    }

    public void speak(){
        super.speak();
    }
}
