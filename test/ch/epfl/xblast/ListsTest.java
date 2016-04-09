package ch.epfl.xblast;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListsTest {

    @Test
    public void mirroredTest() {
        // one element
        ArrayList<Character> myList = new ArrayList<>();
        myList.add('r');
        List<Character> testList = Lists.mirrored(myList);
        List<Character> result = Arrays.asList('r');
        assertEquals(result, testList);

        // two elements
        myList.add('a');
        testList = Lists.mirrored(myList);
        result = Arrays.asList('r', 'a', 'r');
        assertEquals(result, testList);

        // more elements
        myList.add('c');
        myList.add('e');
        testList = Lists.mirrored(myList);
        result = Arrays.asList('r', 'a', 'c', 'e', 'c', 'a', 'r');
        assertEquals(result, testList);
    }

}
