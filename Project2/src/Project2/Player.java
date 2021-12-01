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
  private Vector velocity;
  private float speed;
  private int damage, maxhealth, health, attackCooldownTimer, attackCooldown, playerType;
  private boolean faceRight, attackReady;
  private Animation moveLeft, moveRight, idleLeft, idleRight, current;
  public Weapon weapon;

  /***
   * Constructor, prepares default stats and Images/anmiations
   * @param x
   *  x Coordinate to spawn the player in
   * @param y
   *  y coordinate to spawn the player in
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
   * @param tilemap
   *  The tilemap representing the level layout.
   * @return
   * A boolean showing if the move is valid or not.
   */
  public boolean isMoveValid(int direction, Tile[][] tilemap) {
    Coordinate location = getLocation();
    boolean adjacencyCheck = false;
    // Diagonol directions must check both the directions they are the diagonal of and the diagonal tile.
    // Down
    if(direction == 2 || direction == 1 || direction == 3) {
      // Check if the tile left is a wall
      if(tilemap[location.x][location.y+1].getID() == 1) {
        // If it is, we need to check were not too far into the tile where we will go into the wall.
        Vector offset = getTileOffset();
        adjacencyCheck = true;
        if(offset.getY() <= 0)
          return false;
      }
    }
    // Left
    // ** Process for checking is similar to above, but directions and tiles checked are changed.
    if (direction == 4 || direction == 1 || direction == 7) {
      if(tilemap[location.x-1][location.y].getID() == 1) {
        Vector offset = getTileOffset();
        adjacencyCheck = true;
        if(offset.getX() >= 0)
          return false;
      }
    }
    // Right
    if (direction == 6 || direction == 9 || direction == 3) {
      if(tilemap[location.x+1][location.y].getID() == 1) {
        Vector offset = getTileOffset();
        adjacencyCheck = true;
        if(offset.getX() <= 0)
          return false;
      }
    }
    // Up
    if (direction == 8 || direction == 7 || direction == 9) {
      if(tilemap[location.x][location.y-1].getID() == 1) {
        Vector offset = getTileOffset();
        adjacencyCheck = true;
        if(offset.getY() >= 0)
          return false;
      }
    }

    // We only want to do the diagnol check if none of the adjacent tiles are walls, so hiccup movments don't happen.
    // ** Process is similar to cardinal direction checks, but we must check x AND y offsets to ensure we won't
    // enter the tile.
    if(!adjacencyCheck) {
      // Up Right
      if (direction == 9) {
        if(tilemap[location.x+1][location.y-1].getID() == 1) {
          Vector offset = getTileOffset();
          if(offset.getY() >= 0 || offset.getX() <= 0)
            return false;
        }
      }
      // Up Left
      else if (direction == 7) {
        if(tilemap[location.x-1][location.y-1].getID() == 1) {
          Vector offset = getTileOffset();
          if(offset.getY() >= 0 || offset.getX() >= 0)
            return false;
        }
      }
      // Down Right
      else if (direction == 3) {
        if(tilemap[location.x+1][location.y+1].getID() == 1) {
          Vector offset = getTileOffset();
          if(offset.getY() <= 0 || offset.getX() <= 0)
            return false;
        }
      }
      // Down Left
      else if (direction == 1) {
        if(tilemap[location.x-1][location.y+1].getID() == 1) {
          Vector offset = getTileOffset();
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
    // Check if were on CD, if so decrement and release if the timer is finished
    if(!attackReady) {
      attackCooldownTimer -= delta;
      if(attackCooldownTimer <= 0) {
        attackReady = true;
      }
    }
    weapon.update(getX(), getY());
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
    if(tilemap[location.x + 1][location.y].getID() == 1 && getTileOffset().getX() <= 0) {
      translate(getTileOffset().getX(), 0 );
    }
  }

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
