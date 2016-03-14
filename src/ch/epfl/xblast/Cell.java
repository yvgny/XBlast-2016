package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

/**
 * Une case du plateau du jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Cell {

    private int x;
    private int y;

    /**
     * Nombre de colonnes du plateau de jeu (doit être un nombre impair !)
     */
    public static final int COLUMNS = 15;

    /**
     * Nombre de lignes du plateau de jeu (doit être un nombre impair !)
     */
    public static final int ROWS = 13;

    /**
     * Nombre total de cases du plateau de jeu
     */
    public static final int COUNT = COLUMNS * ROWS;

    /**
     * Liste de toutes les cases du plateau de jeu, classé dans l'ordre de
     * lecture (Row Major Order)
     */
    public static List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());

    /**
     * Liste de toutes les cases du plateau de jeu, classé en spirale (Spiral
     * Order)
     */
    public static List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(spiralOrder());

    /**
     * Construit une case avec coordonnées à choix
     * 
     * @param x
     *            Coordonnées x de la case
     * @param y
     *            Coordonnées y de la case
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;

        normalize();
    }

    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> rowMajor = new ArrayList<Cell>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                rowMajor.add(new Cell(j, i));
            }
        }

        return rowMajor;

    }

    private static ArrayList<Cell> spiralOrder() {
        ArrayList<Integer> ix = new ArrayList<Integer>();
        ArrayList<Integer> iy = new ArrayList<Integer>();
        ArrayList<Integer> i1;
        ArrayList<Integer> i2;
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        Integer c2;

        boolean horizontal = true;

        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }

        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }

        while (ix.size() != 0 && iy.size() != 0) {
            i1 = horizontal ? ix : iy;
            i2 = horizontal ? iy : ix;

            c2 = i2.get(0);
            i2.remove(0);

            for (Integer c1 : i1) {
                if (horizontal) {
                    spiral.add(new Cell(c1, c2));
                } else {
                    spiral.add(new Cell(c2, c1));
                }
            }

            Collections.reverse(i1);
            horizontal = !horizontal;
        }

        return spiral;
    }

    private void normalize() {
        x = Math.floorMod(x, COLUMNS);
        y = Math.floorMod(y, ROWS);

    }

    /**
     * Retourne la coordonnée x de la case
     * 
     * @return La coordonnée x
     */
    public int x() {
        return x;
    }

    /**
     * Retourne la coordonnée y de la case
     * 
     * @return La coordonnée y
     */
    public int y() {
        return y;
    }

    /**
     * Calcul l'index auquel se trouve la case dans la liste de case du plateau
     * de jeu (classé dans l'ordre de lecture)
     * 
     * @return L'index calculé, -1 si rien n'est trouvé
     */
    public int rowMajorIndex() {
        return ROW_MAJOR_ORDER.indexOf(this);
    }

    /**
     * Calcul la case voisine dans la direction souhaitée
     * 
     * @param dir
     *            La direction souhaitée
     * @return La case voisine dans la direction choisie
     */
    public Cell neighbor(Direction dir) {
        Cell neighbor;
        switch (dir) {
        case N:
            neighbor = new Cell(x, y - 1);
            break;
        case S:
            neighbor = new Cell(x, y + 1);
            break;
        case E:
            neighbor = new Cell(x + 1, y);
            break;
        case W:
            neighbor = new Cell(x - 1, y);
            break;
        default:
            neighbor = new Cell(x, y);
        }

        return neighbor;
    }

    /**
     * Compare un objet avec cette case pour savoir si ce sont les mêmes. Deux
     * cases sont égales si et seulement si elles on les même coordonnées x et
     * y.
     * 
     * @param that
     *            L'objet a comparer avec la case
     * @return true si l'objet est une case possédant les mêmes coordonnées,
     *         false sinon
     */
    @Override
    public boolean equals(Object that) {
        if (that.getClass() == Cell.class) {
            if (((Cell) that).x() == x && ((Cell) that).y() == y) {
                return true;

            } else {
                return false;

            }
        } else {
            return false;

        }
    }

    /**
     * Retourne une représentation textuelle des coordonnées de la case, sous la
     * forme (x,y)
     * 
     * @return La représenattion textuelle de la case
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
