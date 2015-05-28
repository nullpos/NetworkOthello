package application;

import javax.swing.*;

import util.*;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.net.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.io.*;

public class Client extends JFrame implements MouseListener {
    private static final long serialVersionUID = 1L;
    private JButton[][] buttonArray = new JButton[Const.BSIZE][Const.BSIZE];//オセロ盤用のボタン配列
    private JButton menuBtn;
    private Container c; // コンテナ
    private PrintWriter out;//データ送信用オブジェクト
    private Receiver receiver; //データ受信用オブジェクト
    private Othello game; //Othelloオブジェクト
    private Player player; //Playerオブジェクト
    private Images images;
    private JPlayerDisp bdisp;
    private JPlayerDisp wdisp;
    private int[] opt = {Const.OFF, Const.OFF};
    private Computer computer = null;
    private OptWindow optwin;

    /*
     *  コンストラクタ
     */
    public Client(Othello game, Player player, Images images) { //OthelloオブジェクトとPlayerオブジェクトを引数とする
        this.game = game; //引数のOthelloオブジェクトを渡す
        this.player = player; //引数のPlayerオブジェクトを渡す
        this.images = images;
        int[][] board = game.getBoard(); //getGridメソッドにより局面情報を取得
        int row = Const.BSIZE; //getRowメソッドによりオセロ盤の縦横マスの数を取得
        ImagePanel ip= new ImagePanel();
        ip.setImage(images.getBgIcon().getImage());
        
        //ウィンドウ設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
        setTitle("Othello...");//ウィンドウのタイトル
        setSize(700, 700);//ウィンドウのサイズを設定
        setResizable(false);
        this.add(ip);
        
        c = ip;//フレームのペインを取得
        c.setLayout(null);

        //オセロ盤の生成

        for(int i=0; i<Const.BSIZE; i++) {
            for(int j=0; j<Const.BSIZE; j++) {
                if(board[i][j] == Const.BLACK){ buttonArray[i][j] = new JButton(images.getBlackIcon());}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.PBLACK){ buttonArray[i][j] = new JButton(images.getpBlackIcon());}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.WHITE){ buttonArray[i][j] = new JButton(images.getWhiteIcon());}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.PWHITE){ buttonArray[i][j] = new JButton(images.getpWhiteIcon());}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.SPACE){ buttonArray[i][j] = new JButton(images.getBoardIcon());}//盤面状態に応じたアイコンを設定
                if(board[i][j] == Const.PUTABLE){ buttonArray[i][j] = new JButton(images.getPutableIcon());}//盤面状態に応じたアイコンを設定
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
        bdisp = new JPlayerDisp("", images.getBlackIcon().getImage(), images.getArrowIcon().getImage());
        wdisp = new JPlayerDisp("", images.getWhiteIcon().getImage(), images.getArrowIcon().getImage());
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
            Integer.parseInt(msg);
            game.applyAction(msg);
            play();
        } catch (NumberFormatException e) {
            // オプション情報
            if(msg.startsWith(Const.OPTION_ID[0])) {
                msg = msg.replace(Const.OPTION_ID[0], "");
                System.out.println(msg);
                if(player.getMove().equals(Const.WHITE_STR)) {
                    opt[0] = Integer.parseInt(msg);
                }
                return;
            }
            
            // 先手後手情報
            if(msg.equals(Const.BLACK_STR)) {
                this.player.setMove(Const.BLACK_STR);
                playServer();
                return;
            } else if(msg.equals(Const.WHITE_STR)) {
                this.player.setMove(Const.WHITE_STR);
                playServer();
                return;
            }

            // 切断されたとき
            if(msg.equals(Const.LEAVE_MES)) {
                String winner = player.getMove();
                System.out.println(player.getMove() + " is winner.");
                this.updateDisp();
                if(winner.equals(Const.BLACK_STR)) {
                    this.bdisp.setTurn(true);
                    this.wdisp.setTurn(false);
                    this.wdisp.setText(msg);
                } else if(winner.equals(Const.WHITE_STR)) {
                    this.bdisp.setTurn(false);
                    this.bdisp.setText(msg);
                    this.wdisp.setTurn(true);
                }
                return;
            }
            
            // 名前
            if(msg.startsWith(Const.PLAYER_NAME_ID)) {
                msg = msg.replace(Const.PLAYER_NAME_ID, "");
                if(this.player.getMove().equals(Const.BLACK_STR)) {
                    this.bdisp.setText(player.getName());
                    this.wdisp.setText(msg);
                } else {
                    this.bdisp.setText(msg);
                    this.wdisp.setText(player.getName());
                }
                return;
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
                if(board[i][j] == Const.BLACK){ buttonArray[i][j].setIcon(images.getBlackIcon()); continue;}
                if(board[i][j] == Const.PBLACK){ buttonArray[i][j].setIcon(images.getpBlackIcon()); continue;}
                if(board[i][j] == Const.WHITE){ buttonArray[i][j].setIcon(images.getWhiteIcon()); continue;}
                if(board[i][j] == Const.PWHITE){ buttonArray[i][j].setIcon(images.getpWhiteIcon()); continue;}
                if(board[i][j] == Const.SPACE){ buttonArray[i][j].setIcon(images.getBoardIcon()); continue;}
                if(board[i][j] == Const.PUTABLE && opt[1] == Const.ON) {
                    buttonArray[i][j].setIcon(images.getPutableIcon()); continue;
                } else {
                    buttonArray[i][j].setIcon(images.getBoardIcon()); continue;
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
            if(Integer.parseInt(action) > 64) opt[0]--;
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
            wdisp.setText("Lv."+(level+1) + " Computer");
        } else {
            bdisp.setText("Lv."+(level+1) + " Computer");
            wdisp.setText(player.getName());
        }
        this.updateDisp();
        play();
    }
    
    public void playServer() {
        this.game.setMove(Const.BLACK_STR);
        this.updateDisp();
        this.sendMessage(Const.PLAYER_NAME_ID + player.getName());
        this.sendMessage(Const.OPTION_ID[0] + opt[0]);
        play();
    }
    
    public void play() {
        if(game.isGameFinished()) {
            String winner = game.whichIsWinner();
            System.out.println(winner + " is winner.");
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
            if(e.getButton() == MouseEvent.BUTTON3 && opt[0] == 1) {
                cmdint += 64;
            }
            this.acceptOperation(Integer.toString(cmdint));
        }
    }
    public void mouseEntered(MouseEvent e) {}//マウスがオブジェクトに入ったときの処理
    public void mouseExited(MouseEvent e) {}//マウスがオブジェクトから出たときの処理
    public void mousePressed(MouseEvent e) {}//マウスでオブジェクトを押したときの処理
    public void mouseReleased(MouseEvent e) {}//マウスで押していたオブジェクトを離したときの処理

    //テスト用のmain
    public static void main(String args[]){
        Images images = new Images();
        try {
            images.loadImages();
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(null, "Please check "+Const.SETTINGS_NAME+".propaties." + e, "Failed to load images.", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }
        
        //ログイン処理
        String myName = JOptionPane.showInputDialog(null,"Enter your name.","login",JOptionPane.QUESTION_MESSAGE);
        if(myName.equals("")){
            myName = "No name";//名前がないときは，"No name"とする
        }
        Player player = new Player(); //プレイヤオブジェクトの用意(ログイン)
        player.setName(myName); //名前を受付
        Othello game = new Othello(); //オセロオブジェクトを用意
        Client oclient = new Client(game, player, images); //引数としてオセロオブジェクトを渡す
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

        ResourceBundle rb = ResourceBundle.getBundle(Const.SETTINGS_NAME);
        String ip = "127.0.0.1";
        String port = "10000";
        try {
            port = rb.getString("Port");
            ip = rb.getString("Server");
        } catch (MissingResourceException e) {
            System.err.println("Server settings missing. Default settings set.");
        }
        
        addrTextField = new JLabelTextField("IP", ip);
        c.add(addrTextField);
        portTextField = new JLabelTextField("port", port);
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
        immoveCheckBox.setSelected((option[0] == Const.ON) ? true : false);
        showCheckBox.setSelected((option[1] == Const.ON) ? true : false);
    }

    public void setMode(int i) {
        if(i == 1) {
            serverRadioButton.setEnabled(false);
            addrTextField.getTextField().setEnabled(false);
            localRadioButton.setEnabled(false);
            portTextField.getTextField().setEnabled(false);
            compComboBox.setEnabled(false);
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