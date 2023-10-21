package model;

public class Hyperlane {

    private int to;

    private int length;

    public Hyperlane(int to, int length) {
        this.to = to;
        this.length = length;
    }

    public String toString() {
        return " {\n" +
                "\t\t\t\tto=" + to + "\n" +
                "\t\t\t\tlength="+length+"\n" +
                "\t\t\t}";
    }
}
