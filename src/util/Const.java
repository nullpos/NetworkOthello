package util;

public class Const {
    private Const() {};

    public static final int SPACE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int PBLACK = 3; //ひっくり返らない
    public static final int PWHITE = 4; //やつ
    public static final int PUTABLE = 5; // 置ける場所

    public static final int BSIZE = 8; // ボードのサイズ
    public static final int OPTSIZE = 2; // オプション
    
    public static final int NGENE = 46;

    public static final String DRAW_STR = "DRAW";
    public static final String BLACK_STR = "BLACK";
    public static final String WHITE_STR = "WHITE";
    
    public static final int[] LEVEL_DEPTH = {3, 5, 7, 10, 15};

    public static final int OFF = 0;
    public static final int ON = 1;
}
