package utils.swing;

import java.net.URL;

public class LinkedImageButton extends ImageButton
{
	protected LinkedImageButton previousButton, nextButton;
	public LinkedImageButton(URL up, URL down)
	{
		super(up, down);
	}
	public LinkedImageButton(String normalImageFile, String pressedImageFile)
	{
		super(normalImageFile, pressedImageFile);
	}
	
	public LinkedImageButton(URL up, URL down, LinkedImageButton previous, LinkedImageButton next)
	{
		super(up, down);
		previousButton = previous;
		nextButton = next;
	}

	public LinkedImageButton(String normalImageFile, String pressedImageFile, LinkedImageButton previous, LinkedImageButton next)
	{
		super(normalImageFile, pressedImageFile);
		previousButton = previous;
		nextButton = next;
	}
	
	public LinkedImageButton getPreviousButton()
	{
		return previousButton;
	}
	public LinkedImageButton getNextButton()
	{
		return nextButton;
	}
	
	public void setPreviousButton(LinkedImageButton previous)
	{
		previousButton = previous;
	}
	public void setNextButton(LinkedImageButton next)
	{
		nextButton = next;
	}
	
	public void translateAllNextLocation(int x, int y)
	{
		this.translate(x, y);
		if(nextButton!=null)
			nextButton.translateAllNextLocation(x,y);
	}
	
	public boolean equalsWithinPreviousQueue(Object o)
	{
		if(equals(o))
			return true;
		if(previousButton==null)
			return false;
		return previousButton.equalsWithinPreviousQueue(o);
	}
}
