package sr.client.components;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SRTableModel extends AbstractTableModel {

	private int rows=5, columns=10;
	public SRTableModel() {
		super();
	}

	public int getRowCount() {
		return rows;
	}

	public int getColumnCount() {
		return columns;
	}

	public Object getValueAt(int arg0, int arg1) {
		return null;
	}

}
