package view;

import java.awt.event.MouseEvent;
import java.util.Map.Entry;
import java.util.NavigableMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import model.Race;
import model.preferences.PlayerPreferences;

public class PlayerListPanel extends ListPanel implements TableModelListener
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

		int row = 0;
		for (Entry<Integer, Race> race : races.entrySet()) {
			int col = 0;
			ltm.setValueAt(race.getValue(), row, col);
			col++;
			ltm.setValueAt(PlayerPreferences.getRelationsForRace(race.getKey()), row, col);
			row++;
		}

		ltm.addTableModelListener(this);
	}

	@Override
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
