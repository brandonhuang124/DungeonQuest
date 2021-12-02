package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/***
 * Enitity class for reprsenting the player character.
 *
 *  Methods:
 *    moveUp()
 *    moveDown()
 *    moveLeft()
 *    moveRight()
 *    stop()
 *    fire()
 */

public class Player extends Entity {
  private static final String TAG =  "Player -" ;
  private Vector velocity;
  private float speed;
 // MapUtil levelMap;


  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
   */
  public Player(final float x, final float y) {
    super(x,y);
    addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_ARROWTEST_RSC));
    velocity = new Vector(0,0);
    speed = 0.25f;
  }



  /**+
   * Below are the 5 movement functions for allowing the player to move around the map
   */
  public void moveRight() {
    velocity = new Vector(speed, 0);
  }

  public void moveLeft() {
    velocity = new Vector(-speed, 0);
  }

  public void moveUp() {
    velocity = new Vector(0, -speed);
  }

  public void moveDown() {
    velocity = new Vector(0, speed);
  }

  // For the diaganol movement, speed is scaled in each direction by 1/sqrt(2) since its at a 45 degree angle.
  public void moveDownRight() { velocity = new Vector(0.71f * speed, 0.71f * speed);}

  public void moveDownLeft() { velocity = new Vector(-0.71f * speed, 0.71f * speed);}

  public void moveUpRight() { velocity = new Vector(0.71f * speed, -0.71f * speed);}

  public void moveUpLeft() { velocity = new Vector(-0.71f * speed, -0.71f * speed);}

  public void stop() {
    velocity = new Vector(0, 0);
  }

  /**
   * This function is for getting the coordinate of the player.
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public Coordinate getLocation() {
    int x = Math.round((this.getX() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    int y = Math.round((this.getY() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    return new Coordinate(x,y);
  }

  /**
   * This function is for calculating the offset from the center of the tile the player currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the player
   */
  public Vector getTileOffset(MapUtil levelMap) {
    Coordinate location = getLocation();
    // The center of the tile location is (Tile * tilewidth) + 1/2 tile width, since the entity's origin is the center.
    float tilex = (location.x * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
    float tiley = (location.y * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
    float x = this.getX();
    float y = this.getY();
    // Return the offset from the center
    return new Vector(tilex - x, tiley - y);
  }

  /**
   * This function is to be called before executing a player move to ensure the move is valid.
   * Directions ints come from their position on the numpad.
   * @param direction
   *  An int representing the direction of movement:
   *  1: Down and Left
   *  2: Down
   *  3: Down and Right
   *  4: Left
   *  6: Right
   *  7: Up and Left
   *  8: Up
   *  9: Up and Right
   *  The tilemap representing the level layout.
   * @return
   * A boolean showing if the move is valid or not.
   */
  public boolean isMoveValid(int direction, MapUtil levelMap) {
    Coordinate playerloc = getLocation();
    boolean adjacencyCheck = false;
    // Diagonal directions must check both the directions they are the diagonal of and the diagonal tile.
    // Down
    if(direction == 2 || direction == 1 || direction == 3) {
      // Check if the tile left is a wall
      if(levelMap.currentTileMap[playerloc.x][playerloc.y+1].getID() == 1) {
        // If it is, we need to check were not too far into the tile where we will go into the wall.
        Vector offset = getTileOffset(levelMap);
        adjacencyCheck = true;
        if(offset.getY() <= 0)
          return false;
      }
    }
    // Left
    // ** Process for checking is similar to above, but directions and tiles checked are changed.
    if (direction == 4 || direction == 1 || direction == 7) {
      if(levelMap.currentTileMap[playerloc.x-1][playerloc.y].getID() == 1) {
        Vector offset = getTileOffset(levelMap);
        adjacencyCheck = true;
        if(offset.getX() >= 0)
          return false;
      }
    }
    // Right
    if (direction == 6 || direction == 9 || direction == 3) {
      if(levelMap.currentTileMap[playerloc.x+1][playerloc.y].getID() == 1) {
        Vector offset = getTileOffset(levelMap);
        adjacencyCheck = true;
        if(offset.getX() <= 0)
          return false;
      }
    }
    // Up
    if (direction == 8 || direction == 7 || direction == 9) {
      if(levelMap.currentTileMap[playerloc.x][playerloc.y-1].getID() == 1) {
        Vector offset = getTileOffset(levelMap);
        adjacencyCheck = true;
        if(offset.getY() >= 0)
          return false;
      }
    }

    // We only want to do the diagonal check if none of the adjacent tiles are walls, so hiccup movments don't happen.
    // ** Process is similar to cardinal direction checks, but we must check x AND y offsets to ensure we won't
    // enter the tile.
    if(!adjacencyCheck) {
      // Up Right
      if (direction == 9) {
        if(levelMap.currentTileMap[playerloc.x+1][playerloc.y-1].getID() == 1) {
          Vector offset = getTileOffset(levelMap);
          if(offset.getY() >= 0 || offset.getX() <= 0)
            return false;
        }
      }
      // Up Left
      else if (direction == 7) {
        if(levelMap.currentTileMap[playerloc.x-1][playerloc.y-1].getID() == 1) {
          Vector offset = getTileOffset(levelMap);
          if(offset.getY() >= 0 || offset.getX() >= 0)
            return false;
        }
      }
      // Down Right
      else if (direction == 3) {
        if(levelMap.currentTileMap[playerloc.x+1][playerloc.y+1].getID() == 1) {
          Vector offset = getTileOffset(levelMap);
          if(offset.getY() <= 0 || offset.getX() <= 0)
            return false;
        }
      }
      // Down Left
      else if (direction == 1) {
        if(levelMap.currentTileMap[playerloc.x-1][playerloc.y+1].getID() == 1) {
          Vector offset = getTileOffset(levelMap);
          if(offset.getY() <= 0 || offset.getX() >= 0) {
            return false;
          }
        }
      }
    }

    // Return true if all tests were passed.
    return true;
  }

  /***
   * This function is to be called when the player fire a projectile.
   * @param angle
   * The angle to which the projectile will be fired in.
   * @return
   * The newly constructed projectile with the angle, originated at the player.
   */
  public Projectile fire(double angle) {
    Projectile newProjectile = new Projectile(this.getX(), this.getY(), 1, angle);
    return newProjectile;
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }


  /**
   * This function offsets the player's location so they aren't in walls. Call after every update.
   */
  public void offsetUpdate(MapUtil levelMap) {
    // Check if any adjacent tiles are walls, and if were inside any of them. If so do an offset update.
    Coordinate location = getLocation();
    // Tile above
    if(levelMap.currentTileMap != null) {
      if (levelMap.currentTileMap[location.x][location.y - 1].getID() == 1 && getTileOffset(levelMap).getY() >= 0) {
        translate(0, getTileOffset(levelMap).getY());
      }
      // Tile Below
      if (levelMap.currentTileMap[location.x][location.y + 1].getID() == 1 && getTileOffset(levelMap).getY() <= 0) {
        translate(0, getTileOffset(levelMap).getY());
      }
      // Tile Left
      if (levelMap.currentTileMap[location.x - 1][location.y].getID() == 1 && getTileOffset(levelMap).getX() >= 0) {
        translate(getTileOffset(levelMap).getX(), 0);
      }
      // Tile Right
      if (levelMap.currentTileMap[location.x + 1][location.y].getID() == 1 && getTileOffset(levelMap).getX() <= 0) {
        translate(getTileOffset(levelMap).getX(), 0);
      }
    }else{
      System.out.println(TAG + "TileMap is null");
    }
  }

  /***
   * This function rotates the player in the given direction. Meant to be used towards the mouse and called every
   * update.
   * @param theta
   *  The absolute angle to rotate to.
   */
  public void mouseRotate(final double theta) {
    setRotation(theta);
  }
}
