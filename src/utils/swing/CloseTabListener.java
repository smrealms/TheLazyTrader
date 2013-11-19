package utils.swing;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;

public class CloseTabListener implements MouseListener, ActionListener
{
	private final JTabbedPane tabbedPane;
	private final JPopupMenu popup;
	private int tabIndex;
	private final JMenuItem menuItem;

	public CloseTabListener(JTabbedPane _tabbedPane)
	{
		this.tabbedPane = _tabbedPane;
		popup = new JPopupMenu();
		menuItem = new JMenuItem("Close Tab");
		menuItem.addActionListener(this);
		popup.add(menuItem);
	}

	private int checkTabIndex(Point p)
	{
		for (int i = 0; i < tabbedPane.getTabCount(); i++)
		{
			if (tabbedPane.getUI().getTabBounds(tabbedPane, i).contains(p))
			{
				return i;
			}
		}
		return -1;
	}

	private void showPopup(MouseEvent e)
	{
		if (e.isPopupTrigger())
		{
			tabIndex = checkTabIndex(e.getPoint());
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (SwingUtilities.isMiddleMouseButton(e))
			if (this.tabbedPane.getTabCount() > 1)
			{
				int tIndex = checkTabIndex(e.getPoint());
				if (tIndex != -1)
					this.tabbedPane.removeTabAt(tIndex);
			}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		showPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		showPopup(e); // here because different platforms handle popups differently
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
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.menuItem)
		{
			if (this.tabbedPane.getTabCount() > 1)
				this.tabbedPane.removeTabAt(tabIndex);
		}
	}
}