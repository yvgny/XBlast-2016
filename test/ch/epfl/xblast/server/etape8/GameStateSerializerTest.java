package ch.epfl.xblast.server.etape8;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class GameStateSerializerTest {

    @Test
    public void SerialOfDefaultLevelIsCorrect() {
        Level defaultLevel = Level.DEFAULT_LEVEL;

        List<Byte> gameStateSerialized = GameStateSerializer.serialize(defaultLevel.getBoardPainter(), defaultLevel.getGameState());

        Byte[] gameStateStatic = gameStateSerialized.toArray(new Byte[gameStateSerialized.size()]);
        System.out.println("Size = " + gameStateSerialized.size());
        
        Byte[] verifiedList = {121, -50, 2, 1, -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3,
                                      1, 3, 1, 1, -2, 0, 1, 3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1,
                                      3, 1, 1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                                      2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2,
                                      3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1, 1, 0, 0, 1, 3,
                                      1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5, 2, 3, 2,
                                      3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2,4, -128, 16, -63, 16,
                                      3, 24, 24, 6,
                                      3, -40, 24, 26,
                                      3, -40, -72, 46,
                                      3, 24, -72, 66,
                                      60};
        
         assertArrayEquals(verifiedList, gameStateStatic);
    }

}
