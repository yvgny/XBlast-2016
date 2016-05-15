package ch.epfl.xblast.server;

import java.awt.Color;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public final class XBlastComponent extends JPanel {
    private int connectedPlayers;
    private int maxPlayers;
    
    private JProgressBar gameProgress;
    private JLabel connectedPlayersLabel;
    private JLabel serverInfo;

    public XBlastComponent(InetAddress inetAddress, int port) {
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Create server infos
        serverInfo = new JLabel("Server address : " + inetAddress.getHostAddress() + ":" + port);
        serverInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        serverInfo.setFont(getFont().deriveFont(getFont().BOLD));
        
        // Create connectes players info
        connectedPlayers = 0;
        connectedPlayersLabel = new JLabel();
        connectedPlayersLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        updateConnectedPlayers();
        
        // Create game progress bar
        gameProgress = new JProgressBar();
        gameProgress.setMaximum(Ticks.TOTAL_TICKS);
        gameProgress.setStringPainted(true);
        gameProgress.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Game progress"));
        
        
        this.add(serverInfo);
        this.add(connectedPlayersLabel);
        this.add(gameProgress);
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

    public void setTicks(int ticks) {
        gameProgress.setValue(ticks);;
        repaint();
    }
    
    public void setMaximumPlayers (int maximumPlayers){
        this.maxPlayers = maximumPlayers;
        updateConnectedPlayers();
    }

}
