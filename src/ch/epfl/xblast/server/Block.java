package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

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
    CRUMBLING_WALL,

    /**
     * Représente une case avec un bonus "bombe"
     */
    BONUS_BOMB(Bonus.INC_BOMB),

    /**
     * Représente une case avec un bonus "range"
     */
    BONUS_RANGE(Bonus.INC_RANGE);

    private Bonus maybeAssociatedBonus;

    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    private Block() {
        this.maybeAssociatedBonus = null;
    }

    /**
     * Vérifie si la case est libre
     * 
     * @return true si elle est libre (FREE), false sinon
     */
    public boolean isFree() {
        return (this == Block.FREE);
    }

    /**
     * @return true si la case est libre (de type FREE), false sinon
     */
    public boolean canHostPlayer() {
        return isFree() || isBonus();
    }

    /**
     * @return vrai si la case projette une ombre (si c'est une case de type
     *         mur), flase sinon
     */
    public boolean castsShadow() {
        return this.toString().endsWith("_WALL");

    }

    /**
     * @return Vrai si et seulement si la case est un bonus
     */
    public boolean isBonus() {
        return this.toString().startsWith("BONUS_");
    }

    /**
     * 
     * @return Le bonus associé à ce bloc
     * @throws NoSuchElementException
     *             Si aucun bonus n'est associé à ce bloc
     */
    public Bonus associatedBonus() throws NoSuchElementException {
        if (maybeAssociatedBonus != null) {
            return maybeAssociatedBonus;
        } else {
            throw new NoSuchElementException("Aucun bonus n'est associé à ce bloc !");
        }
    }

}
