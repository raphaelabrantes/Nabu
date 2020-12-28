package dev.abrantes.Nabu;

public class CSI {
    static private final String c = "\033[";

    public static String eraseLineMoveUp(int n) {
        String cleanUp = c + "2K" + c + "1F";
        StringBuilder returnToStart = new StringBuilder(cleanUp);
        returnToStart.append(cleanUp.repeat(Math.max(0, n - 1)));
        return returnToStart.toString();
    }
    public static String eraseLine(){
        return c + "2K";
    }
}
