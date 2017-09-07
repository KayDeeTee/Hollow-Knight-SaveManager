package HKSM;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.google.gson.JsonObject;

public class SaveField extends JPanel {

	public enum DataType{
		INTEGER,
		STRING,
		BOOL
	}
	
	public String[] jpath;
	public String title;
	public JsonObject json;
	
	public static void validateSaveData(JsonObject json){
		JsonObject playerData = json.getAsJsonObject("playerData");
		
		//NAIL ARTS VERIFY
		boolean dashSlash = playerData.get("hasDashSlash").getAsBoolean();
		boolean cyclone = playerData.get("hasCyclone").getAsBoolean();
		boolean upward = playerData.get("hasUpwardSlash").getAsBoolean();
		boolean hasNailArt = dashSlash | cyclone | upward;
		boolean hasAllNailArt = dashSlash & cyclone & upward;
		
		playerData.addProperty("hasNailArt", hasNailArt);
		playerData.addProperty("hasAllNailArts", hasAllNailArt);
		
		//SKILL VERIFY
		playerData.addProperty("canDash", playerData.get("hasDash").getAsBoolean());
		
		
	}
	
	public void initIntegerField(){
		int data = json.getAsJsonObject(jpath[0]).get(jpath[1]).getAsInt();
		//System.out.println(data);
		IntJTextField intField = new IntJTextField(data);
		this.add(intField, BorderLayout.CENTER);
		intField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    check();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    check();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    check();
			  }
			  
			  public void check(){
				  json.getAsJsonObject(jpath[0]).addProperty(jpath[1], intField.getText());
			  }
		});
	}
	
	public void initStringField(){
		this.add(new JTextField("STR"), BorderLayout.CENTER);
	}
	
	public void initBoolField(){
		boolean data = json.getAsJsonObject(jpath[0]).get(jpath[1]).getAsBoolean();
		JButton button = new JButton(data ? "owned" : "unowned");
		
		button.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean bool = json.getAsJsonObject(jpath[0]).get(jpath[1]).getAsBoolean();
				json.getAsJsonObject(jpath[0]).addProperty(jpath[1], !bool);
				button.setText(!bool ? "owned" : "unowned");
			}
			
		});
		
		this.add(button, BorderLayout.CENTER);
	}
	
	public SaveField(JsonObject jObject, String[] jsonpath, String name, DataType dt){
		this.jpath = jsonpath;
		this.title = name;
		this.json = jObject;
		this.setLayout(new BorderLayout());
		this.add(new JLabel(title), BorderLayout.PAGE_START);
		if( dt == DataType.INTEGER){
			initIntegerField();
		}
		if( dt == DataType.STRING){
			initStringField();
		}
		if( dt == DataType.BOOL){
			initBoolField();
		}
	}
	
	
}
