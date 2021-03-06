package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Représente un joueur
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Player {
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPositions;
    private final int maxBombs;
    private final int bombRange;

    /**
     * Construit un joueur avec les attributs donnés
     * 
     * @param id
     *            L'identifiant du joueur
     * @param lifeStates
     *            La séquence d'état du joueur
     * @param directedPos
     *            La séquence de position dirigée du joueur
     * @param maxBombs
     *            Le nombre maximum de bombe que peut poser le joueur
     * @param bombRange
     *            La portée des bombes du joueur
     * @throws IllegalArgumentException
     *             Si un des entiers passés en paramètre est strictement négatif
     * @throws NullPointerException
     *             Si un objet passé en argument est nul
     */
    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs,
            int bombRange) throws IllegalArgumentException, NullPointerException {

        this.id = Objects.requireNonNull(id, "id must not be null");
        this.lifeStates = Objects.requireNonNull(lifeStates, "lifeStates must not be null");
        this.directedPositions = Objects.requireNonNull(directedPos, "directedPositions must not be null");
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);
    }

    /**
     * Construit un joueur avec les attributs donnés
     * 
     * @param id
     *            L'identifiant du joueur
     * @param lives
     *            Le nombre de vie que le joueur possède.
     * @param position
     *            La position du joueur
     * @param maxBombs
     *            Le nombre de bombes maximum que le joueur possède
     * @param bombRange
     *            La portée des bombes du joueur
     * @throws IllegalArgumentException
     *             Si au moins un des entiers passés en argument est strictement
     *             négatifs
     * @throws NullPointerException
     *             Si au moins un des objets passé en arguments est nul
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange) throws IllegalArgumentException, NullPointerException {

        this(id, createLifeSequence(lives), DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(Objects.requireNonNull(position, "position must not be null")), Direction.S)), maxBombs, bombRange);
    }

    /**
     * @return La séquence d'états pour la prochaine vie du joueur
     */
    public Sq<LifeState> statesForNextLife() {
        int newLives = lives() - 1;
        Sq<LifeState> nextLifeSequence = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), LifeState.State.DYING));
    
        nextLifeSequence = nextLifeSequence.concat(createLifeSequence(newLives));
    
        return nextLifeSequence;
    }

    private static Sq<LifeState> createLifeSequence(int lives) throws IllegalArgumentException {
        ArgumentChecker.requireNonNegative(lives);
        Sq<LifeState> lifeSequence;
    
        if (lives > 0) {
            lifeSequence = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, LifeState.State.INVULNERABLE));
            lifeSequence = lifeSequence.concat(Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE)));
        } else {
            lifeSequence = Sq.constant(new LifeState(0, Player.LifeState.State.DEAD));
        }
    
        return lifeSequence;
    }

    /**
     * Retourne un joueur identique à celui auquel on l'applique, si ce n'est
     * que son nombre maximum de bombes est celui donné
     * 
     * @param newMaxBombs
     *            Le nombre de bombes maximum que peut poser le nouveau joueur
     * @return Le nouveau joueur
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id(), lifeStates(), directedPositions(), newMaxBombs, bombRange());
    }

    /**
     * Retourne un joueur identique à celui auquel on l'applique, si ce n'est
     * que la portée de ses bombes est celle donnée
     * 
     * @param newBombRange
     *            La portée des bombes du nouveau joueur
     * @return Le nouveau joueur
     */
    public Player withBombRange(int newBombRange) {
        return new Player(id(), lifeStates(), directedPositions(), maxBombs(), newBombRange);
    }

    /**
     * @return Une bombe positionnée sur la case sur laquelle le joueur se
     *         trouve actuellement, dont la mèche a la longueur donnée par la
     *         constante {@value ch.epfl.xblast.server.Ticks#BOMB_FUSE_TICKS} et
     *         dont la portée est celle des bombes du joueur.
     */
    public Bomb newBomb() {
        return new Bomb(id(), position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange());
    }

    /**
     * @return L'identifiant du joueur
     */
    public PlayerID id() {
        return id;
    }

    /**
     * @return La séquence des couples (nombre de vies, état) du joueur
     */
    public Sq<LifeState> lifeStates() {
        return lifeStates;
    }

    /**
     * @return Le couple (nombre de vies, état) actuel du joueur
     */
    public LifeState lifeState() {
        return lifeStates().head();
    }

    /**
     * @return Vrai si et seulement si le joueur est vivant, c-à-d si son nombre
     *         de vies actuel est supérieur à 0
     */
    public boolean isAlive() {
        return lifeState().lives() > 0;
    }

    /**
     * @return Le nombre de vies actuel du joueur
     */
    public int lives() {
        return lifeState().lives();
    }

    /**
     * @return La séquence des positions dirigées du joueur
     */
    public Sq<DirectedPosition> directedPositions() {
        return directedPositions;
    }

    /**
     * @return La position actuelle du joueur
     */
    public SubCell position() {
        return directedPositions().head().position();
    }

    /**
     * @return La direction vers laquelle le joueur regarde actuellement
     */
    public Direction direction() {
        return directedPositions().head().direction();
    }

    /**
     * @return Le nombre maximum de bombes que le joueur peut déposer
     */
    public int maxBombs() {
        return maxBombs;
    }

    /**
     * @return La portée (en nombre de cases) des explosions produites par les
     *         bombes du joueur
     */
    public int bombRange() {
        return bombRange;
    }

    /**
     * Représente un couple (nombre de vies, état) du joueur
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public static final class LifeState {
        private final int lives;
        private final State state;

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
            this.state = Objects.requireNonNull(state, "state must not be null");
        }

        /**
         * Détermine si l'état permet au joueur de se déplacer, ce qui est le
         * cas uniquement s'il est invulnérable ou vulnérable
         * 
         * @return vrai si il peut se déplacer, faux sinon
         */
        public boolean canMove() {
            return state() == State.INVULNERABLE || state() == State.VULNERABLE;
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
             * L'état du joueur vulnérable (son état normal) et peut donc perdre
             * une vie s'il est atteint par une explosion
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

    /**
     * Représente la «position dirigée» d'un joueur, c-à-d une paire (sous-case,
     * direction).
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public static final class DirectedPosition {
        private final SubCell position;
        private final Direction direction;

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
         * joueur se déplaçant dans la direction dans laquelle il regarde; le
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
         * Retourne une position dirigée dont la position est celle donnée, et
         * la direction est identique à celle du récepteur
         * 
         * @param newPosition
         *            La nouvelle position à utiliser pour la position dirigée
         * @return La position dirigée calculée avec la nouvelle position
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction());
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
            return new DirectedPosition(position(), newDirection);
        }

        /**
         * @return La position de la position dirigiée
         */
        public SubCell position() {
            return position;
        }

        /**
         * @return La direction de la position dirigée
         */
        public Direction direction() {
            return direction;
        }
    }

}
