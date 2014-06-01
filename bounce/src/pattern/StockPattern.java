package pattern;

import java.util.ArrayList;

import stock.SimpleLinearRegression;
import stock.StockCandle;
import stock.StockEnum.StockCandleDataType;

public class StockPattern {
	//=============Constant Definition=============
	//The constant name should follow the below naming convention:
	//<Function Name>_<Constant Name>
	private static final double TREND_UP_SLOPE = 0.5;
	private static final double TREND_DOWN_SLOPE = -0.5;
	private static final int TREND_DEFAULT_CANDLE_NUMBER = 5;
	private static final StockCandleDataType TREND_DEFAULT_DATA_TYPE = StockCandleDataType.CLOSE;
	
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
	
	private static final int HAMMER_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final int HANGING_MAN_MIN_TREND_CANDLE_NUMBER = HAMMER_MIN_TREND_CANDLE_NUMBER;
	
	private static final double ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE = 0.8;  
	private static final double ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE = 1.5; 
	private static final double ENGULF_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	
	
	private ArrayList<StockCandle> stockCandleArray;
	
	public ArrayList<StockCandle> getstockCandleArray() {
		return stockCandleArray;
	}

	public void setstockCandleArray(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}

	public StockPattern() {
		
	}
	
	/**
	 * Constructor. The stock candle array should always be normalized!
	 * Call static function stockCandleArray.normalizeStockCandle for normalization.
	 * @param stockCandleArray
	 */
	public StockPattern(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}
	
	/**
	 * @see isWhite(stockCandle)
	 */
	public boolean isWhite(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isWhite(stockCandle);
	}
	
	/**
	 * Return true if the current candle is a white candle.
	 * Definition:
	 * 1. Close > Open
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return true if the current candle is a white candle.
	 */
	public boolean isWhite(StockCandle stockCandle) {
		return ((stockCandle.close > stockCandle.open) ? true : false);
	}
	
	/**
	 * @see isBlack(stockCandle)
	 */
	public boolean isBlack(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isBlack(stockCandle);
	}
	
	/**
	 * Return true if the current candle is a black candle.
	 * Definition:
	 * 1. Close < Open
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return true if the current candle is a black candle.
	 */
	public boolean isBlack(StockCandle stockCandle) {
		return ((stockCandle.close < stockCandle.open) ? true : false);
	}
	
	/**
	 * Return true if the current candle is a white long day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length >= WHITE_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a white long day.
	 */
	public boolean isWhiteLongDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isWhite(stockCandle)) return false;
		if (stockCandle.getBodyLength() >= WHITE_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;		
	}
	
	/**
	 * Return true if the current candle is a black long day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length >= BLACK_LONG_DAY_MIN_BODY_LENGTH
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a black long day.
	 */
	public boolean isBlackLongDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isBlack(stockCandle)) return false;
		if (stockCandle.getBodyLength() >= BLACK_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a white short day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length <= WHITE_SHORT_DAY_MAX_BODY_LENGTH
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a white short day.
	 */
	public boolean isWhiteShortDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isWhite(stockCandle)) return false;
		if (stockCandle.getBodyLength() <= WHITE_SHORT_DAY_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a black short day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length <= BLACK_SHORT_DAY_MAX_BODY_LENGTH
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a black short day.
	 */
	public boolean isBlackShortDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isBlack(stockCandle)) return false;
		if (stockCandle.getBodyLength() <= BLACK_SHORT_DAY_MAX_BODY_LENGTH) return true;
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
	 * @param index The subscript in the stock candle array.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a white marubozu.
	 */
	public boolean isWhiteMarubozu(int index, StockCandleDataType... dataTypes) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isWhite(stockCandle)) return false;
		for (StockCandleDataType dataType : dataTypes) {
			if ((dataType == StockCandleDataType.OPEN) && (stockCandle.getLowerShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
			if ((dataType == StockCandleDataType.CLOSE) && (stockCandle.getUpperShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
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
	 * @param index The subscript in the stock candle array.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a black marubozu.
	 */
	public boolean isBlackMarubozu(int index, StockCandleDataType... dataTypes) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isBlack(stockCandle)) return false;
		for (StockCandleDataType dataType : dataTypes) {
			if ((dataType == StockCandleDataType.OPEN) && (stockCandle.getLowerShadowLength() > BLACK_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
			if ((dataType == StockCandleDataType.CLOSE) && (stockCandle.getUpperShadowLength() > BLACK_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
		}
		return true;
	}
	
	/**
	 * @see isDoji(stockCandle)
	 */
	public boolean isDoji(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;		
		return isDoji(stockCandle);
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
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the candle is a doji (十字线).
	 */
	public boolean isDoji(StockCandle stockCandle) {
		if (stockCandle.getBodyLength() <= DOJI_MAX_BODY_LENGTH) return true;
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
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isDoji(stockCandle)) return false;
		if (stockCandle.getTotalLength() >= LONG_LEGGED_DOJI_MIN_TOTAL_LENGTH) return true;
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
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isDoji(stockCandle)) return false;
		if ((stockCandle.getUpperShadowLength() >= GRAVESTONE_DOJI_MIN_UPPER_SHADOW_LENGTH) 
	     && (stockCandle.getLowerShadowLength() <= GRAVESTONE_DOJI_MAX_LOWER_SHADOW_LENGTH)) return true;
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
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a dragonfly doji (蜻蜓十字线).
	 */
	public boolean isDragonflyDoji(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		if (!isDoji(stockCandle)) return false;
		if ((stockCandle.getUpperShadowLength() <= DRAGONFLY_DOJI_MAX_UPPER_SHADOW_LENGTH) 
	     && (stockCandle.getLowerShadowLength() >= DRAGONFLY_DOJI_MIN_LOWER_SHADOW_LENGTH)) return true;
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
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a star to the last candle (星线).
	 */
	public boolean isStar(int index) {
		StockCandle currentStockCandle, previousStockCandle;
		if (index < 1) return false;
		currentStockCandle = stockCandleArray.get(index);
		if (currentStockCandle.getBodyLength() > STAR_MAX_BODY_LENGTH) return false;
		previousStockCandle = stockCandleArray.get(index - 1);
		if (StockCandle.getGapLength(previousStockCandle, currentStockCandle) >= STAR_MIN_GAP_LENGTH) return true;
		return false;
	}
	
	/**
	 * @see isPaperUmbrella(stockCandle)
	 */
	public boolean isPaperUmbrella(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isPaperUmbrella(stockCandle);
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
	 * 2. Upper shadow length <= PAPER_UMBRELLA_MAX_UPPER_SHADOW_LENGTH.
	 * 3. Lower shadow length >= PAPER_UMBRELLA_MIN_LOWER_SHADOW_LENGTH.
	 * @param StockCandle Stock candle object. Assume not null.
	 * @return True if the candle is a paper umbrella candle.
	 */
	public boolean isPaperUmbrella(StockCandle stockCandle) {
		if ((stockCandle.getBodyLength() <= PAPER_UMBRELLA_MAX_BODY_LENGTH)
		 && (stockCandle.getUpperShadowLength() <= PAPER_UMBRELLA_MAX_UPPER_SHADOW_LENGTH)
		 && (stockCandle.getLowerShadowLength() >= PAPER_UMBRELLA_MIN_LOWER_SHADOW_LENGTH))
			return true;
		return false;
	}
	
	/**
	 * Return true if the candle is a hammer. The trend before the hammer should be bearish.
	 * Example:
	 * □
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * Definition:
	 * 1. The candle is a paper umbrella.
	 * 2. Trend before the candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a hammer. The trend before the hammer should be bearish.
	 */
	public boolean isHammer(int index) {
		if (!isPaperUmbrella(index)) return false;
		int start = index - HAMMER_MIN_TREND_CANDLE_NUMBER + 1;
		if (start < 0) return false;
		return isTrendDown(start, index, TREND_DEFAULT_DATA_TYPE);
	}
	
	/**
	 * Return true if the candle is a white hammer. The trend before the hammer should be bearish.
	 * White hammer is more likely to reverse the trend than black hammer.
	 * Example:
	 * □
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a white hammer. The trend before the hammer should be bearish.
	 */
	public boolean isWhiteHammer(int index) {
		if (!isWhite(index)) return false;
		return isHammer(index);
	}
	
	/**
	 * Return true if the candle is a black hammer. The trend before the hammer should be bearish.
	 * Black hammer is less likely to reverse the trend than white hammer.
	 * Example:
	 * ■
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a black hammer. The trend before the hammer should be bearish.
	 */
	public boolean isBlackHammer(int index) {
		if (!isBlack(index)) return false;
		return isHammer(index);
	}

	/**
	 * Return true if the candle is a hanging man. The trend before the hammer should be bullish.
	 * Example:
	 * □
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * Definition:
	 * 1. The candle is a paper umbrella.
	 * 2. Trend before the candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a hanging man. The trend before the hammer should be bullish.
	 */
	public boolean isHangingMan(int index) {
		if (!isPaperUmbrella(index)) return false;
		int start = index - HANGING_MAN_MIN_TREND_CANDLE_NUMBER + 1;
		if (start < 0) return false;
		return isTrendUp(start, index, TREND_DEFAULT_DATA_TYPE);
	}
	
	/**
	 * Return true if the candle is a white hanging man. The trend before the hammer should be bullish.
	 * White hanging man is less likely to reverse the trend than black hanging man.
	 * Example:
	 * □
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a white hanging man. The trend before the hammer should be bullish.
	 */
	public boolean isWhiteHangingMan(int index) {
		if (!isWhite(index)) return false;
		return isHangingMan(index);
	}
	
	/**
	 * Return true if the candle is a black hanging man. The trend before the hammer should be bullish.
	 * Black hanging man is more likely to reverse the trend than white hanging man.
	 * Example:
	 * ■
	 * |
	 * |
	 * |
	 * |  
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the candle is a black hanging man. The trend before the hammer should be bullish.
	 */
	public boolean isBlackHangingMan(int index) {
		if (!isBlack(index)) return false;
		return isHangingMan(index);
	}
	
	
	/**
	 * @see isBullishEngulfing(index, engulfShadows)
	 */
	public boolean isBullishEngulfing(int index) {
		return isBullishEngulfing(index, false);
	}
	
	/**
	 * Return true if the current candle is a white candle and it engulfs the previous black candle.
	 * The trend before the engulfing should be bearish. 
	 * Example:
	 * 
	 *     |
	 *  |  □
	 *  ■  □
	 *  ■  □
	 *  ■  □
	 *  |  □
	 *     |  
	 *     
	 * Definition:
	 * 1. First candle is a black candle. Second candle is a white candle.
	 * 2. Second candle engulfs the first candle. If the body of the second candle engulfs the shadows of the first candle as well,
	 * then it is more likely to reverse the trend.
	 * 3. First candle body length <= ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * second candle body length.
	 * 4. Second candle volume >= ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * first candle volume.
	 * 5. Trend before the second day is bearish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the current candle is a white candle and it engulfs the previous black candle.
	 */
	public boolean isBullishEngulfing(int index, boolean engulfShadows) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isWhite(currentStockCandle)) return false;
		if (!isBlack(previousStockCandle)) return false;
		if (engulfShadows) {
			if (!((currentStockCandle.open < previousStockCandle.low) && (currentStockCandle.close > previousStockCandle.high))) return false;
		}
		else {
			if (!((currentStockCandle.open < previousStockCandle.close) && (currentStockCandle.close > previousStockCandle.open))) return false;
		}
		//if (previousStockCandle.getBodyLength() > ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * currentStockCandle
		return false;
	}
	
	/**
	 * @see isBearishEngulfing(index, engulfShadows)
	 */
	public boolean isBearishEngulfing(int index) {
		return isBearishEngulfing(index, false);
	}
	
	/**
	 * Return true if the current candle is a black candle and it engulfs the previous white candle.
	 * The trend before the engulfing should be bullish. 
	 * Example:
	 * 
	 *     |
	 *  |  ■
	 *  □  ■
	 *  □  ■
	 *  □  ■
	 *  |  ■
	 *     |
	 *       
	 * Definition:
	 * 1. First candle is a white candle. Second candle is a black candle.
	 * 2. Second candle engulfs the first candle. If the body of the second candle engulfs the shadows of the first candle as well,
	 * then it is more likely to reverse the trend.
	 * 3. First candle body length <= ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * second candle body length.
	 * 4. Second candle volume >= ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * first candle volume.
	 * 5. Trend before the second day is bullish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the current candle is a black candle and it engulfs the previous white candle.
	 */
	public boolean isBearishEngulfing(int index, boolean engulfShadows) {
		return false;
	}
	
	
	
	
	public boolean isTrendUp(int start, int end, StockCandleDataType dataType) {
		SimpleLinearRegression slr = new SimpleLinearRegression();
		double slope;
		for (int i = start; i < end; i++) {
			if (dataType == StockCandleDataType.OPEN) slr.data.add(stockCandleArray.get(i).getOpen());
			else if (dataType == StockCandleDataType.CLOSE) slr.data.add(stockCandleArray.get(i).getClose());
			else if (dataType == StockCandleDataType.HIGH) slr.data.add(stockCandleArray.get(i).getHigh());
			else if (dataType == StockCandleDataType.LOW) slr.data.add(stockCandleArray.get(i).getLow());			
		}
		
		slope = slr.getSlope();
		if (slope >= TREND_UP_SLOPE) return true;
		else return false;
	}
	
	public boolean isTrendDown(int start, int end, StockCandleDataType dataType) {
		SimpleLinearRegression slr = new SimpleLinearRegression();
		double slope;
		for (int i = start; i < end; i++) {
			if (dataType == StockCandleDataType.OPEN) slr.data.add(stockCandleArray.get(i).getOpen());
			else if (dataType == StockCandleDataType.CLOSE) slr.data.add(stockCandleArray.get(i).getClose());
			else if (dataType == StockCandleDataType.HIGH) slr.data.add(stockCandleArray.get(i).getHigh());
			else if (dataType == StockCandleDataType.LOW) slr.data.add(stockCandleArray.get(i).getLow());			
		}
		
		slope = slr.getSlope();
		if (slope <= TREND_UP_SLOPE) return true;
		else return false;
	}
}
