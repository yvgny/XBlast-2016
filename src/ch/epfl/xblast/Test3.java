package ch.epfl.xblast;

import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class Test3 {

    public static void main(String[] args) {
        ch.epfl.xblast.server.GameState defaultGS = Level.DEFAULT_LEVEL.gameState();

        List<Byte> serialized = GameStateSerializer.serialize(Level.DEFAULT_LEVEL.boardPainter(), defaultGS);
        GameState deserialized = GameStateDeserializer.deserializeGameState(serialized);
        
    }

    public static <E> void printWithReturn(List<E> list, int lineSize) {
        int i = 0;
        for (E e : list) {
            System.out.print(e + ", ");
            i++;
            if (i == lineSize) {
                i = 0;
                System.out.println();
            }
        }
    }
    
    private static List<Cell> spiralToRowMajor(List<Cell> list) {
        Cell[] rowMajorOrder = new Cell[list.size()];

        int i = 0;
        for (Cell cell : Cell.SPIRAL_ORDER) {
            rowMajorOrder[cell.rowMajorIndex()] = list.get(i++);
        }

        return Arrays.asList(rowMajorOrder);
    }
}
