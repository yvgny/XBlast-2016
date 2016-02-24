package ch.epfl.xblast;

/**
 * Enumération des directions poisibles sur le plateau de jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum Direction {
    N, E, S, W;

    /**
     * Calcul l'opposé de la direction a celle ou la méthode est appliqué
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
     * Calcul si la direction est horizontale à l'écran (E ou W)
     * 
     * @return true si la direction est horizontale, false sinon
     */
    public boolean isHorizontal() {
        if (this == E || this == W) {
            return true;
        }

        return false;

    }

    /**
     * Calcul si le direction est parrallèle est la direction passée en
     * paramètre. (La direction est parallèle seulement à elle-même et à son
     * opposé)
     * 
     * @param that
     *            La direction a comparer
     * @return true si la direction est parrallèle à celle passée en paramètre,
     *         false sinon
     */
    public boolean isParallelTo(Direction that) {
        if (this == that || this.opposite() == that) {
            return true;
        }

        return false;
    }

}
