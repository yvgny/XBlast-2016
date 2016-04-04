package ch.epfl.xblast.server.debug;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;


public final class GameStatePrinter {
    private static String blackBgd = "\u001b[40m";
    private static String stdBgd = "\u001b[49m";
    private static String cyanBgd = "\u001b[46m";
    private static String redBgd = "\u001b[41m";
    private static String greyBgd = "\u001b[47m";

    private static String blinkSlow = "\u001b[5m";
    private static String blinkOff = "\u001b[25m";

    private static String underlineSingle = "\u001b[4m";
    private static String underlineNone = "\u001b[24m";
    
    private GameStatePrinter() {
    }

    public static void printGameState(GameState s) {
        List<Player> alivePlayers = s.alivePlayers();
        List<Player> players = s.players();
        Set<Cell> blastedCells = s.blastedCells();
        Map<Cell, Bomb> bombedCells = s.bombedCells();
        Board board = s.board();

        Comparator<Player> playerComparator = (player1, player2) -> {
            List<PlayerID> playerIDs = Arrays.asList(PlayerID.values());
            if (playerIDs.indexOf(player1.id()) < playerIDs.indexOf(player2.id())) {
                return -1;
            } else if (playerIDs.indexOf(player1.id()) > playerIDs.indexOf(player2.id())) {
                return 1;
            } else {
                return 0;
            }
        };

        // Effacer le terminal
        System.out.println("\033[2J");
        
        // Affiche des ticks
        System.out.println("Tick : " + s.ticks());
        
        //Affiche le temps restant :
        System.out.println("Temps restant : " + s.remainingTime() + " sec."  + "\n");

        // Ajout des coordonnées pour x
        System.out.print("  ");
        for (int i = 0; i < Cell.COLUMNS; i++) {
            if (i < 10) {
                System.out.print(i + " ");
            } else {
                System.out.print(i);
            }
        }
        System.out.println();

        for (int y = 0; y < Cell.ROWS; ++y) {
            // Ajout des coordonnées pour y
            if (y < 10) {
                System.out.print(y + " ");
            } else {
                System.out.print(y);
            }

            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                for (Player p : alivePlayers) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                if (bombedCells.containsKey(c)) {
                    System.out.print(blinkSlow + redBgd + "XX" + stdBgd + blinkOff);
                } else if (blastedCells.contains(c)) {
                    if (s.board().blockAt(c).isFree()) {
                        System.out.print(redBgd + "  " + stdBgd);
                    } else {
                        Block b = board.blockAt(c);
                        System.out.print(stringForBlock(b));

                    }
                } else {
                    Block b = board.blockAt(c);
                    System.out.print(stringForBlock(b));
                }
            }
            System.out.println();
        }

        Collections.sort(players, playerComparator);
        System.out.println();

        // Affichage des joueurs
        for (Player player : players) {
            int numberOfCurrentBombs = 0;
            
            System.out.println(underlineSingle + "Joueur n°" + player.id().toString().charAt(7) + underlineNone);
            System.out.println("     " + player.lives() + " vies (" + player.lifeState().state() + ")");
            System.out.println("     bombes max : " + player.maxBombs() + ", portée bombes : " + player.bombRange());
            System.out.println("     position : sous-case " + player.position() + " dans la case " + player.position().containingCell());
            System.out.println("     Est sur une sous-case centrale : " + (player.position().isCentral() ? "oui" : "non"));
            System.out.println("     Distance à la sous-case centrale la plus proche : " + player.position().distanceToCentral());
            for (Bomb bomb : s.bombedCells().values()) {
                if (bomb.ownerId().equals(player.id())) {
                    numberOfCurrentBombs++;
                }
            }
            System.out.println("     " + numberOfCurrentBombs + " bombe(s) posée(s) en ce moment sur le plateau");
        }

    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N:
            b.append('^');
            break;
        case E:
            b.append('>');
            break;
        case S:
            b.append('v');
            break;
        case W:
            b.append('<');
            break;
        }
        return cyanBgd + b.toString() + stdBgd;
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE:
            return "  ";
        case INDESTRUCTIBLE_WALL:
            // return "##";
            return blackBgd + "  " + stdBgd;
        case DESTRUCTIBLE_WALL:
            // return "??";
            return greyBgd + "  " + stdBgd;
        case CRUMBLING_WALL:
            // return "¿¿";
            return blinkSlow + greyBgd + "¿¿" + stdBgd + blinkOff;
        case BONUS_BOMB:
            return "+b";
        case BONUS_RANGE:
            return "+r";
        default:
            throw new Error();
        }
    }
}
