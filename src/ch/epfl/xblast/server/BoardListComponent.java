package ch.epfl.xblast.server;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import ch.epfl.xblast.client.ImageCollection;
import javafx.scene.control.ScrollPane;

public class BoardListComponent extends JPanel {
  private static final int COMPONENT_WIDTH = 488;
  private static final int COMPONENT_HEIGHT = 288;
  private Map<String, List<List<Block>>> levels = fillWithLevel(new File(ImageCollection.class.getClassLoader().getResource("levels").toURI()));
  private JList<String> levelList;
  /**
   * Create the panel.
   * @throws URISyntaxException 
   */
  public BoardListComponent() throws URISyntaxException {
    DefaultListModel<String> listModel = new DefaultListModel<String>();
    
    for (Map.Entry<String, List<List<Block>>> entry : levels.entrySet()) {
      listModel.addElement(entry.getKey().replaceAll("_", " "));
    }
    
    levelList = new JList<String>(listModel);
    levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    levelList.setLayoutOrientation(JList.VERTICAL_WRAP);
    
    JScrollPane scrollPane = new JScrollPane(levelList);
    scrollPane.setPreferredSize(new Dimension(120, 200));
    add(scrollPane);

  }

  private Map<String, List<List<Block>>> fillWithLevel(File levelsDirectory) {
    ObjectInputStream ois;
    String name;
    Map<String, List<List<Block>>> levels = new HashMap<>();
    
    try {

      for (File file : levelsDirectory.listFiles()) {
        try {
          name = file.getName();
          name.replaceAll("_", " ");
          ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
          levels.put(name, (List<List<Block>>) ois.readObject());
          ois.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    } catch (Exception e) { // On catch une Exception pour gérer le cas du NullPointerException
      throw new Error("Cannot find folder \"" + levelsDirectory.getName() + "\".");
    }
    
    return levels;
  }
  
  public Board getBoard() {
    return Board.ofQuadrantNWBlocksWalled(levels.get(levelList.getSelectedValue().replaceAll(" ", "_")));
  }

  
}
