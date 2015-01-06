package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockUtil {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	public static double getRoundTwoDecimals(double input) {
		return Math.round(input * 100.0) / 100.0;
	}
	
	public static double getRoundDecimals(double input, int decimalCount) {
		double power = Math.pow(10, decimalCount);
		return Math.round(input * 1.0 * power) / power;
	}
	
	public static int getIntervalNum(double start, double end, double interval) {
		if (end < start) return 0;
		return (int)((end - start) * 1.0 / interval) + 1;
	}
	
	public static double changeRate(double original, double result){
		if ((original <= 0)||(result <= 0))
			return 0;
		return (result - original) / original;
	}
	
	/**
	 * Get symbol name from the filename.
	 * If the filename is an indicator file like <Stock Symbol>_Indicators.csv, then we get the string before "_".
	 * If the filename is like <Stock Symbol>.csv, then we get the string before ".".
	 * @param filename
	 * @return
	 */
	public static String getSymbolFromFileName(String filename) {
		int underscorePos = filename.indexOf("_");
		if (underscorePos > 0) return filename.substring(0, underscorePos).trim();
		int dotPos = filename.indexOf(".");
		if (dotPos > 0) return filename.substring(0, dotPos).trim();
		return null;
	}
	
	public static String getSymbolFromFile(File file) {
		if (file == null) return null;
		return getSymbolFromFileName(file.getName());	
	}
	
	public static double getMax(double[] input) {
		double max = -1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
			}
		}
		return max;
	}
		
	public static double getMin(double[] input) {
		double min = 1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] < min) {
				min = input[i];
			}
		}
		return min;
	}
	
	public static int getMax(int[] input) {
		int max = (int) -1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
			}
		}
		return max;
	}
		
	public static int getMin(int[] input) {
		int min = (int) 1e10; 
		for (int i = 0; i < input.length; i++) {
			if (input[i] < min) {
				min = input[i];
			}
		}
		return min;
	}
	
	public static void setLimitForArray(double[] inputArray, double min, double max, double shift) {
		for (int i = 0; i < inputArray.length; i++) {
			inputArray[i] += shift;
			if (inputArray[i] < min) inputArray[i] = min;
			if (inputArray[i] > max) inputArray[i] = max;
		}
	}

	
	public static void downloadURL(String urlString, String filename) {
		try {
			URL site = new URL(urlString);
		    ReadableByteChannel rbc = Channels.newChannel(site.openStream());
			FileOutputStream fos = new FileOutputStream(filename);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.flush();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadHTMLURL(String urlString, String filename) {
		try {
			URL site = new URL(urlString);
		    ReadableByteChannel rbc = Channels.newChannel(getURLInputStream(site));
			FileOutputStream fos = new FileOutputStream(filename);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.flush();
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static InputStream getURLInputStream(URL url) {
		try {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0");
			return urlConnection.getInputStream();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void createNewDirectory(String directory) {
		File f = new File(directory);
		if (f.exists()) return;
		f.mkdir();
	}
	
	/** 
	 * Finds the value of the given enumeration by name, case-insensitive. 
	 * Throws an IllegalArgumentException if no match is found.  
	 **/
	public static <T extends Enum<T>> T getEnumFromString(Class<T> enumeration, String name) {
	    for (T enumValue : enumeration.getEnumConstants()) {
	        if (enumValue.name().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }
	    throw new IllegalArgumentException("There is no value with name '" + name + " in Enum " + enumeration.getClass().getName());        
	}
	
	/**
	 * Split a CSV line.
	 * Remove the quote as well.
	 * @param line
	 * @return
	 */
	public static String[] splitCSVLine(String line) {
		String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		for (int i = 0; i < data.length; i++) {
			data[i] = data[i].trim().replaceAll("^\"|\"$", "").trim();
		}
		return data;
	}
	
	public static void sleepThread(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pressAnyKeyToContinue() {
		try {
			System.out.println("Press any key to continue...");
			System.in.read();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean fileExists(String filename) {
		File file = new File(filename);
		return file.exists();
	}
	
	/**
	 * Return a string with date format yyyyMMdd.
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
	
	/**
	 * Parse a date string with date format yyyyMMdd.
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return date;
		
	}
	
	/**
	 * Given a full filename, return a filename without extension.
	 * @param filename
	 * @return
	 */
    public static String getFilenameWithoutExtension(String filename) {
        if (filename == null) return null;
        int pos = filename.lastIndexOf(".");
        if (pos == -1) return filename;
        return filename.substring(0, pos);
    }
}
