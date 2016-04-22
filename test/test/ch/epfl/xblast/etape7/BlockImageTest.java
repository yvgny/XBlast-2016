package test.ch.epfl.xblast.etape7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.epfl.xblast.server.BlockImage;

public class BlockImageTest {

    @Test
    public void enumtest() {
        BlockImage[] test = BlockImage.values();
        BlockImage[] compare = { BlockImage.IRON_FLOOR, BlockImage.IRON_FLOOR_S,
                BlockImage.DARK_BLOCK, BlockImage.EXTRA, BlockImage.EXTRA_O,
                BlockImage.BONUS_BOMB, BlockImage.BONUS_RANGE };
        assertTrue(test.length == compare.length);
        for (int i = 0; i < test.length; ++i) {
            assertEquals(test[i], compare[i]);
        }
    }

}
