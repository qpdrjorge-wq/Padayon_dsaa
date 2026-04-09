package main;

import entity.*;
import object.House;
import object.LumangBahay;
import object.OBJ_Key;
import object.Store;

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
    }

    public void setNPCChapter2(){

        for(int i = 0; i < gp.npc.length; i++){
            gp.npc[i] = null;
        }

        gp.npc[0] = new NPC_Dealer(gp);
        gp.npc[0].worldX = 9 * gp.tileSize;
        gp.npc[0].worldY = 13 * gp.tileSize;
    }

    public void setNPCChapter3(){

        for(int i = 0; i < gp.npc.length; i++){
            gp.npc[i] = null;
        }

        gp.npc[0] = new NPC_Chismosa(gp);
        gp.npc[0].worldX = 18 * gp.tileSize;
        gp.npc[0].worldY = 35 * gp.tileSize;

    }

    public void setWheelForChapter3(){
        int col = 85;
        int row = 36;

        gp.wheel.x = col * gp.tileSize;
        gp.wheel.y = row * gp.tileSize;

        gp.obj[0].worldX = col * gp.tileSize;
        gp.obj[0].worldY = row * gp.tileSize;
    }


//    public void setNPCChapter4(){
//
//        for(int i = 0; i < gp.npc.length; i++){
//            gp.npc[i] = null;
//        }
//
//        gp.npc[0] = new NPC_Eyes(gp);
//        gp.npc[0].worldX = 56 * gp.tileSize;
//        gp.npc[0].worldY = 8 * gp.tileSize;
//    }
//
//    public void setWheelForChapter4(){
//        int col = 85;
//        int row = 36;
//
//        gp.wheel.x = col * gp.tileSize;
//        gp.wheel.y = row * gp.tileSize;
//
//        gp.obj[0].worldX = col * gp.tileSize;
//        gp.obj[0].worldY = row * gp.tileSize;
//    }

}