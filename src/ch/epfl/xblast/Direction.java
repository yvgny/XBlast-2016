package ch.epfl.xblast;

import java.util.NoSuchElementException;

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
     * @throws NoSuchElementException
     *             Si l'élément n'est pas trouvé dans l'énumération
     */
    public Direction opposite() throws NoSuchElementException {
        switch (this) {
        case N:
            return S;

        case E:
            return W;

        case S:
            return N;

        case W:
            return E;
        default:
            throw new NoSuchElementException("No such " + this.name());
        }

    }

    /**
     * Calcule si la direction est horizontale à l'écran (E ou W)
     * 
     * @return vrai si la direction est horizontale, faux sinon
     */
    public boolean isHorizontal() {
        return this == E || this == W;

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
        return this == that || this.opposite() == that;
    }

}
