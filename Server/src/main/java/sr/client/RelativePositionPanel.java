package sr.client;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Baspanel för utplacering av komponenter med fix storlek i en panel som ej kan
 * förstoras om.
 * @author bodinp
 *
 */
@SuppressWarnings("serial")
public class RelativePositionPanel extends JPanel {
	private int horisontalGap = 10;
	private int verticalGap = 10;

	protected void addUpperLeft(JComponent componentToPlace){
		componentToPlace.setLocation(horisontalGap,verticalGap);
		add(componentToPlace);
	}

	protected void addUpperLeft(JComponent componentToPlace, int customX, int customY){
		componentToPlace.setLocation(customX,customY);
		add(componentToPlace);
	}

	protected void addLowerRight(JComponent componentToPlace){
		componentToPlace.setLocation(getWidth()-horisontalGap-componentToPlace.getWidth(),getHeight()-verticalGap-componentToPlace.getHeight());
		add(componentToPlace);
	}

	protected void addLowerRight(JComponent componentToPlace, int customX, int customY){
		componentToPlace.setLocation(getWidth()-customX-componentToPlace.getWidth(),getHeight()-customY-componentToPlace.getHeight());
		add(componentToPlace);
	}

	/**
	 * AnvÃ¤nder default horisontalGap
	 * @param componentToPlace komponenten som skall lÃ¤ggas till i containern och som skall placeras ut
	 * @param componentAsHandle komponenten vars placering ligger som grund fÃ¶r var componentToPlace skall placeras 
	 */
	protected void addRight(JComponent componentToPlace, JComponent componentAsHandle){
		addRight(componentToPlace, componentAsHandle, horisontalGap);
	}

	protected void addRight(JComponent componentToPlace, JComponent componentAsHandle, int customHorisontalGap){
		int x = componentAsHandle.getLocation().x + componentAsHandle.getWidth() + customHorisontalGap; 
		int y = componentAsHandle.getLocation().y; 
		componentToPlace.setLocation(x,y);
		add(componentToPlace);
	}

	protected void addLeft(JComponent componentToPlace, JComponent componentAsHandle){
		addLeft(componentToPlace, componentAsHandle, horisontalGap);
	}

	protected void addLeft(JComponent componentToPlace, JComponent componentAsHandle, int customHorisontalGap){
		int x = componentAsHandle.getLocation().x - componentToPlace.getWidth() - customHorisontalGap; 
		int y = componentAsHandle.getLocation().y; 
		componentToPlace.setLocation(x,y);
		add(componentToPlace);
	}

	protected void addBelow(JComponent componentToPlace, JComponent componentAsHandle){
		addBelow(componentToPlace, componentAsHandle, verticalGap);
	}

	protected void addBelow(JComponent componentToPlace, JComponent componentAsHandle, int customVerticalGap){
		int x = componentAsHandle.getLocation().x; 
		int y = componentAsHandle.getLocation().y + componentAsHandle.getHeight() + customVerticalGap; 
		componentToPlace.setLocation(x,y);
		add(componentToPlace);
	}

	protected void addBelow(JComponent componentToPlace, JComponent componentAsHandle, int customVerticalGap, int xOffset){
		int x = componentAsHandle.getLocation().x + xOffset; 
		int y = componentAsHandle.getLocation().y + componentAsHandle.getHeight() + customVerticalGap; 
		componentToPlace.setLocation(x,y);
		add(componentToPlace);
	}

	protected void addAbove(JComponent componentToPlace, JComponent componentAsHandle){
		addAbove(componentToPlace, componentAsHandle, verticalGap);
	}

	protected void addAbove(JComponent componentToPlace, JComponent componentAsHandle, int customVerticalGap){
		int x = componentAsHandle.getLocation().x; 
		int y = componentAsHandle.getLocation().y - componentToPlace.getHeight() - customVerticalGap; 
		componentToPlace.setLocation(x,y);
		add(componentToPlace);
	}

	protected int getHorisontalGap() {
		return horisontalGap;
	}

	protected void setHorisontalGap(int horisontalGap) {
		this.horisontalGap = horisontalGap;
	}

	protected int getVerticalGap() {
		return verticalGap;
	}

	protected void setVerticalGap(int verticalGap) {
		this.verticalGap = verticalGap;
	}
	
}
