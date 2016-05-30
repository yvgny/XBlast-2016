package ch.epfl.xblast.server.GUI;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ch.epfl.xblast.server.Block;

/**
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
@SuppressWarnings("serial")
public class BoardListComponent extends JPanel {
    private static final int COMPONENT_WIDTH = 160;
    private static final int COMPONENT_HEIGHT = 200;
    private static final String LEVEL_RELATIVE_PATH_FOLDER = "game_data/levels";
    private DefaultListModel<String> listModel;
    private Map<String, List<List<Block>>> levels;
    private JList<String> levelList;

    /**
     * Créer un composant de choix de niveau. Ce composant offre une liste
     * affichant tous les niveaux sauvegardés, chargés depuis des fichiers.
     * 
     * @throws URISyntaxException
     */
    public BoardListComponent() throws URISyntaxException {
        listModel = new DefaultListModel<String>();

        levelList = new JList<String>(listModel);
        levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        levelList.setLayoutOrientation(JList.VERTICAL_WRAP);

        JScrollPane scrollPane = new JScrollPane(levelList);
        scrollPane.setPreferredSize(new Dimension(COMPONENT_WIDTH, COMPONENT_HEIGHT));
        add(scrollPane);

        refresh();
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<List<Block>>> fillWithLevel(File levelsDirectory) {
        ObjectInputStream objectInputStream;
        String name;
        Map<String, List<List<Block>>> levels = new HashMap<>();
        new File(LEVEL_RELATIVE_PATH_FOLDER).mkdirs();

        try {

            for (File file : levelsDirectory.listFiles()) {
                try {
                    name = file.getName();
                    objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                    levels.put(name, (List<List<Block>>) objectInputStream.readObject());
                    objectInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) { // On catch une Exception pour gérer le cas du
                                // NullPointerException
            throw new Error("Cannot find folder \"" + levelsDirectory.getName() + "\".");
        }

        return levels;
    }

    /**
     * 
     * @return Le plateau de jeu (seuelement le quadrant nord ouest, non muré)
     *         selectionné dans la liste
     */
    public List<List<Block>> boardNWQuadrant() {
        return new ArrayList<>(levels.get(levelList.getSelectedValue().replaceAll(" ", "_")).stream().map(sublist -> new ArrayList<>(sublist)).collect(Collectors.toList()));
    }

    /**
     * Raffraichit la liste et permet de trouver les nouveau fichiers
     */
    public void refresh() {
        listModel.clear();

        levels = fillWithLevel(new File(LEVEL_RELATIVE_PATH_FOLDER));

        for (Map.Entry<String, List<List<Block>>> entry : levels.entrySet()) {
            listModel.addElement(entry.getKey().replaceAll("_", " "));
        }

        levelList.setVisibleRowCount(listModel.size());

        repaint();
    }

    /**
     * Supprime l'entrée selectionnée, et supprime donc le fichier associé
     */
    public void removeSelected() {
        if (levelList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You didn't select any level !", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String levelName = levelList.getSelectedValue().replaceAll(" ", "_");

        File file = new File(LEVEL_RELATIVE_PATH_FOLDER + "/" + levelName);
        file.delete();
        refresh();

    }

}
