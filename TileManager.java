package tile;

import entity.Player;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][]; //matrix for the map text
    public int[][] groundMap;
    public boolean[][] collisionMap;
    UtilityTool uTool = new UtilityTool();
    BufferedImage grassBackground;
    BufferedImage decorBackground;
    public BufferedImage decorForeground;
    int tiles = 200; // num of total tiles present in the map

    //constructor
    public TileManager(GamePanel gp){
        this.gp = gp;

        tile = new Tile[tiles];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        groundMap = new int[gp.maxWorldCol][gp.maxWorldRow];
        collisionMap = new boolean[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        createGrassBackground();
        loadMap("/maps/map01.txt", groundMap);
        try{
            decorBackground = uTool.getImage("/maps/mapch1.png");
        } catch (Exception e){
            e.printStackTrace();
        }
        loadCollisionMap("/maps/mapCollision01.txt", collisionMap);
    }

    //get the tile images
    public void getTileImage(){

        try{

            setup(0, "/tiles/grass.png", false);
            setup(1, "/tiles/actionTiles/yellow_tile-1.png", false);
            setup(2, "/tiles/actionTiles/yellow_tile-2.png", false);
            setup(3, "/tiles/actionTiles/yellow_tile-3.png", false);
            setup(4, "/tiles/actionTiles/yellow_tile-4.png", false);
            setup(5, "/tiles/actionTiles/yellow_tile-5.png", false);
            setup(6, "/tiles/actionTiles/yellow_tile-6.png", false);
            setup(7, "/tiles/actionTiles/yellow_tile-7.png", false);
            setup(8, "/tiles/actionTiles/yellow_tile-8.png", false);
            setup(9, "/tiles/actionTiles/yellow_tile-9.png", false);
            setup(10, "/tiles/actionTiles/yellow_tile-10.png", false);
            setup(11, "/tiles/actionTiles/yellow_tile-11.png", false);
            setup(12, "/tiles/actionTiles/yellow_tile-12.png", false);
            setup(13, "/tiles/actionTiles/yellow_tile-13.png", false);

            recolor(14,"/tiles/actionTiles/yellow_tile-1.png", Color.BLUE, false);//1
            recolor(15,"/tiles/actionTiles/yellow_tile-2.png", Color.BLUE, false);//2
            recolor(16,"/tiles/actionTiles/yellow_tile-3.png", Color.BLUE, false);//3
            recolor(17,"/tiles/actionTiles/yellow_tile-4.png", Color.BLUE, false);//4
            recolor(18,"/tiles/actionTiles/yellow_tile-5.png", Color.BLUE, false);//5
            recolor(19,"/tiles/actionTiles/yellow_tile-6.png", Color.BLUE, false);//6
            recolor(20,"/tiles/actionTiles/yellow_tile-7.png", Color.BLUE, false);//7
            recolor(21,"/tiles/actionTiles/yellow_tile-8.png", Color.BLUE, false);//8
            recolor(22,"/tiles/actionTiles/yellow_tile-9.png", Color.BLUE, false);//9
            recolor(23,"/tiles/actionTiles/yellow_tile-10.png", Color.BLUE, false);//10
            recolor(24,"/tiles/actionTiles/yellow_tile-11.png", Color.BLUE, false);//11
            recolor(25,"/tiles/actionTiles/yellow_tile-12.png", Color.BLUE, false);//12
            recolor(26, "/tiles/actionTiles/yellow_tile-13.png", Color.BLUE,false);//13
            //Red
            recolor(27,"/tiles/actionTiles/yellow_tile-1.png", Color.RED, false);//1
            recolor(28,"/tiles/actionTiles/yellow_tile-2.png", Color.RED, false);//2
            recolor(29,"/tiles/actionTiles/yellow_tile-3.png", Color.RED, false);//3
            recolor(30,"/tiles/actionTiles/yellow_tile-4.png", Color.RED, false);//4
            recolor(31,"/tiles/actionTiles/yellow_tile-5.png", Color.RED, false);//5
            recolor(32,"/tiles/actionTiles/yellow_tile-6.png", Color.RED, false);//6
            recolor(33,"/tiles/actionTiles/yellow_tile-7.png", Color.RED, false);//7
            recolor(34,"/tiles/actionTiles/yellow_tile-8.png", Color.RED, false);//8
            recolor(35,"/tiles/actionTiles/yellow_tile-9.png", Color.RED, false);//9
            recolor(36,"/tiles/actionTiles/yellow_tile-10.png", Color.RED, false);//10
            recolor(37,"/tiles/actionTiles/yellow_tile-11.png", Color.RED, false);//11
            recolor(38,"/tiles/actionTiles/yellow_tile-12.png", Color.RED, false);//12
            recolor(39, "/tiles/actionTiles/yellow_tile-13.png", Color.RED,false);//13
            //Pink
            recolor(40,"/tiles/actionTiles/yellow_tile-1.png", Color.MAGENTA, false);//1
            recolor(41,"/tiles/actionTiles/yellow_tile-2.png", Color.MAGENTA, false);//2
            recolor(42,"/tiles/actionTiles/yellow_tile-3.png", Color.MAGENTA, false);//3
            recolor(43,"/tiles/actionTiles/yellow_tile-4.png", Color.MAGENTA, false);//4
            recolor(44,"/tiles/actionTiles/yellow_tile-5.png", Color.MAGENTA, false);//5
            recolor(45,"/tiles/actionTiles/yellow_tile-6.png", Color.MAGENTA, false);//6
            recolor(46,"/tiles/actionTiles/yellow_tile-7.png", Color.MAGENTA, false);//7
            recolor(47,"/tiles/actionTiles/yellow_tile-8.png", Color.MAGENTA, false);//8
            recolor(48,"/tiles/actionTiles/yellow_tile-9.png", Color.MAGENTA, false);//9
            recolor(49,"/tiles/actionTiles/yellow_tile-10.png", Color.MAGENTA, false);//10
            recolor(50,"/tiles/actionTiles/yellow_tile-11.png", Color.MAGENTA, false);//11
            recolor(51,"/tiles/actionTiles/yellow_tile-12.png", Color.MAGENTA, false);//12
            recolor(52, "/tiles/actionTiles/yellow_tile-13.png", Color.MAGENTA,false);//13
            //Purple
            recolor(53,"/tiles/actionTiles/yellow_tile-1.png", Color.GREEN, false);//1
            recolor(54,"/tiles/actionTiles/yellow_tile-2.png", Color.GREEN, false);//2
            recolor(55,"/tiles/actionTiles/yellow_tile-3.png", Color.GREEN, false);//3
            recolor(56,"/tiles/actionTiles/yellow_tile-4.png", Color.GREEN, false);//4
            recolor(57,"/tiles/actionTiles/yellow_tile-5.png", Color.GREEN, false);//5
            recolor(58,"/tiles/actionTiles/yellow_tile-11.png", Color.GREEN, false);//11
            recolor(59,"/tiles/actionTiles/yellow_tile-12.png", Color.GREEN, false);//12
            recolor(60, "/tiles/actionTiles/yellow_tile-13.png", Color.GREEN,false);//13

            //ORANGE
            recolor(61,"/tiles/actionTiles/yellow_tile-1.png", Color.ORANGE, false);//1
            recolor(62,"/tiles/actionTiles/yellow_tile-2.png", Color.ORANGE, false);//2
            recolor(63,"/tiles/actionTiles/yellow_tile-3.png", Color.ORANGE, false);//3
            recolor(64,"/tiles/actionTiles/yellow_tile-4.png", Color.ORANGE, false);//4
            recolor(65,"/tiles/actionTiles/yellow_tile-5.png", Color.ORANGE, false);//5
            recolor(66,"/tiles/actionTiles/yellow_tile-11.png", Color.ORANGE, false);//11
            recolor(67,"/tiles/actionTiles/yellow_tile-12.png", Color.ORANGE, false);//12
            recolor(68, "/tiles/actionTiles/yellow_tile-13.png", Color.ORANGE,false);//13


//            setup(25, "/elements/city/mailBox.png", true);
//            setup(26, "/elements/city/sampay_left.png", false);
//            setup(27, "/elements/city/sampay_right.png", false);
//            setup(28, "/elements/city/potPlant.png", false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setup(int index, String image, boolean collision){

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream(image));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recolor(int index, String image, Color newColor, boolean collision){

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream(image));
            tile[index].image = uTool.recolor(tile[index].image, newColor);
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    //load the general map
    public void loadMap(String path, int[][] map) {

        try {
            InputStream is = getClass().getResourceAsStream(path);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String[] numbers = br.readLine().split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    map[col][row] = Integer.parseInt(numbers[col]);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load collision map
    public void loadCollisionMap(String path, boolean[][] map){

        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++){
                String[] nums = br.readLine().split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++){
                    map[col][row] = nums[col].equals("1");
                }
            }

            br.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //draws the ground tile map
    public void drawGround(Graphics2D g2) {
        g2.drawImage(grassBackground, 0, 0, null);
        draw(g2, groundMap);
    }

    //draws the decor map
    public void drawDecor(Graphics2D g2) {

        int screenX = 0 - gp.cameraWorldX + (gp.screenWidth / 2);
        int screenY = 0 - gp.cameraWorldY + (gp.screenHeight / 2);

        if (decorBackground != null) {
            g2.drawImage(decorBackground, screenX, screenY, null);
        }
    }

    //draws the ground and decorations
    private void draw(Graphics2D g2, int[][] map) {

        int leftCol = (gp.cameraWorldX - gp.screenWidth / 2) / gp.tileSize;
        int rightCol = (gp.cameraWorldX + gp.screenWidth / 2) / gp.tileSize;
        int topRow = (gp.cameraWorldY - gp.screenHeight / 2) / gp.tileSize;
        int bottomRow = (gp.cameraWorldY + gp.screenHeight / 2) / gp.tileSize;

        // add small buffer
        leftCol -= 1;
        rightCol += 1;
        topRow -= 1;
        bottomRow += 1;

        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {

                if (col < 0 || row < 0 || col >= gp.maxWorldCol || row >= gp.maxWorldRow)
                    continue;

                int tileNum = map[col][row];
                if (tileNum == -1) continue;

                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;

                int screenX = worldX - gp.cameraWorldX + gp.screenWidth / 2;
                int screenY = worldY - gp.cameraWorldY + gp.screenHeight / 2;

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }
    }

    private void createGrassBackground() {

        grassBackground = new BufferedImage(
                gp.screenWidth,
                gp.screenHeight,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = grassBackground.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.dispose();
    }

    public void resizeWorld(int cols, int rows) {
        mapTileNum = new int[cols][rows];
        groundMap = new int[cols][rows];
        collisionMap = new boolean[cols][rows]; // if this exists separately
    }

    //chapters

    public void loadChapter2() {

        gp.maxWorldCol = 90;
        gp.maxWorldRow = 60;
        resizeWorld(gp.maxWorldCol, gp.maxWorldRow);
        // Load the new ground tile map
        loadMap("/maps/map02.txt", groundMap);

        // Load the new collision map
        loadCollisionMap("/maps/mapCollision02.txt", collisionMap);

        // Load the new decor image
        try {
            decorBackground = uTool.getImage("/maps/mapch2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        createGrassBackground();
    }

    //chapter 3
    public void loadChapter3() {

        gp.maxWorldCol = 90;
        gp.maxWorldRow = 60;
        resizeWorld(gp.maxWorldCol, gp.maxWorldRow);

        // Load the new decor image
        try {
            decorBackground = uTool.getImage("/maps/mapch3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the new ground tile map
        loadMap("/maps/map03.txt", groundMap);

        // Load the new collision map
        loadCollisionMap("/maps/mapCollision03.txt", collisionMap);

        createGrassBackground();
    }

}