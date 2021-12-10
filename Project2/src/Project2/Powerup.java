package Project2;

import jig.Entity;
import jig.ResourceManager;

public class Powerup extends Entity {
  TileIndex location;
  private boolean removeMe;
  private int id;
  public Coordinate worldPos;

  /***
   * Constructor
   * @param x
   *  x tile coordinate of pickup in tilemap coordinates
   * @param y
   *  y tile coordinate of pickup in tilemap coordinates
   * @param newid
   *  id of the pickup, options:
   *    1: Armor, restores health to full.
   */
  public Powerup(int x, int y, int newid) {
    location = new TileIndex(x,y);
    TileIndex tileIndex = new TileIndex(x,y);
    worldPos = MapUtil.convertTileToWorld(tileIndex);
    id = newid;
    removeMe = false;
    if(id == 1) {
      addImage(ResourceManager.getImage(DungeonGame.POWERUP_HEALTHPOTION_RSC));
    }
  }

  public boolean playerCollision(Player player) {
    TileIndex playerLoc = player.getTileIndex();
    if(playerLoc.x == location.x && playerLoc.y == location.y) {
      // Do stuff based on which powerup we are
      if(id == 1)
        healthPotion(player);
      removeMe = true;
      return true;
    }
    return false;
  }

  private void healthPotion(Player player) {
    player.maxHeal();
  }

  public int getId() { return id;}

  public TileIndex getLocation() { return location; }

  public boolean getRemoveMe() { return removeMe; }

  /***
   * Function to get render data to send to the second player in 2P mode.
   * In the format: 'TYPE;XPOS;YPOS'
   * @return
   * A string to be sent to the dummy client for rendering.
   */
  public String getData() {
    String data = "";
    data = data.concat(id + ";");
    data = data.concat(worldPos.x + ";" + worldPos.y + ";");

    return data;
  }
}
