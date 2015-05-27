package application;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import util.Const;


public class Server{
    private int port; // サーバの待ち受けポート
    private boolean[] online; //オンライン状態管理用配列
    private PrintWriter[] out; //データ送信用オブジェクト
    private Receiver[] receiver; //データ受信用オブジェクト
    private int oddeven; // 偶奇を切り替えるためのもの

    //コンストラクタ
    public Server(int port) { //待ち受けポートを引数とする
        this.port = port; //待ち受けポートを渡す
        out = new PrintWriter[Const.MEMBER]; //データ送信用オブジェクトを2クライアント分用意
        receiver = new Receiver[Const.MEMBER]; //データ受信用オブジェクトを2クライアント分用意
        online = new boolean[Const.MEMBER]; //オンライン状態管理用配列を用意
        oddeven = 0;
    }

    // データ受信用スレッド(内部クラス)
    class Receiver extends Thread {
        private InputStreamReader sisr; //受信データ用文字ストリーム
        private BufferedReader br; //文字ストリーム用のバッファ
        private int playerNo; //プレイヤを識別するための番号

        // 内部クラスReceiverのコンストラクタ
        Receiver (Socket socket[], int playerNo){
            try{
                this.playerNo = playerNo;
                sisr = new InputStreamReader(socket[playerNo].getInputStream());
                br = new BufferedReader(sisr);
            } catch (IOException e) {
                System.err.println("データ受信時にエラーが発生しました: " + e);
            }
        }
        // 内部クラス Receiverのメソッド
        public void run(){
            try{
                while(true) {// データを受信し続ける
                    String inputLine = br.readLine();//データを一行分読み込む
                    if (inputLine != null){ //データを受信したら
                        forwardMessage(inputLine, playerNo); //もう一方に転送する
                    }
                }
            } catch (IOException e){ // 接続が切れたとき
                System.err.println("プレイヤ " + playerNo + "との接続が切れました．");
                online[playerNo] = false; //プレイヤの接続状態を更新する
                if(playerNo % 2 == oddeven) {
                    try {
                        sendMessage(playerNo+1, Const.LEAVE_MES);
                    } catch (NullPointerException e2) {
                        oddeven = (oddeven == 0) ? 1 : 0;
                    }
                } else {
                    sendMessage(playerNo-1, Const.LEAVE_MES);
                }
            }
        }
    }

    // メソッド
    public void acceptClient(){ //クライアントの接続(サーバの起動)

        try {
            System.out.println("サーバが起動しました．");
            ServerSocket ss = new ServerSocket(port);//サーバソケットを用意
            int i=0;
            while (true) {
                Socket[] socket = new Socket[Const.MEMBER];
                socket[i] = ss.accept();//新規接続を受け付ける
                out[i] = new PrintWriter(socket[i].getOutputStream(), true);//データ送信オブジェクトを用意
                receiver[i] = new Receiver(socket,i);//データ受信オブジェクト(スレッド)を用意
                System.out.println("クライアント"+i+"と接続しました．");
                online[i] = true;
                receiver[i].start();
                
                if(isRoomFull(i)){
                    System.out.println("先手後手送信．");
                    sendColor(i);
                }
                i++;
            }
        } catch (Exception e) {
            System.err.println("ソケット作成時にエラーが発生しました: " + e);
        }
    }

    public boolean isRoomFull(int i){ //クライアント接続状態の確認
        if(i == 0 || i == Const.MEMBER - 1) return false;
        if(online[i-1] && online[i]){
            return true;
        } else {
            return false;
        }
    }

    public void sendColor(int i){ //先手後手情報(白黒)の送信
        sendMessage(i-1, Const.BLACK_STR);
        sendMessage(i  , Const.WHITE_STR);
    }

    public void forwardMessage(String msg, int playerNo){ //操作情報の転送
        //左上から0~63,右クリックで+64
        System.out.println("player"+ playerNo +": "+msg);
        if(playerNo % 2 == oddeven) {
            sendMessage(playerNo+1, msg);
        } else {
            sendMessage(playerNo-1, msg);
        }
    }
    
    public void sendMessage(int n, String msg) {
        out[n].println(msg);
        out[n].flush();
    }
    
    public static void main(String[] args){ //main
        Server server = new Server(10000); //待ち受けポート10000番でサーバオブジェクトを準備
        server.acceptClient(); //クライアント受け入れを開始
    }
}