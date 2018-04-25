/*
 * Created on 2005-jan-02
 */
package sr.client.interfaces;

/**
 * @author WMPABOD
 *
 * Interface for components that are scrollable and listens to events from a ScrollList
 */
public interface ScrollListListener {

	public void move(boolean up);

	public void show(double position);

}