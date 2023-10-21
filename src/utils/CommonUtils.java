package utils;

public class CommonUtils {

    //Look through a String to return the substring between the @start and @end args
    public String extractString(String data, String start, String end) {

        int startIndex = data.indexOf(start);
        int endIndex = data.indexOf(end);

        if (startIndex >= 0 && endIndex >= 0) {
            return data.substring(startIndex, endIndex).trim();
        } else {
            return null;
        }
    }

}
