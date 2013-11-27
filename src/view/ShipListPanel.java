package view;

import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.event.TableModelListener;

import model.ship.Ship;
import model.ship.ShipEquipment;

public class ShipListPanel extends ListPanelWithRaces implements ActionListener, TableModelListener
{

	public ShipListPanel(MainContainer _parent)
	{
		super(_parent);
		// setTitle( getTitle()+" Ship List" );
		this.title = "Ship List";
	}

	@Override
	protected void initListTableModel()
	{
		// Ship list table
		int numberEquipments = ShipEquipment.getShipEquipments().size();
		Map<String, Ship> ships = Ship.getShips();
		this.createLTM(ships.size(), 7 + numberEquipments + 1);
		ltm.setEditable(false);

		int col = 0;
		ltm.setColumnName("Name", col);
		col++;
		ltm.setColumnName("Race", col);
		col++;
		ltm.setColumnName("Cost", col);
		col++;
		ltm.setColumnName("Turns Rate", col);
		col++;
		ltm.setColumnName("Trade Potential", col);
		col++;
//		ltm.setColumnName("Maneuverability", col);
//		col++;
		ltm.setColumnName("Hardpoints", col);
		col++;
		ltm.setColumnName("Power", col);
		col++;
//		ltm.setColumnName("Shields", col);
//		col++;
//		ltm.setColumnName("Armour", col);
//		col++;
//		ltm.setColumnName("Cargo", col);
//		col++;
//		ltm.setColumnName("Combats", col);
//		col++;
//		ltm.setColumnName("Scouts", col);
//		col++;
//		ltm.setColumnName("Mines", col);
//		col++;

		ShipEquipment[] shipEquips = ShipEquipment.getShipEquipments().values().toArray(new ShipEquipment[1]);
		for (ShipEquipment se : shipEquips) {
			ltm.setColumnName(se.getName(), col);
			col++;
		}

//		ShipAbility[] shipAbils = ShipAbility.getShipAbilities().values().toArray(new ShipAbility[1]);
//		ltm.setColumnName("Abilities", col);
//		col++;
		ltm.setColumnName("Restrictions", col);

		int row = 0;
		for (Ship s : ships.values()) {
			col = 0;
			ltm.setValueAt(s.getName(), row, col);
			col++;
			ltm.setValueAt(s.getRaceName(), row, col);
			col++;
			ltm.setValueAt(s.getCost(), row, col);
			col++;
			ltm.setValueAt(s.getTurnRate(), row, col);
			col++;
			ltm.setValueAt(s.getTradePotential(), row, col);
			col++;
//			ltm.setValueAt(s.getManu(), row, col);
//			col++;
			ltm.setValueAt(s.getHardpoints(), row, col);
			col++;
			ltm.setValueAt(s.getPower(), row, col);
			col++;
//			ltm.setValueAt(s.getShields(), row, col);
//			col++;
//			ltm.setValueAt(s.getArmour(), row, col);
//			col++;
//			ltm.setValueAt(s.getCargo(), row, col);
//			col++;
//			ltm.setValueAt(s.getCombats(), row, col);
//			col++;
//			ltm.setValueAt(s.getScouts(), row, col);
//			col++;
//			ltm.setValueAt(s.getMines(), row, col);
//			col++;

			Map<ShipEquipment,Integer> shipEq = s.getEquipments();
			for (ShipEquipment se : shipEquips)
			{
				if (shipEq.containsKey(se))
					ltm.setValueAt(shipEq.get(se),row,col);
				else
					ltm.setValueAt(0, row, col);
//				if (shipEq.contains(shipEquips[i]))
//					ltm.setValueAt(true, row, col);
//				else
//					ltm.setValueAt(false, row, col);
				col++;
			}
//			ArrayList<ShipAbility> shipAb = s.getAbilities();
//			String abilities = "";
//			for (int i = 0; i < shipAbils.length; i++)
//			{
//				if (shipAb.contains(shipAbils[i]))
//					abilities += shipAbils[i].getName() + ", ";
//			}
//			if (abilities.length() > 0)
//				abilities = abilities.substring(0, abilities.length() - 2);
//			ltm.setValueAt(abilities, row, col);
//			col++;
			ltm.setValueAt(s.getRestriction(), row, col);

			row++;
		}
	}
}
