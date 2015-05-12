package util;

public class Logger {
    private static final int STATE = 0;
    
    public Logger() {}
    
    public void log(String str) {
        switch (STATE) {
        case 0:
            System.out.println(str);
            break;
            
        default:
            System.out.println(str);
            break;
        }
    }
}