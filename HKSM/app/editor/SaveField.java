package HKSM.app.editor;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.BoolButtonListener;
import HKSM.app.editor.Listeners.DocChecker;
import HKSM.app.editor.Listeners.IntField;

/**
 * SaveFields are panels which continually report a restricted value to a JsonObject.
 * They may contain integer or string text fields, or boolean button toggles. 
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class SaveField extends JPanel {

	public static DataType INTEGER = DataType.INTEGER;
	public static DataType STRING = DataType.STRING;
	public static DataType BOOL = DataType.BOOL;
	
	/** It is only necessary to access the data types, not an enum */
	protected enum DataType{
		INTEGER,
		STRING,
		BOOL
	}
	
	public String jpath;
	public JsonObject json;
	
	/** Initialize this save field to contain integer values */
	private void initIntegerField(){
		int data = json.get(jpath).getAsInt();
		IntField intField = new IntField(data);
		DocChecker dc = new DocChecker(json, jpath, intField);
		intField.getDocument().addDocumentListener(dc);
		this.add(intField, BorderLayout.CENTER);
	}
	
	/** Initialize this save field to contain String values */
	private void initStringField(){
		this.add(new JTextField("STR"), BorderLayout.CENTER);
	}
	
	/** Initialize this save field to contain boolean values */
	private void initBoolField(){
		boolean data = json.get(jpath).getAsBoolean();
		JButton button = new JButton(data ? "owned" : "unowned");
		BoolButtonListener bbl = new BoolButtonListener(button, json, jpath, "owned", "unowned"); 
		button.addActionListener(bbl);
		this.add(button, BorderLayout.CENTER);
	}
	
	/**
	 * Create an editable or togglable SaveField restricted to the specs provided 
	 * 
	 * @param json the JsonObject for the SaveField to listen
	 * @param jpath the path of the JSON, which contains two string values
	 * @param title the title of the field's JLabel
	 * @param type the DataType to which the SaveField is restricted
	 */
	public SaveField(JsonObject json, String[] jpath, String title, DataType type){
		this.json = json.getAsJsonObject(jpath[0]);
		this.jpath = jpath[1];
		this.setLayout(new BorderLayout());
		this.add(new JLabel(title), BorderLayout.PAGE_START);
		switch(type){
			case INTEGER: initIntegerField(); break;
			case STRING: initStringField(); break;
			case BOOL: initBoolField();
		}
	}
}
