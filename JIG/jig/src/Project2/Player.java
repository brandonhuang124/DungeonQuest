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
	
 /* values for determining current
  * tiles that the player is standing on:
  */
  public int tileX;
  public int tileY;
  
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
   // addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.TEST_PLAYER_MELEE_RSC));
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
	/***Helpful methods for player tile based movement***
	 * Converts tile to screen position and returns the vector:
	 */
	public static Vector convertTileToPos(int tileY, int tileX)
	{
		float posY = tileY * DungeonGame.TILESCALE;
		float posX = tileX * DungeonGame.TILESCALE;
		return new Vector(posX, posY);
	}
  
	/*
	 * here we can move the player by screen position by desired tile:
	 */
	public void movePlayer(int newTileY, int newTileX)
	{
		tileY = newTileY;
		tileX = newTileX;
		Vector position = convertTileToPos(tileY, tileX);
		this.setY(position.getY());
		this.setX(position.getX());
	}	
	/* Setting a new velocity:
	 */
	public void setVelocity(final Vector v) 
	{	
		velocity = v;
	}
  
}
