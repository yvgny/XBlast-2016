package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Représente l'état du jeu, du point du vue du client
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class GameState {

    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> scores;
    private final List<Image> time;

    /**
     * Créer un état de jeu du point de vue du client
     * 
     * @param players
     *            La liste des joueurs
     * @param board
     *            Les images représentants le plateau de jeu, dans l'ordre en
     *            spirale
     * @param bombsAndExplosions
     *            Les images représentants les bombes et les explosions
     * @param scores
     *            Les images représentants la ligne des scores
     * @param time
     *            Les images représentants la ligne du temps
     */
    public GameState(List<Player> players, List<Image> board,
            List<Image> bombsAndExplosions, List<Image> scores,
            List<Image> time) {
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = Collections.unmodifiableList(spiralToRowMajor(new ArrayList<>(board)));
        this.explosions = Collections.unmodifiableList(new ArrayList<>(bombsAndExplosions));
        this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
        this.time = Collections.unmodifiableList(new ArrayList<>(time));
    }

    /**
     * Change l'ordre d'une liste d'image pour la tansformer d'un ordre en
     * spirale à un ordre de lecture
     * 
     * @param list
     *            La liste à transformer
     * @return La liste transformée, de taille fixe
     */
    private List<Image> spiralToRowMajor(List<Image> list) {
        Image[] rowMajorOrder = new Image[list.size()];

        int i = 0;
        for (Cell cell : Cell.SPIRAL_ORDER) {
            rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
        }

        return Arrays.asList(rowMajorOrder);
    }

    /**
     * @return the players
     */
    public List<Player> players() {
        return new ArrayList<>(players);
    }

    /**
     * @return the board
     */
    public List<Image> board() {
        return new ArrayList<>(board);
    }

    /**
     * @return the bombsAndExplosions
     */
    public List<Image> explosions() {
        return new ArrayList<>(explosions);
    }

    /**
     * @return the scores
     */
    public List<Image> scores() {
        return new ArrayList<>(scores);
    }

    /**
     * @return the time
     */
    public List<Image> getTime() {
        return new ArrayList<>(time);
    }

    /**
     * Représente un joueur, du point de vue du client
     * 
     * @author Sacha Kozma, 260391
     * @author Alexia Bogaert, 258330
     *
     */
    public static class Player {
        private final PlayerID playerID;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Construit un joueur, du point de vue du client
         * 
         * @param playerID
         *            L'identité du joueur
         * @param lives
         *            Son nombre de vie(s)
         * @param position
         *            Sa position
         * @param image
         *            L'image le représentant
         */
        public Player(PlayerID playerID, int lives, SubCell position,
                Image image) {
            this.playerID = playerID;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }

        /**
         * @return L'identité du joueur
         */
        public PlayerID playerID() {
            return playerID;
        }

        /**
         * @return Le nombre de vie(s) du joueur
         */
        public int lives() {
            return lives;
        }

        /**
         * @return L'image du joueur
         */
        public Image image() {
            return image;
        }

        /**
         * @return La position du joueur
         */
        public SubCell position() {
            return position;
        }

    }
}
