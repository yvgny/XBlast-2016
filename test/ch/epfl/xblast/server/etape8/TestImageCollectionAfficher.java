package ch.epfl.xblast.server.etape8;

import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.epfl.xblast.ImageCollection;

public class TestImageCollectionAfficher extends JFrame{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    JPanel jp = new JPanel();
    JLabel jl = new JLabel();

    public TestImageCollectionAfficher() throws URISyntaxException, IOException
    {
        ImageCollection c = new ImageCollection("explosion");
        @SuppressWarnings("static-access")
        Image bomb = c.imageOrNull(30);
           setTitle("Image");
           setVisible(true);
           setSize(100, 100);
           setDefaultCloseOperation(EXIT_ON_CLOSE);

           jl.setIcon(new ImageIcon(bomb));
           jp.add(jl);
           add(jp);

           validate();
    }

    public static void main(String[] args) throws URISyntaxException, IOException
    {
          @SuppressWarnings("unused")
        TestImageCollectionAfficher t = new TestImageCollectionAfficher();
    }
}
