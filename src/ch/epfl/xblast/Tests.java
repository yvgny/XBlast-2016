package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Ticks;
import ch.epfl.xblast.server.debug.GameStatePrinter;

/**
 * For testing purposes
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public class Tests {
    /**
     * Default main class
     * 
     * @param args
     *            Arguments to pass to main
     */
    public static void main(String[] args) {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;

        Board board2 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, xx),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

        List<Player> players = Arrays.asList(
                new Player(PlayerID.PLAYER_1, 1, new Cell(1,1), 3, 3),
                new Player(PlayerID.PLAYER_2, 1, new Cell(Cell.COLUMNS - 2,1), 3, 3), 
                new Player(PlayerID.PLAYER_3, 1, new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 3, 3), 
                new Player(PlayerID.PLAYER_4, 3, new Cell(1, Cell.ROWS - 2), 3, 3));

        GameState gs1 = new GameState(board2, players);

        ArrayList<Bomb> bombs = new ArrayList<Bomb>();
        bombs.add(new Bomb(PlayerID.PLAYER_1, new Cell(1,1), 1, 4));

        ArrayList<Sq<Sq<Cell>>> explosions = new ArrayList<Sq<Sq<Cell>>>();

        GameState gs2 = new GameState(Ticks.TOTAL_TICKS, board2, players, bombs, new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());

        Player tempPlayer = new Player(PlayerID.PLAYER_1, 5, new Cell(0,0), 7, 7);

        List<Sq<Cell>> nextBlasts = GameState.nextBlasts(new ArrayList<>(), board2, bombs.get(0).explosion());

        nextBlasts = GameState.nextBlasts(nextBlasts, board2, new ArrayList<>());

        for (Sq<Cell> sq : nextBlasts) {
            Sq<Cell> tempSq = sq;
            while (!tempSq.isEmpty()) {
                System.out.print(tempSq.head());
                tempSq = tempSq.tail();
            }
            System.out.println();
        }

        System.out.println();System.out.println();

        nextBlasts = GameState.nextBlasts(nextBlasts, board2, new ArrayList<>());

        for (Sq<Cell> sq : nextBlasts) {
            Sq<Cell> tempSq = sq;
            while (!tempSq.isEmpty()) {
                System.out.print(tempSq.head());
                tempSq = tempSq.tail();
            }
            System.out.println();
        }

        System.out.println();System.out.println();

        nextBlasts = GameState.nextBlasts(nextBlasts, board2, new ArrayList<>());

        for (Sq<Cell> sq : nextBlasts) {
            Sq<Cell> tempSq = sq;
            while (!tempSq.isEmpty()) {
                System.out.print(tempSq.head());
                tempSq = tempSq.tail();
            }
            System.out.println();
        }

        System.out.println();System.out.println();

        nextBlasts = GameState.nextBlasts(nextBlasts, board2, new ArrayList<>());

        for (Sq<Cell> sq : nextBlasts) {
            Sq<Cell> tempSq = sq;
            while (!tempSq.isEmpty()) {
                System.out.print(tempSq.head());
                tempSq = tempSq.tail();
            }
            System.out.println();
        }



        GameStatePrinter.printGameState(gs2);

    }
}