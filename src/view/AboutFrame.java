package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import loci.visbio.util.BrowserLauncher;

public class AboutFrame extends JFrame implements ActionListener, HyperlinkListener
{
	JEditorPane jep;
	JButton jbtnOk;

	public AboutFrame(String aboutText)
	{
		super();
		jep = new JEditorPane("text/html", aboutText);
		// jta.set.setLineWrap(true);
		// jta.setWrapStyleWord(true);
		jep.setEditable(false);
		jep.addHyperlinkListener(this);

		jbtnOk = new JButton("Ok");
		jbtnOk.addActionListener(this);
		jbtnOk.grabFocus();

		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 300);
		setTitle("About");
		add(jep);
		add(jbtnOk, BorderLayout.SOUTH);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == jbtnOk)
		{
			this.dispose();
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			if (e instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
				HTMLDocument doc = (HTMLDocument) jep.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				String source = e.getURL().toString();

				// launch external browser to handle the link
				try
				{
					BrowserLauncher.openURL(source);
				}
				catch (IOException exc)
				{
					exc.printStackTrace();
				}
			}
		}
	}
}
