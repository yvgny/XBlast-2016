package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Lists {

    private Lists() {
        // Non-instanciable
    }

    /**
     * Retourne une version symétrique de la liste donnée
     * 
     * @param l
     *            La liste à transformer
     * @param <T>
     *            Le type de la liste passée en paramètre
     * @return La version "mirroir" de la liste passée en paramètre
     * @throws IllegalArgumentException
     *             Si la liste est vide
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException {
        if (l == null || l.isEmpty()) {
            throw new IllegalArgumentException("List is empty or not inizialized !");
        }

        List<T> mirrored = new ArrayList<T>();
        List<T> reversed = new ArrayList<T>(l);
        reversed.remove(reversed.size() - 1);
        Collections.reverse(reversed);

        mirrored.addAll(l);
        mirrored.addAll(reversed);

        return mirrored;
    }

    /**
     * Copie la liste de liste passée en paramètre. Il faut toutefois faire
     * attention que seul la référence l'objet de type T est copié, il se s'agit
     * donc pas de copie profonde
     * 
     * @param l
     *            La liste de liste à copier
     * @param <T>
     *            Le type de la liste passée en paramètre
     * @return Une nouvelle liste de liste de même contenu que celle passée en
     *         paramètre
     * @throws IllegalArgumentException
     *             Si la liste est vide ou si l'objet est nul
     */
    public static <T> List<List<T>> copy(List<List<T>> l) throws IllegalArgumentException {
        if (l == null || l.isEmpty()) {
            throw new IllegalArgumentException("List is empty or not inizializated !");
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
