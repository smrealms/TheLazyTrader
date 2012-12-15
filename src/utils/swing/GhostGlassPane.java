package utils.swing;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GhostGlassPane extends JPanel
{
	private final AlphaComposite composite;
	private Point location = new Point(0, 0);
	private BufferedImage draggingGhost = null;

	public GhostGlassPane()
	{
		setOpaque(false);
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	}

	public void setImage(BufferedImage draggingGhost)
	{
		this.draggingGhost = draggingGhost;
	}

	public void setPoint(Point location)
	{
		this.location = location;
	}

	public void paintComponent(Graphics g)
	{
		if (draggingGhost == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(composite);
		double xx = location.getX() - (draggingGhost.getWidth(this) / 2);
		double yy = location.getY() - (draggingGhost.getHeight(this) / 2);
		g2.drawImage(draggingGhost, (int) xx, (int) yy, null);
	}
}