package HKSM.panel;

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
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import HKSM.GUI;

/**
 * SavePanel and LoadDeletePanel Listeners, migrated from their original location in SavePanel.java
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 * @migratedBy J Conrad
 *
 */
public class Listeners {
	
	/**
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	public static class LoadAll implements ActionListener{
		
		GUI gui;
		
		public LoadAll(GUI gui){
			this.gui = gui;
		}
		
		public void actionPerformed(ActionEvent e){
			System.out.println("LoadAll activated");
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
					System.out.println("Done.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	public static class Delete implements ActionListener{
		
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
				e.printStackTrace();
			}      
		}
		
		GUI gui;
		
		public Delete(GUI gui){
			this.gui = gui;
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("Delete activated");
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
			System.out.println(path);
			deleteFiles(path);
			gui.ft.model.reload();
			System.out.println("Done");
		}
	}
	
	/**
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	public static class Load implements ActionListener{
		
		GUI gui;
		JLabel filename;
		
		public Load(GUI gui, JLabel filename){
			this.gui = gui;
			this.filename = filename;
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("Load activated");
			if( gui.ft.lastClicked == null )
				return;
			String path = gui.savePath;
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
				e1.printStackTrace();
			}
			System.out.println("Done");
		}
	}
	
	/**
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	public static class Save implements ActionListener{
		
		GUI gui;
		JLabel filename;
		
		public Save(GUI gui, JLabel filename){
			this.gui = gui;
			this.filename = filename;
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("Save activated");
			if( gui.ft.lastClicked == null )
				return;
			String path = gui.savePath;
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
				e1.printStackTrace();
			}
			System.out.println("Done");
		}
	}
	
	/**
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	public static class SaveAs implements ActionListener{
		
		GUI gui;
		JLabel filename;
		
		public SaveAs(GUI gui, JLabel filename){
			this.gui = gui;
			this.filename = filename;
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("Save As activated");
			GUI.fc.setCurrentDirectory(new File(gui.savePath));
			int returnVal = GUI.fc.showOpenDialog(new JFrame("Save As..."));
			String path = gui.savePath;
			
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	
	            File file = GUI.fc.getSelectedFile();
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
					e1.printStackTrace();
				}

	        } else {
	        }
			gui.refreshFileTree(gui.savePath);
			System.out.println("Done");
		}
	}
	
}
