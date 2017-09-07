package HKSM;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class GUI {
	
	public String savePath;
	
	public boolean backup;
	
	private Properties props;
	
	private JFrame frame;
	
	private SavePanel[] loadedSaves;
	private JPanel saveContainer;
	private JPanel directoryExplorer;
	
	final JFileChooser fc = new JFileChooser();
	
	public FileTree ft;
	
	public JTextField dirOrSaveName;
	
	public void refreshFileTree(String path){
        directoryExplorer.remove(ft);
        ft = new FileTree(new File(path));
        
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		
        directoryExplorer.add(ft, c);
        
        ft.gui = this;
		
		directoryExplorer.revalidate();
		directoryExplorer.repaint();
	}
	
	private JFrame createFrame(String name){
		JFrame ret = new JFrame(name);
		
		ret.getContentPane().setPreferredSize(new Dimension(0,0));
		ret.setMinimumSize(new Dimension(815, 500));
		ret.setMaximumSize(new Dimension(1920,1080));
		ret.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		return ret;		
	};
	
	private SavePanel[] createSavePanels(int count){
		SavePanel[] ret = new SavePanel[count];
		for( int i = 0; i < count-1; i++){
			ret[i] = new SavePanel(this, "user" + Integer.toString(i+1) + ".dat");
		}
		ret[count-1] = new SavePanel(this, false);
		ret[count-1].filename.setText("selected.dat");
		return ret;
	}
	
	private void saveProperties(String filename) {
		try {
		File f = new File(filename);
			if( !(f.exists() && !f.isDirectory())) {
				System.out.println("File didn't Exist, creating");
				f.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(filename, false);
			props.store(out, "---Test Comment---");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public GUI(Properties prop){
		props = prop;
		
		savePath = props.getProperty("path");
		backup = Boolean.parseBoolean(props.getProperty("backup"));
		
		frame = createFrame("Hollow Knight - Save Manager");
		
		frame.setLayout(new BorderLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		
		loadedSaves = createSavePanels(5);
		saveContainer = new JPanel(new GridBagLayout());
		for( int i = 0; i < loadedSaves.length; i++ ){
			c.weightx = 1;c.weighty = 1;
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = i;
			saveContainer.add(loadedSaves[i], c);
		}
		c.weightx = 1;c.weighty = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		directoryExplorer = new JPanel();
		directoryExplorer.setLayout(new GridBagLayout());
		
		ft = new FileTree(new File(props.getProperty("path")));
		ft.gui = this;
		directoryExplorer.add(ft, c);
		
		JPanel directoryButtons = new JPanel();
		c.weighty = 0;
		c.gridy = 1;
		JButton backupToggle = new JButton("Backup: ON " );
		backupToggle.setMinimumSize(new Dimension(250,0));
		if( backup == false)
			backupToggle.setText("Backup: OFF");
		JButton setSaveLocation = new JButton("Set Save Location");
		JButton addDirectory = new JButton("Add Folder");
		JButton addSavefile = new JButton("New Savefile");
		dirOrSaveName = new JTextField("", 32);
		
		setSaveLocation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal = fc.showOpenDialog(new JFrame("Select Hollow Knight Save Directory"));

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            
		            String path = file.getParent();
		            savePath = path;
		            
		            props.setProperty("path", savePath);
		            saveProperties("appProperties");
		            
		            refreshFileTree(savePath);

		        } else {
		        }
			}
		});
		
		backupToggle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				backup = !backup;
				props.setProperty("backup", backup ? "true" : "false" );
				saveProperties("appProperties");
				
				backupToggle.setText(backup ?  "Backup: ON " : "Backup: OFF");
				
			}
		});
		
		addDirectory.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( ft.lastClicked == null )
					return;
				if( dirOrSaveName.getText().isEmpty())
					return;
				String path = savePath;
				int count = ft.lastClicked.getPath().length;
					
				for( int i = 1; i < count; i++ ){
					path += "/" + ft.lastClicked.getPath()[i].toString();
				}
				String name = dirOrSaveName.getText();
				if( new File(path).isFile()){
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) ft.lastClicked.getParent();
					dmtn.add(new DefaultMutableTreeNode(name));
					path = savePath;
					for( int i = 1; i < count-1; i++ ){
						path += "/" + ft.lastClicked.getPath()[i].toString();
					}
				} else {
					ft.lastClicked.add(new DefaultMutableTreeNode(name));
				}
				
				ft.model.reload();
				new File(path+"/"+name).mkdir();
				
				System.out.println(path + "" + name);	
				
				
			}
		});
		
		addSavefile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( ft.lastClicked == null )
					return;
				if( dirOrSaveName.getText().isEmpty())
					return;
				String path = savePath;
				int count = ft.lastClicked.getPath().length;
					
				for( int i = 1; i < count; i++ ){
					path += "/" + ft.lastClicked.getPath()[i].toString();
				}
				String name = dirOrSaveName.getText();
				if( new File(path).isFile()){
					path = savePath;
					for( int i = 1; i < count-1; i++ ){
						path += "/" + ft.lastClicked.getPath()[i].toString();
					}
				}
				try {
					new File(path+"/"+name+".dat").createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				System.out.println(path + "/" + name);	
				
				refreshFileTree(savePath);
			}
		});
		
		directoryButtons.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;gbc.weighty=1;
		gbc.gridx = 0;gbc.gridy=0;
		directoryButtons.add(backupToggle, gbc);
		gbc.gridx = 1;
		directoryButtons.add(setSaveLocation, gbc);
		gbc.gridx = 2;
		directoryButtons.add(addDirectory, gbc);
		gbc.gridx = 3;
		directoryButtons.add(addSavefile, gbc);
		gbc.gridx = 0;gbc.gridy=1;gbc.gridwidth=4;
		directoryButtons.add(dirOrSaveName, gbc);
		
		directoryExplorer.add(directoryButtons, c);
		
		//c.gridy = 0;
		//c.ipadx = 500;
		frame.add(saveContainer, BorderLayout.LINE_START);		
		//c.ipadx = 200;
		//c.weightx = 1;c.weighty = 1;
		//c.gridx = 1;
		//c.gridy = 0;
		frame.add(directoryExplorer, BorderLayout.CENTER);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}
