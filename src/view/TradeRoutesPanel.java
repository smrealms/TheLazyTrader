package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import utils.gui.JIntegerField;

import model.Galaxy;
import model.Good;
import model.Race;
import controller.RouteHandler;
import controller.RouteSwingWorker;
import controller.pathfinding.RouteGenerator;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.swing.JButton;
import model.Route;

public class TradeRoutesPanel extends TheLazyTraderPanel implements TableModelListener, ItemListener
{
	protected JTextArea jta;

	protected JTable selectGoods, selectRaces;

	protected Map<Integer, Boolean> races;
	protected Map<Integer, Boolean> goods;
	protected JIntegerField selectStart, selectEnd, selectMaxDistance,
			selectNumberPorts, selectNumberOfRoutes, routesForPort;
	protected JComboBox selectGalaxy, selectDisplayType;
	private JButton[] jbActionButtons;
	protected File saveRoutesFile;

	private RouteHandler routeHandler;

	private RouteSwingWorker rsw;
	private JProgressBar progressBar;

	public TradeRoutesPanel(MainContainer _parent)
	{
		super(_parent);
		// setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.title = "Trade Routes";
	}

	@Override
	protected void initComponents()
	{
		races = new HashMap<Integer, Boolean>((int)(Race.getNumberOfRaces()*1.5));
		goods = new HashMap<Integer, Boolean>();
		jta = new JTextArea();
		if (FileLocate.getUniverseParser() != null) {
			ArrayList galaxies = new ArrayList(FileLocate.getUniverseParser().getGalaxies().values());
			galaxies.add(0, "Entire Universe");
			selectGalaxy = new JComboBox(galaxies.toArray());
		}
		else
			selectGalaxy = new JComboBox();
		selectDisplayType = new JComboBox(Route.DisplayType.values());
		selectStart = new JIntegerField(0);
		selectEnd = new JIntegerField(7000);
		selectMaxDistance = new JIntegerField(100);
		selectNumberPorts = new JIntegerField(2);
		selectNumberOfRoutes = new JIntegerField(20);
		routesForPort = new JIntegerField();

		// int count=0;
		// for(int i=0;i<Good.EXISTS.length;i++)
		// {
		// if(Good.EXISTS[i])
		// count++;
		// }

		// Goods Table
		MyTableModel mtm = new MyTableModel(1, Good.getNames().size());
		int count = 0;
		for (Entry<Integer, String> good : Good.getNames().entrySet()) {
			mtm.setColumnName(good.getValue(), count);
			mtm.setValueAt(true, 0, count);
			goods.put(good.getKey(), true);
			count++;
		}
		mtm.addTableModelListener(this);
		this.selectGoods = new JTable(mtm);
		this.selectGoods.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Races table
		mtm = new MyTableModel(1, Race.getNumberOfRaces());
		count = 0;
		for (Entry<Integer, Race> race : Race.getRaces().entrySet()) {
			mtm.setColumnName(race.getValue().getName(), count);
			mtm.setValueAt(true, 0, count);
			races.put(race.getKey(), true);
			count++;
		}
		mtm.addTableModelListener(this);
		this.selectRaces = new JTable(mtm);
		this.selectRaces.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		this.progressBar = new JProgressBar(0, 100);

		this.jta.setEditable(false);
		selectGalaxy.addItemListener(this);
		routeHandler = new RouteHandler(RouteGenerator.EXP_ROUTE, this.selectNumberOfRoutes.getIntValue(), this.selectStart.getIntValue(), this.selectEnd.getIntValue(), this.selectMaxDistance.getIntValue(), this.selectNumberPorts.getIntValue(), -1, (Route.DisplayType) this.selectDisplayType.getSelectedItem(), this.races, this.goods);
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
		JScrollPane jsp = new JScrollPane(selectGoods);
		gbl.setConstraints(jsp, c);
		add(jsp);

		// Second row
		jsp = new JScrollPane(selectRaces);
		gbl.setConstraints(jsp, c);
		add(jsp);

		// Third Row
		{
			JPanel jpOptions = new JPanel();
			jpOptions.add(createLabelJComponentPair("Galaxy: ", selectGalaxy, 120));
			jpOptions.add(createLabelJComponentPair("Display Type: ", selectDisplayType, 135));
			jpOptions.add(createLabelJComponentPair("Max ports: ", selectNumberPorts, 40));
			jpOptions.add(createLabelJComponentPair("Routes for port: ", routesForPort, 60));
			jpOptions.add(createLabelJComponentPair("Number of routes: ", selectNumberOfRoutes, 60));
			jpOptions.add(createLabelJComponentPair("Max Dist: ", selectMaxDistance, 60));
			jpOptions.add(createLabelJComponentPair("Start Sector: ", selectStart, 60));
			jpOptions.add(createLabelJComponentPair("End Sector: ", selectEnd, 60));


			// After Last
			c.ipady = 44;
			jsp = new JScrollPane(jpOptions);
			gbl.setConstraints(jsp, c);
			add(jsp);
		}

		c.ipady = 36;
		jbActionButtons = new JButton[4];
		jbActionButtons[0] = new JButton("Generate Exp");
		jbActionButtons[0].setMnemonic('E');
		jbActionButtons[1] = new JButton("Generate Money");
		jbActionButtons[1].setMnemonic('M');
		jbActionButtons[2] = new JButton("Save Exp");
		jbActionButtons[2].setMnemonic('S');
		jbActionButtons[3] = new JButton("Save Money");
		jbActionButtons[3].setMnemonic('Y');

		JPanel jpButtons = new JPanel();
		for(JButton jbActionButton : jbActionButtons) {
			jbActionButton.addActionListener(this);
			jpButtons.add(jbActionButton);
		}
		jsp = new JScrollPane(jpButtons);
		gbl.setConstraints(jsp, c);
		add(jsp);

		// Last Row
		c.ipady = 100;
		c.gridwidth = 0;
		c.weighty = 400;
		jsp = new JScrollPane(jta);
		gbl.setConstraints(jsp, c);
		add(jsp);

		// Progress bar
		c.gridwidth = 0;
		c.ipady = 10;
		c.weighty = 1;
		gbl.setConstraints(progressBar, c);
		add(progressBar);
	}

	@Override
	protected void initActionsFileMenu()
	{
		jmiActionOptions = new JMenuItem[4];
		jmiActionOptions[0] = new JMenuItem("Generate Exp Routes");
		jmiActionOptions[0].setMnemonic('E'); // Set E as the shortcut key
		jmiActionOptions[1] = new JMenuItem("Generate Money Routes");
		jmiActionOptions[1].setMnemonic('M'); // Set M as the shortcut key
		jmiActionOptions[2] = new JMenuItem("Save Exp Routes");
		jmiActionOptions[2].setMnemonic('S'); // Set S as the shortcut key
		jmiActionOptions[3] = new JMenuItem("Save Money Routes");
		jmiActionOptions[3].setMnemonic('Y'); // Set A as the shortcut key
	}

	private boolean askForSaveRoutesFile()
	{
		this.saveRoutesFile = FileLocate.askForSaveRoutesFile(this, this.saveRoutesFile);
		if (this.saveRoutesFile == null)
			return false;
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (this.standardActionChecks(e)) {
			return;
		}
		else if (source == jmiActionOptions[0] || source == jbActionButtons[0]) { // Checks if user has chosen to generate exp routes
			if (checkSectorsFileOpened()) {
				this.routeHandler.setDoSave(false);
				this.routeHandler.setTypeOfRoute(RouteGenerator.EXP_ROUTE);
				this.doRouteGeneration();
			}
		}
		else if (e.getSource() == jmiActionOptions[1] || source == jbActionButtons[1]) { // Checks if user has chosen to generate money routes
			if (checkSectorsFileOpened()) {
				this.routeHandler.setDoSave(false);
				this.routeHandler.setTypeOfRoute(RouteGenerator.MONEY_ROUTE);
				this.doRouteGeneration();
			}
		}
		else if (e.getSource() == jmiActionOptions[2] || source == jbActionButtons[2]) { // Checks if user has chosen to save exp routes
			if (checkSectorsFileOpened()) {
				if (askForSaveRoutesFile()) {
					this.routeHandler.setDoSave(true);
					this.routeHandler.setSaveFile(this.saveRoutesFile);
					this.routeHandler.setTypeOfRoute(RouteGenerator.EXP_ROUTE);
					this.doRouteGeneration();
				}
			}
		}
		else if (e.getSource() == jmiActionOptions[3] || source == jbActionButtons[3]) { // Checks if user has chosen to save money routes
			if (checkSectorsFileOpened()) {
				if (askForSaveRoutesFile()) {
					this.routeHandler.setDoSave(true);
					this.routeHandler.setSaveFile(this.saveRoutesFile);
					this.routeHandler.setTypeOfRoute(RouteGenerator.MONEY_ROUTE);
					this.doRouteGeneration();
				}
			}
		}
	}

	private void doRouteGeneration()
	{
		this.jta.setText("Starting Generating.");
		if(routesForPort.hasValue())
			routeHandler.setRoutesForPort(routesForPort.getIntValue());
		else
			routeHandler.setRoutesForPort(-1);
		routeHandler.setNumberOfRoutes(this.selectNumberOfRoutes.getIntValue());
		routeHandler.setStartSector(this.selectStart.getIntValue());
		routeHandler.setEndSector(this.selectEnd.getIntValue());
		routeHandler.setMaxDistance(this.selectMaxDistance.getIntValue());
		routeHandler.setMaxNumberOfPorts(this.selectNumberPorts.getIntValue());
		routeHandler.setRaces(this.races);
		routeHandler.setGoods(this.goods);
		routeHandler.setDisplayType((Route.DisplayType) this.selectDisplayType.getSelectedItem());
		if (this.rsw != null)
			this.rsw.cancel(true);
		this.rsw = new RouteSwingWorker(this.jta, this.routeHandler, this.progressBar);
		this.rsw.execute();
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		if (e.getSource() == this.selectGoods.getModel())
			this.goods.put(Good.getId(this.selectGoods.getColumnName(e.getColumn())), (Boolean) this.selectGoods.getValueAt(e.getFirstRow(), e.getColumn()));

		else if (e.getSource() == this.selectRaces.getModel())
			this.races.put(Race.getId(this.selectRaces.getColumnName(e.getColumn())), (Boolean) this.selectRaces.getValueAt(e.getFirstRow(), e.getColumn()));
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object item = e.getItem();
		if (item instanceof String) {
			String s = (String) item;
			if(s.equals("Entire Universe")) {
				this.selectStart.setText("0");
				this.selectEnd.setText("7000");
			}
		}
		else if (item instanceof Galaxy) {
			Galaxy g = (Galaxy) item;
			this.selectStart.setText(Integer.toString(g.getStartSectorId()));
			this.selectEnd.setText(Integer.toString(g.getEndSectorId()));
		}
	}
}
