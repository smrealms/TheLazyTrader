package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
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
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import java.util.Map.Entry;
import java.util.NavigableMap;
import javax.swing.JTree;

import utils.gui.JIntegerField;

public class NearestXListPanel extends ListPanelWithRaces implements TreeSelectionListener, MouseListener, KeyListener
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
		for (ShipEquipment se : ShipEquipment.getShipEquipments().values()) {
			shipEqs.add(new FilterableTreeNode(se, false));
		}


		FilterableTreeNode shipsByRace = new FilterableTreeNode("Ships");
		FilterableTreeNode shipRaceNode = new FilterableTreeNode("All Ships");
		for (Ship s : Ship.getShips().values()) {
			shipRaceNode.add(new FilterableTreeNode(s, false));
		}
		shipsByRace.add(shipRaceNode);
		for (Entry<String, NavigableMap<String, Ship>> raceShips : Ship.getShipsByRace().entrySet()) {
			shipRaceNode = new FilterableTreeNode(raceShips.getKey());
			for (Ship s : raceShips.getValue().values()) {
				shipRaceNode.add(new FilterableTreeNode(s, false));
			}
			shipsByRace.add(shipRaceNode);
		}


		FilterableTreeNode weaponsByPower = new FilterableTreeNode("Weapons");
		FilterableTreeNode weaponPowerNode = new FilterableTreeNode("All Levels");
		for (Weapon w : Weapon.getWeapons().values()) {
			weaponPowerNode.add(new FilterableTreeNode(w, false));
		}
		weaponsByPower.add(weaponPowerNode);
		for (Entry<Integer, NavigableMap<String, Weapon>> powerWeapons : Weapon.getWeaponsByPower().entrySet()) {
			weaponPowerNode = new FilterableTreeNode("Level " + powerWeapons.getKey());
			for (Weapon w : powerWeapons.getValue().values()) {
				weaponPowerNode.add(new FilterableTreeNode(w, false));
			}
			weaponsByPower.add(weaponPowerNode);
		}


		FilterableTreeNode locationsByType = new FilterableTreeNode("Locations");
		for (Entry<String, NavigableMap<String, Location>> typeLocations : Location.getLocationsByType().entrySet()) {
			String locationTypeName = typeLocations.getKey();
			FilterableTreeNode locationTypeNode = new FilterableTreeNode(Location.getLocationByType(locationTypeName).firstEntry().getValue().getType());
			for (Location l : typeLocations.getValue().values()) {
				locationTypeNode.add(new FilterableTreeNode(l, false));
			}
			locationsByType.add(locationTypeNode);
		}


		FilterableTreeNode goodsBought = new FilterableTreeNode("Bought");
		FilterableTreeNode goodsSold = new FilterableTreeNode("Sold");
		FilterableTreeNode goodsEither = new FilterableTreeNode("Either");
		for (int goodID : Good.getNames().keySet()) {
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

	@Override
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
				TIntObjectMap<Distance> distances = Pathfinding.findDistanceToX(x, FileLocate.getUniverseParser().getUniverse().getSectors(), (int) this.sectorBox.getValue(), Long.MIN_VALUE, Long.MAX_VALUE, Long.MAX_VALUE, false);
				if (distances.size() > 0)
				{
					this.storedDistances = distances.values(new Distance[0]);
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
					TIntArrayList path = this.storedDistances[selRow].getPath();
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

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
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
