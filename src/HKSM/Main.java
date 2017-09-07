package HKSM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

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
	
	public static void main(String[] args) throws Exception {
		Properties appProps = loadProperties("appProperties");
		GUI gui = new GUI(appProps);
	}
	
}
