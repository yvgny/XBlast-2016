package ch.epfl.xblast.server;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import ch.epfl.xblast.server.GUI.LevelEditorWindow;

/**
 * Créer le lanceur de serveur, qui permet de configurer les différentes options
 * (carte, nombres de joueur, de bombes, etc..) du serveur avant de démarrer
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class Main {
    private static LevelEditorWindow levelEditorWindow;

    /**
     * Créer le lanceur de serveur
     * 
     * @param args
     *            Prend un seul argument : le nombre de joueur minimum connectés
     *            necessaire avant le lancement du serveur. Utile pour le debug.
     *            Si rien n'est spécifié, la valeur de 4 est utilisée
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(() -> createUI());

        levelEditorWindow.setMinimumPlayerToStart(args.length == 0 ? 4 : Integer.parseInt(args[0]));
    }

    private static void createUI() {
        try {
            levelEditorWindow = new LevelEditorWindow();
            levelEditorWindow.setVisible(true);
            levelEditorWindow.setResizable(false);
            levelEditorWindow.pack();
            levelEditorWindow.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
