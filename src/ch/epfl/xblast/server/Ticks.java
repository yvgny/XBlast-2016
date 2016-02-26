package ch.epfl.xblast.server;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public interface Ticks {
    public static int PLAYER_DYING_TICKS = 8;
    public static int PLAYER_INVULNERABLE_TICKS = 64;
    public static int BOMB_FUSE_TICKS = 100;
    public static int EXPLOSION_TICKS = 30;
    public static int WALL_CRUMBLING_TICKS = EXPLOSION_TICKS;
    public static int BONUS_DISAPPEARING_TICKS = EXPLOSION_TICKS;

}
