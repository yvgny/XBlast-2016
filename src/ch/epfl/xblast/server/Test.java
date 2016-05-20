package ch.epfl.xblast.server;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class Test {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> createUI());
    }
    
    public static void createUI(){
        JFrame window = new JFrame("Board editor");
        
        BoardCreatorComponent XBC = new BoardCreatorComponent();
        
        window.setContentPane(XBC);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}
