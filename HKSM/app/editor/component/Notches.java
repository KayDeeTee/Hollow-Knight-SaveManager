package HKSM.app.editor.component;

import com.google.gson.JsonObject;

import HKSM.app.editor.Listeners.DocChecker;

/**
 * An wrapper API for equipped and max notches. Primarily
 * to clean up CharmPanel and CharmPanelListener.
 * 
 * @author J Conrad
 */
public class Notches{
	
	IntField equipped;
	IntField max;
	
	public Notches(JsonObject playerData, int equipped, int max){
		this.equipped = new IntField(equipped);
		this.max = new IntField(max);
		this.equipped.getDocument().addDocumentListener(new DocChecker(playerData, "charmSlotsFilled", this.equipped));
		this.max.getDocument().addDocumentListener(new DocChecker(playerData, "charmSlotsFilled", this.max));
	}
	
	// Equipped Notches methods
	public IntField equipped(){ return equipped; }
	public int getEquipped(){ return equipped.getValue(); }
	public void setEquipped(int n){ equipped.setText(n); }
	
	// Max Notches methods
	public IntField max(){ return max; }
	public int getMax(){ return max.getValue(); }
	public void setMax(int n){ max.setText(n); }
	
	public boolean overcharmed(){ return equipped.greaterThan(max); }
}