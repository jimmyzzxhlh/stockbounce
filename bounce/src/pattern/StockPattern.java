package pattern;

import java.util.ArrayList;

import stock.StockEnum.StockPriceDataType;
import stock.StockPrice;

public class StockPattern {
	//=============Constant Definition=============
	//The constant name should follow the below naming convention:
	//<Function Name>_<Constant Name>
	private static final double WHITE_LONG_DAY_MIN_BODY_LENGTH = 10;	
	private static final double BLACK_LONG_DAY_MIN_BODY_LENGTH = WHITE_LONG_DAY_MIN_BODY_LENGTH;	
	private static final double WHITE_SHORT_DAY_MAX_BODY_LENGTH = 5;	
	private static final double BLACK_SHORT_DAY_MAX_BODY_LENGTH = WHITE_SHORT_DAY_MAX_BODY_LENGTH;
	
	private static final double WHITE_MARUBOZU_MAX_SHADOW_LENGTH = 1;	
	private static final double BLACK_MARUBOZU_MAX_SHADOW_LENGTH = WHITE_MARUBOZU_MAX_SHADOW_LENGTH;
	
	private static final double DOJI_MAX_BODY_LENGTH = 2;	
	private static final double LONG_LEGGED_DOJI_MIN_TOTAL_LENGTH = 10;	
	private static final double GRAVESTONE_DOJI_MIN_UPPER_SHADOW_LENGTH = 8;	
	private static final double GRAVESTONE_DOJI_MAX_LOWER_SHADOW_LENGTH = 1;	
	private static final double DRAGONFLY_DOJI_MIN_LOWER_SHADOW_LENGTH = GRAVESTONE_DOJI_MIN_UPPER_SHADOW_LENGTH;	
	private static final double DRAGONFLY_DOJI_MAX_UPPER_SHADOW_LENGTH = GRAVESTONE_DOJI_MAX_LOWER_SHADOW_LENGTH;
		
	private static final double STAR_MAX_BODY_LENGTH = 2;
	private static final double STAR_MIN_GAP_LENGTH = 2;
	
	private static final double PAPER_UMBRELLA_MAX_BODY_LENGTH = 2;
	private static final double PAPER_UMBRELLA_MIN_LOWER_SHADOW_LENGTH = 8;
	private static final double PAPER_UMBRELLA_MAX_UPPER_SHADOW_LENGTH = 1;
	
	private ArrayList<StockPrice> stockPriceArray;
	
	public ArrayList<StockPrice> getStockPriceArray() {
		return stockPriceArray;
	}

	public void setStockPriceArray(ArrayList<StockPrice> stockPriceArray) {
		this.stockPriceArray = stockPriceArray;
	}

	public StockPattern() {
		
	}
	
	/**
	 * Constructor. The stock price array should always be normalized!
	 * Call static function StockPriceArray.normalizeStockPrice for normalization.
	 * @param stockPriceArray
	 */
	public StockPattern(ArrayList<StockPrice> stockPriceArray) {
		this.stockPriceArray = stockPriceArray;
	}
	
	/**
	 * Return true if the current candle is a white candle.
	 * Definition:
	 * 1. Close > Open
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a white candle.
	 */
	public boolean isWhite(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		return isWhite(stockPrice);
	}
	
	/**
	 * Return true if the current candle is a white candle.
	 * Definition:
	 * 1. Close > Open
	 * @param stockPrice Stock price object. Assume not null.
	 * @return true if the current candle is a white candle.
	 */
	public boolean isWhite(StockPrice stockPrice) {
		return ((stockPrice.close > stockPrice.open) ? true : false);
	}
	
	/**
	 * Return true if the current candle is a black candle.
	 * Definition:
	 * 1. Open > Close
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a black candle.
	 */
	public boolean isBlack(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		return isBlack(stockPrice);
	}
	
	/**
	 * Return true if the current candle is a black candle.
	 * Definition:
	 * 1. Close < Open
	 * @param stockPrice Stock price object. Assume not null.
	 * @return true if the current candle is a black candle.
	 */
	public boolean isBlack(StockPrice stockPrice) {
		return ((stockPrice.close < stockPrice.open) ? true : false);
	}
	
	/**
	 * Return true if the current candle is a white long day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length >= WHITE_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a white long day.
	 */
	public boolean isWhiteLongDay(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isWhite(stockPrice)) return false;
		if (stockPrice.getBodyLength() >= WHITE_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;		
	}
	
	/**
	 * Return true if the current candle is a black long day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length >= BLACK_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a black long day.
	 */
	public boolean isBlackLongDay(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isBlack(stockPrice)) return false;
		if (stockPrice.getBodyLength() >= BLACK_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a white short day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length <= WHITE_SHORT_DAY_MAX_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a white short day.
	 */
	public boolean isWhiteShortDay(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isWhite(stockPrice)) return false;
		if (stockPrice.getBodyLength() <= WHITE_SHORT_DAY_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a black short day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length <= BLACK_SHORT_DAY_MAX_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the current candle is a black short day.
	 */
	public boolean isBlackShortDay(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isBlack(stockPrice)) return false;
		if (stockPrice.getBodyLength() <= BLACK_SHORT_DAY_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a white marubozu.
	 * Example (white marubozu for open price):
	 *  |
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 * Definition:
	 * 1. White candle.
	 * 2. Upper shadow length or lower shadow length <= WHITE_MARUBOZU_MAX_SHADOW_LENGTH
	 * @param index The subscript in the stock price array.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a white marubozu.
	 */
	public boolean isWhiteMarubozu(int index, StockPriceDataType... dataTypes) {
		
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isWhite(stockPrice)) return false;
		for (StockPriceDataType dataType : dataTypes) {
			if ((dataType == StockPriceDataType.OPEN) && (stockPrice.getLowerShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
			if ((dataType == StockPriceDataType.CLOSE) && (stockPrice.getUpperShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
		}
		return true;
	}
	
	/**
	 * Return true if the current candle is a black marubozu.
	 * Example (black marubozu for open price):
	 *  |
	 *  |
	 *  ■
	 *  ■
	 *  ■
	 *  ■
	 * Definition:
	 * 1. Black candle.
	 * 2. Upper shadow length or lower shadow length <= BLACK_MARUBOZU_MAX_SHADOW_LENGTH
	 * @param index The subscript in the stock price array.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a black marubozu.
	 */
	public boolean isBlackMarubozu(int index, StockPriceDataType... dataTypes) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isBlack(stockPrice)) return false;
		for (StockPriceDataType dataType : dataTypes) {
			if ((dataType == StockPriceDataType.OPEN) && (stockPrice.getLowerShadowLength() > BLACK_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
			if ((dataType == StockPriceDataType.CLOSE) && (stockPrice.getUpperShadowLength() > BLACK_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
		}
		return true;
	}
	
	/**
	 * Return true if the candle is a doji (十字线).
	 * Example:
	 *  |
	 *  |
	 *  |
	 * -+-
	 *  |
	 *  |
	 *  |
	 * Definition:
	 * 1. Body Length <= DOJI_MAX_BODY_LENGTH
	 * @param index The subscript in the stock price array.
	 * @return True if the candle is a doji (十字线).
	 */
	public boolean isDoji(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;		
		return isDoji(stockPrice);
	}
	
	/**
	 * Return true if the candle is a doji (十字线).
	 * Example:
	 *  |
	 *  |
	 *  |
	 * -+-
	 *  |
	 *  |
	 *  |
	 * Definition:
	 * 1. Body Length <= DOJI_MAX_BODY_LENGTH
	 * @param stockPrice Stock price object. Assume not null.
	 * @return True if the candle is a doji (十字线).
	 */
	public boolean isDoji(StockPrice stockPrice) {
		if (stockPrice.getBodyLength() <= DOJI_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the candle is a long legged doji (长腿十字线).
	 * Example:
	 *  |
	 *  |
	 *  |
	 * -+-
	 *  |
	 *  |
	 *  |
	 * Definition:
	 * 1. Candle is a doji.
	 * 2. Total length including shadows >= LONG_LEGGED_DOJI_MIN_TOTAL_LENGTH.
	 * @param index
	 * @return True if the candle is a long legged doji (长腿十字线).
	 */
	public boolean isLongLeggedDoji(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isDoji(stockPrice)) return false;
		if (stockPrice.getTotalLength() >= LONG_LEGGED_DOJI_MIN_TOTAL_LENGTH) return true;
		return false;						
	}
	
	/**
	 * Return true if the candle is a gravestone doji (墓碑十字线).
	 * Example:
	 *  |
	 *  |
	 *  |
	 * -+-
	 * Definition:
	 * 1. Candle is a doji.
	 * 2. Upper shadow length >= GRAVESTONE_DOJI_MIN_UPPER_SHADOW_LENGTH.
	 * 3. Lower shadow Length <= GRAVESTONE_DOJI_MAX_LOWER_SHADOW_LENGTH.
	 * @param index
	 * @return True if the candle is a gravestone doji (墓碑十字线).
	 */
	public boolean isGravestoneDoji(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isDoji(stockPrice)) return false;
		if ((stockPrice.getUpperShadowLength() >= GRAVESTONE_DOJI_MIN_UPPER_SHADOW_LENGTH) 
	     && (stockPrice.getLowerShadowLength() <= GRAVESTONE_DOJI_MAX_LOWER_SHADOW_LENGTH)) return true;
		return false;		
	}
	
	/**
	 * Return true if the candle is a dragonfly doji (蜻蜓十字线).
	 * Example:
	 * -+-
	 *  |
	 *  |
	 *  |
	 * Definition:
	 * 1. Candle is a doji.
	 * 2. Upper shadow length <= DRAGONFLY_DOJI_MAX_UPPER_SHADOW_LENGTH.
	 * 3. Lower shadow Length <= DRAGONFLY_DOJI_MIN_LOWER_SHADOW_LENGTH.
	 * @param index
	 * @return True if the candle is a dragonfly doji (蜻蜓十字线).
	 */
	public boolean isDragonflyDoji(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if (!isDoji(stockPrice)) return false;
		if ((stockPrice.getUpperShadowLength() <= DRAGONFLY_DOJI_MAX_UPPER_SHADOW_LENGTH) 
	     && (stockPrice.getLowerShadowLength() >= DRAGONFLY_DOJI_MIN_LOWER_SHADOW_LENGTH)) return true;
		return false;		
	}
	
	/**
	 * Return true if the candle is a star to the last candle (星线).
	 * Example:
	 *     |
	 *     □
	 *     |
	 * 
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 *  |
	 * Definition:
	 * 1. Current candle body length <= STAR_MAX_BODY_LENGTH.
	 * 2. Gap length between the current candle and the last candle >= STAR_MIN_GAP_LENGTH.
	 * @param index
	 * @return True if the candle is a star to the last candle (星线).
	 */
	public boolean isStar(int index) {
		StockPrice currentStockPrice, lastStockPrice;
		if (index < 1) return false;
		currentStockPrice = stockPriceArray.get(index);
		if (currentStockPrice.getBodyLength() > STAR_MAX_BODY_LENGTH) return false;
		lastStockPrice = stockPriceArray.get(index - 1);
		if (StockPrice.getGapLength(lastStockPrice, currentStockPrice) >= STAR_MIN_GAP_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the candle is a paper umbrella candle.
	 * Example:
	 * □
	 * |
	 * |
	 * |
	 * |
	 * 
	 * Definition:
	 * 1. Body length <= PAPER_UMBRELLA_MAX_BODY_LENGTH.
	 * 2. Upper shadow length <= 
	 * @param index
	 * @return
	 */
	public boolean isPaperUmbrella(int index) {
		StockPrice stockPrice = stockPriceArray.get(index);
		if (stockPrice == null) return false;
		if ((stockPrice.getBodyLength() <= PAPER_UMBRELLA_MAX_BODY_LENGTH)
		 && (stockPrice.getUpperShadowLength() <= PAPER_UMBRELLA_MAX_UPPER_SHADOW_LENGTH)
		 && (stockPrice.getLowerShadowLength() >= PAPER_UMBRELLA_MIN_LOWER_SHADOW_LENGTH))
			return true;
		return false;
	}

}
