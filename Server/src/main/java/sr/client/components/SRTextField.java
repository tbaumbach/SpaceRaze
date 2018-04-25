package sr.client.components;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: Wrapper around JTextField to add SpaceRaze looks</p>
 * @author Paul Bodin
 * @version 3.0
 */

@SuppressWarnings("serial")
public class SRTextField extends JTextField {

  public SRTextField() {
    this("");
  }

  public SRTextField(String text) {
    super(text);
    setBackground(StyleGuide.colorBackground);
    setForeground(StyleGuide.colorCurrent);
    setBorder(new LineBorder(StyleGuide.colorCurrent));
    setCaretColor(StyleGuide.colorCurrent);
    setSelectedTextColor(StyleGuide.colorCurrent.darker());
    setSelectionColor(StyleGuide.colorCurrent.brighter());
    this.setDisabledTextColor(StyleGuide.colorCurrent);
  }

}