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
