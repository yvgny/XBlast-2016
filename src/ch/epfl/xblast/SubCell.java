package ch.epfl.xblast;

/**
 * Une sous-case d'une case du plateau de jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class SubCell {

    private final int x;
    private final int y;
    private static final int GRANULARITY = 16;
    private static final int COLUMNS = GRANULARITY * Cell.COLUMNS;
    private static final int ROWS = GRANULARITY * Cell.ROWS;

    /**
     * Construit une sous-case avec les coordonnées choisies
     * 
     * @param x
     *            La coordonnée x de la sous-case
     * @param y
     *            La coordonnées y de la sous-case
     */
    public SubCell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Calcule la sous-case d'une case donnée
     * 
     * @param cell
     *            La case dont la sous-case doit être trouvée
     * @return La sous-case de la case passée en paramètre
     */
    public static SubCell centralSubCellOf(Cell cell) {
        int xSubCell = (cell.x() * GRANULARITY) + (GRANULARITY / 2);
        int ySubCell = (cell.y() * GRANULARITY) + (GRANULARITY / 2);

        return new SubCell(xSubCell, ySubCell);

    }

    /**
     * Calcule la distance de Manhattan entre la sous-case et la sous-case
     * centrale la plus proche
     * 
     * @return La distance de Manhattan jusqu'à la sous-case la plus proche
     */
    public int distanceToCentral() {
        SubCell centralSubcell = centralSubCellOf(containingCell());
        int xDistance = Math.abs(centralSubcell.x() - x);
        int yDistance = Math.abs(centralSubcell.y() - y);

        return xDistance + yDistance;
    }

    /**
     * Calcule si cette sous-case est une sous-case centrale
     * 
     * @return vrai si la sous-case est une sous-case centrale, faux sinon
     */
    public boolean isCentral() {
        SubCell centralSubCell = centralSubCellOf(containingCell());

        if (centralSubCell.equals(this)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calcule la case voisine a cette sous-case dans la direction choisie
     * 
     * @param d
     *            La direction de la case-voisine ciblée
     * @return La case ciblée
     */
    public SubCell neighbor(Direction d) {
        SubCell neighbor;
        switch (d) {
        case N:
            neighbor = new SubCell(x, y - 1);
            break;
        case S:
            neighbor = new SubCell(x, y + 1);
            break;
        case E:
            neighbor = new SubCell(x + 1, y);
            break;
        case W:
            neighbor = new SubCell(x - 1, y);
            break;
        default:
            neighbor = new SubCell(x, y);
        }

        return neighbor;
    }

    /**
     * Calcule la case contenant cette sous-case
     * 
     * @return La case contenant cette sous-case
     */
    public Cell containingCell() {
        int xCell = x / GRANULARITY;
        int yCell = y / GRANULARITY;

        return new Cell(xCell, yCell);
    }

    /**
     * Permet de comparer cette sous-case à un objet pour voir si ce sont les
     * mêmes. Deux sous-cases sont identiques si elles ont les même coordonnées.
     * 
     * @param that
     *            L'objet à comparer avec cette sous-case
     * @return vrai si l'objet ets une sous-case ayant les même coordonnées,
     *         faux sinon
     */
    @Override
    public boolean equals(Object that) {
        if (that.getClass() == SubCell.class) {
            if (((SubCell) that).x == x && ((SubCell) that).y == y) {
                return true;

            } else {
                return false;

            }
        } else {
            return false;

        }
    }

    /**
     * Retourne une représentation textuelle des coordonnées de la sous-case,
     * sous la forme (x,y)
     * 
     * @return La représentation textuelle de la sous-case
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Retourne la coordonnée x de la sous-case
     * 
     * @return La coordonnée x de la sous-case
     */
    public int x() {
        return x;
    }

    /**
     * Retourne la coordonnée y de la sous-case
     * 
     * @return La coordonnée y de la sous-case
     */
    public int y() {
        return y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (COLUMNS * x) + (y);
    }

    
}
