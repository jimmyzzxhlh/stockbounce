package stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Read the text file which contains the earnings date.
 * @author Dongyue Xue
 *
 */
public class StockEarningsDateMap {
	
	private StockEarningsDateMap() {
		
	}
	
	private static HashMap<String, ArrayList<Date>> map;
	
	public static HashMap<String, ArrayList<Date>> getMap() { 
		if (map == null) setMap();
		return map;
	}
	
	private static void setMap() {
		String filename = "D:\\zzx\\Stock\\Earning_Dates.csv";
		try{
			File file = new File(filename);
			if (!file.exists()){
				setup();
				return;
			}
			map = new HashMap<String, ArrayList<Date>>();
			BufferedReader br = new BufferedReader(new FileReader(file));		
			String line;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split(",");
				String symbol = elements[0];
				ArrayList<Date> dates = new ArrayList<Date>();
				for (int index = 1; index < elements.length; index ++){
					DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
					Date date = format.parse(elements[index]);
					dates.add(date);
				}
				map.put(symbol, dates);
			}
			br.close();
		}
		catch(Exception ex){
			ex.getMessage();
		}

	}
	
	private static void setup() {
		map = new HashMap<String, ArrayList<Date>>();
		
		ArrayList<String> symbolList = StockAPI.getSymbolList();
		for (int index = 0; index < symbolList.size(); index ++) {
			String symbol = symbolList.get(index);
			String filename = "D:\\zzx\\Stock\\Earning_Dates\\" + symbol + "_EarningDate.html";
			try{
				BufferedReader br = new BufferedReader(new FileReader(new File(filename)));		
				String line;
				String dateString;

				while ((line = br.readLine()) != null) {
					//TODO: should look at "Details" instead of "check-icon"
					if (line.contains("check-icon")){
						String[] elements = line.split("/");
						dateString = elements[0].substring(4) + "/" + elements[1] + "/" + elements[2].substring(0,2);
						DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
						Date date = format.parse(dateString);
						if (!map.containsKey(symbol)){
							ArrayList<Date> dates = new ArrayList<Date>();
							dates.add(date);
							map.put(symbol, dates);
							}
						else{
							map.get(symbol).add(date);
						}
					}
				}
				br.close();
			}
			catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
		
		String filename = "D:\\zzx\\Stock\\Earning_Dates.csv";
		try{
			File file = new File(filename);
			if (!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (String symbol : map.keySet()){
				bw.append(symbol);
				for (Date date : map.get(symbol)){
					DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
					bw.append("," + df.format(date));
				}
				bw.append("\n");
			}
			
			bw.close();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Check whether there is an earning date that is right after the current date. 
	 * @param symbol
	 * @param date
	 * @return
	 */
	public static boolean isCloseToEarningsDate(String symbol, Date date) {
		if (map == null) setMap();
		if (!map.containsKey(symbol)){
			return false;
		}
		ArrayList<Date> dates = map.get(symbol);
		for (int index = 0; index < dates.size(); index ++){
			long diff = dates.get(index).getTime() - date.getTime();
			if ((diff > 0) && (diff / (24 * 60 * 60 * 1000) < StockConst.CLOSE_TO_EARNING_DAYS)){
				return true;
			}
		}
		return false;
	}
	
}
