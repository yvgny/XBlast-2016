package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

/**
 * Composant Swing affichant l'état du'une partie de XBlast
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
@SuppressWarnings("serial")
public final class XBlastComponent extends JComponent {
    private static final int DEFAULT_IMAGE_WIDTH = 64;
    private static final int DEFAULT_IMAGE_HEIGHT = 48;
    private static final int PLAYER_LIVE_VERTICAL_POSITION = 659;
    private static final int[] PLAYER_LIVE_HORIZONTAL_POSITIONS = { 96, 240, 768, 912 };
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOWS_HEIGHT = 688;
    private static final Font ARIAL_BOLD = new Font("Arial", Font.BOLD, 25);
    private static final Function<Integer, Integer> X_PLAYER_IMAGE_POSITION = x -> (4 * x) - 24;
    private static final Function<Integer, Integer> Y_PLAYER_IMAGE_POSITION = y -> (3 * y) - 52;
    private GameState gameState;
    private PlayerID playerID;

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g0) {
        if (gameState == null) {
            return;
        }

        Graphics2D g = (Graphics2D) g0;

        g.setColor(Color.WHITE);
        g.setFont(ARIAL_BOLD);

        List<Image> boardImages = gameState.board();
        List<Image> explosionImages = gameState.explosions();
        List<Image> scoreImages = gameState.scores();
        List<Image> timeImages = gameState.getTime();

        List<Player> players = gameState.players();
        sortPlayers(players);

        Image boardImage;
        Image explosionImage;

        int index;

        //
        // Dessin du board et des explosions
        //
        for (int y = 0; y < Cell.ROWS; y++) {
            for (int x = 0; x < Cell.COLUMNS; x++) {
                index = (y * Cell.COLUMNS) + x;

                boardImage = boardImages.get(index);
                explosionImage = explosionImages.get(index);

                g.drawImage(boardImage, x * DEFAULT_IMAGE_WIDTH, y * DEFAULT_IMAGE_HEIGHT, null);

                if (explosionImage != null)
                    g.drawImage(explosionImage, x * DEFAULT_IMAGE_WIDTH, y * DEFAULT_IMAGE_HEIGHT, null);
            }
        }

        //
        // Dessin de la ligne des scores
        //
        int xWidth = 0;
        for (Image image : scoreImages) {
            g.drawImage(image, xWidth, Cell.ROWS * DEFAULT_IMAGE_HEIGHT, null);
            xWidth += image.getWidth(null);
        }

        //
        // Dessin des joueurs et nombres de vies
        //
        for (Player player : players) {
            g.drawImage(player.image(), X_PLAYER_IMAGE_POSITION.apply(player.position().x()), Y_PLAYER_IMAGE_POSITION.apply(player.position().y()), null);

            // Dessin du nombre de vie
            g.drawString(Integer.toString(player.lives()), PLAYER_LIVE_HORIZONTAL_POSITIONS[player.playerID().ordinal()], PLAYER_LIVE_VERTICAL_POSITION);
        }

        //
        // Dessin de la ligne du temps
        //
        xWidth = 0;
        for (Image image : timeImages) {
            g.drawImage(image, xWidth, (Cell.ROWS + 1) * DEFAULT_IMAGE_HEIGHT, null);
            xWidth += image.getWidth(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOW_WIDTH, WINDOWS_HEIGHT);
    }

    /**
     * Permet de changer l'état de jeu affiché par le composant
     * 
     * @param gameState
     *            L'état de jeu à afficher
     * @param playerID
     *            L'identité du joueur pour lequel le jeu est affiché
     */
    public void setGameState(GameState gameState, PlayerID playerID) {
        this.gameState = Objects.requireNonNull(gameState);
        this.playerID = Objects.requireNonNull(playerID);
        repaint();
    }

    private void sortPlayers(List<Player> players) {
        List<PlayerID> playerIDs = Arrays.asList(PlayerID.values());
        Collections.rotate(playerIDs, playerIDs.size() - (playerID.ordinal() + 1));
        Collections.reverse(players);

        Comparator<Player> ySort = Comparator.comparingInt(player -> player.position().y());
        Comparator<Player> yEqualSort = Comparator.comparingInt(player -> playerIDs.indexOf(player.playerID()));

        Collections.sort(players, ySort.thenComparing(yEqualSort));

    }

}
