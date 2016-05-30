package ch.epfl.xblast.server.GUI;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Ticks;

/**
 * Fenêtre de status du serveur
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
@SuppressWarnings("serial")
public final class ServerStatusComponent extends JPanel {
    private static final int DEFAULT_BORDER_SIZE = 10;
    private int connectedPlayers;
    private int maxPlayers;

    private JProgressBar gameProgressBar;
    private JLabel connectedPlayersLabel;
    private JLabel serverInfo;
    private GameState gameState;
    private JPanel gameProgess;
    private JLabel lblPlayersAlive;

    /**
     * Construit la fenêtre de status avec les éléments passés en arguments
     * 
     * @param inetAddress
     *            L'adresse du serveur
     * @param port
     *            Le port du serveur
     */
    public ServerStatusComponent(InetAddress inetAddress, int port) {
        Objects.requireNonNull(inetAddress);
        
        setBorder(BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create server infos
        serverInfo = new JLabel("Server address : " + inetAddress.getHostAddress() + ":" + port);
        serverInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, DEFAULT_BORDER_SIZE, 0));
        getFont();
        serverInfo.setFont(getFont().deriveFont(Font.BOLD));

        // Create connectes players info
        connectedPlayers = 0;
        connectedPlayersLabel = new JLabel();
        connectedPlayersLabel.setHorizontalAlignment(SwingConstants.LEFT);
        connectedPlayersLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, DEFAULT_BORDER_SIZE, 0));
        updateConnectedPlayers();

        add(serverInfo);
        add(connectedPlayersLabel);

        gameProgess = new JPanel();
        gameProgess.setBorder(
                new CompoundBorder(
                new TitledBorder(
                new LineBorder(
                        new Color(0, 0, 0)), "Game progress", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE, DEFAULT_BORDER_SIZE)
                ));
        add(gameProgess);
        gameProgess.setLayout(new BoxLayout(gameProgess, BoxLayout.Y_AXIS));
        gameProgess.setVisible(false);

        // Create game progress bar
        gameProgressBar = new JProgressBar();
        gameProgess.add(gameProgressBar);
        gameProgressBar.setMaximum(Ticks.TOTAL_TICKS);
        gameProgressBar.setStringPainted(true);
        gameProgressBar.setBorder(null);

        lblPlayersAlive = new JLabel();
        lblPlayersAlive.setBorder(new EmptyBorder(DEFAULT_BORDER_SIZE / 2, 0, 0, 0));
        gameProgess.add(lblPlayersAlive);

    }

    /**
     * Increment le nombre de joueur connectés
     */
    public void incrementPlayers() {
        connectedPlayers++;
        updateConnectedPlayers();
    }


    /**
     * Permet de changer l'état du jeu sur lequel se base la status du serveur
     * 
     * @param gameState
     */
    public void setGameState(GameState gameState) {
        this.gameState = Objects.requireNonNull(gameState);
        gameProgess.setVisible(true);
        updateGameStateStatus();
        repaint();
    }

    /**
     * Permet de choisir
     * 
     * @param maximumPlayers
     */
    public void setMaximumPlayers(int maximumPlayers) {
        this.maxPlayers = maximumPlayers;
        updateConnectedPlayers();
    }
    
    private void updateGameStateStatus(){
        gameProgressBar.setValue(gameState.ticks());
        lblPlayersAlive.setText("Players alive : " + gameState.alivePlayers().size());
    }

    private void updateConnectedPlayers() {
        connectedPlayersLabel.setText("Connected players : " + connectedPlayers + "/" + maxPlayers);
        repaint();
    }
}
