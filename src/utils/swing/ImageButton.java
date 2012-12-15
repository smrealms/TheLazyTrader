package utils.swing;

import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A button whose entire look is specified by images.
 */
public class ImageButton extends JButton
{
	Icon upIcon = null;
	Icon downIcon = null;
	public ImageButton(URL normalImageFile, URL pressedImageFile)
	{
		super();
		changeIcons(normalImageFile,pressedImageFile);

		setBorderPainted(false);
		setFocusPainted(false);
		setMargin(new Insets(-3,-3,-3,-5));// Just enough margin to have black box all the way round
	}
	public ImageButton(String normalImageFile, String pressedImageFile)
	{
		super();
	
		try
		{
			upIcon = new ImageIcon(new URL(normalImageFile));
			downIcon = new ImageIcon(new URL(pressedImageFile));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		setIcon(upIcon);
		setBorderPainted(false);
		setFocusPainted(false);
		setPressedIcon(downIcon);
		setMargin(new Insets(-3,-3,-3,-4));// Just enough margin to have black box all the way round
	}
	
	public void changeIcons(URL up, URL down)
	{
		if(up != null)
			upIcon = new ImageIcon(up);
		if(down != null)
			downIcon = new ImageIcon(down);
		setIcon(upIcon);
		setPressedIcon(downIcon);
	}
	
	public void keepPressed()
	{
		setIcon(downIcon);
	}
	
	public void unpress()
	{
		setIcon(upIcon);
	}
	
	public void translate(int x, int y)
	{
		this.setLocation(this.getX()+x, this.getY()+y);
	}
}
