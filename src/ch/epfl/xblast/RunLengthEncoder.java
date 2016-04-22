package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Représente un codeur/décodeur par plages
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
     * Permet de coder par plages une séquence de bytes
     * 
     * @param byteList
     *            La séquence de byte à coder
     * @return La liste codée par plages
     * @throws IllegalArgumentException
     *             Si l'un des byte de byteList est négatif
     */
    public static List<Byte> encode(List<Byte> byteList) throws IllegalArgumentException {
        List<Byte> byteListEncoded = new ArrayList<>();
        int numberOfRepetitions = 0;
        Byte repeatedByte = byteList.isEmpty() ? null : byteList.get(0);
        Byte currentByte;
        Iterator<Byte> i = byteList.iterator();

        while (i.hasNext()) {
            
            currentByte = i.next();
            ArgumentChecker.requireNonNegative(repeatedByte);

            if (currentByte == repeatedByte && i.hasNext()) {
                numberOfRepetitions++;
            } else {
                if (currentByte == repeatedByte) 
                    numberOfRepetitions++;
                
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
                        byteListEncoded.add(repeatedByte);

                        numberOfRepetitions = remainder;

                    } else {
                        byteListEncoded.addAll(Collections.nCopies(numberOfRepetitions, repeatedByte));
                        numberOfRepetitions = remainder;
                    }
                }
                repeatedByte = currentByte;
                numberOfRepetitions = 1;
                
            }
        }

        return byteListEncoded;
    }

    /**
     * Décode une série de byte codée par plages
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
