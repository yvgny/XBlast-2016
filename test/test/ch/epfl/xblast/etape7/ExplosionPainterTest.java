package test.ch.epfl.xblast.etape7;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.ExplosionPainter;

public class ExplosionPainterTest {

    
    @Test
    public void explosionPainterByte(){
        assertEquals(16, ExplosionPainter.BYTE_FOR_EMPTY);
    }
    
    @Test
    public void byteForBombsTest() {
       List<Integer> powList = Arrays.asList(1,2,4,8,16);
        for(int i=17; i>0;--i){
            Bomb bombTest = new Bomb(PlayerID.PLAYER_1, new Cell(0,0), i, 3);
            if(powList.contains(i))
                assertEquals(21, ExplosionPainter.byteForBomb(bombTest));
            else
                assertEquals(20, ExplosionPainter.byteForBomb(bombTest));
        }
       
        
    }
    
    @Test
    public void byteForBlastTest(){
       // N,E,S,W
        assertEquals(0,
                ExplosionPainter.byteForBlast(false, false, false, false));
        assertEquals(5,
                ExplosionPainter.byteForBlast(false, true, false, true));
        assertEquals(9,
                ExplosionPainter.byteForBlast(true, false, false, true));
        assertEquals(13,
                ExplosionPainter.byteForBlast(true, true, false, true));
        assertEquals(15,
                ExplosionPainter.byteForBlast(true, true, true, true));
    }
    

}
