package download;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import stock.StockConst;
import util.StockUtil;

public class StockDataMerge {
	
	public static void DataMerge() throws IOException {
		File dir = new File(StockConst.INTRADAY_DIRECTORY_PATH_YAHOO);
		//File dir = new File("D:\\zzx\\Stock\\IntraDayTest\\");
		String newDir = "D:\\zzx\\Stock\\IntraDayTest\\";
		StockUtil.createNewDirectory(newDir);
		File[] symbolListing = dir.listFiles();
		if (symbolListing == null)
		{
			return;
		}

		for (File symbolFolder : symbolListing) //Symbol folder
		{
			String symbol = symbolFolder.getName();
			String symbolText = newDir + "\\" + symbol + "\\" + symbol + ".txt";
			BufferedWriter bw = null;
			BufferedReader br = null;
			
			File[] dateListing = symbolFolder.listFiles();
			if (dateListing == null) 
			{
				continue;
			}
			
			StockUtil.createNewDirectory(newDir + "\\" + symbol);
			File file = new File(symbolText);
			if (!file.exists())
			{
				file.createNewFile();
			}	    

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			{
				for (File intraDayFile : dateListing) //Intraday file
				{
					//Writing header for intraday data
					String fileName = intraDayFile.getName();
					if (fileName.indexOf('.') == -1)
					{
						continue;
					}
					String date = fileName.substring(0, fileName.indexOf('.'));
					bw.write("#" + date);
					bw.newLine();
					br = new BufferedReader(new FileReader(intraDayFile));
					String line;
					while ((line = br.readLine()) != null) 
					{
						if (line.startsWith("volume"))
						{
							break;
						}
						if (line.startsWith("timezone"))
						{
							bw.write("#" + line);
							bw.newLine();
							continue;
						}
						else if (line.startsWith("gmtoffset"))
						{
							bw.write("#" + line);
							bw.newLine();
							continue;
						}
					}
					while ((line = br.readLine()) != null) 
					{
						//Writing intraday data
						String data[] = line.split(",");
						Timestamp ts;
						if (data.length > 5) 
						{
							bw.write(line);
							bw.newLine();
						}
					}
					br.close();
				}
			}
			bw.close();
		}								
	}
}

