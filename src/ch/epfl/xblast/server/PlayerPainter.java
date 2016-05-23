package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Représente un peintre de joueur
 * TODO remove more magic numbers
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class PlayerPainter {
    private static final byte BYTE_FOR_DEAD = 15;
    private static final byte UNIT_PER_PLAYER = 20;
    private static final byte UNIT_PER_DIRECTION = 3;
    private static final byte WHITE_PLAYER_SET_ID = 4;
    private static final byte LOSING_LIFE_PLAYER_IMAGE = 12;
    private static final byte DYING_PLAYER_IMAGE = 13;
    private static final byte PLAYER_STEP_FRAME_NUMBER = 4;

    private PlayerPainter() {
        // Non-instanciable
    }

    /**
     * Identifie l'image à utiliser pour le joueur
     * 
     * @param tick
     *            Le nombre de ticks actuels
     * @param player
     *            Le joueur à utiliser pour identifier l'image
     * @return L'identifiant de l'image, sous forme de byte
     */
    public static byte byteForPlayer(int tick, Player player) {
        byte byteForPlayer = 0;
        State playerState = player.lifeState().state();

        // Selection selon l'identité
        byteForPlayer += player.id().ordinal() * UNIT_PER_PLAYER;

        if (player.lifeState().canMove()) {
            // Vérification s'il faut utiliser le set blanc
            if (playerState == State.INVULNERABLE && ((tick & 1) == 1))
                byteForPlayer = WHITE_PLAYER_SET_ID * UNIT_PER_PLAYER;
            
            // Si le joueur est vulnérable ou invulnérable, sélection selon la
            // direction
            byteForPlayer += player.direction().ordinal() * UNIT_PER_DIRECTION;

            // Sélection selon la position (pour les pieds, etc)
            switch (player.direction().isHorizontal() ? player.position().x() % PLAYER_STEP_FRAME_NUMBER : player.position().y() % PLAYER_STEP_FRAME_NUMBER) {
            case 1:
                byteForPlayer += 1;
                break;

            case 3:
                byteForPlayer += 2;
                break;

            default:
                break;
            }

        } else {
            
            if (playerState == State.DYING) {
                byteForPlayer += player.lives() <= 1 ? DYING_PLAYER_IMAGE : LOSING_LIFE_PLAYER_IMAGE;
            } else {
                byteForPlayer = BYTE_FOR_DEAD;
            }

        }

        return byteForPlayer;
    }
}
