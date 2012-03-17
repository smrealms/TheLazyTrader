package view;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.NavigableMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import model.Race;
import model.preferences.PlayerPreferences;

public class PlayerListPanel extends ListPanel implements ActionListener, TableModelListener
{

	public PlayerListPanel(MainContainer _parent)
	{
		super(_parent);
		this.title = "Player Settings";
	}

	@Override
	protected void initListTableModel()
	{
		// Nearest list table
		NavigableMap<Integer, Race> races = Race.getRaces();
		createLTM(races.size(), 2);

		ltm.setColumnName("Race", 0);
		ltm.setColumnName("Relations", 1);

		Iterator<Integer> rIter = races.keySet().iterator();
		int row = 0;
		while (rIter.hasNext())
		{
			int raceId = rIter.next();
			int col = 0;
			ltm.setValueAt(races.get(raceId), row, col);
			col++;
			ltm.setValueAt(PlayerPreferences.getRelationsForRace(raceId), row, col);
			row++;
		}

		ltm.addTableModelListener(this);
	}

	public void tableChanged(TableModelEvent e)
	{
		if (e.getSource() == this.ltm)
		{
			PlayerPreferences.setRelation(((Race) this.ltm.getRealValueAt(e.getFirstRow(), 0)).getId(), (Integer) this.ltm.getRealValueAt(e.getFirstRow(), 1));
		}
	}

	public void rowSelected()
	{
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	public void mouseReleased(MouseEvent e)
	{
		if(e.getSource()==this.list)
			rowSelected();
	}
}
