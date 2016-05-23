package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Représente un codeur/décodeur par plage
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class RunLengthEncoder {

    private RunLengthEncoder() {
        // Non-instanciable
    }

    /**
     * Permet de coder par plage une séquence de bytes
     * 
     * @param byteList
     *            La séquence de byte à coder
     * @return La liste codée par plage
     * @throws IllegalArgumentException
     *             Si l'un des bytes de byteList est négatif
     */
    public static List<Byte> encode(List<Byte> byteList) throws IllegalArgumentException {
        List<Byte> byteListEncoded = new ArrayList<>();
        int numberOfRepetitions = 1;
        Byte currentByte;
        boolean hasNext;

        loop: for (int j = 0; j < byteList.size(); j++) {
            hasNext = j + 1 < byteList.size();
            currentByte = byteList.get(j);
            ArgumentChecker.requireNonNegative(currentByte);

            while (hasNext && currentByte.equals(byteList.get(j + 1))) {
                numberOfRepetitions++;
                continue loop;
            }

            int remainder = 0;

            while (numberOfRepetitions != 0) {
                if (numberOfRepetitions > 130) {
                    remainder = numberOfRepetitions - 130;
                    numberOfRepetitions = 130;
                } else {
                    remainder = 0;
                }
                if (numberOfRepetitions > 2) {

                    byteListEncoded.add((byte) (2 - numberOfRepetitions));
                    byteListEncoded.add(currentByte);

                    numberOfRepetitions = remainder;

                } else {
                    byteListEncoded.addAll(Collections.nCopies(numberOfRepetitions, currentByte));
                    numberOfRepetitions = remainder;
                }
            }

            numberOfRepetitions = 1;
        }

        return byteListEncoded;
    }

    /**
     * Décode une série de bytes codée par plage
     * 
     * @param byteList
     *            La liste de bytes codée
     * @return La liste de bytes décodée
     * @throws IllegalArgumentException
     *             Si le dernier élément de la liste est strictement inférieur à
     *             zéro
     */
    public static List<Byte> decode(List<Byte> byteList) throws IllegalArgumentException {
        List<Byte> byteListDecoded = new ArrayList<>();
        Iterator<Byte> i = byteList.iterator();
        Byte currentByte;
        int numberOfRepetitions;

        while (i.hasNext()) {
            currentByte = i.next();

            if (!i.hasNext()) {
                ArgumentChecker.requireNonNegative(currentByte);
            }

            if (currentByte < 0) {
                numberOfRepetitions = 2 - currentByte;
                byteListDecoded.addAll(Collections.nCopies(numberOfRepetitions, i.next()));
            } else {
                byteListDecoded.add(currentByte);
            }

        }

        return byteListDecoded;
    }
}
