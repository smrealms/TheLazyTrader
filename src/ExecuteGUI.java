import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import model.preferences.GeneralPreferences;
import view.MainContainer;

public class ExecuteGUI
{
	public static void main(String[] args)
	{
//		setMessageStream();
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Exception e)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception ee)
			{
			}
		}

		if (args.length == 0)
		{
			boolean askForMemory = GeneralPreferences.askForMemory();
			int memoryToUse = GeneralPreferences.getMemoryToAllocate();
			if (askForMemory)
			{
				Object temp = JOptionPane.showInputDialog(null, "Please input the amount of memory to allocate for java in MB (0 for default)", "Memory to use", JOptionPane.QUESTION_MESSAGE);

				if(temp!=null)
				{
					String memoryToUseString = (String)temp;
					try
					{
						memoryToUse = Integer.parseInt(memoryToUseString);
						GeneralPreferences.setMemoryToAllocate(memoryToUse);
					}
					catch (NumberFormatException nfe)
					{
					}
					Boolean[] trueFalse = { true, false };
					temp = JOptionPane.showInputDialog(null, "Ask for memory each time?", "Ask for memory", JOptionPane.QUESTION_MESSAGE, null, trueFalse, Boolean.valueOf(true));
					if(temp!=null)
						askForMemory = (Boolean) temp;
				}
			}

			if (memoryToUse > 0)
			{
				String memUseOption = "-Xmx" + memoryToUse + "m";
				ProcessBuilder pb;
				String filePath = new File(ExecuteGUI.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getAbsolutePath();
				if (filePath.toUpperCase().endsWith(".JAR"))
				{
					pb = new ProcessBuilder("java.exe", memUseOption, "-jar", new File(filePath).getName(), "-ForReal");
					filePath = new File(filePath).getParent();
				}
				else
				{
					pb = new ProcessBuilder("java.exe", memUseOption, "ExecuteGUI", "-ForReal");
				}

				pb.directory(new File(filePath));
				try
				{
					Process p = pb.start();
					int exitVal = p.waitFor();
					if (exitVal == 0)
						GeneralPreferences.setAskForMemory(askForMemory);
					Scanner sc = new Scanner(p.getErrorStream());
					while(sc.hasNextLine()) {
						System.out.println(sc.nextLine());
					}
					sc.close();
					sc = new Scanner(p.getInputStream());
					while(sc.hasNextLine()) {
						System.out.println(sc.nextLine());
					}
					sc.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				doMain();
				GeneralPreferences.setAskForMemory(askForMemory);
			}
		}
		else
		{
			doMain();
		}
	}

	private static void doMain()
	{
		new MainContainer();
	}

	/**
	 * Sets the System.out and System.err streams to a GUI panel.
	 */
	private static void setMessageStream()
	{
		final JTextArea error = new JTextArea();
		PrintStream errorStream = new PrintStream(new OutputStream(){
			@Override
			public void write(byte[] b)
			{
				error.setText(error.getText()+new String(b));
			}

			@Override
			public void write(int b){
				byte[] c = { Byte.parseByte(Integer.toString(b)) };
				write(c);
			}
		});
		System.setErr(errorStream);
		JFrame vis = new JFrame();
		vis.setSize(800,600);
		vis.add(error);
		vis.setVisible(true);


		final JTextArea out = new JTextArea();
		PrintStream outStream = new PrintStream(new OutputStream(){
			@Override
			public void write(byte[] b)
			{
				out.setText(out.getText()+new String(b));
			}

			@Override
			public void write(int b){
				byte[] c = { Byte.parseByte(Integer.toString(b)) };
				write(c);
			}
		});
		System.setOut(outStream);
		vis = new JFrame();
		vis.setSize(800,600);
		vis.add(out);
		vis.setVisible(true);
	}

}