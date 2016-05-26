package ch.epfl.xblast.server;

import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * Représente un peintre de bombes et d'explosions
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class ExplosionPainter {
    private static final int BYTE_IF_FALSE = 0;

    private static final int BYTE_IF_TRUE = 1;

    /**
     * Identifiant à utiliser pour les cases dénuées de particules d'explosions
     */
    public static final byte BYTE_FOR_EMPTY = 16;

    private static final byte BYTE_FOR_WHITE_BOMB = 21;
    private static final byte BYTE_FOR_BLACK_BOMB = 20;

    private ExplosionPainter() {
        // Non-instanciable
    }

    /**
     * Retourne l'octet identifiant l'image à utiliser pour dessiner la bombe
     * qu'on lui passe en argument
     * 
     * @param bomb
     *            La bombe à identifer
     * @return L'identifiant de l'image, sous forme de byte
     */
    public static byte byteForBomb(Bomb bomb) {
        return (byte) (Integer.bitCount(bomb.fuseLength()) == 1 ? BYTE_FOR_WHITE_BOMB : BYTE_FOR_BLACK_BOMB);
    }

    /**
     * Identifie l'image à utiliser pour la particule d'explosion en fonction de
     * ses voisines, sans tenir compte du cas ou la case n'est pas libre
     * 
     * @param northCellIsBlasted
     *            Indique si la cellule au nord de la particule contient une
     *            particule d'explosions
     * @param eastCellIsBlasted
     *            Indique si la cellule à l'est de la particule contient une
     *            particule d'explosions
     * @param southCellIsBlasted
     *            Indique si la cellule au sud de la particule contient une
     *            particule d'explosions
     * @param westCellIsBlasted
     *            Indique si la cellule à l'ouest de la particule contient une
     *            particule d'explosions
     * @return L'identifiant de l'image à utiliser
     */
    public static byte byteForBlast(boolean northCellIsBlasted, boolean eastCellIsBlasted, boolean southCellIsBlasted, boolean westCellIsBlasted) {
        byte byteForBlast = 0;
        boolean[] neighboorBlast = { northCellIsBlasted, eastCellIsBlasted, southCellIsBlasted, westCellIsBlasted };

        for (int i = 0; i < neighboorBlast.length; i++) {
            byteForBlast = (byte) (byteForBlast << 1);
            byteForBlast = (byte) (byteForBlast | (neighboorBlast[i] ? BYTE_IF_TRUE : BYTE_IF_FALSE));
        }

        return byteForBlast;
    }

    /**
     * Identifie l'image à utiliser pour la particule d'explosion sur la case en
     * fonction de ses voisines et en tenant compte des cas ou la case est libre
     * ou si aucune explosion n'est présente sur la case
     * 
     * @param cell
     *            La case occupant une particule d'explosion
     * @param gameState
     *            L'état de jeu actuel
     * @return L'identifiant de l'image à utiliser, ou BYTE_FOR_EMPTY(
     *         {@value #BYTE_FOR_EMPTY}) si la particule d'explosion est sur une
     *         case non libre (mur destructible ou non destructible)
     * 
     */
    public static byte byteForBlast(Cell cell, GameState gameState) {
        Set<Cell> blastedCells = gameState.blastedCells();
        Block currentBlock = gameState.board().blockAt(cell);

        if (!currentBlock.isFree() || !blastedCells.contains(cell)) {
            return BYTE_FOR_EMPTY;
        }
        
        boolean northCellIsBlasted = blastedCells.contains(cell.neighbor(Direction.N));
        boolean eastCellIsBlasted = blastedCells.contains(cell.neighbor(Direction.E));
        boolean southCellIsBlasted = blastedCells.contains(cell.neighbor(Direction.S));
        boolean westCellIsBlasted = blastedCells.contains(cell.neighbor(Direction.W));

        return byteForBlast(northCellIsBlasted, eastCellIsBlasted, southCellIsBlasted, westCellIsBlasted);
    }

}
