package ch.epfl.xblast;

import java.util.Arrays;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class Tests {

    public static void main(String[] args) {
        Block __ = Block.FREE;
        Block II = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofInnerBlocksWalled(
          Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __, __, __, __, __, xx, __),
            Arrays.asList(__, II, xx, II, xx, II, xx, __, __, __, __, xx, __),
            Arrays.asList(__, xx, __, __, __, xx, __, __, __, __, __, __, __),
            Arrays.asList(xx, II, __, II, II, II, II, __, __, __, __, __, __),
            Arrays.asList(__, xx, __, xx, __, __, __, __, __, __, __, xx, __),
            Arrays.asList(__, __, __, __, __, xx, __, __, __, __, __, xx, II),
            Arrays.asList(__, II, xx, II, xx, II, xx, __, __, __, __, xx, __),
            Arrays.asList(__, xx, __, __, __, xx, __, __, __, __, __, __, __),
            Arrays.asList(xx, II, __, II, II, II, II, __, __, __, __, __, __),
            Arrays.asList(__, xx, __, xx, __, __, __, __, __, __, __, xx, __),
            Arrays.asList(xx, II, xx, II, xx, II, __, __, __, __, __, __, xx)));
        
        Board board2 = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                  Arrays.asList(__, __, __, __, __, xx, __),
                  Arrays.asList(__, II, xx, II, xx, II, xx),
                  Arrays.asList(__, xx, __, __, __, xx, __),
                  Arrays.asList(xx, II, __, II, II, II, II),
                  Arrays.asList(__, xx, __, xx, __, __, __),
                  Arrays.asList(xx, II, xx, II, xx, II, __)));
        
            
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                if (board.blockAt(new Cell(j, i)) == Block.FREE) {
                    System.out.print("__");
                } else if (board.blockAt(new Cell(j, i)) == Block.INDESTRUCTIBLE_WALL) {
                    System.out.print("II");
                } else if (board.blockAt(new Cell(j, i)) == Block.DESTRUCTIBLE_WALL) {
                    System.out.print("xx");
                } else {
                    System.out.print("??");
                }
                System.out.print(", ");
                
            }
            
            System.out.println();
        }
        
    }
    
    

}
