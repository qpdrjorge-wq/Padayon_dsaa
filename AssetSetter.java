package main;

import entity.*;
import object.OBJ_Key;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    //the wheel outer
    public void setObject(){
        gp.obj[0] = new OBJ_Key();
        gp.obj[0].worldX = 38 * gp.tileSize;
        gp.obj[0].worldY = 14 * gp.tileSize;
        gp.obj[0].size = gp.tileSize * 6;
        gp.obj[0].collision = true;
    }

    public void setNPC(){
        gp.npc[0] = new NPC_Father(gp);
        gp.npc[0].worldX = 30 * gp.tileSize;
        gp.npc[0].worldY = 19 * gp.tileSize;

        gp.npc[1] = new NPC_Mother(gp);
        gp.npc[1].worldX = 18 * gp.tileSize;
        gp.npc[1].worldY = 35 * gp.tileSize;

        gp.npc[2] = new NPC_Cat(gp);
        gp.npc[2].worldX = 39 * gp.tileSize;
        gp.npc[2].worldY = 22 * gp.tileSize;

        gp.npc[3] = new NPC_Dealer(gp);
        gp.npc[3].worldX = 28 * gp.tileSize;
        gp.npc[3].worldY = 33 * gp.tileSize;
    }

    public void setNPCChapter2(){

        for(int i = 0; i < gp.npc.length; i++){
            gp.npc[i] = null;
        }

        gp.npc[0] = new NPC_Dealer(gp);
        gp.npc[0].worldX = 6 * gp.tileSize;
        gp.npc[0].worldY = 13 * gp.tileSize;
    }

    public void setNPCChapter3(){

        for(int i = 0; i < gp.npc.length; i++){
            gp.npc[i] = null;
        }

        gp.npc[0] = new NPC_Chismosa(gp);
        gp.npc[0].worldX = 21 * gp.tileSize;
        gp.npc[0].worldY = 38 * gp.tileSize;

        gp.npc[1] = new NPC_Jumpscare(gp);
        gp.npc[1].worldX = 24 * gp.tileSize;   // ← pick your tile coordinates
        gp.npc[1].worldY = 31 * gp.tileSize;

        gp.npc[2] = new NPC_Jo(gp);
        gp.npc[2].worldX = 56 * gp.tileSize;   // ← pick your tile coordinates
        gp.npc[2].worldY = 20 * gp.tileSize;

        gp.npc[3] = new NPC_Girlie(gp);
        gp.npc[3].worldX = 51 * gp.tileSize;
        gp.npc[3].worldY = 29 * gp.tileSize;
    }

    public void setWheelForChapter3(){
        int col = 72;
        int row = 5;

        gp.wheel.x = col * gp.tileSize;
        gp.wheel.y = row * gp.tileSize;

        gp.obj[0].worldX = col * gp.tileSize;
        gp.obj[0].worldY = row * gp.tileSize;
    }

    public void setNPCChapter4(){

        for(int i = 0; i < gp.npc.length; i++){
            gp.npc[i] = null;
        }

        gp.npc[0] = new NPC_Eyes(gp);
        gp.npc[0].worldX = 59 * gp.tileSize;
        gp.npc[0].worldY = 8 * gp.tileSize;

        gp.npc[1] = new NPC_Eyes2(gp);
        gp.npc[1].worldX = 35 * gp.tileSize;
        gp.npc[1].worldY = 8 * gp.tileSize;
    }

    public void setWheelForChapter4(){
        int col = 85;
        int row = 36;

        gp.wheel.x = col * gp.tileSize;
        gp.wheel.y = row * gp.tileSize;

        gp.obj[0].worldX = col * gp.tileSize;
        gp.obj[0].worldY = row * gp.tileSize;
    }

}
