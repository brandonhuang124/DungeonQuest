package Project2;

import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;

public class Level1 extends BasicGameState {
    Player player;
    LinkedList<Projectile> projectileList;
    Tile tileMap[][];
    Vertex [][] path;
    int level2_width = 20;
    int level1_height = 20;


    @Override
    public int getID() {
        return DungeonGame.LEVEL1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        projectileList = new LinkedList<Projectile>();
        player = new Player(250,250);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

    public double getPlayerMouseAngle(Input input) {
        float mousex = input.getMouseX();
        float mousey = input.getMouseY();
        float playerx = player.getX();
        float playery = player.getY();
        Vector angleVector = new Vector(mousex - playerx, mousey - playery);
        return angleVector.getRotation();
    }

}
