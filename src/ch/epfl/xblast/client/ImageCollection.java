package ch.epfl.xblast.client;

import java.awt.Image;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * Représente une collection d'images provenant d'un répertoire, la collection est indexée par
 * un entier
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class ImageCollection {

    private static final int IMAGE_NUMBER_INDEX = 0;
    private static final int IMAGE_NUMBER_PREFIX_SIZE = 3;
    private final Map<Integer, Image> imageCollection;

    /**
     * Construit une collection d'image
     * 
     * @param dirName
     *            Le nom du dossier à partir duquel la collection doit être
     *            créée
     */
    public ImageCollection(String dirName) {
        File imageDirectory;
        String fileName;
        Integer id;
        Image image;
        Map<Integer, Image> imageCollectionBuilder = new HashMap<>();

        try {
            imageDirectory = new File(ImageCollection.class.getClassLoader().getResource(dirName).toURI());

            for (File file : imageDirectory.listFiles()) {
                try {
                    fileName = file.getName();
                    id = Integer.parseInt(fileName.substring(IMAGE_NUMBER_INDEX, IMAGE_NUMBER_INDEX + IMAGE_NUMBER_PREFIX_SIZE));
                    image = ImageIO.read(file);
                    imageCollectionBuilder.put(id, image);
                } catch (Exception e) {
                    // Ignore this file
                }
            }

        } catch (Exception e) { // On catch une Exception pour gérer le cas du NullPointerException
            throw new Error("Cannot find folder \"" + dirName + "\".");
        }

        imageCollection = Collections.unmodifiableMap(imageCollectionBuilder);

    }

    /**
     * Obtient l'image à l'index donné, ou retourne null si l'image n'est pas
     * trouvée
     * 
     * @param index
     *            L'index de l'image à obtenir
     * @return L'image si elle est trouvée, null sinon
     */
    public Image imageOrNull(int index) {
        return imageCollection.get(index);
    }

    /**
     * Obtient l'image à l'index donné, ou lance une exception si l'image n'est
     * pas trouvée
     * 
     * @param index
     *            L'index de l'image à obtenir
     * @return L'image si elle est trouvée dans la collection
     * @throws NoSuchElementException
     *             Si l'image n'est pas trouvée
     */
    public Image image(int index) throws NoSuchElementException {
        Image image = imageOrNull(index);
        if (image == null)
            throw new NoSuchElementException("Cannot find image of index " + index + ".");

        return image;

    }
}
