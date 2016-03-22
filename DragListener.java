/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marvin
 */
public class DragListener implements DropTargetListener {
     private JTextField textField;
    //DefaultTableModel dtm = new DefaultTableModel();
    DefaultTableModel dtm;
    private JTable table=new JTable();
    DBConnection conn;
    GUI gui;
    
   // GUI gui = new GUI();
    public DragListener(GUI g){
        gui=g;
        try {
            conn = new DBConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
        }
       //dtm=dt;
    }
     @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        //accepts drop
       dtde.acceptDrop(DnDConstants.ACTION_COPY);
       //get drop
       Transferable t = dtde.getTransferable();
       //get data format
       DataFlavor[] df = t.getTransferDataFlavors();

       for(DataFlavor f: df){
           try{
               //check if itmes are file type
               if(f.isFlavorJavaFileListType()){
                   //get list of them
                   List<File> files = (List<File>) t.getTransferData(f);
                   for(File file: files){
                       String path=file.getPath();
                       System.out.println(path);
                       //drop=file;
                      
                       ID3v2 tag = null;
                       Mp3File song = null;
                       try {
                           song = new Mp3File(path);
                       } catch (UnsupportedTagException | InvalidDataException | IOException ex) {
                           Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                       } 
                       if(song.hasId3v2Tag()){
                           System.out.println("Song has ID3v2: ");
                           tag=song.getId3v2Tag();
                           String sql = "insert into music values(?,?,?,?,?,?,?)";
                           int id = 1;
              
                            try {
                                id=conn.getRowNumber();
                            } catch (SQLException ex) {
                                   Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String artist, album, track, genre, m_length, filepath;
                            artist = tag.getArtist();
                            album = tag.getAlbum();
                            track = tag.getTitle();
                            genre = "Other";
                            m_length = "Unknown";
                            filepath = path;
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
                                conn.prepareStmt(sql,id,artist,album,track,genre,m_length,filepath);
                            } catch (SQLException ex) {
                                System.out.println("db.prepareStmt didnt work "+ ex);
                            }
                           try {
                               updateTable();
                           } catch (ClassNotFoundException ex) {
                               Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                           } catch (SQLException ex) {
                               Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                           }
                       }
                       else{
                           
                            tag = new ID3v24Tag();
                            song.setId3v2Tag(tag);
                    
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
                                    id = conn.getRowNumber();
                                    conn.prepareStmt(sql,id,a,al,tr,gen,m,path);
                                    dtm.addRow(new Object[] { conn.getSong("Select track from music where id="+id),conn.getArtist("Select artist from music where id="+id), conn.getAlbum("Select album from music where id="+id), conn.getFilepath("Select filepath from music where id="+id) });
                                } catch (SQLException ex) {
                                    System.out.println("couldn't get id "+ ex);
                                }
                                
                                
                                try {
                                    song.save(tr+".mp3");
                                     
                                } catch (IOException ex) {
                                    Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (NotSupportedException ex) {
                                    Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    updateTable();
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        
                            }
                        }
                   }
               }
           } catch (UnsupportedFlavorException ex) {
               Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
           }
           
           /* conn.prepareStmt(sql,id,a,al,tr,gen,m,songPath);
            song.save(tr+".mp3");*/
       }
       
         try {
             gui.updateTable();
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(DragListener.class.getName()).log(Level.SEVERE, null, ex);
         }

    }
    public void updateTable() throws ClassNotFoundException, SQLException{
           
       }
   
}
