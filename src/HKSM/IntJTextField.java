package HKSM;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * A simple wrapper for a JTextField which only accepts numerical input.
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class IntJTextField extends JTextField {

	    public IntJTextField(int def){
	    	this.setText(Integer.toString(def));
	        addKeyListener(new KeyAdapter() {
	            public void keyTyped(KeyEvent e) {
	                char ch = e.getKeyChar();

	                if (!Character.isDigit(ch) && ch != '\b') {
	                    e.consume();
	                }
	            }
	        });

	    }
}
