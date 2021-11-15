package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;

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
  }

  public void update(final int delta) {
    translate(velocity.scale(delta));
  }

  public int getID() { return id;}
}
