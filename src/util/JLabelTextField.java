package util;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JLabelTextField extends JPanel {
    private static final long serialVersionUID = 1L;
    JLabel lb;
    JTextField tf;
    public JLabelTextField(String lbstr,String tfstr) { // constructor1
        jlbtxfld(lbstr,tfstr);
    }// constructor1
    public JLabelTextField(String lbstr) { // constructor2
        jlbtxfld(lbstr,null);
    }// constructor2

    void jlbtxfld(String lbstr,String tfstr) {
        this.setLayout(new BorderLayout());
        lb=new JLabel(lbstr);
        this.add(lb,BorderLayout.WEST);
        tf=new JTextField(tfstr);
        this.add(tf,BorderLayout.CENTER);
    }

    public String getText(){ return tf.getText();}
    public void setText(String txt){tf.setText(txt);}
    public String getlbText(){ return lb.getText();}
    public void setlbText(String txt){lb.setText(txt);}
}