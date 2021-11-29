package Project2;



import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

import java.io.*;



public class MapUtil {
    private final String TAG = "MapUtil -";
    Boolean debug = true;
    //Path to the level 60x60 array:
    private final String level1Data = "C:\\Users\\Sdesh\\IdeaProjects\\cs447finalgame\\Project2\\src\\Project2\\Data\\LevelOneMap.csv";
    public static final int TILESIZE = 32;

    String level2Data ="";
    String level3Data = "";

    int levelWidth;
    int levelHeight;
    Tile[][] currentTileMap;
    Coordinate playerPrevLoc = new Coordinate(4,4);
    Coordinate screenTileRender = new Coordinate(0,0);
    Coordinate screenEndTiles = new Coordinate(20,20);



    String currentMapString;
    int scrollOffsetX = 0;
    int scrollOffsetY = 0;
    int startOffsetX = 0;
    int startOffsetY = 0;

    int maxTilesOnScreen = 20;

    public void loadLevelMap(int level) throws IOException {
        switch(level){
            case 1:
                currentMapString = readLevelCSV(level1Data);
                break;
            case 2:
                //TODO level2 map
                break;
            case 3:
                //TODO level3 map
                break;
            default:
                System.out.print(TAG + "Level not found ");
                break;
        }
    }

    public Coordinate convertPosToTile(float posX, float posY){
        Coordinate tileIndex = new Coordinate(Math.round(posX/TILESIZE), Math.round(posY/TILESIZE));
        return tileIndex;
    }
    public Vector convertTileToPos(Coordinate tileIndex){
        Vector screenPos = new Vector((tileIndex.x - screenTileRender.x) * TILESIZE, (tileIndex.y - screenTileRender.y) *TILESIZE);
        return screenPos;
    }

    public float getDistanceFromCenterOfTile(float pos){
        return pos % TILESIZE;
    }

    public void renderMap(Graphics g){

        for(int screenX = 0; screenX < maxTilesOnScreen; screenX++){
            for(int screenY = 0; screenY < maxTilesOnScreen; screenY++) {
                int tileIndexX = screenTileRender.x + screenX;
                int tileIndexY = screenTileRender.y + screenY;
                Tile renderTile = currentTileMap[tileIndexX][tileIndexY];
                // Floor tile
                if(renderTile.getID() == 0) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            screenX * DungeonGame.TILESIZE, screenY * DungeonGame.TILESIZE);
                }
                // Wall tile
                else if(renderTile.getID() == 1) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                            screenX * DungeonGame.TILESIZE, screenY * DungeonGame.TILESIZE);
                }

            }
        }
    }

    public void updateMap(Coordinate playerLoc){

        if(playerLoc.x > playerPrevLoc.x +1){
            // scroll right
            screenTileRender.x = Math.min(screenTileRender.x+1,levelWidth-1);
            playerPrevLoc.x = playerLoc.x;
        }
        if(playerLoc.x < playerPrevLoc.x -1){
            // scroll left
            screenTileRender.x = Math.max(screenTileRender.x-1, 0);
            playerPrevLoc.x = playerLoc.x;
        }
        if(playerLoc.y > playerPrevLoc.y +1){
            // scroll down
            screenTileRender.y = Math.min(screenTileRender.y +1, levelHeight-1);
            playerPrevLoc.y = playerLoc.y;
        }
        if(playerLoc.y < playerPrevLoc.y -1){
            // scroll up
            screenTileRender.y = Math.max(screenTileRender.y-1, 0);
            playerPrevLoc.y = playerLoc.y;
        }
    }

    private String readLevelCSV(String MapData) throws IOException {

        File file = new File(level1Data);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String map = "";
        String st;
        while ((st = br.readLine()) != null) {
            st = st.replaceAll(",", "");
            if (debug) {
                System.out.println(st);
            }
            map += st;
        }
        return map;
    }

    public Coordinate scrollRight(int x, int y , int levelWidth){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(x+1 < levelWidth){
            scrollOffsetX = x+1;
            scrollOffsetY = y;

            startRenderLoc = new Coordinate(scrollOffsetX, scrollOffsetY);
        }
        return startRenderLoc;
    }

    public Coordinate scrollLeft(int x, int y, int levelWidth){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(x-1 > 0){
            //TODO set offset in maputil only
            scrollOffsetX = x-1;
            scrollOffsetY = y;

            startRenderLoc = new Coordinate(scrollOffsetX, scrollOffsetY);
        }
        return startRenderLoc;
    }

    public Coordinate scrollDown(int x, int y, int levelHeight){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(y+1 < levelHeight){
            scrollOffsetX = x;
            scrollOffsetY = y+1;

            startRenderLoc = new Coordinate(scrollOffsetX, scrollOffsetY);
        }
        return startRenderLoc;
    }
    public Coordinate scrollUp(int x, int y, int levelHeight){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(y-1 > 0){
            scrollOffsetX = x;
            scrollOffsetY = y-1;

            startRenderLoc = new Coordinate(scrollOffsetX, scrollOffsetY);
        }
        return startRenderLoc;
    }

}
