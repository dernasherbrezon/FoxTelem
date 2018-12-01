package gui;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * FOX 1 Telemetry Decoder
 * @author chris.e.thompson g0kla/ac2cz
 *
 * Copyright (C) 2015 amsat.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
@SuppressWarnings("serial")
class IsisAntennaStatusTableModel extends AbstractTableModel {
	String[] columnNames = null;
    private String[][] data = null;

    IsisAntennaStatusTableModel() {
		columnNames = new String[17];
		columnNames[0] = "Resets";
		columnNames[1] = "Uptime";
		columnNames[2] = "Armed";
		columnNames[3] = "Burn";
		columnNames[4] = "Switch Ignore";
		columnNames[5] = "ANT 1"; // B
		columnNames[6] = "Last"; // T
		columnNames[7] = "Dep"; // S
		columnNames[8] = "ANT 2"; // B
		columnNames[9] = "Last"; // T
		columnNames[10] = "Dep"; // S
		columnNames[11] = "ANT 3"; // B
		columnNames[12] = "Last"; // T
		columnNames[13] = "Dep"; // S
		columnNames[14] = "ANT 4"; // B
		columnNames[15] = "Last"; // T
		columnNames[16] = "Dep"; // S
	}
	
    public void setData(String[][] d) { 
    	data = d;
    	fireTableDataChanged();
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
    	if (data != null)
    		return data.length;
    	else 
    		return 0;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        
        return false;
        
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(String value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
}