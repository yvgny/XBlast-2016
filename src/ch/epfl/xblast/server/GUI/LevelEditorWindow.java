package ch.epfl.xblast.server.GUI;

import java.awt.Component;
import java.awt.Dimension;
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
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.XBlastServer;

/**
 * Fenêtre de création de niveau, qui permet de sélectionner le niveua par
 * défaut, de créer des niveau, d'en sauvegarder et d'en charger depuis une
 * sauvegarde
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
@SuppressWarnings("serial")
public class LevelEditorWindow extends JFrame {
    private static final DefaultComboBoxModel<Integer> DEFAULT_VALUE_SELECTOR_MODEL = new DefaultComboBoxModel<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
    private static final int BORDER_SIZE = 5;
    private static final int COMPONENENT_WIDTH = 458;
    private int minConnectedPlayerToStart = 4;
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
     * Créer la fenêtre de création de niveau
     * 
     * @throws URISyntaxException
     */
    public LevelEditorWindow() throws URISyntaxException {
        // paramètres générauy de la fenêtre
        setTitle("Level selection");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(COMPONENENT_WIDTH, 0));
        setMaximumSize(new Dimension(COMPONENENT_WIDTH, Integer.MAX_VALUE));
        contentPane = new JPanel();
        contentPane.setMaximumSize(new Dimension(COMPONENENT_WIDTH - 2 * BORDER_SIZE, Integer.MAX_VALUE));
        contentPane.setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
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
                boardListComponent.refresh();
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
        boardPanel.setBorder(new EmptyBorder(BORDER_SIZE, 0, 0, 0));
        contentPane.add(boardPanel);
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));

        boardCreatorComponent = new BoardCreatorComponent();
        boardPanel.add(boardCreatorComponent);
        boardCreatorComponent.setBorder(null);

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
                    levelName = JOptionPane.showInputDialog(null, "Please enter level name (be careful, if the level already exists, it will be replaced !) : ", "Level name", JOptionPane.PLAIN_MESSAGE);
                    if (levelName == null) {
                        return;
                    } else if (levelName.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a name !", "Level name invalid", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            // Sérialize le quadrant de jeu dans un fichier portant le nom désiré
                            objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(BoardListComponent.LEVEL_RELATIVE_PATH_FOLDER + levelName.replaceAll(" ", "_")))));
                            objectOutputStream.writeObject(boardCreatorComponent.getBoardNWQuadrant());
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
                boardListComponent.removeSelected();
            }
        });

        JButton btnOpenInEditor = new JButton("Open in editor");
        btnOpenInEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardCreatorComponent.setBoardNWQuadrant(boardListComponent.getBoardNWQuadrant());
                rdbtnUseCustomLevel.doClick();
            }
        });
        boardListSettings.add(btnOpenInEditor);
        boardListSettings.add(btnDelete);

        JButton btnRefresh = new JButton("Refresh list");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardListComponent.refresh();
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
        comboBoxLives.setModel(DEFAULT_VALUE_SELECTOR_MODEL);
        comboBoxLives.setSelectedIndex(2);
        playersSpecsEditor.add(comboBoxLives);

        JLabel lblBombs = new JLabel("Bombs :");
        playersSpecsEditor.add(lblBombs);

        JComboBox<Integer> comboBoxBombs = new JComboBox<Integer>();
        comboBoxBombs.setName("\n");
        comboBoxBombs.setModel(DEFAULT_VALUE_SELECTOR_MODEL);
        comboBoxBombs.setSelectedIndex(1);
        playersSpecsEditor.add(comboBoxBombs);

        JLabel lblBombRange = new JLabel("Bomb range :");
        playersSpecsEditor.add(lblBombRange);

        JComboBox<Integer> comboBoxBombsRange = new JComboBox<Integer>();
        comboBoxBombsRange.setModel(DEFAULT_VALUE_SELECTOR_MODEL);
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

                // Création de la liste des joueurs selon le choix de l'utilisateur
                if (rdbtnUseDefaultLevel.isSelected()) {
                    players = Level.DEFAULT_LEVEL.gameState().players();
                } else {
                    for (PlayerID playerID : PlayerID.values()) {
                        players.add(new Player(playerID, (Integer) comboBoxLives.getSelectedItem(), defaultPlayerPositions[playerID.ordinal()], (Integer) comboBoxBombs.getSelectedItem(), (Integer) comboBoxBombsRange.getSelectedItem()));
                    }
                }

                // Création du plateau de jeu selon le choix de l'utilisateur
                if (rdbtnUseCustomLevel.isSelected()) {
                    gameState = new GameState(Board.ofQuadrantNWBlocksWalled(boardCreatorComponent.getBoardNWQuadrant()), players);
                } else if (rdbtnUseDefaultLevel.isSelected()) {
                    gameState = Level.DEFAULT_LEVEL.gameState();
                } else {
                    gameState = new GameState(Board.ofQuadrantNWBlocksWalled(boardListComponent.getBoardNWQuadrant()), players);
                }

                Level level = new Level(gameState, Level.DEFAULT_LEVEL.boardPainter());

                // Lancement de la partie
                new Thread(new Runnable() {
                    public void run() {

                        try {
                            XBlastServer.startServer(level, minConnectedPlayerToStart);
                        } catch (InvocationTargetException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }

        });
    }

    /**
     * Permet de spécifier le nombre de joueurs minimum nécessaires avant le
     * lancement du serveur. Utile pour le debug
     * 
     * @param playerNumber
     */
    public void setMinimumPlayerToStart(int playerNumber) {
        this.minConnectedPlayerToStart = playerNumber;
    }
}
