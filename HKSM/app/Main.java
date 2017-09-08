package HKSM.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import java.util.Properties;

/**
 * The launcher for HKSM. Main deals with platform- and version-dependent properties
 * of the application, and directs users to download JCE if necessary.
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 *
 */
public class Main {
	
	/**
	 * Ensures and loads the file containing the application properties exists
	 * 
	 * @param filename the name of the application properties
	 * @return the Properties loaded from %user.dir%/appProperties
	 * @throws IOException if the FileInputStream fails
	 */
	public static Properties loadProperties(String filename) throws IOException{
		Properties prop = new Properties();
		File f = new File(filename);
		/* In case of non-existent properties file */
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
	
	/** @return the truncated java version as a double; e.g. "1.7.0_1" returns 1.7 */
	static double getVersion () {
	    String version = System.getProperty("java.version");
	    int pos = version.indexOf('.');
	    pos = version.indexOf('.', pos+1);
	    return Double.parseDouble (version.substring (0, pos));
	}
	
	public static void main(String[] args) throws Exception {
		Security.setProperty("crypto.policy", "unlimited");
		Properties appProps = loadProperties("appProperties");
		
		@SuppressWarnings("unused")
		GUI gui = new GUI(appProps);
	}
	
}
