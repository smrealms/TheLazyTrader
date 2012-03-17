import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import model.SectorList;


public class Resize
{
	public static void main(String[] args)
	{
		try
		{
			FileReader mapFile=new FileReader("H:\\Development\\Java\\TheLazyTrader\\allMoneyRoutes.txt");
			FileWriter mapFile2=new FileWriter("H:\\Development\\Java\\TheLazyTrader\\allMoneyRoutes2.txt");
			{
				Scanner sc = new Scanner(mapFile);
				int i=0;
				while(sc.hasNextLine() && i<4000)
				{
					i++;
						mapFile2.write(sc.nextLine()+"\r\n");
				}
				mapFile2.flush();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
