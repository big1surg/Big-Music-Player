
package mediaplayer;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileFilter;
import static java.nio.channels.AsynchronousFileChannel.open;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;


public class GUI extends JFrame {
        
	JPanel buttonPanel;
        JPanel libraryPanel;
	JButton pause, play, stop, add, delete, next, previous, update;
	JTable table, resultSetUpdate, table1;
	Player pmp;
        JPopupMenu pop;
        private JProgressBar jpb;
        JMenuBar menuBar;
        JMenu file;
        JMenuItem addSong, deleteSong, openSong, addSong2, deleteSong2, exit;
        JSlider volumeSlider;
        JFileChooser fc;
        DefaultTableModel dtm;
        public int rowindex;
        DBConnection db;
        
       
        //used for primary key for artist. 
        // int aId
        int x;
        
        @SuppressWarnings("empty-statement")
	public GUI(Player pitt) throws ClassNotFoundException, SQLException{
            super("PiMP");
            db = new DBConnection();
            pmp = pitt;
           // rowindex=0;
            //jpb=new JProgressBar();
            //jpb.setBorderPainted(true);
            //jpb.setBounds(25, 2, 280, 20);
            this.setSize(700, 500);
            buttonPanel = new JPanel();
            libraryPanel = new JPanel();
            libraryPanel.setLayout(new BorderLayout());
           // playlist = new JPanel();      
           //playlist.setBorder(new TitledBorder(new EtchedBorder(), "Style"));
//------start meunbar
            menuBar = new JMenuBar();
            file = new JMenu("File");
            menuBar.add(file);
            
            openSong = new JMenuItem("Open");
            openSong.addActionListener(new openButtonListener());
            exit=new JMenuItem("Exit");
            exit.addActionListener(new exitButtonListener());
         
            addSong = new JMenuItem("Add");
            addSong.addActionListener(new addButtonListener());
            
            deleteSong = new JMenuItem("Delete");
            deleteSong.addActionListener(new deleteButtonListener());
            
            
            file.add(openSong);
            file.add(addSong);
            file.add(deleteSong);
            file.add(exit);
            this.setJMenuBar(menuBar);		
//------end menubar section
//==========Buttons==============          
            previous = new JButton("<<");
            pause = new JButton("Pause");
            play = new JButton("Play");
            stop = new JButton("Stop");
            add = new JButton("+");
            next = new JButton(">>");
            delete = new JButton("-");
            buttonPanel.add(previous);
            buttonPanel.add(play);
            buttonPanel.add(pause);
            buttonPanel.add(stop);
            buttonPanel.add(next);
            buttonPanel.add(add);
            buttonPanel.add(delete);
            
            volumeSlider = new JSlider(0, 100, (int)(pmp.getVolume()*100));
            volumeSlider.addChangeListener(new VolumeListener());
            buttonPanel.add(volumeSlider);
            //adding action listeners
            play.addActionListener(new playButtonListener());
            pause.addActionListener(new pauseButtonListener());
            stop.addActionListener(new stopButtonListener());
            add.addActionListener(new addButtonListener());
            next.addActionListener(new nextButtonListener());
            previous.addActionListener(new previousButtonListener());
            delete.addActionListener(new deleteButtonListener());
            
            this.add(buttonPanel, BorderLayout.NORTH);
            
            table1 = new JTable();
            //i used this method to intitiazle the table/library
            dtm = db.openView();
            table1.setModel(dtm);
            //Playlist panel
            JPanel playList = new JPanel();
            JPanel p2 =new JPanel();
            p2.setBorder(new EtchedBorder());
            playList.setBorder(new EtchedBorder());
            playList.add(p2);
            //playlist section
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Library");
           // int x = playList.getHeight();
                //playList.setSize(new Dimension(150, 150));
           
            libraryPanel.add(table1, BorderLayout.EAST);

            libraryPanel.add(new JScrollPane(table1));
            libraryPanel.add(playList, BorderLayout.WEST);
            
            this.add(libraryPanel, BorderLayout.CENTER);
            //-------popup-----------//
            pop = new JPopupMenu();
            addSong2 = new JMenuItem("Add"); 
            deleteSong2 = new JMenuItem("Delete");
            deleteSong2.addActionListener(new deleteButtonListener());
            addSong2.addActionListener(new addButtonListener());
            pop.add(addSong2);
            pop.add(deleteSong2);
            table1.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    
                   if(e.getButton()==MouseEvent.BUTTON3){
           // JPopupMenu popup = createYourPopUp();
                    int r = table1.rowAtPoint(e.getPoint());
                    if(r>=0 && r<table1.getRowCount()){
                        table1.setRowSelectionInterval(r,r);
                    }
                    else{
                        table1.clearSelection();
                    }
                    rowindex = table1.getSelectedRow();
                    if (rowindex < 0)
                          return;
                        pop.show(table1, e.getX(), e.getY());
                    }
                }
            });
 //-------------------------------------------------------------
            //DRAG AND DROP
            connectToDnD();
            setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            x=0;
	}
        /*public void alternateViewTable(Connection con) throws SQLException {
            
        }*/
        
    public void updateTable() throws ClassNotFoundException, SQLException{
       
        table1.setModel(db.openView());
    }
  
    class playButtonListener implements ActionListener {

       @Override
       public void actionPerformed(ActionEvent e) {
               // TODO Auto-generated method stub
           System.out.println("You pressed " + e.getActionCommand());
          // rowindex=table1.getSelectedRow();
           System.out.println("Song row "+ rowindex+" was selected.");
           if(rowindex!=table1.getSelectedRow()){
              pmp.stop();
              rowindex=table1.getSelectedRow(); 
              System.out.println(" "+rowindex);
           }   
           String path = (String)table1.getValueAt(rowindex, 5); 
           pmp.play(path);
           System.out.println(" "+rowindex);
       }
   }
         
    class stopButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
            System.out.println("You pressed " + e.getActionCommand());
            pmp.stop();
        }
    }
    class pauseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            pmp.pause();  
            System.out.println("You pressed " + e.getActionCommand());
        }
    }
    //kinda done
    class addButtonListener implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent e){
            DBConnection db = null;
            MyWindow addingSong = null;
            try {
                db = new DBConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            try{
                
                System.out.println("Added song");
                addingSong = new MyWindow(); 
                addingSong.setVisible(true);

            }catch (Exception err){
                System.out.println(err);
            }
            System.out.println("You press " +e.getActionCommand());
            addingSong.dispose();
            
           try {
               //updateTable(table1);
                updateTable();
           } catch (ClassNotFoundException | SQLException ex) {
               Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
    }
    
    class nextButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            rowindex++;
            System.out.println("You pressed " + e.getActionCommand()+" row = "+rowindex);
            
            try{

                if(pmp.isPlaying()||pmp.getStatus()==1){
                    pmp.stop();
                }
                if(rowindex==table1.getRowCount()){
                    rowindex=0;
                }
                System.out.println("new row ="+rowindex);
                String path = (String)table1.getValueAt(rowindex, 5);
                pmp.play(path);
                //changes higlighted row. 
                table1.setRowSelectionInterval(rowindex,rowindex);
            }
            catch(Exception err){
                System.out.println(err);
            }
        }
    }
    class previousButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           rowindex--;
           System.out.println("You pressed " + e.getActionCommand()+" row = "+rowindex);
           try{

               if(pmp.isPlaying()||pmp.getStatus()==1){
                    pmp.stop();
               }
                if(rowindex<0){
                    rowindex=table1.getRowCount()-1;
                }
                System.out.println("new row ="+rowindex);
                String path = (String)table1.getValueAt(rowindex, 5);
                pmp.play(path);
                //changes higlighted row. 
                table1.setRowSelectionInterval(rowindex,rowindex);
            }
            catch(Exception err){
                System.out.println(err);
            }     
        }
    }

    class deleteButtonListener implements ActionListener {

        @Override 
        public void actionPerformed(ActionEvent e){
           
            String url = (String)table1.getValueAt(table1.getSelectedRow(), 5);
          
            System.out.println(url);
            try{
              
                db.deleteStatement("delete from music where FILEPATH='"+url+"'");
                
            }catch (Exception err){
                System.out.println("inside exception");
                System.out.println(err);
            }
            System.out.println("You press " +e.getActionCommand());
            try {
               updateTable();
               System.out.println("inside you press e");
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
     class exitButtonListener implements ActionListener {

		 @Override 
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
     }
    //drag and drop
    private void connectToDnD(){
        DragListener dl = new DragListener(this);
        DropTarget dropTarget = new DropTarget(this, dl);
       /* try{
            //updateTable(table1, true);
             table1.setModel(db.openView());
        }catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
   class openButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton open = new JButton();
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new java.io.File("."));
            fc.setDialogTitle("Open Song");
            if(fc.showOpenDialog(open)==JFileChooser.APPROVE_OPTION){
            
            }
            String path = fc.getSelectedFile().getAbsolutePath();
            pmp.stop();
            pmp.play(path);
        }
   }
   
   class VolumeListener implements ChangeListener {
      
        @Override
        public void stateChanged(ChangeEvent e) {
            //player change volume
            double volume = (double)volumeSlider.getValue()/100;
            pmp.setVolume(volume);
            //System.out.println(volume);
        }
   }
}
   
        

