package ch.epfl.xblast;

public class Tests {

    public static void main(String[] args) {
        System.out.println(Direction.N.ordinal());
        System.out.println(Direction.S.ordinal());
        for (Direction d: Direction.values())
            System.out.println((Math.abs(d.ordinal() - d.opposite().ordinal())));
        
        int i = 0;
        
        for (Cell c : Cell.SPIRAL_ORDER){
            System.out.println(i + " = " + c);
            i++;
        }
    }
    
    

}
