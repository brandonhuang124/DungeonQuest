package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/***
 * Enitity class for reprsenting Enemies.
 *
 *  Methods:
 *    moveUp()
 *    moveDown()
 *    moveLeft()
 *    moveRight()
 *    stop()
 *    fire()
 *
 *  Important Fields:
 *    id: differentiates types of enemies
 *      1: Melee enemy, attacks from melee distance and moves directly toward the player.
 */


public class Enemy extends Entity{

  private Vector velocity;
  private float speed;
  private int id, health, sleeptimer;
  private boolean isDead, sleep;

  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
   */
  public Enemy(final float x, final float y, int newid) {
    super(x,y);
    addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_ARROWTEST_RSC));
    velocity = new Vector(0,0);
    speed = 0.15f;
    id = newid;
    isDead = sleep = false;
    sleeptimer = 0;
    if(id == 1) {
      health = 10;
    }
    else {
      isDead = true;
      health = 0;
    }
  }

  /**
   * This method is to be called before every enemy update. Contains all behaviors.
   * @param tilemap
   *  Tilemap representing the level the enemy is in
   * @param path1
   *  Vertex map from getDijkstras() which helps the enemy determine the path it wants to take. Pathing towards player 1
   * @param player1
   *  Player object representing player 1.
   */
  public void makeMove(Tile[][] tilemap, Vertex[][] path1, Player player1, int delta) {
    // First check if were currently asleep due to actions such as attacking.
    if(sleep) {
      sleeptimer -= delta;
      if(sleeptimer <= 0)
        sleep = false;
      return;
    }

    // Get location and retrieve direction from the vertex map.
    Coordinate location = getLocation();
    int direction = path1[location.x][location.y].getDirection();

    // Attempt attacks depending on enemy type:
    //  Melee
    if(id == 1) {
      Coordinate playerLocation = player1.getLocation();
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        player1.damage(2);
        System.out.println("Player Hit! " + player1.getCurrentHealth());
        sleep = true;
        sleeptimer = 500;
        stop();
        return;
      }
    }

    // If attacks are unavailable, the enemy will just move.
    switch (direction) {
      case 1:  moveDownLeft();
               break;
      case 2:  moveDown();
               break;
      case 3:  moveDownRight();
               break;
      case 4:  moveLeft();
               break;
      case 6:  moveRight();
               break;
      case 7:  moveUpLeft();
               break;
      case 8:  moveUp();
               break;
      case 9:  moveUpRight();
               break;
      default: stop();
               break;
    }
  }

  /**
   * Function to be called when the enemy is damaged.
   * @param damage
   *  Amount of damage taken as a positive integer
   * @return
   * A boolean which is identical to to the isDead boolean.
   */
  public boolean damage(int damage) {
    health -= damage;
    if(health <= 0) {
      isDead = true;
      return true;
    }
    return false;
  }

  public boolean isDead() { return isDead;}
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
   * This function is for getting the coordinate of the Enemy
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public Coordinate getLocation() {
    int x = Math.round((this.getX() - DungeonGame.TILESIZE / 2) / DungeonGame.TILESIZE);
    int y = Math.round((this.getY() - DungeonGame.TILESIZE / 2) / DungeonGame.TILESIZE);
    return new Coordinate(x,y);
  }

  /**
   * This function is for calculating the offset from the center of the tile the Enemy currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the Enemy
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
   * This function is to be called before executing an enemy move.
   * TODO Add relevant checks for determining if a move is valid. Walls checks are unneccesary since due to the
   * pathfinding algorithm, the enemy should only make valid moves.
   */
  public boolean isMoveValid(int direction, Tile[][] tilemap) {

    return true;
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }


  /**
   * This function offsets the enemies's location so they aren't in walls. Call after every update.
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
    if(tilemap[location.x + 1][location.y].getID() == 1 && getTileOffset().getX() <= 0) {
      translate(getTileOffset().getX(), 0 );
    }
  }
}
