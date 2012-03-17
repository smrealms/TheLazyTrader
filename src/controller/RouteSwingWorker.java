package controller;

import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import controller.pathfinding.RouteGenerator;

public class RouteSwingWorker extends SwingWorker<String, String>
{
	private JTextArea routeDisplay;
	private RouteHandler routeHandler;
	private JProgressBar progressBar;

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
		return routeHandler.doInBackground(this);
	}

	@Override
	protected void done()
	{
		try
		{
			if (!this.isCancelled())
				this.routeDisplay.setText(this.get());
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
		this.progressBar.setValue((int) (100 * (todo * todo - (todo - done) * (todo - done)) / (double) (todo * todo)));
	}
}
