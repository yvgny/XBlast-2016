package ch.epfl.xblast.server;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public class LevelEditorWindow extends JFrame {

    private JPanel contentPane;
    private JButton btnClear;
    private BoardCreatorComponent boardCreatorComponent;
    private JPanel playersSpecsEditor;
    private Cell[] defaultPlayerPositions = {
            new Cell(1, 1), 
            new Cell(Cell.COLUMNS - 2, 1), 
            new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2), 
            new Cell(1, Cell.ROWS - 2)
            };

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
     */
    public LevelEditorWindow() {
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
                btnClear.setVisible(false);
                playersSpecsEditor.setVisible(false);
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
        contentPane.add(rdbtnUseCustomLevel);
        rdbtnUseCustomLevel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rdbtnUseCustomLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardCreatorComponent.setVisible(true);
                btnClear.setVisible(true);
                playersSpecsEditor.setVisible(true);
                repaint();
                pack();
            }
        });
        levelSelection.add(rdbtnUseCustomLevel);

        JLabel lblYouCanCustomize = new JLabel("You can customize your board by clicking on each cell");
        contentPane.add(lblYouCanCustomize);
        lblYouCanCustomize.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblYouCanCustomize.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        // lblYouCanCustomize.setBorder(new EmptyBorder(0, 27, 5, 0));

        boardCreatorComponent = new BoardCreatorComponent();
        boardCreatorComponent.setBorder(null);
        boardCreatorComponent.setMinimumSize(new Dimension(488, 288));
        boardCreatorComponent.setMaximumSize(new Dimension(488, 288));
        boardCreatorComponent.setVisible(false);
        contentPane.add(boardCreatorComponent);
        
        playersSpecsEditor = new JPanel();
        contentPane.add(playersSpecsEditor);
        playersSpecsEditor.setVisible(false);
        
        JLabel lblLives = new JLabel("Lives :");
        playersSpecsEditor.add(lblLives);
        
        JComboBox<Integer> comboBoxLives = new JComboBox<Integer>();
        comboBoxLives.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
        comboBoxLives.setSelectedIndex(2);
        playersSpecsEditor.add(comboBoxLives);
        
        JLabel lblBombs = new JLabel("Bombs :");
        playersSpecsEditor.add(lblBombs);
        
        JComboBox<Integer> comboBoxBombs = new JComboBox<Integer>();
        comboBoxBombs.setName("\n");
        comboBoxBombs.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
        comboBoxBombs.setSelectedIndex(1);
        playersSpecsEditor.add(comboBoxBombs);
        
        JLabel lblBombRange = new JLabel("Bomb range :");
        playersSpecsEditor.add(lblBombRange);
        
        JComboBox<Integer> comboBoxBombsRange = new JComboBox<Integer>();
        comboBoxBombsRange.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9}));
        comboBoxBombsRange.setSelectedIndex(2);
        playersSpecsEditor.add(comboBoxBombsRange);

        JPanel playPanel = new JPanel();
        contentPane.add(playPanel);

        JButton btnPlay = new JButton("Play !");
        playPanel.add(btnPlay);
        btnPlay.setHorizontalAlignment(SwingConstants.CENTER);

        btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardCreatorComponent.clear();
            }
        });
        btnClear.setVisible(false);
        playPanel.add(btnClear);
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                List<Player> players;
                GameState gameState;
                
                if (rdbtnUseCustomLevel.isSelected()) {
                    players = new ArrayList<>();
                    for (PlayerID playerID : PlayerID.values()) {
                        players.add(
                                new Player(playerID, 
                                (Integer)comboBoxLives.getSelectedItem(), 
                                defaultPlayerPositions[playerID.ordinal()], 
                                (Integer)comboBoxBombs.getSelectedItem(), 
                                (Integer)comboBoxBombsRange.getSelectedItem()));
                    }
                    gameState = new GameState(boardCreatorComponent.board(), players);
                } else {
                    players = Level.DEFAULT_LEVEL.gameState().players();
                    gameState = Level.DEFAULT_LEVEL.gameState();
                }
                
                Level level = new Level(gameState, Level.DEFAULT_LEVEL.boardPainter());

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Main.startServer(level, 1);
                        } catch (InvocationTargetException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });
    }

}
