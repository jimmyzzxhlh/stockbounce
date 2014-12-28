package indicator;

import intraday.IntraDayPriceVolumeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import stock.StockAPI;
import stock.StockCandle;
import stock.StockCandleArray;
import stock.StockDayTradingDistribution;
import stock.StockSellRate;
import yahoo.YahooParser;

public class StockAverageCostIndicator {

	private StockCandleArray stockCandleArray;
	private ArrayList<HashMap<Integer, Long>> priceVolumeMapArray;
	private double[] dayTradingDistribution;
	private double[] sellRates;
	
	public StockAverageCostIndicator(File file) {
		stockCandleArray = StockAPI.getStockCandleArrayYahoo(file);
		setMapping();
	}
	
	public StockAverageCostIndicator(String symbol) {
		stockCandleArray = YahooParser.readCSVFile(symbol);
		setMapping();
	}
	
	public int size() {
		return stockCandleArray.size();
	}
	
	public StockCandleArray getStockCandleArray() {
		return stockCandleArray;
	}
	
	private void setMapping() {
		//Set price volume mapping
		priceVolumeMapArray = new ArrayList<HashMap<Integer, Long>>();
		for (int i = 0; i < stockCandleArray.size(); i++) {
			StockCandle stockCandle = stockCandleArray.get(i);
			HashMap<Integer, Long> priceVolumeMap = IntraDayPriceVolumeMap.getMap(stockCandle);
			priceVolumeMapArray.add(priceVolumeMap);
			
		}
		
		//Set day trading distribution
		dayTradingDistribution = StockDayTradingDistribution.getDayTradingDistribution();
		
		//Set daily sell rate mapping
		sellRates = StockSellRate.getSellRates();
		
	}
	
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
				volume = Math.round(volume * (1 - dayTradingDistribution[turnoverRateInt]) * (1 - sellRates[lookbackDays]));
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
		return totalCapital * 1.0 / totalVolume;
	}
	
	
	
	
}
