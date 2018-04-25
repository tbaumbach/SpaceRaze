package sr.client.components;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import sr.client.StyleGuide;

/**
 * <p>Title: SpaceRaze 3.0</p>
 * <p>Description: Wrapper around JTable to add SpaceRaze looks</p>
 * @author Ragnar Klinga
 * @version 1.2
 */

@SuppressWarnings("serial")
public class SRTable extends JTable {

  public SRTable() {
    super();
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }

  public SRTable(int numRows, int numCols) {
    super(numRows, numCols);
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }

  public SRTable(Object [][] rowData, Object [] columnNames) {
    super(rowData, columnNames);
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }

  public SRTable(TableModel dm) {
    super(dm);
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }

  public SRTable(TableModel dm, TableColumnModel cm) {
    super(dm, cm);
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }

  public SRTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
    super(dm, cm, sm);
    setForeground(StyleGuide.colorCurrent);
    setBackground(StyleGuide.colorBackground);
  }
  
  @SuppressWarnings("unchecked")
  public SRTable(Vector rowData, Vector columnNames) {
	  super(rowData, columnNames);
	  setForeground(StyleGuide.colorCurrent);
	  setBackground(StyleGuide.colorBackground);
  }
  
  public boolean isCellEditable(int rowIndex, int vColIndex) {
      return false;
  }
  
}