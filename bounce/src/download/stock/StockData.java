package download.stock;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.net.MalformedURLException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class StockData {

	private static final String filename = "D:\\zzx\\Stock\\CSV\\companylist.csv";
	private String startDate; 
	private String endDate;
	private static final String DELIMITER = ",";
	public int lineNumber = 0;
	private BufferedReader br;
		
	private void startReadFile() throws Exception {
		this.br = new BufferedReader(new FileReader(filename));
	}

	private void closeFile() throws Exception {
		if (br == null) return;
		br.close();
	}
	
	private String nextLine() throws Exception {
		String line = null;
		if (br == null) return null;
		do {
			lineNumber++;
			line = br.readLine();
			if (line == null) return null;
		}
		while (!isLineValid(line));		
		return line;		
	}
	
	/*
	 startDate and endDate are strings of the form "MM/dd/yyyy"
	Download csv files from startDate to endDate. Input null, null to use the default dates. 
	startDate is 4/1/2010 by default. endDate is today by default.
	 */
	public StockData(String startDate, String endDate){		
		this.startDate = startDate;
		this.endDate = endDate;
	}	
	
	public void downloadStocks() throws Exception{
		String line;
		if (this.startDate == null)
			this.startDate = "4/1/2010";
		
		startReadFile();
		
		while ((line = nextLine()) != null){
			String stock = line.split(DELIMITER)[0];
			stock = stock.split("\"")[1]; //stock code is of the form "XXX" in the csv file
			downloadStock(stock);
			Thread.sleep(300);
		}
		
		closeFile();
	}
	
	private void downloadStock(String stock) throws Exception{
		String fileStock = "D:\\zzx\\Stock\\CSV\\"+stock+".csv";
		File file = new File(fileStock);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date fromDate = dateFormat.parse(startDate);
		Date toDate;
		if (endDate == null)
			toDate = new Date();
		else toDate = dateFormat.parse(endDate);
				
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		String startMonth = Integer.toString(cal.get(Calendar.MONTH));
        String startDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String startYear = Integer.toString(cal.get(Calendar.YEAR));
        cal.setTime(toDate);
		String endMonth = Integer.toString(cal.get(Calendar.MONTH));
        String endDay = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String endYear = Integer.toString(cal.get(Calendar.YEAR));
        String siteAddr = "http://ichart.finance.yahoo.com/table.csv?s="+stock+"&d="+endMonth+"&e="+endDay+"&f="+endYear+"&g=d&a="+startMonth+"&b="+startDay+"&c="+startYear+"&ignore=.csv";
		
        URL site = new URL(siteAddr);
        try {
		ReadableByteChannel rbc = Channels.newChannel(site.openStream());
		FileOutputStream fos = new FileOutputStream(file);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
        }
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private boolean isLineValid(String line) {
		if (lineNumber == 1) return false;
		return true;
	}
}
