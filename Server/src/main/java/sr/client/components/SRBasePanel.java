package sr.client.components;

import java.awt.LayoutManager;

import sr.client.RelativePositionPanel;
import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: SpaceRaze with Swing GUI</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRBasePanel extends RelativePositionPanel {

  public SRBasePanel() {
    this(null,false);
  }

  public SRBasePanel(LayoutManager layout, boolean isDoubleBuffered) {
    super();
    setLayout(layout);
    setDoubleBuffered(isDoubleBuffered);
    setBackground(StyleGuide.colorBackground);
  }

  public SRBasePanel(LayoutManager layout) {
    this(layout,false);
  }

  public SRBasePanel(boolean isDoubleBuffered) {
    this(null,isDoubleBuffered);
  }

}