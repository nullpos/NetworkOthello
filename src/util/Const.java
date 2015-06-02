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
    public static final int GENE_NUM = 50; // 集団の遺伝子数
    public static final int CHROMO_NUM = 36; // chromosomeの数
    public static final String GENE_DIR = "genes/"; // 遺伝子情報を保存するディレクトリ名
    
    public static final byte[][] GENE_ANS = {
        {66, 22, 112, -39, -31, -52, -37, 28, 41, -102, -24, 2, -3, -87, -109, -3, -48, -19, -83, 116, 81, 5, 126, -29, 2, -88, 29, 68, -4, -16, -118, -118, 22, -3, 88, 18},
        {62, -86, 14, 97, 123, -33, 123, 54, -42, 57, 26, -29, 96, 30, 52, -23, 59, 72, -95, 85, -41, -71, -59, 69, 35, -37, -46, -68, 91, 33, -79, 94, -93, 58, 43, 17},
        {102, 18, -21, -58, -82, -120, 80, 60, 59, -8, -13, 17, 105, -30, -31, 95, 46, -69, -70, 87, 64, -40, 62, -119, 10, 86, 37, 125, -105, -117, -53, 116, 21, -107, 78, -30},
        {109, -33, 30, 69, -118, -78, -61, 55, 88, -71, 127, 65, 126, 30, -82, -58, -127, -26, -27, -78, -18, 106, 44, 9, 101, 1, 74, -58, -64, -89, -114, -73, -100, -105, 100, 101},
        {92, -87, 33, 107, -116, -124, 16, -99, 121, 122, 56, 117, 3, -107, 103, -62, 7, -59, 71, -38, -112, -49, 28, 35, 110, 13, -67, -42, -101, -110, -65, -43, -115, -120, 95, 112},
    };
    
    public static final byte[][][] GENE_TEACHER = {
        {
            {-125, 116, -98, 121, -117, -33, 76, 42, -122, -123, -100, 109, 117, 74, 42, 38, 2, -33, 60, -83, 23, 78, 76, -96, 111, 16, 116, -68, 24, -7, -11, 81, -27, 61, 40, -114},
            {25, 90, -38, -79, 52, -51, 105, -61, 76, 27, 65, 126, -104, 118, 0, -18, 75, 39, -54, 7, -45, 56, -78, 59, -22, 81, 26, 101, -85, -70, -48, -24, 47, 96, 106, -40},
            {-93, 9, -77, 58, 81, 113, -39, 71, 82, 66, -80, -100, 50, -80, -110, -66, -54, -105, -4, 92, -91, -102, -32, 75, -40, 123, 23, 90, 123, 21, -48, -115, -40, 97, 17, -116},
            {101, 125, 37, -28, -107, -62, -58, 4, 30, 47, 70, -113, -15, -111, 17, 92, -84, 98, -97, -1, -18, 3, 121, 97, -44, 103, 44, -101, -43, 51, -12, -60, -100, -90, 14, -56},
            {-91, 68, -3, -75, 104, 32, 32, 19, -24, 27, -30, -92, -94, 10, 74, 64, -36, 4, -116, 76, 51, 81, 116, 54, 6, 5, -76, -48, -51, 47, -72, -36, 94, 13, 110, 23},
            {15, 124, -72, -86, 68, 82, -62, 107, -46, 74, -29, -76, -4, -119, 44, 90, 116, 27, 94, 118, 55, -114, 27, 64, 119, 110, -62, -35, -118, 37, 38, -43, -108, 99, 4, 56},
            {90, 27, -29, -115, -110, 124, 71, -84, 91, -106, 26, -116, 76, -55, -19, -127, 57, 73, 88, 16, 50, 19, 63, 102, -112, -27, 116, 108, -1, -63, 113, -58, 52, 51, 69, -55},
            {87, 50, 29, -71, -8, -9, -1, 29, 58, -85, 29, -108, 62, 44, 84, -11, -13, -57, -63, -74, 45, 77, 10, 111, -121, 28, -73, -3, 47, -49, -43, 97, -79, -60, 5, -128},
            {-123, -19, 93, -121, 107, -54, 70, -67, -9, 6, -26, 110, 28, -59, 34, 58, -32, 115, -11, 119, -4, 117, -11, -67, -30, -8, -81, -68, 64, 85, 124, -65, 18, -20, 106, 37},
            {23, 34, -114, -20, 63, -53, -110, 29, -101, 123, 124, 98, 59, 47, -101, -24, 97, -94, -20, -47, 70, 11, 47, -53, 69, 0, 50, -38, -22, -28, -51, 9, 94, 123, -20, 102}
        }
    };
    
    // コンピュータ関連
    public static final int[] LEVEL_DEPTH = {1, 1, 2, 3, 4};
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
    private static final int s = Const.SPACE;
    private static final int b = Const.BLACK;
    private static final int w = Const.WHITE;
    private static final int B = Const.PBLACK;
    private static final int W = Const.PWHITE;
    //private static final int O = Const.PUTABLE;

    public static final int[][] INIT_BOARD2 = {
        {s, s, s, s, s, s, s, s},
        {s, s, s, s, s, s, s, s},
        {s, s, s, s, s, s, s, s},
        {s, s, s, b, w, s, s, s},
        {s, s, s, w, b, s, s, s},
        {s, s, s, s, s, s, s, s},
        {s, s, s, s, s, s, s, s},
        {s, s, s, s, s, s, s, s}
    };
    public static final int[][] INIT_BOARD = {
        {s, s, s, s, w, s, b, w},
        {s, s, b, b, b, w, b, w},
        {s, w, w, b, w, b, w, w},
        {w, s, b, b, W, w, w, w},
        {b, b, b, b, w, b, w, s},
        {s, w, b, w, b, w, w, w},
        {s, s, w, b, b, b, w, s},
        {s, s, w, w, w, s, s, s}
    };
    
    // サーバー関連
    public static final int MEMBER = 1024; // 最大接続可能人数
    public static final String LEAVE_MES = "PLAYER LEFT!"; // 切断されたときに送る
}
