package util;

public class Const {
    private Const() {};

    // 基本的な設定
    public static final int SPACE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    public static final int PBLACK = 3; // ひっくり返らない
    public static final int PWHITE = 4; // やつ
    public static final int PUTABLE = 5; // 置ける場所

    public static final int BSIZE = 8; // ボードのサイズ
    public static final int OPTSIZE = 2; // オプションの数

    public static final String DRAW_STR = "DRAW";
    public static final String BLACK_STR = "BLACK";
    public static final String WHITE_STR = "WHITE";
    public static final String PASS_STR = "PASS";

    public static final String SETTINGS_NAME = "settings";
    public static final String PLAYER_NAME_ID = "pLAYERnAME:";

    public static final String[] PRINT_BOARD = {"_", "b", "w", "B", "W", "@"};
    
    // オプション関連
    public static final String[] OPTION_ID = {"Premium_Option"};
    public static final int OFF = 0;
    public static final int ON = 1;
    
    //学習関連
    public static final int GENE_NUM = 20; // 集団の遺伝子数
    public static final int CHROMO_NUM = 12; // chromosomeの数
    public static final String GENE_DIR = "genes/"; // 遺伝子情報を保存するディレクトリ名
    
    public static final byte[][] GENE_ANS = {
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12}
    };
    public static final byte[][] TEACHERS01 = {
        {23, -103, 109, 119, 95, 23, -87, 105, -74, 20, 77, -48},
        {95, -106, 20, -27, 83, -52, 53, -49, 4, -46, 66, 79},
        {21, 22, -106, 111, 65, -111, -85, 98, -102, -105, 56, 25},
        {109, -86, 87, 53, 21, -58, -126, 121, -36, -104, 22, 1},
        {74, -46, 126, 55, 13, -119, -71, 53, 57, 58, 28, 58},
        {-77, 28, 82, -47, -90, -62, -5, 113, -124, -1, 95, -78},
        {67, 100, 21, -120, 46, 9, -74, -53, -20, -118, 107, -8},
        {58, -82, 99, 123, -90, 17, 35, -20, 61, 63, -14, 62},
        {-26, -46, 26, 76, -92, -74, 41, 54, -38, -21, 47, -112},
        {36, 126, 52, 75, -39, 29, 10, -122, -91, 27, 90, 93}
    };
    
    // コンピュータ関連
    public static final int[] LEVEL_DEPTH = {1, 2, 3, 4, 5};
    public static final int[][] WEIGHT = {
        {0, 1, 2, 3, 3, 2, 1, 0},
        {1, 4, 5, 6, 6, 5, 4, 1},
        {2, 5, 7, 8, 8, 7, 5, 2},
        {3, 6, 8, 9, 9, 8, 6, 3},
        {3, 6, 8, 9, 9, 8, 6, 3},
        {2, 5, 7, 8, 8, 7, 5, 2},
        {1, 4, 5, 6, 6, 5, 4, 1},
        {0, 1, 2, 3, 3, 2, 1, 0}
    };
    
    // 盤面の設定
    private static final int $ = Const.SPACE;
    private static final int b = Const.BLACK;
    private static final int w = Const.WHITE;
    //private static final int B = Const.PBLACK;
    private static final int W = Const.PWHITE;
    //private static final int O = Const.PUTABLE;

    public static final int[][] INIT_BOARD = {
        {$, $, $, $, $, $, $, $},
        {$, $, $, $, $, $, $, $},
        {$, $, $, $, $, $, $, $},
        {$, $, $, b, w, $, $, $},
        {$, $, $, w, b, $, $, $},
        {$, $, $, $, $, $, $, $},
        {$, $, $, $, $, $, $, $},
        {$, $, $, $, $, $, $, $}
    };
    public static final int[][] INIT_BOARD2 = {
        {w, w, w, b, W, $, b, w},
        {b, w, b, b, b, w, b, w},
        {b, w, w, b, w, b, w, w},
        {b, w, b, b, w, w, w, w},
        {b, w, b, b, w, b, w, $},
        {b, w, b, w, b, w, w, w},
        {b, b, w, b, b, b, w, w},
        {b, b, b, b, b, w, w, w}
    };
    
    // サーバー関連
    public static final int MEMBER = 1024; // 最大接続可能人数
    public static final String LEAVE_MES = "PLAYER LEFT!"; // 切断されたときに送る
}
