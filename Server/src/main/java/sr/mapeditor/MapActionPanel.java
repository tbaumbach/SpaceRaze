/*
 * Created on 2005-jun-18
 */
package sr.mapeditor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.client.components.SRButton;

/**
 * @author WMPABOD
 *
 * Shows SpacesRaze map action texts (what a user is supposed to do on the map
 * at a certain time).
 */
@SuppressWarnings("serial")
public class MapActionPanel extends JPanel implements Runnable, ActionListener{
	private String actionText;
	private Thread runner;
	private boolean showText;
	private SRButton cancelBtn;
	private EditorGUIPanel guiPanel;
	
	public MapActionPanel(EditorGUIPanel guiPanel){
		this.guiPanel = guiPanel;
		setBorder(new LineBorder(StyleGuide.colorNeutral));
		setBackground(StyleGuide.colorBackground);
		setLayout(null);
		setOpaque(false);
		
		cancelBtn = new SRButton("Cancel");
		cancelBtn.setBounds(34,68,55,17);
		cancelBtn.setVisible(false);
		cancelBtn.addActionListener(this);
		add(cancelBtn);
	}
	
	public void paintComponent(Graphics g){
  		Dimension d = getSize();
  		FontMetrics fm = getFontMetrics(getFont());
  		int xoffset = 0;
		g.setColor(StyleGuide.colorNeutral.darker());
  		xoffset = (d.width - fm.stringWidth("Map action:"))/2;
		g.drawString("Map action:",xoffset,16);
		if (actionText == null){
			g.setColor(StyleGuide.colorNeutral);
	  		xoffset = (d.width - fm.stringWidth("None"))/2;
			g.drawString("None",xoffset,46);
		}else{
			if (showText){
				Font newFont = new Font("SansSerif",1,14);
				g.setFont(newFont);
				fm = getFontMetrics(newFont);
				int index = actionText.indexOf(";");
				String actionText1 = null;
				String actionText2 = null;
				if (index > -1){
					actionText2 = actionText.substring(index + 1);
					actionText1 = actionText.substring(0,index);
				}
				g.setColor(StyleGuide.colorMapEditorGrid);
				if (actionText2 != null){
					xoffset = (d.width - fm.stringWidth(actionText1))/2;
					g.drawString(actionText1,xoffset,38);
					xoffset = (d.width - fm.stringWidth(actionText2))/2;
					g.drawString(actionText2,xoffset,58);
				}else{
					xoffset = (d.width - fm.stringWidth(actionText))/2;
					g.drawString(actionText,xoffset,46);
				}
			}
		}
	}
	
	/**
	 * String can contain one ";" to signal a line break
	 * @param newActionText
	 */
	@SuppressWarnings("deprecation")
	public void setMapActionText(String newActionText){
		actionText = newActionText;
		if (newActionText == null){
			cancelBtn.setVisible(false);
			// stop any active animation
			if (runner != null){
				runner.stop();
				runner = null;
				showText = true;
				repaint();
			}
		}else{
			cancelBtn.setVisible(true);
			repaint();
			// start a new animation
			runner = new Thread(this);
			runner.start();
		}
	}

	public void run() {
		for (;;){
			try {
				Thread.sleep(500);
				showText = false;
				repaint();
				Thread.sleep(250);
				showText = true;
				repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent arg0) {
		guiPanel.clearMapActions();
		runner.stop();
		runner = null;
		actionText = null;
		showText = true;
		repaint();
		cancelBtn.setVisible(false);
	}

}
