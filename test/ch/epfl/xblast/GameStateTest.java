package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class GameStateTest {
   

    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void playerConstructorFailTicks() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
      
        GameState gameState = new GameState(-1, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailBoard() {
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(1, null, new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void playerConstructorFailPlayer() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(1, new Board(liste), new ArrayList<Player>(player), new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );

    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailBomb() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(1, new Board(liste), new ArrayList<Player>(player),null, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailExplosion() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(1, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), null, new ArrayList<Sq<Cell>>() );
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailBlasst() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(1, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), null);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void playerConstructorsAchieve() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(11, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
    }
    
    @Test
    public void RemainingTimeTest() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        GameState gameState = new GameState(400, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
        
        assertEquals(100 ,gameState.remainingTime(), 0);
    }
    
    @Test
    public void isGameOverTest() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2)); 
         }
        
        GameState gameState = new GameState(2400, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
        assertEquals(false, gameState.isGameOver());
        
        GameState gameState2 = new GameState(400, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
        assertEquals(false, gameState2.isGameOver());
        
        ArrayList<Player> player2 = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            player2.add(new Player(PlayerID.PLAYER_1, 0, new Cell(3,3), 2, 2)); 
         }
        player2.add(new Player(PlayerID.PLAYER_1, 0, new Cell(3,3), 2, 2));
        GameState gameState3 = new GameState(400, new Board(liste), new ArrayList<Player>(player2),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
        assertEquals(true , gameState3.isGameOver());
    }
    
    @Test
    public void alivePlayertest() {
        List<Sq<Block>> liste = new ArrayList<Sq<Block>>();
        for(int i = 0; i < 195; i++) {
           liste.add(Sq.constant(Block.FREE)); 
        }
        
        ArrayList<Player> player = new ArrayList<>();
            player.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2));
            player.add(new Player(PlayerID.PLAYER_2, 0, new Cell(3,3), 2, 2)); 
            player.add(new Player(PlayerID.PLAYER_3, 2, new Cell(3,3), 2, 2)); 
            player.add(new Player(PlayerID.PLAYER_4, 0, new Cell(3,3), 2, 2)); 
   
        
        GameState gameState = new GameState(2400, new Board(liste), new ArrayList<Player>(player),new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>() );
        
        ArrayList<Player> alivePlayer = new ArrayList<>();
        alivePlayer.add(new Player(PlayerID.PLAYER_1, 2, new Cell(3,3), 2, 2));
        alivePlayer.add(new Player(PlayerID.PLAYER_3, 2, new Cell(3,3), 2, 2)); 

        ArrayList<Player> fonction = new ArrayList<>(gameState.alivePlayers());
        
        assertEquals(alivePlayer.size(), gameState.alivePlayers().size());
        for(int i = 0; i< alivePlayer.size(); i++) {
            if (alivePlayer.get(i).id() == fonction.get(i).id()) {
                assertTrue("OK", true);
            }
            else {
                assertTrue("Les joueurs en vie ne sont pas les mêmes", false);
            }
                
        }
    }

}
