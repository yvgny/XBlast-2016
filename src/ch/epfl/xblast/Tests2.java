package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class Tests2 {
    
    public static void main(String[] args) throws InterruptedException {
        RandomEventGenerator randomEvent = new RandomEventGenerator(2016, 330, 100);
        
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board2 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, __, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, __),
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
        
        
        List<Bomb> bombs = Arrays.asList(new Bomb(PlayerID.PLAYER_1, new Cell(7, 11), 1, 5));
        
        GameState GS2 = new GameState(0, board2, players, bombs, new ArrayList<>(), new ArrayList<>());

        Map<PlayerID, Optional<Direction>> player3SpeedChangeEvent = new HashMap<>();
        
        // player3SpeedChangeEvent.put(PlayerID.PLAYER_3, Optional.of(Direction.W));
        
        while (!GS2.isGameOver()) {
            
            GameStatePrinter.printGameState(GS2);
            GS2 = GS2.next(player3SpeedChangeEvent, new HashSet<PlayerID>());
            
            Thread.sleep(2000);
        }
        
    }

}
