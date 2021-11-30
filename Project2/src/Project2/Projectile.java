package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.LinkedList;

/***
 * Enitity class for reprsenting projectiles. Tied to an ID:
 *  ID = 1: Ranged Player projectile
 *  ID = 2: Ranged Enemy projectile
 * Methods:
 *
 */

public class Projectile extends Entity {
  private float speed;
  private Vector velocity;
  private boolean removeMe;
  int id, damage;

  /***
   * Constructor
   * @param x
   *  x location to spawn the projectile
   * @param y
   *  y location to spawn the projectile
   * @param type
   *  projectile type:
   *    type 1: Ranged Player projectile
   * @param angle
   *  Angle at which the projectile is fire in.
   */
  public Projectile (final float x, final float y, int type, double angle, int damageAmount) {
    super(x,y);
    id = type;
    damage = damageAmount;
    // If player projectile
    if(id == 1) {
      speed = 1f;
      addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_RANGEDARROW1_RSC));
    }
    // If enemy Projectile
    else if(id == 2) {
      speed = 0.5f;
      addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_PROJECTILE_RSC));
    }

    velocity = new Vector(speed,0);
    velocity = velocity.setRotation(angle);
    setRotation(angle);
    removeMe = false;
  }

  /**
   * This function is for collision checking the projectile, should be called every update.
   * Marks the removeMe field if the projectile needs to be removed.
   * @param tilemap
   *  The tilemap representing the layout of the map.
   */
  public void collisionCheck(Tile[][] tilemap, LinkedList<Enemy> enemyList, Player player) {
    Coordinate location = getLocation();
    // Always do a wall check
    if(tilemap[location.x][location.y].getID() == 1) {
      removeMe = true;
    }
    // If were a player projectile, do an enemy check
    if(id == 1) {
      for(Enemy enemy : enemyList) {
        Coordinate enemyLocation = enemy.getLocation();
        if(enemyLocation.x == location.x && enemyLocation.y == location.y) {
          removeMe = true;
          enemy.damage(10);
        }
      }
    }
    // If were an enemy projectile, do a player check
    if(id == 2) {
      Coordinate playerLocation = player.getLocation();
      if(playerLocation.x == location.x && playerLocation.y == location.y) {
        player.damage(2);
        System.out.println("Player hit by projectile: " + player.getCurrentHealth());
        removeMe = true;
      }
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

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public boolean needsRemove() {return removeMe;}

  public int getID() { return id;}
}
