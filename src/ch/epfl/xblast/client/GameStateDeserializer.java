package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.omg.CORBA.Object;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

/**
 * Représente un désérialiseur d'état
 * 
 * @author Sacha Kozma, 260391
 * @author Alexia Bogaert, 258330
 *
 */
public final class GameStateDeserializer {

    private static final int BOARD_SIZE_INDEX = 0;
    private static final int PLAYER_FACE_UNIT = 2;
    private static final int PLAYER_FACE_ALIVE = 0;
    private static final int PLAYER_FACE_DEAD = 1;
    private static final int PLAYER_VOID_TILE_WIDTH = 8;
    private static final int PLAYER_TILE_VOID_IMAGE = 12;
    private static final int PLAYER_TEXT_RIGHT_IMAGE = 11;
    private static final int PLAYER_TEXT_MIDDLE_IMAGE = 10;
    private static final int TIMELINE_FILLED_RECTANGLE_IMAGE = 21;
    private static final int TIMELINE_EMPTY_RECTANGLE_IMAGE = 20;
    private static final int TIMELINE_IMAGE_WIDTH = 60;
    private static final ImageCollection blockImageCollection = new ImageCollection("block");
    private static final ImageCollection explosionImageCollection = new ImageCollection("explosion");
    private static final ImageCollection playerImageCollection = new ImageCollection("player");
    private static final ImageCollection scoreImageCollection = new ImageCollection("score");
        
    private GameStateDeserializer() {
        // Non-instanciable
    }

    /**
     * Désérialise un état de jeu
     * 
     * @param serializedGameState
     *            Le jeu à désérialiser
     * @return Le jeu désérialisé, sous forme de GameState
     */
    public static GameState deserializeGameState(List<Byte> serializedGameState) {
        Objects.requireNonNull(serializedGameState, "serializedGameState should not be null");
        
        int boardSize = Byte.toUnsignedInt(serializedGameState.get(BOARD_SIZE_INDEX));
        int boardStartIndex = 1;
        int boardExclusiveEndIndex = boardStartIndex + boardSize;
        int explosionsSize = Byte.toUnsignedInt(serializedGameState.get(boardExclusiveEndIndex));
        int explosionStartIndex = boardExclusiveEndIndex + 1;
        int explosionExlusiveEndIndex = explosionStartIndex + explosionsSize;
        int playersStartIndex = explosionExlusiveEndIndex;
        byte remainingTime = serializedGameState.get(serializedGameState.size() - 1);
        
        List<Byte> boardSubList = serializedGameState.subList(boardStartIndex, boardExclusiveEndIndex);
        List<Byte> explosionSubList = serializedGameState.subList(explosionStartIndex, explosionExlusiveEndIndex);
        List<Byte> playersSubList = serializedGameState.subList(playersStartIndex, serializedGameState.size() - 1);
        
        List<Player> players = deserializePlayers(playersSubList);
        
        return new GameState(players, deserializeBoard(boardSubList), deserializeExplosions(explosionSubList), deserializeScores(players), deserializeTime(remainingTime));
    }

    private static List<Image> deserializeBoard(List<Byte> serializedBoard) {
        List<Byte> decodedBoard = RunLengthEncoder.decode(serializedBoard);
        List<Image> deserializedBoard = new ArrayList<>();

        for (Byte byte1 : decodedBoard) {
            deserializedBoard.add(blockImageCollection.image(byte1));
        }

        return deserializedBoard;
    }

    private static List<Image> deserializeExplosions(List<Byte> serializedExplosions) {
        List<Byte> decodedExplosions = RunLengthEncoder.decode(serializedExplosions);
        List<Image> deserializedExplosions = new ArrayList<>();

        for (Byte byte1 : decodedExplosions) {
            deserializedExplosions.add(explosionImageCollection.imageOrNull(byte1));
        }

        return deserializedExplosions;
    }

    private static List<Player> deserializePlayers(List<Byte> serializedPlayers) {
        PlayerID[] playerIDs = PlayerID.values();
        List<Player> players = new ArrayList<>();

        int lives, positionX, positionY, image;
        int index = 0;

        for (int i = 0; i < playerIDs.length; i++) {
            lives = Byte.toUnsignedInt(serializedPlayers.get(index++));
            positionX = Byte.toUnsignedInt(serializedPlayers.get(index++));
            positionY = Byte.toUnsignedInt(serializedPlayers.get(index++));
            image = Byte.toUnsignedInt(serializedPlayers.get(index++));

            players.add(new Player(playerIDs[i], lives, new SubCell(positionX, positionY), playerImageCollection.imageOrNull(image)));
        }

        return players;
    }

    private static List<Image> deserializeTime(byte serializedTime) {
        int remainingTime = Byte.toUnsignedInt(serializedTime);
        List<Image> deserializedTime = new ArrayList<>();
        
        deserializedTime.addAll(Collections.nCopies(remainingTime, scoreImageCollection.image(TIMELINE_FILLED_RECTANGLE_IMAGE)));
        deserializedTime.addAll(Collections.nCopies(TIMELINE_IMAGE_WIDTH - remainingTime, scoreImageCollection.image(TIMELINE_EMPTY_RECTANGLE_IMAGE)));
        
        return deserializedTime;
    }
    
    private static List<Image> deserializeScores (List<Player> players){
        List<Image> deserializedScores = new ArrayList<>();
        PlayerID id;
        
        for (Player player : players) {
            id = player.playerID();
            deserializedScores.add(scoreImageCollection.image((player.lives() > 0 ? PLAYER_FACE_ALIVE : PLAYER_FACE_DEAD) + (id.ordinal() * PLAYER_FACE_UNIT))); // add face
            deserializedScores.add(scoreImageCollection.image(PLAYER_TEXT_MIDDLE_IMAGE)); // add filling
            deserializedScores.add(scoreImageCollection.image(PLAYER_TEXT_RIGHT_IMAGE)); // add filling for text
            
            if (id == PlayerID.PLAYER_2) {
              deserializedScores.addAll(Collections.nCopies(PLAYER_VOID_TILE_WIDTH, scoreImageCollection.image(PLAYER_TILE_VOID_IMAGE)));
            }
            
        }
        
        
        return deserializedScores;
    }

}
