package ch.epfl.xblast.server;

import java.util.Collections;
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
     *            La palette à utilisé qui décrit quelles sont les images à
     *            utiliser pour les différents blocs.
     * @param blockFreeShadowed
     *            Le bloc à utiliser pour les blocs libres ombrés
     */
    public BoardPainter(Map<Block, BlockImage> palette,
            BlockImage blockFreeShadowed) {
        this.palette = new HashMap<>(Collections.unmodifiableMap(Objects.requireNonNull(palette, "palette must not be null")));
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
     *             Si l
     */
    public byte byteForCell(Board board, Cell cell) throws NoSuchElementException {
        Block block = board.blockAt(cell);
        Block leftNeighboorBlock = board.blockAt(cell.neighbor(Direction.W));

        if (block.isFree() && leftNeighboorBlock.castsShadow()) {
            return (byte) blockFreeShadowed.ordinal();
        }

        return (byte) palette.get(block).ordinal();

    }
}
