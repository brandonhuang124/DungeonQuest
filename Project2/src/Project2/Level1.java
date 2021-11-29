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
    Tile tileMap[][];
    Vertex [][] path;
    MapUtil levelMap;
    int levelWidth, levelHeight;





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
        levelWidth = 60;
        levelHeight = 60;
        levelMap.levelHeight = levelHeight;
        levelMap.levelWidth = levelWidth;

        // parse the CSV map file, throw exception in case of IO error:
        try {
            levelMap.loadLevelMap(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tileMap = DungeonGame.getTileMap(levelMap.currentMapString,
                levelWidth,levelHeight);
        projectileList = new LinkedList<Projectile>();
        player = new Player((DungeonGame.TILESIZE * 4) + (0.5f * DungeonGame.TILESIZE),
                (DungeonGame.TILESIZE * 4) + (0.5f * DungeonGame.TILESIZE));
        container.setSoundOn(true);
        // record initial location of player in our Map:
        levelMap.playerPrevLoc = player.getLocation(levelMap.screenTileRender);
        levelMap.currentTileMap = tileMap;


    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        DungeonGame rg = (DungeonGame)game;

        // Render tiles
        levelMap.renderMap(g);

        // Render Dijkstras Overlay if needed
        /***
         * Directions are listed by a integer in the overlay corresponding to the direction on the numpad
         * 2: down, 4: left, 6: right, 8: up
         * 1: down left, 3: down right, 7: up left, 9: up right
         */
     /*   if(true) {
            if(path != null) {
                for(int x = 0; x < offsetX; x++) {
                    for(int y = 0; y < offsetY; y++) {
                        if(path[x][y].getDistance() < 1000) {
                            //g.drawString("" + path[x][y].getDistance(), (x * DungeonGame.TILESIZE) + 5, (y * DungeonGame.TILESIZE) + 20);
                            g.drawString("" + path[x][y].getDirection(), (x * DungeonGame.TILESIZE) + 5, (y * DungeonGame.TILESIZE) + 8);
                        }
                    }
                }
            }
        } */
        // Render projectiles on the screen
        for(Projectile p : projectileList) {
            p.render(g);
        }

        player.render(g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Boolean playerMoved = false;
        Input input = container.getInput();
        DungeonGame dg = (DungeonGame)game;
        Coordinate playerloc = player.getLocation(levelMap.screenTileRender);

       // path = DungeonGame.getDijkstras(playerloc.x,playerloc.y,tileMap, offsetX, offsetY);

        /*** CONTROLS SECTION ***/
        // Left click for attacking
        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            System.out.println("LClick pressed");
            projectileList.add(player.fire(getPlayerMouseAngle(input)));
            for(Projectile p: projectileList) {
                System.out.println(p);
            }
        }

        // Check diagonals first
        // W and A for Up Left

        if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(7, levelMap)) {
            player.moveUpLeft();
            levelMap.updateMap(playerloc);
        }
        // W and D for Up Right
        else if(input.isKeyDown(Input.KEY_W) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(9, levelMap)) {
            player.moveUpRight();
            levelMap.updateMap(playerloc);
        }
        // S and A for Down Left
        else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_A) && player.isMoveValid(1, levelMap)) {
            player.moveDownLeft();
            levelMap.updateMap(playerloc);
        }
        // S and D for Down Right
        else if(input.isKeyDown(Input.KEY_S) && input.isKeyDown(Input.KEY_D) && player.isMoveValid(3, levelMap)) {
            player.moveDownRight();
            levelMap.updateMap(playerloc);
        }
        // W for moving up
        else if(input.isKeyDown(Input.KEY_W) && player.isMoveValid(8, levelMap)) {
            player.moveUp();
            levelMap.updateMap(playerloc);
        }
        // A for moving left
        else if(input.isKeyDown(Input.KEY_A) && player.isMoveValid(4, levelMap)) {
            player.moveLeft();
            levelMap.updateMap(playerloc);
        }
        // S for moving down
        else if(input.isKeyDown(Input.KEY_S) && player.isMoveValid(2, levelMap)) {
            player.moveDown();
            levelMap.updateMap(playerloc);
        }
        // D for moving right
        else if(input.isKeyDown(Input.KEY_D) && player.isMoveValid(6, levelMap)) {
            player.moveRight();
            if(levelMap.updateMap(playerloc)) {
                player.setX(player.getX() - levelMap.TILESIZE);
            }
        }
        else {
            player.stop();
        }


        // Update the player model
        player.mouseRotate(getPlayerMouseAngle(input));
        player.update(delta);

        // Now offset if were near a wall so no in the wall happens
        player.offsetUpdate(levelMap);



        // Update projectiles
        for(Projectile p : projectileList) {
            p.update(delta);
        }
        // Collision check for projectiles
        for(Projectile projectile : projectileList) {
            projectile.collisionCheck(tileMap);
        }
        // Remove Projectiles that have collided with objects.
        projectileList.removeIf( (Projectile projectile) -> projectile.needsRemove());

        //TODO: Bugs
        // - Why does it only move the map every two positions?
        // - How can we adjust the player position after the map updates?
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
