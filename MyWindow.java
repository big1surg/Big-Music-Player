package mediaplayer;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
 
 
public class MyWindow extends JFrame {
    
    private JTextField textField;
    private JFileChooser fc;
  //  private DefaultTableModel dtm;

    public MyWindow() throws SQLException, ClassNotFoundException, IOException, NotSupportedException {
        DBConnection db = new DBConnection();
        
        addSong(db);
    }

    private void addSong(DBConnection db) throws SQLException, IOException, NotSupportedException {   
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //fc.add
        if (fc == null) {
            fc = new JFileChooser(".");
        }
        
        // Show it.
        FileFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try{
                String s = fc.getSelectedFile().getPath();
                System.out.print(s);
            }catch(NullPointerException e){
                System.out.println("Doesnt work " + e);
            }
            String songPath ="";
            if(fc.getSelectedFile().equals("")){
                System.out.println("Nothing selected");
            }else{
                Mp3File song1 = null;
                ID3v2 tag;
                songPath = fc.getSelectedFile().getPath();
                try {
                    song1 = new Mp3File(songPath);
                    //System.out.println("\nTEXT FIELD HERREE!!!"+ songPath);
                } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                    Logger.getLogger(MyWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (song1.hasId3v2Tag()) {
                    System.out.println("Song has ID3v2 tag:");
                    tag = song1.getId3v2Tag();
                    
                    System.out.println("traxk "+tag.getTitle()+"\nArtist "+tag.getArtist());
                    String sql = "insert into music values(?,?,?,?,?,?,?)";
                        int id=1;
                        try {
                            id = db.getRowNumber();
                        } catch (SQLException ex) {
                            System.out.println("couldn't get id "+ ex);
                        }
                    String artist, album, track, genre, m_length, filepath;
                    artist = tag.getArtist();
                    album = tag.getAlbum();
                    track = tag.getTitle();
                    genre = "Other";
                    m_length = "Unknown";
                    filepath = songPath;
                    
                    if(artist == null || album == null || track == null){
                        JTextField art = new JTextField(10);
                        JTextField alb= new JTextField(10);
                        JTextField tra = new JTextField(10);
                        JPanel myPanel = new JPanel(new BorderLayout(3,3));
                        myPanel.setBorder(new EmptyBorder(5,5,5,5));

                        JPanel labels = new JPanel(new GridLayout(0,1));
                        JPanel controls = new JPanel(new GridLayout(0,1));
                        myPanel.add(labels, BorderLayout.WEST);
                        myPanel.add(controls,BorderLayout.CENTER);

                        labels.add(new JLabel("Song Name:"));
                        controls.add(tra);
                        labels.add(new JLabel("Album:"));
                        controls.add(alb);
                        labels.add(new JLabel("Arist:"));
                        controls.add(art);
                        
                        int result = JOptionPane.showConfirmDialog(null, myPanel, "Add Song Info", JOptionPane.OK_CANCEL_OPTION);
                        if(result == JOptionPane.OK_OPTION){
                            //tag.setTrack("Unknown");
                            tag.setArtist(art.getText());
                            tag.setTitle(tra.getText());
                            tag.setAlbum(alb.getText());
                            
                            artist = tag.getArtist();
                            track = tag.getTitle();
                            album = tag.getAlbum();
                            
                        }
                    }
                    
                    System.out.println("Song: "+tag.getTitle()+"\nArtist "+tag.getArtist());
                    try {
                        db.prepareStmt(sql,id,artist,album,track,genre,m_length,filepath);
                    } catch (SQLException ex) {
                        System.out.println("db.prepareStmt didnt work "+ ex);
                    }
                    
               
                }else{
                    tag = new ID3v24Tag();
                    song1.setId3v2Tag(tag);
                    
                    //------------------------------------- DIALOG BOX
                    
                    System.out.println("Song doesnt have ID3v2 tags");
                    
                    JTextField artist = new JTextField(10);
                        JTextField album = new JTextField(10);
                        JTextField track = new JTextField(10);
                        JTextField genre = new JTextField(10);
                       // JTextField year = new JTextField(10);
                         //JTextField m_length = new JTextField(10);

                    JPanel myPanel = new JPanel(new BorderLayout(3,3));
                    myPanel.setBorder(new EmptyBorder(5,5,5,5));

                    JPanel labels = new JPanel(new GridLayout(0,1));
                    JPanel controls = new JPanel(new GridLayout(0,1));
                    myPanel.add(labels, BorderLayout.WEST);
                    myPanel.add(controls,BorderLayout.CENTER);

                    labels.add(new JLabel("Song Name:"));
                    controls.add(track);
                    labels.add(new JLabel("Album:"));
                    controls.add(album);
                    labels.add(new JLabel("Arist:"));
                    controls.add(artist);
                    //labels.add(new JLabel("Year:"));
                    //controls.add(year);
                    
                    //-------------------------------------------------------------------------------
                    //---------------SETTING SONG FROM THE NEW INFO
                  
                    int result = JOptionPane.showConfirmDialog(null, myPanel, "Add Song Info", JOptionPane.OK_CANCEL_OPTION);
                    if(result == JOptionPane.OK_OPTION){
                        tag.setTrack("Unknown");
                        tag.setArtist(artist.getText());
                        tag.setTitle(track.getText());
                        tag.setAlbum(album.getText());
                        tag.setYear("1992");
                        tag.setGenre(1);
                        tag.setComment("No Comments");
                            
                        String m = tag.getComment();
                        String a = tag.getArtist();
                        String al = tag.getAlbum();
                        String tr = tag.getTitle();
                        String gen = "Other";
                            //tag = song1.getId3v1Tag();
                        String sql = "insert into music values(?,?,?,?,?,?,?)";
                        int id=1;
                        try {
                            id = db.getRowNumber();
                        } catch (SQLException ex) {
                            System.out.println("couldn't get id "+ ex);
                        }
                       
                         db.prepareStmt(sql,id,a,al,tr,gen,m,songPath);
                         song1.save(tr+".mp3");
                        
                }
                   
                    }//end else if
                    
              
                }//end else
                    fc.setSelectedFile(null);
            }else if (returnVal == JFileChooser.CANCEL_OPTION){
                        System.out.println("Closed select chooser");
                        //textField.setText("");
            }  //end else
 
        }//end if
}
