package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

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

        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        List<List<Block>> copiedInnerBlocks = Lists.copy(innerBlocks);
        
        List<Block> rowOfWall = Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL);
        
        
        for (List<Block> list : copiedInnerBlocks) {
            list.add(0, Block.INDESTRUCTIBLE_WALL);
            list.add(0, Block.INDESTRUCTIBLE_WALL);
        }
        
        copiedInnerBlocks.add(0, rowOfWall);
        copiedInnerBlocks.add(0, rowOfWall);
        
        return ofRows(copiedInnerBlocks);
    }

    public static Board ofQuadrantNWBlocksWalled(
            List<List<Block>> quadrantNWBlocks)
                    throws IllegalArgumentException {

        return null;
    }

    private static void checkBlockMatrix(List<List<Block>> matrix, int rows,
            int columns) throws IllegalArgumentException {
        if (matrix.size() != rows) {
            throw new IllegalArgumentException(
                    "La liste doit contenir " + rows + " lignes.");
        } else {
            for (List<Block> list : matrix) {
                if (list.size() != columns) {
                    throw new IllegalArgumentException(
                            "La liste doit contenir " + columns + " colonnes.");
                }
            }
        }

    }

}
