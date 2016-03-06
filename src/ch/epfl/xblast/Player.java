package ch.epfl.xblast;

import java.util.Objects;

import ch.epfl.cs108.Sq;

/**
 * Représente un joueur
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Player {
    private PlayerID id;
    private Sq<LifeState> lifeStates;
    private Sq<DirectedPosition> directedPos;
    private int maxBombs;
    private int bombRange;

    /**
     * Construit un joueur avec les attributs donnés
     * 
     * @param id
     *            L'identifiant du joueur
     * @param lifeStates
     *            La séquence d'état su joueur
     * @param directedPos
     *            La séquence de position dirigée du joueur
     * @param maxBombs
     *            Le nombre maximum de bombe que peut poser le joueur
     * @param bombRange
     *            La portée des bombes du joueur
     * @throws IllegalArgumentException
     *             Si un des entiers passé en paramètre est strictement négatif
     * @throws NullPointerException
     *             Si un objet passé en argument est nul
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs,
            int bombRange) throws IllegalArgumentException, NullPointerException {

        this.id = Objects.requireNonNull(id, "id must not be null");
        this.lifeStates = Objects.requireNonNull(lifeStates, "lifeStates must not be null");
        this.directedPos = Objects.requireNonNull(directedPos, "directedPos must not be null");
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
    }
    
    

    /**
     * Représente un couple (nombre de vies, état) du joueur
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public final class LifeState {
        private int lives;
        private State state;

        /**
         * Construit le couple (nombre de vies, état) avec les valeurs données
         * 
         * @param lives
         *            Le nombre de vie du joueur
         * @param state
         *            L'état du joueur
         * @throws IllegalArgumentException
         *             Si le nombre de vies est strictement négatif
         * @throws NullPointerException
         *             Si l'état de l'objet est nul
         */
        public LifeState(int lives,
                State state) throws IllegalArgumentException, NullPointerException {
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);
        }

        /**
         * 
         * @return Le nombre de vie du joueur
         */
        public int lives() {
            return lives;
        }

        /**
         * 
         * @return L'état du joueur
         */
        public State state() {
            return state;
        }

        /**
         * Détermine si l'état permet au joueur de se déplacer, ce qui est le
         * cas uniquement s'il est invulnérable ou vulnérable
         * 
         * @return true si il peut se déplacer, false sinon
         */
        public boolean canMove() {
            return state == State.INVULNERABLE || state == State.VULNERABLE;
        }

    }

    /**
     * Représente la « position dirigée » d'un joueur, c-à-d une paire
     * (sous-case, direction).
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public final static class DirectedPosition {
        private SubCell position;
        private Direction direction;

        /**
         * Construit une position dirigée avec la position et la direction
         * donnés
         * 
         * @param position
         *            Position à utiliser
         * @param direction
         *            Direction à utiliser pour construire l'objet
         * @throws NullPointerException
         *             Si au moins un des objets passé en paramètre est nul
         */
        public DirectedPosition(SubCell position,
                Direction direction) throws NullPointerException {
            this.position = Objects.requireNonNull(position, "position must not be null");
            this.direction = Objects.requireNonNull(direction, "direction must not be null");
        }

        /**
         * Retourne une séquence infinie composée uniquement de la position
         * dirigée donnée et représentant un joueur arrêté dans cette position
         * 
         * @param p
         *            La position dirigée à utiliser pour la séquence
         * @return La séquence de position calculée
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);
        }

        /**
         * Retourne une séquence infinie de positions dirigées représentant un
         * joueur se déplaçant dans la direction dans laquelle il regarde ; le
         * premier élément de cette séquence est la position dirigée donnée, le
         * second a pour position la sous-case voisine de celle du premier
         * élément dans la direction de regard, et ainsi de suite
         * 
         * @param p
         *            La position dirigée à utiliser pour la séquence
         * @return La séquence de position calculée
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq.iterate(p, x -> x.withPosition(x.position().neighbor(x.direction())));
        }

        /**
         * @return La position de la position dirigiée
         */
        public SubCell position() {
            return position;
        }

        /**
         * Retourne une position dirigée dont la position est celle donnée, et
         * la direction est identique à celle du récepteur
         * 
         * @param newPosition
         *            La nouvelle position à utiliser pour la position dirigée
         * @return La position dirigée calculée avec la nouvelle position
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction);
        }

        /**
         * @return La direction de la position dirigée
         */
        public Direction direction() {
            return direction;
        }

        /**
         * Retourne une position dirigée dont la direction est celle donnée, et
         * la position est identique à celle du récepteur
         * 
         * @param newDirection
         *            La nouvelle direction à utiliser pour la position dirigée
         * @return La nouvelle position dirigée calculée avec la direction
         *         donnée
         */
        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(position, newDirection);
        }
    }

    /**
     * Représente les états des joueurs
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public enum State {
        /**
         * L'état du joueur invulnérable aux explosions, et ne peut donc pas
         * perdre de vie
         */
        INVULNERABLE,

        /**
         * L'état du joueur vulnérable (son état normal) et peut donc perdre une
         * vie s'il est atteint par une explosion
         */
        VULNERABLE,

        /**
         * L'état du joueur mourant
         */
        DYING,

        /**
         * L'état du joueur mort et qui ne participe donc plus au jeu
         */
        DEAD;

    }

}
