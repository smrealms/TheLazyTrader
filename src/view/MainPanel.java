package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;

import model.Galaxy;

public class MainPanel extends TheLazyTraderPanel implements ActionListener, ItemListener
{
	protected JComboBox selectGalaxy = new JComboBox();
	private JButton[] jbActionButtons;

	// private TradeRoutesPanel trc;
	// private ShipListPanel slc;
	// private WeaponListPanel wlc;

	public MainPanel(MainContainer _parent)
	{
		super(_parent);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setTitle( " Main" );
		this.title = "Main";
	}

	protected void initActionsFileMenu()
	{
		jmiActionOptions = new JMenuItem[5];
		jmiActionOptions[0] = new JMenuItem("Open Route Generator");
		jmiActionOptions[0].setMnemonic('R'); // Set R as the shortcut key
		jmiActionOptions[1] = new JMenuItem("Open Ship List");
		jmiActionOptions[1].setMnemonic('S'); // Set S as the shortcut key
		jmiActionOptions[2] = new JMenuItem("Open Weapon List");
		jmiActionOptions[2].setMnemonic('W'); // Set W as the shortcut key
		jmiActionOptions[3] = new JMenuItem("Open Nearest X List");
		jmiActionOptions[3].setMnemonic('N'); // Set N as the shortcut key
		jmiActionOptions[4] = new JMenuItem("Open Player Setup");
		jmiActionOptions[4].setMnemonic('P'); // Set P as the shortcut key
	}

	protected void initComponents()
	{
		jbActionButtons = new JButton[5];
		jbActionButtons[0] = new JButton("Open Route Generator");
		jbActionButtons[0].setMnemonic('R'); // Set R as the shortcut key
		jbActionButtons[1] = new JButton("Open Ship List");
		jbActionButtons[1].setMnemonic('S'); // Set S as the shortcut key
		jbActionButtons[2] = new JButton("Open Weapon List");
		jbActionButtons[2].setMnemonic('W'); // Set W as the shortcut key
		jbActionButtons[3] = new JButton("Open Nearest X List");
		jbActionButtons[3].setMnemonic('N'); // Set N as the shortcut key
		jbActionButtons[4] = new JButton("Open Player Setup");
		jbActionButtons[4].setMnemonic('P'); // Set P as the shortcut key
	}

	protected void addComponents()
	{
		setLayout(new FlowLayout());
		for (int i = 0; i < jbActionButtons.length; i++)
		{
			jbActionButtons[i].addActionListener(this); // Add action listeners
			// for the menu options
			add(jbActionButtons[i]);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		// Override
		if (e.getSource() == jmiFileOptions[2]) // Checks if user has chosen to
		// Close Window, means to quit
		// for main window.
		{
			this.checkQuit();
		}
		else if (this.standardActionChecks(e))
			return;
		else if (e.getSource() == jmiActionOptions[0] || e.getSource() == jbActionButtons[0])
		{
			this.parent.showRouteGenerator();
		}
		else if (e.getSource() == jmiActionOptions[1] || e.getSource() == jbActionButtons[1])
		{
			this.parent.showShipList();
		}
		else if (e.getSource() == jmiActionOptions[2] || e.getSource() == jbActionButtons[2])
		{
			this.parent.showWeaponList();
		}
		else if (e.getSource() == jmiActionOptions[3] || e.getSource() == jbActionButtons[3])
		{
			this.parent.showNearestX();
		}
		else if (e.getSource() == jmiActionOptions[4] || e.getSource() == jbActionButtons[4])
		{
			this.parent.showPlayer();
		}
	}// end of actionPerformed

	public void itemStateChanged(ItemEvent e)
	{
		if (e.getItem().getClass().equals(Galaxy.class))
		{// TODO
			// Galaxy g = (Galaxy)e.getItem();
			// this.selectStart.setText(Integer.toString(g.getStartSectorId()));
			// this.selectEnd.setText(Integer.toString(g.getEndSectorId()));
		}
	}
}
