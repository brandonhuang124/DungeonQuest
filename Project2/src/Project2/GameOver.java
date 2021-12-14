package Project2;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameOver extends BasicGameState {
    @Override
    public int getID() { // state id = 6
        return DungeonGame.GAMEOVER;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
      MapUtil.setLevelName(LevelName.GAMEOVER);
      // If we were playing in 2 player mode we should disconnect at this point.
      DungeonGame dg = (DungeonGame) game;
      if(((Level1)game.getState(DungeonGame.LEVEL1)).twoPlayer) {
        DungeonGame.client.disconnect();
      }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
      g.drawString("Ruh roh game over", 200, 300);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
      Input input = container.getInput();
      if(input.isKeyDown(Input.KEY_Y)){
        game.enterState(DungeonGame.STARTUPSTATE, new FadeOutTransition(), new FadeInTransition());
      }
      if(input.isKeyDown(Input.KEY_N)){
        container.exit();
      }
    }
}
