package view;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settings.VersionUtil;
import utils.swing.CloseTabListener;
import utils.swing.DnDTabbedPane;

public class MainContainer extends JFrame implements ChangeListener, FileLocateListener
{
	protected DnDTabbedPane jtp = new DnDTabbedPane(this);
	private MainPanel mp;
	private TradeRoutesPanel trc;
	private ShipListPanel slc;
	private WeaponListPanel wlc;
	private NearestXListPanel nxlc;
	private PlayerListPanel plp;

	public MainContainer()
	{
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle( "The Lazy Trader " + VersionUtil.getVersion() );

		initComponents();
		addComponents();
		FileLocate.addFileLocateListener(this);

		setSize(900, 600);
		this.setVisible(true);
		// this.title = "Main";
	}

	protected void initActionsFileMenu()
	{
	}

	protected void initComponents()
	{
		this.jtp.addChangeListener(this);
		this.jtp.addMouseListener(new CloseTabListener(this.jtp));
		showMainMenu();
	}

	protected void addComponents()
	{
		this.add(jtp);
	}

	protected boolean checkForUniverseFile()
	{
		if(FileLocate.getUniverseParser() == null)
			FileLocate.openUniverseFile(this);
		return FileLocate.getUniverseParser() != null;
	}

	public void showShipList()
	{
		if(!checkForUniverseFile())
			return;
		if (this.slc == null)
		{
			this.slc = new ShipListPanel(this);
		}
		switchToTab(this.slc);
	}

	public void showWeaponList()
	{
		if(!checkForUniverseFile())
			return;
		if (this.wlc == null)
		{
			this.wlc = new WeaponListPanel(this);
		}
		switchToTab(this.wlc);
	}

	public void showNearestX()
	{
		if(!checkForUniverseFile())
			return;
		if (this.nxlc == null)
		{
			this.nxlc = new NearestXListPanel(this);
		}
		switchToTab(this.nxlc);
	}

	public void showPlayer()
	{
		if(!checkForUniverseFile())
			return;
		if (this.plp == null)
		{
			this.plp = new PlayerListPanel(this);
		}
		switchToTab(this.plp);
	}

	public void showRouteGenerator()
	{
		if(!checkForUniverseFile())
			return;
		if (this.trc == null)
		{
			this.trc = new TradeRoutesPanel(this);
		}
		switchToTab(this.trc);
	}

	public void showMainMenu()
	{
		checkForUniverseFile();
		if (this.mp == null)
		{
			this.mp = new MainPanel(this);
		}
		switchToTab(this.mp);
	}

	private void switchToTab(TheLazyTraderPanel tlp)
	{
		if (this.jtp.indexOfComponent(tlp) == -1)
		{
			this.jtp.addTab(tlp.getTitle(), tlp);
		}
		this.setJMenuBar(tlp.getMenuBar());
		this.jtp.setSelectedComponent(tlp);
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() == this.jtp)
			this.setJMenuBar(((TheLazyTraderPanel) this.jtp.getSelectedComponent()).getMenuBar());
	}

	@Override
	public void universeLocated()
	{
		if(jtp!=null)
		{
			jtp.remove(trc);
			jtp.remove(slc);
			jtp.remove(wlc);
			jtp.remove(nxlc);
			jtp.remove(plp);
		}
		trc = null;
		slc = null;
		wlc = null;
		nxlc = null;
		plp = null;
	}

	@Override
	public void universeLost()
	{
		if(jtp!=null)
		{
			jtp.remove(trc);
			jtp.remove(slc);
			jtp.remove(wlc);
			jtp.remove(nxlc);
			jtp.remove(plp);
		}
		trc = null;
		slc = null;
		wlc = null;
		nxlc = null;
		plp = null;
	}
}
