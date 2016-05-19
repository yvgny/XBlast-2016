package ch.epfl.xblast.server;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.client.ImageCollection;

public final class BoardCreatorComponent extends JComponent {
    private final static int NW_QUADRANT_CELL_WIDTH = 7;
    private final static int NW_QUADRANT_CELL_HEIGHT = 6;
    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 48;
    private final Block[] blocks = Block.values();
    private final List<List<Block>> board;

    public BoardCreatorComponent() {
        board = new ArrayList<>();
        Board defaultBoard = Level.DEFAULT_LEVEL.gameState().board();
        addMouseListener(new BoardCreatorMouseListener(this));

        List<Block> tempList = new ArrayList<>();
        for (int y = 1; y <= NW_QUADRANT_CELL_HEIGHT; y++) {
            for (int x = 1; x <= NW_QUADRANT_CELL_WIDTH; x++) {
                tempList.add(defaultBoard.blockAt(new Cell(x, y)));
            }
            board.add(tempList);
            tempList = new ArrayList<>();
        }
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
    
    public void nextBlockAt (int x, int y){
        board.get(y).set(x, blocks[(board.get(y).get(x).ordinal() + 1) % 3]);
        repaint();
    }

    private Image imageForBlock(Block block) throws IOException, URISyntaxException {
        File image = null;

        switch (block) {
        case FREE:
            image = new File(getClass().getClassLoader().getResource("block/000_iron_floor.png").toURI());
            break;
        case DESTRUCTIBLE_WALL:
            image = new File(getClass().getClassLoader().getResource("block/003_extra.png").toURI());
            break;
        case INDESTRUCTIBLE_WALL:
            image = new File(getClass().getClassLoader().getResource("block/002_dark_block.png").toURI());
        default:
            break;
        }

        return ImageIO.read(image);
    }
    
    public Board board(){
        return Board.ofQuadrantNWBlocksWalled(board);
    }

    private final static class BoardCreatorMouseListener extends MouseAdapter {
        private final BoardCreatorComponent BCC;
        
        public BoardCreatorMouseListener(BoardCreatorComponent BCC) {
            this.BCC = BCC;
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() / DEFAULT_IMAGE_WIDTH;
            int y = e.getY() / DEFAULT_IMAGE_HEIGHT;
            
            BCC.nextBlockAt(x, y);
        }
        
    }
    
}