package Project2;
/*
* Simple map class that allows for switching
* map strings by which level ID is passed in
* while conversion to a array is still done in
* DungeonGame class. */

public class Maps {
    private final String TAG ="Maps class - ";
    public Tile[][] initializeLevel(int level, int levelWidth, int levelHeight) {
        Tile tileMap[][] = new Tile[1][1];
        switch(level) {
            case 1:
                tileMap = DungeonGame.getTileMap(
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
                                "11111111111111111111",
                                levelWidth, levelHeight);
                break;
            case 2:
                //TODO add level2 map
                System.out.println(TAG +"level2 map TODO");
                break;
            case 3:
                //TODO add level3 map
                System.out.println(TAG +"level3 map TODO");
            default:
                System.out.println(TAG +"**Invalid level ID**");
                System.out.println(TAG +"returned tileMap[1][1]");
                System.out.println(TAG +"please check level ID being passed");
                break;
        }
    return tileMap;
    }
}
