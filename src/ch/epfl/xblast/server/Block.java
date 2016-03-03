package ch.epfl.xblast.server;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum Block {
    /**
     * Représente une case libre
     */
    FREE,

    /**
     * Représente une case avec un mur indestructible
     */
    INDESTRUCTIBLE_WALL,

    /**
     * Représente une case avec un mur destructible
     */
    DESTRUCTIBLE_WALL,

    /**
     * Représente une case avec un mur en train de se casser
     */
    CRUMBLING_WALL;

    /**
     * Vérifie si la case est libre
     * 
     * @return true si elle est libre (FREE), false sinon
     */
    public boolean isFree() {
        if (this == Block.FREE) {
            return true;

        } else {
            return false;
        }

    }

    /**
     * Vérifie si le joueur peut venir sur cette case, donc si la case est libre
     * (FREE)
     * 
     * @return true si la case est libre, false sinon
     */
    public boolean canHostPlayer() {
        return isFree();
    }

    /**
     * Vérifie si la case projette une ombre sur le tableau
     * 
     * @return vrai si la case projette une ombre (si c'est une case de type
     *         mur), flase sinon
     */
    public boolean castsShadow() {
        if (this.toString().endsWith("_WALL")) {
            return true;

        } else {
            return false;
        }

    }

}
