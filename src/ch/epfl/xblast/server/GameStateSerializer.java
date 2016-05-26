package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * Représente un sérialiseur d'état de jeu
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class GameStateSerializer {
    private static final int TIME_MUTLIPLE_INTEGERS_PER_SECONDS = 2;

    private GameStateSerializer() {
        // Non-instanciable
    }

    /**
     * Retourne la version sérialisée de selon l'état de jeu et le peintre de
     * plateau donné
     * 
     * @param boardPainter
     *            Le peintre de tableau à utiliser pour sérialiser l'état de jeu
     * @param gameState
     *            L'état de jeu à sérialisé
     * @return La version sérialisée de l'état de jeu
     * @throws NullPointerException
     *             Si l'état de jeu ou le peintre de plateau est nul
     */
    public static List<Byte> serialize(BoardPainter boardPainter, GameState gameState) throws NullPointerException {
        Objects.requireNonNull(boardPainter, "boardPainter must not be null");
        Objects.requireNonNull(gameState, "gameState must not be nul");
        
        List<Byte> serialized = new ArrayList<>();
        
        // Ajout du plateau de jeu (compressé)
        List<Byte> boardSerialized = RunLengthEncoder.encode(serializeBoard(boardPainter, gameState.board()));
        
        serialized.add((byte) boardSerialized.size());
        
        serialized.addAll(boardSerialized);
        
        // Ajout des bombes et des explosions (compressés)
        List<Byte> bombsExplosionsSerialized = RunLengthEncoder.encode(serializeBombsExplosions(boardPainter, gameState));
        serialized.add((byte) bombsExplosionsSerialized.size());
        
        serialized.addAll(bombsExplosionsSerialized);
        
        // Ajout des joueurs
        serialized.addAll(serializePlayers(gameState.players(), gameState.ticks()));
        
        // Ajout du temps restant
        serialized.add((byte) Math.ceil(gameState.remainingTime() / TIME_MUTLIPLE_INTEGERS_PER_SECONDS));
        
        return serialized;
    }

    private static List<Byte> serializeBoard(BoardPainter boardPainter, Board board) {
        List<Byte> serializedBoard = new ArrayList<>();

        for (Cell cell : Cell.SPIRAL_ORDER) {
            serializedBoard.add(boardPainter.byteForCell(board, cell));
        }
        
        return serializedBoard;
    }

    private static List<Byte> serializeBombsExplosions(BoardPainter boardPainter, GameState gameState) {
        List<Byte> serializedBombsExplosions = new ArrayList<>();

        for (Cell cell : Cell.ROW_MAJOR_ORDER) {

            if (gameState.bombedCells().containsKey(cell)) {
                serializedBombsExplosions.add(ExplosionPainter.byteForBomb(gameState.bombedCells().get(cell)));

            } else if (gameState.blastedCells().contains(cell)) {
                serializedBombsExplosions.add(ExplosionPainter.byteForBlast(cell, gameState));

            } else {
                serializedBombsExplosions.add(ExplosionPainter.BYTE_FOR_EMPTY);
            }
        }

        return serializedBombsExplosions;
    }

    private static List<Byte> serializePlayers(List<Player> players, int ticks) {
        List<Byte> serializedPlayers = new ArrayList<>();
        
        for (Player player : players) {
            serializedPlayers.add((byte) player.lives());
            serializedPlayers.add((byte) player.position().x());
            serializedPlayers.add((byte) player.position().y());
            serializedPlayers.add(PlayerPainter.byteForPlayer(ticks, player));
        }
        
        return serializedPlayers;
    }

}
