package util;

public class Logger {
    private static final boolean B = true;
    public static void Logln(String str) {
        if(B)
            System.out.println(str);
    }
    public static void Log(String str) {
        if(B)
            System.out.print(str);
    }
}