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
import java.util.Optional;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

public final class Main {
    private static final int PORT = 2016;
    private static XBlastComponent XBC;
    private static GameState gameState;
    private static JFrame window;

    public static void startServer(Level level, int minPlayerToStart) throws IOException, InterruptedException, InvocationTargetException {

        //
        // Connections des joueurs
        //
        // int minPlayerToStart = args.length == 0 ? 4 : Integer.parseInt(args[0]); //FIXME si retour : changer signature méthode et uncomment ca

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

        System.out.println(gameState.winner().isPresent() ? "\nThe winner is " + gameState.winner().get() + " !" : "\nTime elapsed without any  winner !");

    }

    private static void sendGameState(Map<SocketAddress, PlayerID> players, BoardPainter boardPainter, GameState gameState, DatagramChannel channel) throws IOException {
        List<Byte> serializedGameState = GameStateSerializer.serialize(boardPainter, gameState);
        ByteBuffer buffer = ByteBuffer.allocate(serializedGameState.size() + 1);
        buffer.put((byte) 0); // arbritraire, jutse pour avancer d'une case

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
        XBC = new XBlastComponent(inetAddress, port);

        window.setContentPane(XBC);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setMinimumSize(new Dimension(450, 0));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
