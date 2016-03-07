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
     * Construit un joueur avec les attributs donnés
     * 
     * @param id
     *            L'identifiant du joueur
     * @param lives
     *            Le nombre de vie que le joueur posssède.
     * @param position
     *            La position du joueur
     * @param maxBombs
     *            Le nombre de bombes maximum que le joueur possède
     * @param bombRange
     *            La portée des bombes du joueu
     * @throws IllegalArgumentException
     *             Si au moins un des entiers passé en argument est strictement
     *             négatifs
     * @throws NullPointerException
     *             Si au moins un des objets passé en arguments est nul
     */
    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange) throws IllegalArgumentException, NullPointerException {

        this(Objects.requireNonNull(id), createLifeSequence(lives), Sq.constant(new DirectedPosition(SubCell.centralSubCellOf(Objects.requireNonNull(position)), Direction.S)), maxBombs, bombRange);
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
     * @return La séquence d'états pour la prochaine vie du joueur
     */
    public Sq<LifeState> statesForNextLife() {
        int newLives = lives() - 1;
        Sq<LifeState> nextLifeSequence = Sq.repeat(Ticks.PLAYER_DYING_TICKS, new LifeState(lives(), LifeState.State.DYING));

        if (!isAlive()) {
            nextLifeSequence.concat(Sq.constant(new LifeState(0, LifeState.State.DEAD)));
        } else {
            nextLifeSequence.concat(Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(newLives, LifeState.State.INVULNERABLE)));
            nextLifeSequence.concat(Sq.constant(new LifeState(newLives, LifeState.State.VULNERABLE)));
        }

        return nextLifeSequence;
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
        return directedPos;
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
     * Retourne un joueur identique à celui auquel on l'applique, si ce n'est
     * que son nombre maximum de bombes est celui donné
     * 
     * @param newMaxBombs
     *            Le nombre de bombes maximum que peut poser le nouveau joueur
     * @return Le nouveau joueur
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }

    /**
     * @return La portée (en nombre de cases) des explosions produites par les
     *         bombes du joueur
     */
    public int bombRange() {
        return bombRange;
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
        return new Player(id, lifeStates, directedPos, maxBombs, newBombRange);
    }

    /**
     * @return Une bombe positionnée sur la case sur laquelle le joueur se
     *         trouve actuellement, dont la mèche a la longueur donnée par la
     *         constante {@value ch.epfl.xblast.server.Ticks#BOMB_FUSE_TICKS} et
     *         dont la portée est celle des bombes du joueur.
     */
    public Bomb newBomb() {
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS, bombRange);
    }

    private static Sq<LifeState> createLifeSequence(int lives) throws IllegalArgumentException {
        ArgumentChecker.requireNonNegative(lives);

        Sq<LifeState> lifeSequence = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(lives, LifeState.State.INVULNERABLE));
        Sq<LifeState> constantVulnerable = Sq.constant(new LifeState(lives, LifeState.State.VULNERABLE));
        lifeSequence.concat(constantVulnerable);

        return lifeSequence;
    }

    /**
     * Représente un couple (nombre de vies, état) du joueur
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public final static class LifeState {
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


}
