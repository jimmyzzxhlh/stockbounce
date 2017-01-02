package test;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import download.StockDataMerge;
import intraday.IntraDayReaderYahoo;
import intraday.IntraDaystockCandleList;
import stock.StockConst;
import stock.StockEnum.Exchange;
import util.StockUtil;

public class DataMergeTest {

	public static void main(String[] args) throws Exception {
//		mergeOneIntraDayData();
		mergeOneIntraDayDataForSymbols();
	}
	
	private static void mergeAllData() throws Exception {
		StockDataMerge.mergeAllData(Exchange.NASDAQ);
	}
	
	private static void mergeOneIntraDayData() throws Exception {
		Exchange exchange = Exchange.NASDAQ;
		String symbol = "AAON";
		StockDataMerge.mergeOneIntraDayData(exchange, symbol, "20151019");
	}
	
	private static void mergeOneIntraDayDataForSymbols() throws Exception {
		Exchange exchange = Exchange.NASDAQ;
		StockDataMerge.mergeOneIntraDayData(exchange, "20151019");
	}
	
	/**
	 * Verify the intraday data.
	 * If invalid data is found, e.g. Data is not downloaded completely or no intraday data has been found,
	 * then delete the file.
	 */
	private static void testVerifyIntraDayData() {
		File directory = new File(StockConst.INTRADAY_DIRECTORY_SZSE_PATH);
		Scanner scanner = new Scanner(System.in);
		for (File subDirectory : directory.listFiles()) {
			String symbol = subDirectory.getName();
//			System.out.println(symbol);
			for (File file : subDirectory.listFiles()) {
				String date = StockUtil.getFilenameWithoutExtension(file.getName());
				IntraDaystockCandleList idstockCandleList = null;
				try {
					idstockCandleList = IntraDayReaderYahoo.getIntraDaystockCandleList(symbol, file);					
				}
				catch (Exception e) {
//					System.out.println("Delete file/directory " + file.getAbsolutePath() + "? (1 to delete, -1 to exit the program)");
//					int input = scanner.nextInt();
//					int input = 1;
//					if (input == 1) {
//						boolean deleted = false;
//						if (file.isDirectory()) {
//							deleted = StockUtil.deleteDirectory(file);
//						}
//						else {
//							deleted = file.delete();
//						}
//						if (deleted) {			
//							System.out.println("File/Directory " + file.getAbsolutePath() + " is deleted.");
//						}
//						else {
//							System.err.println("File/Directory " + file.getAbsolutePath() + " cannot be deleted.");
//						}
//					}
//					else if (input == -1) {
//						scanner.close();
//						return;
//					}
				}
				if (idstockCandleList != null) {
					idstockCandleList.destroy();
				}
			}
			
		}
		scanner.close();
		
		
	}

}
