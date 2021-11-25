package Project2;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/***
 * Description:
 *
 * Transitions From
 *
 * Transitions To TestState
 */


public class StartState extends BasicGameState {

  // used to toggle between level1 and teststate transition:
  // set to true to go back to test state.
  private boolean debug = false;
  private int select, timer;
  private boolean selected, arrowBlink, musicRestart;
  Animation player, melee, melee2, ranged;

  @Override
  public int getID() {
    return DungeonGame.STARTUPSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
    musicRestart = true;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    DungeonGame rg = (DungeonGame)game;
    container.setSoundOn(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    Input input = container.getInput();
    DungeonGame rg = (DungeonGame)game;
    if(debug)
      rg.enterState(DungeonGame.TESTSTATE);
    else
      rg.enterState(DungeonGame.LEVEL1);
  }
}
