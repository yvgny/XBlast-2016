package ch.epfl.xblast;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState;

public class Tests {
    public static void main(String[] args) {
        
        Sq<Integer> seq = Sq.constant(3);
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), seq, 3);
        Bomb a = new Bomb(PlayerID.PLAYER_1, new Cell(1, 1), 3, 5);
        
        Player.LifeState test = new Player.LifeState(0, Player.LifeState.State.DEAD);

        //
        //
        
        Player playerA = new Player(PlayerID.PLAYER_1, 0, new Cell(5,0), 4, 5);
        Player PlayerB = playerA.withBombRange(6);
        
        System.out.println(playerA.bombRange());
        System.out.println(PlayerB.bombRange());
        
        Sq<LifeState> ls = playerA.statesForNextLife();
        
        
        
        for (int i = 0; i < 100; i++) {
            System.out.print(i + ") ");
            System.out.println(ls.head().state() + ", " + ls.head().lives());
            ls = ls.tail();
        }
        
        
        
        
    }
}