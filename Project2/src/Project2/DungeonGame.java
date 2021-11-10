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
  // States
  public static final int STARTUPSTATE = 0;
  public static final int TESTSTATE = 1;

  /*** ASSET PATHS ***/
  public static final String PLAYER_ARROWTEST_RSC = "Project2/Assets/arrow.png";
  public static final String PLAYER_PROJECTILE_RSC = "Project2/Assets/projectile.png";

  // Parameters
  public final int ScreenWidth;
  public final int ScreenHeight;
  /**
   * Create a new state based game
   *
   * @param title The name of the game
   */
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

    /*** RESOURCE LOADING ***/
    ResourceManager.loadImage(PLAYER_ARROWTEST_RSC);
    ResourceManager.loadImage(PLAYER_PROJECTILE_RSC);
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
