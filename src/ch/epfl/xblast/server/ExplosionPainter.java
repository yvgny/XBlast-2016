package ch.epfl.xblast.server;

/**
 * Représente un peintre de bombes et d'explosions
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class ExplosionPainter {
    /**
     * Indentifiant à utiliser pour les cases dénués de particules d'explosions
     */
    public static final byte BYTE_FOR_EMPTY = 16;

    private ExplosionPainter() {
        // Non-instanciable
    }

    /**
     * Retourne l'octet identifiant l'image à utiliser pour dessiner la bombe
     * qu'on lui passe en argument
     * 
     * @param bomb
     *            La bombe à identifer
     * @return L'indentifiant de l'image, sous forme de byte
     */
    public static byte byteForBomb(Bomb bomb) {
        return (byte) (Integer.bitCount(bomb.fuseLength()) == 1 ? 21 : 20);
    }

    /**
     * Identifie l'image à utiliser pour la particule d'explosion en fonction de
     * ses voisines
     * 
     * @param northCellIsBlasted
     *            Indique si la cellule au nord de la particule contient une
     *            particule d'explosions
     * @param eastCellisBlastedboolean
     *            Indique si la cellule au est de la particule contient une
     *            particule d'explosions
     * @param southCellIsBlasted
     *            Indique si la cellule au sud de la particule contient une
     *            particule d'explosions
     * @param westCellIsBlasted
     *            Indique si la cellule au ouest de la particule contient une
     *            particule d'explosions
     * @return L'indentifiant de l'image à utiliser
     */
    public static byte byteforBlast(boolean northCellIsBlasted, boolean eastCellisBlastedboolean, boolean southCellIsBlasted, boolean westCellIsBlasted) {
        byte byteForBlast = 0;
        boolean[] neighboorBlast = { northCellIsBlasted, eastCellisBlastedboolean, southCellIsBlasted, westCellIsBlasted };

        for (int i = 0; i < neighboorBlast.length; i++) {
            byteForBlast = (byte) (byteForBlast << 1);
            byteForBlast = (byte) (byteForBlast | (neighboorBlast[i] ? 1 : 0));
        }

        return byteForBlast;
    }
}
