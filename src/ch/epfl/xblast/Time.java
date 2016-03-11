package ch.epfl.xblast;

/**
 * Définit plusieurs constantes entières liées au temps
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public interface Time {
    /**
     * Le nombre de secondes par minute
     */
    public static final int S_PER_MIN = 60;

    /**
     * Le nombre de milisecondes par seconde
     */
    public static final int MS_PER_S = 1000;

    /**
     * Le nombre de microsecondes par seconde
     */
    public static final int US_PER_S = 1000 * MS_PER_S;

    /**
     * Le nombre de nanosecondes par seconde
     */
    public static final int NS_PER_S = 1000 * US_PER_S;
}
