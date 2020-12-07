package view;

import java.util.Map;

import model.Race;
import model.Weapon;
import model.ship.Restriction;

public class WeaponListPanel extends ListPanelWithRaces
{

	public WeaponListPanel(MainContainer _parent)
	{
		super(_parent);
		// setTitle( getTitle()+" Weapon List" );
		this.title = "Weapon List";
	}

	@Override
	protected void initListTableModel()
	{
		// Weapon list table
		Map<String, Weapon> weapons = Weapon.getWeapons();
		this.createLTM(weapons.size(), 8);
		ltm.setEditable(false);

		int col = 0;
		ltm.setColumnName("Name", col);
		col++;
		ltm.setColumnName("Race", col);
		col++;
		ltm.setColumnName("Cost", col);
		col++;
		ltm.setColumnName("Shield", col);
		col++;
		ltm.setColumnName("Armour", col);
		col++;
		ltm.setColumnName("Accuracy", col);
		col++;
		ltm.setColumnName("Power Level", col);
		col++;
		ltm.setColumnName("Restrictions", col);

		int row = 0;
		for (Weapon w : weapons.values()) {
			col = 0;
			ltm.setValueAt(w.getName(), row, col);
			col++;
			ltm.setValueAt(Race.getName(w.getRace()), row, col);
			col++;
			ltm.setValueAt(w.getCost(), row, col);
			col++;
			ltm.setValueAt(w.getShieldDamage(), row, col);
			col++;
			ltm.setValueAt(w.getArmourDamage(), row, col);
			col++;
			ltm.setValueAt(w.getAccuracy(), row, col);
			col++;
			ltm.setValueAt(w.getPowerLevel(), row, col);
			col++;
			String rString = "";
			for (Restriction r : w.getRestrictions()) {
				rString += r + ", ";
			}
			if (rString.length() > 0)
				rString = rString.substring(0, rString.length() - 2);
			ltm.setValueAt(rString, row, col);

			row++;
		}
	}
}
