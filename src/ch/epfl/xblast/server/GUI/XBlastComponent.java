package ch.epfl.xblast.server.GUI;

import java.awt.Color;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Ticks;

import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

public final class XBlastComponent extends JPanel {
    private int connectedPlayers;
    private int maxPlayers;
    
    private JProgressBar gameProgressBar;
    private JLabel connectedPlayersLabel;
    private JLabel serverInfo;
    private GameState gameState;
    private JPanel gameProgess;
    private JLabel lblPlayersAlive;

    public XBlastComponent(InetAddress inetAddress, int port) {
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Create server infos
        serverInfo = new JLabel("Server address : " + inetAddress.getHostAddress() + ":" + port);
        serverInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        serverInfo.setFont(getFont().deriveFont(getFont().BOLD));
        
        // Create connectes players info
        connectedPlayers = 0;
        connectedPlayersLabel = new JLabel();
        connectedPlayersLabel.setHorizontalAlignment(SwingConstants.LEFT);
        connectedPlayersLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        updateConnectedPlayers();
        
        
        add(serverInfo);
        add(connectedPlayersLabel);
        
        gameProgess = new JPanel();
        gameProgess.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Game progress", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), new EmptyBorder(5, 5, 5, 5)));
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
        lblPlayersAlive.setBorder(new EmptyBorder(5, 0, 0, 0));
        gameProgess.add(lblPlayersAlive);
        
    }
    
    public void incrementPlayers() {
        connectedPlayers++;
        updateConnectedPlayers();
    }

    public void decrementPlayers() {
        connectedPlayers--;
        updateConnectedPlayers();
    }

    private void updateConnectedPlayers() {
        connectedPlayersLabel.setText("Connected players : " + connectedPlayers + "/" + maxPlayers);
        repaint();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        gameProgess.setVisible(true);
        gameProgressBar.setValue(gameState.ticks());
        lblPlayersAlive.setText("Players alive : " + gameState.alivePlayers().size());
        repaint();
    }
    
    public void setMaximumPlayers (int maximumPlayers){
        this.maxPlayers = maximumPlayers;
        updateConnectedPlayers();
    }

}
