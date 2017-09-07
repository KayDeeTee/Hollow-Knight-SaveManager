package HKSM;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.security.Security;
import java.text.DateFormat.Field;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {
	
	public static Properties loadProperties(String filename) throws IOException{
		Properties prop = new Properties();
		File f = new File(filename);
		if( !(f.exists() && !f.isDirectory())) {
			System.out.println("File didn't Exist, creating");
			f.createNewFile();
			FileOutputStream out = new FileOutputStream(filename);
			prop.put("path", ".");
			prop.put("backup", "true");
			prop.store(out, "---No Comment---");
			out.close();
		}
		FileInputStream in = new FileInputStream(filename);
		prop.load(in);
		in.close();
		return prop;
	}
	
	static double getVersion () {
	    String version = System.getProperty("java.version");
	    int pos = version.indexOf('.');
	    pos = version.indexOf('.', pos+1);
	    return Double.parseDouble (version.substring (0, pos));
	}
	
	public static void main(String[] args) throws Exception {
		Security.setProperty("crypto.policy", "unlimited");
		Properties appProps = loadProperties("appProperties");
		
		/*System.out.println(Cipher.getMaxAllowedKeyLength( "Rijndael" ));
		String sec = System.getProperty("java.home")+"/lib/security/";

		if( Cipher.getMaxAllowedKeyLength( "Rijndael" ) < 2147483647){
			JFrame fJava = new JFrame("Encryption Limits");
			String text = "";
			double v = getVersion();
			if( v < 1.7 ){
				text = "Update your java to version 1.7 or above.";
			} else {
				if( v == 1.7 ){
					if(Desktop.isDesktopSupported())
					{
						text = "<html><div style='text-align: center;'>Install the files from the loaded webpage by<br> Accepting the License Agreement<br>Downloading and unzipping the Files<br>Copying local_policy.jar and US_export_policy.jar into lib/security folder that should have opened<br>then reload to fix Save Editing<br></div></html>";
						Desktop.getDesktop().browse(new URI("http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html"));
						Desktop.getDesktop().open(new File(sec));
					}
				}
				if( v == 1.8 ){
					if(Desktop.isDesktopSupported())
					{
						text = "<html><div style='text-align: center;'>Install the files from the loaded webpage by<br> Accepting the License Agreement<br>Downloading and unzipping the Files<br>Copying local_policy.jar and US_export_policy.jar into lib/security folder that should have opened<br>then reload to fix Save Editing<br></div></html>";
						Desktop.getDesktop().browse(new URI("http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html"));
						Desktop.getDesktop().open(new File(sec));
					}
				}
				if( v == 1.9 ){
					text = "There should be no problems with the Save Editor, message @KDT on the discord";
				}
			}
			JLabel msg = new JLabel(text);
			
			msg.setHorizontalAlignment(SwingConstants.CENTER);
			
			fJava.add(msg, SwingConstants.CENTER);
			fJava.setLocationRelativeTo(null);
			fJava.setAlwaysOnTop (true);
			fJava.pack();
			fJava.setVisible(true);
		}*/
		
		GUI gui = new GUI(appProps);
	}
	
}
