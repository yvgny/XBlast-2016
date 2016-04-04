package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

/**
 * @author Martin Chatton (262215)
 * @author Alexandre Abbey (235688)
 */
public class RandomGameSimulator {

    public static void main(String[] args) throws InterruptedException {
        RandomEventGenerator randomEvent = new RandomEventGenerator(2016, 30, 100);

        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board1 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        Board board2 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, __, __),
                              Arrays.asList(__, __, __, __, __, __, __),
                              Arrays.asList(__, XX, XX, XX, XX, XX, xx),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, xx, __, __, __, __, __)));
        
        Board board3 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                  Arrays.asList(__, __, __, __, __, xx, __),
                  Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                  Arrays.asList(__, xx, __, __, __, xx, __),
                  Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                  Arrays.asList(__, xx, __, xx, __, __, __),
                  Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

        ArrayList<Player> players = new ArrayList<>();
        
        players.add(new Player(PlayerID.PLAYER_1, 30000, new Cell(1, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 30000, new Cell(Cell.COLUMNS - 2, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 30000, new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 30000, new Cell(1, Cell.ROWS - 2), 2, 3));
        
        
        GameState GS = new GameState(board3, players);

        while (!GS.isGameOver()) {
            
            GameStatePrinter.printGameState(GS);

            Map<PlayerID, Optional<Direction>> randomSpeedChangeEvents = randomEvent.randomSpeedChangeEvents();
            Set<PlayerID> randomBombDropEvents = randomEvent.randomBombDropEvents();
            
            boolean debugActivated = false;
            
            if (debugActivated && (!randomBombDropEvents.isEmpty() || !randomSpeedChangeEvents.isEmpty())) {
                System.out.println();
                
                for (PlayerID playerID : randomBombDropEvents) {
                    System.out.println("Le joueur n°" + playerID.toString().charAt(7) + " va poser une bombe !");
                }
                
                for (Map.Entry<PlayerID, Optional<Direction>> entry : randomSpeedChangeEvents.entrySet()) {
                    if (entry.getValue().isPresent()) {
                        System.out.println("Le joueur n°" + entry.getKey().toString().charAt(7) + " va dans la direction " + entry.getValue().get() + ".");
                    } else {
                        System.out.println("Le joueur n°" + entry.getKey().toString().charAt(7) + " va s'arrêter !");
                    }
                    
                }

                Thread.sleep(1000);
            }
            
            GS = GS.next(randomSpeedChangeEvents, randomBombDropEvents);
            

            
            Thread.sleep(50);
        }
    }
}
