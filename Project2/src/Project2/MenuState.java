package Project2;

import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.naming.spi.ResolveResult;
import java.io.IOException;
import java.util.LinkedList;

public class MenuState extends BasicGameState {

  private int select, timer, phase;
  private boolean selected, arrowBlink, playerFound, singleplayer;

  /********** PUT ADDRESS OF SERVER INTHIS VARIABLE WHEN RUNNING **********/
  private String serverAddress = "192.168.0.107";

  @Override
  public int getID() {
    return 3;
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {

  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) {
    container.setSoundOn(true);
    selected = playerFound = singleplayer = false;
    arrowBlink = true;

    timer = -1;
    select = 0;
    phase = 1;

    Input input = container.getInput();
    input.clearKeyPressedRecord();
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    // Main menu
    if(phase == 1) {
      // Render Static images
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_TITLE_RSC), 63, 25);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_1P_RSC), 222, 340);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_2P_RSC), 222, 440);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_HOWTOPLAY_RSC), 222, 540);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_JOIN_RSC), 222, 640);

      // Render selector based on which icon is hovered over
      if(select == 1)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 428,342);
      else if(select == 2)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 428,442);
      else if(select == 3)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 428,542);
      else if(select == 4)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 428,642);

    }

    // Character select
    if(phase == 2) {
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_TITLE_RSC), 63, 25);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_CHARACTERSELECT_RSC), 173, 330);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_MELEEICON_RSC), 120, 465);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_RANGEDICON_RSC), 465, 465);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_BACK_RSC), 25, 645);
      if(select == 1)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 231, 647);
      else if(select == 2)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 194,475);
      else if(select == 3)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 540,475);
    }

    // Game search
    if(phase == 3) {
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_TITLE_RSC), 63, 25);
      if(playerFound) {
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_FOUND_RSC), 58, 370);
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_PLAY_RSC), 222, 440);
        if(select == 1)
          g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 428, 442);
      }
      else {
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_WAIT_RSC), 108, 370);
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_BACK_RSC), 25, 645);
        if(select == 1)
          g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 231, 647);
      }
    }

    // How to play
    if(phase == 4) {
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_BACK_RSC), 25, 645);
      if(select == 1)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 231, 647);
    }

    // Game join
    if(phase == 5) {
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_TITLE_RSC), 63, 25);
      g.drawImage(ResourceManager.getImage(DungeonGame.MENU_BACK_RSC), 25, 645);
      if(select == 1)
        g.drawImage(ResourceManager.getImage(DungeonGame.MENU_SELECTOR_RSC), 231, 647);
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
    /***
     * DIMENSIONS OF ICONS FOR USING IN LOCATIONAL CALCULATIONS:
     *  All buttons: 196x68
     *  Melee Icon: 64 x 64
     *  Ranged Icon: 64 x 64
     */
    Input input = container.getInput();
    // Get mouse position
    float mousex = input.getMouseX();
    float mousey = input.getMouseY();

    // Check which icon were hovered over

    // Main menu phase
    if(phase == 1) {
      // Since all icons are in a line, check x coordinate first,
      if(222 < mousex && mousex < 418) {
        // Now check ys for each button.
        if(340 < mousey && mousey < 408)
          select = 1;
        else if(440 < mousey && mousey < 508)
          select = 2;
        else if(540 < mousey && mousey < 608)
          select = 3;
        else if(640 < mousey && mousey < 708)
          select = 4;
        else
          select = 0;
      }
      else
        select = 0;

      // Check if we click on something
      if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
        if(select == 1) {
          System.out.println("1 Selected");
          phase = 2;
          singleplayer = true;
        }
        else if(select == 2) {
          System.out.println("2 Selected");
          phase = 2;
          singleplayer = false;
        }
        else if(select == 3) {
          System.out.println("3 Selected");
          phase = 4;
        }
        else if(select == 4) {
          System.out.println("4 Selected");
          phase = 5;
          // If were joining a game, we need to open a connection and tell the server were P2
          try {
            DungeonGame.client = new Client(serverAddress, 4999);
            System.out.println("Client created: " + DungeonGame.client);
          } catch (Exception e) {
            e.printStackTrace();
          }
          try {
            DungeonGame.client.dataOutputStream.writeUTF("P2CLIENT;");
            DungeonGame.client.dataOutputStream.flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    // Character select phase
    else if(phase == 2) {
      // First check if were on the back button
      if(25 < mousex && mousex < 221 && 645 < mousey && mousey < 713)
        select = 1;
      // Now check which character were hovered over.
      else if(0 < mousex && mousex < 184 && 400 < mousey && mousey < 645)
        select = 2;
      else if(465 < mousex && mousex < 640 && 400 < mousey && mousey < 645)
        select = 3;
      else
        select = 0;


      if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
        if(select == 1){
          phase = 1;
        }
        else if(select == 2) {
          System.out.println("Melee Selected");
          ((Level1)game.getState(DungeonGame.LEVEL1)).setPlayerType(2);
          if(!singleplayer) {
            // If were in multiplayer, wee need to open a connection to the server and tell them were player1
            try {
              phase = 3;
              DungeonGame.client = new Client(serverAddress, 4999);
              System.out.println("Client created: " + DungeonGame.client);
            } catch (Exception e) {
              e.printStackTrace();
            }
            try {
              DungeonGame.client.dataOutputStream.writeUTF("P1CLIENT;");
              DungeonGame.client.dataOutputStream.flush();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

          else {
            System.out.println("Start game now");
            ((Level1)game.getState(DungeonGame.LEVEL1)).set2Player(false);
            game.enterState(DungeonGame.LEVEL1);
          }
        }
        else if(select == 3) {
          System.out.println("Ranged Selected");
          ((Level1)game.getState(DungeonGame.LEVEL1)).setPlayerType(1);
          if(!singleplayer) {
            try {
              phase = 3;
              DungeonGame.client = new Client(serverAddress, 4999);
              System.out.println("Client created: " + DungeonGame.client);
            } catch (Exception e) {
              e.printStackTrace();
            }
            try {
              DungeonGame.client.dataOutputStream.writeUTF("P1CLIENT;");
              DungeonGame.client.dataOutputStream.flush();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          else {
            System.out.println("Start Game Now");
            ((Level1)game.getState(DungeonGame.LEVEL1)).set2Player(false);
            game.enterState(DungeonGame.LEVEL1);
          }
        }
      }

    }

    // Game searching phase
    else if(phase == 3) {
      if(input.isKeyPressed(Input.KEY_SPACE))
        playerFound = true;
      if(playerFound) {
        // We can press the start button here
        if(222 < mousex && mousex < 418 && 440 < mousey && mousey < 508)
          select = 1;
        else
          select = 0;

        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
          if(select == 1) {
            System.out.println("Start the game now");
            ((Level1)game.getState(DungeonGame.LEVEL1)).set2Player(true);
            game.enterState(DungeonGame.LEVEL1);
          }
        }
      }
      else {
        // Backing out is still an option.
        if(25 < mousex && mousex < 221 && 645 < mousey && mousey < 713)
          select = 1;
        else
          select = 0;

        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
          if(select == 1) {
            phase = 1;
          }
        }
      }
    }

    // How to play Phase
    else if(phase == 4) {
      // Only thing here to select is the back button, so check for that.
      if(25 < mousex && mousex < 221 && 645 < mousey && mousey < 713)
        select = 1;
      else
        select = 0;

      if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
        if(select == 1) {
          phase = 1;
        }
      }
    }

    // Join game Phase
    else if(phase == 5) {
      // Do stuff here
      game.enterState(DungeonGame.DUMMYSTATE);
      if(25 < mousex && mousex < 221 && 645 < mousey && mousey < 713)
        select = 1;
      else
        select = 0;

      if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
        if(select == 1) {
          phase = 1;
        }
      }
    }
  }
}
