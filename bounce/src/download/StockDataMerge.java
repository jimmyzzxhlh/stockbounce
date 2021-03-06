package download;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import stock.ExchangeUtil;
import stock.StockAPI;
import stock.StockEnum.Exchange;
import util.StockFileWriter;
import util.StockUtil;

/**
 * Merging the downloaded data to improve the reading performance
 * @author Dongyue Xue
 *
 */
public class StockDataMerge {
	
	public static void mergeAllData(Exchange exchange) throws Exception {
		
		File dir = new File(ExchangeUtil.getIntraDayDirectory(exchange));
		//File dir = new File("D:\\zzx\\Stock\\IntraDayTest\\");
		String newDir = "D:\\zzx\\Stock\\IntraDayTest\\";
		StockUtil.createNewDirectory(newDir);
		
		for (File symbolFolder : dir.listFiles()) { //Symbol folder
			String symbol = symbolFolder.getName();
			String newFileName = newDir + "\\" + symbol + "\\" + symbol + ".txt";
			System.out.println(symbol);
			BufferedReader br = null;
			
			File[] dateListing = symbolFolder.listFiles();
			if (dateListing == null) continue;			
			StockUtil.createNewDirectory(newDir + "\\" + symbol);
			
			StockFileWriter sfw = new StockFileWriter(newFileName);
			for (File intraDayFile : dateListing) {    //Intraday file
				//Writing header for intraday data
				String fileName = intraDayFile.getName();
				if (fileName.indexOf('.') == -1) continue;
				String date = fileName.substring(0, fileName.indexOf('.'));
				sfw.writeLine("#" + date);
				br = new BufferedReader(new FileReader(intraDayFile));
				String line;
				while ((line = br.readLine()) != null) 	{
					if (line.startsWith("volume")) break;
					if (line.startsWith("timezone")) {
						sfw.writeLine("#" + line);
						continue;
					}
					else if (line.startsWith("gmtoffset")) {
						sfw.writeLine("#" + line);
						continue;
					}
				}
				while ((line = br.readLine()) != null) {
					//Writing intraday data
					String data[] = line.split(",");
					if (data.length <= 5) {
						System.err.println("Data does not have length of 5: " + line + " in " + symbol + " " + intraDayFile.getName());	
					}
					sfw.writeLine(line);
					
				}
				br.close();
			}
			sfw.close();
		}								
	}
	
	public static boolean mergeOneIntraDayData(Exchange exchange, String symbol, String dateString) throws Exception {
		String outputFileName = ExchangeUtil.getIntraDayDirectory(exchange) + symbol + "\\" + symbol + ".txt";
		//Pass true parameter to append file instead of overwrite the file!
		StockFileWriter sfw = new StockFileWriter(outputFileName, true);
		String intraDayFileName = ExchangeUtil.getIntraDayDirectory(exchange) + symbol + "\\" + dateString + ".txt";
		File intraDayFile = new File(intraDayFileName);
		if (!intraDayFile.exists()) {
			System.err.println("File not found: " + intraDayFileName);
			return false;
		}
		sfw.writeLine("#" + dateString);
		BufferedReader br = new BufferedReader(new FileReader(intraDayFile));
		String line;
		boolean success = true;
		while ((line = br.readLine()) != null) 	{
			if (line.startsWith("volume")) break;
			if (line.startsWith("timezone")) {
				sfw.writeLine("#" + line);
				continue;
			}
			else if (line.startsWith("gmtoffset")) {
				sfw.writeLine("#" + line);				
				continue;
			}
		}
		while ((line = br.readLine()) != null) {
			//Writing intraday data
			String data[] = line.split(",");
			if (data.length <= 5) {
				System.err.println("Data does not have length of 6: " + line + " in " + intraDayFileName);
				success = false;
			}
			sfw.writeLine(line);	
			
		}
		br.close();	
		sfw.close();
//		System.out.println(symbol + ": Intraday data on " + dateString + " merged.");
		if (success) {
			intraDayFile.delete();
//			System.out.println(symbol + ": Intraday data on " + dateString + " deleted.");
		}
		return success;
		
	}
	
	public static void mergeOneIntraDayData(Exchange exchange, String dateString) throws Exception {
		List<String> symbolList = StockAPI.getSymbolListFromExchange(exchange);
		int success = 0;
		int failure = 0;
		for (String symbol : symbolList) {
			File symbolDirectory = new File(ExchangeUtil.getIntraDayDirectory(exchange, symbol));
			if (!symbolDirectory.isDirectory()) {
				System.err.println(symbolDirectory.getAbsolutePath() + " is not a directory.");
				continue;
			}
			if (mergeOneIntraDayData(exchange, symbol, dateString)) {
				success++;
			}
			else {
				failure++;
			}
			
		}
		System.out.println("Total merged: " + (success+failure) + ", error: " + failure);
	}
}

