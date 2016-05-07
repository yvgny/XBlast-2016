package ch.epfl.xblast;

/**
 * Enumère les différentes actions qu'un joueur humain peut effectuer
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum PlayerAction {
    /**
     * Exprime sa volonté de se joindre à une partie
     */
    JOIN_GAME,

    /**
     * Demande à son joueur simulé de se déplacer vers le nord
     */
    MOVE_N,

    /**
     * Demande à son joueur simulé de se déplacer vers l'est
     */
    MOVE_E,

    /**
     * Demande à son joueur simulé de se déplacer vers le sud
     */
    MOVE_S,

    /**
     * Demande à son joueur simulé de se déplacer vers l'ouest
     */
    MOVE_W,

    /**
     * Demande à son joueur simulé de s'arrêter
     */
    STOP,

    /**
     * Demande à son joueur simulé de déposer une bombe
     */
    DROP_BOMB;

}
