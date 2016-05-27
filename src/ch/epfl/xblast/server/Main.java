package ch.epfl.xblast.server;

import java.io.IOException;
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

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Main {
    private static final int DEFAULT_MIN_REQUIRED_CONNECTIONS = 4;
    private static final int PORT = 2016;

    /**
     * Lance un serveur serveur XBLast avec le niveau par défaut. Si le nombre
     * de client minimum nécessaire au démarrage de la partie n'est pas précisé,
     * une valeur de quatre est utilisée
     * 
     * @param args
     *            Prend un seul argument, le nombre de client minimum qui
     *            doivent se connecter avant de commencer la partie
     * @throws IOException
     *             Si une erreur apparait lors de l'utilistaion d'un flot
     * @throws InterruptedException
     *             Si un fil d'éxectuion arrête ce fil d'exécution
     * 
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        //
        // Connections des joueurs
        //
        int minPlayerToStart = args.length == 0 ? DEFAULT_MIN_REQUIRED_CONNECTIONS : Integer.parseInt(args[0]);

        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(PORT));
        ByteBuffer buffer = ByteBuffer.allocate(1);

        PlayerID[] playerIDs = PlayerID.values();

        Map<SocketAddress, PlayerID> players = new HashMap<>();

        SocketAddress senderAddress;

        System.out.println("Waiting for connections... (" + minPlayerToStart + " player(s) needed)\n");

        while (players.size() < minPlayerToStart) {
            senderAddress = channel.receive(buffer);

            buffer.rewind();

            if (buffer.get() == PlayerAction.JOIN_GAME.ordinal()) {
                if (players.putIfAbsent(senderAddress, playerIDs[players.size()]) == null)
                    System.out.println(playerIDs[players.size() - 1] + " connected from " + senderAddress);
            }

            buffer.clear();
        }

        //
        // Itération de la partie
        //
        GameState gameState = Level.DEFAULT_LEVEL.gameState();

        // Variables de temps
        long nextTickTime = System.nanoTime() + Ticks.TICK_NANOSECOND_DURATION;
        long waitingTime;

        channel.configureBlocking(false);

        Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
        Set<PlayerID> bombDropEvents = new HashSet<>();
        byte bufferValue;
        PlayerID currentPlayer;

        while (!gameState.isGameOver()) {
            sendGameState(players, Level.DEFAULT_LEVEL.boardPainter(), gameState, channel);

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

}
