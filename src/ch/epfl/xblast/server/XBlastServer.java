package ch.epfl.xblast.server;

import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;
import ch.epfl.xblast.server.GUI.ServerStatusComponent;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class XBlastServer {
    private static final int SERVER_STATUS_UI_WIDTH = 450;
    private static final int PORT = 2016;
    private static ServerStatusComponent XBC;
    private static GameState gameState;
    private static JFrame window;

    /**
     * Lancer un serveur XBlast, accompagné de son interface graphique
     * permettant de voir l'avancement des connections puis de la partie
     * 
     * @param level
     *            Le niveau le serveur doit être lancé
     * @param minPlayerToStart
     *            Le nombre minimum de joueurs connectés nécessaire pour le
     *            lancement du serveur. Utile pour le debug
     * @throws IOException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static void startServer(Level level, int minPlayerToStart) throws IOException, InterruptedException, InvocationTargetException {
        Objects.requireNonNull(level);
        
        //
        // Connections des joueurs
        //
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        InetAddress localIP = Inet4Address.getLocalHost();
        channel.bind(new InetSocketAddress(PORT));
        ByteBuffer buffer = ByteBuffer.allocate(1);
        Map<SocketAddress, PlayerID> players = new HashMap<>();

        PlayerID[] playerIDs = PlayerID.values();

        SocketAddress senderAddress;

        SwingUtilities.invokeAndWait(() -> createUI(localIP, PORT));

        XBC.setMaximumPlayers(minPlayerToStart);

        System.out.println("Waiting for connections... (" + minPlayerToStart + " player(s) needed)\n");

        while (players.size() < minPlayerToStart) {
            senderAddress = channel.receive(buffer);

            buffer.rewind();

            if (buffer.get() == PlayerAction.JOIN_GAME.ordinal()) {
                if (players.putIfAbsent(senderAddress, playerIDs[players.size()]) == null) {
                    System.out.println(playerIDs[players.size() - 1] + " connected from " + senderAddress);
                    SwingUtilities.invokeLater(() -> XBC.incrementPlayers());
                }
            }

            buffer.clear();
        }

        //
        // Itération de la partie
        //
        gameState = level.gameState();

        // Variables de temps
        long nextTickTime = System.nanoTime() + Ticks.TICK_NANOSECOND_DURATION;
        long waitingTime;

        channel.configureBlocking(false);

        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        byte bufferValue;
        PlayerID currentPlayer;

        while (!gameState.isGameOver()) {
            XBC.setGameState(gameState);
            window.pack();

            sendGameState(players, level.boardPainter(), gameState, channel);

            waitingTime = nextTickTime - System.nanoTime();

            if (waitingTime > 0)
                Thread.sleep(Math.floorDiv(waitingTime, Time.NS_PER_MS), (int) Math.floorMod(waitingTime, Time.NS_PER_MS));

            nextTickTime += Ticks.TICK_NANOSECOND_DURATION;

            //
            // Récupération des actions des joueurs
            //
            senderAddress = channel.receive(buffer);
            while (senderAddress != null) {
                currentPlayer = players.get(senderAddress);
                buffer.rewind();
                bufferValue = buffer.get();
                if (bufferValue > PlayerAction.JOIN_GAME.ordinal()) {
                    if (bufferValue == PlayerAction.DROP_BOMB.ordinal()) {
                        bombDropEvents.add(currentPlayer);
                    } else {
                        if (bufferValue == PlayerAction.STOP.ordinal()) {
                            speedChangeEvents.put(currentPlayer, Optional.empty());
                        } else {
                            speedChangeEvents.put(currentPlayer, Optional.of(Direction.values()[bufferValue - 1]));
                        }
                    }
                }
                buffer.clear();
                senderAddress = channel.receive(buffer);
            }

            //
            // Création du jeu suivant et envoi aux joueurs
            //

            gameState = gameState.next(speedChangeEvents, bombDropEvents);

            speedChangeEvents.clear();
            bombDropEvents.clear();

        }

        // Envoi du tout dernier état de jeu (pas besoin d'attendre puisque le
        // jeu ets deja fini, les actions des joueurs ne changent plus rien)
        sendGameState(players, Level.DEFAULT_LEVEL.boardPainter(), gameState, channel);
        System.out.println("\n" + (gameState.winner().isPresent() ? "The winner is " + gameState.winner().get() + " !" : "Time elapsed without any  winner !"));

        channel.close();

    }

    private static void sendGameState(Map<SocketAddress, PlayerID> players, BoardPainter boardPainter, GameState gameState, DatagramChannel channel) throws IOException {
        List<Byte> serializedGameState = GameStateSerializer.serialize(boardPainter, gameState);
        ByteBuffer buffer = ByteBuffer.allocate(serializedGameState.size() + 1);
        buffer.get(); // arbritraire, juste pour avancer d'une position

        for (Byte byte1 : serializedGameState) {
            buffer.put(byte1);
        }

        for (Map.Entry<SocketAddress, PlayerID> entry : players.entrySet()) {
            buffer.rewind();
            buffer.put((byte) entry.getValue().ordinal());
            buffer.rewind();
            channel.send(buffer, entry.getKey());
        }

    }

    private static void createUI(InetAddress inetAddress, int port) {
        window = new JFrame("XBlast Server");
        XBC = new ServerStatusComponent(inetAddress, port);

        window.setContentPane(XBC);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setMinimumSize(new Dimension(SERVER_STATUS_UI_WIDTH, 0));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
