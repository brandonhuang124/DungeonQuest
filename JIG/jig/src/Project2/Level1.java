package Project2;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Level1 extends BasicGameState{
	
	
	DungeonGame dg;
	
	
	// used for debugging purposes if needed:
	@SuppressWarnings("unused")
	private final String TAG = "Level1";

	// TODO set whatever dimensions we want for our map here:
	public final int MAZEWIDTH = 28;
	public final int MAZEHEIGHTH = 10; 
	// starting positions of the player within the map (tiles):
	private int playerStartX = 1;
	private int playerStartY = 1;

	
	// our map (not scaled), where each level has a different array
	// where 1 is our collision, 0 is a blank tile, 7 is a player (reference)
	// (TODO) we can add other values for our power-ups.
	// (TODO) add location for 2 players
	int dungeon[][] = 
		{	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,0,1,1,1,0,0,0,1,1,0,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
			{1,0,0,1,1,1,0,0,0,1,1,0,0,1,1,0,1,1,1,1,1,0,1,1,1,1,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
			{1,0,1,1,1,1,0,0,0,0,0,0,0,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
	};
	
	// determine direction clearly for player(s):
	enum Moves 
	{
		UP, 
		DOWN,
		LEFT,
		RIGHT
	}
	
	private Moves direction;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		dg = (DungeonGame) game;
		container.setSoundOn(true);
		Vector startPosition = Player.convertTileToPos(playerStartY, playerStartX);
		dg.player1 = new Player(startPosition.getX(),startPosition.getY());
		dg.player1.tileY = playerStartY;
		dg.player1.tileX = playerStartX;
		// TODO add assets for player(s), player(s) location is indicated on map.
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		g.drawImage(ResourceManager.getImage(DungeonGame.TEST_PLAYER_MELEE_RSC).getScaledCopy(DungeonGame.TILESCALE, DungeonGame.TILESCALE),
											dg.player1.getX(), dg.player1.getY());
		// where ever there is a 1 in our array, we draw a collision tile
		// and scale it by the size of our tile scale (integer) declared in DungeonGame.
		for (int i = 0; i < MAZEHEIGHTH; i++) 
		{
			for (int j = 0; j < MAZEWIDTH; j++) 
			{
				if (dungeon[i][j] == 1) 
				{
					g.drawImage(ResourceManager.getImage(DungeonGame.TEST_TILE_COLLISION_RSC).getScaledCopy(DungeonGame.TILESCALE, DungeonGame.TILESCALE), 
														(j * DungeonGame.TILESCALE),(i * DungeonGame.TILESCALE));
				}
			}
		}
		
		//TODO add power-ups / enemies to map
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return DungeonGame.LEVEL1;
	}
	
}
