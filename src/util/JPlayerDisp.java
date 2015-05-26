package util;

import java.awt.Canvas;
import java.awt.FlowLayout;
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
        this.canvas = new OCanvas(img, arrowImg);
        this.nameLabel = new JLabel(name);
        this.nameLabel.setSize(300, 45);
        this.nameLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        this.scoreLabel = new JLabel();
        this.scoreLabel.setSize(120, 45);
        this.scoreLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        
        this.setSize(500, 45);
        this.setLayout(new FlowLayout());
        
        this.add(canvas);
        this.add(nameLabel);
        this.add(scoreLabel);
    }
    
    public void setTurn(boolean b) {
        this.canvas.setTurn(b);
    }
    
    public void update(int score) {
        scoreLabel.setText(Integer.toString(score));
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
        this.setSize(80, 45);
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