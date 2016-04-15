package ch.epfl.xblast.server;

/**
 * Enumère les images des blocs, afin qu'il soit possible de les désigner par
 * nom plutôt que par numéro
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum BlockImage {
    /**
     * Représente le sol standard
     */
    IRON_FLOOR,

    /**
     * Représente le sol avec une ombre sur la gauche
     */
    IRON_FLOOR_S,

    /**
     * Représente un bloc foncé (mur indestructible)
     */
    DARK_BLOCK,

    /**
     * Représente une bloc extra (mur destructible)
     */
    EXTRA,

    /**
     * Représente un bloc se détruisant
     */
    EXTRA_O,

    /**
     * Représente un bloc avec le bonus de bombe
     */
    BONUS_BOMB,

    /**
     * Représente un bloc avec le bonus de portée
     */
    BONUS_RANGE;

}
