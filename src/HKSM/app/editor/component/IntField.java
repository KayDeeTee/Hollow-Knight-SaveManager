package HKSM.app.editor.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * A component wrapper for a JTextField which only accepts numerical input.
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 * @additional J Conrad
 *
 */
@SuppressWarnings("serial")
public class IntField extends JTextField {

	private class DigitOnlyAdapter extends KeyAdapter{
		
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
    
    public void setText(int n){
    	setText(String.valueOf(n));
    }
}