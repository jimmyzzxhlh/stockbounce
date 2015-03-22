package indicator;

import intraday.IntraDayAnalysisYahoo;
import intraday.IntraDayPriceVolumeMap;
import intraday.IntraDayStockCandleArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockDayTradingDistribution;
import stock.StockSellRate;
import stock.StockEnum.StockCandleDataType;
import util.StockUtil;
import yahoo.YahooParser;

/**
 * Compute the average cost indicator.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockAverageCostIndicator {

	private StockCandleArray stockCandleArray;
	private ArrayList<HashMap<Integer, Long>> priceVolumeMapArray;
	private double[] dayTradingDistribution;
	private double[] sellRates;
	private String symbol;
	
	/**
	 * Read daily stock candles from a file.
	 * @param file
	 */
	public StockAverageCostIndicator(File file) {
		stockCandleArray = StockAPI.getStockCandleArrayYahoo(file);
		symbol = stockCandleArray.getSymbol();
		setMapping();
	}
	
	/**
	 * Read daily stock candles given a symbol.
	 * @param symbol
	 */
	public StockAverageCostIndicator(String symbol) {
		this.symbol = symbol;
		stockCandleArray = YahooParser.readCSVFile(symbol);
		setMapping();
	}
	
	public StockAverageCostIndicator(StockCandleArray stockCandleArray) {
		this.symbol = stockCandleArray.getSymbol();
		this.stockCandleArray = stockCandleArray;
		setMapping();
	}
	
	public void destroy() {
		stockCandleArray = null;
		if (priceVolumeMapArray != null) {
			priceVolumeMapArray.clear();
		}
		priceVolumeMapArray = null;
		dayTradingDistribution = null;
		sellRates = null;
		symbol = null;		
		
	}
	
	/**
	 * Return number of stock candles.
	 * @return
	 */
	public int size() {
		return stockCandleArray.size();
	}
	
	public StockCandleArray getStockCandleArray() {
		return stockCandleArray;
	}
	
	/**
	 * Set the following mapping:
	 * 1. Price volume mapping.
	 * 2. Day trading distribution.
	 * 3. Sell rate distribution.
	 */
	private void setMapping() {
		//Set price volume mapping
		priceVolumeMapArray = new ArrayList<HashMap<Integer, Long>>();
		ArrayList<IntraDayStockCandleArray> mdStockCandleArray = new ArrayList<IntraDayStockCandleArray>();
		try {
			mdStockCandleArray = IntraDayAnalysisYahoo.getIntraDayStockCandleArray(symbol);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < stockCandleArray.size(); i++) {
			StockCandle stockCandle = stockCandleArray.get(i);
			Date date = stockCandle.date;
			long intraDayVolume = stockCandle.getVolume();
			IntraDayStockCandleArray intraDayCandleArray = getIntraDayData(date, mdStockCandleArray);
			if (intraDayCandleArray != null){
				HashMap<Integer, Long> priceVolumeMap = IntraDayPriceVolumeMap.getMap(intraDayCandleArray, intraDayVolume);
				priceVolumeMapArray.add(priceVolumeMap);
				System.out.println("Reading " + symbol + "-" + date.toString());
			}
			else {
				HashMap<Integer, Long> priceVolumeMap = IntraDayPriceVolumeMap.getMap(stockCandle);	
				priceVolumeMapArray.add(priceVolumeMap);
			}		
		}
		
		//Set day trading distribution
		dayTradingDistribution = StockDayTradingDistribution.getDayTradingDistribution();
		
		//Set daily sell rate mapping
		sellRates = StockSellRate.getSellRates();
		
	}
	
	//Given a date, find the corresponding intraday candle array if we have the intraday data for that date
	private IntraDayStockCandleArray getIntraDayData(Date date, ArrayList<IntraDayStockCandleArray> mdStockCandleArray) {
		for (int i = 0; i < mdStockCandleArray.size(); i++){
			IntraDayStockCandleArray array = mdStockCandleArray.get(i);
			if (array.getDate().equals(date)){
				return array;
			}
		}
		return null;
	}
	
	/**
	 * Get average cost for the current candle.
	 * We look back for a few days. For each past candle, we do the following:
	 * 1. Compute the turnover rate. 
	 * 2. According to the day trading distribution, we will know how many shares are traded on the same day.
	 * We subtract those shares from the volume.
	 * 3. According to the sell rate distribution, we will know how many shares are sold at the current time.
	 * We subtract those shares from the volume as well.
	 * 4. For the remaining shares, compute the average cost of those shares.
	 * We use this indicator as the current cost for all investors. The strategy will be:
	 * 1. Buy long: We should wait until the stock price is almost on the same level as the current cost.
	 * Once the stock price starts to raise up, we will initiate a buy.
	 * 2. Sell long: One strategy is to check whether the cost of the shares significant raise up. For example,
	 * if huge volumes are traded at the top, then they will largely increase the cost and will be a signal of top.
	 * Another strategy could be waiting until the price is below the cost of the shares so people start to lose money.
	 * Strategy for buy short / sell short is similar.
	 * @param index
	 * @return
	 */
	public double getAverageCost(int index) {
		HashMap<Integer, Long> priceVolumeMap = new HashMap<Integer, Long>(); 
		for (int i = index; i >= Math.max(0, index - StockIndicatorConst.MAX_SELL_PERIOD + 1); i--) {
			int lookbackDays = index - i;
			double turnoverRate = stockCandleArray.getTurnoverRate(i);
			int turnoverRateInt = (int) Math.round(turnoverRate * 1000);
			if (turnoverRateInt > 1000) turnoverRateInt = 1000;
			
			HashMap<Integer, Long> currentPriceVolumeMap = priceVolumeMapArray.get(i);
			
			for (Entry<Integer, Long> entry : currentPriceVolumeMap.entrySet()) {
				int price = entry.getKey();
				long volume = entry.getValue();
//				try {
				volume = Math.round(volume * (1 - dayTradingDistribution[turnoverRateInt]) * (1 - sellRates[lookbackDays]));
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//					System.out.println(symbol + " " + turnoverRateInt);
//					System.exit(1);
//				}
				if (priceVolumeMap.containsKey(price)) {
					priceVolumeMap.put(price, priceVolumeMap.get(price) + volume);
				}
				else {
					priceVolumeMap.put(price, volume);
				}
			}
		}
		
		//Compute all the average cost.
		long totalCapital = 0;
		long totalVolume = 0;
		for (Entry<Integer, Long> entry : priceVolumeMap.entrySet()) {
			int price = entry.getKey();
			long volume = entry.getValue();
			totalCapital += price * volume;
			totalVolume += volume;
		}
		//Price in the price/volume mapping was multiplied by 100.
		return totalCapital / totalVolume / 100.0;
	}
	
	public double[] getAverageCostArray() {
		double averageCostArray[] = new double[stockCandleArray.size()];
		for (int i = 0; i < stockCandleArray.size(); i++) {
			averageCostArray[i] = getAverageCost(i);
		}
		return averageCostArray;
	}
	
	
	public ArrayList<Integer> getReverseUpDatesIndex() {
		ArrayList<Integer> dateIndexList = new ArrayList<Integer>();
		//Start with the second day as we need the previous close price to calculate the difference.
		for (int i = 1; i < stockCandleArray.size(); i++) {
			Date date = stockCandleArray.getDate(i);
			double previousClose = stockCandleArray.getClose(i - 1);
			double previousAverageCost = getAverageCost(i - 1);
			double previousAverageCostDiff = (previousClose - previousAverageCost) / previousClose;
			double close = stockCandleArray.getClose(i);
			double averageCost = getAverageCost(i);
			double averageCostDiff = (close - averageCost) / close;
//			if (date.equals(StockUtil.parseDate("20130909"))) { 
//				System.out.println("Previous close: " + previousClose);
//				System.out.println("Previous average cost: " + previousAverageCost);
//				System.out.println("Previous average cost diff: " + previousAverageCostDiff);
//				System.out.println("Close: " + close);
//				System.out.println("Average cost: " + averageCost);
//				System.out.println("Average cost diff: " + averageCostDiff);
//			}
			if (previousAverageCostDiff > 0.01) continue;
			if (averageCostDiff < 0) continue;
			if (averageCostDiff - previousAverageCostDiff < StockIndicatorConst.AVERAGE_COST_REVERSE_UP_MIN_DIFFERENCE) continue;
			dateIndexList.add(i);
		}
		return dateIndexList;
	}
	
	
	
}
