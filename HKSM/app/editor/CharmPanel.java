package HKSM.app.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.BoolCheckboxListener;
import HKSM.app.editor.Listeners.DocChecker;
import HKSM.app.editor.Listeners.IntField;

@SuppressWarnings("serial")
public class CharmPanel extends JPanel implements Comparable<CharmPanel>  {
	
	public int id;
	public String name;
	public JsonObject playerData;

	public CharmPanel(int id, String name, JsonObject playerData){
		this.id = id;
		this.name = name;
		this.playerData = playerData;
		
		this.setLayout(new BorderLayout());
		this.add(new JLabel( name ), BorderLayout.PAGE_START);
		JPanel info = new JPanel();
		info.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
					
		String s = Integer.toString(id+1);
		
		boolean ow = playerData.get("gotCharm_" + s).getAsBoolean();
		boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
		int co = playerData.get("charmCost_" + s).getAsInt();
		
		JCheckBox owned = new JCheckBox("", ow);
		JCheckBox equip = new JCheckBox("", eq);		
		IntField cost = new IntField(co);
		
		owned.setToolTipText("Charm owned");
		equip.setToolTipText("Charm equipped");
		
		// Now uses boxed listeners
		owned.addActionListener(new BoolCheckboxListener(owned, playerData, "gotCharm_" + s));
		equip.addActionListener(new BoolCheckboxListener(equip, playerData, "equippedCharm_" + s));
		cost.getDocument().addDocumentListener(new DocChecker(playerData, "charmCost_" + s, cost));
		
		c.fill = GridBagConstraints.BOTH;
		c.gridy=0;c.weighty=0;c.weightx=0;
		c.gridx = 0;		
		info.add(owned, c);
		c.gridx = 1;
		info.add(equip, c);
		
		if( id == 22 || id == 23 || id == 24){
			//Charm is breakable
			boolean br = playerData.get("brokenCharm_" + s).getAsBoolean();
			JCheckBox broken = new JCheckBox("", br);
			broken.setToolTipText("Charm broken");
			broken.addActionListener(new BoolCheckboxListener(broken, playerData, "brokenCharm_" + s));
			c.gridx = 2;
			info.add(broken, c);
			c.gridx = 3;
		} else {
			c.gridx = 2;
		}		
		c.weightx=1;
		info.add(cost, c);
		
		this.add(info, BorderLayout.CENTER);
		this.add(Box.createRigidArea(new Dimension(0,15)), BorderLayout.PAGE_END);
	}
	
	public int compareTo(CharmPanel charmPanel){
		return this.name.compareTo(charmPanel.name);
	}
	
}
