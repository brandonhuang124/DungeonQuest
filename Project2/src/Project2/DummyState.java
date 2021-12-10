package Project2;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.ArrayList;

public class DummyState extends BasicGameState {
  MapUtil levelMap;
  Player meleePlayer, rangedPlayer;

  @Override
  public int getID() {
    return 4;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    // parse the CSV map file, throw exception in case of IO error:
    try {
      levelMap.loadLevelMap(1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    meleePlayer = new Player(0,0, 2);
    rangedPlayer = new Player(0,0,1);
    // keeping the string to matrix method in DungeonGame
    levelMap.currentTileMap  = DungeonGame.getTileMap(levelMap.currentMapString,
        MapUtil.LEVELWIDTH,MapUtil.LEVELWIDTH);

    container.setSoundOn(true);

  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    levelMap.renderMapByCamera(g);

  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    // Important calls at the beginning of each update
    Input input = container.getInput();
    String data;
    String[] dataToken = null;
    // Read render data from client
    try {
      data = DungeonGame.client.dataInputStream.readUTF();
      dataToken = data.split(";");
      for(int i = 0; i < dataToken.length; i++)
        System.out.println(dataToken[i]);
    } catch (IOException e) {
      System.out.println("IOException from run() in ClientHandler");
      e.printStackTrace();
    }

    /*** CONTROLS SECTION ***/
    data = "";
    // Left click for attacking: Send 1 for yes we attacked, and 0 for no we didn't
    if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
      data = data.concat("1;");
    else
      data = data.concat("0;");
    // Check diagonals first
    // W and A for Up Left
    if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)) {
      data = data.concat("WA;");
    }
    // W and D for Up Right
    else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)) {
      data = data.concat("WD;");
    }
    // S and A for Down Left
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)) {
      data = data.concat("SA;");

    }
    // S and D for Down Right
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)) {
      data = data.concat("SD;");
    }
    // W for moving up
    else if(input.isKeyDown(Input.KEY_W)) {
      data = data.concat("W;");
    }
    // A for moving left
    else if(input.isKeyDown(Input.KEY_A)) {
      data = data.concat("A;");
    }
    // S for moving down
    else if(input.isKeyDown(Input.KEY_S)) {
      data = data.concat("S;");
    }
    // D for moving right
    else if(input.isKeyDown(Input.KEY_D)) {
      data = data.concat("D;");
    }
    // Other wise were just gonna stop moving.
    else {
      data = data.concat("NA;");
    }


    // Write control data
    try {
      DungeonGame.client.dataOutputStream.writeUTF(data);
      DungeonGame.client.dataOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void parseRenderData(String[] token) {

  }
}
