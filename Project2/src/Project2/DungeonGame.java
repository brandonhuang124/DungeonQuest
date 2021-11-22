package Project2;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 *
 * Description:
 *
 * Controls:
 *
 * States:
 *
 * Graphical Asset Credits:
 *
 * Sound Credits:
 *
 * @author
 *
 */

public class DungeonGame extends StateBasedGame {
  // States
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;
  public static final int LEVEL1 = 2;

  // Important parameters
  public static final int TILESIZE = 32;
  public static final int SCALE = 1;

  /*** ASSET PATHS ***/
  public static final String PLAYER_ARROWTEST_RSC = "Project2/Assets/arrow.png";
  public static final String PLAYER_PROJECTILE_RSC = "Project2/Assets/projectile.png";

  public static final String MAP_WALL_RSC = "Project2/Assets/wall.png";
  public static final String MAP_FLOOR_RSC = "Project2/Assets/floor.png";

  // Parameters
  public final int ScreenWidth;
  public final int ScreenHeight;
  /**
   * Create a new state based game
   *
   * @param title The name of the game
   */
  public DungeonGame(String title, int width, int height) {
    super(title);
    ScreenHeight = height;
    ScreenWidth = width;

    Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    // Load states
    addState(new StartState());
    addState(new TestState());

    /*** RESOURCE LOADING ***/
    ResourceManager.loadImage(PLAYER_ARROWTEST_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILE_RSC);

    ResourceManager.loadImage(MAP_FLOOR_RSC);
    ResourceManager.loadImage(MAP_WALL_RSC);
  }

  public static void main(String[] args) {
    AppGameContainer app;
    // Open the game
    try {
      app = new AppGameContainer(new DungeonGame("Project2", 1000,1000));
      app.setDisplayMode(640,640, false);
      app.setVSync(true);
      app.setTargetFrameRate(60);
      app.start();
    } catch(SlickException e) {
      e.printStackTrace();
    }
  }

  /***
   * This function builds a tilemap from a given string which represents the level layout. The string is one long
   * uninterrupted string of 100 characters to build a 10x10 map. The String consists of integers which are IDs for
   * what type each space is. Specifically only works for 100 character strings to build 10x10s. DO NOT GIVE OTHER
   * STINGS.
   * Note: This method needs to be modified if different sized maps are made.
   * @param map
   *  The string for building the level
   * @return
   *  A finished tile map (2D tile array) of constructed tiles
   */
  public static Tile[][] getTileMap(String map, int width, int height) {
    // Check if an invalid string to build was given.
    if(width * height != map.length()) {
      System.out.println("String length did not match map dimensions");
      return null;
    }
    Tile tileMap[][] = new Tile[width][height];
    char tempMap[] = map.toCharArray();
    int x = 0, y = 0, i = 0;
    for(char current : tempMap) {
      if(x == height) {
        x = 0;
        y++;
      }
      tileMap[x][y] = new Tile(Character.getNumericValue(tempMap[i]));
      i++;
      x++;
    }
    return tileMap;
  }

  /**
   * Dijkstras Algorithm for a 10x10 map of Tile objects. Will build a 2D Vertex array to be used for pathfinding for
   * enemies in the game. Only works for 10x10 maps currently
   * Note: If different maps sizes are ever used, this function needs to be modified.
   *
   * @param sourcex
   *  The x coordinate to path to
   * @param sourcey
   *  The y coordinate to path to
   * @param tileMap
   *  The constructed tile map showing the map layout
   * @return
   *  A completed 2D vertex array, filled with costs and directions to move in.
   */
  public static Vertex[][] getDijkstras(int sourcex, int sourcey, Tile[][] tileMap, int width, int height) {
    Vertex path[][] = new Vertex[width][height];
    boolean seen[][] = new boolean[width][height];

    // Intialize the path and seen arrays
    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        path[x][y] = new Vertex(tileMap[x][y].getCost());
        seen[x][y] = false;
      }
    }

    // Set the source distance to 0
    path[sourcex][sourcey].setDistance(0);

    // Keep going until all nodes are seen
    while(hasUnseenNodes(seen, width , height)) {
      // Get the node with the current shortest distance
      Coordinate current = shortestDistance(path, seen, width, height);
      int x = current.x;
      int y = current.y;
      // Mark the current node as seen.
      seen[x][y] = true;
      double compare;
      double currentDist = path[x][y].getDistance();

      // Now update tile distances that are adjacent if the distance is shorter then currently recorded.
      // Tile below
      if(y > 0) {
        compare = currentDist + path[x][y-1].getCost();
        if(path[x][y-1].getDistance() > compare) {
          path[x][y-1].setDistance(compare);
          path[x][y-1].setDirection(2);
        }
      }
      // Tile above
      if(y < height - 1) {
        compare = currentDist + path[x][y+1].getCost();
        if(path[x][y+1].getDistance() > compare) {
          path[x][y+1].setDistance(compare);
          path[x][y+1].setDirection(8);
        }
      }
      // Tile right
      if(x > 0) {
        compare = currentDist + path[x-1][y].getCost();
        if(path[x-1][y].getDistance() > compare) {
          path[x-1][y].setDistance(compare);
          path[x-1][y].setDirection(6);
        }
      }
      // Tile left
      if(x < width - 1) {
        compare = currentDist + path[x+1][y].getCost();
        if(path[x+1][y].getDistance() > compare) {
          path[x+1][y].setDistance(compare);
          path[x+1][y].setDirection(4);
        }
      }
      // Tile down right
      if(x > 0 && y > 0) {
        compare = currentDist + (path[x-1][y-1].getCost() * Math.sqrt(2));
        if(path[x-1][y-1].getDistance() > compare) {
          path[x-1][y-1].setDistance(compare);
          path[x-1][y-1].setDirection(3);
        }
      }
      // Tile down left
      if(x < width - 1 && y > 0) {
        compare = currentDist + (path[x+1][y-1].getCost() * Math.sqrt(2));
        if(path[x+1][y-1].getDistance() > compare) {
          path[x+1][y-1].setDistance(compare);
          path[x+1][y-1].setDirection(1);
        }
      }
      // Tile up right
      if(x > 0 && y < height - 1) {
        compare = currentDist + (path[x-1][y+1].getCost() * Math.sqrt(2));
        if(path[x-1][y+1].getDistance() > compare) {
          path[x-1][y+1].setDistance(compare);
          path[x-1][y+1].setDirection(9);
        }
      }
      // Tile up left
      if(x < width - 1 && y < height - 1) {
        compare = currentDist + (path[x+1][y+1].getCost() * Math.sqrt(2));
        if(path[x+1][y+1].getDistance() > compare) {
          path[x+1][y+1].setDistance(compare);
          path[x+1][y+1].setDirection(7);
        }
      }
    }
    return path;
  }

  /***
   * Sub method for getDijkstras(). Checks the seen array if there are unseen nodes.
   * @param seen
   *  2D array of booleans showing all the currently seen nodes
   * @return
   *  true if there are unseen nodes
   *  false if there aren't
   */
  private static boolean hasUnseenNodes(boolean seen[][], int width, int height) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (!seen[x][y]) {
          // System.out.println("found a node x:" + x + " y:" + y);
          return true;
        }
      }
    }
    return false;
  }

  /***
   * Sub method for getDijkstras(). Searches the vertex graph for the unseen node with the lowest distance.
   * @param graph
   *  Vertex graph being searched
   * @param seen
   *  2D boolean array showing which nodes have been marked as seen
   * @return
   *  Coordinate of the node in the vertex which has the lowest distance and is unseen.
   */
  private static Coordinate shortestDistance (Vertex graph[][], boolean seen[][], int width, int height) {
    Coordinate shortest = new Coordinate(0,0);
    double distance = 100000000;
    // Iterate through the graph and find the right node.
    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        double newDistance = graph[x][y].getDistance();
        if(newDistance < distance && !seen[x][y]) {
          distance = newDistance;
          shortest.x = x;
          shortest.y = y;
        }
      }
    }
    return shortest;
  }
}
