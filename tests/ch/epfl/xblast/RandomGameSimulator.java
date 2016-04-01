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
        // System.out.println(Block.FREE.isFree());
        // System.out.println(Block.DESTRUCTIBLE_WALL.isFree());

        /*
         * List<Sq<Block>> m = new ArrayList<>();
         * m.add(Sq.constant(Block.DESTRUCTIBLE_WALL)); for (int i = 0; i < 194;
         * ++i) { m.add(Sq.constant(Block.FREE)); } Board b1 = new Board(m);
         * 
         * Cell c = new Cell(0, 0); System.out.println(b1.blockAt(c));
         */

        // ------------------------------------------------------------------------------

        RandomEventGenerator randomEvent = new RandomEventGenerator(2016, 330, 100);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board2 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, __, __),
                              Arrays.asList(__, __, __, __, __, __, __),
                              Arrays.asList(__, XX, XX, XX, XX, XX, xx),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, xx, __, __, __, __, __)));

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1, 30, new Cell(1, 1), 1, 3));
        players.add(new Player(PlayerID.PLAYER_2, 30, new Cell(Cell.COLUMNS - 2, 1), 1, 3));
        players.add(new Player(PlayerID.PLAYER_3, 30, new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 1, 3));
        players.add(new Player(PlayerID.PLAYER_4, 30, new Cell(1, Cell.ROWS - 2), 1, 3));
        
        
        GameState GS = new GameState(board2, players);

        while (!GS.isGameOver()) {
            Set<PlayerID> randomBombDropEvents = randomEvent.randomBombDropEvents();
            Map<PlayerID, Optional<Direction>> randomSpeedChangeEvents = randomEvent.randomSpeedChangeEvents();
            
            GameStatePrinter.printGameState(GS);
            GS = GS.next(randomSpeedChangeEvents, randomBombDropEvents);
            
            Thread.sleep(50);
        }
    }
}
