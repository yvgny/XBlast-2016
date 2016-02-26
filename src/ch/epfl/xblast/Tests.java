package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    public static void main(String[] args) {
        ArrayList<String> test = new ArrayList<String>();
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
        }
        
        
    }
    
    

}
