package util;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPlayerDisp extends JPanel {
    private static final long serialVersionUID = 0L;
    private OCanvas canvas;
    private JLabel nameLabel;
    private JLabel scoreLabel;
    
    public JPlayerDisp(String name, Image img, Image arrowImg) {
        int tate = 45;
        this.canvas = new OCanvas(img, arrowImg);
        this.canvas.setSize(120, tate);
        this.nameLabel = new JLabel(name);
        this.nameLabel.setSize(300, tate);
        this.nameLabel.setFont(new Font("メイリオ", Font.PLAIN, 30));
        this.scoreLabel = new JLabel();
        this.scoreLabel.setSize(100, tate);
        this.scoreLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        
        this.setLayout(new BorderLayout());
        
        this.add(canvas, BorderLayout.WEST);
        this.add(nameLabel, BorderLayout.CENTER);
        this.add(scoreLabel, BorderLayout.EAST);
    }
    
    public void setTurn(boolean b) {
        this.canvas.setTurn(b);
    }
    
    public void setText(String text) {
        this.nameLabel.setText(text);
    }
    
    public void update(int score) {
        scoreLabel.setText(Integer.toString(score) + " points.");
        canvas.repaint();
    }
}

class OCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    private boolean turn;
    private Image img;
    private Image arrowImg;

    public OCanvas(Image img, Image arrowImg) {
        this.img = img;
        this.arrowImg = arrowImg;
        turn = true;
    }

    public void setTurn(boolean b) {
        this.turn = b;
    }
    
    public void paint(Graphics g) {
        g.drawImage(img, 40, 0, this);
        if(turn) {
            g.drawImage(arrowImg, 0, 0, this);
        }
    }
}