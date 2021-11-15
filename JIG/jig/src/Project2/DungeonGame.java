package Project2;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 *
 * Description:
 *
 * Controls:
 *
 * States:
 *
 * Graphical Asset Credits:
 *
 * Sound Credits:
 *
 * @author
 *
 */

public class DungeonGame extends StateBasedGame {
  // States (TODO organize states upon final release)
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;
  public static final int LEVEL1 = 2;
  public static final int GAMEOVERSTATE = 4;
  
  /*** ASSET PATHS ***/
  public static final String PLAYER_ARROWTEST_RSC = "Project2/Assets/arrow.png";
  public static final String PLAYER_PROJECTILE_RSC = "Project2/Assets/projectile.png";
  // stationary melee sprite for initial map testing purposes:
  public static final String TEST_PLAYER_MELEE_RSC = "Project2/Assets/testMeleePlayer.png";
  public static final String TEST_TILE_COLLISION_RSC = "Project2/Assets/tileCollisionTest.png";

  // Parameters
  public final int ScreenWidth;
  public final int ScreenHeight;
  
  // the size of our tiles:
  public final static int TILESCALE= 50;
  /**
   * Create a new state based game
   *
   * @param title The name of the game
   */
  //TODO add logic for a second player:
  Player player1;
  
  public DungeonGame(String title, int width, int height) {
    super(title);
    ScreenHeight = height;
    ScreenWidth = width;

    Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);
  }

  @Override
  public void initStatesList(GameContainer container) throws SlickException {
    // Load states
    addState(new StartState());
    addState(new TestState());
    addState(new Level1());
    /*** RESOURCE LOADING ***/
    ResourceManager.loadImage(PLAYER_ARROWTEST_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILE_RSC);
    // stationary melee sprite for initial map testing purposes:
    ResourceManager.loadImage(TEST_PLAYER_MELEE_RSC);
    // tile collision test image:
    ResourceManager.loadImage(TEST_TILE_COLLISION_RSC);
  }

  public static void main(String[] args) {
    AppGameContainer app;
    // Open the game
    try {
      app = new AppGameContainer(new DungeonGame("Project2", 1000,1000));
      app.setDisplayMode(750,850, false);
      app.setVSync(true);
      app.setTargetFrameRate(60);
      app.start();
    } catch(SlickException e) {
      e.printStackTrace();
    }
  }

}
