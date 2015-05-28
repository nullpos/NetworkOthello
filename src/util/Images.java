package util;

import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

public class Images {
    private ImageIcon blackIcon, whiteIcon, boardIcon, putableIcon, arrowIcon, pBlackIcon, pWhiteIcon, bgIcon; //アイコン
    
    public Images() {}
    
    public void loadImages() throws MissingResourceException, IOException {
        ResourceBundle rb = ResourceBundle.getBundle(Const.SETTINGS_NAME);
        
        blackIcon = new ImageIcon(getPath(rb.getString("Black")));
        pBlackIcon = new ImageIcon(getPath(rb.getString("PBlack")));
        whiteIcon = new ImageIcon(getPath(rb.getString("White")));
        pWhiteIcon = new ImageIcon(getPath(rb.getString("PWhite")));
        boardIcon = new ImageIcon(getPath(rb.getString("Board")));
        putableIcon = new ImageIcon(getPath(rb.getString("Putable")));
        arrowIcon = new ImageIcon(getPath(rb.getString("Arrow")));
        bgIcon = new ImageIcon(getPath(rb.getString("Background")));
    }
    
    public String getPath(String path) throws IOException {
        return new File(path).getCanonicalPath().replace("\\", "/");
    }

    public ImageIcon getBlackIcon() {
        return blackIcon;
    }

    public ImageIcon getWhiteIcon() {
        return whiteIcon;
    }

    public ImageIcon getBoardIcon() {
        return boardIcon;
    }

    public ImageIcon getPutableIcon() {
        return putableIcon;
    }

    public ImageIcon getArrowIcon() {
        return arrowIcon;
    }

    public ImageIcon getpBlackIcon() {
        return pBlackIcon;
    }

    public ImageIcon getpWhiteIcon() {
        return pWhiteIcon;
    }

    public ImageIcon getBgIcon() {
        return bgIcon;
    }
}