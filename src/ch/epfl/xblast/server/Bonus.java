package ch.epfl.xblast.server;

/**
 * Représente les différents bonus disponibles dans le jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public enum Bonus {
    
    /**
     * Le bonus bombe (rajoute 1 au nombre maximum de bombe que peut avoir le
     * joueur sur lui, jusqu'à 9 maximum)
     */
    INC_BOMB {
        private final int MAX_BOMBS_CAPACITY = 9;
        
        @Override
        public Player applyTo(Player player) {
            return player.maxBombs() == MAX_BOMBS_CAPACITY ? player : player.withMaxBombs(player.maxBombs() + 1);
        }
    },

    /**
     * Le bonus portée (rajoute 1 à la portée des bombes du joueur, jusqu'à 9
     * maximum)
     */
    INC_RANGE {
        private final int MAX_BOMBS_RANGE = 9;
        
        @Override
        public Player applyTo(Player player) {
            return player.bombRange() == MAX_BOMBS_RANGE ? player : player.withBombRange(player.bombRange() + 1);
        }
    };

    /**
     * Applique le bonus au joueur donné, et retourne le joueur affecté
     * 
     * @param player
     *            Le joueur sur lequel il faut appliquer le bonus
     * @return Le joueur affecté
     */
    abstract public Player applyTo(Player player);
}
