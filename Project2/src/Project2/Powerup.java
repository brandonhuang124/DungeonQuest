package Project2;

import jig.Entity;
import jig.ResourceManager;

import java.util.ArrayList;

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
    if(id == 2) {
      addImage(ResourceManager.getImage(DungeonGame.POWERUP_SELFREVIVE_RSC));
    }
    if(id == 3) {
      addImage(ResourceManager.getImage(DungeonGame.POWERUP_INVINCIBILITY_RSC));
    }
    if(id == 4) {
      addImage(ResourceManager.getImage(DungeonGame.POWERUP_DOUBLESTRENGTH_RSC));
    }
  }

  public boolean playerCollision(Player player) {
    TileIndex playerLoc = player.getTileIndex();
    if(playerLoc.x == location.x && playerLoc.y == location.y) {
      // Do stuff based on which powerup we are
      if(id == 1)
        healthPotion(player);
      if(id == 2)
        selfRevive(player);
      if(id == 3)
        invincible(player);
      if(id == 4)
        doubleStrength(player);
      removeMe = true;
      return true;
    }
    return false;
  }

  private void healthPotion(Player player) {
    player.maxHeal();
  }

  private void selfRevive(Player player) {
    player.setSelfRevive(true);
  }

  private void invincible(Player player) {
    player.setInvincible(true);
  }

  private void doubleStrength(Player player) {
    player.setDoubleStrength(true);
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
  public static ArrayList<Powerup> buildPowerUpList() {
    ArrayList<Powerup> powerUpList = new ArrayList<Powerup>();
    if(MapUtil.levelName == LevelName.TWO) {
      powerUpList.add(new Powerup(19, 4, 1));
      powerUpList.add(new Powerup(49, 31, 1));
      powerUpList.add(new Powerup(18, 40, 1));
      powerUpList.add(new Powerup(12, 40, 1));
      powerUpList.add(new Powerup(54, 42, 1));
      powerUpList.add(new Powerup(6,  57, 1));
    }
    if(MapUtil.levelName == LevelName.THREE) {
      powerUpList.add(new Powerup(10, 15, 1));
      powerUpList.add(new Powerup(20, 20, 1));
      powerUpList.add(new Powerup(40, 26, 1));
    }
    return powerUpList;
  }
}
