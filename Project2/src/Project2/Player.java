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
  private Vector velocity;
  private float speed;

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
   * Below are the 5 movement functions for allowing the player to mve around the map
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

  public void stop() {
    velocity = new Vector(0, 0);
  }

  /**
   * This function is for getting the coordinate of the player.
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public Coordinate getLocation() {
    int x = Math.round((this.getX() - DungeonGame.TILESIZE / 2) / DungeonGame.TILESIZE);
    int y = Math.round((this.getY() - DungeonGame.TILESIZE / 2) / DungeonGame.TILESIZE);
    return new Coordinate(x,y);
  }

  /**
   * This function is for calculating the offset from the center of the tile the player currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the player
   */
  public Vector getTileOffset() {
    Coordinate location = getLocation();
    // The center of the tile location is (Tile * tilewidth) + 1/2 tile width, since the entity's origin is the center.
    float tilex = (location.x * DungeonGame.TILESIZE) + (DungeonGame.TILESIZE / 2);
    float tiley = (location.y * DungeonGame.TILESIZE) + (DungeonGame.TILESIZE / 2);
    float x = this.getX();
    float y = this.getY();
    // Return the offset from the center
    return new Vector(tilex - x, tiley - y);
  }

  /**
   * This function is to be called before executing a player move to ensure the move is valid.
   * @param direction
   *  An int representing the direction of movement:
   *  2: Down
   *  4: Left
   *  6: Right
   *  8: Up
   * @param tilemap
   *  The tilemap representing the level layout.
   * @return
   * A boolean showing if the move is valid or not.
   */
  public boolean isMoveValid(int direction, Tile[][] tilemap) {
    Coordinate location = getLocation();
    // Down
    if(direction == 2) {
      // Check if the tile left is a wall
      if(tilemap[location.x][location.y+1].getID() == 1) {
        // If it is, we need to check were not too far into the tile where we will go into the wall.
        Vector offset = getTileOffset();
        if(offset.getY() <= 0)
          return false;
      }
    }
    // Left
    else if (direction == 4) {
      if(tilemap[location.x-1][location.y].getID() == 1) {
        Vector offset = getTileOffset();
        if(offset.getX() >= 0)
          return false;
      }
    }
    // Right
    else if (direction == 6) {
      if(tilemap[location.x+1][location.y].getID() == 1) {
        Vector offset = getTileOffset();
        if(offset.getX() <= 0)
          return false;
      }
    }
    // Up
    else if (direction == 8) {
      if(tilemap[location.x][location.y-1].getID() == 1) {
        Vector offset = getTileOffset();
        if(offset.getY() >= 0)
          return false;
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
  public void offsetUpdate(Tile[][] tilemap) {
    // Check if any adjacent tiles are walls, and if were inside any of them. If so do an offset update.
    Coordinate location = getLocation();
    // Tile above
    if(tilemap[location.x][location.y - 1].getID() == 1 && getTileOffset().getY() >= 0) {
      translate(0, getTileOffset().getY());
    }
    // Tile Below
    if(tilemap[location.x][location.y + 1].getID() == 1 && getTileOffset().getY() <= 0) {
      translate(0, getTileOffset().getY());
    }
    // Tile Left
    if(tilemap[location.x - 1][location.y].getID() == 1 && getTileOffset().getX() >= 0) {
      translate(getTileOffset().getX(), 0 );
    }
    // Tile Right
    if(tilemap[location.x][location.y].getID() == 1 && getTileOffset().getX() <= 0) {
      translate(getTileOffset().getX(), 0 );
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
