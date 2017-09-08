package HKSM;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Display a file system in a JTree view
 * 
 * @version $Id: FileTree.java,v 1.9 2004/02/23 03:39:22 ian Exp $
 * @author Ian Darwin
 */
@SuppressWarnings("serial")
public class FileTree extends JPanel {
	
	public GUI gui;
	
	public DefaultMutableTreeNode lastClicked;
	public DefaultTreeModel model;
	
  /** Construct a FileTree from the given File */
  public FileTree(File dir) {
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
    JTree tree = new JTree(addNodes(null, dir));
    model = (DefaultTreeModel) tree.getModel();
    // Add a listener
    
    tree.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                       tree.getLastSelectedPathComponent();
                if (node == null) return;
//                Object nodeInfo = node.getUserObject(); // unused
                // Cast nodeInfo to your object and do whatever you want
                System.out.println("You double clicked " + node);
                
                String path = gui.savePath;
				int c = gui.ft.lastClicked.getPath().length;
				for( int i = 1; i < c; i++ ){
					path += "/" + gui.ft.lastClicked.getPath()[i].toString();
				}
                
				// This 
				@SuppressWarnings("unused")
                JFrame SaveEditor = new SaveEditor(path, node.toString());
            }
        }
    });
    
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
            .getPath().getLastPathComponent();
        lastClicked = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        System.out.println("You selected " + node);
      }
    });

    // Lastly, put the JTree into a JScrollPane.
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollpane);
  }

  /** Add nodes from under "dir" into curTop. Highly recursive. */
  DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
    String curPath = dir.getPath();
    //DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(dir.getName());
    if (curTop != null) { // should only be null at root
      curTop.add(curDir);
    }
    Vector<String> ol = new Vector<String>();
    //String[] tmp = dir.list();
    File[] tmp = dir.listFiles();
    for (int i = 0; i < tmp.length; i++){
    	ol.addElement(tmp[i].getName());
    }
    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
    File f;
    Vector<String> files = new Vector<String>();
    // Make two passes, one for Dirs and one for Files. This is #1.
    for (int i = 0; i < ol.size(); i++) {
      String thisObject = (String) ol.elementAt(i);
      String newPath;
      if (curPath.equals("."))
        newPath = thisObject;
      else
        newPath = curPath + File.separator + thisObject;
      if ((f = new File(newPath)).isDirectory()){
    		  addNodes(curDir, f);
      }
      else
    	  if(thisObject.toLowerCase().endsWith("dat"))
    		  files.addElement(thisObject);
    }
    // Pass two: for files.
    for (int fnum = 0; fnum < files.size(); fnum++)
      curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
    return curDir;
  }

  public Dimension getMinimumSize() {
    return new Dimension(200, 400);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 400);
  }
}