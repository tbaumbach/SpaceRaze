/*
 * Created on 2005-feb-25
 */
package sr.client.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.general.logging.Logger;

/**
 * @author wmpabod
 *
 * Base class for all popup panels in SpaceRaze
 */
@SuppressWarnings("serial")
public abstract class BasicPopupPanel extends SRBasePanel implements ActionListener, MouseListener {
	protected SRButton okBtn, cancelBtn;
	private String title;
	private JPanel eventPanel;
	private JRootPane rootPane;
	private String selection;

	public BasicPopupPanel(String title, ActionListener listener){
		super();
		this.title = title;
	    setBorder(new LineBorder(StyleGuide.colorCurrent));

	    cancelBtn = new SRButton("Cancel");
	    if (listener != null){
	    	cancelBtn.addActionListener(listener);
	    }
	    cancelBtn.addActionListener(this);
	    add(cancelBtn);

	    okBtn = new SRButton("Ok");
	    if (listener != null){
	    	okBtn.addActionListener(listener);
	    }
	    okBtn.addActionListener(this);
	    add(okBtn);
	}
	
	public void setPopupSize(int width, int height){
		setSize(new Dimension(width,height));
	    cancelBtn.setBounds((width/2)-120,height-35,100,20);
		okBtn.setBounds((width/2)+20,height-35,100,20);
	}
	
	public void open(JPanel creatorPanel){
		// get layered pane
		rootPane = creatorPanel.getRootPane();
		JLayeredPane layeredPane = rootPane.getLayeredPane();
		// compute and set location
		Dimension d = getSize();
		int xcoor = (rootPane.getSize().width - d.width)/2;
		int ycoor = (rootPane.getSize().height - d.height)/2;
		setLocation(xcoor,ycoor);
		// add an opaque eventstopper
		eventPanel = new JPanel();
		eventPanel.setBounds(0,0,rootPane.getSize().width,rootPane.getSize().height);
		eventPanel.setOpaque(false);
		eventPanel.addMouseListener(this);
//		layeredPane.add(eventPanel,JLayeredPane.MODAL_LAYER);
		layeredPane.add(eventPanel,JLayeredPane.PALETTE_LAYER);
		// add the popup
//		layeredPane.add(this,JLayeredPane.POPUP_LAYER);
		layeredPane.add(this,JLayeredPane.MODAL_LAYER);
		// update
		paintComponent(getGraphics());
		paintChildren(getGraphics());
	}
	
	protected void clearPopup(){
		JLayeredPane layeredPane = rootPane.getLayeredPane();
		setVisible(false);
		layeredPane.remove(this);
		eventPanel.setVisible(false);
		layeredPane.remove(eventPanel);
	}
	
	public void paintComponent(Graphics g){
		Color darker = StyleGuide.colorCurrent.darker().darker().darker();
		Dimension d = getSize();
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,d.width,d.height);
		g.setColor(darker);
		g.drawRect(2,2,d.width-5,d.height-5);
		g.fillRect(4,4,d.width-8,20);
		g.setColor(StyleGuide.colorCurrent);
		g.setFont(StyleGuide.buttonFont);
		g.drawString(title,10,18);
	}
	
	public void actionPerformed(ActionEvent ae){
		Logger.fine("BasicPopupPanel actionPerformed: " + ae.getActionCommand());
		selection = ae.getActionCommand();
		clearPopup();
	}
	
	public String getSelection(){
		return selection;
	}
	
	public void mousePressed(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
