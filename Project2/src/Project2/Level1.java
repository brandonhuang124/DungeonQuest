package Project2;

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
    Vertex [][] path;
    MapUtil levelMap;


    @Override
    public int getID() {
        return DungeonGame.LEVEL1;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        levelMap = new MapUtil();

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
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
        player = new Player( 0, 0, 1);
        player.setWorldPos(new TileIndex(4,4));
        container.setSoundOn(true);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        DungeonGame rg = (DungeonGame)game;

        // Render tiles
        levelMap.renderMapByCamera(g);

        // Render projectiles on the screen
        for(Projectile p : projectileList) {
            p.render(g);
        }
        Coordinate playerScreenPos = levelMap.convertWorldToScreen(player.worldPos);
        player.setX(playerScreenPos.x);
        player.setY(playerScreenPos.y);
        player.render(g);
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
            projectileList.add(player.fire(getPlayerMouseAngle(input)));
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
        if(direction != Direction.NONE && player.isMoveValid(direction, player.getVelocity().scale(delta),levelMap)){
        }
        else {
            player.stop();
        }

        // Update the player model
        player.mouseRotate(getPlayerMouseAngle(input));
        player.update(delta);

        // Now offset if were near a wall so no in the wall happens
        //player.offsetUpdate(levelMap);

        levelMap.updateCamera(player.prevMoveVelocity);

        // Update projectiles
        for(Projectile p : projectileList) {
            p.update(delta);
        }
        // Collision check for projectiles
        for(Projectile projectile : projectileList) {
            //TODO add enemy here:
       //     projectile.collisionCheck(levelMap.currentTileMap);
        }
        // Remove Projectiles that have collided with objects.
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


}
