package Project2;



import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.Image;
import java.io.*;



public class MapUtil {
    private final String TAG = "MapUtil -";
    Boolean debug = true;
    //Path to the level 60x60 array:
    private final String level1Data = "C:\\Users\\Sdesh\\IdeaProjects\\cs447finalgame\\Project2\\src\\Project2\\Data\\LevelOneMap.csv";
    String level2Data ="";
    String level3Data = "";

    Tile[][] currentTileMap;

    String currentMapString;

    int startX = 0;
    int startY = 0;
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

    public Coordinate scrollRight(int x, int y , int width){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(x+1 < width){
            startX = x+1;
            startY = y;

            startRenderLoc = new Coordinate(startX,startY);
        }
        return startRenderLoc;
    }

    public Coordinate scrollLeft(int x, int y, int width){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(x-1 > 0){
            startX = x-1;
            startY = y;

            startRenderLoc = new Coordinate(startX,startY);
        }
        return startRenderLoc;
    }

    public Coordinate scrollDown(int x, int y, int height){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(y+1 < height){
            startX = x;
            startY = y+1;

            startRenderLoc = new Coordinate(startX,startY);
        }
        return startRenderLoc;
    }
    public Coordinate scrollUp(int x, int y, int height){
        Coordinate startRenderLoc = new Coordinate(x,y);
        if(y-1 > 0){
            startX = x;
            startY = y-1;

            startRenderLoc = new Coordinate(startX,startY);
        }
        return startRenderLoc;
    }

}
