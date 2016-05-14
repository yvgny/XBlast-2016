package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

public final class Main {
    private static final int MAX_BUFFER_ALLOCATION = 409;
    private static final int PORT = 2016;
    private static final int TIME_BETWEEN_CONNECTION_TRY = 1000;
    private static XBlastComponent XBC;

    public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException {
        DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
        SocketAddress serverAddress = new InetSocketAddress(args.length == 0 ? "localhost" : args[0], PORT);
        List<Byte> serializedGameState;
        GameState gameState;
        
        ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_ALLOCATION);
        ByteBuffer connectionBuffer = ByteBuffer.allocate(1);

        connectionBuffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
        connectionBuffer.flip();
        channel.configureBlocking(false);

        do {
            Thread.sleep(TIME_BETWEEN_CONNECTION_TRY);
            channel.send(connectionBuffer, serverAddress);
            System.out.println("Connection attempt on " + serverAddress);
        } while (channel.receive(buffer) == null);
        
        System.out.println("Connected to " + serverAddress + " !");

        channel.configureBlocking(true);
        SwingUtilities.invokeAndWait(() -> createUI(channel, serverAddress));

        do {
            buffer.flip();
            serializedGameState = new ArrayList<>();

            while (buffer.hasRemaining()) {
                serializedGameState.add(buffer.get());
            }

            gameState = GameStateDeserializer.deserializeGameState(serializedGameState.subList(1, serializedGameState.size()));

            XBC.setGameState(gameState, PlayerID.values()[serializedGameState.get(0)]);

            buffer.clear();

        } while (channel.receive(buffer) != null);
        
        System.out.println("\nGame finished !");

    }

    public static void createUI(DatagramChannel channel, SocketAddress adress) {
        Map<Integer, PlayerAction> keyBindings = new HashMap<>();
        ByteBuffer actionBuffer = ByteBuffer.allocate(1);

        keyBindings.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        keyBindings.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        keyBindings.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        keyBindings.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        keyBindings.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        keyBindings.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);

        Consumer<PlayerAction> sendAction = action -> {
            actionBuffer.clear();
            actionBuffer.put((byte) action.ordinal());
            actionBuffer.flip();

            try {
                channel.send(actionBuffer, adress);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        XBC = new XBlastComponent();

        JFrame window = new JFrame("XBlast");
        window.addKeyListener(new KeyboardEventHandler(keyBindings, sendAction));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(XBC);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
