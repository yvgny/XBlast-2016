package ch.epfl.xblast.server;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.FlowLayout;

public class LevelEditorWindow extends JFrame {

    private JPanel contentPane;
    private JButton btnClear;
    private BoardCreatorComponent boardCreatorComponent;

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
                List<Player> defaultPlayers = Level.DEFAULT_LEVEL.gameState().players();
                GameState gameState = new GameState(boardCreatorComponent.board(), defaultPlayers);
                Level level = new Level(gameState, Level.DEFAULT_LEVEL.boardPainter());

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Main.startServer(level, 4);
                        } catch (InvocationTargetException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });
    }

}
