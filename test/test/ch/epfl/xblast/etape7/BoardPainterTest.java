package test.ch.epfl.xblast.etape7;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.BlockImage;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.BoardPainter;

public class BoardPainterTest {

    @Test
    public void byteForCellTest() {
        Map<Block, BlockImage> paletteTest = new HashMap<>();
        paletteTest.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        paletteTest.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        BoardPainter bpTest = new BoardPainter(paletteTest,
                BlockImage.IRON_FLOOR_S);
        
        List<Sq<Block>> listTest = new ArrayList<>();
        listTest.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        listTest.add(Sq.constant(Block.CRUMBLING_WALL));
        for (int k = 2; k < Cell.COUNT; k++) {
            listTest.add(Sq.constant(Block.FREE));
        }
        Board boardTest = new Board(listTest);
        Cell cell1 = new Cell(0,0);
        Cell cell2 = new Cell(1,0);
        assertEquals(2,bpTest.byteForCell(boardTest, cell1));
        assertEquals(4,bpTest.byteForCell(boardTest, cell2));
        
    }

}
