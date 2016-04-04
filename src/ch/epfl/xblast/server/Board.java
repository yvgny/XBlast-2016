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

    private final List<Sq<Block>> boardList;
    private final static int quadrantNWBlocksRows = (((Cell.ROWS - 2) / 2) + 1);
    private final static int quadrantNWBlocksColumns = (((Cell.COLUMNS - 2) / 2) + 1);

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
     * @return La plateau construit a l'aide de la matrice passée en paramètre
     * @throws IllegalArgumentException
     *             Si la liste reçue n'est pas constituée de
     *             {@value ch.epfl.xblast.Cell#ROWS} listes de
     *             {@value ch.epfl.xblast.Cell#COLUMNS} éléments chacune
     */
    public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException {

        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);
        
        List<List<Block>> rowsCopied = copyList(rows);

        List<Sq<Block>> sqBlockList = new ArrayList<Sq<Block>>();

        for (List<Block> list : rowsCopied) {
            for (Block block : list) {
                sqBlockList.add(Sq.constant(block));
            }
        }

        return new Board(Collections.unmodifiableList(sqBlockList));
    }

    /**
     * Construit un plateau muré avec les blocs intérieurs donnés
     * 
     * @param innerBlocks
     *            Les blocs intérieurs a utilisé pour construire le plateau
     * @return La pleateua construit a l'aide des blocs intérieurs passé en
     *         paramètre
     * @throws IllegalArgumentException
     *             si la liste reçue n'est pas constituée de (
     *             {@value ch.epfl.xblast.Cell#ROWS} - 2) listes de (
     *             {@value ch.epfl.xblast.Cell#COLUMNS} - 2) éléments chacune
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException {

        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        List<List<Block>> innerBlocksCopied = copyList(innerBlocks);

        List<Block> rowOfWall = Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL);

        for (List<Block> list : innerBlocksCopied) {
            list.add(0, Block.INDESTRUCTIBLE_WALL);
            list.add(Block.INDESTRUCTIBLE_WALL);
        }

        innerBlocksCopied.add(0, rowOfWall);
        innerBlocksCopied.add(rowOfWall);

        return ofRows(Collections.unmodifiableList(innerBlocksCopied));
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

        checkBlockMatrix(quadrantNWBlocks, quadrantNWBlocksRows, quadrantNWBlocksColumns);
        
        List<List<Block>> quadrantNWBlocksCopied = copyList(quadrantNWBlocks);

        List<List<Block>> quadrantNWBlocksWalled = new ArrayList<List<Block>>();

        for (List<Block> list : quadrantNWBlocksCopied) {
            quadrantNWBlocksWalled.add(Lists.mirrored(list));
        }

        return ofInnerBlocksWalled(Collections.unmodifiableList(Lists.mirrored(quadrantNWBlocksWalled)));
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
            System.out.println(matrix.size());
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
    
    /**
     * Copie la liste de liste passée en paramètre. Il faut toutefois faire
     * attention que seul la référence l'objet de type T est copié, il se s'agit
     * donc pas de copie profonde
     * 
     * @param l
     *            La liste de liste à copier
     * @param <T>
     *            Le type de la liste passée en paramètre
     * @return Une nouvelle liste de liste de même contenu que celle passée en
     *         paramètre
     * @throws IllegalArgumentException
     *             Si la liste est vide ou si l'objet est nul
     */
    private static <T> List<List<T>> copyList(List<List<T>> l) throws IllegalArgumentException {
        if (l == null || l.isEmpty()) {
            throw new IllegalArgumentException("List is empty or not inizializated !");
        }

        List<List<T>> copied = new ArrayList<List<T>>();

        for (List<T> list : l) {
            List<T> temporary = new ArrayList<T>();

            for (T t : list) {
                temporary.add(t);

            }

            copied.add(temporary);
        }

        return copied;
    }

}
