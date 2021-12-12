package Project2;



import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

import java.io.*;



public class MapUtil {
    private final String TAG = "MapUtil -";
    Boolean debug = true;
    //Path to the level 60x60 array:
    private final String level1Data = "Project2/src/Project2/Data/LevelOneMap_2.csv";
    public static final int TILESIZE = 32;
    public static final int SCREENWIDTH = 20;
    public static final int SCREENHEIGHT = 20;
    public static final int LEVELWIDTH = 60;
    public static final int LEVELHEIGHT = 60;
    private LevelName levelName;

    String level2Data = "";
    String level3Data = "";


    Tile[][] currentTileMap;
    Coordinate cameraPos = new Coordinate(0, 0);
    Coordinate maxCameraPos = new Coordinate((LEVELWIDTH * TILESIZE) - (SCREENWIDTH * TILESIZE),
            (LEVELHEIGHT * TILESIZE) - (SCREENHEIGHT * TILESIZE));


    String currentMapString;
    // returns the levelname of type LevelName, can be LevelName.ONE, LevelName.START, etc.
    public LevelName getCurrentLevel(){
        return levelName;
    }
    // mostly used in changing states within dungeon game:
    public void setLevelName(LevelName name){
        this.levelName = name;
    }
    // could be refactored to take a levelName in dungeon game
    public void loadLevelMap(int level) throws IOException {
        switch (level) {
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
                System.out.print(TAG + "error: Level and state not found or listed ");
                break;
        }
    }

    /***
     * Screen coordinates: where objects are on camera.
     * World coordinates: where objects are a separate
     *      coordinate system relative to the entire map including off camera.
     * TileIndex: the index of the tile map, a tile that a position is on.
     */
    public Coordinate convertScreenToWorld(Coordinate screenPos){
        return new Coordinate(cameraPos.x + screenPos.x, cameraPos.y + screenPos.y);
    }

    public Coordinate convertWorldToScreen(Coordinate worldPos){
        return new Coordinate(worldPos.x - cameraPos.x, worldPos.y - cameraPos.y);
    }

    public static Coordinate convertTileToWorld(TileIndex tileIndex){
        return new Coordinate((tileIndex.x * TILESIZE) + (0.5f * TILESIZE), (tileIndex.y * TILESIZE) + (0.5f * TILESIZE));
    }

    public Coordinate convertTileToScreen(TileIndex tileIndex){
        Coordinate worldPos = convertTileToWorld(tileIndex);
        return convertWorldToScreen(worldPos);
    }

    // a very handy method for handling collision checks for a tile index:
    public Boolean hasCollision(TileIndex tileIndex){
        int tileValue = currentTileMap[tileIndex.x][tileIndex.y].getID();
        // return to true if the tile is not blank and not the door tile:
        return (tileValue > 0 && tileValue != 6);
    }
    // allowing for a vector from world to tile:
    public static TileIndex convertWorldToTile(Vector worldPos){
        return new TileIndex((int)Math.floor(worldPos.getX() / TILESIZE), (int)Math.floor(worldPos.getY() / TILESIZE));
    }
    // converting from world to tile:
    public static TileIndex convertWorldToTile(Coordinate worldPos){
        return new TileIndex((int)Math.floor(worldPos.x / TILESIZE), (int)Math.floor(worldPos.y / TILESIZE));
    }
    // convert screen to tile if needed:
    public TileIndex convertScreenToTile(Coordinate screenPos){
        Coordinate worldPos = convertScreenToWorld(screenPos);
        return convertWorldToTile(worldPos);
    }
    // offset of the camera can be in between and partially on tiles:
    public Coordinate getCameraTileOffset() {
        Coordinate cameraTilePos = new Coordinate(cameraPos.x % TILESIZE, cameraPos.y % TILESIZE);
        return cameraTilePos;
    }
    // scroll our camera based on independent player movement:
    public void updateCamera(Coordinate deltaMove) {
        cameraPos.x = deltaMove.x - (0.5f * SCREENWIDTH * TILESIZE);
        cameraPos.y = deltaMove.y - (0.5f * SCREENHEIGHT * TILESIZE);
        if (cameraPos.x < 0) {
            cameraPos.x = 0;
        } else if (cameraPos.x > maxCameraPos.x) {
            cameraPos.x = maxCameraPos.x;
        }
        if (cameraPos.y < 0) {
            cameraPos.y = 0;
        } else if (cameraPos.y > maxCameraPos.y) {
            cameraPos.y = maxCameraPos.y;
        }
    }


    public void renderMapByCamera(Graphics g) {
        TileIndex currentTile = convertWorldToTile(cameraPos);
        Coordinate cameraOffset = getCameraTileOffset();
        float maxWidth =  SCREENWIDTH * TILESIZE;
        float maxHeight = SCREENHEIGHT * TILESIZE;
        // -cameraOffset is the amount off screen to the left:
        for (float renderX = (-cameraOffset.x); renderX < maxWidth; renderX += TILESIZE, currentTile.x++) {
            int currentY = currentTile.y;
            for (float renderY = (-cameraOffset.y); renderY < maxHeight; renderY += TILESIZE, currentY++) {
                Tile renderTile = currentTileMap[currentTile.x][currentY];
                // Floor tile
                if (renderTile.getID() == 0) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
                // Wall tile
                else if (renderTile.getID() == 1) {
                    g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
                            renderX, renderY);
                }
            }
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
}
