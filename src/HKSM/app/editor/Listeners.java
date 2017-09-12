package HKSM.app.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.google.gson.JsonObject;

import HKSM.app.editor.component.Notches;
import HKSM.app.editor.component.IntField;

public class Listeners {
	
	public static class AutoCalcActivator implements ActionListener{
		
		JsonObject playerData;
		Notches notches;
		JCheckBox autoCalc;
		JCheckBox overcharmed;
		
		public AutoCalcActivator(JsonObject playerData, Notches notches, JCheckBox autoCalc, JCheckBox overcharmed){
			this.playerData = playerData;
			this.notches = notches;
			this.autoCalc = autoCalc;
			this.overcharmed = overcharmed;
		}
		
		@Override
		/** Additional auto-calc logic is contained in CharmPanelListener */
		public void actionPerformed(ActionEvent ae){
			if(autoCalc.isSelected()){
				int tNotches = 0;
				for( int i = 0; i < 36; i++){
					String s = Integer.toString(i+1);
					boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
					int co = playerData.get("charmCost_" + s).getAsInt();
					if( eq )
						tNotches += co;
				}
				playerData.addProperty("charmSlotsFilled", tNotches);
				notches.setEquipped(tNotches);
				overcharmed.setSelected(notches.overcharmed());
			}
		}
	}
	
	/**
	 * Simple ActionListener class to improve code readability. All actions
	 * trigger the member's boolean value to switch.
	 * 
	 * @author K Thorpe
	 *
	 */
	public static class BoolCheckboxListener implements ActionListener{
		
		JCheckBox target;
		JsonObject playerData;
		String memberName;
		
		public BoolCheckboxListener(JCheckBox target, JsonObject playerData, String memberName){
			this.target = target;
			this.playerData = playerData;
			this.memberName = memberName;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			boolean bool = playerData.get(memberName).getAsBoolean();
			playerData.addProperty(memberName, !bool);
			target.setSelected(!bool);
		}
	}
	
	public static class BoolSpellListener implements ActionListener{
		
		JCheckBox target;
		JsonObject playerData;
		String memberName;
		int value;
		
		public BoolSpellListener(JCheckBox target, JsonObject playerData, String memberName, int value){
			this.target = target;
			this.playerData = playerData;
			this.memberName = memberName;
			this.value = value;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			boolean b = target.isSelected();
			int v = playerData.get(memberName).getAsInt();
			
			if( b ){
				v += value;
			} else {
				v -= value;
			}
			
			playerData.addProperty(memberName, v);
		}
	}
	
	public static class BoolVoidListener implements ActionListener{
		
		JCheckBox target;
		JsonObject playerData;
		String memberName;
		int value;
		
		public BoolVoidListener(JCheckBox target, JsonObject playerData, String memberName, int value){
			this.target = target;
			this.playerData = playerData;
			this.memberName = memberName;
			this.value = value;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			boolean b = target.isSelected();
			//target.setSelected(b);
			int v = playerData.get("royalCharmState").getAsInt();
			
			if( b ){
				v += value;
			} else {
				v -= value;
			}
			
			playerData.addProperty("royalCharmState", v);
		}
	}
	
	/**
	 * Because of the relationship between charm data, it makes sense
	 * to box these listeners as a set. This:<br/><br/>
	 * 
	 * -codependently changes gotCharm and equippedCharm
	 * -tracks changes to the charm's cost
	 * -determines whether to autocalculate equipped notches and overcharmed status 
	 * 
	 * @author J Conrad
	 */
	public static class CharmPanelListener implements ActionListener, DocumentListener{
		
		JsonObject playerData;
		
		Notches notches;
		JCheckBox got;
		JCheckBox equipped;
		IntField cost;
		
		JCheckBox autoCalc;
		JCheckBox overcharmed;
		private Runnable runAutoCalc;
		
		public CharmPanelListener(JsonObject playerData, JCheckBox got, JCheckBox equipped, IntField cost, JCheckBox autoCalc, JCheckBox overcharmed, Notches notches){
			this.playerData = playerData;
			
			this.notches = notches;
			this.got = got;
			this.equipped = equipped;
			this.cost = cost;
			
			this.autoCalc = autoCalc;
			this.overcharmed = overcharmed;
			setRunAutoCalc();
			
			got.addActionListener(this);
			equipped.addActionListener(this);
			cost.getDocument().addDocumentListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent ae){
			JCheckBox source = (JCheckBox)ae.getSource();
			String property = ae.getActionCommand();
			System.out.print(property + ": ");
			boolean val = source.isSelected();
			System.out.println(val);
			playerData.addProperty(property, val);
			
			if(source.equals(got)){
				boolean is = equipped.isSelected() && got.isSelected();
				equipped.setSelected(is);
				playerData.addProperty(equipped.getActionCommand(), is);
			}
			else{
				boolean is = equipped.isSelected() || got.isSelected();
				got.setSelected(is);
				playerData.addProperty(got.getActionCommand(), is);
			}
			if(autoCalc.isSelected()){
				SwingUtilities.invokeLater(runAutoCalc);
			}
		}
	
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			check();
			if(autoCalc.isSelected()){
				SwingUtilities.invokeLater(runAutoCalc);
			}
		}
	
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			check();
			if(autoCalc.isSelected()){
				SwingUtilities.invokeLater(runAutoCalc);
			}
		}
	
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			check();
			if(autoCalc.isSelected()){
				SwingUtilities.invokeLater(runAutoCalc);
			}
		}
		
		private void check(){
			playerData.addProperty(cost.getName(), cost.getText());
		}
		
		private void setRunAutoCalc(){
			this.runAutoCalc = new Runnable(){
				@Override
				public void run() {
					if(autoCalc.isSelected()){
						int tNotches = 0;
						for( int i = 0; i < 36; i++){
							String s = Integer.toString(i+1);
							boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
							int co = playerData.get("charmCost_" + s).getAsInt();
							if( eq )
								tNotches += co;
						}
						playerData.addProperty("charmSlotsFilled", tNotches);
						notches.setEquipped(tNotches);
						overcharmed.setSelected(notches.overcharmed());
					}
				}
			};
		}
	}

	/**
	 * Simple DocumentListener class to improve code readability. All 
	 * document events trigger a change to the property's value through check()
	 * 
	 * @author J Conrad
	 */
	public static class DocChecker implements DocumentListener{
		
		JsonObject playerData;
		String propertyName;
		IntField target;
		
		public DocChecker(JsonObject playerData, String propertyName, IntField target){
			this.playerData = playerData;
			this.propertyName = propertyName;
			this.target = target;
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
		    check();
		}
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			check();
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
		    check();
		}
		
		public void check(){
			playerData.addProperty(propertyName, target.getText());
		}
	}

}
