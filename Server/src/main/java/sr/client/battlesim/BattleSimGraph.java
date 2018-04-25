package sr.client.battlesim;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import sr.client.StyleGuide;
import sr.client.components.SRBasePanel;

public class BattleSimGraph extends SRBasePanel{
	private static final long serialVersionUID = 1L;
	private List<Integer> points;
	private int iterations;
	
	public void setPointsList(List<Integer> newPoints){
		points = newPoints;
	}
	
	public void drawGraph(int newIterations){
		iterations = newIterations;
		paintComponent(getGraphics());
	}

	public void paintComponent(Graphics g){
		// create buffer
		Dimension d = getSize();
		Image bufferImage = createImage(d.width,d.height);
		Graphics bg = bufferImage.getGraphics();
		// draw graphics
		// draw background
		bg.setColor(Color.BLACK);
		bg.fillRect(0, 0, d.width, d.height);
		// draw 50% line
		bg.setColor(StyleGuide.colorCurrent.darker().darker());
		bg.drawLine(0,50,d.width,50);
		// draw graph
		if (points != null){
			bg.setColor(StyleGuide.colorCurrent);
			int i = 0;
			while ((i < d.width) & (i < points.size())){
				int x = d.width - i;
				int y = points.get(i);
				bg.drawLine(x,y,x,y);
				i++;
			}
		}
		// draw iterations
		bg.drawString("#:" + String.valueOf(iterations), d.width-43, 93);
		// draw border
		bg.setColor(StyleGuide.colorCurrent);
		bg.drawRect(0, 0, d.width-1, d.height-1);
		// draw buffer
		g.drawImage(bufferImage,0,0,this);
	}
	
}
