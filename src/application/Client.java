package application;

import javax.swing.*;

import util.Const;
import util.ImagePanel;

import util.JLabelTextField;
import util.JPlayerDisp;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.net.*;
import java.util.regex.Pattern;
import java.io.*;

public class Client extends JFrame implements MouseListener {
    private static final long serialVersionUID = 1L;
    private JButton[][] buttonArray = new JButton[Const.BSIZE][Const.BSIZE];//オセロ盤用のボタン配列
    private JButton menuBtn;
    private Container c; // コンテナ
    private ImageIcon blackIcon, whiteIcon, boardIcon, putableIcon, arrowIcon; //アイコン
    private PrintWriter out;//データ送信用オブジェクト
    private Receiver receiver; //データ受信用オブジェクト
    private Othello game; //Othelloオブジェクト
    private Player player; //Playerオブジェクト
    private JPlayerDisp bdisp;
    private JPlayerDisp wdisp;
    private int[] opt = {Const.OFF, Const.OFF};
    private Computer computer = null;
    private OptWindow optwin;

    /*
     *  コンストラクタ
     */
    public Client(Othello game, Player player) { //OthelloオブジェクトとPlayerオブジェクトを引数とする
        this.game = game; //引数のOthelloオブジェクトを渡す
        this.player = player; //引数のPlayerオブジェクトを渡す
        int[][] board = game.getBoard(); //getGridメソッドにより局面情報を取得
        int row = Const.BSIZE; //getRowメソッドによりオセロ盤の縦横マスの数を取得
        Image bgimg = (new ImageIcon("bg.jpg")).getImage();
        ImagePanel ip= new ImagePanel();
        ip.setImage(bgimg);
        
        //ウィンドウ設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
        setTitle("Othello...");//ウィンドウのタイトル
        setSize(700, 700);//ウィンドウのサイズを設定
        setResizable(false);
        this.add(ip);
        
        c = ip;//フレームのペインを取得
        
        //アイコン設定(画像ファイルをアイコンとして使う)
        whiteIcon = new ImageIcon("White.jpg");
        blackIcon = new ImageIcon("Black.jpg");
        boardIcon = new ImageIcon("GreenFrame.jpg");
        putableIcon = new ImageIcon("Putable.jpg");
        arrowIcon = new ImageIcon("Arrow.png");
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
                int x = (j % row) * 45 + 170;
                int y = i * 45 + 100;
                buttonArray[i][j].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
                buttonArray[i][j].addMouseListener(this);//マウス操作を認識できるようにする
                buttonArray[i][j].setActionCommand(Integer.toString(i*Const.BSIZE + j));//ボタンを識別するための名前(番号)を付加する
            }
        }

        // メニューボタン
        menuBtn = new JButton("Menu");
        menuBtn.setActionCommand("Menu");
        menuBtn.setContentAreaFilled(false);
        menuBtn.setBounds(20, 20, 140, 70);
        menuBtn.addMouseListener(this);
        c.add(menuBtn);
        
        
        // 手番情報などのあたり
        bdisp = new JPlayerDisp("", blackIcon.getImage(), arrowIcon.getImage());
        wdisp = new JPlayerDisp("", whiteIcon.getImage(), arrowIcon.getImage());
        bdisp.setBounds(100, 500, 500, 40);
        wdisp.setBounds(100, 575, 500, 40);
        bdisp.update(0);
        wdisp.update(0);
        c.add(bdisp);
        c.add(wdisp);
    }

    /*
     *  サーバー関連のメソッド
     */
    public void connectServer(String ipAddress, int port){  // サーバに接続
        Socket socket = null;
        try {
            socket = new Socket(ipAddress, port); //サーバ(ipAddress, port)に接続
            out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意
            receiver = new Receiver(socket); //受信用オブジェクトの準備
            receiver.start();//受信用オブジェクト(スレッド)起動
            System.out.println("connect success");
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
        System.out.println("Send: " + msg); //テスト標準出力
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
        System.out.println("Receive: " + msg); //テスト用標準出力
        
        // 操作情報
        try {
            int action = Integer.parseInt(msg);
            game.applyAction(msg);
            play();
        } catch (NumberFormatException e) {
            // オプション情報
            
            
            // 先手後手情報
            if(msg.equals(Const.BLACK_STR)) {
                this.player.setMove(Const.BLACK_STR);
                playServer();
                return;
            } else if(msg.equals(Const.WHITE_STR)) {
                this.player.setMove(Const.WHITE_STR);
                playServer();
                return;
            } else {
                if(this.player.getMove().equals(Const.BLACK_STR)) {
                    this.bdisp.setText(player.getName());
                    this.wdisp.setText(msg);
                } else {
                    this.bdisp.setText(msg);
                    this.wdisp.setText(player.getName());
                }
            }
        }
    }
    
    /*
     *  クライアント関連のメソッド
     */
    public void updateDisp(){   // 画面を更新する
        int[][] board = game.getBoard();
        game.checkPutable(game.getIntMove());
        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.BLACK){ buttonArray[i][j].setIcon(blackIcon); continue;}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.WHITE){ buttonArray[i][j].setIcon(whiteIcon); continue;}
                if(board[i][j] == Const.SPACE){ buttonArray[i][j].setIcon(boardIcon); continue;}
                if(board[i][j] == Const.PUTABLE && opt[1] == Const.ON) {
                    buttonArray[i][j].setIcon(putableIcon); continue;
                } else {
                    buttonArray[i][j].setIcon(boardIcon); continue;
                }
            }
        }

        this.bdisp.update(game.getScore(Const.BLACK));
        this.wdisp.update(game.getScore(Const.WHITE));
        this.bdisp.setTurn((game.getIntMove() == Const.BLACK));
        this.wdisp.setTurn((game.getIntMove() == Const.WHITE));
    }
    public void acceptOperation(String action){    // プレイヤの操作を受付
        if(!game.getMove().equals(player.getMove())) {
            System.out.println("not your turn");
            return;
        } else if(game.applyAction(action)) {
            if(computer == null ) sendMessage(action);
            play();
        }
    }

    public void setOption(int[] opt) {
        for(int i=0; i<opt.length; i++) {
            this.opt[i] = opt[i];
        }
    }

    public int[] getOption() {
        return opt;
    }
    
    public void playLocal(int level) {
        this.computer = new Computer(level);
        this.player.setMove(Const.BLACK_STR); // TODO
        this.game.setMove(Const.BLACK_STR);
        
        if(this.player.getMove().equals(Const.BLACK_STR)) {
            bdisp.setText(player.getName());
            wdisp.setText("Computer Level "+(level+1));
        } else {
            bdisp.setText("Computer Level "+(level+1));
            wdisp.setText(player.getName());
        }
        this.updateDisp();
        play();
    }
    
    public void playServer() {
        this.game.setMove(Const.BLACK_STR);
        this.updateDisp();
        this.sendMessage(player.getName());
        play();
    }
    
    public void play() {
        if(game.isGameFinished()) {
            String winner = game.whichIsWinner();
            System.out.println(game.whichIsWinner() + " is winner.");
            this.updateDisp();
            if(winner.equals(Const.BLACK_STR)) {
                this.bdisp.setTurn(true);
                this.wdisp.setTurn(false);
            } else if(winner.equals(Const.WHITE_STR)) {
                this.bdisp.setTurn(false);
                this.wdisp.setTurn(true);
            } else {
                this.bdisp.setTurn(false);
                this.wdisp.setTurn(false);
            }
            return;
        } else {
            if (!game.getMove().equals(player.getMove())) {
                if(!(computer == null)) {
                    game.applyAction(computer.getNextAction(game));
                }
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
            if(optwin == null) {
                optwin = new OptWindow(this, ModalityType.MODELESS);
                optwin.setMode(1);
                optwin.setOption(this.getOption());
            }
            optwin.setVisible(true);
        } else {
            int cmdint = Integer.parseInt(command);
            cmdint = (e.getButton() == MouseEvent.BUTTON1) ? cmdint : cmdint + 64 ;
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
    private static final long serialVersionUID = 1L;
    private Client client;
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
    
    public OptWindow(Client client, ModalityType mt) {
        super(client, mt);
        this.client = client;
        
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
        immoveCheckBox.setSelected(true);
        c.add(immoveCheckBox);
        showCheckBox = new JCheckBox("石を置ける場所を表示する");
        showCheckBox.setSelected(true);
        c.add(showCheckBox);
        showCheckBox.addMouseListener(this);
        
        okButton = new JButton("OK");
        c.add(okButton);
        okButton.addMouseListener(this);
    }

    public void setOption(int[] option) {
        if(option[0] == Const.ON) immoveCheckBox.setSelected(true);
        if(option[1] == Const.ON) showCheckBox.setSelected(true);
        
    }

    public void setMode(int i) {
        if(i == 1) {
            serverRadioButton.setEnabled(false);
            localRadioButton.setEnabled(false);
            immoveCheckBox.setEnabled(false);
        }
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
        } else if(e.getSource() == this.showCheckBox) {
            this.client.setOption(this.getOption());
            if(!serverRadioButton.isEnabled()) {
                this.client.updateDisp();
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