package application;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

//import ServerSample1.Receiver;

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
		Receiver (Socket socket,int playerNo){
			try{
				this.playerNo = playerNo;
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr);
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		// 内部クラス Receiverのメソッド
		public void run(){
			sendColor(String.valueOf(playerNo));
			try{
				while(true) {// データを受信し続ける
					String inputLine = br.readLine();//データを一行分読み込む
					if (inputLine != null){ //データを受信したら
						forwardMessage(inputLine,playerNo); //もう一方に転送する
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
			ServerSocket ss0 = new ServerSocket(port);
			ServerSocket ss1 = new ServerSocket(port+1);//サーバソケットを用意
			while (true) {
				Socket socket0 = ss0.accept();//新規接続を受け付ける
				out[0] = new PrintWriter(socket0.getOutputStream(), true);//データ送信オブジェクトを用意
				Socket socket1 = ss1.accept();//新規接続を受け付ける
				out[1] = new PrintWriter(socket1.getOutputStream(), true);//データ送信オブジェクトを用意
				receiver[0] = new Receiver(socket0,0);//データ受信オブジェクト(スレッド)を用意
				receiver[1] = new Receiver(socket1,1);//データ受信オブジェクト(スレッド)を用意
				if(printStatus()){
					System.out.println("クライアントと接続しました．");
				receiver[0].start();
				receiver[1].start();//データ送信オブジェクト(スレッド)を起動
				}
			}
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}

	public boolean printStatus(){ //クライアント接続状態の確認
		boolean check=false;
		if(online[0]==online[1]==true){
				check=true;
				return check;
		}else{
			return check;
		}

	}

	public void sendColor(String num){ //先手後手情報(白黒)の送信
		if(num=="0"){
			out[0].write("black");//先が黒
		}else{
			out[1].write("white");//
		}
	}

	public void forwardMessage(String msg,int playerNo){ //操作情報の転送
	    if(playerNo==0){
	    	out[1].write(msg);
	    }else{
	    	out[0].write(msg);
	    }
		//左上から0~63,右クリックで+64
	}
	public void getOption(){

	}

	public static void main(String[] args){ //main
		Server server = new Server(10000); //待ち受けポート10000番でサーバオブジェクトを準備
		server.acceptClient(); //クライアント受け入れを開始
	}
}