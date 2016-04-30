package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        int boardSize = Byte.toUnsignedInt(serializedGameState.get(0));
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

        ImageCollection imageCollection = new ImageCollection("block");

        for (Byte byte1 : decodedBoard) {
            deserializedBoard.add(imageCollection.image(byte1));
        }

        return deserializedBoard;
    }

    private static List<Image> deserializeExplosions(List<Byte> serializedExplosions) {
        List<Byte> decodedExplosions = RunLengthEncoder.decode(serializedExplosions);
        List<Image> deserializedExplosions = new ArrayList<>();

        ImageCollection imageCollection = new ImageCollection("explosion");

        for (Byte byte1 : decodedExplosions) {
            deserializedExplosions.add(imageCollection.imageOrNull(byte1));
        }

        return deserializedExplosions;
    }

    private static List<Player> deserializePlayers(List<Byte> serializedPlayers) {
        PlayerID[] playerIDs = PlayerID.values();
        List<Player> players = new ArrayList<>();

        ImageCollection imageCollection = new ImageCollection("player");

        int lives, positionX, positionY, image;
        int index = 0;

        for (int i = 0; i < 4; i++) {
            lives = Byte.toUnsignedInt(serializedPlayers.get(index++));
            positionX = Byte.toUnsignedInt(serializedPlayers.get(index++));
            positionY = Byte.toUnsignedInt(serializedPlayers.get(index++));
            image = Byte.toUnsignedInt(serializedPlayers.get(index++));

            players.add(new Player(playerIDs[i], lives, new SubCell(positionX, positionY), imageCollection.imageOrNull(image)));
        }

        return players;
    }

    private static List<Image> deserializeTime(byte serializedTime) {
        int remainingTime = Byte.toUnsignedInt(serializedTime);
        ImageCollection imageCollection = new ImageCollection("score");
        List<Image> deserializedTime = new ArrayList<>();
        
        deserializedTime.addAll(Collections.nCopies(remainingTime, imageCollection.image(21)));
        deserializedTime.addAll(Collections.nCopies(60 - remainingTime, imageCollection.image(20)));
        
        return deserializedTime;
    }
    
    private static List<Image> deserializeScores (List<Player> players){
        List<Image> deserializedScores = new ArrayList<>();
        ImageCollection imageCollection = new ImageCollection("score");
        PlayerID id;
        for (Player player : players) {
            id = player.playerID();
            deserializedScores.add(imageCollection.image((player.lives() > 0 ? 0 : 1) + (id.ordinal() * 2))); // add face
            deserializedScores.add(imageCollection.image(10)); // add filling
            deserializedScores.add(imageCollection.image(11)); // add filling for text
        }
        
        return deserializedScores;
    }

}
