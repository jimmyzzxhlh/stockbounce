package stock;

import stock.StockEnum.Exchange;

/**
 * Static class for returning stock exchange information.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockExchange {
	
	public static String getCompanyListFilename(Exchange exchange) {
		switch (exchange) {
		case NASDAQ:
			return StockConst.COMPANY_LIST_NASDAQ_FILENAME;
		case NYSE:
			return StockConst.COMPANY_LIST_NYSE_FILENAME;
		case AMEX:
			return null;
		}
		return null;
	}
	
	public static String getDownloadCompanyListURL(Exchange exchange) {
		switch (exchange) {
		case NASDAQ:
			return "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download";
		case NYSE:
			return "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nyse&render=download";
		case AMEX:
			return null;
		}
		return null;
	}
}
