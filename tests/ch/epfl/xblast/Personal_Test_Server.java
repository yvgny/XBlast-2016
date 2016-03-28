package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.GameStatePrinter;

/**
 * @author Martin Chatton (262215)
 * @author Alexandre Abbey (235688)
 */
public class Personal_Test_Server {

    public static void main(String[] args) {
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
                              Arrays.asList(__, XX, XX, XX, XX, XX, __),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, XX, __, __, __, __, __),
                              Arrays.asList(__, __, __, __, __, __, __)));

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(PlayerID.PLAYER_1, 3, new Cell(7, 6), 1, 5));
        players.add(new Player(PlayerID.PLAYER_2, 3, new Cell(7, 6), 1, 5));
        players.add(new Player(PlayerID.PLAYER_3, 3, new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 1, 0));
        players.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, Cell.ROWS - 2), 1, 0));

        List<Sq<Cell>> blasts = new ArrayList<>();
        int x = 5;
        int y = 1;
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x + 1, y))));
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x, y + 1))));
        blasts.add(Sq.repeat(1, new Cell(3, y)).concat(Sq.repeat(1, new Cell(3, y + 1))));
        x = 9;
        y = 9;
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x, y + 1))));
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x - 1, y))));
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x - 1, y))));
        x = 1;
        y = 5;
        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x, y - 1))));
        
        ArrayList<Bomb> bombs = new ArrayList<Bomb>();
        
        
        GameState GS = new GameState(0, board, players, bombs, new ArrayList<>(), new ArrayList<>());
        

        System.out.println("Ticks : " + GS.ticks());
        System.out.println("--------------\n");
        GameStatePrinter.printGameState(GS);
        
        HashSet<PlayerID> bombDropEvents = new HashSet<PlayerID>();
        bombDropEvents.add(PlayerID.PLAYER_1);
        bombDropEvents.add(PlayerID.PLAYER_2);
        
        GS = GS.next(new HashMap<>(), new HashSet<>());
        
        System.out.println("Ticks : " + GS.ticks());
        System.out.println("--------------\n");
        GameStatePrinter.printGameState(GS);
        
        GS = GS.next(new HashMap<>(), bombDropEvents);
        
        System.out.println("Ticks : " + GS.ticks());
        System.out.println("--------------\n");
        GameStatePrinter.printGameState(GS);


        for (int i = 3; i < 135; ++i) {
            GS = GS.next(new HashMap<>(), new HashSet<>());
            System.out.println("--------------\n");
            System.out.println("Ticks : " + GS.ticks());
            GameStatePrinter.printGameState(GS);
            System.out.println();
        }
        
//        x = 9;
//        y = 9;
//        blasts.add(Sq.repeat(1, new Cell(x, y)).concat(Sq.repeat(1, new Cell(x, y + 1))));
//
//        GameState GS2 = new GameState(GS.ticks(), GS.board(), players, new ArrayList<>(), new ArrayList<>(), blasts);
//
//        for (int i = 0; i < 35; ++i) {
//            GS2 = GS2.next(new HashMap<>(), new HashSet<>());
//            System.out.println("--------------\n");
//            System.out.println("Ticks : " + GS2.ticks());
//            if (GS2.ticks() == 50) {
//                blasts.clear();
//                blasts.add(Sq.repeat(1, new Cell(9, 9)).concat(Sq.repeat(1, new Cell(9, 10))));
//                GS2 = new GameState(GS2.ticks(), GS2.board(), GS2.players(), new ArrayList<>(), new ArrayList<>(), blasts);
//            }
//            GameStatePrinter.printGameState(GS2);
//            System.out.println();
//        }

        // ------------------------------------------------------------------------------
        /*
         * Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(7, 7), 1, 1); for
         * (Sq<Sq<Cell>> arm : b.explosion()) { System.out.println(
         * arm.head().head().x() + " - " + arm.head().head().y());
         * 
         * System.out.println(arm.head().tail().head().x() + " - " +
         * arm.head().tail().head().y());
         * 
         * System.out.println(arm.head().tail().tail().head().x() + " - " +
         * arm.head().tail().tail().head().y());
         * 
         * System.out.println(arm.head().tail().tail().tail().head().x() + " - "
         * + arm.head().tail().tail().tail().head().y());
         * 
         * System.out.println("-+-+-+-"); System.out.println(""); }
         */
    }

}
