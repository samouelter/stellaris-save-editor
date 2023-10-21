package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinates {

    private float x;

    private float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static Coordinates getCoordinates(String coordinates) {

        String regexX = "x=(-?\\d+\\.\\d+)";
        String regexY = "y=(-?\\d+\\.\\d+)";

        // Create Pattern objects
        Pattern patternX = Pattern.compile(regexX);
        Pattern patternY = Pattern.compile(regexY);

        // Create Matcher objects
        Matcher matcherX = patternX.matcher(coordinates);
        Matcher matcherY = patternY.matcher(coordinates);
        
        Float xValue = new Float(0);
        Float yValue = new Float(0);

        // Find and print the values of x and y
        if (matcherX.find()) {
            xValue = Float.valueOf(matcherX.group(1));
        }

        if (matcherY.find()) {
            yValue = Float.valueOf(matcherY.group(1));
        }

        return new Coordinates(xValue, yValue);

    }

    public static int calculateDistance(Coordinates A, Coordinates B) {
        return (int) (Math.round(Math.sqrt(Math.pow(B.getX() - A.getX(), 2) + Math.pow(B.getY() - A.getY(), 2))))/10*9;
    }
}
