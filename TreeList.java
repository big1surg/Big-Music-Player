/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mediaplayer;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Marvin
 */
public class TreeList extends JTree {
    private JTree tree;
    
    public TreeList(){
        DefaultMutableTreeNode playList = new DefaultMutableTreeNode("Playlist");
        createNodes(playList);
        tree = new JTree(playList);
        
        JScrollPane treeView = new JScrollPane(tree);
    }
    
    private void createNode(DefaultMutableTreeNode top){
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode book = null;
        
        category = new DefaultMutableTreeNode("B")
    }
}
