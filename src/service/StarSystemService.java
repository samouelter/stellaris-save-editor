package service;

import utils.CommonUtils;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class StarSystemService {

    private static final CommonUtils commonUtils = new CommonUtils();

    //Extract the galactic_object part of the gamestate (the star systems), and return a list of solar system
    public List<String> toStarSystemList(String gamestate) {
        String data = commonUtils.extractString(gamestate, "galactic_object={", "starbase_mgr=");
        boolean isActive = true;
        List<String> starSystems = new ArrayList<>();

        int i = 0;
        String current  = "0={";
        String next  = "1={";
        String starSystem = null;
        while (isActive) {
            current = i + "={";
            next = i + 1 + "={";

            starSystem = commonUtils.extractString(data, current, next);
            i++;

            if (starSystem == null) {
                starSystem = commonUtils.extractString(data, current, "\t}\n}");
                if (starSystem == null) {
                    isActive = false;
                } else {
                    starSystems.add(starSystem+"\n}");
                    isActive = false;
                }
            } else {
                starSystems.add(starSystem);
            }
        }
        return starSystems;
    }

    //indexA & indexB : the Indexes of the solar systems to swap
    //A & B : the body of the solar systems
    //Swap the two systems inside the gamestate
    public static String swapStarSystem(String gamestate, int indexA, String A, int indexB, String B) {

        //Getting the map coordinates of the systems from the body
        String coordinateA = commonUtils.extractString(A, "coordinate={", "type=");
        String coordinateB = commonUtils.extractString(B, "coordinate={", "type=");

        //Getting the hyperlanes of the systems from the body
        String hyperlaneA = commonUtils.extractString(A, "hyperlane={", "}\n \n\t\t}");
        String hyperlaneB = commonUtils.extractString(B, "hyperlane={", "}\n \n\t\t}");

        //Swapping the coodinates and hyperlanes
        String newA = A.replace(coordinateA, coordinateB);
        String newB = B.replace(coordinateB, coordinateA);

        newA = newA.replace(hyperlaneA, hyperlaneB);
        newB = newB.replace(hyperlaneB, hyperlaneA);

        gamestate = gamestate.replace(A, newA);
        gamestate = gamestate.replace(B, newB);


        //Swapping the incoming hyperlanes
        String toA = "to="+ indexA + "\n";
        String toB = "to="+ indexB + "\n";

        gamestate = gamestate.replace(toA, "PLACEHOLDER");
        gamestate = gamestate.replace(toB, toA);
        gamestate = gamestate.replace("PLACEHOLDER", toB);

        return gamestate;
    }




}
