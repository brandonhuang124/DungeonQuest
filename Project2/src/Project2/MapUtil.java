package Project2;



import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

import java.io.*;



public class MapUtil {
    private final String TAG = "MapUtil -";
    Boolean debug = true;
    //Path to the level 60x60 array:
    private final String level1Data = "C:\\Users\\Sdesh\\IdeaProjects\\cs447finalgame\\Project2\\src\\Project2\\Data\\LevelOneMap_2.csv";
    public static final int TILESIZE = 32;
    public static final int SCREENWIDTH = 20;
    public static final int SCREENHEIGHT = 20;
    public static final int LEVELWIDTH = 60;
    public static final int LEVELHEIGHT = 60;


    String level2Data = "";
    String level3Data = "";


    Tile[][] currentTileMap;
    CameraCoordinate cameraPos = new CameraCoordinate(0, 0);
    CameraCoordinate maxCameraPos = new CameraCoordinate((LEVELWIDTH * TILESIZE) - (SCREENWIDTH * TILESIZE),
            (LEVELHEIGHT * TILESIZE) - (SCREENHEIGHT * TILESIZE));


    String currentMapString;


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
                System.out.print(TAG + "Level not found ");
                break;
        }
    }

    public Coordinate getTileIndexByCameraPosition(CameraCoordinate cameraPos) {
        Coordinate tileIndex = new Coordinate(0, 0);
        tileIndex.x = (int) Math.floor(cameraPos.x / TILESIZE);
        tileIndex.y = (int) Math.floor(cameraPos.y / TILESIZE);
        return tileIndex;
    }

    public CameraCoordinate getCameraTileOffset() {
        CameraCoordinate cameraTilePos = new CameraCoordinate(cameraPos.x % TILESIZE, cameraPos.y % TILESIZE);
        return cameraTilePos;
    }

    public void updateCamera(CameraCoordinate deltaMove) {
        cameraPos.x += deltaMove.x;
        cameraPos.y += deltaMove.y;
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
        Coordinate currentTile = getTileIndexByCameraPosition(cameraPos);
        CameraCoordinate cameraOffset = getCameraTileOffset();
        float maxWidth = cameraPos.x + (SCREENWIDTH * TILESIZE);
        float maxHeight = cameraPos.y + (SCREENHEIGHT * TILESIZE);
        // -cameraOffset is the amount off screen to the left:
        for (float renderX = (-cameraOffset.x); renderX <= maxWidth; renderX += TILESIZE, currentTile.x++) {
            int currentY = currentTile.y;
            for (float renderY = (-cameraOffset.y); renderY <= maxHeight; renderY += TILESIZE, currentY++) {
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
