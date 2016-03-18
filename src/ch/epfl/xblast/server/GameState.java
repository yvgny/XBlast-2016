package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Collections;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;

/**
 * Représente l'état d'une partie
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class GameState {
    private int ticks;
    private Board board;
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Sq<Sq<Cell>>> explosions;
    private List<Sq<Cell>> blasts;
    public List<List<PlayerID>> playerIdPermutations;
    private static final Random RANDOM = new Random(2016);
    private static final List<Block> appearableBonus = Collections.unmodifiableList(
            Arrays.asList(Block.BONUS_BOMB, Block.BONUS_RANGE, Block.FREE));

    /**
     * Construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les
     * joueurs, les bombes, les explosions et les particules d'explosion donnés
     * 
     * @param ticks
     *            Le coup d'horloge de l'état du jeu
     * @param board
     *            La plaeatu de jeu
     * @param players
     *            La liste des quatre joueurs
     * @param bombs
     *            Les bombes du jeu
     * @param explosions
     *            Les explosions du jeu
     * @param blasts
     *            Les particules d'epxlosion
     * @throws IllegalArgumentException
     *             Si le coup d'horloge est strcitement négatif
     * @throws NullPointerException
     *             Si un des objets est null
     */
    public GameState(int ticks, Board board, List<Player> players, List<Bomb> bombs,
            List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts) throws IllegalArgumentException, NullPointerException {

        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board, "board must not be null");
        this.players = Objects.requireNonNull(players, "players must not be null");
        if (players.size() != 4) {
            throw new IllegalArgumentException("La liste de joueurs ne contient pas 4 éléments !");
        }
        this.explosions = Objects.requireNonNull(explosions, "explosions must not be null");
        this.blasts = Objects.requireNonNull(blasts, "blasts must not be null");
        this.bombs = Objects.requireNonNull(bombs, "bombs must not be null");
        this.playerIdPermutations = Lists.permutations(Arrays.asList(PlayerID.values()));
    }

    /**
     * Construit l'état du jeu pour le plateau et les joueurs donnés, pour le
     * coup d'horloge 0 et aucune bombe, explosion ou particule d'explosion
     * 
     * @param board
     *            Le plateau de jeu à utiliser
     * @param players
     *            La liste de joueur à utiliser
     * @throws IllegalArgumentException
     *             Si il n'y a pas quatre joueurs dans la liste de joueurs
     * @throws NullPointerException
     *             Si un des objets est null
     */
    public GameState(Board board,
            List<Player> players) throws IllegalArgumentException, NullPointerException {

        this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    /**
     * @return Le coup d'horloge correspondant à l'état
     */
    public int ticks() {
        return ticks;
    }

    /**
     * @return Retourne vrai si et seulement si l'état correspond à une partie
     *         terminée, c-à-d si le nombre de coups d'horloge d'une partie (
     *         {@value ch.epfl.xblast.server.Ticks#TOTAL_TICKS}) est écoulé, ou
     *         s'il n'y a pas plus d'un joueur vivant.
     */
    public boolean isGameOver() {
        return ticks >= Ticks.TOTAL_TICKS || alivePlayers().size() < 2;
    }

    /**
     * @return Le temps restant dans la partie, en secondes
     */
    public double remainingTime() {
        double remainingTime = (Ticks.TOTAL_TICKS - ticks) / (double) Ticks.TICKS_PER_SECOND;
        if (remainingTime < 0.0) {
            return 0.0;
        } else {
            return remainingTime;
        }
    }

    /**
     * @return L'identité du vainqueur de cette partie s'il y en a un, sinon la
     *         valeur optionnelle vide
     */
    public Optional<PlayerID> winner() {
        return alivePlayers().size() == 1 ? Optional.of(
                alivePlayers().get(0).id()) : Optional.empty();
    }

    /**
     * @return Le plateau de jeu
     */
    public Board board() {
        return board;
    }

    /**
     * @return La liste de joueurs
     */
    public List<Player> players() {
        return new ArrayList<Player>(players);
    }

    /**
     * @return Les joueurs vivants, c-à-d ceux ayant au moins une vie
     */
    public List<Player> alivePlayers() {
        ArrayList<Player> alivePlayers = new ArrayList<Player>();
        for (Player player : players) {
            if (player.isAlive()) {
                alivePlayers.add(player);
            }
        }

        return alivePlayers;
    }

    /**
     * Calcule les particules d'explosion pour l'état suivant
     * 
     * @param blasts0
     *            L'état courant du jeu
     * @param board0
     *            Le plateau de jeu courant
     * @param explosions0
     *            Les explosions courantes
     * @return Les particuels d'explosions pour l'état suivant
     */
    public static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0,
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();

        for (Sq<Cell> blastSq : blasts0) {
            if (board0.blockAt(blastSq.head()).isFree() && !blastSq.tail().isEmpty()) {
                blasts1.add(blastSq.tail());
            }
        }

        for (Sq<Sq<Cell>> explosionSq : explosions0) {
            blasts1.add(explosionSq.head());
        }

        return blasts1;
    }

    /**
     * @return Une table associant les bombes aux cases qu'elles occupent
     */
    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombedCellsMap = new HashMap<>();
        for (Bomb bomb : bombs) {
            bombedCellsMap.put(bomb.position(), bomb);
        }

        return bombedCellsMap;

    }

    /**
     * @return L'ensemble des cases sur lesquelles se trouve au moins une
     *         particule d'explosion
     */
    public Set<Cell> blastedCells() {
        Set<Cell> blastedCells = new HashSet<>();

        for (Sq<Cell> sq : blasts) {
            while (!sq.isEmpty()) {
                blastedCells.add(sq.head());
                sq = sq.tail();
            }
        }

        return blastedCells;
    }

    /**
     * Retourne l'état du jeu pour le coup d'horloge suivant, en fonction de
     * l'actuel
     * 
     * @param speedChangeEvents
     *            //TODO
     * @param bombDropEvents
     *            La liste des identifiants joueurs qui ont posé une bombe
     * @return l'état du jeu pour le coup d'horloge suivant
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {

        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);

        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        Set<Cell> consumedBonuses = new HashSet<>();
        for (Player player : players) {
            if (board.blockAt(player.position().containingCell()).isBonus()) {
                consumedBonuses.add(player.position().containingCell());
                playerBonuses.put(player.id(),
                        board.blockAt(player.position().containingCell()).associatedBonus());
            }
        }

        Set<Cell> blastedCells = new HashSet<>();
        for (Sq<Cell> sq : blasts1) {
            while (!sq.isEmpty()) {
                blastedCells.add(sq.head());
                sq = sq.tail();
            }
        }

        Board board1 = nextBoard(board, consumedBonuses, blastedCells);

        List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);

        List<Bomb> bombs1 = newlyDroppedBombs(players, bombDropEvents, bombs);

        Set<Cell> bombedCells = new HashSet<>();
        for (Bomb bomb : bombs1) {
            bombedCells.add(bomb.position());
        }
        List<Player> players1 = nextPlayers(players, playerBonuses, bombedCells, board1,
                blastedCells, speedChangeEvents);

        return new GameState(ticks + 1, board1, players1, bombs1, explosions1, blasts1);
    }

    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {

        List<Sq<Block>> blocksList = new ArrayList<Sq<Block>>();
        Block tempBlock;
        Cell currentPosition;

        for (int y = 0; y < Cell.ROWS; y++) {
            for (int x = 0; x < Cell.COLUMNS; x++) {
                currentPosition = new Cell(x, y);
                tempBlock = board0.blockAt(currentPosition);

                // Enlève les bonus consumés par les joueurs
                if (consumedBonuses.contains(currentPosition)) {
                    blocksList.add(Sq.constant(Block.FREE));

                    // Transforme les murs destructible atteints par une
                    // explosion en mur en train de se détruire
                } else if (tempBlock == Block.DESTRUCTIBLE_WALL && blastedCells1.contains(
                        currentPosition)) {
                    Sq<Block> crumblingWall = Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
                            Block.CRUMBLING_WALL);

                    // Concaténation avec une séquence infinie d'un bloc pris au
                    // hasard dans la liste de bonus ou de case libre disponible
                    crumblingWall = crumblingWall.concat(
                            Sq.constant(appearableBonus.get(RANDOM.nextInt(4))));
                    blocksList.add(crumblingWall);

                } else if (tempBlock.isBonus() && blastedCells1.contains(currentPosition)) {
                    Sq<Block> disappearingBonus = board0.blocksAt(currentPosition).limit(
                            Ticks.BONUS_DISAPPEARING_TICKS);
                    disappearingBonus = disappearingBonus.concat(Sq.constant(Block.FREE));
                    blocksList.add(disappearingBonus);

                } else {
                    blocksList.add(Sq.constant(tempBlock));
                }

            }
        }

        return new Board(blocksList);
    }

    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1, Board board1,
            Set<Cell> blastedCells1, Map<PlayerID, Optional<Direction>> speedChangeEvents) {

        return players0;
    }

    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

        for (Sq<Sq<Cell>> sq : explosions1) {
            explosions1.add(sq.tail());
        }

        return explosions1;
    }

    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents,
            List<Bomb> bombs0) {

        List<Player> authorisedPlayers = new ArrayList<>(players0);
        int playerBombsOnBoard;
        List<Bomb> bombs1 = new ArrayList<>();

        // Iteration sur players0 pour trouver les joueurs à supprimer dans
        // authorisedPlayers pour ne laisser que ceux ayant le droit de poser
        // des bombes
        for (Player player : players0) {
            playerBombsOnBoard = 0;

            for (Bomb bomb : bombs0) {
                if (bomb.ownerId() == player.id()) {
                    playerBombsOnBoard++;
                }

                if (bomb.position() == player.position().containingCell()) {
                    authorisedPlayers.remove(player);
                }
            }

            if (!player.isAlive() || playerBombsOnBoard > player.maxBombs()) {
                authorisedPlayers.remove(player);
            }
        }

        for (Player player : authorisedPlayers) {
            if (bombDropEvents.contains(player.id())) {
                boolean bombAlreadyHere = false;

                for (Bomb bomb : bombs1) {

                    if (bomb.position() == player.position().containingCell()) {
                        bombAlreadyHere = true;

                    }
                }

                if (!bombAlreadyHere) {
                    bombs1.add(
                            new Bomb(player.id(), player.position().containingCell(), Ticks.BOMB_FUSE_TICKS, player.bombRange()));
                }

            }
        }

        return bombs1;
    }

}
