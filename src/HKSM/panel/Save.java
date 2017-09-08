package HKSM.panel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.google.gson.JsonObject;

import HKSM.GUI;
import HKSM.SaveLoader;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

/**
 * SavePanel contains the 4 loaded player saves and the buttons to interact with them.
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 * @cleanup J Conrad
 */
@SuppressWarnings("serial")
public class Save extends JPanel{
	
	/* Data Function Buttons */
	public JButton load;
	public JButton save;
	public JButton saveAs;
	
	/* Content Panels */
	public JPanel Title;
	public JPanel Content;
	public JPanel Overview; // formerly ContentMain
	public JPanel Functions;
	
	/* Save Overview Labels */
	public JLabel fileName;
	public JLabel completion;
	public JLabel hpLabel;
	public JLabel geoLabel;
	public JLabel regionLabel;
	public JLabel permaLabel;
	
	/**
	 * This SavePanel creates a user data manager and its necessary buttons.
	 * 
	 * @param gui the listener to the SavePanel 
	 * @param file the name of the file to be opened or created
	 */
	public Save(GUI gui, String file){
		/* ******** *
		 * PRE-INIT *
		 * ******** */
		IconFontSwing.register(FontAwesome.getIconFont());
		this.setLayout(new BorderLayout());
		
		/* Set & Load start values for Save Overview Labels */
		initializeLabels(gui, file);
		
		/* ******* *
		 * BUTTONS *
		 * ******* */
		
		/* Load < */
		load = new JButton();
		load.setIcon(IconFontSwing.buildIcon(FontAwesome.CHEVRON_LEFT, 18));
		load.addActionListener(new Listeners.Load(gui, fileName));
		
		/* Save > */
		save = new JButton();
		save.setIcon(IconFontSwing.buildIcon(FontAwesome.CHEVRON_RIGHT, 18));
		save.addActionListener(new Listeners.Save(gui, fileName));
		
		/* Save As [] */
		saveAs = new JButton();
		saveAs.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 18));
		saveAs.addActionListener(new Listeners.SaveAs(gui, fileName));
		
		/* ************ *
		 * CONTENT INIT *
		 * ************ */
		
		Content = new JPanel();
		Content.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		/* Overview */
		Overview = new JPanel();
		Overview.setLayout(new GridBagLayout());
		
		setGBC(c, 0, 0, 1, 1);
		Overview.add(hpLabel, c);
		setGBC(c, 0, 1, 1, 1);
		Overview.add(regionLabel, c);
		setGBC(c, 0, 2, 1, 1);
		Overview.add(geoLabel, c);
		setGBC(c, 0, 3, 1, 1);
		Overview.add(permaLabel, c);
		setGBC(c, 0, 0, 1, 1);
		Content.add(Overview, c);
		
		/* Functions */
		Functions = new JPanel();
		Functions.setLayout(new GridBagLayout());
		
		setGBC(c, 0, 0, 1, 1);
		Functions.add(load,c);
		setGBC(c, 0, 1, 1, 1);
		Functions.add(save,c);
		setGBC(c, 0, 2, 1, 1);
		Functions.add(saveAs,c);
		setGBC(c, 1, 0, 0, 1);
		Content.add(Functions, c);
		
		/* Build */
		this.add(this.Title, BorderLayout.PAGE_START);
		this.add(Content, BorderLayout.CENTER);
		this.add(new JSeparator(), BorderLayout.PAGE_END);
	}
	
	/**
	 * Shorthand method to set GridBagConstraints before adding a component.
	 * Submit GridBagConstraints, gridx, gridy, weightx, and weighty in that order.
	 */
	private void setGBC(GridBagConstraints gbc, int gridx, int gridy, int weightx, int weighty){
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
	}
	
	/**
	 * Attempt to load overview of user data. If none is available, then
	 * the placeholder labels are maintained
	 * 
	 */
	private void initializeLabels(GUI gui, String file){
		fileName = new JLabel(file);
		fileName.setHorizontalAlignment(SwingConstants.LEFT);
		
		completion = new JLabel("0%");
		completion.setHorizontalAlignment(SwingConstants.RIGHT);
		
		Title = new JPanel();
		Title.setLayout(new BorderLayout());
		Title.add(fileName, BorderLayout.WEST);
		Title.add(completion, BorderLayout.EAST);
		
		hpLabel = new JLabel("HEALTH | MANA");
		regionLabel = new JLabel("REGION");
		geoLabel = new JLabel("GEO");
		permaLabel = new JLabel("STEEL SOUL");
		try {
			File f = new File(gui.savePath + "/" + file);
			System.out.println( f.toString() );
			if( f.exists() ){
				System.out.println("FILE EXISTS");
				JsonObject jsondata = SaveLoader.loadSave(f);
				completion.setText(SaveLoader.getCompletion(jsondata));
				hpLabel.setText(SaveLoader.getHealthAndSoul(jsondata));
				regionLabel.setText(SaveLoader.getLocation(jsondata));
				geoLabel.setText(SaveLoader.getGeo(jsondata));
				permaLabel.setText(SaveLoader.getPerma(jsondata));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}