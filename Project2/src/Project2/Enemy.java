package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.LinkedList;

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
 *      2: Ranged enemy, attacks the player from range, only fires if they have line of sight on the player, and
 *      other wise moves towards them.
 */


public class Enemy extends Entity{

  private Vector velocity;
  private float speed;
  public int id;
  private int health, sleeptimer, damage;
  private boolean isDead, sleep, faceRight;
  private double targetAngle;
  private Animation moveLeft, moveRight, idleLeft, idleRight, current;
  public Coordinate worldPos;
  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
   */
  public Enemy(final float x, final float y, int newid) {
    super(x,y);
    // addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_ARROWTEST_RSC));
    velocity = new Vector(0,0);
    speed = 0.15f;
    id = newid;
    isDead = sleep = faceRight = false;
    sleeptimer = 0;
    // Set animations and attributes based on type:
    //  Melee
    if(id == 1) {
      health = 10;
      damage = 2;
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_MELEEIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
    }
    //  Ranged
    else if(id == 2) {
      health = 10;
      damage = 2;
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
              DungeonGame.ENEMY_RANGEDIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
              true, 100, true);
    }
    //  Invalid id, so we need to kill it immediately
    else {
      isDead = true;
      health = 0;
    }
    addAnimation(idleLeft);
    current = idleLeft;
  }

  public void setWorldPos(TileIndex tileIndex) {
    worldPos = MapUtil.convertTileToWorld(tileIndex);
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
  public void makeMove(Tile[][] tilemap, Vertex[][] path1, Player player1,
                       LinkedList<Projectile> projectileList, int delta) {
    // First check if were currently asleep due to actions such as attacking.
    if(sleep) {
      sleeptimer -= delta;
      if(sleeptimer <= 0)
        sleep = false;
      return;
    }

    // Get location and retrieve direction from the vertex map.
    TileIndex playerLocation = MapUtil.convertWorldToTile(player1.worldPos);
    TileIndex location = MapUtil.convertWorldToTile(worldPos);
    int direction = path1[location.x][location.y].getDirection();

    // Attempt attacks depending on enemy type:
    //  Melee
    if(id == 1) {
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        player1.damage(damage);
        System.out.println("Player Hit! " + player1.getCurrentHealth());
        sleep = true;
        sleeptimer = 500;
        stop();
        return;
      }
    }

    //  Ranged
    if(id == 2) {
      // Check if the enemy has a clear line of sight on the player.
      if(lineOfSight(player1, tilemap)) {
        // targetAngle is set when lineOfSight() is called. It's an object property so we just need to access it.
        Projectile newProjectile = new Projectile(getX(), getY(), 2, targetAngle, damage);
        newProjectile.worldPos = new Coordinate(worldPos.x, worldPos.y);
        projectileList.add(newProjectile);
        System.out.println("Player in sight!");
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
   * They also set the proper animations for the enemy.
   */
  public void moveRight() {
    velocity = new Vector(speed, 0);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveLeft() {
    velocity = new Vector(-speed, 0);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void moveUp() {
    velocity = new Vector(0, -speed);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(moveRight);
      current = moveRight;
    }
    else {
      addAnimation(moveLeft);
      current = moveLeft;
    }
  }

  public void moveDown() {
    velocity = new Vector(0, speed);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(moveRight);
      current = moveRight;
    }
    else {
      addAnimation(moveLeft);
      current = moveLeft;
    }
  }

  // For the diaganol movement, speed is scaled in each direction by 1/sqrt(2) since its at a 45 degree angle.
  public void moveDownRight() {
    velocity = new Vector(0.71f * speed, 0.71f * speed);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveDownLeft() {
    velocity = new Vector(-0.71f * speed, 0.71f * speed);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void moveUpRight() {
    velocity = new Vector(0.71f * speed, -0.71f * speed);
    faceRight = true;
    removeAnimation(current);
    addAnimation(moveRight);
    current = moveRight;
  }

  public void moveUpLeft() {
    velocity = new Vector(-0.71f * speed, -0.71f * speed);
    faceRight = false;
    removeAnimation(current);
    addAnimation(moveLeft);
    current = moveLeft;
  }

  public void stop() {
    velocity = new Vector(0, 0);
    removeAnimation(current);
    if(faceRight) {
      addAnimation(idleRight);
      current = moveRight;
    }
    else {
      addAnimation(idleLeft);
      current = moveLeft;
    }
  }

  /**
   * This function is for getting the coordinate of the Enemy
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public TileIndex getLocation() {
    return MapUtil.convertWorldToTile(worldPos);
  }

  /**
   * This function is for calculating the offset from the center of the tile the Enemy currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the Enemy
   */
  public TileIndex getTileIndex() {
    int x = Math.round((this.getX() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    int y = Math.round((this.getY() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    return new TileIndex(x, y);
  }


  public Vector getTileOffset() {
    TileIndex location = getTileIndex();
    // The center of the tile location is (Tile * tilewidth) + 1/2 tile width, since the entity's origin is the center.
    float tilex = (location.x * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
    float tiley = (location.y * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
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
    Vector movement = velocity.scale(delta);
    worldPos.x += movement.getX();
    worldPos.y += movement.getY();
  }



  /***
   * Function for determining if there is a clear line between a target and the enemy.
   * @param player
   * @param tilemap
   * @return
   */
  private boolean lineOfSight(Player player, Tile[][] tilemap) {
    float enemyx = worldPos.x;
    float enemyy = worldPos.y;
    float playerx = player.worldPos.x;
    float playery = player.worldPos.y;

    // Get a Vector between the two objects and scale it to 1.
    Vector sightLine = new Vector(playerx - enemyx, playery - enemyy);
    sightLine = sightLine.unit();
    targetAngle = sightLine.getRotation();

    // Starting at the enemy, and iterating through this unit vector until we reach or pass the player,
    // Check if there is an obstacle in the way. If there is, we don't have line of sight.
    // Check which x coordinate is greater to determine which way to do the check.
    if(enemyx > playerx) {
      while(enemyx > playerx) {
        TileIndex currentLocation = MapUtil.convertWorldToTile(new Coordinate(enemyx,enemyy));
        // Check if were in a wall
        if(tilemap[currentLocation.x][currentLocation.y].getID() == 1) {
          return false;
        }
        // If no collisions, traverse further down the line.
        enemyx += sightLine.getX();
        enemyy += sightLine.getY();
      }
    }
    // POSSIBLY ADD A CHECK IF WERE DIRECTLY VERTICAL OF THE PLAYER. THIS IS AN EDGE CASE ALTHOUGH
    else {
      while(enemyx < playerx) {
        TileIndex currentLocation = MapUtil.convertWorldToTile(new Coordinate(enemyx,enemyy));
        // Check if were in a wall
        if(tilemap[currentLocation.x][currentLocation.y].getID() == 1) {
          return false;
        }
        // If no collisions, traverse further down the line.
        enemyx += sightLine.getX();
        enemyy += sightLine.getY();
      }
    }
    return true;
  }
}
