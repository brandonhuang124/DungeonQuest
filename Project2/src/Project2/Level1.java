package Project2;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.LinkedList;

/***
 * Description:
 *
 * Transitions From StartState
 *
 * Transitions To
 */
public class Level1 extends BasicGameState {
    Player player;
    LinkedList<Projectile> projectileList;
    LinkedList<Enemy> enemyList;
    Vertex [][] path;
    MapUtil levelMap;
    int player1type, player2type;;
    boolean player1Dead, player2Dead, gameover, twoPlayer;

    @Override
    public int getID() {
        return DungeonGame.LEVEL1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        player1type = player2type = 0;
        levelMap = new MapUtil();
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        if(player1type == 0)
          player1type = 1;
        player1Dead = player2Dead = gameover = false;
        // parse the CSV map file, throw exception in case of IO error:
        try {
            levelMap.loadLevelMap(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // keeping the string to matrix method in DungeonGame
        levelMap.currentTileMap  = DungeonGame.getTileMap(levelMap.currentMapString,
                MapUtil.LEVELWIDTH,MapUtil.LEVELWIDTH);
        projectileList = new LinkedList<Projectile>();
        player = new Player( 0, 0, player1type);
        player.setWorldPos(new TileIndex(4,4));
        enemyList = new LinkedList<Enemy>();
        enemyList.add(new Enemy(0, 0, 2));
        enemyList.add(new Enemy(0, 0, 1));
        for(Enemy enemy : enemyList) {
            if(enemy.id == 1){
                enemy.setWorldPos(new TileIndex(10,10));
            }else if(enemy.id == 2){
                enemy.setWorldPos(new TileIndex(18,18));
            }else{
                enemy.setWorldPos(new TileIndex(20,20));
            }

        }

        container.setSoundOn(true);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        DungeonGame rg = (DungeonGame)game;

        // Render tiles
        levelMap.renderMapByCamera(g);

        // Render projectiles on the screen
        for(Projectile p : projectileList) {
            Coordinate pScreenPos = levelMap.convertWorldToScreen(p.worldPos);
            p.setX(pScreenPos.x);
            p.setY(pScreenPos.y);
            p.render(g);
        }
        for(Enemy enemy : enemyList) {
            Coordinate enemyScreenPos = levelMap.convertWorldToScreen(enemy.worldPos);
            enemy.setX(enemyScreenPos.x);
            enemy.setY(enemyScreenPos.y);
            enemy.render(g);
        }

        Coordinate playerScreenPos = levelMap.convertWorldToScreen(player.worldPos);
        player.setX(playerScreenPos.x);
        player.setY(playerScreenPos.y);
        player.render(g);
        player.weapon.render(g);

      // Render HUD
      if(player.getPlayerType() == 1)
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTRANGED_RSC), 20, 640);
      else if(player.getPlayerType() == 2)
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_PARCHMENTMELEE_RSC), 20, 640);

      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_P1_RSC), 5, 640);
      g.drawImage(ResourceManager.getImage(DungeonGame.HUD_DIVIDER_RSC), 276, 640);

      // Render Left cap of health bar
      if(player.getCurrentHealth() > 0)
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARL_RSC), 152, 660);
      else
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARL_RSC), 152, 660);
      // Render middle of bar
      for(int i = 1; i < player.getMaxHealth(); i++) {
          if(i <= player.getCurrentHealth())
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBAR_RSC), 152 + (i*6), 660);
        else
            g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBAR_RSC), 152 + (i*6), 660);
      }
      // Render Right cap of health bar
      if(player.getCurrentHealth() == player.getMaxHealth())
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_GBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);
      else
          g.drawImage(ResourceManager.getImage(DungeonGame.HUD_RBARR_RSC), 152 + (player.getMaxHealth() * 6), 660);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        TileIndex playerTile = levelMap.convertWorldToTile(player.worldPos);
        path = DungeonGame.getDijkstras(playerTile.x,playerTile.y, levelMap);

        /*** CONTROLS SECTION ***/
        // Left click for attacking
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            System.out.println("Left Click pressed");
            Projectile newProjectile = player.fire(getPlayerMouseAngle(input));
            if(newProjectile != null)
                projectileList.add(newProjectile);
            for(Projectile p: projectileList) {
                System.out.println(p);
            }
        }
        // Check diagonals first
        // W and A for Up Left
        Direction direction = Direction.NONE;
        if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A)) {
            direction = Direction.UP_LEFT;
            player.moveUpLeft();
        }
        // W and D for Up Right
        else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D)) {
            direction = Direction.UP_RIGHT;
            player.moveUpRight();
        }
        // S and A for Down Left
        else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A)) {
            direction = Direction.DOWN_LEFT;
            player.moveDownLeft();

        }
        // S and D for Down Right
        else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D)) {
            direction = Direction.DOWN_RIGHT;
            player.moveDownRight();
        }
        // W for moving up
        else if(input.isKeyDown(Input.KEY_W)) {
            direction = Direction.UP;
            player.moveUp();
        }
        // A for moving left
        else if(input.isKeyDown(Input.KEY_A)) {
            direction = Direction.LEFT;
            player.moveLeft();
        }
        // S for moving down
        else if(input.isKeyDown(Input.KEY_S)) {
            direction = Direction.DOWN;
            player.moveDown();
        }
        // D for moving right
        else if(input.isKeyDown(Input.KEY_D)) {
            direction = Direction.RIGHT;
            player.moveRight();
        }
        if(direction == Direction.NONE || !player.isMoveValid(direction,
                player.getVelocity().scale(delta),levelMap)){
            player.stop();
        }

        // Update the player model
        player.mouseRotate(getPlayerMouseAngle(input));
        player.update(delta);


        levelMap.updateCamera(player.prevMoveVelocity);

        // Update projectiles
        for(Projectile p : projectileList) {
            p.update(delta);
        }
        // Update All enemies
        for(Enemy enemy : enemyList) {
            enemy.makeMove(levelMap.currentTileMap, path, player, projectileList, delta);
            enemy.update(delta);
        }


        // Collision check for projectiles
        for(Projectile projectile : projectileList) {
            projectile.collisionCheck(levelMap.currentTileMap, enemyList, player);
        }

        // Check if players have died.
        if(player.getCurrentHealth() <= 0) {
          gameover = true;
        }

        // Remove Projectiles that have collided with objects.
        projectileList.removeIf( (Projectile projectile) -> projectile.needsRemove());
        // Remove enemies that have died.
        enemyList.removeIf( (Enemy enemy) -> enemy.isDead());
    }

  public void setPlayerType(int id) {
    player1type = id;
  }

  public void set2Player(boolean status) {
    twoPlayer = status;
    if(player1type == 1)
      player2type = 2;
    else
      player2type = 1;
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
