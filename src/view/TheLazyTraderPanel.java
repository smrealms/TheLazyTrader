package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import settings.Settings;

public abstract class TheLazyTraderPanel extends JPanel implements ActionListener
{
	protected JMenuBar jmb = new JMenuBar();
	protected JMenu[] fileMenu = { new JMenu("File"), new JMenu("Actions"), new JMenu("View"), new JMenu("Help") };
	protected JMenuItem[] jmiFileOptions = new JMenuItem[4];
	protected JMenuItem[] jmiActionOptions = new JMenuItem[4];
	protected JMenuItem[] jmiViewOptions = new JMenuItem[6];
	protected JMenuItem[] jmiHelpOptions = new JMenuItem[1];
	protected AboutFrame aboutFrame;
	protected String title;
	protected MainContainer parent;

	public TheLazyTraderPanel(MainContainer _parent)
	{
		this.parent = _parent;
		// setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setSize(900, 600);
		// setTitle( "The Lazy Trader " +settings.VERSION );

		initActionsFileMenu();
		createStandardMenuBar();
		initComponents();
		addComponents();

		setVisible(true);
	}

	abstract void initComponents();

	abstract protected void addComponents();

	protected JPanel createLabelJComponentPair(String label, JComponent jc, int width) {
		JPanel jp = new JPanel();
		jp.add(new JLabel(label));
		jc.setPreferredSize(new Dimension(width, 20));
		jp.add(jc);
		return jp;
	}

	protected JPanel createLabelJComponentPair(String label, JComponent jc) {
		return createLabelJComponentPair(label, jc, 100);
	}

	abstract protected void initActionsFileMenu();

	protected void createStandardMenuBar()
	{
		// setJMenuBar(jmb); // Add menu bar to frame
		for (int i = 0; i < fileMenu.length; i++)
		{
			jmb.add(fileMenu[i]); // Add File menu to menu bar
		}
		fileMenu[0].setMnemonic('F'); // Set Alt-F as the shortcut key
		fileMenu[1].setMnemonic('A'); // Set Alt-H as the shortcut key
		fileMenu[2].setMnemonic('V'); // Set Alt-V as the shortcut key
		fileMenu[3].setMnemonic('H'); // Set Alt-H as the shortcut key

		jmiFileOptions[0] = new JMenuItem("Open SMR File");
		jmiFileOptions[0].setMnemonic('O'); // Set O as the shortcut key
		jmiFileOptions[1] = new JMenuItem("Close SMR File");
		jmiFileOptions[1].setMnemonic('C'); // Set C as the shortcut key
		jmiFileOptions[2] = new JMenuItem("Close window");
		jmiFileOptions[2].setMnemonic('W'); // Set W as the shortcut key
		jmiFileOptions[3] = new JMenuItem("Quit");
		jmiFileOptions[3].setMnemonic('Q'); // Set Q as the shortcut key

		jmiViewOptions[0] = new JMenuItem("Open Main Menu");
		jmiViewOptions[0].setMnemonic('M'); // Set M as the shortcut key
		jmiViewOptions[1] = new JMenuItem("Open Route Generator");
		jmiViewOptions[1].setMnemonic('R'); // Set R as the shortcut key
		jmiViewOptions[2] = new JMenuItem("Open Ship List");
		jmiViewOptions[2].setMnemonic('S'); // Set S as the shortcut key
		jmiViewOptions[3] = new JMenuItem("Open Weapon List");
		jmiViewOptions[3].setMnemonic('W'); // Set W as the shortcut key
		jmiViewOptions[4] = new JMenuItem("Open Nearest X List");
		jmiViewOptions[4].setMnemonic('N'); // Set N as the shortcut key
		jmiViewOptions[5] = new JMenuItem("Open Player Setup");
		jmiViewOptions[5].setMnemonic('P'); // Set P as the shortcut key

		jmiHelpOptions[0] = new JMenuItem("About");
		jmiHelpOptions[0].setMnemonic('A'); // Set A as the shortcut key

		for (int i = 0; i < jmiFileOptions.length; i++)
		{
			fileMenu[0].add(jmiFileOptions[i]); // Add the File menu items to
			// the File menu
			jmiFileOptions[i].addActionListener(this); // Add action listeners
			// for the menu options
		}
		for (int i = 0; i < jmiActionOptions.length; i++)
		{
			fileMenu[1].add(jmiActionOptions[i]); // Add the File menu items
			// to the File menu
			jmiActionOptions[i].addActionListener(this); // Add action
			// listeners for the
			// menu options
		}
		for (int i = 0; i < jmiViewOptions.length; i++)
		{
			fileMenu[2].add(jmiViewOptions[i]); // Add the File menu items to
			// the File menu
			jmiViewOptions[i].addActionListener(this); // Add action listeners
			// for the menu options
		}
		for (int i = 0; i < jmiHelpOptions.length; i++)
		{
			fileMenu[3].add(jmiHelpOptions[i]); // Add the Help menu items to
			// the Help menu
			jmiHelpOptions[i].addActionListener(this);// Add action listeners
			// for the help options
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		standardActionChecks(e);
	}// end of actionPerformed

	protected boolean standardActionChecks(ActionEvent e)
	{
		if (e.getSource() == jmiFileOptions[0]) // Checks if user has chosen to
		// Open SMR File
		{
			FileLocate.openUniverseFile(this);
			return true;
		}
		else if (e.getSource() == jmiFileOptions[1]) // Checks if user has
		// chosen to Close SMR
		// File
		{
			FileLocate.closeUniverseFile();
			return true;
		}
		else if (e.getSource() == jmiFileOptions[2]) // Checks if user has
		// chosen to Close
		// Window
		{
			this.setVisible(false);
		}
		else if (e.getSource() == jmiFileOptions[3]) // Checks if user has
		// chosen to Quit
		{
			return checkQuit();
		}
		if (e.getSource() == jmiFileOptions[2]) // Checks if user has chosen to
		// Close Window, means to quit
		// for main window.
		{
			this.checkQuit();
		}
		else if (e.getSource() == jmiViewOptions[0])
		{
			this.parent.showMainMenu();
		}
		else if (e.getSource() == jmiViewOptions[1])
		{
			this.parent.showRouteGenerator();
		}
		else if (e.getSource() == jmiViewOptions[2])
		{
			this.parent.showShipList();
		}
		else if (e.getSource() == jmiViewOptions[3])
		{
			this.parent.showWeaponList();
		}
		else if (e.getSource() == jmiViewOptions[4])
		{
			this.parent.showNearestX();
		}
		else if (e.getSource() == jmiViewOptions[5])
		{
			this.parent.showPlayer();
		}
		else if (e.getSource() == jmiHelpOptions[0]) // Checks if user has
		// chosen to view the
		// about frame
		{
			if (this.aboutFrame == null)
			{
				this.aboutFrame = new AboutFrame(Settings.ABOUT);
				this.aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}
			this.aboutFrame.setVisible(true);
			this.aboutFrame.toFront();
			return true;
		}
		return false;
	}

	protected boolean checkQuit()
	{
		if (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
			System.exit(0);
		return true;
	}

	protected boolean checkSectorsFileOpened()
	{
		if (FileLocate.hasUniverseParser())
			return true;
		JOptionPane.showMessageDialog(null, "You do not have a sectors file opened.", "No Sectors File Open", JOptionPane.ERROR_MESSAGE);
		return FileLocate.openUniverseFile(this);
	}

	/**
	 * @return the jmb
	 */
	public JMenuBar getMenuBar()
	{
		return this.jmb;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return this.title;
	}
}
