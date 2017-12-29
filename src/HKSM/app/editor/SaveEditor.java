package HKSM.app.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import com.google.gson.JsonObject;

import HKSM.app.editor.component.CharmPanel;
import HKSM.app.editor.component.Notches;
import HKSM.data.SaveLoader;
import HKSM.app.editor.Listeners.DocChecker;
import HKSM.app.editor.Listeners.AutoCalcActivator;

/**
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 */
@SuppressWarnings("serial")
public class SaveEditor extends JFrame {
	
	public JsonObject json;
	public File fObject;
	
	public SaveEditor(String path, String title){
		
		/* ****************** *
		 * PRE-INITIALIZATION *
		 * ****************** */
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(20000);
		String p = "playerData";
		fObject = new File(path);
		
		try {
			json = SaveLoader.loadSave(fObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonObject playerData = json.getAsJsonObject(p);
		
		/* ******************** *
		 * FRAME INITIALIZATION *
		 * ******************** */
		this.setTitle(title);
		this.setLayout(new BorderLayout());
		
		JTabbedPane tabs = new JTabbedPane();
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
			JMenuItem fileSave = new JMenuItem("Save");
			
		menu.add(file);
			file.add(fileSave);
			
		fileSave.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SaveLoader.saveSave(fObject, json);
			}
		});
		
		/* ************* *
		 * INVENTORY TAB *
		 * ************* */
		JPanel inventoryEditor = new JPanel();
		inventoryEditor.setLayout(new GridLayout(0,3));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "maxHealthBase"}, "HP", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "MPReserveMax"}, "MP", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "nailDamage"}, "Nail Damage", SaveField.INTEGER));

		inventoryEditor.add(new SaveField(json, new String[]{p, "ore"}, "Pale Ore", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "rancidEggs"}, "Rancid Eggs", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "simpleKeys"}, "Simple Keys", SaveField.INTEGER));

		inventoryEditor.add(new SaveField(json, new String[]{p, "geo"}, "Geo", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "dreamOrbs"}, "Dream Orbs", SaveField.INTEGER));
		inventoryEditor.add(new SaveField(json, new String[]{p, "permadeathMode"}, "Steel Soul", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasCyclone"}, "Cyclone", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDashSlash"}, "Dash Slash", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasUpwardSlash"}, "Great Slash", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "fireballLevel"}, "Fireball Lv.", SaveField.SPELL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "screamLevel"}, "Scream Lv.", SaveField.SPELL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "quakeLevel"}, "Dive Lv.", SaveField.SPELL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDash", "hasShadowDash"}, "Mothwing Cloak", SaveField.DASH));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasWalljump"}, "Mantis Claw", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasSuperDash"}, "Crystal Dash", SaveField.BOOL));
		
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDoubleJump"}, "Monarch Wings", SaveField.BOOL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasDreamNail", "hasDreamGate", "dreamNailUpgraded"}, "Dreamnail", SaveField.DREAMNAIL));
		inventoryEditor.add(new SaveField(json, new String[]{p, "hasAcidArmour"}, "Ismas", SaveField.BOOL));
		
		tabs.addTab("Inventory", inventoryEditor);
		
		/* ********* *
		 * CHARM TAB *
		 * ********* */
		
		// Notches
		int usedNotches = playerData.get("charmSlotsFilled").getAsInt();
		int totalNotches = playerData.get("charmSlots").getAsInt();
		Notches notches = new Notches(playerData, usedNotches, totalNotches);
		
		notches.equipped().getDocument().addDocumentListener(new DocChecker(playerData, "charmSlotsFilled", notches.equipped()));
		notches.max().getDocument().addDocumentListener(new DocChecker(playerData, "charmSlotsFilled", notches.max()));
		
		// Overcharmed flag
		JCheckBox overcharmed = new JCheckBox("Overcharmed");
		overcharmed.addActionListener( new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		          AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		          boolean selected = abstractButton.getModel().isSelected();
		          playerData.addProperty("overcharmed", selected);
		        }
		 });
		
		// Auto-Calculator
		JCheckBox autoCalc = new JCheckBox("Auto-Calculate");
		autoCalc.addActionListener(new AutoCalcActivator(playerData, notches, autoCalc, overcharmed));
		
		// Upper charm panel
		JPanel charmTop = new JPanel();
		charmTop.setLayout(new GridLayout(0,2));
		charmTop.add( new JLabel("Equipped Notches") );
		charmTop.add( new JLabel("Max Notches") );
		charmTop.add( notches.equipped() );
		charmTop.add( notches.max() );
		
		charmTop.add( autoCalc );
		charmTop.add( overcharmed );
		
		// Lower charm panel
		JPanel charmBottom = new JPanel();
		charmBottom.setLayout(new GridLayout(0,2));
		List<CharmPanel> charmList = CharmPanel.createCharmPanels(playerData, notches, autoCalc, overcharmed);
		
		for(CharmPanel each : charmList){
			charmBottom.add(each);
		}
		
		JPanel charmTab = new JPanel();
		charmTab.setLayout(new BorderLayout());
		
		charmTab.add(charmTop, BorderLayout.PAGE_START);
		charmTab.add(charmBottom, BorderLayout.CENTER);
		JScrollPane charmEditor = new JScrollPane(charmTab);
		
		
		/* ******************* *
		 * POST-INITIALIZATION *
		 * ******************* */
		tabs.addTab("Inventory", inventoryEditor);
		tabs.addTab("Charms", charmEditor);
		
		this.add(menu, BorderLayout.PAGE_START);
		this.add(tabs, BorderLayout.CENTER);
		this.pack();
		this.setSize(450, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
