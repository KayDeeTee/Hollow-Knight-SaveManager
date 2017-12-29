package HKSM.app.editor.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.BoolCheckboxListener;
import HKSM.app.editor.Listeners.BoolVoidListener;
import HKSM.app.editor.Listeners.CharmPanelListener;

@SuppressWarnings("serial")
/**
 * The CharmPanel class contains all of the pertinent information about
 * a particular charm. It also contains a static method for the creation and
 * delivery of all of the SaveEditor's CharmPanels.
 * 
 * @author K Thorpe
 */
public class CharmPanel extends JPanel implements Comparable<CharmPanel>  {
	
	private static String[] charmNames = new String[]{
			"Gathering Swarm",
			"Wayward Compass",
			"Grubsong",
			"Stalwart Shell",
			"Baldur Shell",
			"Fury of the Fallen",
			"Quick Focus",
			"Lifeblood Heart",
			"Lifeblood Core",
			"Defender's Crest",
			"Flukenest",
			"Thorns of Agony",
			"Mark of Pride",
			"Steady Body",
			"Heavy Blow",
			"Sharp Shadow",
			"Spore Shroom",
			"Longnail",
			"Shaman Stone",
			"Soul Catcher",
			"Soul Eater",
			"Glowing Womb",
			"Fragile Heart",
			"Fragile Greed",
			"Fragile Strength",
			"Nailmaster's Glory",
			"Joni's Blessing",
			"Shape of Unn",
			"Hiveblood",
			"Dream Wielder",
			"Dashmaster",
			"Quick Slash",
			"Spell Twister",
			"Deep Focus",
			"Grubberfly's Elegy",
			"Void Soul",
			// The Grimm Troupe DLC
			"Sprintmaster",
			"Dreamshield",
			"Weaversong",
			"Grimmchild / Carefree Melody"
	};
	
	public static List<CharmPanel> createCharmPanels(JsonObject playerData, Notches notches, JCheckBox autoCalc, JCheckBox overcharmed){
		List<CharmPanel> out = new ArrayList<CharmPanel>(); 
		for( int i = 0; i < 40; i++){
			CharmPanel charm = new CharmPanel(i, charmNames[i], playerData, notches, autoCalc, overcharmed);
			out.add(charm);
		}
		Collections.sort(out);
		return out;
	}
	
	public String name;
	
	public CharmPanel(int id, String name, JsonObject playerData, Notches notches, JCheckBox autoCalc, JCheckBox overcharmed){
		this.name = name;
		
		this.setLayout(new BorderLayout());
		this.add(new JLabel(name), BorderLayout.PAGE_START);
					
		String s = Integer.toString(id+1);
		boolean g = playerData.get("gotCharm_" + s).getAsBoolean();
		boolean e = playerData.get("equippedCharm_" + s).getAsBoolean();
		int c = playerData.get("charmCost_" + s).getAsInt();
		
		/* CharmPanelListener setup */
		JCheckBox got = new JCheckBox("", g);
		got.setActionCommand("gotCharm_" + s);
		got.setToolTipText("Charm owned");
		
		JCheckBox equipped = new JCheckBox("", e);
		equipped.setActionCommand("equippedCharm_" + s);
		equipped.setToolTipText("Charm equipped");
		
		IntField cost = new IntField(c);
		cost.setName("charmCost_" + s);
		
		@SuppressWarnings("unused")
		CharmPanelListener TESTL = new CharmPanelListener(playerData, got, equipped, cost, autoCalc, overcharmed, notches);
		
		
		/* Add */
		JPanel info = new JPanel();
		info.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;		
		info.add(got, gbc);
		
		gbc.gridx = 1;
		info.add(equipped, gbc);
		

		if( id == 22 || id == 23 || id == 24){
			String [] unbrNm = new String[]{"Health", "Greed", "Strength"};

			//Charm is breakable
			boolean broken = playerData.get("brokenCharm_" + s).getAsBoolean();
			JCheckBox brokenBox = new JCheckBox("", broken);
			brokenBox.setToolTipText("Charm broken");
			brokenBox.addActionListener(new BoolCheckboxListener(brokenBox,
					playerData,
					"brokenCharm_" + s));
			gbc.gridx = 1;
			info.add(brokenBox, gbc);
			gbc.gridx = 2;

			//Unbreakable Version
			boolean nonBreakable = playerData.get("fragile" + unbrNm[id-22] + "_unbreakable").getAsBoolean();
			JCheckBox nonBreakableBox = new JCheckBox("", nonBreakable);
			nonBreakableBox.setToolTipText("Unbreakable");
			nonBreakableBox.addActionListener(new BoolCheckboxListener(nonBreakableBox,
				playerData,
				"fragile" + unbrNm[id-22] + "_unbreakable"));
			info.add(nonBreakableBox, gbc);
			gbc.gridx = 3;
		} else {
			gbc.gridx = 2;
		}


		if( id == 39 ){
			//TODO: add Box for Charm: Grimmchild -> Carefree Melody
		}

		//CHARM_NAME_36_A White Fragment
		//CHARM_NAME_36_B Kingsoul
		//CHARM_NAME_36_C Void Heart
		if( id == 35 ){
			//Charm is void soul
			int charmState = playerData.get("royalCharmState").getAsInt();
			
			JCheckBox left = new JCheckBox("", (charmState&1)!=0);
			JCheckBox right = new JCheckBox("", (charmState&2)!=0);
			JCheckBox voidSoul = new JCheckBox("", (charmState&4)!=0);
			
			left.addActionListener(new BoolVoidListener(left, playerData, "royalCharmState", 1));
			right.addActionListener(new BoolVoidListener(right, playerData, "royalCharmState", 2));
			voidSoul.addActionListener(new BoolVoidListener(voidSoul, playerData, "royalCharmState", 4));
			
			
			left.setToolTipText("Left Fragment");
			right.setToolTipText("Right Fragment");
			voidSoul.setToolTipText("Voided");
			
			info.remove(got);
			gbc.gridx = 2;
			info.add(left, gbc);
			gbc.gridx = 3;
			info.add(right, gbc);
			gbc.gridx = 4;
			info.add(voidSoul, gbc);
			gbc.gridx = 5;
		} else {
			gbc.gridx = 2;
		}


		gbc.weightx = 1;
		info.add(cost, gbc);
		
		this.add(info, BorderLayout.CENTER);
		this.add(Box.createRigidArea(new Dimension(0,15)), BorderLayout.PAGE_END);
	}
	
	public int compareTo(CharmPanel charmPanel){
		return this.name.compareTo(charmPanel.name);
	}
}
