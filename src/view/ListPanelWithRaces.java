package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import model.Race;

public abstract class ListPanelWithRaces extends ListPanel implements TableModelListener
{
	protected JTable selectRaces;
	protected Map<Integer, Boolean> races;

	public ListPanelWithRaces(MainContainer _parent)
	{
		super(_parent);
	}

	abstract protected void initListTableModel();

	protected void initComponents()
	{
		super.initComponents();
		initRaceTable();
	}

	protected void initRaceTable()
	{
		races = new HashMap<Integer, Boolean>(Race.getNumberOfRaces());
		// Races table
		rtm = new MyTableModel(1, Race.getNumberOfRaces());
		int i = 0;
		for (Entry<Integer, Race> race : Race.getRaces().entrySet()) {
			rtm.setColumnName(race.getValue().getName(), i);
			rtm.setValueAt(true, 0, i);
			races.put(race.getKey(), true);
			i++;
		}
		rtm.addTableModelListener(this);
		this.selectRaces = new JTable(rtm);
		this.selectRaces.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	protected void addComponents()
	{
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gbl);

		c.gridwidth = 0;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		// First row
		c.ipady = 36;
		JScrollPane jsp = new JScrollPane(this.selectRaces);
		gbl.setConstraints(jsp, c);
		add(jsp);
		// Last Row
		c.weighty = 1000;
		jsp = new JScrollPane(this.list);
		gbl.setConstraints(jsp, c);
		add(jsp);
	}

	protected void initActionsFileMenu()
	{
		jmiActionOptions = new JMenuItem[1];
		jmiActionOptions[0] = new JMenuItem("Not thought of any actions for this..");
		jmiActionOptions[0].setMnemonic('E'); // Set E as the shortcut key
		// jmiActionOptions[1] = new JMenuItem("Generate Money Routes");
		// jmiActionOptions[1].setMnemonic('M'); // Set M as the shortcut key
		// jmiActionOptions[2] = new JMenuItem("Save Exp Routes");
		// jmiActionOptions[2].setMnemonic('S'); // Set S as the shortcut key
		// jmiActionOptions[3] = new JMenuItem("Save Money Routes");
		// jmiActionOptions[3].setMnemonic('A'); // Set A as the shortcut key
	}

	public void tableChanged(TableModelEvent e)
	{
		checkForStandardTableChanged(e);
	}

	protected boolean checkForStandardTableChanged(TableModelEvent e)
	{
		if (e.getSource() == this.selectRaces.getModel())
		{
			this.races.put(Race.getId(this.selectRaces.getColumnName(e.getColumn())), (Boolean) this.selectRaces.getValueAt(e.getFirstRow(), e.getColumn()));
			filterTableByRace();
			return true;
		}
		return false;
	}

	protected void filterTable(String filterString)
	{
		RowFilter<MyTableModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try
		{
			rf = RowFilter.regexFilter(filterString);
		}
		catch (java.util.regex.PatternSyntaxException e)
		{
			return;
		}
		sorter.setRowFilter(rf);
	}

	protected void filterTableByRace()
	{
		String filterString = "";
		for (int raceId : this.races.keySet())
		{
			if (this.races.get(raceId))
				filterString += "^" + Race.getName(raceId) + "$|";
		}
		if (filterString.length() > 0)
			filterString = filterString.substring(0, filterString.length() - 1);
		filterTable(filterString);
	}
}