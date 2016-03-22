package mediaplayer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DBConnection {
    //database for music player
    
    Connection conn;
    Statement stmt;
    ResultSet rs;
    int y;
    PreparedStatement ps;
    
    public DBConnection ()throws ClassNotFoundException, SQLException {
       try{
           Class.forName("org.apache.derby.jdbc.ClientDriver");
       }
       catch(ClassNotFoundException e){
           System.out.println("Class not found "+ e);
       }
       //System.out.println("JDBC class found");
       int no_of_rows = 0;
       try{
           conn = DriverManager.getConnection("jdbc:derby://localhost:1527/musicLibrary", "chris", "chris");
           stmt = conn.createStatement();
           //rs = stmt.executeQuery("SELECT * FROM artists");
           //while(rs.next()){
           //    no_of_rows++;
           //}
           //System.out.println("Rows: "+no_of_rows);   
       }
       catch(SQLException e){
           System.out.println("SQL Exception " + e);
       }
    }
    public int getCurrentRow() throws SQLException{
        return y;
    }
    
    public int getID(String sql) throws SQLException{
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        int x=0;
        if(rs.next()){
            x = rs.getInt("id");
        }
        return x;
    }
    public int getRowNumber()throws SQLException{
        int no_rows =0;
        rs = stmt.executeQuery("SELECT artist FROM music");
        while(rs.next()){
            no_rows++;
        }
        return no_rows;
            
    }
   
    public void insertStatement(String sql) throws SQLException{
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        
    }
    
    public void deleteStatement(String sql) throws SQLException{
        /*
        deleteStatement
        Connection conn;
        Statement stmt;
        ResultSet rs;
        */   
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }
    
    public String getFilepath(String sql) throws SQLException{
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        String x = "";
        if(rs.next()){
            x = rs.getString("filepath");
        }
        return x;
    }
    
    public String getSong(String sql) throws SQLException{
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        String x = "";
        if(rs.next()){
            x = rs.getString("track");
        }
        return x;
    }
    
    public String getArtist(String sql) throws SQLException{
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        String x = "";
        if(rs.next()){
            x = rs.getString("artist");
        }
        return x;
    }
    
    public String getAlbum(String sql) throws SQLException{
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        String x = "";
        if(rs.next()){
            x = rs.getString("album");
        }
        return x;
    }
    
    public void resultStatement(String sql) throws SQLException{
        stmt= conn.createStatement();
        rs = stmt.executeQuery(sql);
        //String e = rs.getString(getRowNumber());
        //return e;
        //JTable table = new JTable(buildTableModel(rs));
        //DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        //tableModel.fireTableDataChanged();
        //table = new JTable(tableModel);
        System.out.println("Table has been updated");
        //table.addMouseListener(new MouseAdapter(){
          // public void mouseClicked(MouseEvent evnt){
            //   if(evnt.getClickCount()==1){
              //     y =table.getSelectedRow();
              // }
           //} 
        //});
        //y = table.getSelectedRow();
        //y = table.getRowCount();
        //return table;
        //JOptionPane.showMessageDialog(null,new JScrollPane(table));    
    }
    
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);

    }
    public DefaultTableModel openView() throws SQLException {
        Statement stmt = null;
        String query = "Select * from music";
        JTable table = new JTable();
        String[] columnName = {"Song", "Artist", "Album", "legth", "Genre", "Path"};
        Object[][] data = new Object[getRowNumber()][6];       
        DefaultTableModel dtm = new DefaultTableModel(0,0);
        dtm.setColumnIdentifiers(columnName);
        table.setModel(dtm);
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            //int i = 0;
            while(rs.next()){
                String song = rs.getString(4);
                String artist = rs.getString(2);
                String album = rs.getString(3);
                String length = rs.getString(6);
                String genre = rs.getString(5);
                String path = rs.getString(7);
                dtm.addRow(new Object[] {song, artist, album, length, genre, path});
            }
            //return dtm;
        }
       catch(SQLException e){
            System.out.println(e);
        }
        return dtm;
    }
     public void prepareStmt(String sql, int id,String  artist, String album, String track, String genre, String m_length, String filepath) throws SQLException{
         System.out.println("Putting into database...");
         ps= conn.prepareStatement(sql);
         ps.setInt(1, id);
         ps.setString(2, artist);
         ps.setString(3, album);
         ps.setString(4, track);
         ps.setString(5, genre);
         ps.setString(6, m_length);
         ps.setString(7, filepath);
         int rows = ps.executeUpdate();
         resultStatement("Select * from music");
         
     }   
}
    

