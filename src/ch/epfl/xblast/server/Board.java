package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Board {

    private List<Sq<Block>> boardList;

    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException {
        if (blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException(
                    "La liste ne contient pas " + Cell.COUNT + " éléments.");
        }

        boardList = new ArrayList<Sq<Block>>(blocks);
    }

    public static Board ofRows(List<List<Block>> rows)
            throws IllegalArgumentException {
        if (rows.size() != Cell.ROWS) {
            throw new IllegalArgumentException(
                    "Le pleateau doit contenir " + Cell.ROWS + " lignes.");
        }

        List<Sq<Block>> sqBlockList = new ArrayList<Sq<Block>>();

        for (List<Block> list : rows) {
            if (list.size() != Cell.COLUMNS) {
                throw new IllegalArgumentException("Le plateau doit contenir "
                        + Cell.COLUMNS + " colonnes.");
            } else {
                for (Block block : list) {
                    sqBlockList.add(Sq.constant(block));
                }
            }
        }

        return new Board(sqBlockList);
    }

    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks)
            throws IllegalArgumentException {
        if (innerBlocks.size() != Cell.ROWS - 2) {
            throw new IllegalArgumentException(
                    "La liste doit contenir " + (Cell.ROWS - 2) + " lignes.");
        }

        List<Sq<Block>> sqBlockList = new ArrayList<Sq<Block>>();

        for (List<Block> list : innerBlocks) {
            if (list.size() != Cell.COLUMNS - 2) {
                throw new IllegalArgumentException("La liste doit contenir "
                        + (Cell.COLUMNS - 2) + " colonnes.");

            } else {
                sqBlockList.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));

                for (Block block : list) {
                    sqBlockList.add(Sq.constant(block));
                }

                sqBlockList.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));

            }

            sqBlockList.addAll(Collections.nCopies(Cell.COLUMNS,
                    Sq.constant(Block.INDESTRUCTIBLE_WALL))); // ajoute le mur
                                                              // sud

            sqBlockList.addAll(0, Collections.nCopies(Cell.COLUMNS,
                    Sq.constant(Block.INDESTRUCTIBLE_WALL))); // ajoute le mur
                                                              // nord

        }

        return new Board(sqBlockList);
    }

    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks)
                    throws IllegalArgumentException {
        
        //TODO

        return null;
    }

}
