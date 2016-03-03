package ch.epfl.xblast.server;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public interface Ticks {
    /**
     * Durée de la période durant laquelle un joueur est mourant
     */
    public static int PLAYER_DYING_TICKS = 8;

    /**
     * Durée de la période d'invulnérabilité d'un joueur
     */
    public static int PLAYER_INVULNERABLE_TICKS = 64;

    /**
     * Durée de consommation de la mèche d'une bombe, c-à-d le temps avant
     * qu'elle n'explose
     */
    public static int BOMB_FUSE_TICKS = 100;

    /**
     * Durée d'explosion d'une bombe
     */
    public static int EXPLOSION_TICKS = 30;

    /**
     * Durée d'écroulement d'un mur destructible
     */
    public static int WALL_CRUMBLING_TICKS = EXPLOSION_TICKS;

    /**
     * Durée de disparition d'un bonus atteint par une explosion
     */
    public static int BONUS_DISAPPEARING_TICKS = EXPLOSION_TICKS;

}
