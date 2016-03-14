package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
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
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
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
        double remainingTime = (Ticks.TOTAL_TICKS - ticks) / (double)Ticks.TICKS_PER_SECOND;
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
    public List<Player> players(){
        return new ArrayList<Player>(players);
    }
    
    /**
     * @return Les joueurs vivants, c-à-d ceux ayant au moins une vie
     */
    public List<Player> alivePlayers(){
        ArrayList<Player> alivePlayers = new ArrayList<Player>();
        for (Player player : players) {
            if (player.isAlive()) {
                alivePlayers.add(player);
            }
        }
        
        return alivePlayers;
    }
    
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0, Board board0, List<Sq<Sq<Cell>>> explosions0){
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();
        
        for (Sq<Cell> blastSq : blasts0) {
            if (board0.blockAt(blastSq.head()).isFree()  && !blastSq.tail().isEmpty()) {
                blasts1.add(blastSq.tail());
            }
        }
        
        for (Sq<Sq<Cell>> explosionSq : explosions0) {
            blasts1.add(explosionSq.head());
        }
        
        
        return blasts1;
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
