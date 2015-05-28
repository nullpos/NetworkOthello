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
    public static final String GENE_FILE = "genes.txt"; // 遺伝子情報を保存するファイル名
    
    public static final byte[][] GENE_ANS = {
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12},
        {1,2,3,4,5,6,7,8,9,10,11,12}
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
    private static final int B = Const.PBLACK;
    private static final int W = Const.PWHITE;
    private static final int O = Const.PUTABLE;

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
