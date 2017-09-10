package HKSM.app.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.google.gson.JsonObject;

public class Listeners {

	/**
	 * A simple wrapper for a JTextField which only accepts numerical input.
	 * 
	 * @author Kristian Thorpe <sfgmugen@gmail.com>
	 *
	 */
	@SuppressWarnings("serial")
	public static class IntField extends JTextField {

		private static class DigitOnlyAdapter extends KeyAdapter{
			
			@Override
			public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                // Added a guarantee that IntFields will not fail to be in the range of integers
                if (!Character.isDigit(ch) && ch != '\b' || ((JTextField) e.getSource()).getText().length() > 8) {
                    e.consume();
                }
            }
		}
		
	    public IntField(int def){
	    	this.setText(Integer.toString(def));
	    	this.addKeyListener(new DigitOnlyAdapter());
	    }
	    
	    public int getValue(){
	    	return getText().length() > 0 ? Integer.parseInt(getText()) : 0;
	    }
	    
	    public boolean greaterThan(IntField n){
	    	return getValue() > n.getValue();
	    }
	}
	
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
	
	/**
	 * 
	 *
	 */
//	public static class BoolEquipListener implements ActionListener{
//		
//		JCheckBox target;
//		JCheckBox autoCalc;
//		IntField totalNotches;
//		JsonObject playerData;
//		String memberName;
//		
//		public BoolEquipListener(JCheckBox target, JCheckBox autoCalc, IntField totalNotches, IntField value, JsonObject playerData, String memberName){
//			this.target = target;
//			this.autoCalc = autoCalc;
//			this.totalNotches = totalNotches;
//			this.playerData = playerData;
//			this.memberName = memberName;
//		}
//		
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			boolean bool = playerData.get(memberName).getAsBoolean();
//			playerData.addProperty(memberName, !bool);
//			target.setSelected(!bool);
//			int tNotches = 0;
//			if(autoCalc.isSelected()){
//				for( int i = 0; i < 36; i++){
//					String s = Integer.toString(i+1);
//					boolean eq = playerData.get("equippedCharm_" + s).getAsBoolean();
//					int co = playerData.get("charmCost_" + s).getAsInt();
//					if( eq )
//						tNotches += co;
//				}
//				playerData.addProperty("charmSlotsFilled", tNotches);
//				totalNotches.setText(Integer.toString(tNotches));
//			}
//			
//		}
//	}
	
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
			//target.setSelected(b);
			int v = playerData.get(memberName).getAsInt();
			
			if( b ){
				v += value;
			} else {
				v -= value;
			}
			
			playerData.addProperty(memberName, v);
		}
	}
	

	/**
	 * Simple DocumentListener class to improve code readability. All 
	 * document events trigger a change to the property's value through check()
	 * 
	 * @author J Conrad
	 *
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

	/**
	 * Because of the relationship between charm data, it makes sense
	 * to box these listeners as a set. This:<br/><br/>
	 * 
	 * -codependently changes gotCharm and equippedCharm
	 * -tracks changes to the charm's cost
	 * -determines whether to autocalculate equipped notches and overcharmed status 
	 * 
	 * @author J Conrad
	 *
	 */
	public static class CharmPanelListener implements ActionListener, DocumentListener{
		
		JsonObject playerData;
		
		JCheckBox got;
		JCheckBox equipped;
		IntField cost;
		
		IntField eNotch;
		IntField mNotch;
		
		JCheckBox autoCalc;
		JCheckBox overcharmed;
		private Runnable runAutoCalc;
		
		public CharmPanelListener(JsonObject playerData, JCheckBox got, JCheckBox equipped, IntField cost, JCheckBox autoCalc, JCheckBox overcharmed, IntField eNotch, IntField mNotch){
			this.playerData = playerData;
			
			this.got = got;
			this.equipped = equipped;
			this.cost = cost;
			
			this.eNotch = eNotch;
			this.mNotch = mNotch;
			
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
						eNotch.setText(Integer.toString(tNotches));
						overcharmed.setSelected(eNotch.greaterThan(mNotch));
					}
				}
			};
		}
	}
	
}
