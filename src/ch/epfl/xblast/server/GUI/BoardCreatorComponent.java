package ch.epfl.xblast.server.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.ImageCollection;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Level;

/**
 * Composant d'édition de niveau
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
@SuppressWarnings("serial")
public final class BoardCreatorComponent extends JComponent {
    private final static int DESTRUCTIBLE_BLOCK_IMAGE_INDEX = 3;
    private final static int INDESTRUCTIBLE_BLOCK_IMAGE_INDEX = 2;
    private final static int FREE_BLOCK_IMAGE_INDEX = 0;
    private final static int NW_QUADRANT_CELL_WIDTH = 7;
    private final static int NW_QUADRANT_CELL_HEIGHT = 6;
    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 48;
    private final static ImageCollection blockImages = new ImageCollection("block");
    private final Block[] blocks = Block.values();
    private final List<List<Block>> defaultBoardNWQuadrant;
    private List<List<Block>> board;

    /**
     * Créer un composant Swing permettant de modifier le plateau de jeu. Le
     * quadrant nord ouest est affiché et peut être modifié. Cela permet ensuite
     * de générer un plateau symétrique, entouré de mur indestrucible
     * facilement.
     */
    public BoardCreatorComponent() {
        defaultBoardNWQuadrant = new ArrayList<>();
        Board defaultBoard = Level.DEFAULT_LEVEL.gameState().board();
        addMouseListener(new BoardCreatorMouseListener(this));

        List<Block> tempList = new ArrayList<>();
        for (int y = 1; y <= NW_QUADRANT_CELL_HEIGHT; y++) {
            for (int x = 1; x <= NW_QUADRANT_CELL_WIDTH; x++) {
                tempList.add(defaultBoard.blockAt(new Cell(x, y)));
            }
            defaultBoardNWQuadrant.add(tempList);
            tempList = new ArrayList<>();
        }

        // Copie profonde de la liste
        board = new ArrayList<>(defaultBoardNWQuadrant.stream().map(list -> new ArrayList<>(list)).collect(Collectors.toList()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        for (int y = 0; y < NW_QUADRANT_CELL_HEIGHT; y++) {
            for (int x = 0; x < NW_QUADRANT_CELL_WIDTH; x++) {
                try {
                    g.drawImage(imageForBlock(board.get(y).get(x)), x * DEFAULT_IMAGE_WIDTH, y * DEFAULT_IMAGE_HEIGHT, null);
                } catch (Exception e) {
                    // Ignore this image
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_IMAGE_WIDTH * NW_QUADRANT_CELL_WIDTH, DEFAULT_IMAGE_HEIGHT * NW_QUADRANT_CELL_HEIGHT);
    }

    /**
     * Permet de passer au bloc "suivant" dans l'énumération Block (en ne
     * considérant que les blocs libres, indestructible et destructible).
     * 
     * @param x
     *            La coordonnée x de la case a changer
     * @param y
     *            La coordonnée y de la case à changer
     */
    public void nextBlockAt(int x, int y) {
        // Permet de changer le bloc en prenant le bloc suivant
        board.get(y).set(x, blocks[(board.get(y).get(x).ordinal() + 1) % 3]);
        repaint();
    }

    private Image imageForBlock(Block block) throws IOException, URISyntaxException {
        Image image = null;

        switch (block) {
        case FREE:
            image = blockImages.image(FREE_BLOCK_IMAGE_INDEX);
            break;
        case INDESTRUCTIBLE_WALL:
            image = blockImages.image(INDESTRUCTIBLE_BLOCK_IMAGE_INDEX);
            break;
        case DESTRUCTIBLE_WALL:
            image = blockImages.image(DESTRUCTIBLE_BLOCK_IMAGE_INDEX);
            break;
        default:
            throw new NoSuchElementException("cannot find block " + block);
        }

        return image;
    }

    /**
     * 
     * @return Le quadrant nord ouest du plateau de jeu
     */
    public List<List<Block>> getBoardNWQuadrant() {
        return new ArrayList<>(board.stream().map(sublist -> new ArrayList<>(sublist)).collect(Collectors.toList()));
    }

    /**
     * Modifie le plateau de jeu utilisé par l'éditeur de niveau
     * 
     * @param board
     *            Le board à utiliser
     */
    public void setBoardNWQuadrant(List<List<Block>> board) {
        Objects.requireNonNull(board);
        this.board = new ArrayList<>(board.stream().map(sublist -> new ArrayList<>(sublist)).collect(Collectors.toList()));
    }

    private final static class BoardCreatorMouseListener extends MouseAdapter {
        private final BoardCreatorComponent BCC;

        public BoardCreatorMouseListener(BoardCreatorComponent BCC) {
            this.BCC = BCC;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() / DEFAULT_IMAGE_WIDTH;
            int y = e.getY() / DEFAULT_IMAGE_HEIGHT;
            if (x == 0 && y == 0) {
                JOptionPane.showMessageDialog(BCC, "Cannot modify cell (0,0) because this is the players spawn position", "Invalid action", JOptionPane.WARNING_MESSAGE);
            } else {
                BCC.nextBlockAt(x, y);
            }
        }

    }

    /**
     * Remplit l'éditeur de niveau avec des blocs vides
     */
    public void clear() {
        for (List<Block> list : board) {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, Block.FREE);
            }
        }
        repaint();

    }

    /**
     * Remplit l'éditeur de niveua avec le plateau de jeu par défaut
     */
    public void reset() {
        // Copie profonde de la liste
        board = new ArrayList<>(defaultBoardNWQuadrant.stream().map(list -> new ArrayList<>(list)).collect(Collectors.toList()));
        repaint();
    }

}
