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
    String level1Data = "cs447finalgame\\Project2\\src\\Project2\\Data\\LevelOneMap.csv";
    String level2Data ="";
    String level3Data = "";

    Tile[][] currentTileMap;

    String currentMapString;


    public void MapUtil(String MapData, int level) throws IOException {

        File file = new File(level1Data);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            st = st.replaceAll(",", "");
            if (debug) {
                System.out.println(TAG + "Initialized Map:");
                System.out.println(st);
            }
        }
    }
    /**
     * Render a section of the tile map
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param startx The x tile location to start rendering
     * @param starty The y tile location to start rendering
     * @param width The width of the section to render (in tiles)
     * @param height The height of the secton to render (in tiles)
     */
    public void render(int x,int y,int startx,int starty,int width,int height) {

        render(x,y,startx,starty,width,height);
    }

}
