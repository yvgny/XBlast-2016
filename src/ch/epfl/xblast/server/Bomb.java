package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Bomb {
    private PlayerID ownerId;
    private Cell position;
    private Sq<Integer> fuseLenghts;
    private int range;

    /**
     * Construit une bombe avec le propriétaire, la position, la séquence de
     * longueurs de mèche et la portée donnés
     * 
     * @param ownerId
     *            Le propriétaire de la bombe
     * @param position
     *            La position de la bombe
     * @param fuseLengths
     *            La séquence de longueurs de mèche
     * @param range
     *            La portée de la bombe
     * @throws NullPointerException
     *             Si soit le propiètaire, la position ou la portée est un objet
     *             nul
     * @throws IllegalArgumentException
     *             Si la portée est strictement ingérieure à zéro
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths,
            int range) throws NullPointerException, IllegalArgumentException {

        this.ownerId = Objects.requireNonNull(ownerId, "ownerId must not be null");
        this.position = Objects.requireNonNull(position, "position must not be null");
        this.fuseLenghts = Objects.requireNonNull(fuseLengths, "fuseLengths must not be null");
        this.range = ArgumentChecker.requireNonNegative(range);

        if (fuseLengths.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Construit une bombe avec le propriétaire, la position, la séquence de
     * longueurs de mèche et la portée donnés
     * 
     * @param ownerId
     *            Le propriétaire de la bombe
     * @param position
     *            La position de la bombe
     * @param fuseLengths
     *            La séquence de longueurs de mèche
     * @param range
     *            La portée de la bombe
     * @throws NullPointerException
     *             Si soit le propiètaire, la position ou la portée est un objet
     *             nul
     * @throws IllegalArgumentException
     *             Si la portée est strictement ingérieure à zéro
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength,
            int range) throws NullPointerException, IllegalArgumentException {

        this(ownerId, position, Sq.iterate(fuseLength, x -> x - 1).limit(fuseLength), range);
    }

    /**
     * 
     * @return Le propriétaire de la bombe
     */
    public PlayerID ownerId() {
        return ownerId;
    }

    /**
     * 
     * @return La position de la bombe
     */
    public Cell position() {
        return position;
    }

    /**
     * 
     * @return La séquence de longueurs de mèche de la bombe
     */
    public Sq<Integer> fuseLengths() {
        return fuseLenghts;
    }

    /**
     * 
     * @return La séquence de longueurs de mèche de la bombe
     */
    public int fuseLength() {
        return fuseLengths().head().intValue();
    }

    /**
     * 
     * @return La portée de la bombe
     */
    public int range() {
        return range;
    }

    /**
     * 
     * @return L'explosion correspondant à la bombe, sous la forme d'un
     *         tableau de 4 éléments, chacun représentant un bras ; la durée de
     *         cette explosion est donnée par la constante
     *         {@value ch.epfl.xblast.Ticks#Ticks.EXPLOSION_TICKS}
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> explosion = new ArrayList<Sq<Sq<Cell>>>();
        
        for (Direction direction : Direction.values()) {
            explosion.add(explosionArmTowards(direction));
        }
        
        return explosion;
    }

    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
        
        Sq<Cell> explosionArmInSpace = (Sq.iterate(position, c -> position.neighbor(dir))).limit(range);
        Sq<Sq<Cell>> explosionArmInTime = Sq.repeat(Ticks.EXPLOSION_TICKS, explosionArmInSpace);
        
        return explosionArmInTime;
    }
}
