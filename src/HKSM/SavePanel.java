package HKSM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

//SavePanel is the panel that contain the 4 loaded saves data, and the buttons to interact with them
public class SavePanel extends JPanel{
	public JPanel panel;
	public JLabel filename;
	public JLabel completion;
	
	private GUI gui;
	
	public JButton loadSave;
	public JButton saveSave;
	public JButton saveAs;
	
	public JPanel Title;
	public JPanel Content;
	public JPanel ContentMain;
	public JPanel Functions;
	
	public JLabel geoLabel;
	public JLabel hpLabel;
	public JLabel regionLabel;
	public JLabel permaLabel;
	
	public static void deleteFiles (String path){    
		 try {
			 Path directory = Paths.get(path);
			 Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			    @Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			        Files.delete(file);
			        return FileVisitResult.CONTINUE;
			    }

			    @Override
			    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			        Files.delete(dir);
			        return FileVisitResult.CONTINUE;
			    }
			 });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	}
	
	public SavePanel(GUI _gui, boolean bool){
		gui = _gui;
		
		this.setLayout(new BorderLayout());
		
		filename = new JLabel("test");
		
		Title = new JPanel();
		Functions = new JPanel();
		Content = new JPanel();
		
		JButton delete = new JButton("DELETE");
		JButton loadAll = new JButton("LOAD ALL");
		
		loadAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( gui.ft.lastClicked == null )
					return;
				String path;
				if( gui.backup ){
					path = gui.savePath;
					String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
					for( int i = 1; i < 5; i++){
						File _src = new File(path+"/"+"user"+i+".dat");
						File _dst = new File(path+"/hksm-back/"+timeStamp+"."+"user"+i+".dat");	
						Path src = _src.toPath();
						Path dst = _dst.toPath();
						if( _src.exists()){
							try {
								Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				
				path = gui.savePath;
				int c = gui.ft.lastClicked.getPath().length;
				for( int i = 1; i < c; i++ ){
					path += "/" + gui.ft.lastClicked.getPath()[i].toString();
				}
				Path cop = new File(path).toPath();
				path = gui.savePath;
				for( int i = 1; i < 5; i++){
					Path src = new File(path+"/"+"user"+i+".dat").toPath();
					try {
						Files.copy(cop, src, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if( gui.ft.lastClicked == null )
					return;
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "This will delete all folders and files under what you have selected. Continue?","Warning",dialogButton);
				if(dialogResult != JOptionPane.YES_OPTION)
					return;
				String path = gui.savePath;
				int c = gui.ft.lastClicked.getPath().length;
				for( int i = 1; i < c; i++ ){
					path += "/" + gui.ft.lastClicked.getPath()[i].toString();
				}
				Path cop = new File(path).toPath();
				System.out.println(path);
				deleteFiles(path);
				gui.ft.model.reload();
			}
		});
		
		filename.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(filename, BorderLayout.PAGE_START);
		this.add(loadAll, BorderLayout.LINE_START);
		this.add(delete, BorderLayout.LINE_END);
	}
	
	public SavePanel(GUI _gui, String fileName){
		gui = _gui;
		
		this.setLayout(new BorderLayout());
		
		filename = new JLabel(fileName);
		
		filename.setHorizontalAlignment(SwingConstants.LEFT);
		
		completion = new JLabel("0%");
		completion.setHorizontalAlignment(SwingConstants.RIGHT);
		
		Title = new JPanel();
		Functions = new JPanel();
		Content = new JPanel();
		ContentMain = new JPanel();
		
		loadSave = new JButton("LOAD");
		saveSave = new JButton("SAVE");
		saveAs = new JButton("SAVE AS");
		
		loadSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( gui.ft.lastClicked == null )
					return;
				String path = gui.savePath;
				int count = gui.ft.lastClicked.getPath().length;
				File f = new File(path+"/hksm-back");
				if( !f.exists() )
					f.mkdir();
				String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
				File _src = new File(path+"/"+filename.getText());
				File _dst = new File(path+"/hksm-back/"+timeStamp+"."+filename.getText());	
				Path src = _src.toPath();
				Path dst = _dst.toPath();
				try {
					if( !_src.exists() || !gui.backup ){
						path = gui.savePath;
						int c = gui.ft.lastClicked.getPath().length;
						for( int i = 1; i < c; i++ ){
							path += "/" + gui.ft.lastClicked.getPath()[i].toString();
						}
						Path cop = new File(path).toPath();
						Files.copy(cop, src, StandardCopyOption.REPLACE_EXISTING);
					} else {
						Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
						if( _dst.exists() ){
							path = gui.savePath;
							int c = gui.ft.lastClicked.getPath().length;
							for( int i = 1; i < c; i++ ){
								path += "/" + gui.ft.lastClicked.getPath()[i].toString();
							}
							Path cop = new File(path).toPath();
							Files.copy(cop, src, StandardCopyOption.REPLACE_EXISTING);
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		saveSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if( gui.ft.lastClicked == null )
					return;
				String path = gui.savePath;
				int count = gui.ft.lastClicked.getPath().length;
				File f = new File(path+"/hksm-back");
				if( !f.exists() )
					f.mkdir();
				String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
				File _src = new File(path+"/"+filename.getText());
				File _dst = new File(path+"/hksm-back/"+timeStamp+"."+filename.getText());	
				Path src = _src.toPath();
				Path dst = _dst.toPath();
				try {
					if( !_src.exists() ){
						return;
					} else {
						path = gui.savePath;
						int c = gui.ft.lastClicked.getPath().length;
						for( int i = 1; i < c; i++ ){
							path += "/" + gui.ft.lastClicked.getPath()[i].toString();
						}
						Path cop = new File(path).toPath();
						if( !gui.backup)
							Files.copy(cop, dst, StandardCopyOption.REPLACE_EXISTING);
						Files.copy(src, cop, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		saveAs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				gui.fc.setCurrentDirectory(new File(gui.savePath));
				int returnVal = gui.fc.showOpenDialog(new JFrame("Save As..."));
				String path = gui.savePath;
				
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	
		            File file = gui.fc.getSelectedFile();
		            File _src = new File(path+"/"+filename.getText());
		            Path src = _src.toPath();
		            try {
						if( !_src.exists() ){
							return;
						} else {
							Path cop = file.toPath();
							Files.copy(src, cop, StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

		        } else {
		        }
				gui.refreshFileTree(gui.savePath);
			}
		});
		
		SaveLoader sl = new SaveLoader();
		JsonObject jsondata;
		
		IconFontSwing.register(FontAwesome.getIconFont());
		
		saveAs.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 18));
		saveAs.setText("");
		
		loadSave.setIcon(IconFontSwing.buildIcon(FontAwesome.CHEVRON_LEFT, 18));
		loadSave.setText("");
		
		saveSave.setIcon(IconFontSwing.buildIcon(FontAwesome.CHEVRON_RIGHT, 18));
		saveSave.setText("");
		
		hpLabel = new JLabel("HEALTH | MANA");
		regionLabel = new JLabel("REGION");
		geoLabel = new JLabel("GEO");
		permaLabel = new JLabel("STEEL SOUL");
		try {
			File f =new File(gui.savePath + "/" + fileName);
			System.out.println( f.toString() );
			if( f.exists() ){
				System.out.println("FILE EXISTS");
				jsondata = sl.loadSave(f);
				completion.setText(sl.getCompletion(jsondata));
				hpLabel.setText(sl.getHealthAndSoul(jsondata));
				regionLabel.setText(sl.getLocation(jsondata));
				geoLabel.setText(sl.getGeo(jsondata));
				permaLabel.setText(sl.getPerma(jsondata));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Title.setLayout(new BorderLayout());
		Title.add(filename, BorderLayout.WEST);
		Title.add(completion, BorderLayout.EAST);
		
		Functions.setLayout(new GridBagLayout());
		
		Content.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		ContentMain.setLayout(new GridBagLayout());
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx =1;c.weighty=1;
		c.gridx=0;c.gridy=0;
		
		ContentMain.add(hpLabel, c);
		c.gridy=1;
		ContentMain.add(regionLabel, c);
		c.gridy=2;
		ContentMain.add(geoLabel, c);
		c.gridy=3;
		ContentMain.add(permaLabel, c);
		
		this.add(this.Title, BorderLayout.PAGE_START);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx =1;c.weighty=1;
		c.gridx=0;c.gridy=0;
		
		Functions.add(loadSave,c);
		c.gridy=1;
		Functions.add(saveSave,c);
		c.gridy =2;
		Functions.add(saveAs,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		Content.add(Functions, c);
		c.gridx = 0;
		c.weightx = 1;
		Content.add(ContentMain, c);
		
		this.add(Content, BorderLayout.CENTER);
		
		this.add(new JSeparator(), BorderLayout.PAGE_END);
	}
}
