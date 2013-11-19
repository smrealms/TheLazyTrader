package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;

public abstract class ListPanel extends TheLazyTraderPanel implements ActionListener
{
	protected JTable list;
	protected MyTableRowSorter<MyTableModel> sorter;
	protected MyTableModel rtm;
	protected MyTableModel ltm;

	public ListPanel(MainContainer _parent)
	{
		super(_parent);
	}

	abstract protected void initListTableModel();

	@Override
	protected void initComponents()
	{
		initListTableModel();
		initListTable();
	}

	protected void initListTable()
	{
		this.list = new JTable(ltm);
		this.list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		sorter = new MyTableRowSorter<MyTableModel>(ltm);
		this.list.setRowSorter(sorter);
	}

	@Override
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
		// Only Row
		c.weighty = 1000;
		JScrollPane jsp = new JScrollPane(this.list);
		gbl.setConstraints(jsp, c);
		add(jsp);
	}

	@Override
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.standardActionChecks(e))
			return;
		else if (e.getSource() == jmiActionOptions[0])
		{
		}
	}// end of actionPerformed

	protected void filterTable(String filterString)
	{
		// If current expression doesn't parse, don't update.
		try
		{
			RowFilter<MyTableModel, Object> rf = RowFilter.regexFilter(filterString);
			sorter.setRowFilter(rf);
		}
		catch (java.util.regex.PatternSyntaxException e)
		{
		}
	}

	protected void createLTM(int rows, int cols)
	{
		ltm = new MyTableModel(rows, cols);
	}
}