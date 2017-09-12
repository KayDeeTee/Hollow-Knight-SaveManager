package HKSM.unused;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import com.google.gson.JsonObject;

import HKSM.app.editor.component.IntField;

/**
 * Unused/deprecated listeners are compiled here so that they can be used later if needed
 */
public class Listeners {

	/**
	 * Simple ActionListener class to improve code readability. All actions
	 * trigger the member's boolean value to switch.
	 * 
	 * @author J Conrad
	 *
	 */
	public static class BoolButtonListener implements ActionListener{
		
		JButton target;
		JsonObject playerData;
		String memberName;
		String ifTrue;
		String ifFalse;
		
		public BoolButtonListener(JButton target, JsonObject playerData, String memberName, String ifTrue, String ifFalse){
			this.target = target;
			this.playerData = playerData;
			this.memberName = memberName;
			this.ifTrue = ifTrue;
			this.ifFalse = ifFalse;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean bool = playerData.get(memberName).getAsBoolean();
			playerData.addProperty(memberName, !bool);
			target.setText(!bool ? ifTrue : ifFalse);
		}
	}
	
	/**
	 * Originally slated for use for equip buttons in the CharmPanel class,
	 * the functions of this listener are now implemented in CharmPanelListener.
	 */
	public static class BoolEquipListener implements ActionListener{
		
		JCheckBox target;
		JCheckBox autoCalc;
		IntField totalNotches;
		JsonObject playerData;
		String memberName;
		
		public BoolEquipListener(JCheckBox target, JCheckBox autoCalc, IntField totalNotches, IntField value, JsonObject playerData, String memberName){
			this.target = target;
			this.autoCalc = autoCalc;
			this.totalNotches = totalNotches;
			this.playerData = playerData;
			this.memberName = memberName;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean bool = playerData.get(memberName).getAsBoolean();
			playerData.addProperty(memberName, !bool);
			target.setSelected(!bool);
			int tNotches = 0;
			if(autoCalc.isSelected()){
				for( int i = 0; i < 36; i++){
					String s = Integer.toString(i+1);
					boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
					int co = playerData.get("charmCost_" + s).getAsInt();
					if( eq )
						tNotches += co;
				}
				playerData.addProperty("charmSlotsFilled", tNotches);
				totalNotches.setText(Integer.toString(tNotches));
			}
		}
	}
}
