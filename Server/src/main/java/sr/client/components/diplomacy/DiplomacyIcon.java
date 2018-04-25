package sr.client.components.diplomacy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import sr.client.StyleGuide;
import sr.enums.DiplomacyIconState;
import sr.enums.DiplomacyLevel;
import sr.general.logging.Logger;

@SuppressWarnings("serial")
public class DiplomacyIcon extends JPanel implements MouseListener{
	private DiplomacyLevel level;
	private Color activeColor,passiveColor,disabledColor,selectedColor;
	private DiplomacyIconState state;
	private ActionListener listener;
	private boolean activePlayer;

	public DiplomacyIcon (DiplomacyLevel level, boolean activePlayer){
		this.level = level;
		this.activePlayer = activePlayer;
		setColors(level);
		addMouseListener(this);
	}

	private void setColors(DiplomacyLevel aLevel){
		activeColor = StyleGuide.getDiplomacyLevelColor(aLevel);
		passiveColor = activeColor;
		selectedColor = activeColor.brighter();
		disabledColor = StyleGuide.COLOR_DIPLOMACY_DISABLED;
	}
	
	public void addActionListener(ActionListener listener){
		this.listener = listener;
	}
	
	public void setState(DiplomacyIconState state) {
		Logger.finest("Sätt state på " + level + ": " + state);
		this.state = state;
	}

	public DiplomacyIconState getState() {
		return state;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		Dimension d = getSize();
		g.fillRect(0, 0, d.width, d.height);
		if (state == DiplomacyIconState.ACTIVE){
			g.setColor(activeColor);
			g.fillRect(0, 0, d.width, d.height);
			g.setColor(Color.BLACK);
		}else{
			if ((state == DiplomacyIconState.PASSIVE) | (state == DiplomacyIconState.PASSIVE_AND_SELECTED) | (state == DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED) | (state == DiplomacyIconState.PASSIVE_AND_SUGGESTED)){
				g.setColor(passiveColor);
			}else{
				g.setColor(disabledColor);
			}
			g.drawRect(0, 0, d.width-1, d.height-1);
			g.drawRect(1, 1, d.width-3, d.height-3);
		}
		g.setFont(new Font("Monospaced",0,11));
		FontMetrics fm = g.getFontMetrics();
		String levelStr = getLevelString();
		int strWidth = fm.stringWidth(levelStr);
		g.drawString(levelStr, ((d.width-strWidth)/2), (d.height/2)+4);
		if (activePlayer){
			if ((state == DiplomacyIconState.PASSIVE_AND_SELECTED) | (state == DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED)){
				g.setColor(selectedColor);
				g.drawOval(0, 0, d.width-1, d.height-1);
				g.drawOval(1, 1, d.width-3, d.height-3);
			}
			if ((state == DiplomacyIconState.PASSIVE_AND_SUGGESTED) | (state == DiplomacyIconState.PASSIVE_AND_SELECTED_AND_SUGGESTED)){
				g.setColor(selectedColor.darker());
				g.drawOval(2, 2, d.width-5, d.height-5);
				g.drawOval(3, 3, d.width-7, d.height-7);
			}
		}
	}
	
	private String getLevelString(){
		String levelString = level.toString();
		levelString = levelString.substring(0,3);
		levelString = levelString.toUpperCase();
		return levelString;
	}

	public void mousePressed(MouseEvent e) {
		if (state == DiplomacyIconState.PASSIVE){
			listener.actionPerformed(new ActionEvent(this,0,level.toString()));
		}
	}
	
	public DiplomacyLevel getLevel(){
		return level;
	}

	public void mouseClicked(MouseEvent e) {
		Logger.fine("mouseClicked: " + level.getName());
		listener.actionPerformed(new ActionEvent(this,-1,level.getName()));
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
