package Project2;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

public class WinState extends BasicGameState {
    @Override
    public int getID() {
        return DungeonGame.WIN;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }
    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        MapUtil.levelName = LevelName.WIN;

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawString("You have cleared The Dungeon!", 200, 300);
        g.drawString("Would you like to start a new game?", 200, 330);
        g.drawString("[y] Yes | [n] No ", 240, 360);

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
