package HKSM.app.editor;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.BoolCheckboxListener;
import HKSM.app.editor.Listeners.BoolSpellListener;
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
	public static DataType SPELL = DataType.SPELL;
	public static DataType DASH = DataType.DASH;
	public static DataType DREAMNAIL = DataType.DREAMNAIL;
	
	/** It is only necessary to access the data types, not an enum */
	protected enum DataType{
		INTEGER,
		STRING,
		BOOL,
		SPELL,
		DASH,
		DREAMNAIL
	}
	
	public String[] jpath;
	public JsonObject json;

	/** Initialize this save field to contain integer values */
	private void initIntegerField(){
		int data = json.get(jpath[0]).getAsInt();
		IntField intField = new IntField(data);
		DocChecker dc = new DocChecker(json, jpath[0], intField);
		intField.getDocument().addDocumentListener(dc);
		this.add(intField, BorderLayout.CENTER);
	}
	
	/** Initialize this save field to contain String values */
	private void initStringField(){
		this.add(new JTextField("STR"), BorderLayout.CENTER);
	}
	
	/** Initialize this save field to contain boolean values */
	private void initBoolField(){
		boolean data = json.get(jpath[0]).getAsBoolean();
		JCheckBox button = new JCheckBox("", data);
		BoolCheckboxListener bbl = new BoolCheckboxListener(button, json, jpath[0]); 
		button.addActionListener(bbl);
		this.add(button, BorderLayout.CENTER);
	}
	
	/** Initialize this save field to contain spell data */
	private void initSpellField(){
		int data = json.get(jpath[0]).getAsInt();
		JCheckBox rankOne = new JCheckBox("", (data&1)!=0);
		JCheckBox rankTwo = new JCheckBox("", (data&2)!=0);
		
		rankOne.setToolTipText("Rank 1");
		rankTwo.setToolTipText("Rank 2");
		
		BoolSpellListener bsl1 = new BoolSpellListener(rankOne, json, jpath[0], 1); 
		BoolSpellListener bsl2 = new BoolSpellListener(rankTwo, json, jpath[0], 2);
		
		rankOne.addActionListener(bsl1);
		rankTwo.addActionListener(bsl2);
		
		JPanel container = new JPanel();
		container.add(rankOne);
		container.add(rankTwo);
		this.add(container, BorderLayout.LINE_START);
	}
	
	private void initDashField(){
		
		JCheckBox rankOne = new JCheckBox("", json.get(jpath[0]).getAsBoolean());
		JCheckBox rankTwo = new JCheckBox("", json.get(jpath[1]).getAsBoolean());
		
		rankOne.setToolTipText("Mothwing Cloak");
		rankTwo.setToolTipText("Shadow Cloak");
		
		BoolCheckboxListener bcl1 = new BoolCheckboxListener(rankOne, json, jpath[0]); 
		BoolCheckboxListener bcl2 = new BoolCheckboxListener(rankTwo, json, jpath[1]); 
		
		rankOne.addActionListener(bcl1);
		rankTwo.addActionListener(bcl2);
		
		JPanel container = new JPanel();
		container.add(rankOne);
		container.add(rankTwo);
		this.add(container, BorderLayout.LINE_START);
	}
	
	private void initDreamField(){
		JCheckBox rankOne = new JCheckBox("",json.get(jpath[0]).getAsBoolean());
		JCheckBox rankTwo = new JCheckBox("",json.get(jpath[1]).getAsBoolean());
		JCheckBox rankThr = new JCheckBox("",json.get(jpath[2]).getAsBoolean());
		
		rankOne.setToolTipText("Dreamnail");
		rankTwo.setToolTipText("Dreamgate");
		rankThr.setToolTipText("Awakened Dreamnail");
		
		BoolCheckboxListener bcl1 = new BoolCheckboxListener(rankOne, json, jpath[0]); 
		BoolCheckboxListener bcl2 = new BoolCheckboxListener(rankTwo, json, jpath[1]); 
		BoolCheckboxListener bcl3 = new BoolCheckboxListener(rankThr, json, jpath[2]); 
		
		rankOne.addActionListener(bcl1);
		rankTwo.addActionListener(bcl2);
		rankThr.addActionListener(bcl3);
		
		JPanel container = new JPanel();
		container.add(rankOne);
		container.add(rankTwo);
		container.add(rankThr);
		this.add(container, BorderLayout.LINE_START);
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
		this.jpath = new String[jpath.length-1];
		for( int i = 0; i < this.jpath.length; i++)
			this.jpath[i] = jpath[i+1];
		this.setLayout(new BorderLayout());
		this.add(new JLabel(title), BorderLayout.PAGE_START);
		switch(type){
			case INTEGER: initIntegerField(); break;
			case STRING: initStringField(); break;
			case BOOL: initBoolField(); break;
			case SPELL: initSpellField(); break;
			case DASH: initDashField(); break;
			case DREAMNAIL: initDreamField(); break;
		}
	}
}
