package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

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
    public final static int PLAYER_DYING_TICKS = 8;

    /**
     * Durée de la période d'invulnérabilité d'un joueur
     */
    public final static int PLAYER_INVULNERABLE_TICKS = 64;

    /**
     * Durée de consommation de la mèche d'une bombe, c-à-d le temps avant
     * qu'elle n'explose
     */
    public final static int BOMB_FUSE_TICKS = 100;

    /**
     * Durée d'explosion d'une bombe
     */
    public final static int EXPLOSION_TICKS = 30;

    /**
     * Durée d'écroulement d'un mur destructible
     */
    public final static int WALL_CRUMBLING_TICKS = EXPLOSION_TICKS;

    /**
     * Durée de disparition d'un bonus atteint par une explosion
     */
    public final static int BONUS_DISAPPEARING_TICKS = EXPLOSION_TICKS;

    /**
     * Le nombre de coups d'horloge par seconde
     */
    public final static int TICKS_PER_SECOND = 20;

    /**
     * La durée, en nanosecondes, d'un coup d'horloge
     */
    public final static int TICK_NANOSECOND_DURATION = Time.NS_PER_S / TICKS_PER_SECOND;

    /**
     * Le nombre total de coups d'horloge d'une partie
     */
    public final static int TOTAL_TICKS = 2 * Time.S_PER_MIN * TICKS_PER_SECOND;
}
