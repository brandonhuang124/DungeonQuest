package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

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
  private static final String TAG = "Player -";

  private Vector velocity;
  private float speed;
  private int damage, maxhealth, health, attackCooldownTimer, attackCooldown, playerType;
  private boolean faceRight, attackReady;
  private Animation moveLeft, moveRight, idleLeft, idleRight, current;
  public Weapon weapon;
  public Coordinate worldPos;
  public Coordinate prevMoveVelocity;

  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
   * @param id
   *  Player type:
   *    1: Ranged player
   *    2: Melee player
   */
  public Player(final float x, final float y, int id) {
    super(x,y);

    weapon = new Weapon(x,y,id);
    damage = 10;
    maxhealth = 10;
    health = maxhealth;
    velocity = new Vector(0,0);
    playerType = id;
    speed = 0.25f;


    // Ranged player assignments
    if(id == 1) {
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_RANGEDMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_RANGEDMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_RANGEDIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_RANGEDIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      attackCooldown = 200;
    }
    // Melee player assignments
    if(id == 2) {
      moveLeft = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_MELEEMOVELEFT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      moveRight = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_MELEEMOVERIGHT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      idleLeft = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_MELEEIDLELEFT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      idleRight = new Animation(ResourceManager.getSpriteSheet(
          DungeonGame.PLAYER_MELEEIDLERIGHT_RSC, 32, 32), 0, 0, 3, 0,
          true, 100, true);
      attackCooldown = 200;
    }
    attackReady = true;
    faceRight = false;
    current = idleRight;
    addAnimation(current);
  }


  public Vector getVelocity() {
    return velocity;
  }

  public void setWorldPos(TileIndex tileIndex) {
    worldPos = MapUtil.convertTileToWorld(tileIndex);
  }

  /**+
   * Below are the 5 movement functions for allowing the player to move around the map
   * Each movement function also sets the proper animation.
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

  // For the diagonal movement, speed is scaled in each direction by 1/sqrt(2) since its at a 45 degree angle.
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
      current = idleRight;
    }
    else {
      addAnimation(idleLeft);
      current = idleLeft;
    }
  }

  /**
   * This function is for getting the coordinate of the player.
   * @return
   * A TileIndex object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public TileIndex getTileIndex() {
    return MapUtil.convertWorldToTile(worldPos);
  }


  public boolean isMoveValid(Direction direction, Vector movement, MapUtil levelMap) {
    Vector moveDirection = movement.unit();
    // this is where the player would be if the move was successful
    Vector newPlayerPos = new Vector(worldPos.x + movement.getX(), worldPos.y + movement.getY());
    moveDirection = moveDirection.scale((float)(MapUtil.TILESIZE /2));
    newPlayerPos = newPlayerPos.setX(newPlayerPos.getX() + moveDirection.getX());
    newPlayerPos = newPlayerPos.setY(newPlayerPos.getY() + moveDirection.getY());
    TileIndex newTile = MapUtil.convertWorldToTile(newPlayerPos);

    if (levelMap.hasCollision(newTile)) {
      return false;
    }
    // ensure diagonals are not blocked by neighboring tiles
    if (direction == Direction.DOWN_LEFT) {
      // check the tile above new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x, newTile.y - 1))) {
        return false;
      }
      // check the tile to the right of the new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x + 1, newTile.y))) {
        return false;
      }

    } else if (direction == Direction.DOWN_RIGHT) {
      // check the tile above new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x, newTile.y - 1))) {
        return false;
      }
      // check the tile to the left of the new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x - 1, newTile.y))) {
        return false;
      }
    } else if (direction == Direction.UP_LEFT) {
      // check the tile below new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x, newTile.y + 1))) {
        return false;
      }
      // check the tile to the right of the new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x + 1, newTile.y))) {
        return false;
      }
    } else if (direction == Direction.UP_RIGHT) {
      // check the tile below new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x, newTile.y + 1))) {
        return false;
      }
      // check the tile to the left of the new tile:
      if (levelMap.hasCollision(new TileIndex(newTile.x - 1, newTile.y))) {
        return false;
      }
    }
    return true;
  }


  /***
   * This function is to be called when the player takes damage from any source.
   * @param damage
   *  The amount of damage taken as a positive integer
   * @return
   *  true: if the player's health is 0 or less
   *  false: otherwise
   */
  public boolean damage(int damage) {
    health -= damage;
    if(health <= 0)
      return true;
    return false;
  }

  public int getCurrentHealth() { return health;}

  public int getMaxHealth() {return maxhealth;}

  public int getPlayerType() { return playerType;}

  /***
   * This function is to be called when the player fire a projectile.
   * @param angle
   * The angle to which the projectile will be fired in.
   * @return
   * The newly constructed projectile with the angle, originated at the player.
   */
  public Projectile fire(double angle) {
    // Check if were ready to attack, and only fire if so.
    if(attackReady) {
      Projectile newProjectile;
      if(playerType == 1)
        newProjectile = new Projectile(this.getX(), this.getY(), 1, angle, damage);
      else
        newProjectile = new Projectile(this.getX(), this.getY(), 3, angle, damage);
      attackReady = false;
      attackCooldownTimer = attackCooldown;
      newProjectile.worldPos = new Coordinate(worldPos.x, worldPos.y);
      return newProjectile;
    }
    return null;
  }

  /***
   * This function updates all time based things attached to the player, such as cooldowns and location.
   * @param delta
   *  Amount of time passed
   */

  public void update(final int delta) {
    Vector movement = velocity.scale(delta);
    prevMoveVelocity = new Coordinate(movement.getX(), movement.getY());
    worldPos.x += movement.getX();
    worldPos.y += movement.getY();

    // Check if were on CD, if so decrement and release if the timer is finished
    if(!attackReady) {
      attackCooldownTimer -= delta;
      if(attackCooldownTimer <= 0) {
        attackReady = true;
      }
    }
    weapon.update(getX(), getY());
  }

  /**
   * This function offsets the player's location so they aren't in walls. Call after every update.
   */
  /** Putting these here temporarily as I'm going to need these as reference when fixing things.
  public void offsetUpdate(Tile[][] tilemap) {
    // Check if any adjacent tiles are walls, and if were inside any of them. If so do an offset update.
    Coordinate location = getLocation();
    // Tile above
    if (tilemap[location.x][location.y - 1].getID() == 1 && getTileOffset().getY() >= 0) {
      translate(0, getTileOffset().getY());
    }
    // Tile Below
    if (tilemap[location.x][location.y + 1].getID() == 1 && getTileOffset().getY() <= 0) {
      translate(0, getTileOffset().getY());
    }
    // Tile Left
    if (tilemap[location.x - 1][location.y].getID() == 1 && getTileOffset().getX() >= 0) {
      translate(getTileOffset().getX(), 0);
    }
    // Tile Right
    if (tilemap[location.x + 1][location.y].getID() == 1 && getTileOffset().getX() <= 0) {
      translate(getTileOffset().getX(), 0);
    }
  }
  **/

  /**
   * This function is for calculating the offset from the center of the tile the player currently exists in.
   * @return
   * A Vector containing the x and y difference between the center of the tile and the player
   */
  /**
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
   **/


  /***
   * This function rotates the player's weapon in the given direction. Meant to be used towards the mouse and
   * called every update.
   * @param theta
   *  The absolute angle to rotate to.
   */
  public void mouseRotate(final double theta) {
    weapon.setRotation(theta);
  }
}
