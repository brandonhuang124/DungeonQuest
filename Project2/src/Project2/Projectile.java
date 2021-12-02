package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/***
 * Enitity class for reprsenting projectiles. Tied to an ID:
 *  ID = 1: Ranged Player projectile
 *
 * Methods:
 *
 */

public class Projectile extends Entity {
  private float speed;
  private Vector velocity;
  private boolean removeMe;
  int id;

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
  public Projectile (final float x, final float y, int type, double angle) {
    super(x,y);
    speed = 1f;
    id = type;
    velocity = new Vector(speed,0);
    velocity = velocity.setRotation(angle);
    addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_PROJECTILE_RSC));
    setRotation(angle);
    removeMe = false;
  }

  /**
   * This function is for collision checking the projectile, should be called every update.
   * Marks the removeMe field if the projectile needs to be removed.
   * @param tilemap
   *  The tilemap representing the layout of the map.
   */
  public void collisionCheck(Tile[][] tilemap) {
    TileIndex location = getLocation();
    if(tilemap[location.x][location.y].getID() == 1) {
      removeMe = true;
    }
  }

  /**
   * This function is for getting the coordinate of the player.
   * @return
   * A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
   */
  public TileIndex getLocation() {
    int x = Math.round((this.getX() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    int y = Math.round((this.getY() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
    return new TileIndex(x,y);
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public boolean needsRemove() {return removeMe;}

  public int getID() { return id;}
}
