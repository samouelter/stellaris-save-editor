package service;


import model.Coordinates;
import model.Hyperlane;
import utils.CommonUtils;
import utils.FileUtils;

import static model.Coordinates.calculateDistance;

public class HyperlaneService {

    private static final FileUtils fileUtils = new FileUtils();
    private static final CommonUtils commonUtils = new CommonUtils();



    public static String addHyperlane(String gamestate, int indexA, String A, int indexB, String B) {

        //Getting the map coordinates of the systems from the body
        String coordinateA = commonUtils.extractString(A, "coordinate={", "type=");
        String coordinateB = commonUtils.extractString(B, "coordinate={", "type=");
        int distance = calculateDistance(
                Coordinates.getCoordinates(coordinateA), Coordinates.getCoordinates(coordinateB)
        );

        //Getting the hyperlanes of the systems from the body

        String newA = A.replace("hyperlane={", "hyperlane={\n" + new Hyperlane(indexB, distance));
        String newB = B.replace("hyperlane={", "hyperlane={\n" + new Hyperlane(indexA, distance));

        gamestate = gamestate.replace(A, newA);
        gamestate = gamestate.replace(B, newB);

        return gamestate;
    }

    public static String removeHyperlane(String gamestate, int indexA, String A, int indexB, String B) {

        //Getting the hyperlanes of the systems from the body
        String hyperlaneA = commonUtils.extractString(A, "hyperlane={", "}\n \n\t\t}");
        String hyperlaneB = commonUtils.extractString(B, "hyperlane={", "}\n \n\t\t}");

        String newA = A.replaceAll("\\{\\s*to=" + indexB + "\\s*length=\\b(?:[1-9]|[1-9]\\d{1,2})\\b\\s*(bridge=yes)?\\s*}", "");
        String newB = B.replaceAll("\\{\\s*to=" + indexA + "\\s*length=\\b(?:[1-9]|[1-9]\\d{1,2})\\b\\s*(bridge=yes)?\\s*}", "");

        gamestate = gamestate.replace(A, newA);
        gamestate = gamestate.replace(B, newB);

        return gamestate;
    }

}
