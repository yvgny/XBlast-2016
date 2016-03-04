package ch.epfl.xblast;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.Ticks;

public class Tests {

    public static void main(String[] args) {
        Cell position = new Cell(0, 0);

        //Sq<Sq<Cell>> arm = Bomb.explosionArmTowards(position, Direction.E);

        /*
        for (int i = 0; i < 4; i++) {
            System.out.println(arm.head().head());
            arm = arm.tail();
        }*/

    }

}
