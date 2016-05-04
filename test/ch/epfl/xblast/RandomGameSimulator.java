package ch.epfl.xblast;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.GameStatePrinter;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

/**
 * @author Martin Chatton (262215)
 * @author Alexandre Abbey (235688)
 */
public class RandomGameSimulator {

    public static void main(String[] args) throws InterruptedException {
        RandomEventGenerator randomEvent = new RandomEventGenerator(2016, 30, 100);

        Scanner scanner = new Scanner(System.in);
        
        Level defaultLevel = Level.DEFAULT_LEVEL;

        GameState GS = defaultLevel.gameState();



        while (!GS.isGameOver()) {


            Map<PlayerID, Optional<Direction>> randomSpeedChangeEvents = randomEvent.randomSpeedChangeEvents();
            Set<PlayerID> randomBombDropEvents = randomEvent.randomBombDropEvents();

            boolean debugActivated = false;

            if (debugActivated && (!randomBombDropEvents.isEmpty() || !randomSpeedChangeEvents.isEmpty())) {
                System.out.println();

                for (PlayerID playerID : randomBombDropEvents) {
                    System.out.println("Le joueur n°" + playerID.toString().charAt(7) + " va poser une bombe !");
                }

                for (Map.Entry<PlayerID, Optional<Direction>> entry : randomSpeedChangeEvents.entrySet()) {
                    if (entry.getValue().isPresent()) {
                        System.out.println("Le joueur n°" + entry.getKey().toString().charAt(7) + " va dans la direction " + entry.getValue().get() + ".");
                    } else {
                        System.out.println("Le joueur n°" + entry.getKey().toString().charAt(7) + " va s'arrêter !");
                    }

                }

                //Thread.sleep(1000);
            }

            // GameStatePrinter.printGameState(GS);
            // scanner.nextLine();

            
            GS = GS.next(randomSpeedChangeEvents, randomBombDropEvents);


            //Thread.sleep(10);
        }
        
        GameStatePrinter.printGameState(GS);

    }
}
