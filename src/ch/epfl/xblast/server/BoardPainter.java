package ch.epfl.xblast.server;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * Représente un peintre de plateau
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class BoardPainter {
    private final Map<Block, BlockImage> palette;
    private final BlockImage blockFreeShadowed;

    /**
     * Créer un peintre de plateau
     * 
     * @param palette
     *            La palette (qui décrit quelles images utiliser pour les
     *            différents blocs) à utiliser
     * @param blockFreeShadowed
     *            Le bloc à utiliser pour les blocs libres ombrés
     */
    public BoardPainter(Map<Block, BlockImage> palette, BlockImage blockFreeShadowed) {
        this.palette = new HashMap<>(Objects.requireNonNull(palette, "palette must not be null"));
        this.blockFreeShadowed = Objects.requireNonNull(blockFreeShadowed, "block must not be null");
    }

    /**
     * Retourne l'image d'une certaine cellule selon un certain plateau
     * 
     * @param board
     *            La plateau à utiliser
     * @param cell
     *            La cellule à représenter
     * @return L'image représentant la cellule, sous forme de byte
     * @throws NoSuchElementException 
     * Si l
     */
    public byte byteForCell(Board board, Cell cell) throws NoSuchElementException {
        Block block = board.blockAt(cell);
        Block leftNeighboorBlock = board.blockAt(cell.neighbor(Direction.W));

        switch (block) {
        case FREE:
            return (byte) (leftNeighboorBlock.castsShadow() ? 1 : 0);

        case INDESTRUCTIBLE_WALL:
            return (byte) 2;
            
        case DESTRUCTIBLE_WALL:
            return (byte) 3;

        case CRUMBLING_WALL:
            return (byte) 4;

        case BONUS_BOMB:
            return (byte) 5;

        case BONUS_RANGE:
            return (byte) 6;

        default:
            throw new NoSuchElementException("cannot find block " + block.name());
        }

    }
}
