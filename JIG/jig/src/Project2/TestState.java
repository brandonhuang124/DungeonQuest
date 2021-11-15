package Project2;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;

/***
 * Description:
 *
 * Transitions From StartState
 *
 * Transitions To
 */
public class TestState extends BasicGameState {
  Player player;
  LinkedList<Projectile> projectileList;

  @Override
  public int getID() {
    return DungeonGame.TESTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    projectileList = new LinkedList<Projectile>();
    player = new Player(250,250);
    container.setSoundOn(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    DungeonGame rg = (DungeonGame)game;
    for(Projectile p : projectileList) {
      p.render(g);
    }
    player.render(g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    Input input = container.getInput();
    DungeonGame dg = (DungeonGame)game;


    /*** CONTROLS SECTION ***/
    if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
      System.out.println("LClick pressed");
      projectileList.add(player.fire(getPlayerMouseAngle(input)));
      for(Projectile p: projectileList) {
        System.out.println(p);
      }
    }

    if(input.isKeyDown(Input.KEY_W)) {
      System.out.println("w pressed");
      player.moveUp();
    }
    else if(input.isKeyDown(Input.KEY_A)) {
      System.out.println("a pressed");
      player.moveLeft();
    }
    else if(input.isKeyDown(Input.KEY_S)) {
      System.out.println("s pressed");
      player.moveDown();
    }
    else if(input.isKeyDown(Input.KEY_D)) {
      System.out.println("d pressed");
      player.moveRight();
    }
    else {
      player.stop();
    }

    if(input.isKeyPressed(Input.KEY_LEFT)) {
      player.mouseRotate(-45.0);
    }
    else if (input.isKeyPressed(Input.KEY_RIGHT)) {
      player.mouseRotate(45.0);
    }

    player.mouseRotate(getPlayerMouseAngle(input));
    player.update(delta);
    for(Projectile p : projectileList) {
      p.update(delta);
    }
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
