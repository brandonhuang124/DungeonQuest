package Project2;
/***
* Simple map class that allows for switching
* map strings by which level ID is passed in
* while conversion to a array is still done in
* DungeonGame class. */

public class MapUtil {
    // bool for toggling printing maps and logs
    // set to true to print, additional tag for logs:
    private boolean debug = true;
    private final String TAG ="Maps class - ";
    public Tile[][] nextMapData;
    public Tile[][] previousMapData;
    public Tile[][] currentMap;

    /*** Selects the map depending on the level ID: */
    public Tile[][] initializeLevel(int level, int levelWidth, int levelHeight) {
        Tile tileMap[][] = new Tile[levelWidth][levelHeight];
        switch(level) {
            case 1:
                String map =
                                "11111111111111111111" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000011111100000001" +
                                "10000011111100000001" +
                                "10000000000000000001" +
                                "10000000000000000001" +
                                "10000011111100000001" +
                                "10000011111100000001" +
                                "10000000110000000001" +
                                "10000000110000000001" +
                                "10000011111100000001" +
                                "10000011111100000001" +
                                "10000000000000000001" +
                                "10000000000000000001" +
                                "11111111111111111111";
                tileMap = DungeonGame.getTileMap(map,
                                levelWidth, levelHeight);
                //initialize the remainder of the map(20 x 20 initially):
                nextMapData = levelMapNextData(level, levelWidth, levelHeight);
                break;
            case 2:
                //TODO add level2 map
                System.out.println(TAG +"level2 map TODO");
                break;
            case 3:
                //TODO add level3 map
                System.out.println(TAG +"level3 map TODO");
                break;
            default:
                System.out.println(TAG +"**Invalid level ID**");
                System.out.println(TAG +"returned tileMap[20][20] - no map data");
                System.out.println(TAG +"please check level ID being passed");
                break;
        }
        if(debug){
            System.out.println(TAG + "Initial level Map:");

        }
    return tileMap;
    }

    public void printCurrentMap(){
        printMapTiles(currentMap);
    }

    /***
     * picks the next data in the map, based on the level id
     * we could potentially use a file of char or ints to parse
     * and map to 20 by 20 array for scrolling for performance
     * purposes, this is done in the in initializeLevel method.
     */
    public Tile[][] levelMapNextData(int level, int levelWidth, int levelHeight){
        // as current path finding can only use a fixed height
        Tile tileMap[][] = new Tile[20][20];
        switch(level) {
            case 1:
                String map =
                        "11111111111111111111" +
                        "00000000110000000001" +
                        "00000000110000000001" +
                        "11110000110000000001" +
                        "11110000000000000001" +
                        "11110000000000000001" +
                        "00000000110000000001" +
                        "00000011111100000001" +
                        "00000011111100000001" +
                        "11000000000000000001" +
                        "11000000000000000001" +
                        "00000011111100000001" +
                        "00000011111100000001" +
                        "00000000000000000001" +
                        "11000000000000000001" +
                        "11000011111100000001" +
                        "11000011111100000001" +
                        "00000000000000000001" +
                        "00000000000000000001" +
                        "11111111111111111111";
                tileMap = DungeonGame.getTileMap(
                           map, levelWidth, levelHeight);
                if(debug)
                    System.out.println(TAG+"NextMapData");
                    System.out.println(map);
                break;
            case 2:
                //TODO add level2 map
                System.out.println(TAG +"level2 map data TODO");
                break;
            case 3:
                //TODO add level3 map
                System.out.println(TAG +"level3 map data TODO");
                break;
            default:
                System.out.println(TAG +"**Invalid level ID**");
                System.out.println(TAG +"returned tileMap[20][20] - no map data");
                System.out.println(TAG +"please check level ID being passed");
                break;
        }
        return tileMap;
    }

    /***
     *
     * copyCurrentToPreviousMap saves the current state of the map
     * to a previous 2d array for backtracking.
     * this should be done upon moving in any direction.
     */
    private Tile[][] copyCurrentToPreviousMap(Tile [][] currentMap){
        Tile [][] previousMapData = new Tile[currentMap.length][];
        // copy the currentMap to previousMapData.
        for(int i = 0; i < currentMap.length; i++) {
            Tile[] column = currentMap[i];
            int colLength = column.length;
            previousMapData [i] = new Tile[colLength];
            System.arraycopy(column, 0, previousMapData[i], 0, colLength);
        }
        return previousMapData;
    }

    /*** scroll right based on the CURRENT state of the map */
    public Tile[][] ScrollRight(Tile[][] currentMap) {
        // copy the currentMap to previous so player can backtrack:
        previousMapData = copyCurrentToPreviousMap(currentMap);
        // [row][col]
        Tile [][] shiftedTempMap = new Tile[currentMap.length][];
        // copy the currentMap and shift it over by 1 column:
        for(int i = 0; i < currentMap.length; i++) {
            Tile[] column = currentMap[i];
            int colLength = column.length;
            shiftedTempMap[i] = new Tile[colLength];
            System.arraycopy(column, 1, shiftedTempMap[i], 0, colLength-1);
        }
        // we take in the nextMapData - remaining data of our map
        // we copy the columns of the nextMapData into the new map
        // we may assume here that nextMap data is not of the same dimensions as the currentmap
        // and then copy the first column of nextMap data and add it to the end of the shiftedMap
        for(int i = 0; i < nextMapData.length; i++) {
            Tile[] column = nextMapData[i];
            int colLength = column.length;
            System.arraycopy(nextMapData[i], 0,  shiftedTempMap[i], colLength-1, 1);
        }
        return shiftedTempMap;
    }

    /*** scroll left based on the CURRENT state of the map */
    public void ScrollLeft(Tile currentMap[][]) {
        //remove rightmost column.

        //insert new column in the leftmost position, shifting other columns right one
    }

    /***
     * simple print method for printing a map for debugging purposes:
     */
    private void printMapTiles(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j].getID()+ " ");
            }
            System.out.println();
        }
    }
}
