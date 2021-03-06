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
     * @return La version "miroir" de la liste passée en paramètre
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
     * Retourne les permutations de la liste donnée en argument, dans un ordre
     * quelconque.
     * 
     * @param l
     *            La liste à utiliser pour calculer les permutations de ses
     *            objets
     * @param <T>
     *            Le type de la liste passée en paramètre
     * @return Une liste des permutations des objets de la liste
     */
    public static <T> List<List<T>> permutations(List<T> l) {
        List<List<T>> permutations = new ArrayList<List<T>>();
        List<List<T>> subListPermutations = new ArrayList<List<T>>();
        ArrayList<T> copiedL = new ArrayList<T>(l);

        if (l.isEmpty() || l.size() == 1) {
            permutations.add(copiedL);
            
            return permutations;
        } else {
            
            T firstElement = l.get(0);
            subListPermutations = permutations(l.subList(1, l.size()));
            
            for (List<T> list : subListPermutations) {
                for (int i = 0; i <= list.size(); i++) {
                    ArrayList<T> tempList = new ArrayList<T>(list);
                    tempList.add(i, firstElement);
                    permutations.add(tempList);
                }
            }
            
             return permutations;
        }
    }

}
