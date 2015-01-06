package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.StockUtil;

public class GeneralTest {

	public static void main(String args[]) {
		splitCSVLineTest();
//		regularExpressionTest();
	}
	
	
	private static void splitCSVLineTest() {
		String s = "\"AAWW\",    24,805,000";
		String[] splitted = StockUtil.splitCSVLine(s);
		for (int i = 0; i < splitted.length; i++) {
			System.out.println(splitted[i]);
		}
	}
	
	private static void regularExpressionTest() {
		String regEx = "[\\d]{1,2}[/]{1}[\\d]{1,2}[/]{1}[\\d]{2}[\\D]";
		String str = "1/2/14 ";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
        	System.out.println(matcher.group() + " " + matcher.start() + " " + matcher.end());
            
        }
		
				
	}
}
