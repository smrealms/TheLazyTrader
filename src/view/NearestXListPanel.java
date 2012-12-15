package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import model.Distance;
import model.Good;
import model.Location;
import model.Sector;
import model.Weapon;
import model.ship.Ship;
import model.ship.ShipEquipment;
import view.swing.tree.FilterableTreeNode;
import view.swing.tree.FilteredTreeModel;
import controller.pathfinding.Pathfinding;
import javax.swing.JTree;

import utils.swing.JIntegerField;

public class NearestXListPanel extends ListPanelWithRaces implements ActionListener, TableModelListener, TreeSelectionListener, MouseListener, KeyListener
{
	protected JTree itemsTree;
	protected JIntegerField sectorBox;
	protected JIntegerField plotToBox;
	protected FilterableTreeNode root;
	protected FilteredTreeModel treeModel;
	protected JTextArea routeDisplay;
	protected Distance[] storedDistances;
	protected Object lastSearchedObject;

	public NearestXListPanel(MainContainer _parent)
	{
		super(_parent);
		this.title = "Nearest Location List";
	}

	@Override
	protected void initComponents()
	{
		super.initComponents();

		sectorBox = new JIntegerField(1);
		sectorBox.addKeyListener(this);
		plotToBox = new JIntegerField();
		plotToBox.addKeyListener(this);
		
		routeDisplay = new JTextArea();

		FilterableTreeNode shipEqs = new FilterableTreeNode("ShipEquipment");
		Iterator<String> iter = ShipEquipment.getShipEquipments().keySet().iterator();
		while (iter.hasNext())
		{
			shipEqs.add(new FilterableTreeNode(ShipEquipment.getShipEquipment(iter.next()), false));
		}


		FilterableTreeNode shipsByRace = new FilterableTreeNode("Ships");
		FilterableTreeNode shipRaceNode = new FilterableTreeNode("All Ships");
		Iterator<Ship> shipsIter = Ship.getShips().values().iterator();
		while (shipsIter.hasNext())
		{
			shipRaceNode.add(new FilterableTreeNode(shipsIter.next(), false));
		}
		shipsByRace.add(shipRaceNode);
		iter = Ship.getShipsByRace().keySet().iterator();
		while (iter.hasNext())
		{
			String raceName = iter.next();
			shipRaceNode = new FilterableTreeNode(raceName);
			shipsIter = Ship.getShipsByRace(raceName).values().iterator();
			while (shipsIter.hasNext())
			{
				shipRaceNode.add(new FilterableTreeNode(shipsIter.next(), false));
			}
			shipsByRace.add(shipRaceNode);
		}


		FilterableTreeNode weaponsByPower = new FilterableTreeNode("Weapons");
		FilterableTreeNode weaponPowerNode = new FilterableTreeNode("All Levels");
		Iterator<Weapon> weaponsIter = Weapon.getWeapons().values().iterator();
		while (weaponsIter.hasNext())
		{
			weaponPowerNode.add(new FilterableTreeNode(weaponsIter.next(), false));
		}
		weaponsByPower.add(weaponPowerNode);
		Iterator<Integer> intIter = Weapon.getWeaponsByPower().keySet().iterator();
		while (intIter.hasNext())
		{
			int weaponPowerLevel = intIter.next();
			weaponPowerNode = new FilterableTreeNode("Level " + weaponPowerLevel);
			weaponsIter = Weapon.getWeaponsByPower(weaponPowerLevel).values().iterator();
			while (weaponsIter.hasNext())
			{
				weaponPowerNode.add(new FilterableTreeNode(weaponsIter.next(), false));
			}
			weaponsByPower.add(weaponPowerNode);
		}


		FilterableTreeNode locationsByType = new FilterableTreeNode("Locations");
		iter = Location.getLocationsByType().keySet().iterator();
		while (iter.hasNext())
		{
			String locationTypeName = iter.next();
			FilterableTreeNode locationTypeNode = new FilterableTreeNode(Location.getLocationByType(locationTypeName).firstEntry().getValue().getType());
			Iterator<String> locationsOfTypeIter = Location.getLocationByType(locationTypeName).keySet().iterator();
			while (locationsOfTypeIter.hasNext())
			{
				locationTypeNode.add(new FilterableTreeNode(Location.getLocation(locationsOfTypeIter.next()), false));
			}
			locationsByType.add(locationTypeNode);
		}


		FilterableTreeNode goodsBought = new FilterableTreeNode("Bought");
		FilterableTreeNode goodsSold = new FilterableTreeNode("Sold");
		FilterableTreeNode goodsEither = new FilterableTreeNode("Either");
		intIter = Good.getNames().keySet().iterator();
		while (intIter.hasNext())
		{
			int goodID = intIter.next();
			goodsBought.add(new FilterableTreeNode(new Good(goodID,Good.BUYS), false));
			goodsSold.add(new FilterableTreeNode(new Good(goodID,Good.SELLS), false));
			goodsEither.add(new FilterableTreeNode(new Good(goodID,Good.ADD_BUY_SELL), false));
		}
		FilterableTreeNode goodsByType = new FilterableTreeNode("Goods");
		goodsByType.add(goodsBought);
		goodsByType.add(goodsSold);
		goodsByType.add(goodsEither);
		
		root = new FilterableTreeNode("Nearest X");
		root.add(shipEqs);
		root.add(shipsByRace);
		root.add(weaponsByPower);
		root.add(locationsByType);
		root.add(goodsByType);

		treeModel = new FilteredTreeModel(root);
		itemsTree = new JTree(treeModel);
		itemsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		itemsTree.addTreeSelectionListener(this);
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
		// First row
		c.ipady = 36;
		JScrollPane jsp = new JScrollPane(this.selectRaces);
		gbl.setConstraints(jsp, c);
		add(jsp);
		// Second row
		c.ipady = 18;
		c.weighty = 1;
		
		JPanel sectorsPanel = new JPanel();
		sectorsPanel.add(createLabelJComponentPair("Sector: ", this.sectorBox));
		sectorsPanel.add(createLabelJComponentPair("Plot to: ", this.plotToBox));
		
		jsp = new JScrollPane(sectorsPanel);
		gbl.setConstraints(jsp, c);
		add(jsp);
		// Third row
		c.weighty = 500;
		jsp = new JScrollPane(this.itemsTree);
		gbl.setConstraints(jsp, c);
		add(jsp);
		// Fourth Row
		c.weighty = 1000;
		jsp = new JScrollPane(this.list);
		gbl.setConstraints(jsp, c);
		add(jsp);
		// Last Row
		c.weighty = 1;
		c.ipady = 40;
		jsp = new JScrollPane(this.routeDisplay);
		gbl.setConstraints(jsp, c);
		add(jsp);
	}

	@Override
	protected void initListTableModel()
	{
		// Nearest list table
		createLTM(1);
		int col = 0;
		ltm.setValueAt("", 0, col);
		col++;
		ltm.setValueAt("", 0, col);
		col++;
		ltm.setValueAt("", 0, col);
		col++;
		ltm.setValueAt("", 0, col);
		ltm.addTableModelListener(this);
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		FilterableTreeNode node = (FilterableTreeNode) itemsTree.getLastSelectedPathComponent();
		if (node == null)
			return;
		Object selectedObj = node.getUserObject();
//		if (selectedObj instanceof String)
//			return;
		doPlotNearest(selectedObj);
	}
	
	protected void doPlotNearest(Object x)
	{
		this.lastSearchedObject = x;
		if(this.sectorBox.hasValue())
			if (this.checkSectorsFileOpened())
			{
				Map<Integer, Distance> distances = Pathfinding.findDistanceToX(x, FileLocate.getUniverseParser().getUniverse().getSectors(), (int) this.sectorBox.getValue(), Long.MIN_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, false);
				if (distances.size() > 0)
				{
					this.storedDistances = distances.values().toArray(new Distance[1]);
					Arrays.sort(this.storedDistances);
					createLTM(this.storedDistances.length);
					for (int i = 0; i < this.storedDistances.length; i++)
					{
						int col = 0;
						ltm.setValueAt(this.storedDistances[i].getDistance() + " (" + this.storedDistances[i].getNumWarps() + " warps)", i, col);
						col++;
						ltm.setValueAt(this.storedDistances[i].getTurns(), i, col);
						col++;
						ltm.setValueAt(this.storedDistances[i].getNumWarps(), i, col);
						col++;
						ltm.setValueAt(this.storedDistances[i].getEndSectorId(), i, col);
					}
				}
				else
				{
					createLTM(1);
					int col = 0;
					ltm.setValueAt("None Found", 0, col);
					col++;
					ltm.setValueAt("None Found", 0, col);
					col++;
					ltm.setValueAt("None Found", 0, col);
					col++;
					ltm.setValueAt("None Found", 0, col);
	
				}
				sorter = new MyTableRowSorter<MyTableModel>(ltm);
	
				ltm.addTableModelListener(this);
				this.list.setModel(ltm);
				this.list.setRowSorter(sorter);
				this.list.addMouseListener(this);
			}
	}

	protected void createLTM(int rows)
	{
		createLTM(rows, 4);
		ltm.setEditable(false);

		int col = 0;
		ltm.setColumnName("Distance", col);
		col++;
		ltm.setColumnName("Turns", col);
		col++;
		ltm.setColumnName("Warps", col);
		col++;
		ltm.setColumnName("End Sector", col);
	}

	@Override
	protected void filterTableByRace()
	{
		root.setRaceFilter(races);
		treeModel.nodeStructureChanged(root);
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		if (checkForStandardTableChanged(e))
			return;
		if (e.getSource() == this.ltm)
		{
		}
	}

	public void rowSelected()
	{
		if (this.storedDistances != null)
		{
			String pathString = "";
			int selRow = this.list.getSelectedRow();
			if (selRow >= 0)
			{
				if (this.storedDistances[selRow] != null)
				{
					ArrayList<Integer> path = this.storedDistances[selRow].getPath();
					for (int x = 0; x < path.size(); x++)
					{
						pathString += path.get(x) + ", ";
					}
					if (pathString.length() > 1)
						pathString = pathString.substring(0, pathString.length() - 2);
					this.routeDisplay.setText(pathString);
				}
			}
		}
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
		rowSelected();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getSource()==this.plotToBox)
		{
			if(this.plotToBox.hasValue())
				doPlotNearest(new Sector((int)this.plotToBox.getValue()));
		}
		else if(e.getSource()==this.sectorBox)
		{
			doPlotNearest(this.lastSearchedObject);
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}
}
