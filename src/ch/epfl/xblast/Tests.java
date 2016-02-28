package ch.epfl.xblast;

import java.util.ArrayList;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class Tests {

    public static void main(String[] args) {
        /*ArrayList<String> test = new ArrayList<String>();
        test.add("k");
        //test.add("a");
        //test.add("y");
        
        List<String> testReversed = Lists.mirrored(test);
        
        for (String string : testReversed) {
            System.out.println(string);
        }
        
        System.out.println("Ne devrait pas changer :");
        
        for (String string : test) {
            System.out.println(string);
        } */
        
        ArrayList<Sq<String>> BoardList = new ArrayList<Sq<String>>();
        
        Sq<String> s = Sq.constant("bla");
        
        BoardList.add(s);
        
        ArrayList<Sq<String>> BoardListCopy = new ArrayList<Sq<String>>(BoardList);
        
        BoardListCopy.get(0);

        
        // Board test = new Board(BoardList);
        
        
        
    }
    
    

}
