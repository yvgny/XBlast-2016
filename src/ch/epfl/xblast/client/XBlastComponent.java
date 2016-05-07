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
    private GameState gameState;
    private PlayerID playerID;
    private final int IMAGE_WIDTH = 64;
    private final int IMAGE_HEIGHT = 48;

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;

        Font arialBold = new Font("Arial", Font.BOLD, 25);
        g.setColor(Color.WHITE);
        g.setFont(arialBold);

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

                g.drawImage(boardImage, x * IMAGE_WIDTH, y * IMAGE_HEIGHT, null);

                if (explosionImage != null)
                    g.drawImage(explosionImage, x * IMAGE_WIDTH, y * IMAGE_HEIGHT, null);
            }
        }
        
        //
        // Dessin de la ligne des scores
        //
        int xWidth = 0;
        for (Image image : scoreImages) {
            g.drawImage(image, xWidth, 13 * IMAGE_HEIGHT, null);
            xWidth += image.getWidth(null);
        }

        //
        // Dessin des joueurs et nombres de vies
        //
        for (Player player : players) {
            g.drawImage(player.image(), 4 * player.position().x() - 24, 3 * player.position().y() - 52, null);

            // Dessin nombre de vie
            int[] positions = { 96, 240, 768, 912 };
            g.drawString(Integer.toString(player.lives()), positions[player.playerID().ordinal()], 659);
        }

        //
        // Dessin de la ligne du temps
        //
        xWidth = 0;
        for (Image image : timeImages) {
            g.drawImage(image, xWidth, 14 * IMAGE_HEIGHT, null);
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
        return new Dimension(960, 688);
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
        Collections.rotate(playerIDs, PlayerID.values().length - (playerID.ordinal() + 1));
        Collections.reverse(players);

        Comparator<Player> ySort = Comparator.comparingInt(player -> player.position().y());
        Comparator<Player> yEqualSort = Comparator.comparingInt(player -> playerIDs.indexOf(player.playerID()));

        Collections.sort(players, ySort.thenComparing(yEqualSort));

    }

}
