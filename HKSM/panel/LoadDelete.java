package HKSM.panel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import HKSM.app.GUI;

@SuppressWarnings("serial")
public class LoadDelete extends JPanel{
	
	public JLabel filename;
	
	/**
	 * This SavePanel creates delete and load buttons
	 * 
	 * @param gui
	 */
	public LoadDelete(GUI gui){
		
		this.setLayout(new BorderLayout());
		
		filename = new JLabel("test");
		filename.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(filename, BorderLayout.PAGE_START);
		
		JButton delete = new JButton("DELETE");
		delete.addActionListener(new Listeners.Delete(gui));
		this.add(delete, BorderLayout.LINE_END);
		
		JButton loadAll = new JButton("LOAD ALL");
		loadAll.addActionListener(new Listeners.LoadAll(gui));
		this.add(loadAll, BorderLayout.LINE_START);
	}
}