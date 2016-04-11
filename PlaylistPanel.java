/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Marvin
 */
public class PlaylistPanel extends JPanel implements TreeSelectionListener {

    //private JPanel panel = new JPanel();
    private JTree tree = new JTree();
    public String playlistName;
    private JPopupMenu pop = new JPopupMenu();
    TreePath treePath;
    Startup str;
    DBConnection db;
    DefaultMutableTreeNode pl;
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
    private DefaultMutableTreeNode lib = new DefaultMutableTreeNode("Library");
    
    public PlaylistPanel(Startup s) throws ClassNotFoundException, SQLException{
        super();
        tree.setRootVisible(false);
        str = s;
        db = s.getConn();
        setBorder(new EtchedBorder());
 
        /////////\\\\\\\\//////////playlist section ///////\\\\/////////\\\\\\\\\\/////
        pl = new DefaultMutableTreeNode("Playlist");
         try {
           pl = db.getPlaylistNodes();
       } catch (SQLException ex) {
           Logger.getLogger(PlaylistPanel.class.getName()).log(Level.SEVERE, null, ex);
       }
        //use this to initialize the playlist panel
        //create a tree that allows one selection at a time
        tree = new JTree(pl);
        //create a tree that allows one selection at a time
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JMenuItem openWindow = new JMenuItem("Open in New Window");
        openWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //not sure how to implent this...\
                Player pmp = new Player();
                try {
                    GUI pView = new GUI(str);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PlaylistPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(PlaylistPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenuItem deletePlaylist = new JMenuItem("DeletePlaylist");
        deletePlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
               
            }
        });
        
        pop.add(openWindow);
        pop.add(deletePlaylist);
        tree.addTreeSelectionListener(this);
        
        add(tree);
        
    }
    
     public void updateNode(String name) throws SQLException{
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);

       pl.add(node);
        
       DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
       
       model.nodeStructureChanged(pl);

   }
   /* public JPanel getPlaylists(){
        return panel;
    }*/
    
    
    @Override
    public void valueChanged(TreeSelectionEvent t) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        tree.getLastSelectedPathComponent();
        
        if (node == null) return;
        playlistName = t.getNewLeadSelectionPath().getLastPathComponent().toString();
        
        if (node.isLeaf()) {
           
           
        }
        tree.addMouseListener(new MouseAdapter(){
            public void mouseReleased(MouseEvent e){
               //if(tree.getLastSelectedPathComponent().isLeaf()){
                treePath = tree.getPathForLocation(e.getX(), e.getY());
                if(treePath == null){
                    System.out.println("No node at this location");
                }
                else{
                    if(e.getButton()==MouseEvent.BUTTON3){
                        tree.setSelectionPath(treePath);
                        tree.scrollPathToVisible(treePath);
                        System.out.println(playlistName);
                        pop.show(tree, e.getX(), e.getY());
                       
                        
                    }
                }
            }
        });
    }
}
