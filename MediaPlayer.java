/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Marvin
 */
public class MediaPlayer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, UnsupportedTagException, InvalidDataException, NotSupportedException{
        // TODO code application logic here'
        Player  test = new Player();
	GUI lately = new GUI(test);
    }
    
}
