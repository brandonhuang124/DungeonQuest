package Project2;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;

import java.util.Map;

/**
 * This state is used to transition back into Entering the playing state.
 */
public class TransitionState extends BasicGameState {

    Boolean playerWon = false;

    private int timer;

    @Override // TRANSITION = 7
    public int getID() {
        return DungeonGame.TRANSITION;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game)
    {
        // switching levels:
        timer = 1000;
        if(MapUtil.levelName == LevelName.MENU) {
            MapUtil.levelName = LevelName.ONE;
        }
        else if(MapUtil.levelName == LevelName.ONE)
            MapUtil.setLevelName(LevelName.TWO);
        else if(MapUtil.levelName == LevelName.TWO){
            MapUtil.setLevelName(LevelName.THREE);
        }else if(MapUtil.levelName == LevelName.THREE){
            playerWon = true;
        }

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException
    {
        timer -= delta;
        if (timer <= 0)
            if(!playerWon) {
                game.enterState(DungeonGame.LEVEL1, new EmptyTransition(), new HorizontalSplitTransition());
            }else{
                game.enterState(DungeonGame.WIN, new EmptyTransition(), new HorizontalSplitTransition());
            }
    }
}
