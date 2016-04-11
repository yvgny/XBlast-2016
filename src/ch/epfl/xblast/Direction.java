package ch.epfl.xblast;

/**
 * Enumération des directions possibles sur le plateau de jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum Direction {
    /**
     * Direction nord
     */
    N,

    /**
     * Direction est
     */
    E,

    /**
     * Direction sud
     */
    S,

    /**
     * Direction ouest (west en anglais)
     */
    W;

    /**
     * Calcule l'opposé de la direction a celle où la méthode est appliquée
     * 
     * @return La direction opposée
     */
    public Direction opposite() {
        switch (this) {
        case N:
            return S;

        case E:
            return W;

        case S:
            return N;

        case W:
            return E;
        }

        return null;
    }

    /**
     * Calcule si la direction est horizontale à l'écran (E ou W)
     * 
     * @return vrai si la direction est horizontale, faux sinon
     */
    public boolean isHorizontal() {
        if (this == E || this == W) {
            return true;
        }

        return false;

    }

    /**
     * Calcule si la direction est parrallèle à la direction passée en
     * paramètre. (La direction est parallèle seulement à elle-même et à son
     * opposé)
     * 
     * @param that
     *            La direction a comparer
     * @return vrai si la direction est parrallèle à celle passée en paramètre,
     *         faux sinon
     */
    public boolean isParallelTo(Direction that) {
        if (this == that || this.opposite() == that) {
            return true;
        }

        return false;
    }

}
