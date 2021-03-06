package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StockUtil {
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
	private static final double maxEqualDifference = 1e-10; 
	
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

	
	public static boolean downloadURL(String urlString, String filename) {
		try {
			URL site = new URL(urlString);
		    ReadableByteChannel rbc = Channels.newChannel(site.openStream());
			FileOutputStream fos = new FileOutputStream(filename);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.flush();
			fos.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
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
	
	public static void downloadHTMLURLWithPost(String urlString, String filename, String urlParameters) {
		byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
			connection.setUseCaches(false);
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			dos.write(postData);
		    ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
			FileOutputStream fos = new FileOutputStream(filename);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.flush();
			fos.close();
			dos.close();
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
		createNewDirectory(f);
	}
	
	public static void createNewDirectory(File directory) {
		if (directory.exists()) return;
		directory.mkdir();		
	}
	
	/**
	 * ***********This function should be used cautiously***********
	 * @param directory
	 * @return
	 */
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(files != null){
	            for (int i = 0; i < files.length; i++) {
	                if(files[i].isDirectory()) deleteDirectory(files[i]);
	                else files[i].delete();	                
	            }
	        }
	    }
	    return (directory.delete());
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
		return simpleDateFormat.format(date);
	}
	
	public static String formatDate(LocalDate localDate) {
		return simpleDateFormat.format(localDate.toDate());
	}
	
	/**
	 * Parse a date string with date format yyyyMMdd.
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			date = simpleDateFormat.parse(dateString);
		}
		catch (Exception e) {
			
		}
		return date;
		
	}
	
	public static LocalDate parseLocalDate(String dateString) {
		if (dateString == null) return LocalDate.now();
		dateString = dateString.trim();
		if (dateString.length() == 0) return LocalDate.now();
		LocalDate localDate = null;
		try {
			localDate = dateTimeFormatter.parseLocalDate(dateString);
		}
		catch (Exception e) {
		
		}
		return localDate; 
	}
	
	public static String formatLocalDate(LocalDate localDate) {
		if (localDate == null) return null;
		return localDate.toString(dateTimeFormatter);
	}
	
	/**
	 * Format a price with two decimals.
	 * @param price
	 * @return
	 */
	public static String formatPrice(double price) {
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return decimalFormat.format(price);
	}
	
	/**
	 * Return true if two dates are close to each other.
	 * For example, if difference = 3, then 20141120 is close to 20141117, but not close to 20141116.
	 * @param dateOne
	 * @param dateTwo
	 * @param difference
	 * @return
	 */
	public static boolean isCloseDates(Date dateOne, Date dateTwo, int difference) {
		if (difference < 0) return false;
		LocalDate localDateOne = new LocalDate(dateOne);
		LocalDate localDateTwo = new LocalDate(dateTwo);
		LocalDate localDateOnePlus = localDateOne.plusDays(difference);
		//If the second date is between the first date and the first date + diff days then they are close.
		if (!localDateOne.isAfter(localDateTwo) && !localDateOnePlus.isBefore(localDateTwo)) return true;
		LocalDate localDateOneMinus = localDateOne.minusDays(difference);
		//If the second date is between the first date and the first date - diff days then they are close.
		if (!localDateOne.isBefore(localDateTwo) && !localDateOneMinus.isAfter(localDateTwo)) return true;
		return false;		
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
    
    public static boolean isEqualDouble(double doubleOne, double doubleTwo) {
    	if (Math.abs(doubleOne - doubleTwo) < maxEqualDifference) return true;
    	return false;
    }
    
    /**
     * Copy a file.
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
    
}
