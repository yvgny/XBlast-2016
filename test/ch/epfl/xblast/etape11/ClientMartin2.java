package ch.epfl.xblast.etape11;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.GUI.XBlastComponent;

/**
 * 
 * @author Martin
 *
 */
public class ClientMartin2 {

    private final static Map<Integer, PlayerAction> ACTIONS_MAP = defaultActionMap();

    private static XBlastComponent XBC;
    private static DatagramChannel channel;
    private static SocketAddress serverAddress;
    private static Consumer<PlayerAction> actionConsumer;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, IOException {

        System.out.println("CLIENT\n");

        // args
        String hostName = args.length == 0 ? "localhost" : args[0];

        // should send on port UDP 2016
        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        serverAddress = new InetSocketAddress(hostName, 2016);
        ByteBuffer startingBuffer = ByteBuffer.allocate(1);
        startingBuffer.put((byte) 0);
        startingBuffer.flip();
        ByteBuffer maskBuffer = ByteBuffer.allocate(409);
        ByteBuffer buffer = ByteBuffer.allocate(409);

        // send connection info
        while (buffer == maskBuffer || buffer.equals(maskBuffer)) {
            System.out.println("Connecting to " + hostName);
            channel.send(startingBuffer, serverAddress);
            Thread.sleep(1000);
            channel.receive(buffer);
        }
        System.out.println("Connected");

        actionConsumer = (action) -> {
            ByteBuffer actionBuffer = ByteBuffer.allocate(1);
            actionBuffer.put((byte) action.ordinal());
            actionBuffer.flip();
            try {
                channel.send(actionBuffer, serverAddress);
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            actionBuffer.clear();
        };

        // TODO : N'A PAS L'AIR D'ENVOYER QUOI QUE CE SOIT NI DE S'ATTACHER AU
        // KEYBOARDHANDLER ??
        /*
         * Consumer<PlayerAction> actionConsumer = (action) -> { ByteBuffer
         * actionBuffer = ByteBuffer.allocate(1); actionBuffer.put((byte)
         * action.ordinal()); actionBuffer.flip(); try {
         * channel.send(actionBuffer, serverAddress); } catch (Exception e) {
         * throw new IllegalArgumentException(); } actionBuffer.clear(); };
         */
        // Consumer<PlayerAction> actionConsumer = System.out::println;

        // receive gameState and refresh UI

        {
            List<Byte> bytes = new ArrayList<>();
            if (!buffer.hasArray()) {
                throw new IllegalArgumentException();
            }
            byte[] bytesArray = buffer.array();
            for (byte b : bytesArray) {
                bytes.add(b);
            }

            PlayerID id = PlayerID.values()[bytes.get(0)];
            bytes.remove(0);

            try {
                createUI(bytes, id);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        while (true) {
            channel.receive(buffer);
            List<Byte> bytes = new ArrayList<>();
            if (!buffer.hasArray()) {
                throw new IllegalArgumentException();
            }
            byte[] bytesArray = buffer.array();
            for (byte b : bytesArray) {
                bytes.add(b);
            }
            PlayerID id = PlayerID.values()[bytes.get(0)];
            bytes.remove(0);

            GameState gameState = GameStateDeserializer.deserializeGameState(bytes);

            SwingUtilities.invokeAndWait(() -> {
                XBC.setGameState(gameState, id);
            });
            buffer.clear();
        }
    }

    /**
     * 
     * @param bytes
     * @throws IOException
     */
    private static void createUI(List<Byte> bytes, PlayerID id) throws IOException {

        XBC = new XBlastComponent();
        XBC.addKeyListener(new KeyboardEventHandler(ACTIONS_MAP, actionConsumer));
        XBC.setFocusable(true);

        XBC.setGameState(GameStateDeserializer.deserializeGameState(bytes), id);
        JFrame WINDOW = new JFrame();
        WINDOW.setTitle("Xblast");
        WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WINDOW.setVisible(true);
        WINDOW.getContentPane().add(XBC);
        WINDOW.pack();
        XBC.requestFocusInWindow();
    }

    /**
     * 
     * @return
     */
    private static Map<Integer, PlayerAction> defaultActionMap() {
        Map<Integer, PlayerAction> actionMap = new HashMap<>();
        actionMap.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        actionMap.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        actionMap.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        actionMap.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        actionMap.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        actionMap.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        return Collections.unmodifiableMap(actionMap);
    }
}
