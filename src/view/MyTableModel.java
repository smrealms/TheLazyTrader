package view;

import java.text.DecimalFormat;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel
{
	private final String[] columnNames;
	private final Object[][] data;
	private boolean editable = true;
	private final DecimalFormat dfInt = new DecimalFormat("#,##0");
	private final DecimalFormat dfReal = new DecimalFormat("#,##0.###");

	public MyTableModel(int _rowCount, int _columnCount)
	{
		this.columnNames = new String[_columnCount];
		this.data = new Object[_rowCount][_columnCount];
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return data.length;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Object value = data[row][col];
		if(value==null)
			return value;
		if (value.getClass() == Byte.class || value.getClass() == Short.class || value.getClass() == Integer.class || value.getClass() == Long.class)
			value = dfInt.format(value);
		else if (value.getClass() == Float.class || value.getClass() == Double.class)
			value = dfReal.format(value);
		return value;
	}

	public Object getRealValueAt(int row, int col)
	{
		return data[row][col];
	}

	public void setColumnName(String value, int col)
	{
		columnNames[col] = value;
	}

	@Override
	public void setValueAt(Object newValue, int row, int col)
	{
		if(newValue.getClass() == String.class)
		{
			newValue = ((String)newValue).replaceAll(",", "");
			Class c = getRealColumnClass(col);
			if (c == Byte.class)
				newValue = Byte.parseByte((String)newValue);
			else if (c == Short.class)
				newValue = Short.parseShort((String)newValue);
			else if (c == Integer.class)
				newValue = Integer.parseInt((String)newValue);
			else if (c == Long.class)
				newValue = Long.parseLong((String)newValue);
			else if (c == Float.class)
				newValue = Float.parseFloat((String)newValue);
			else if (c == Double.class)
				newValue = Double.parseDouble((String)newValue);
		}
		data[row][col] = newValue;
		fireTableCellUpdated(row, col);
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class getColumnClass(int c)
	{
		Object o = getValueAt(0, c);
		if(o==null)
			return null;
		return o.getClass();
	}

	@SuppressWarnings("unchecked")
	public Class getRealColumnClass(int c)
	{
		Object o = getRealValueAt(0, c);
		if(o==null)
			return null;
		return o.getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return this.editable;
	}

	public void setEditable(boolean value)
	{
		this.editable = value;
	}

	public boolean getEditable()
	{
		return this.editable;
	}
}