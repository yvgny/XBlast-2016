package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Lists {

    private Lists() {
        // Non-instanciable
    }

    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException {
        if (l.isEmpty()) {
            throw new IllegalArgumentException("List is empty !");
        }

        List<T> mirrored = new ArrayList<T>();
        List<T> reversed = new ArrayList<T>(l);
        reversed.remove(reversed.size() - 1);
        Collections.reverse(reversed);

        mirrored.addAll(l);
        mirrored.addAll(reversed);

        return mirrored;
    }

    public static <T> List<List<T>> copy(List<List<T>> l) throws IllegalArgumentException {
        if (l.isEmpty()) {
            throw new IllegalArgumentException("List is empty !");
        }

        List<List<T>> copied = new ArrayList<List<T>>();

        for (List<T> list : l) {
            List<T> temporary = new ArrayList<T>();

            for (T t : list) {
                temporary.add(t);

            }

            copied.add(temporary);
        }

        return copied;
    }
}
