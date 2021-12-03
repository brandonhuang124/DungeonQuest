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
    private static final String TAG = "Player -";
    private Vector velocity;
    private float speed;

    public Coordinate worldPos;
    public Coordinate prevMoveVelocity;


    /***
     * Constructor, prepares default stats and Images/anmiations
     * @param x
     *  x Coordinate to spawn the player in
     * @param y
     *  y coordinate to spawn the player in
     */
    public Player(final float x, final float y) {
        super(x, y);
        addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_ARROWTEST_RSC));
        velocity = new Vector(0, 0);
        speed = 0.25f;
    }

    public void setWorldPos(TileIndex tileIndex) {
        worldPos = MapUtil.convertTileToWorld(tileIndex);
    }

    public Vector getVelocity() {
        return velocity;
    }

    /**
     * +
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

    // For the diagonal movement, speed is scaled in each direction by 1/sqrt(2) since its at a 45 degree angle.
    public void moveDownRight() {
        velocity = new Vector(0.71f * speed, 0.71f * speed);
    }

    public void moveDownLeft() {
        velocity = new Vector(-0.71f * speed, 0.71f * speed);
    }

    public void moveUpRight() {
        velocity = new Vector(0.71f * speed, -0.71f * speed);
    }

    public void moveUpLeft() {
        velocity = new Vector(-0.71f * speed, -0.71f * speed);
    }

    public void stop() {
        velocity = new Vector(0, 0);
    }

    /**
     * This function is for getting the coordinate of the player.
     *
     * @return A Coordinate object with an x and y field representing the location in the tilemap the player currently exists in.
     */
    public TileIndex getTileIndex() {
        int x = Math.round((this.getX() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
        int y = Math.round((this.getY() - MapUtil.TILESIZE / 2) / MapUtil.TILESIZE);
        return new TileIndex(x, y);
    }

    /**
     * This function is for calculating the offset from the center of the tile the player currently exists in.
     *
     * @return A Vector containing the x and y difference between the center of the tile and the player
     */
    public Vector getTileOffset(MapUtil levelMap) {
        TileIndex location = getTileIndex();
        // The center of the tile location is (Tile * tilewidth) + 1/2 tile width, since the entity's origin is the center.
        float tilex = (location.x * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
        float tiley = (location.y * MapUtil.TILESIZE) + (MapUtil.TILESIZE / 2);
        float x = this.getX();
        float y = this.getY();
        // Return the offset from the center
        return new Vector(tilex - x, tiley - y);
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
        Vector movement = velocity.scale(delta);
        prevMoveVelocity = new Coordinate(movement.getX(), movement.getY());
        worldPos.x += movement.getX();
        worldPos.y += movement.getY();
    }

    /**
     * This function offsets the player's location so they aren't in walls. Call after every update.
     */
    public void offsetUpdate(MapUtil levelMap) {
        // Check if any adjacent tiles are walls, and if were inside any of them. If so do an offset update.
        TileIndex location = getTileIndex();
        // Tile above
        if (levelMap.currentTileMap != null) {
            if (levelMap.currentTileMap[location.x][location.y - 1].getID() == 1 && getTileOffset(levelMap).getY() >= 0) {
                translate(0, getTileOffset(levelMap).getY());
            }
            // Tile Below
            if (levelMap.currentTileMap[location.x][location.y + 1].getID() == 1 && getTileOffset(levelMap).getY() <= 0) {
                translate(0, getTileOffset(levelMap).getY());
            }
            // Tile Left
            if (levelMap.currentTileMap[location.x - 1][location.y].getID() == 1 && getTileOffset(levelMap).getX() >= 0) {
                translate(getTileOffset(levelMap).getX(), 0);
            }
            // Tile Right
            if (levelMap.currentTileMap[location.x + 1][location.y].getID() == 1 && getTileOffset(levelMap).getX() <= 0) {
                translate(getTileOffset(levelMap).getX(), 0);
            }
        } else {
            System.out.println(TAG + "TileMap is null");
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
