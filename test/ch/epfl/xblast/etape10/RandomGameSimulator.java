package ch.epfl.xblast.etape10;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

public class RandomGameSimulator {
    private static ch.epfl.xblast.server.GameState defaultGS = Level.DEFAULT_LEVEL.gameState();
    private static RandomEventGenerator random = new RandomEventGenerator(2016, 30, 100);

    public static void main(String[] args) {


        Map<PlayerID, Optional<Direction>> speedChange = new HashMap<>();
        Set<PlayerID> bombs = new HashSet<>();

        List<Byte> serialized = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.boardPainter(), defaultGS);
        GameState deserialized = GameStateDeserializer.deserializeGameState(serialized);

        XBlastComponent xblastComponement = new XBlastComponent();

        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        kb.put(KeyEvent.VK_R, PlayerAction.JOIN_GAME);

        Consumer<PlayerAction> c = action -> {
            switch (action) {
            case MOVE_N:
                speedChange.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
                break;
            case MOVE_S:
                speedChange.put(PlayerID.PLAYER_1, Optional.of(Direction.S));
                break;
            case MOVE_E:
                speedChange.put(PlayerID.PLAYER_1, Optional.of(Direction.E));
                break;
            case MOVE_W:
                speedChange.put(PlayerID.PLAYER_1, Optional.of(Direction.W));
                break;
            case STOP:
                speedChange.put(PlayerID.PLAYER_1, Optional.empty());
                break;
            case DROP_BOMB:
                bombs.add(PlayerID.PLAYER_1);
                break;
            case JOIN_GAME:
                defaultGS = Level.DEFAULT_LEVEL.gameState();
                random = new RandomEventGenerator(2016, 30, 100);
                break;
            default:
                break;
            }
        };

        serialized = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.boardPainter(), defaultGS);
        deserialized = GameStateDeserializer.deserializeGameState(serialized);

        xblastComponement.setGameState(deserialized, PlayerID.PLAYER_1);

        JFrame window = new JFrame("XBlast");
        window.addKeyListener(new KeyboardEventHandler(kb, c));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(xblastComponement);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        long time = System.currentTimeMillis();

        while (true) {
            if (System.currentTimeMillis() - time > 50) {


                serialized = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.boardPainter(), defaultGS);
                deserialized = GameStateDeserializer.deserializeGameState(serialized);

                xblastComponement.setGameState(deserialized, PlayerID.PLAYER_1);

                speedChange.clear();
                bombs.clear();
                speedChange.putAll(random.randomSpeedChangeEvents());
                bombs.addAll(random.randomBombDropEvents());
                if (!defaultGS.isGameOver())
                    defaultGS = defaultGS.next(speedChange, bombs);

                time = System.currentTimeMillis();

            }

        }

    }

}
