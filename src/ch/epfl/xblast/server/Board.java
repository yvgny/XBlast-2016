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

    private static final int QUADRANT_NW_BLOCKS_ROWS = (((Cell.ROWS - 2) / 2) + 1);
    private static final int QUADRANT_NW_BLOCKS_COLUMNS = (((Cell.COLUMNS - 2) / 2) + 1);
    private final List<Sq<Block>> boardList;

    /**
     * Construit un tableau a partir d'une liste de séquence de blocs
     * 
     * @param blocks
     *            La liste de séquence de bloc qui représente le tableau dans
     *            l'ordre de lecture
     * @throws IllegalArgumentException
     *             Si la liste ne contient pas
     *             {@value ch.epfl.xblast.Cell#COUNT} éléments
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException {

        if (blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException("La liste ne contient pas " + Cell.COUNT + " éléments.");
        }

        boardList = Collections.unmodifiableList(new ArrayList<Sq<Block>>(blocks));
    }

    /**
     * Construit un plateau constant avec la matrice de blocs donnée
     * 
     * @param rows
     *            La matrice de blocs utilisée pour construire le plateau
     * @return Le plateau construit a l'aide de la matrice passée en paramètre
     * @throws IllegalArgumentException
     *             Si la liste reçue n'est pas constituée de
     *             {@value ch.epfl.xblast.Cell#ROWS} listes de
     *             {@value ch.epfl.xblast.Cell#COLUMNS} éléments chacune
     */
    public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException {

        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

        List<Sq<Block>> sqBlockList = new ArrayList<Sq<Block>>();

        for (List<Block> row : rows) {
            row.forEach(block -> sqBlockList.add(Sq.constant(block)));
        }

        return new Board(sqBlockList);
    }

    /**
     * Construit un plateau muré avec les blocs intérieurs donnés
     * 
     * @param innerBlocks
     *            Les blocs intérieurs a utilisé pour construire le plateau
     * @return La plateau construit a l'aide des blocs intérieurs passé en
     *         paramètre
     * @throws IllegalArgumentException
     *             si la liste reçue n'est pas constituée de (
     *             {@value ch.epfl.xblast.Cell#ROWS} - 2) listes de (
     *             {@value ch.epfl.xblast.Cell#COLUMNS} - 2) éléments chacune
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException {

        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        List<List<Block>> innerBlocksWalled = new ArrayList<>();

        List<Block> rowOfWall = Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL);

        innerBlocksWalled.add(rowOfWall);

        for (List<Block> list : innerBlocks) {
            List<Block> tempList = new ArrayList<>();
            tempList.add(Block.INDESTRUCTIBLE_WALL);
            tempList.addAll(list);
            tempList.add(Block.INDESTRUCTIBLE_WALL);
            innerBlocksWalled.add(tempList);
        }

        innerBlocksWalled.add(rowOfWall);

        return ofRows(innerBlocksWalled);
    }

    /**
     * Construit un plateau muré symétrique avec les blocs du quadrant
     * nord-ouest donnés
     * 
     * @param quadrantNWBlocks
     *            Les blocs du qaudrant nord-ouest à utiliser pour construire le
     *            plateau
     * @return Le plateau construit à l'aide du cadran nord-ouest passé en
     *         paramètre
     * @throws IllegalArgumentException
     *             Si la liste reçue n'est pas constituée de 6 listes de 7
     *             éléments chacune
     */
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) throws IllegalArgumentException {

        checkBlockMatrix(quadrantNWBlocks, QUADRANT_NW_BLOCKS_ROWS, QUADRANT_NW_BLOCKS_COLUMNS);

        List<List<Block>> quadrantNWBlocksWalled = new ArrayList<List<Block>>();

        quadrantNWBlocks.forEach(row -> quadrantNWBlocksWalled.add(Lists.mirrored(row)));

        return ofInnerBlocksWalled(Lists.mirrored(quadrantNWBlocksWalled));
    }

    /**
     * Vérifie si la matrice est conforme
     * 
     * @param matrix
     *            La matrice à vérifier
     * @param rows
     *            La nombre de ligne que doit posséder la matrice
     * @param columns
     *            Le nombre de colonnes que doit posséder la matrice
     * @throws IllegalArgumentException
     *             Si la matrice est nulle ou ne contient pas le nombre de
     *             lignes et de colonnes passé en paramètre
     */
    public static void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns) throws IllegalArgumentException {
        if (matrix == null) {
            throw new IllegalArgumentException("L'objet est vide");
        } else if (matrix.size() != rows) {
            throw new IllegalArgumentException("La liste doit contenir " + rows + " lignes.");
        } else {
            for (List<Block> list : matrix) {
                if (list.size() != columns) {
                    throw new IllegalArgumentException("La liste doit contenir " + columns + " colonnes.");
                }
            }
        }

    }

    /**
     * Retourne la séquence des blocs pour la case données
     * 
     * @param c
     *            La case pour laquelle la séquence doit être trouvée
     * @return La séquence des blocs demandée
     */
    public Sq<Block> blocksAt(Cell c) {
        return boardList.get((c.y() * Cell.COLUMNS) + c.x());
    }

    /**
     * Retourne le bloc pour la case donnée
     * 
     * @param c
     *            La case pour laquelle le bloc doit être trouvé
     * @return Le bloc de la case passée en paramètre
     */
    public Block blockAt(Cell c) {
        return blocksAt(c).head();
    }
}
