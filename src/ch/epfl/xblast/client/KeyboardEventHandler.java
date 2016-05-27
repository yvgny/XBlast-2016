package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * Représente un auditeur d'événements clavier pour contrôler un joueur simulé
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class KeyboardEventHandler implements KeyListener {
    private final Map<Integer, PlayerAction> actions;
    private final Consumer<PlayerAction> actionConsumer;

    /**
     * Construit un auditeur d'évenements clavier
     * 
     * @param actions
     *            Une table associant des actions de joueur à des codes de
     *            touche
     * @param actionConsumer
     *            Un consommateur d'action de joueur
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> actions,
            Consumer<PlayerAction> actionConsumer) {
        this.actions = Collections.unmodifiableMap(new HashMap<>(Objects.requireNonNull(actions)));
        this.actionConsumer = Objects.requireNonNull(actionConsumer);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (actions.containsKey(e.getKeyCode()))
            actionConsumer.accept(actions.get(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused
    }

}
