package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Représente un niveau de jeu XBlast
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Level {
    @SuppressWarnings("javadoc")
    public final static Level DEFAULT_LEVEL = generateDefaultLevel();
    private final GameState gameState;
    private final BoardPainter boardPainter;

    /**
     * Créer un niveau du jeu XBlast
     * 
     * @param gameState
     *            Les conditions initiales du niveau
     * @param boardPainter
     *            Le peintre de jeu à utiliser
     */
    public Level(GameState gameState, BoardPainter boardPainter) {
        this.gameState = Objects.requireNonNull(gameState, "gameState must not be null");
        this.boardPainter = Objects.requireNonNull(boardPainter, "boardPainter must not be null");
    }

    private static Level generateDefaultLevel() {
        // Définitons des blocs
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        
        // Création du plateau
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        // Ajout des joueurs
        ArrayList<Player> players = new ArrayList<>();

        players.add(new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_2, 5, new Cell(Cell.COLUMNS - 2, 1), 2, 3));
        players.add(new Player(PlayerID.PLAYER_3, 5, new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 2, 3));
        players.add(new Player(PlayerID.PLAYER_4, 5, new Cell(1, Cell.ROWS - 2), 2, 3));
        
        // Création de l'état de jeu
        GameState defaultGameState = new GameState(board, players);
        
        // Création de la palette
        Map<Block, BlockImage> palette = new HashMap<Block, BlockImage>();
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        palette.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        palette.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        palette.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        palette.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        palette.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        
        // Création du peintre de plateau
        BoardPainter boardPainter = new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
        
        return new Level(defaultGameState, boardPainter);
    }

    /**
     * @return L'état du jeu actuel du niveau
     */
    public GameState gameState() {
        return gameState;
    }

    /**
     * @return Le peintre de plateau utilisé pour ce niveau
     */
    public BoardPainter boardPainter() {
        return boardPainter;
    }

}