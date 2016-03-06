package ch.epfl.xblast;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class ArgumentChecker {

    private ArgumentChecker() {
        // non-instanciable
    }

    /**
     * Retourne la valeur donnée si elle est positive ou nulle
     * 
     * @param value
     *            La valeur à tester
     * @return La valeur donnée si elle est positive ou nulle
     * @throws IllegalArgumentException
     *             Si la valeur est strictement inférieure à zéro
     */
    public static int requireNonNegative(int value) throws IllegalArgumentException {
        if (value >= 0) {
            return value;
        } else {
            throw new IllegalArgumentException("La valeur doit être plus grande ou égale à zéro !");
        }
    }

}