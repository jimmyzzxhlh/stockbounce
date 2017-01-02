package stock;

import stock.StockEnum.Exchange;

/**
 * Static class for returning stock exchange information.
 * @author jimmyzzxhlh-Dell
 *
 */
public class ExchangeUtil {
	
	public static String getCompanyListFilename(Exchange exchange) {
		switch (exchange) {
		case NASDAQ:
			return StockConst.COMPANY_LIST_NASDAQ_FILENAME;
		case NYSE:
			return StockConst.COMPANY_LIST_NYSE_FILENAME;
		case SSE:
			return StockConst.COMPANY_LIST_SSE_FILENAME;
		case SZSE:
			return StockConst.COMPANY_LIST_SZSE_FILENAME;
		case AMEX:
			return null;
		}
		return null;
	}
	
	public static String getIntraDayDirectory(Exchange exchange) {
		switch (exchange) {
		case NASDAQ:
		case NYSE:
			return StockConst.INTRADAY_DIRECTORY_PATH_YAHOO;
		case SSE:
			return StockConst.INTRADAY_DIRECTORY_SSE_PATH;
		case SZSE:
			return StockConst.INTRADAY_DIRECTORY_SZSE_PATH;
		case AMEX:
			return null;
		}
		return null;
	}
	
	public static String getIntraDayDirectory(Exchange exchange, String symbol) {
		return getIntraDayDirectory(exchange) + symbol + "\\";
	}
	
	/**
	 * Return the file path for the merged data. For now the file name is the symbol itself.
	 * If in the far future the data file is too big, we can consider split them by year.
	 * @param symbol
	 * @return
	 */
	public static String getIntraDayMergedFilePath(Exchange exchange, String symbol) {
		return getIntraDayDirectory(exchange) + symbol + ".txt";
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
	
	public static String getURLSymbol(Exchange exchange, String symbol) {
		String urlSymbol = symbol;
		if (symbol.startsWith("^")) {
			urlSymbol = symbol.replace("^", "%5E");
		}
    	switch (exchange) {
    	case SSE:
    		urlSymbol = urlSymbol + ".SS";
    		break;
    	case SZSE:
    		urlSymbol = urlSymbol + ".SZ";
    		break;
    	default:
    		break;
    	}
    	return urlSymbol;
	}
}
