package ch.epfl.xblast.server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Point;
import org.eclipse.wb.swing.FocusTraversalOnArray;

public class LevelEditorWindow extends JFrame {

    private JPanel contentPane;
    private BoardCreatorComponent boardCreatorComponent;
    /**
     * Launch the application.
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
     * Create the frame.
     */
    public LevelEditorWindow() {
        setLocation(new Point(0, 0));
        setTitle("Level selection");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(458, 0));
        setMaximumSize(new Dimension(458, 1000));
        contentPane = new JPanel();
        contentPane.setMaximumSize(new Dimension(458, 32767));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        
        JRadioButton rdbtnUseDefaultLevel = new JRadioButton("Use default level");
        rdbtnUseDefaultLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardCreatorComponent.setVisible(false);
                repaint();
                pack();
                setLocationRelativeTo(null);
            }
        });
        contentPane.add(rdbtnUseDefaultLevel);
        
        JRadioButton rdbtnUseCustomLevel = new JRadioButton("Use custom level");
        rdbtnUseCustomLevel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardCreatorComponent.setVisible(true);
                repaint();
                pack();
                setLocationRelativeTo(null);
                setMaximumSize(new Dimension(458, 1000));
            }
        });
        
        JLabel lblDefaultLevelExplanation = new JLabel("Default level initialize players with three lives and two bombs");
        lblDefaultLevelExplanation.setVisible(false);
        lblDefaultLevelExplanation.setBorder(new EmptyBorder(0, 27, 10, 0));
        lblDefaultLevelExplanation.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        contentPane.add(lblDefaultLevelExplanation);
        contentPane.add(rdbtnUseCustomLevel);
        
        ButtonGroup levelSelection = new ButtonGroup();
        levelSelection.add(rdbtnUseCustomLevel);
        levelSelection.add(rdbtnUseDefaultLevel);
        
        rdbtnUseDefaultLevel.setSelected(true);
        
        boardCreatorComponent = new BoardCreatorComponent();
        boardCreatorComponent.setMaximumSize(new Dimension(200, 200));
        boardCreatorComponent.setSize(new Dimension(448, 288));
        boardCreatorComponent.setVisible(false);
        
        JLabel lblYouCanCustomize = new JLabel("You can customize your board by clicking on each cell");
        lblYouCanCustomize.setVisible(false);
        lblYouCanCustomize.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        lblYouCanCustomize.setBorder(new EmptyBorder(0, 27, 5, 0));
        contentPane.add(lblYouCanCustomize);
        boardCreatorComponent.setBorder(new EmptyBorder(0, 0, 5, 0));
        contentPane.add(boardCreatorComponent);
        
        JPanel playPanel = new JPanel();
        playPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(playPanel);
        
        JButton btnPlay = new JButton("Play !");
        playPanel.add(btnPlay);
        btnPlay.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{rdbtnUseDefaultLevel, lblDefaultLevelExplanation, rdbtnUseCustomLevel, lblYouCanCustomize, boardCreatorComponent, playPanel, btnPlay}));
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

}
