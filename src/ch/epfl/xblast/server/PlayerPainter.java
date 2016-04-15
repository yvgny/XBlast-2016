package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Représente un peintre de joueur
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class PlayerPainter {
    private static final byte BYTE_FOR_DEAD = 15;

    private PlayerPainter() {
        // Non-instanciable
    }

    /**
     * Identifie l'image à utiliser pour le joueur
     * 
     * @param tick
     *            Le nombre de tick actuel
     * @param player
     *            Le joueur à utiliser pour identifier l'image
     * @return L'identifiant de l'image, sous forme de byte
     */
    public static byte byteForPlayer(int tick, Player player) {
        byte byteForPlayer = 0;
        State playerState = player.lifeState().state();

        // Selection selon l'indentité
        switch (player.id()) {
        case PLAYER_1:
            break;
            
        case PLAYER_2:
            byteForPlayer += 20;
            break;

        case PLAYER_3:
            byteForPlayer += 40;
            break;

        case PLAYER_4:
            byteForPlayer += 60;
            break;

        default:
            break;
        }

        if (player.lifeState().canMove()) {
            // Vérification si il faut utiliser le set blanc
            if (playerState == State.INVULNERABLE && ((tick & 1) == 1)) {
                byteForPlayer = 80;
            }
            // Si le joueur est vulnérable ou invulnérable, sélection selon la
            // direction
            switch (player.direction()) {
            case N:
                break;

            case E:
                byteForPlayer += 3;
                break;

            case S:
                byteForPlayer += 6;
                break;

            case W:
                byteForPlayer += 9;
                break;

            default:
                break;
            }

            // Selection selon la position (pour les pieds)
            switch (player.direction().isHorizontal() ? player.position().x() % 4 : player.position().y() % 4) {
            case 1:
                byteForPlayer += 2;
                break;

            case 3:
                byteForPlayer += 3;
                break;

            default:
                byteForPlayer += 1;
                break;
            }

        } else {
            if (playerState == State.DYING) {
                byteForPlayer += player.lives() == 1 ? 13 : 12;
            } else {
                byteForPlayer = BYTE_FOR_DEAD;
            }

        }

        return byteForPlayer;
    }
}
