package controller;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import controller.pathfinding.RouteGenerator;
import java.util.Date;

public class RouteSwingWorker extends SwingWorker<String, String>
{
	private final JTextArea routeDisplay;
	private final RouteHandler routeHandler;
	private final JProgressBar progressBar;
	private Date startDate;

	public RouteSwingWorker(JTextArea _routeDisplay, RouteHandler _routeHandler, JProgressBar _progressBar)
	{
		this.routeDisplay = _routeDisplay;
		this.routeHandler = _routeHandler;
		this.progressBar = _progressBar;
	}

	@Override
	protected String doInBackground() throws Exception
	{
		RouteGenerator.setPublishProgressTo(this);
		this.startDate = new Date();
		return routeHandler.doInBackground(this);
	}

	@Override
	protected void done()
	{
		try
		{
			if (!this.isCancelled())
			{
				long timeDiff = new Date().getTime() - startDate.getTime();
				this.routeDisplay.setText(this.get() + "\n\nTook: " + timeDiff + "ms");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	void publishProgress(String chunks)
	{
		this.publish(chunks);
	}

	@Override
	protected void process(List<String> chunks)
	{
		for (String progress : chunks)
		{
			this.routeDisplay.append("\n" + progress);
		}
	}

	public void publishProgressToBar(int done, int todo)
	{
		int todoSq = todo * todo;
		int remaining = todo - done;
		this.progressBar.setValue((int) (100 * (todoSq - remaining * remaining) / (double) (todoSq)));
	}
}
