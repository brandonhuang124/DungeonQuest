package Project2;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Weapon extends Entity {
  public Weapon(final float x, final float y, int id) {
    super(x,y);
    if(id == 1) {
     addImageWithBoundingBox(ResourceManager.getImage(DungeonGame.PLAYER_RANGEDBOW1_RSC));
    }
  }

  public void update(final float x, final float y) {
    setX(x);
    setY(y+5);
  }

  public void mouseRotate(final double theta) {
    setRotation(theta);
  }
}
