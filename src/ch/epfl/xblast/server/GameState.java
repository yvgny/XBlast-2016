package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;

/**
 * Représente l'état d'une partie
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class GameState {
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;
    private static final List<List<PlayerID>> playerIdPermutations = createUnmodifiableView(Lists.permutations(Arrays.asList(PlayerID.values())));
    private static final Random RANDOM = new Random(2016);
    private static final List<Block> appearableBonus = Collections.unmodifiableList(Arrays.asList(Block.BONUS_BOMB, Block.BONUS_RANGE, Block.FREE));

    /**
     * Construit l'état du jeu pour le coup d'horloge, le plateau de jeu, les
     * joueurs, les bombes, les explosions et les particules d'explosion donnés
     * 
     * @param ticks
     *            Le coup d'horloge de l'état du jeu
     * @param board
     *            La plateau de jeu
     * @param players
     *            La liste des quatre joueurs
     * @param bombs
     *            Les bombes du jeu
     * @param explosions
     *            Les explosions du jeu
     * @param blasts
     *            Les particules d'explosion
     * @throws IllegalArgumentException
     *             Si le coup d'horloge est strcitement négatif
     * @throws NullPointerException
     *             Si un des objets est nul
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts) throws IllegalArgumentException, NullPointerException {

        this.ticks = ArgumentChecker.requireNonNegative(ticks);
        this.board = Objects.requireNonNull(board, "board must not be null");
        this.players = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(players, "players must not be null")));
        if (players.size() != 4) {
            throw new IllegalArgumentException("La liste de joueurs ne contient pas 4 éléments !");
        }
        this.explosions = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(explosions, "explosions must not be null")));
        this.blasts = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(blasts, "blasts must not be null")));
        this.bombs = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(bombs, "bombs must not be null")));
        Lists.permutations(Arrays.asList(PlayerID.values()));
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
     *             Si un des objets est nul
     */
    public GameState(Board board,
            List<Player> players) throws IllegalArgumentException, NullPointerException {

        this(0, board, players, new ArrayList<Bomb>(), new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    /**
     * Calcule une liste non-modifiable en prodondeur
     * 
     * @param list
     *            La liste à rendre non-modifiable
     * @return La liste non-modifiable de liste(s) non-modifibale(s)
     */
    private static <E> List<List<E>> createUnmodifiableView(List<List<E>> list) {
        List<List<E>> copiedList = new ArrayList<>();

        for (List<E> eachList : list) {
            copiedList.add(Collections.unmodifiableList(eachList));
        }

        return Collections.unmodifiableList(copiedList);
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
        return ticks > Ticks.TOTAL_TICKS || alivePlayers().size() < 2;
    }

    /**
     * @return Le temps restant dans la partie, en secondes
     */
    public double remainingTime() {
        double remainingTime = (Ticks.TOTAL_TICKS - ticks) / (double) Ticks.TICKS_PER_SECOND;

        return remainingTime < 0.0 ? 0.0 : remainingTime;
    }

    /**
     * @return L'identité du vainqueur de cette partie s'il y en a un, sinon la
     *         valeur optionnelle vide
     */
    public Optional<PlayerID> winner() {
        return alivePlayers().size() == 1 ? Optional.of(alivePlayers().get(0).id()) : Optional.empty();
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
     * @return Une table associant les bombes aux cases qu'elles occupent
     */
    public Map<Cell, Bomb> bombedCells() {
        return bombedCells(bombs);
    }

    /**
     * Calcule l'ensemble des cases sur lesquelles se trouvent une bombe
     * 
     * @param bombs
     *            La liste des bombes de l'état actuel
     * @return Les cases sur lesquelles se trouvent une bombe
     * 
     */
    private static Map<Cell, Bomb> bombedCells(List<Bomb> bombs) {
        Map<Cell, Bomb> bombedCellsMap = new HashMap<>();
        bombs.forEach(u -> bombedCellsMap.put(u.position(), u));

        return bombedCellsMap;
    }

    /**
     * @return L'ensemble des cases sur lesquelles se trouve au moins une
     *         particule d'explosion
     */
    public Set<Cell> blastedCells() {
        return blastedCells(blasts);
    }

    /**
     * Calcule l'ensemble des cases sur lesquelles se trouvent au moins une
     * particule d'explosion
     * 
     * @param blasts0
     *            Les branches d'explosions de l'état actuel
     * @return Les cases sur lesquelles se trouvent au moins une particule
     *         d'explosion
     */
    private static Set<Cell> blastedCells(List<Sq<Cell>> blasts0) {
        Set<Cell> blastedCells = new HashSet<>();

        for (Sq<Cell> sq : blasts0) {
            if (!sq.isEmpty()) {
                blastedCells.add(sq.head());
            }
        }

        return blastedCells;
    }

    /**
     * Retourne l'état du jeu pour le coup d'horloge suivant, en fonction de
     * l'actuel
     * 
     * @param speedChangeEvents
     *            Les événements de changement de direction pour les joueurs
     *            désirant changer de direction
     * @param bombDropEvents
     *            La liste des identifiants joueurs qui ont posé une bombe
     * @return l'état du jeu pour le coup d'horloge suivant
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents, Set<PlayerID> bombDropEvents) {

        Comparator<Player> playerComparator = (player1, player2) -> {
            List<PlayerID> currentPermutation = playerIdPermutations.get(ticks % playerIdPermutations.size());
            if (currentPermutation.indexOf(player1.id()) < currentPermutation.indexOf(player2.id())) {
                return -1;

            } else if (currentPermutation.indexOf(player1.id()) > currentPermutation.indexOf(player2.id())) {
                return 1;

            } else {
                return 0;
            }
        };
        List<Player> playersSorted = new ArrayList<>(players);
        playersSorted.sort(playerComparator);

        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);

        // Création de playerBonuses et consumedBonuses
        Map<PlayerID, Bonus> playerBonuses = new HashMap<>();
        Set<Cell> consumedBonuses = new HashSet<>();
        for (Player player : playersSorted) {
            if (player.position().isCentral() && board.blockAt(player.position().containingCell()).isBonus()) {
                consumedBonuses.add(player.position().containingCell());
                playerBonuses.put(player.id(), board.blockAt(player.position().containingCell()).associatedBonus());
            }
        }

        // Création de blastedCells
        Set<Cell> blastedCells1 = blastedCells(blasts1);

        Board board1 = nextBoard(board, consumedBonuses, blastedCells1);

        List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);

        List<Bomb> bombs1 = new ArrayList<>();
        List<Bomb> newlyDroppedBombs = newlyDroppedBombs(playersSorted, bombDropEvents, bombs);

        // Evolution et ajout des bombes nouvellement créer
        for (Bomb bomb : newlyDroppedBombs) {
            if (blastedCells1.contains(bomb.position())) {
                explosions1.addAll(bomb.explosion());
            } else if (bomb.fuseLength() <= 1) {
                explosions1.addAll(bomb.explosion());
            } else {
                bombs1.add(new Bomb(bomb.ownerId(), bomb.position(), bomb.fuseLengths().tail(), bomb.range()));
            }
        }

        // Evolution des bombes présentes a ce moment-la
        for (Bomb bomb : bombs) {
            if (blastedCells1.contains(bomb.position())) {
                explosions1.addAll(bomb.explosion());
            } else if (bomb.fuseLength() <= 1) {
                explosions1.addAll(bomb.explosion());
            } else {
                bombs1.add(new Bomb(bomb.ownerId(), bomb.position(), bomb.fuseLengths().tail(), bomb.range()));
            }
        }

        Set<Cell> bombedCells = bombedCells(bombs1).keySet();

        List<Player> players1 = nextPlayers(players, playerBonuses, bombedCells, board1, blastedCells1, speedChangeEvents);

        return new GameState(ticks + 1, board1, players1, bombs1, explosions1, blasts1);
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
     * @return Les particules d'explosion pour l'état suivant
     */
    public static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();

        for (Sq<Cell> blastSq : blasts0) {
            if (!blastSq.isEmpty() && board0.blockAt(blastSq.head()).isFree() && !blastSq.tail().isEmpty()) {
                blasts1.add(blastSq.tail());
            }
        }

        for (Sq<Sq<Cell>> explosionSq : explosions0) {
            if (!explosions0.isEmpty()) {
                blasts1.add(explosionSq.head());
            }
        }

        return blasts1;
    }

    /**
     * Calcule le prochain état du plateau
     * 
     * @param board0
     *            Le plateau actuel
     * @param consumedBonuses
     *            Les bonus consommés par les joueurs
     * @param blastedCells1
     *            Les cases du prochain états qui contiennent une particule
     *            d'explosion
     * @return
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses, Set<Cell> blastedCells1) {
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
                } else if (tempBlock == Block.DESTRUCTIBLE_WALL && blastedCells1.contains(currentPosition)) {
                    Sq<Block> crumblingWall = Sq.repeat(Ticks.WALL_CRUMBLING_TICKS, Block.CRUMBLING_WALL);

                    // Concaténation avec une séquence infinie d'un bloc pris au
                    // hasard dans la liste de bonus/case libre disponible
                    Block chosenBlock = appearableBonus.get(RANDOM.nextInt(appearableBonus.size()));

                    crumblingWall = crumblingWall.concat(Sq.constant(chosenBlock));

                    blocksList.add(crumblingWall);

                } else if (tempBlock.isBonus() && blastedCells1.contains(currentPosition)) {
                    Sq<Block> disappearingBonus = board0.blocksAt(currentPosition).limit(Ticks.BONUS_DISAPPEARING_TICKS);
                    disappearingBonus = disappearingBonus.concat(Sq.constant(Block.FREE));
                    blocksList.add(disappearingBonus);

                } else {
                    blocksList.add(board0.blocksAt(currentPosition).tail());
                }

            }
        }

        return new Board(blocksList);
    }

    /**
     * Fait évoluer les joueurs pour le prochain état
     * 
     * @param players0
     *            Les joueurs actuels
     * @param playerBonuses
     *            Associe les bonus collectés à leurs identités
     * @param bombedCells1
     *            Les cellules sur lesquelles se trouvent une bombe
     * @param board1
     *            Le plateau de jeu du prochain état
     * @param blastedCells1
     *            Les cases du prochain état qui contiennent une particule
     *            d'explosion
     * @param speedChangeEvents
     *            Les événements de changement de direction pour les joueurs
     *            désirant changer de direction
     * @return Les joueurs pour le prochain état
     */
    private static List<Player> nextPlayers(List<Player> players0, Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1, Board board1, Set<Cell> blastedCells1, Map<PlayerID, Optional<Direction>> speedChangeEvents) {

        List<Player> players1 = new ArrayList<>();
        boolean evolve;

        for (Player player : players0) {
            SubCell centralSubCell1Position;
            DirectedPosition directedPosition1;
            Sq<DirectedPosition> directedPositions1 = player.directedPositions();
            DirectedPosition centralSubCell1;

            //
            // Evolution de la position
            //

            evolve = true;

            // On trouve les coordonnées de la prochaine SubCell centrale
            centralSubCell1 = (directedPositions1.findFirst(u -> u.position().isCentral()));
            centralSubCell1Position = centralSubCell1.position();

            // Si le joueur veut changer de direction/s'arrêter quand on
            // retourne en arrière
            if (speedChangeEvents.containsKey(player.id())) {
                Optional<Direction> chosenDirection = speedChangeEvents.get(player.id());

                // On coupe la séquence quand on arrive à la prochaine
                // case centrale (case centrale exclue)
                directedPositions1 = directedPositions1.takeWhile(u -> !u.position().isCentral());

                if (chosenDirection.isPresent()) {

                    if (!chosenDirection.get().isParallelTo(player.direction())) {

                        // On rajoute la séquence après la changement de
                        // direction s'il y en a une
                        directedPosition1 = new DirectedPosition(centralSubCell1Position, chosenDirection.get());
                        directedPositions1 = directedPositions1.concat(DirectedPosition.moving(directedPosition1));

                    } else {

                        // Si le joueur retourne en arrière il peut directement
                        // le faire
                        directedPosition1 = new DirectedPosition(player.position(), chosenDirection.get());
                        directedPositions1 = DirectedPosition.moving(directedPosition1);
                    }

                } else {
                    // On rajoute la séquence constante pour le joueur stoppé
                    directedPosition1 = new DirectedPosition(centralSubCell1Position, centralSubCell1.direction());
                    directedPositions1 = directedPositions1.concat(DirectedPosition.stopped(directedPosition1));
                }
            }

            SubCell subCell0 = player.position();
            Cell cell0 = subCell0.containingCell();

            Cell cell1 = directedPositions1.head().position().containingCell().neighbor(directedPositions1.head().direction());

            // Test si le joueur est mourant
            if (!player.lifeState().canMove()) {
                evolve = false;
            }

            // Test si il y a un mur
            if (!board1.blockAt(cell1).canHostPlayer()) { 
                if (subCell0.isCentral()) {
                    evolve = false;
                }
            }

            // Test si il y une bombe
            if (bombedCells1.contains(cell0)) { 

                // On calcule si il se trouve sur la case sur laquelle il doit
                // s'arrêter si il y a une bombe

                SubCell blockedSubCell = SubCell.centralSubCellOf(cell0);

                while (blockedSubCell.distanceToCentral() < 6) {
                    blockedSubCell = blockedSubCell.neighbor(player.direction().opposite());
                }

                if (subCell0.equals(blockedSubCell)) {
                    evolve = false;
                }
            }

            // On fait évoluer la séquence de position dirigée
            if (evolve) {
                directedPositions1 = directedPositions1.tail();
            }

            //
            // Evolution de l'état
            //

            Sq<LifeState> lifeStates1 = player.lifeStates();
            if (blastedCells1.contains(directedPositions1.head().position().containingCell()) && player.lifeState().state() == Player.LifeState.State.VULNERABLE) {
                lifeStates1 = player.statesForNextLife();
            } else {
                lifeStates1 = lifeStates1.tail();
            }

            //
            // Evolution des capacités
            //

            Player playerWithBonus = player;
            if (playerBonuses.containsKey(player.id())) {
                playerWithBonus = playerBonuses.get(player.id()).applyTo(player);
            }

            // Rajout du joueur dans la nouvelle liste
            players1.add(new Player(player.id(), lifeStates1, directedPositions1, playerWithBonus.maxBombs(), playerWithBonus.bombRange()));

        }

        return players1;
    }

    /**
     * Calcule les explosions pour le prochain état en fonction des actuelles
     * 
     * @param explosions0
     *            Les explosions actuelles
     * @return Les explosions pour le prochain état
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<>();

        for (Sq<Sq<Cell>> sq : explosions0) {
            if (!sq.tail().isEmpty()) {
                explosions1.add(sq.tail());
            }
        }

        return explosions1;
    }

    /**
     * Calcule les nouvelles bombes posée en fonction des règles (qui a le droit
     * de poser une bombe, a quel endroit, etc...)
     * 
     * @param players0
     *            La liste des joueurs actuels
     * @param bombDropEvents
     *            Les événements de dépôts de bombes
     * @param bombs0
     *            La liste des bombes actuelles
     * @return La liste des bombes nouvellement posées
     */
    private static List<Bomb> newlyDroppedBombs(List<Player> players0, Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
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

                if (bomb.position().equals(player.position().containingCell())) {
                    authorisedPlayers.remove(player);
                }
            }


            if (!player.isAlive() || (playerBombsOnBoard >= player.maxBombs())) {
                authorisedPlayers.remove(player);
            }
        }

        for (Player player : authorisedPlayers) {
            if (bombDropEvents.contains(player.id())) {
                boolean bombAlreadyHere = false;

                for (Bomb bomb : bombs1) {
                    if (bomb.position().equals(player.position().containingCell())) {
                        bombAlreadyHere = true;
                    }
                }

                for (Bomb bomb : bombs0) {
                    if (bomb.position().equals(player.position().containingCell())) {
                        bombAlreadyHere = true;
                    }
                }

                if (!bombAlreadyHere) {
                    bombs1.add(new Bomb(player.id(), player.position().containingCell(), Ticks.BOMB_FUSE_TICKS, player.bombRange()));
                }

            }
        }

        return bombs1;
    }

}
