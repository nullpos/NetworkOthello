package application;

import javax.swing.*;

import util.Const;
import util.JLabelTextField;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.net.*;
import java.util.regex.Pattern;
import java.io.*;

public class Client extends JFrame implements MouseListener {
    private JButton[][] buttonArray = new JButton[Const.BSIZE][Const.BSIZE];//オセロ盤用のボタン配列
    private JButton stop, pass; //停止、スキップ用ボタン
    private JLabel colorLabel; // 色表示用ラベル
    private JLabel turnLabel; // 手番表示用ラベル
    private Container c; // コンテナ
    private ImageIcon blackIcon, whiteIcon, boardIcon, putableIcon; //アイコン
    private PrintWriter out;//データ送信用オブジェクト
    private Receiver receiver; //データ受信用オブジェクト
    private Othello game; //Othelloオブジェクト
    private Player player; //Playerオブジェクト
    private int[] opt = {0, 0};
    private Computer computer;

    // コンストラクタ
    public Client(Othello game, Player player) { //OthelloオブジェクトとPlayerオブジェクトを引数とする
        this.game = game; //引数のOthelloオブジェクトを渡す
        this.player = player; //引数のPlayerオブジェクトを渡す
        int[][] board = game.getBoard(); //getGridメソッドにより局面情報を取得
        int row = Const.BSIZE; //getRowメソッドによりオセロ盤の縦横マスの数を取得

        //ウィンドウ設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
        setTitle("Othello...");//ウィンドウのタイトル
        setSize(700, 700);//ウィンドウのサイズを設定
        setResizable(false);
        c = getContentPane();//フレームのペインを取得

        //アイコン設定(画像ファイルをアイコンとして使う)
        whiteIcon = new ImageIcon("White.jpg");
        blackIcon = new ImageIcon("Black.jpg");
        boardIcon = new ImageIcon("GreenFrame.jpg");
        putableIcon = new ImageIcon("Putable.jpg");
        c.setLayout(null);//

        //オセロ盤の生成

        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.BLACK){ buttonArray[i][j] = new JButton(blackIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.WHITE){ buttonArray[i][j] = new JButton(whiteIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.SPACE){ buttonArray[i][j] = new JButton(boardIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.PUTABLE){ buttonArray[i][j] = new JButton(putableIcon);}//盤面状態に応じたアイコンを設定
                c.add(buttonArray[i][j]);//ボタンの配列をペインに貼り付け

                // ボタンを配置する
                int x = (j % row) * 45;
                int y = i * 45;
                buttonArray[i][j].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
                buttonArray[i][j].addMouseListener(this);//マウス操作を認識できるようにする
                buttonArray[i][j].setActionCommand(Integer.toString(i*Const.BSIZE + j));//ボタンを識別するための名前(番号)を付加する
            }
        }

        //終了ボタン
        stop = new JButton("終了");//終了ボタンを作成
        c.add(stop); //終了ボタンをペインに貼り付け
        stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);//終了ボタンの境界を設定
        stop.addMouseListener(this);//マウス操作を認識できるようにする
        stop.setActionCommand("stop");//ボタンを識別するための名前を付加する
        //パスボタン
        pass = new JButton("パス");//パスボタンを作成
        c.add(pass); //パスボタンをペインに貼り付け
        pass.setBounds((row * 45 + 10) / 2, row * 45 + 30, (row * 45 + 10 ) / 2, 30);//パスボタンの境界を設定
        pass.addMouseListener(this);//マウス操作を認識できるようにする
        pass.setActionCommand("pass");//ボタンを識別するための名前を付加する
        //色表示用ラベル
        String myName = player.getName();
        colorLabel = new JLabel(myName + "さんの色は未定です");//色情報を表示するためのラベルを作成
        colorLabel.setBounds(10, row * 45 + 60 , row * 45 + 10, 30);//境界を設定
        c.add(colorLabel);//色表示用ラベルをペインに貼り付け
        //手番表示用ラベル
        turnLabel = new JLabel("手番は未定です");//手番情報を表示するためのラベルを作成
        turnLabel.setBounds(10, row * 45 + 120, row * 45 + 10, 30);//境界を設定
        c.add(turnLabel);//手番情報ラベルをペインに貼り付け
    }

    // メソッド
    public void connectServer(String ipAddress, int port){  // サーバに接続
        Socket socket = null;
        try {
            socket = new Socket(ipAddress, port); //サーバ(ipAddress, port)に接続
            out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意
            receiver = new Receiver(socket); //受信用オブジェクトの準備
            receiver.start();//受信用オブジェクト(スレッド)起動
        } catch (UnknownHostException e) {
            System.err.println("ホストのIPアドレスが判定できません: " + e);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("サーバ接続時にエラーが発生しました: " + e);
            System.exit(-1);
        }
    }

    public void sendMessage(String msg){    // サーバに操作情報を送信
        out.println(msg);//送信データをバッファに書き出す
        out.flush();//送信データを送る
        System.out.println("サーバにメッセージ " + msg + " を送信しました"); //テスト標準出力
    }

    // データ受信用スレッド(内部クラス)
    class Receiver extends Thread {
        private InputStreamReader sisr; //受信データ用文字ストリーム
        private BufferedReader br; //文字ストリーム用のバッファ

        // 内部クラスReceiverのコンストラクタ
        Receiver (Socket socket){
            try{
                sisr = new InputStreamReader(socket.getInputStream()); //受信したバイトデータを文字ストリームに
                br = new BufferedReader(sisr);//文字ストリームをバッファリングする
            } catch (IOException e) {
                System.err.println("データ受信時にエラーが発生しました: " + e);
            }
        }
        // 内部クラス Receiverのメソッド
        public void run(){
            try{
                while(true) {//データを受信し続ける
                    String inputLine = br.readLine();//受信データを一行分読み込む
                    if (inputLine != null){//データを受信したら
                        receiveMessage(inputLine);//データ受信用メソッドを呼び出す
                    }
                }
            } catch (IOException e){
                System.err.println("データ受信時にエラーが発生しました: " + e);
            }
        }
    }

    public void receiveMessage(String msg){ // メッセージの受信
        System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力
    }
    public void updateDisp(){   // 画面を更新する
        int[][] board = game.getBoard();
        game.checkPutable(game.getIntMove());
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.BLACK){ buttonArray[i][j].setIcon(blackIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.WHITE){ buttonArray[i][j].setIcon(whiteIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.SPACE){ buttonArray[i][j].setIcon(boardIcon);}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.PUTABLE){ buttonArray[i][j].setIcon(putableIcon);}//盤面状態に応じたアイコンを設定
            }
        }
    }
    public void acceptOperation(String action){    // プレイヤの操作を受付
        if(game.applyAction(action)) {
            play();
        }
    }
    
    public void setOption(int[] opt) {
        this.opt = opt;
    }
    
    public void playLocal(int level) {
        this.computer = new Computer(level);
        this.player.setMove(Const.BLACK_STR); // TODO
        this.game.setMove(Const.BLACK_STR);
        this.updateDisp();
        play();
    }
    
    public void play() {
        if(game.isGameFinished()) {
            System.out.println(game.whichIsWinner() + " is winner.");
        } else {
            // computer's turn TODO Server
            if (!game.getMove().equals(player.getMove())) {
                game.applyAction(computer.getNextAction(game));
            }
            // パスさせる
            if(game.checkPutable(game.getIntMove()) == 0) {
                game.applyAction(Const.PASS_STR);
                play();
            }
        }
        
        this.updateDisp();
    }

    //マウスクリック時の処理
    public void mouseClicked(MouseEvent e) {
        JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．キャストを忘れずに
        String command = theButton.getActionCommand();//ボタンの名前を取り出す
        if(command.equals("Menu")) {
            // TODO
        } else {
            int cmdint = Integer.parseInt(command);
            cmdint = (e.getButton() == MouseEvent.BUTTON1) ? cmdint : cmdint + 64 ;
            //sendMessage(command); //テスト用にメッセージを送信
            this.acceptOperation(Integer.toString(cmdint));
        }
    }
    public void mouseEntered(MouseEvent e) {}//マウスがオブジェクトに入ったときの処理
    public void mouseExited(MouseEvent e) {}//マウスがオブジェクトから出たときの処理
    public void mousePressed(MouseEvent e) {}//マウスでオブジェクトを押したときの処理
    public void mouseReleased(MouseEvent e) {}//マウスで押していたオブジェクトを離したときの処理

    //テスト用のmain
    public static void main(String args[]){
        // TODO デザインの処理
        //ログイン処理
        String myName = JOptionPane.showInputDialog(null,"Enter your name.","login",JOptionPane.QUESTION_MESSAGE);
        if(myName.equals("")){
            myName = "No name";//名前がないときは，"No name"とする
        }
        Player player = new Player(); //プレイヤオブジェクトの用意(ログイン)
        player.setName(myName); //名前を受付
        Othello game = new Othello(); //オセロオブジェクトを用意
        Client oclient = new Client(game, player); //引数としてオセロオブジェクトを渡す
        oclient.setVisible(true);
        
        OptWindow optwin = new OptWindow(oclient, ModalityType.APPLICATION_MODAL);
        optwin.setVisible(true);
        oclient.setOption(optwin.getOption());
        
        if(optwin.isLocalSelected()) {
            //local
            oclient.playLocal(optwin.getComputerLevel());
        } else {
            //server
            String ip = optwin.getServerAddress();
            int port = Integer.parseInt(optwin.getServerPort());
            oclient.connectServer(ip, port);
        }
    }
}

class OptWindow extends JDialog implements MouseListener {
    private JFrame mainFrame;
    private Container c; // コンテナ
    private JRadioButton serverRadioButton;
    private JLabelTextField addrTextField;
    private JLabelTextField portTextField;
    private JRadioButton localRadioButton;
    private JComboBox<String> compComboBox;
    private JCheckBox immoveCheckBox;
    private JCheckBox showCheckBox;
    private JButton okButton;
    private ButtonGroup bGroup;
    
    public OptWindow(JFrame mainFrame, ModalityType mt) {
        super(mainFrame, mt);
        this.mainFrame = mainFrame;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//ウィンドウを閉じる場合の処理
        setTitle("Game Settings");//ウィンドウのタイトル
        setSize(400, 300);
        setResizable(false);
        c = getContentPane();//フレームのペインを取得
        c.setLayout(new GridLayout(9, 1));//
        
        
        bGroup = new ButtonGroup();
        //server
        serverRadioButton = new JRadioButton("Server");
        bGroup.add(serverRadioButton);
        c.add(serverRadioButton);

        addrTextField = new JLabelTextField("IP", "127.0.0.1");
        c.add(addrTextField);
        portTextField = new JLabelTextField("port", "10000");
        c.add(portTextField);
        
        //local
        localRadioButton = new JRadioButton("Local", true);
        bGroup.add(localRadioButton);
        c.add(localRadioButton);
        
        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
        compComboBox = new JComboBox<String>(levels);
        c.add(compComboBox);
        
        //opt
        immoveCheckBox = new JCheckBox("不動の石を用いて対戦を行う");
        c.add(immoveCheckBox);
        showCheckBox = new JCheckBox("石を置ける場所を表示する");
        c.add(showCheckBox);
        
        okButton = new JButton("OK");
        c.add(okButton);
        okButton.addMouseListener(this);
    }

    public String getServerAddress() {
        return this.addrTextField.getText().toLowerCase();
    }
    
    public String getServerPort() {
        return this.portTextField.getText().toLowerCase();
    }
    
    public int getComputerLevel() {
        String sel = (String)this.compComboBox.getSelectedItem();
        sel = sel.replace("Level ", "");
        return Integer.parseInt(sel) - 1;
    }
    
    public int[] getOption() {
        int[] opt = {Const.OFF, Const.OFF};
        if (this.immoveCheckBox.isSelected()) {
            opt[0] = Const.ON;
        }
        if (this.showCheckBox.isSelected()) {
            opt[1] = Const.ON;
        }
        return opt;
    }
    
    public boolean isLocalSelected() {
        if(this.bGroup.getSelection().equals(this.localRadioButton.getModel())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == this.okButton) {
            if(this.bGroup.getSelection().equals(this.serverRadioButton.getModel())) {
                //server
                Pattern pi = Pattern.compile("^[0-9０-９]{1,3}.[0-9０-９]{1,3}.[0-9０-９]{1,3}.[0-9０-９]{1,3}$");
                Pattern pp = Pattern.compile("^[0-9０-９]{1,5}$");
                String addr = this.addrTextField.getText();
                String port = this.portTextField.getText();
                
                if(!pi.matcher(addr).find()) {
                    this.addrTextField.setText("invalid ip number");
                } else if(!pp.matcher(port).find()) {
                    this.portTextField.setText("invalid port number");
                } else {
                    this.setVisible(false);
                }
            } else {
                //local
                this.setVisible(false);
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}