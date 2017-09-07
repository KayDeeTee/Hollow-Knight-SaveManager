package HKSM;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class IntJTextField extends JTextField {

	    public IntJTextField(int def){
	    	this.setText(Integer.toString(def));
	        addKeyListener(new KeyAdapter() {
	            public void keyTyped(KeyEvent e) {
	                char ch = e.getKeyChar();

	                if (!isNumber(ch) && ch != '\b') {
	                    e.consume();
	                }
	            }
	        });

	    }

	    private boolean isNumber(char ch){
	        return ch >= '0' && ch <= '9';
	    }
}
