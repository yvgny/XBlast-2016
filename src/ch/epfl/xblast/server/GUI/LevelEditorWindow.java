package ch.epfl.xblast.server.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.XBlastServer;

public class LevelEditorWindow extends JFrame {
  private static final String LEVEL_FOLDER_RELATIVE_PATH = "game_data/levels/";
  private JPanel contentPane;
  private JPanel boardPanel;
  private JPanel boardButtonSettings;
  private JSeparator bottomSeparator;
  private BoardCreatorComponent boardCreatorComponent;
  private JPanel playersSpecsEditor;
  private JPanel levelListPanel;
  private BoardListComponent boardListComponent;
  private Cell[] defaultPlayerPositions = { new Cell(1, 1), new Cell(Cell.COLUMNS - 2, 1), new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), new Cell(1, Cell.ROWS - 2) };
  
  /**
   * Créer l'éditeur de niveau
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          LevelEditorWindow frame = new LevelEditorWindow();
          frame.setVisible(true);
          frame.setResizable(false);
          frame.pack();
          frame.setLocationRelativeTo(null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Créer la fenêtre de création de niveau
   * 
   * @throws URISyntaxException
   */
  public LevelEditorWindow() throws URISyntaxException {
    setTitle("Level selection");
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(458, 0));
    setMaximumSize(new Dimension(458, 1000));
    contentPane = new JPanel();
    contentPane.setMaximumSize(new Dimension(448, 1000));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

    JRadioButton rdbtnUseDefaultLevel = new JRadioButton("Use default level");
    contentPane.add(rdbtnUseDefaultLevel);
    rdbtnUseDefaultLevel.setAlignmentX(JRadioButton.CENTER_ALIGNMENT);
    rdbtnUseDefaultLevel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        boardCreatorComponent.setVisible(false);
        levelListPanel.setVisible(false);
        boardPanel.setVisible(false);
        playersSpecsEditor.setVisible(false);
        bottomSeparator.setVisible(false);
        repaint();
        pack();
      }
    });

    ButtonGroup levelSelection = new ButtonGroup();

    levelSelection.add(rdbtnUseDefaultLevel);

    rdbtnUseDefaultLevel.setSelected(true);

    JLabel lblDefaultLevelExplanation = new JLabel("Default level initialize players with three lives and two bombs");
    contentPane.add(lblDefaultLevelExplanation);
    lblDefaultLevelExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblDefaultLevelExplanation.setToolTipText("");
    // lblDefaultLevelExplanation.setBorder(new EmptyBorder(0, 27, 10, 0));
    lblDefaultLevelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));

    JRadioButton rdbtnUseCustomLevel = new JRadioButton("Use custom level");
    rdbtnUseCustomLevel.setActionCommand("Create costum level");
    contentPane.add(rdbtnUseCustomLevel);
    rdbtnUseCustomLevel.setAlignmentX(Component.CENTER_ALIGNMENT);
    rdbtnUseCustomLevel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        levelListPanel.setVisible(false);
        boardCreatorComponent.setVisible(true);
        boardPanel.setVisible(true);
        boardButtonSettings.setVisible(true);
        playersSpecsEditor.setVisible(true);
        bottomSeparator.setVisible(true);
        repaint();
        pack();
      }
    });
    levelSelection.add(rdbtnUseCustomLevel);

    JLabel lblCustomLevelExplanation = new JLabel("You can customize your board by clicking on each cell");
    contentPane.add(lblCustomLevelExplanation);
    lblCustomLevelExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblCustomLevelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));

    JRadioButton rdbtnLoadCostumLevel = new JRadioButton("Load costum level");
    rdbtnLoadCostumLevel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          boardListComponent.refresh();
        } catch (URISyntaxException e1) {
          e1.printStackTrace();
        }
        boardCreatorComponent.setVisible(false);
        boardPanel.setVisible(false);
        levelListPanel.setVisible(true);
        boardButtonSettings.setVisible(false);
        playersSpecsEditor.setVisible(true);
        bottomSeparator.setVisible(true);
        repaint();
        pack();
      }
    });
    rdbtnLoadCostumLevel.setActionCommand("Load custom level");
    rdbtnLoadCostumLevel.setAlignmentX(Component.CENTER_ALIGNMENT);
    levelSelection.add(rdbtnLoadCostumLevel);
    contentPane.add(rdbtnLoadCostumLevel);

    JLabel lblLoadedLevelExplanation = new JLabel("You can load a previously created level (using our board creator)");
    lblLoadedLevelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
    lblLoadedLevelExplanation.setAlignmentX(Component.CENTER_ALIGNMENT);
    contentPane.add(lblLoadedLevelExplanation);

    boardPanel = new JPanel();
    boardPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
    contentPane.add(boardPanel);
    boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));

    boardCreatorComponent = new BoardCreatorComponent();
    boardPanel.add(boardCreatorComponent);
    boardCreatorComponent.setBorder(null);
    boardCreatorComponent.setMinimumSize(new Dimension(488, 288));
    boardCreatorComponent.setMaximumSize(new Dimension(488, 288));

    boardButtonSettings = new JPanel();
    boardPanel.add(boardButtonSettings);
    boardButtonSettings.setVisible(false);
    boardButtonSettings.setLayout(new BoxLayout(boardButtonSettings, BoxLayout.X_AXIS));

    JButton btnClear = new JButton("Clear");
    boardButtonSettings.add(btnClear);

    JButton btnSaveAs = new JButton("Save as...");
    btnSaveAs.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ObjectOutputStream objectOutputStream;
        String levelName = null;

        do {
          levelName = JOptionPane.showInputDialog(null, "Please enter level name : ", "Level name", JOptionPane.PLAIN_MESSAGE);
          if (levelName == null) {
            return;
          } else if (levelName.length() == 0) {
            JOptionPane.showMessageDialog(null, "Please enter a name !", "Level name invalid", JOptionPane.ERROR_MESSAGE);
          } else {
            try {
              objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(LEVEL_FOLDER_RELATIVE_PATH + levelName.replaceAll(" ", "_")))));
              objectOutputStream.writeObject(boardCreatorComponent.board());
              objectOutputStream.close();
            } catch (FileNotFoundException e1) {
              e1.printStackTrace();
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          }
        } while (levelName.length() == 0);

      }
    });

    JButton btnReset = new JButton("Reset");
    btnReset.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        boardCreatorComponent.reset();
      }
    });
    boardButtonSettings.add(btnReset);
    boardButtonSettings.add(btnSaveAs);
    btnClear.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        boardCreatorComponent.clear();
      }
    });
    boardCreatorComponent.setVisible(false);

    levelListPanel = new JPanel();
    contentPane.add(levelListPanel);
    levelListPanel.setVisible(false);

    boardListComponent = new BoardListComponent();
    levelListPanel.add(boardListComponent);

    JPanel boardListSettings = new JPanel();
    levelListPanel.add(boardListSettings);
    boardListSettings.setLayout(new BoxLayout(boardListSettings, BoxLayout.Y_AXIS));

    JButton btnDelete = new JButton("Delete board");
    btnDelete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          boardListComponent.removeSelected();
        } catch (URISyntaxException e1) {
          e1.printStackTrace();
        }
      }
    });
    boardListSettings.add(btnDelete);

    JButton btnRefresh = new JButton("Refresh list");
    btnRefresh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          boardListComponent.refresh();
        } catch (URISyntaxException e1) {
          e1.printStackTrace();
        }
      }
    });
    boardListSettings.add(btnRefresh);

    bottomSeparator = new JSeparator();
    contentPane.add(bottomSeparator);
    bottomSeparator.setVisible(false);

    playersSpecsEditor = new JPanel();
    contentPane.add(playersSpecsEditor);
    playersSpecsEditor.setVisible(false);

    JLabel lblLives = new JLabel("Lives :");
    playersSpecsEditor.add(lblLives);

    JComboBox<Integer> comboBoxLives = new JComboBox<Integer>();
    comboBoxLives.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    comboBoxLives.setSelectedIndex(2);
    playersSpecsEditor.add(comboBoxLives);

    JLabel lblBombs = new JLabel("Bombs :");
    playersSpecsEditor.add(lblBombs);

    JComboBox<Integer> comboBoxBombs = new JComboBox<Integer>();
    comboBoxBombs.setName("\n");
    comboBoxBombs.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    comboBoxBombs.setSelectedIndex(1);
    playersSpecsEditor.add(comboBoxBombs);

    JLabel lblBombRange = new JLabel("Bomb range :");
    playersSpecsEditor.add(lblBombRange);

    JComboBox<Integer> comboBoxBombsRange = new JComboBox<Integer>();
    comboBoxBombsRange.setModel(new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
    comboBoxBombsRange.setSelectedIndex(2);
    playersSpecsEditor.add(comboBoxBombsRange);

    JPanel playPanel = new JPanel();
    contentPane.add(playPanel);

    JButton btnPlay = new JButton("Play !");
    playPanel.add(btnPlay);
    btnPlay.setHorizontalAlignment(SwingConstants.CENTER);
    btnPlay.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
        List<Player> players = new ArrayList<>();
        GameState gameState;

        if (rdbtnUseDefaultLevel.isSelected()) {
          players = Level.DEFAULT_LEVEL.gameState().players();
        } else {
          for (PlayerID playerID : PlayerID.values()) {
            players.add(new Player(playerID, (Integer) comboBoxLives.getSelectedItem(), defaultPlayerPositions[playerID.ordinal()], (Integer) comboBoxBombs.getSelectedItem(), (Integer) comboBoxBombsRange.getSelectedItem()));
          }
        }

        if (rdbtnUseCustomLevel.isSelected()) {
          gameState = new GameState(boardCreatorComponent.boardOfQuadrantNWBlocksWalled(), players);
        } else if (rdbtnUseDefaultLevel.isSelected()) {
          gameState = Level.DEFAULT_LEVEL.gameState();
        } else {
          gameState = new GameState(boardListComponent.getBoard(), players);
        }

        Level level = new Level(gameState, Level.DEFAULT_LEVEL.boardPainter());

        new Thread(new Runnable() {
          public void run() {

            try {
              XBlastServer.startServer(level, 1);
            } catch (InvocationTargetException | IOException | InterruptedException e) {
              e.printStackTrace();
            }

          }
        }).start();

      }

    });
  }
}
