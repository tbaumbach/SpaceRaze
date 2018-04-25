/*
 * Created on 2005-jun-18
 */
package sr.mapeditor;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;
import sr.client.components.SRButton;
import sr.client.components.SRLabel;

/**
 * @author WMPABOD
 *
 * This panels contain the controls for the map 
 */
@SuppressWarnings("serial")
public class MapControlsPanel extends JPanel implements ActionListener{
	private SRButton zoominbtn,zoomoutbtn,moverightbtn,moveleftbtn,moveupbtn,movedownbtn,resetBtn;
	private SRLabel movelbl,zoomLevelLbl;
	private MapPanel map;

	public MapControlsPanel(MapPanel map){
		this.map = map;
		setBorder(new LineBorder(StyleGuide.colorNeutral));
	    setLayout(null);

	    // to correct x position of all components
	    int y1 = -46;
	    int y = -15;

	    movelbl = new SRLabel("Move map:");
	    movelbl.setBounds(35,52+y1,70,20);
	    add(movelbl);

	    moverightbtn = new SRButton("Right");
	    moverightbtn.setBounds(68,95+y1,40,20);
	    moverightbtn.setVisible(true);
	    moverightbtn.addActionListener(this);
	    add(moverightbtn);

	    moveleftbtn = new SRButton("Left");
	    moveleftbtn.setBounds(18,95+y1,40,20);
	    moveleftbtn.setVisible(true);
	    moveleftbtn.addActionListener(this);
	    add(moveleftbtn);

	    moveupbtn = new SRButton("Up");
	    moveupbtn.setBounds(43,75+y1,40,20);
	    moveupbtn.setVisible(true);
	    moveupbtn.addActionListener(this);
	    add(moveupbtn);

	    movedownbtn = new SRButton("Down");
	    movedownbtn.setBounds(43,115+y1,40,20);
	    movedownbtn.setVisible(true);
	    movedownbtn.addActionListener(this);
	    add(movedownbtn);

	    zoomLevelLbl = new SRLabel("Zoom level: x" + map.getScaleFromZoom());
	    zoomLevelLbl.setBounds(8,111+y,120,20);
	    add(zoomLevelLbl);

	    zoominbtn = new SRButton("Zoom in");
	    zoominbtn.setBounds(8,134+y,50,20);
//	    zoominbtn.setVisible(true);
	    zoominbtn.addActionListener(this);
	    add(zoominbtn);

	    zoomoutbtn = new SRButton("Zoom out");
	    zoomoutbtn.setBounds(68,134+y,50,20);
	    zoomoutbtn.setFont(new Font("Dialog",0,11));
//	    zoomoutbtn.setVisible(true);
	    zoomoutbtn.addActionListener(this);
	    zoomoutbtn.setEnabled(false);
	    add(zoomoutbtn);

	    resetBtn = new SRButton("Reset");
	    resetBtn.setBounds(8,165+y,110,20);
	    resetBtn.addActionListener(this);
	    add(resetBtn);
}
	
	public void paintComponent(Graphics g){
		g.setColor(StyleGuide.colorBackground);
		g.fillRect(0,0,getSize().width,getSize().height);
	}

	private void checkZoomBtns(){
		zoominbtn.setEnabled(map.canZoomIn());
		zoomoutbtn.setEnabled(map.canZoomOut());
		zoomLevelLbl.setText("Zoom level: x" + map.getScaleFromZoom());
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == zoominbtn){
			map.incZoom();
			checkZoomBtns();
		}else
		if (ae.getSource() == zoomoutbtn){
			map.decZoom();
			checkZoomBtns();
		}else
		if (ae.getSource() == moverightbtn){
			map.moveRight();
		}else
		if (ae.getSource() == moveleftbtn){
			map.moveLeft();
		}else
		if (ae.getSource() == moveupbtn){
			map.moveUp();
		}else
		if (ae.getSource() == movedownbtn){
			map.moveDown();
		}else
		if (ae.getSource() == resetBtn){
			map.reset();
			checkZoomBtns();
		}
	}

}
