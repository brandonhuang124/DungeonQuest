package Project2;

import jig.ResourceManager;
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
  LinkedList<Enemy> enemyList;
  Tile tileMap[][];
  Vertex [][] path;
  int levelWidth, levelHeight;

  @Override
  public int getID() {
    return DungeonGame.TESTSTATE;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    // See below method for details on construction.
    initializeLevel(1);
    projectileList = new LinkedList<Projectile>();
    enemyList = new LinkedList<Enemy>();
    enemyList.add(new Enemy(DungeonGame.TILESIZE * 18, DungeonGame.TILESIZE * 5, 1));
    player = new Player((DungeonGame.TILESIZE * 4) + (0.5f * DungeonGame.TILESIZE),
        (DungeonGame.TILESIZE * 4) + (0.5f * DungeonGame.TILESIZE));
    container.setSoundOn(true);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    DungeonGame rg = (DungeonGame)game;

    // Render tiles
    for(int y = 0;  y < levelHeight; y++) {
      for(int x = 0; x < levelWidth; x++) {
        Tile temp = tileMap[x][y];
        // Floor tile
        if(temp.getID() == 0) {
          g.drawImage(ResourceManager.getImage(DungeonGame.MAP_FLOOR_RSC).getScaledCopy(DungeonGame.SCALE),
              x * DungeonGame.TILESIZE, y * DungeonGame.TILESIZE);
        }
        // Wall tile
        else if(temp.getID() == 1) {
          g.drawImage(ResourceManager.getImage(DungeonGame.MAP_WALL_RSC).getScaledCopy(DungeonGame.SCALE),
              x * DungeonGame.TILESIZE, y * DungeonGame.TILESIZE);
        }
      }
    }

    // Render Dijkstras Overlay if needed
    /***
     * Directions are listed by a integer in the overlay corresponding to the direction on the numpad
     * 2: down, 4: left, 6: right, 8: up
     * 1: down left, 3: down right, 7: up left, 9: up right
      */
    if(true) {
      if(path != null) {
        for(int x = 0; x < levelWidth; x++) {
          for(int y = 0; y < levelHeight; y++) {
            if(path[x][y].getDistance() < 1000) {
              //g.drawString("" + path[x][y].getDistance(), (x * DungeonGame.TILESIZE) + 5, (y * DungeonGame.TILESIZE) + 20);
              g.drawString("" + path[x][y].getDirection(), (x * DungeonGame.TILESIZE) + 5, (y * DungeonGame.TILESIZE) + 8);
            }
          }
        }
      }
    }
    // Render projectiles on the screen
    for(Projectile p : projectileList) {
      p.render(g);
    }

    // Render Entities
    for(Enemy enemy : enemyList) {
      enemy.render(g);
    }
    player.render(g);
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    Input input = container.getInput();
    DungeonGame dg = (DungeonGame)game;

    // Methods called at the start of every update for usage in the loop
    Coordinate playerloc = player.getLocation();
    path = DungeonGame.getDijkstras(playerloc.x,playerloc.y,tileMap, levelWidth, levelHeight);

    /*** CONTROLS SECTION ***/
    // Left click for attacking
    if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
      System.out.println("LClick pressed");
      projectileList.add(player.fire(getPlayerMouseAngle(input)));
      for(Projectile p: projectileList) {
        System.out.println(p);
      }
    }

    // Check diagonols first
    // W and A for Up Left
    if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(7, tileMap)) {
      player.moveUpLeft();
    }
    // W and D for Up Right
    else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(9, tileMap)) {
      player.moveUpRight();
    }
    // S and A for Down Left
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(1, tileMap)) {
      player.moveDownLeft();
    }
    // S and D for Down Right
    else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(3, tileMap)) {
      player.moveDownRight();
    }
    // W for moving up
    else if(input.isKeyDown(Input.KEY_W) && player.isMoveValid(8, tileMap)) {
      player.moveUp();
    }
    // A for moving left
    else if(input.isKeyDown(Input.KEY_A) && player.isMoveValid(4, tileMap)) {
      player.moveLeft();
    }
    // S for moving down
    else if(input.isKeyDown(Input.KEY_S) && player.isMoveValid(2, tileMap)) {
      player.moveDown();
    }
    // D for moving right
    else if(input.isKeyDown(Input.KEY_D) && player.isMoveValid(6, tileMap)) {
      player.moveRight();
    }
    else {
      player.stop();
    }


    // Update All enemies
    for(Enemy enemy : enemyList) {
      enemy.makeMove(tileMap, path, player);
      enemy.update(delta);
      enemy.offsetUpdate(tileMap);
    }

    // Update the player model
    player.mouseRotate(getPlayerMouseAngle(input));
    player.update(delta);

    // Now offset if were near a wall so no in the wall happens
    player.offsetUpdate(tileMap);

    // Update projectiles
    for(Projectile p : projectileList) {
      p.update(delta);
    }
    // Collision check for projectiles
    for(Projectile projectile : projectileList) {
      projectile.collisionCheck(tileMap);
    }
    // Remove Projetiles that have collided with objects.
    projectileList.removeIf( (Projectile projectile) -> projectile.needsRemove());
  }

  public double getPlayerMouseAngle(Input input) {
    float mousex = input.getMouseX();
    float mousey = input.getMouseY();
    float playerx = player.getX();
    float playery = player.getY();
    Vector angleVector = new Vector(mousex - playerx, mousey - playery);
    return angleVector.getRotation();
  }

  /***
   * Internal function for construction of levels based on id.
   * @param level
   *  level number for which to construct.
   */
  private void initializeLevel(int level) {
    levelWidth = 20;
    levelHeight = 20;
    tileMap = DungeonGame.getTileMap(
        "11111111111111111111" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000011111100000001" +
            "10000011111100000001" +
            "10000000000000000001" +
            "10000000000000000001" +
            "10000011111100000001" +
            "10000011111100000001" +
            "10000000110000000001" +
            "10000000110000000001" +
            "10000011111100000001" +
            "10000011111100000001" +
            "10000000000000000001" +
            "10000000000000000001" +
            "11111111111111111111",
        levelWidth,levelHeight);
  }
}
