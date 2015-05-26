package application;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;


public class Server{
    private int port; // サーバの待ち受けポート
    private boolean [] online; //オンライン状態管理用配列
    private PrintWriter [] out; //データ送信用オブジェクト
    private Receiver [] receiver; //データ受信用オブジェクト

    //コンストラクタ
    public Server(int port) { //待ち受けポートを引数とする
        this.port = port; //待ち受けポートを渡す
        out = new PrintWriter [2]; //データ送信用オブジェクトを2クライアント分用意
        receiver = new Receiver [2]; //データ受信用オブジェクトを2クライアント分用意
        online = new boolean[2]; //オンライン状態管理用配列を用意
    }

    // データ受信用スレッド(内部クラス)
    class Receiver extends Thread {
        private InputStreamReader sisr; //受信データ用文字ストリーム
        private BufferedReader br; //文字ストリーム用のバッファ
        private int playerNo; //プレイヤを識別するための番号

        // 内部クラスReceiverのコンストラクタ
        Receiver (Socket socket[],int playerNo){
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
                printStatus(); //接続状態を出力する
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

                Socket[] socket=new Socket[2];
                socket[i] = ss.accept();//新規接続を受け付ける
                out[i] = new PrintWriter(socket[i].getOutputStream(), true);//データ送信オブジェクトを用意
                receiver[i] = new Receiver(socket,i);//データ受信オブジェクト(スレッド)を用意
                System.out.println("クライアント"+i+"と接続しました．");
                online[i]=true;
                receiver[i].start();
                if(printStatus()){
                    System.out.println("先手後手送信．");
                    sendColor();
                }
                i++;

            }
        } catch (Exception e) {
            System.err.println("ソケット作成時にエラーが発生しました: " + e);
        }
    }

    public boolean printStatus(){ //クライアント接続状態の確認
        if(online[0] && online[1]){
            return true;
        } else {
            return false;
        }

    }

    public void sendColor(){ //先手後手情報(白黒)の送信
        out[0].println("BLACK");//先が黒
        out[0].flush();
        out[1].println("WHITE");//後が白
        out[1].flush();
    }

    public void forwardMessage(String msg,int playerNo){ //操作情報の転送
        //左上から0~63,右クリックで+64
        if(playerNo == 0) {
            System.out.println("0"+msg);
            out[1].println(msg);
            out[1].flush();
        } else {
            System.out.println("1"+msg);
            out[0].println(msg);
            out[0].flush();
        }
    
    }
    public void getOption(){

    }

    public static void main(String[] args){ //main
        Server server = new Server(10000); //待ち受けポート10000番でサーバオブジェクトを準備
        server.acceptClient(); //クライアント受け入れを開始
    }
}