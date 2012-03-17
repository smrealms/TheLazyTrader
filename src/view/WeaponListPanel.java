package view;

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.event.TableModelListener;

import model.Race;
import model.Weapon;
import model.ship.Restriction;

public class WeaponListPanel extends ListPanelWithRaces implements ActionListener, TableModelListener
{

	public WeaponListPanel(MainContainer _parent)
	{
		super(_parent);
		// setTitle( getTitle()+" Weapon List" );
		this.title = "Weapon List";
	}

	protected void initListTableModel()
	{
		// Weapon list table
		Map<String, Weapon> weapons = Weapon.getWeapons();
		this.createLTM(weapons.size(), 9);
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
		ltm.setColumnName("EMP", col);
		col++;
		ltm.setColumnName("Restrictions", col);

		Iterator<Weapon> iter = weapons.values().iterator();
		int row = 0;
		while (iter.hasNext())
		{
			Weapon w = iter.next();
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
			ltm.setValueAt(w.getEmpDamage() + (w.isEmpInPercent() ? "%" : ""), row, col);
			col++;
			String rString = "";
			Iterator<Restriction> rIter = w.getRestrictions().iterator();
			while (rIter.hasNext())
			{
				rString += rIter.next() + ", ";
			}
			if (rString.length() > 0)
				rString = rString.substring(0, rString.length() - 2);
			ltm.setValueAt(rString, row, col);

			row++;
		}
	}
}
