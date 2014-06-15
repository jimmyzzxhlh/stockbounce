package pattern;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import stock.SimpleLinearRegression;
import stock.StockCandle;
import stock.StockEnum.StockCandleDataType;
import stock.StockEnum.StockPatternType;

public class StockPattern {
	//=============Constant Definition=============
	//The constant name should follow the below naming convention:
	//<Function Name>_<Constant Name>
	private static final double GAP_UP_MIN_LENGTH = 3;
	private static final double GAP_DOWN_MIN_LENGTH = GAP_UP_MIN_LENGTH;
	
	
	//TODO: When computing trend, do we count the last candle?
	//For example, for meeting lines, should we count the trend from the previous candle, or should we count
	//the trend before the previous candle? Since the previous candle is already a long day, it can affect
	//the trend slope if it is included.
	private static final double TREND_UP_SLOPE = 1;
	private static final double TREND_DOWN_SLOPE = -1;
	private static final int TREND_DEFAULT_CANDLE_NUMBER = 10;
	private static final StockCandleDataType TREND_DEFAULT_DATA_TYPE = StockCandleDataType.CLOSE;
	
	private static final double WHITE_LONG_DAY_MIN_BODY_LENGTH = 10;	
	private static final double BLACK_LONG_DAY_MIN_BODY_LENGTH = WHITE_LONG_DAY_MIN_BODY_LENGTH;	
	private static final double WHITE_SHORT_DAY_MAX_BODY_LENGTH = 5;	
	private static final double BLACK_SHORT_DAY_MAX_BODY_LENGTH = WHITE_SHORT_DAY_MAX_BODY_LENGTH;
	
	private static final double WHITE_MARUBOZU_MAX_SHADOW_LENGTH = 1;	
	private static final double BLACK_MARUBOZU_MAX_SHADOW_LENGTH = WHITE_MARUBOZU_MAX_SHADOW_LENGTH;
	
	private static final double DOJI_MAX_BODY_LENGTH = 2;	
	private static final double LONG_LEGGED_DOJI_MIN_TOTAL_LENGTH = 10;
	private static final double DOJI_STAR_MAX_TOTAL_LENGTH = 3;
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
	private static final int ENGULF_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final double HARAMI_SECOND_DAY_BODY_LENGTH_MAX_PERCENTAGE = ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE;
	private static final double HARAMI_SECOND_DAY_VOLUME_MAX_PERCENTAGE = ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE;
	private static final int HARAMI_MIN_TREND_CANDLE_NUMBER = ENGULF_MIN_TREND_CANDLE_NUMBER;
	
	private static final double INVERTED_HAMMER_MAX_BODY_LENGTH = PAPER_UMBRELLA_MAX_BODY_LENGTH;
	private static final double INVERTED_HAMMER_MIN_UPPER_SHADOW_LENGTH = PAPER_UMBRELLA_MIN_LOWER_SHADOW_LENGTH;
	private static final double INVERTED_HAMMER_MAX_LOWER_SHADOW_LENGTH = PAPER_UMBRELLA_MAX_UPPER_SHADOW_LENGTH;
	private static final int INVERTED_HAMMER_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final double SHOOTING_STAR_MAX_BODY_LENGTH = INVERTED_HAMMER_MAX_BODY_LENGTH;
	private static final double SHOOTING_STAR_MIN_UPPER_SHADOW_LENGTH = INVERTED_HAMMER_MIN_UPPER_SHADOW_LENGTH;
	private static final double SHOOTING_STAR_MAX_LOWER_SHADOW_LENGTH = INVERTED_HAMMER_MAX_LOWER_SHADOW_LENGTH;
	private static final int SHOOTING_STAR_MIN_TREND_CANDLE_NUMBER = INVERTED_HAMMER_MIN_TREND_CANDLE_NUMBER;
	
	private static final double PIERCING_LINE_MIN_BODY_LENGTH_PERCENTAGE = 0.5;
	private static final int PIERCING_LINE_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final double DARK_CLOUD_COVER_MIN_BODY_LENGTH_PERCENTAGE = PIERCING_LINE_MIN_BODY_LENGTH_PERCENTAGE;
	private static final int DARK_CLOUD_COVER_MIN_TREND_CANDLE_NUMBER = PIERCING_LINE_MIN_TREND_CANDLE_NUMBER;
	
	private static final int BULLISH_DOJI_STAR_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final int BEARISH_DOJI_STAR_MIN_TREND_CANDLE_NUMBER = BULLISH_DOJI_STAR_MIN_TREND_CANDLE_NUMBER;
	
	private static final double MORNING_STAR_MIN_BODY_LENGTH_PERCENTAGE = 0.5;
	private static final double EVENING_STAR_MIN_BODY_LENGTH_PERCENTAGE = MORNING_STAR_MIN_BODY_LENGTH_PERCENTAGE;
	private static final int MORNING_STAR_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final int EVENING_STAR_MIN_TREND_CANDLE_NUMBER = MORNING_STAR_MIN_TREND_CANDLE_NUMBER;
	
	private static final int BULLISH_TRI_STAR_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final int BEARISH_TRI_STAR_MIN_TREND_CANDLE_NUMBER = BULLISH_TRI_STAR_MIN_TREND_CANDLE_NUMBER;
	
	private static final double UPSIDE_GAP_TWO_CROWS_ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE = 0.8;
	private static final int UPSIDE_GAP_TWO_CROWS_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	
	private static final double BULLISH_MEETING_LINE_MAX_CLOSE_DIFF = 1;
	private static final int BULLISH_MEETING_LINE_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final double BEARISH_MEETING_LINE_MAX_CLOSE_DIFF = BULLISH_MEETING_LINE_MAX_CLOSE_DIFF;
	private static final int BEARISH_MEETING_LINE_MIN_TREND_CANDLE_NUMBER = BULLISH_MEETING_LINE_MIN_TREND_CANDLE_NUMBER;
	
	private static final double BULLISH_BELT_HOLD_MIN_JUMP_LENGTH = 3;
	private static final int BULLISH_BELT_HOLD_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	private static final double BEARISH_BELT_HOLD_MIN_JUMP_LENGTH = BULLISH_BELT_HOLD_MIN_JUMP_LENGTH;
	private static final int BEARISH_BELT_HOLD_MIN_TREND_CANDLE_NUMBER = BULLISH_BELT_HOLD_MIN_TREND_CANDLE_NUMBER;
	
	private static final int UNIQUE_THREE_RIVER_BOTTOM_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	
	private static final int THREE_WHITE_SOLDIERS_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	
	private static final double ADVANCE_BLOCK_MIN_UPPER_SHADOW_LENGTH = 5;
	private static final int ADVANCE_BLOCK_MIN_TREND_CANDLE_NUMBER = TREND_DEFAULT_CANDLE_NUMBER;
	
	private ArrayList<StockCandle> stockCandleArray;
	
	private String symbol;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public ArrayList<StockCandle> getstockCandleArray() {
		return stockCandleArray;
	}

	public void setstockCandleArray(ArrayList<StockCandle> stockCandleArray) {
		this.stockCandleArray = stockCandleArray;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
	
	public String getDate(int index) {
		return dateFormat.format(stockCandleArray.get(index).date);
	}
	
	/**
	 * @see isTrendUp(start, end, dataType)
	 */
	public boolean isTrendUp(int start, int end) {
		return isTrendUp(start, end, TREND_DEFAULT_DATA_TYPE);
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
	
	/**
	 * @see isTrendDown(start, end, dataType)
	 */
	public boolean isTrendDown(int start, int end) {
		return isTrendDown(start, end, TREND_DEFAULT_DATA_TYPE);
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
		if (slope <= TREND_DOWN_SLOPE) return true;
		else return false;
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
	 * @see isWhiteLongDay(stockCandle)
	 */
	public boolean isWhiteLongDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isWhiteLongDay(stockCandle);		
	}
	
	/**
	 * Return true if the current candle is a white long day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length >= WHITE_LONG_DAY_MIN_BODY_LENGTH
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the current candle is a white long day.
	 */
	public boolean isWhiteLongDay(StockCandle stockCandle) {
		if (!isWhite(stockCandle)) return false;
		if (stockCandle.getBodyLength() >= WHITE_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;		
	}
	
	/**
	 * @see isBlackLongDay(stockCandle)
	 */
	public boolean isBlackLongDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isBlackLongDay(stockCandle);
	}
	
	/**
	 * Return true if the current candle is a black long day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length >= BLACK_LONG_DAY_MIN_BODY_LENGTH
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the current candle is a black long day.
	 */
	public boolean isBlackLongDay(StockCandle stockCandle) {
		if (!isBlack(stockCandle)) return false;
		if (stockCandle.getBodyLength() >= BLACK_LONG_DAY_MIN_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * @see isWhiteShortDay(stockCandle)
	 */
	public boolean isWhiteShortDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isWhiteShortDay(stockCandle);
	}
	
	/**
	 * Return true if the current candle is a white short day.
	 * Definition:
	 * 1. Close > Open.
	 * 2. Body length <= WHITE_SHORT_DAY_MAX_BODY_LENGTH
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the current candle is a white short day.
	 */
	public boolean isWhiteShortDay(StockCandle stockCandle) {
		if (!isWhite(stockCandle)) return false;
		if (stockCandle.getBodyLength() <= WHITE_SHORT_DAY_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * @see isBlackShortDay(stockCandle)
	 */
	public boolean isBlackShortDay(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isBlackShortDay(stockCandle);
	}
	
	/**
	 * Return true if the current candle is a black short day.
	 * Definition:
	 * 1. Close < Open.
	 * 2. Body length <= BLACK_SHORT_DAY_MAX_BODY_LENGTH
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the current candle is a black short day.
	 */
	public boolean isBlackShortDay(StockCandle stockCandle) {
		if (!isBlack(stockCandle)) return false;
		if (stockCandle.getBodyLength() <= BLACK_SHORT_DAY_MAX_BODY_LENGTH) return true;
		return false;
	}
	
	/**
	 * @see isWhiteMarubozu(stockCandle)
	 */
	public boolean isWhiteMarubozu(int index, StockCandleDataType... dataTypes) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isWhiteMarubozu(stockCandle, dataTypes);
	}
	
	/**
	 * Return true if the current candle is a white marubozu.
	 * Example (white marubozu for open price):
	 * 
	 *  |
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 * 
	 * Definition:
	 * 1. White candle.
	 * 2. Upper shadow length or lower shadow length <= WHITE_MARUBOZU_MAX_SHADOW_LENGTH
	 * @param stockCandle StockCandle object. Assume not null.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a white marubozu.
	 */
	public boolean isWhiteMarubozu(StockCandle stockCandle, StockCandleDataType... dataTypes) {
		if (!isWhite(stockCandle)) return false;
		for (StockCandleDataType dataType : dataTypes) {
			if ((dataType == StockCandleDataType.OPEN) && (stockCandle.getLowerShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
			if ((dataType == StockCandleDataType.CLOSE) && (stockCandle.getUpperShadowLength() > WHITE_MARUBOZU_MAX_SHADOW_LENGTH)) return false;
		}
		return true;
	}
	
	/**
	 * @see isBlackMarubozu(stockCandle)
	 */
	public boolean isBlackMarubozu(int index, StockCandleDataType... dataTypes) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isBlackMarubozu(stockCandle, dataTypes);
	}
	
	/**
	 * Return true if the current candle is a black marubozu.
	 * Example (black marubozu for open price):
	 * 
	 *  |
	 *  |
	 *  ■
	 *  ■
	 *  ■
	 *  ■
	 *  
	 * Definition:
	 * 1. Black candle.
	 * 2. Upper shadow length or lower shadow length <= BLACK_MARUBOZU_MAX_SHADOW_LENGTH
	 * @param stockCandle Stock Candle object. Assume not null.
	 * @param dataTypes Should either be Open or Close. Can pass both.
	 * @return True if the current candle is a black marubozu.
	 */
	public boolean isBlackMarubozu(StockCandle stockCandle, StockCandleDataType... dataTypes) {
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
	 * @param index The subscript in the stock candle array.
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
     * @see isDojiStar(stockCandle)
	 */
	public boolean isDojiStar(int index) {
		StockCandle stockCandle = stockCandleArray.get(index);
		if (stockCandle == null) return false;
		return isDojiStar(stockCandle);	
	}
	
	/**
	 * Return true if the candle is a doji star (十字星).
	 * Example:
	 * 
	 *  |
	 * -+-
	 *  |
	 *  
	 * Definition:
	 * 1. Candle is a doji.
	 * 2. Total length including shadows <= DOJI_STAR_MAX_TOTAL_LENGTH.
	 * @param stockCandle Stock candle object. Assume not null.
	 * @return True if the candle is a doji star (十字星).
	 */
	public boolean isDojiStar(StockCandle stockCandle) {
		if (!isDoji(stockCandle)) return false;
		if (stockCandle.getTotalLength() <= DOJI_STAR_MAX_TOTAL_LENGTH) return true;
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
	 * 
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
	 *  
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
	 * 
	 *  □
	 *  |
	 *  |
	 *  |
	 *  |
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
	 * 
	 *  □
	 *  |
	 *  |
	 *  | 
	 *  |  
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
		return isTrendDown(start, index);
	}
	
	/**
	 * Return true if the candle is a white hammer. The trend before the hammer should be bearish.
	 * White hammer is more likely to reverse the trend than black hammer.
	 * Example:
	 * 
	 *  □
	 *  |
	 *  | 
	 *  | 
	 *  |  
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
	 * 
	 *  ■
	 *  |
	 *  |
	 *  |
	 *  |  
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
	 * 
	 *  □
	 *  |
	 *  |
	 *  |
	 *  |  
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
		return isTrendUp(start, index);
	}
	
	/**
	 * Return true if the candle is a white hanging man. The trend before the hammer should be bullish.
	 * White hanging man is less likely to reverse the trend than black hanging man.
	 * Example:
	 * 
	 *  □
	 *  |
	 *  |
	 *  |
	 *  |  
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
	 * 
	 *  ■
	 *  |
	 *  |
	 *  |
	 *  |  
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
	 * Return true if the current candle is a white candle and it engulfs the previous black candle (看涨吞没).
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
	 * 1. First candle is a black candle. Second candle is a white candle, and it is not a short day (should it be a long day?).
	 * 2. Second candle engulfs the first candle. If the body of the second candle engulfs the shadows of the first candle as well,
	 * then it is more likely to reverse the trend.
	 * 3. First candle body length <= ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * second candle body length.
	 * 4. Second candle volume >= ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * first candle volume.
	 * 5. Trend before the second day is bearish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the current candle is a white candle and it engulfs the previous black candle (看涨吞没).
	 */
	public boolean isBullishEngulfing(int index, boolean engulfShadows) {
		if (index < ENGULF_MIN_TREND_CANDLE_NUMBER) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isWhite(currentStockCandle)) return false;
		if (isWhiteShortDay(currentStockCandle)) return false; //Should it be replaced by long day check?
		if (!isBlack(previousStockCandle)) return false;
		if (engulfShadows) {
			if (!((currentStockCandle.open < previousStockCandle.low) && (currentStockCandle.close > previousStockCandle.high))) return false;
		}
		else {
			if (!((currentStockCandle.open < previousStockCandle.close) && (currentStockCandle.close > previousStockCandle.open))) return false;
		}
		if (previousStockCandle.getBodyLength() > ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * currentStockCandle.getBodyLength()) return false;
		if (currentStockCandle.getVolume() < Math.round(ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * previousStockCandle.getVolume())) return false;
		int start = index - ENGULF_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * @see isBearishEngulfing(index, engulfShadows)
	 */
	public boolean isBearishEngulfing(int index) {
		return isBearishEngulfing(index, false);
	}
	
	/**
	 * Return true if the current candle is a black candle and it engulfs the previous white candle (看跌吞没).
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
	 * 1. First candle is a white candle. Second candle is a black candle, and it is not a short day (should it be a long day?).
	 * 2. Second candle engulfs the first candle. If the body of the second candle engulfs the shadows of the first candle as well,
	 * then it is more likely to reverse the trend.
	 * 3. First candle body length <= ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * second candle body length.
	 * 4. Second candle volume >= ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * first candle volume.
	 * 5. Trend before the second day is bullish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the current candle is a black candle and it engulfs the previous white candle (看跌吞没).
	 */
	public boolean isBearishEngulfing(int index, boolean engulfShadows) {
		if (index < ENGULF_MIN_TREND_CANDLE_NUMBER) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isBlack(currentStockCandle)) return false;
		if (isBlackShortDay(currentStockCandle)) return false; //Should it be replaced by long day check?
		if (!isWhite(previousStockCandle)) return false;
		if (engulfShadows) {
			if (!((currentStockCandle.open > previousStockCandle.high) && (currentStockCandle.close < previousStockCandle.low))) return false;
		}
		else {
			if (!((currentStockCandle.open > previousStockCandle.close) && (currentStockCandle.close < previousStockCandle.open))) return false;
		}
		if (previousStockCandle.getBodyLength() > ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * currentStockCandle.getBodyLength()) return false;
		if (currentStockCandle.getVolume() < Math.round(ENGULF_SECOND_DAY_VOLUME_MIN_PERCENTAGE * previousStockCandle.getVolume())) return false;
		int start = index - ENGULF_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles has a form of three outside up (三外升).
	 * Example:
	 * 
	 *        |
	 *     |  □
	 *  |  □  □
	 *  ■  □  □
	 *  ■  □  |
	 *  ■  □
	 *  |  □
	 *     |  
	 * 
	 * Definition:
	 * 1. Current candle is a white candle.
	 * 2. Current candle's close > Previous candle's close.
	 * 3. The two candles before the current candle has a form of bullish engulfing. 
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the recent three candles has a form of three outside up (三外升).
	 */
	public boolean isThreeOutsideUp(int index, boolean engulfShadows) {
		if (index < 2) return false;  //Theoretically, index should >= ENGULF_MIN_TREND_CANDLE_NUMBER + 2
		StockCandle currentCandle, previousCandle;
		currentCandle = stockCandleArray.get(index);
		previousCandle = stockCandleArray.get(index - 1);
		if ((currentCandle == null) || (previousCandle == null)) return false;
		if (!isWhite(currentCandle)) return false;
		if (currentCandle.getClose() <= previousCandle.getClose()) return false;
		return isBullishEngulfing(index - 1, engulfShadows);		
	}
	
	/**
	 * Return true if the recent three candles has a form of three outside down (三外降).
	 * Example:
	 *
	 *     |
	 *  |  ■
	 *  □  ■
	 *  □  ■  |
	 *  □  ■  ■
	 *  |  ■  ■
	 *     |  ■
	 *        |
	 *     
	 * Definition:
	 * 1. Current candle is a black candle.
	 * 2. Current candle's close < Previous candle's close.
	 * 3. The two candles before the current candle has a form of bearish engulfing. 
	 * @param index The subscript in the stock candle array.
	 * @param engulfShadows True if the second candle needs to engulf the shadows of the first candle.
	 * @return True if the recent three candles has a form of three outside down (三外降).
	 */
	public boolean isThreeOutsideDown(int index, boolean engulfShadows) {
		if (index < 2) return false;  //Theoretically, index should >= ENGULF_MIN_TREND_CANDLE_NUMBER + 2
		StockCandle currentCandle, previousCandle;
		currentCandle = stockCandleArray.get(index);
		previousCandle = stockCandleArray.get(index - 1);
		if ((currentCandle == null) || (previousCandle == null)) return false;
		if (!isBlack(currentCandle)) return false;
		if (currentCandle.getClose() >= previousCandle.getClose()) return false;
		return isBearishEngulfing(index - 1, engulfShadows);	
	}
	
	/**
	 * Return true if the previous candle is a black candle and harami the current white candle (看涨孕线).
	 * Example:
	 * 
	 *  |  
	 *  ■  |
	 *  ■  □
	 *  ■  □
	 *  ■  □
	 *  ■  |
	 *  |  
	 * 
	 * Definition:
	 * 1. First candle is a black candle. Second candle is a white candle. (Should the first candle be a long day?)
	 * 2. First candle harami the second candle. (TODO: If the first candle harami the second candle's shadows
	 * as well, is it more likely to reverse the trend?)
	 * 3. First candle body length * HARAMI_SECOND_DAY_BODY_LENGTH_MAX_PERCENTAGE >= Second candle body length.
	 * 4. Second candle volume > first candle volume. (TODO: Is this really possible?)
	 * 5. Trend before the second candle is bearish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param haramiShadows True if the first candle needs to harami the shadows of the second candle.
	 * @param haramiDoji True if the second candle is a doji.
	 * @return True if the previous candle is a black candle and harami the current white candle (看涨孕线).
	 */
	public boolean isBullishHarami(int index, boolean haramiShadows, boolean haramiDoji) {
		if (index < 1) return false;  //Theoritically, index should >= HARAMI_MIN_TREND_CANDLE_NUMBER + 1
		StockCandle currentCandle, previousCandle;
		currentCandle = stockCandleArray.get(index);
		previousCandle = stockCandleArray.get(index - 1);
		if ((currentCandle == null) || (previousCandle == null)) return false;
		if (!isBlackLongDay(previousCandle)) return false;
		if (!isWhite(currentCandle)) return false;
		if (haramiShadows) {
			if (!((previousCandle.open > currentCandle.high) && (previousCandle.close < currentCandle.low))) return false;
		}
		else {
			if (!((previousCandle.open > currentCandle.close) && (previousCandle.close < currentCandle.open))) return false;
		}
		if (haramiDoji) {
			if (!isDoji(currentCandle)) return false;
		}
		else {
			if (previousCandle.getBodyLength() * HARAMI_SECOND_DAY_BODY_LENGTH_MAX_PERCENTAGE < currentCandle.getBodyLength()) return false;
		}
		if (currentCandle.volume <= previousCandle.volume) return false;
		int start = index - HARAMI_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	
	/**
	 * Return true if the previous candle is a white candle and it harami the current black candle (看跌孕线).
	 * Example:
	 * 
	 *  |  
	 *  □  |
	 *  □  ■
	 *  □  ■
	 *  □  ■
	 *  □  |
	 *  |  
	 * 
	 * Definition:
	 * 1. First candle is a white candle. Second candle is a black candle. (Should the first candle be a long day?)
	 * 2. First candle harami the second candle. (TODO: If the first candle harami the second candle's shadows
	 * as well, is it more likely to reverse the trend?)
	 * 3. First candle body length * HARAMI_SECOND_DAY_BODY_LENGTH_MAX_PERCENTAGE >= Second candle body length.
	 * 4. Second candle volume <= HARAMI_SECOND_DAY_VOLUME_MAX_PERCENTAGE * first candle volume.
	 * 5. Trend before the second candle is bullish (Not counting the second day itself).
	 * @param index The subscript in the stock candle array.
	 * @param haramiShadows True if the first candle needs to harami the shadows of the second candle.
	 * @param haramiDoji True if the second candle is a doji.
	 * @return True if the previous candle is a white candle and it harami the current black candle (看跌孕线).
	 */
	public boolean isBearishHarami(int index, boolean haramiShadows, boolean haramiDoji) {
		if (index < 1) return false;  //Theoritically, index should >= HARAMI_MIN_TREND_CANDLE_NUMBER + 1
		StockCandle currentCandle, previousCandle;
		currentCandle = stockCandleArray.get(index);
		previousCandle = stockCandleArray.get(index - 1);
		if ((currentCandle == null) || (previousCandle == null)) return false;
		if (!isWhiteLongDay(previousCandle)) return false;
		if (!isBlack(currentCandle)) return false;
		if (haramiShadows) {
			if (!((previousCandle.open < currentCandle.low) && (previousCandle.close > currentCandle.high))) return false;
		}
		else {
			if (!((previousCandle.open < currentCandle.close) && (previousCandle.close > currentCandle.open))) return false;
		}
		if (haramiDoji) {
			if (!isDoji(currentCandle)) return false;
		}
		else {
			if (previousCandle.getBodyLength() * HARAMI_SECOND_DAY_BODY_LENGTH_MAX_PERCENTAGE < currentCandle.getBodyLength()) return false;
		}
		if (currentCandle.volume > HARAMI_SECOND_DAY_VOLUME_MAX_PERCENTAGE * previousCandle.volume) return false;
		int start = index - HARAMI_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles has a form of three inside up (三内升).
	 * Example:
	 * 
	 *        |
	 *  |     □
	 *  ■  |  □
	 *  ■  □  □
	 *  ■  □  □
	 *  ■  □  |
	 *  ■  |
	 *  |  
	 * 
	 * Definition:
	 * 1. Current candle is a white candle.
	 * 2. Current candle's close > First candle's open
	 * 3. The two candles before the current candle has a form of bullish harami. 
	 * @param index The subscript in the stock candle array.
	 * @param haramiShadows True if the first candle needs to harami the shadows of the second candle.
	 * @param haramiDoji True if the second candle is a doji.
	 * @return True if the recent three candles has a form of three inside up (三内升).
	 */
	public boolean isThreeInsideUp(int index, boolean haramiShadows, boolean haramiCross, boolean haramiDoji) {
		if (index < 2) return false;  //Theoritically, index should >= HARAMI_MIN_TREND_CANDLE_NUMBER + 2
		StockCandle currentCandle, secondPreviousCandle;
		currentCandle = stockCandleArray.get(index);
		secondPreviousCandle = stockCandleArray.get(index - 2);
		if ((currentCandle == null) || (secondPreviousCandle == null)) return false;
		if (!isWhite(currentCandle)) return false;
		if (currentCandle.close <= secondPreviousCandle.open) return false;
		if (isBullishHarami(index - 1, haramiShadows, haramiDoji)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles has a form of three inside down (三内降).
	 * Example:
	 * 
	 *  |  
	 *  □  |
	 *  □  ■  |
	 *  □  ■  ■
	 *  □  ■  ■
	 *  □  |  ■
	 *  |     ■
	 *        |
	 *        
	 * Definition:
	 * 1. Current candle is a black candle.
	 * 2. Current candle's close < First candle's open
	 * 3. The two candles before the current candle has a form of bearish harami. 
	 * @param index The subscript in the stock candle array.
	 * @param haramiShadows True if the first candle needs to harami the shadows of the second candle.
	 * @param haramiDoji True if the second candle is a doji.
	 * @return True if the recent three candles has a form of three inside down (三内降).
	 */
	public boolean isThreeInsideDown(int index, boolean haramiShadows, boolean haramiCross, boolean haramiDoji) {
		if (index < 2) return false;  //Theoritically, index should >= HARAMI_MIN_TREND_CANDLE_NUMBER + 2
		StockCandle currentCandle, secondPreviousCandle;
		currentCandle = stockCandleArray.get(index);
		secondPreviousCandle = stockCandleArray.get(index - 2);
		if ((currentCandle == null) || (secondPreviousCandle == null)) return false;
		if (!isBlack(currentCandle)) return false;
		if (currentCandle.close > secondPreviousCandle.open) return false;
		if (isBearishHarami(index - 1, haramiShadows, haramiDoji)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is an inverted hammer (倒锤子线).
	 * Example:
	 * 
	 *  |
	 *  ■
	 *  ■
	 *  ■  |
	 *  ■  |
	 *  |  |   
	 *     ■
	 *      
	 * Definition:
	 * 1. The candle has a small body length <= INVERTED_HAMMER_MAX_BODY_LENGTH.
	 * 2. The candle has a long upper shadow >= INVERTED_HAMMER_MIN_UPPER_SHADOW_LENGTH.
	 * 3. The candle has a very small lower shadow <= INVERTED_HAMMER_MAX_LOWER_SHADOW_LENGTH.
	 * 4. Trend before the current candle is bearish.
	 * TODO: It is not necessary that there is a gap between the current candle and previous candle.   
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is an inverted hammer (倒锤子线).
	 */
	public boolean isInvertedHammer(int index) {
		StockCandle stockCandle;
		if (index <= INVERTED_HAMMER_MIN_TREND_CANDLE_NUMBER) return false;
		stockCandle = stockCandleArray.get(index);
		if (stockCandle.getBodyLength() > INVERTED_HAMMER_MAX_BODY_LENGTH) return false;
		if (stockCandle.getUpperShadowLength() < INVERTED_HAMMER_MIN_UPPER_SHADOW_LENGTH) return false;
		if (stockCandle.getLowerShadowLength() > INVERTED_HAMMER_MAX_LOWER_SHADOW_LENGTH) return false;
		int start = index - INVERTED_HAMMER_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a shooting star (流星线).
	 * Example:
	 * 
	 *     |
	 *     |
	 *     |
	 *     ■
	 *     
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 *  |   
	 *   
	 *      
	 * Definition:
	 * 1. The candle has a small body length <= SHOOTING_STAR_MAX_BODY_LENGTH.
	 * 2. The candle has a long upper shadow >= SHOOTING_STAR_MIN_UPPER_SHADOW_LENGTH.
	 * 3. The candle has a very small lower shadow <= SHOOTING_STAR_MAX_LOWER_SHADOW_LENGTH.
	 * 4. There is a gap up between the current candle and previous candle (stimulate people to take profits). 
	 * 5. Trend before the current candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a shooting star (流星线).
	 */
	public boolean isShootingStar(int index) {
		StockCandle stockCandle;
		if (index <= SHOOTING_STAR_MIN_TREND_CANDLE_NUMBER) return false;
		stockCandle = stockCandleArray.get(index);
		if (stockCandle.getBodyLength() > SHOOTING_STAR_MAX_BODY_LENGTH) return false;
		if (stockCandle.getUpperShadowLength() < SHOOTING_STAR_MIN_UPPER_SHADOW_LENGTH) return false;
		if (stockCandle.getLowerShadowLength() > SHOOTING_STAR_MAX_LOWER_SHADOW_LENGTH) return false;
		if (!hasGapUp(index)) return false;
		int start = index - SHOOTING_STAR_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * @see hasGapUp(index, false)
	 */
	public boolean hasGapUp(int index) {
		return hasGapUp(index, false);
	}
	
	/**
	 * @see hasGapUp(previousStockCandle, currentStockCandle, useShadows)
	 */
	public boolean hasGapUp(int index, boolean useShadows) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		return hasGapUp(previousStockCandle, currentStockCandle, useShadows);		
	}
	
	/**
	 * @see hasGapUp(previousStockCandle, currentStockCandle, useShadows)
	 */
	public boolean hasGapUp(StockCandle previousStockCandle, StockCandle currentStockCandle) {
		return hasGapUp(previousStockCandle, currentStockCandle, false);
	}
	
	/**
	 * Return true if there is a gap up between current candle and previous candle (向上跳空).
	 * Example:
	 * 
	 *     |
	 *     □
	 *     □
	 *     |
	 * 
	 *  |
	 *  □
	 *  □
	 *  □
	 *  |
	 * 
	 * Notice that we are just computing gap here. If two candles form dark cloud cover, then the gap
	 * is filled so there is no gap up, even though the open price of the current candle jumps up from
	 * the close price of the previous candle.
	 * 
	 *     |
	 *     ■
	 *     ■
	 *     ■
	 *  |  ■
	 *  □  ■
	 *  □  |
	 *  □
	 *  |
	 *
	 * Definition:
	 * If useShadows = True:
	 * 1. Gap top = current candle's low.
	 * 2. Gap bottom = previous candle's high.
	 * 
	 * If useShadows = False:
	 * 1. If current candle is a white candle, then gap top = current candle's open.
	 * 2. If current candle is a black candle, then gap top = current candle's close.
	 * 3. If previous candle is a white candle, then gap bottom = previous candle's close.
	 * 4. If previous candle is a black candle, then gap bottom = previous candle's open.
	 * 
	 * A gap up exists if gap top >= gap bottom + GAP_MIN_LENGTH.  
	 *  
	 * @param previousStockCandle Previous stock candle object. Assume not null.
	 * @param currentStockCandle Current stock candle object. Assume not null.
	 * @param useShadows True if there is still a gap if shadows are also counted in.
	 * @return True if there is a gap up between current candle and previous candle (向上跳空).
	 */
	public boolean hasGapUp(StockCandle previousStockCandle, StockCandle currentStockCandle, boolean useShadows) {
		double gapTop, gapBottom;
		gapTop = gapBottom = 0;
		if (useShadows) {
			gapTop = currentStockCandle.low;
			gapBottom = previousStockCandle.high;
		}
		else {
			if (isWhite(currentStockCandle)) gapTop = currentStockCandle.open;
			if (isBlack(currentStockCandle)) gapTop = currentStockCandle.close;
			if (isWhite(previousStockCandle)) gapBottom = previousStockCandle.close;
			if (isBlack(previousStockCandle)) gapBottom = previousStockCandle.open;
		}
		if (gapTop >= gapBottom + GAP_UP_MIN_LENGTH) return true;
		return false;
	}
	
	/**
	 * @see hasGapDown(index, false)
	 */
	public boolean hasGapDown(int index) {
		return hasGapDown(index, false);
	}
	
	
	/**
	 * @see hasGapDown(previousStockCandle, currentStockCandle, useShadows)
	 */
	public boolean hasGapDown(int index, boolean useShadows) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		return hasGapDown(previousStockCandle, currentStockCandle, useShadows);	
	}
	
	/**
	 * @see hasGapDown(previousStockCandle, currentStockCandle, useShadows)
	 */
	public boolean hasGapDown(StockCandle previousStockCandle, StockCandle currentStockCandle) {
		return hasGapDown(previousStockCandle, currentStockCandle, false);
	}
	/**
	 * Return true if there is a gap down between current candle and previous candle (向下跳空).
	 * Example:
	 * 
	 *  |
	 *  ■
	 *  ■
	 *  |
	 * 
	 *    |
	 *    ■
	 *    ■
	 *    ■
	 *    |
	 * 
	 * Notice that we are just computing gap here. If two candles form piercing line, then the gap
	 * is filled so there is no gap down, even though the open price of the current candle jumps down from
	 * the close price of the previous candle.
	 * 
	 *  |
	 *  ■
	 *  ■
	 *  ■  |
	 *  ■  □
	 *  |  □
	 *     □
	 *     |
	 *  
	 * Definition:
	 * If useShadows = True:
	 * 1. Gap bottom = current candle's high.
	 * 2. Gap top = previous candle's low.
	 * 
	 * If useShadows = False:
	 * 1. If current candle is a white candle, then gap bottom = current candle's close.
	 * 2. If current candle is a black candle, then gap bottom = current candle's open.
	 * 3. If previous candle is a white candle, then gap top = previous candle's open.
	 * 4. If previous candle is a black candle, then gap top = previous candle's close.
	 * 
	 * A gap up exists if gap top >= gap bottom + GAP_MIN_LENGTH.
	 *   
	 * @param previousStockCandle Previous stock candle object. Assume not null.
	 * @param currentStockCandle Current stock candle object. Assume not null.
	 * @param useShadows True if there is still a gap if shadows are also counted in.
	 * @return True if there is a gap down between current candle and previous candle (向下跳空).
	 */
	public boolean hasGapDown(StockCandle previousStockCandle, StockCandle currentStockCandle, boolean useShadows) {
		double gapTop, gapBottom;
		gapTop = gapBottom = 0;
		if (useShadows) {
			gapBottom = currentStockCandle.high;
			gapTop = previousStockCandle.low;
		}
		else {
			if (isWhite(currentStockCandle)) gapBottom = currentStockCandle.close;
			if (isBlack(currentStockCandle)) gapBottom = currentStockCandle.open;
			if (isWhite(previousStockCandle)) gapTop = previousStockCandle.open;
			if (isBlack(previousStockCandle)) gapTop = previousStockCandle.close;
		}
		if (gapTop >= gapBottom + GAP_DOWN_MIN_LENGTH) return true;
		return false;	
	}
	
	/**
	 * Return the value between the current candle's open price and the previous candle's close price,
	 * If the value is > 0, then the current candle's open price jumps up from the previous candle's close price.
	 * If the value is < 0, then the current candle's open price jumps down from the previous candle's close price.
	 * Basically they are the movement of previous candle's after hour market and the current candle's before hour market.
	 * 
	 * Example: The following chart has a jump up of 3 units.
	 * 
	 *     |
	 *     □
	 *  |  □
	 *  ■  □
	 *  ■  |
	 *  ■
	 *  ■
	 *  |
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return The value between the current candle's open price and the previous candle's close price.
	 */
	public double getJump(int index) {
		if (index < 1) return 0;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return 0;
		return currentStockCandle.open - previousStockCandle.close;
	}
	

	/**
	 * Return true if the current candle and the previous candle form piercing line (刺透线).
	 * Example:
	 * 
	 *  |
	 *  ■  |
	 *  ■  □
	 *  ■  □
	 *  ■  □
	 *  |  □
	 *     □
	 *     |
	 *     
	 * Definition:
	 * 1. Previous candle is a black long day. (TODO: Does the current candle need to be a white long day?)
	 * 2. Current candle's open < previous candle's low.
	 * 3. Current candle's close > previous candle's close + previous candle's body length * PIERCING_LINE_MIN_BODY_LENGTH_PERCENTAGE
	 * The constant here should be >= 0.5.
	 * 4. Trend before the current candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form piercing line (刺透线).
	 */
	public boolean isPiercingLine(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isBlackLongDay(previousStockCandle)) return false;
		if (currentStockCandle.open >= previousStockCandle.low) return false;
		if (currentStockCandle.close <= previousStockCandle.close + previousStockCandle.getBodyLength() * PIERCING_LINE_MIN_BODY_LENGTH_PERCENTAGE) return false;
		int start = index - PIERCING_LINE_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle and the previous candle form dark cloud cover (乌云盖顶).
	 * Example:
	 * 
	 *     |
	 *     ■
	 *  |  ■
	 *  □  ■
	 *  □  ■
	 *  □  ■
	 *  □  |
	 *  |  
	 *     
	 * Definition:
	 * 1. Previous candle is a white long day. (TODO: Does the current candle need to be black long day?)
	 * 2. Current candle's open > previous candle's high.
	 * 3. Current candle's close < previous candle's close - previous candle's body length * DARK_CLOUD_COVER_MIN_BODY_LENGTH_PERCENTAGE
	 * The constant here should be >= 0.5.
	 * 4. Trend before the current candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form dark cloud cover (乌云盖顶).
	 */
	public boolean isDarkCloudCover(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isWhiteLongDay(previousStockCandle)) return false;
		if (currentStockCandle.open <= previousStockCandle.high) return false;
		if (currentStockCandle.close >= previousStockCandle.close - previousStockCandle.getBodyLength() * DARK_CLOUD_COVER_MIN_BODY_LENGTH_PERCENTAGE) return false;
		int start = index - DARK_CLOUD_COVER_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle and the previous candle form bullish doji star (看涨十字星).
	 * Example:
	 * 
	 *  |
	 *  ■
	 *  ■
	 *  ■
	 *  ■
	 *  |
	 *     |
	 *     +
	 *     |
	 *    
	 * Definition:
	 * 1. Previous candle is a black long day.
	 * 2. Current candle is a doji star.
	 * 3. There is a gap down between current candle and previous candle.
	 * 4. Trend before the current candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form bullish doji star (看涨十字星).
	 */
	public boolean isBullishDojiStar(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isBlackLongDay(previousStockCandle)) return false;
		if (!isDojiStar(currentStockCandle)) return false;
		if (!hasGapDown(index)) return false;
		int start = index - BULLISH_DOJI_STAR_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle and the previous candle form bearish doji star (看跌十字星).
	 * Example:
	 * 
	 *     |
	 *     +
	 *     |
	 * 
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 *  |
	 *    
	 * Definition:
	 * 1. Previous candle is a white long day.
	 * 2. Current candle is a doji star.
	 * 3. There is a gap up between current candle and previous candle.
	 * 4. Trend before the current candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form bearish doji star (看跌十字星).
	 */
	public boolean isBearishDojiStar(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isWhiteLongDay(previousStockCandle)) return false;
		if (!isDojiStar(currentStockCandle)) return false;
		if (!hasGapUp(index)) return false;
		int start = index - BEARISH_DOJI_STAR_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * @see isMorningStar(index, isDojiStar, isAbandonBaby)
	 */
	public boolean isMorningStar(int index) {
		return isMorningStar(index, false, false);
	}
	
	/**
	 * Return true if the recent three candles have a form of morning star (启明星).
	 * Example:
	 * 
	 *  |
	 *  ■
	 *  ■     |
	 *  ■     □
	 *  ■     □
	 *  ■     □
	 *  |     □
	 *     |  |
	 *     ■
	 *     |    
	 *  
	 * Definition:
	 * 1. First candle is a black long day.
	 * 2. Second candle is either a white short day or black short day. There is a gap down between first candle
	 * and second candle.
	 * 3. Current candle is a white day. Either it is also a long day, or it pierces certain percentage of the first candle.
	 * (current candle's close > first candle's open - MORNING_STAR_MIN_BODY_LENGTH_PERCENTAGE * first candle's body length)
	 * 4. Trend before the second candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @param isDojiStar True if the second candle needs to be a doji.
	 * @param isAbandonBaby True if the second candle needs to be a doji, and the shadows of the doji also gap below the other two candles.
	 * @return True if the recent three candles have a form of morning star (启明星).
	 */
	public boolean isMorningStar(int index, boolean isDojiStar, boolean isAbandonBaby) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isBlackLongDay(secondPreviousStockCandle)) return false;
		if (isDojiStar || isAbandonBaby) {
			if (!isDoji(previousStockCandle)) return false;
		}
		else {
			if (!isWhiteShortDay(previousStockCandle) && !isBlackShortDay(previousStockCandle)) return false;
		}
		if (!hasGapDown(secondPreviousStockCandle, previousStockCandle, isAbandonBaby)) return false;
		if (!isWhiteLongDay(currentStockCandle)) {
			if (!isWhite(currentStockCandle)) return false;
			if (currentStockCandle.close <= secondPreviousStockCandle.open - MORNING_STAR_MIN_BODY_LENGTH_PERCENTAGE * secondPreviousStockCandle.getBodyLength())
				return false;
		}
		int start = index - MORNING_STAR_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendDown(start, index - 2)) return true;
		return false;		
	}
	
	/**
	 * @see isEveningStar(index, isDojiStar, isAbandonBaby)
	 */
	public boolean isEveningStar(int index) {
		return isEveningStar(index, false, false);
	}
	
	/**
	 * Return true if the recent three candles have a form of evening star (黄昏星).
	 * Example:
	 * 
	 *     |
	 *     ■
	 *     |  |
	 *  |     ■
	 *  □     ■     
	 *  □     ■
	 *  □     ■
	 *  □     |
	 *  □
	 *  |
	 *     
	 * Definition:
	 * 1. First candle is a white long day.
	 * 2. Second candle is either a white short day or black short day. There is a gap up between first candle
	 * and second candle.
	 * 3. Current candle is a black day. Either it is also a long day, or it pierces certain percentage of the first candle.
	 * (current candle's close < first candle's close - EVENING_STAR_MIN_BODY_LENGTH_PERCENTAGE * first candle's body length)
	 * 4. Trend before the second candle is bullish
	 * @param index The subscript in the stock candle array.
	 * @param isDojiStar True if the second candle needs to be a doji.
	 * @param isAbandonBaby True if the second candle needs to be a doji, and the shadows of the doji also gap up the other two candles.
	 * @return True if the recent three candles have a form of evening star (黄昏星).
	 */
	public boolean isEveningStar(int index, boolean isDojiStar, boolean isAbandonBaby) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isWhiteLongDay(secondPreviousStockCandle)) return false;
		if (isDojiStar || isAbandonBaby) {
			if (!isDoji(previousStockCandle)) return false;
		}
		else {
			if (!isWhiteShortDay(previousStockCandle) && !isBlackShortDay(previousStockCandle)) return false;
		}
		if (!hasGapUp(secondPreviousStockCandle, previousStockCandle, isAbandonBaby)) return false;
		if (!isBlackLongDay(currentStockCandle)) {
			if (!isBlack(currentStockCandle)) return false;
			if (currentStockCandle.close >= secondPreviousStockCandle.close - EVENING_STAR_MIN_BODY_LENGTH_PERCENTAGE * secondPreviousStockCandle.getBodyLength())
				return false;
		}
		int start = index - EVENING_STAR_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendUp(start, index - 2)) return true;
		return false;	
	}
	
	/**
	 * Return true if the recent three candles has a form of bullish tri star (看涨三星).
	 * Example:
	 * 
	 * +
	 *       +
	 *    +
	 *   
	 * Definition:
	 * 1. All three candles are doji.
	 * 2. Second candle gaps below the first and the current candle.
	 * 3. Trend before the second candle is bearish.  
	 * @param index The subscript in the stock candle array.
	 * @return True if the recent three candles has a form of bullish tri star (看涨三星).
	 */
	public boolean isBullishTriStar(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!(isDoji(currentStockCandle) && isDoji(previousStockCandle) && (isDoji(secondPreviousStockCandle)))) return false;
		if (!(hasGapUp(previousStockCandle, currentStockCandle) && (hasGapDown(secondPreviousStockCandle, previousStockCandle)))) return false;
		int start = index - BULLISH_TRI_STAR_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendDown(start, index - 2)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles has a form of bearish tri star (看跌三星).
	 * Example:
	 * 
	 *    + 
	 *       +
	 * +
	 *   
	 * Definition:
	 * 1. All three candles are doji.
	 * 2. Second candle gaps up the first and the current candle.
	 * 3. Trend before the second candle is bullish.  
	 * @param index The subscript in the stock candle array.
	 * @return True if the recent three candles has a form of bearish tri star (看跌三星).
	 */
	public boolean isBearishTriStar(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!(isDoji(currentStockCandle) && isDoji(previousStockCandle) && (isDoji(secondPreviousStockCandle)))) return false;
		if (!(hasGapDown(previousStockCandle, currentStockCandle) && (hasGapUp(secondPreviousStockCandle, previousStockCandle)))) return false;
		int start = index - BEARISH_TRI_STAR_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendUp(start, index - 2)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles form upside gap two crows (向上跳空两只乌鸦).
	 * Example:
	 * 
	 *        |
	 *     |  ■
	 *     ■  ■
	 *     |  ■
	 *        |
	 *  |
	 *  □
	 *  □
	 *  □
	 *  □
	 *  |
	 *  
	 * Definition:
	 * 1. First candle is a white long day.
	 * 2. Both the second candle and the current candle are black days. Current candle engulfs the second candle.
	 * 3. Both the second candle and the current candle gap up the first candle.
	 * 4. Trend before the second candle is bullish.
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the recent three candles has a form of upside gap two crows (向上跳空两只乌鸦).
	 */
	public boolean isUpsideGapTwoCrows(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isWhiteLongDay(secondPreviousStockCandle)) return false;
		if (!(isBlack(previousStockCandle) && isBlack(currentStockCandle))) return false;
		if (!((currentStockCandle.open > previousStockCandle.open) && (currentStockCandle.close < previousStockCandle.close)
	       && (currentStockCandle.getBodyLength() >= UPSIDE_GAP_TWO_CROWS_ENGULF_FIRST_DAY_BODY_LENGTH_MAX_PERCENTAGE * previousStockCandle.getBodyLength()))) return false;
		if (!(hasGapUp(secondPreviousStockCandle, previousStockCandle) && hasGapUp(secondPreviousStockCandle, currentStockCandle))) return false;
		int start = index - UPSIDE_GAP_TWO_CROWS_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendUp(start, index - 2)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle and the previous candle form bullish meeting line (看涨约会线).
	 * Example:
	 * 
	 *  |
	 *  ■
	 *  ■
	 *  ■
	 *  ■  | 
	 *  |  □
	 *     □     
	 *     □
	 *     □
	 *     |
	 *     
	 * Definition:
	 * 1. Current candle is a white long day. Previous candle is a black long day.
	 * 2. Current candle's close is almost the same as the previous candle's close.
	 * (Abs(Current candle's close - Previous candle's close) < BULLISH_MEETING_LINE_MAX_CLOSE_DIFF).
	 * 3. Trend before the current candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form bullish meeting line (看涨约会线).
	 */
	public boolean isBullishMeetingLine(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isWhiteLongDay(currentStockCandle)) return false;
		if (!isBlackLongDay(previousStockCandle)) return false;
		if (Math.abs(currentStockCandle.close - previousStockCandle.close) > BULLISH_MEETING_LINE_MAX_CLOSE_DIFF) return false;
		int start = index - BULLISH_MEETING_LINE_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle and the previous candle form bearish meeting line (看跌约会线).
	 * Example:
	 * 
	 *     |
	 *     ■
	 *     ■
	 *     ■
	 *  |  ■
	 *  □  |
	 *  □
	 *  □
	 *  □ 
	 *  |
	 *        
	 * Definition:
	 * 1. Current candle is a black long day. Previous candle is a white long day.
	 * 2. Current candle's close is almost the same as the previous candle's close.
	 * (Abs(Current candle's close - Previous candle's close) < BEARISH_MEETING_LINE_MAX_CLOSE_DIFF).
	 * 3. Trend before the current candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle and the previous candle form bearish meeting line (看跌约会线).
	 */
	public boolean isBearishMeetingLine(int index) {
		if (index < 1) return false;
		StockCandle currentStockCandle, previousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		if ((currentStockCandle == null) || (previousStockCandle == null)) return false;
		if (!isBlackLongDay(currentStockCandle)) return false;
		if (!isWhiteLongDay(previousStockCandle)) return false;
		if (Math.abs(currentStockCandle.close - previousStockCandle.close) > BEARISH_MEETING_LINE_MAX_CLOSE_DIFF) return false;
		int start = index - BEARISH_MEETING_LINE_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a bullish belt hold (看涨捉腰带线).
	 * Example
	 * 
	 *  |
	 *  ■
	 *  ■  |
	 *  |  ■
	 *     ■
	 *     ■
	 *     |
	 *        |
	 *        □
	 *        □
	 *        □
	 *        □
	 * 
	 * Definition:
	 * 1. Current candle is a white long day. (TODO: Is it possible that it is not a white long day?)
	 * 2. Current candle's open price jumps down from the previous candle's close price.
	 * 3. Current candle is a white marubozu in terms of the open price.
	 * 4. Trend before the current candle is bearish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a bullish belt hold (看涨捉腰带线).
	 */
	public boolean isBullishBeltHold(int index) {
		StockCandle currentStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		if (currentStockCandle == null) return false;
		if (!isWhiteLongDay(currentStockCandle)) return false;
		if (!isWhiteMarubozu(currentStockCandle, StockCandleDataType.OPEN)) return false;
		double jumpDown = getJump(index);
		if (jumpDown > -BULLISH_BELT_HOLD_MIN_JUMP_LENGTH) return false;
		int start = index - BULLISH_BELT_HOLD_MIN_TREND_CANDLE_NUMBER;
		if (isTrendDown(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the current candle is a bearish belt hold (看跌捉腰带线).
	 * Example:
	 * 
	 *        ■
	 *        ■
	 *        ■
	 *        ■
	 *        |
	 *     |
	 *     □ 
	 *  |  □
	 *  □  |
	 *  □
	 *  □
	 *  | 
	 *  
	 * Definition:
	 * 1. Current candle is a black long day. (TODO: Is it possible that it is not a black long day?)
	 * 2. Current candle's open price jumps down from the previous candle's close price.
	 * 3. Current candle is a black marubozu in terms of the open price.
	 * 4. Trend before the current candle is bullish.
	 * @param index The subscript in the stock candle array.
	 * @return True if the current candle is a bearish belt hold (看跌捉腰带线).
	 */
	public boolean isBearishBeltHold(int index) {
		StockCandle currentStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		if (currentStockCandle == null) return false;
		if (!isBlackLongDay(currentStockCandle)) return false;
		if (!isBlackMarubozu(currentStockCandle, StockCandleDataType.OPEN)) return false;
		double jumpUp = getJump(index);
		if (jumpUp < BEARISH_BELT_HOLD_MIN_JUMP_LENGTH) return false;
		int start = index - BEARISH_BELT_HOLD_MIN_TREND_CANDLE_NUMBER;
		if (isTrendUp(start, index - 1)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles form unique three river bottom (奇特三川底部).
	 * Example:
	 * 
	 *  | 
	 *  ■
	 *  ■  ■  |
	 *  ■  |  □
	 *  ■  |  □
	 *  |  |  |
	 *     |  
	 *     
	 * Definition:
	 * 1. First candle is a black long day.
	 * 2. Second candle is a black short day with a long lower shadow that is lower than the first candle's low price.
	 * 3. First candle harami the second candle's body.
	 * 4. Current candle is a short white day. (TODO: Does the current candle need to be below the second candle?)
	 * 5. Trend before the second candle is bearish.
	 * 
	 * @param index The subscript in the stock candle array.
	 * @return True if the recent three candles form unique three river bottom (奇特三川底部).
	 */
	public boolean isUniqueThreeRiverBottom(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isBlackLongDay(secondPreviousStockCandle)) return false;
		if (!isBlackShortDay(previousStockCandle)) return false;
		if (previousStockCandle.low >= secondPreviousStockCandle.low) return false;
		if (!((secondPreviousStockCandle.open > previousStockCandle.open) && (secondPreviousStockCandle.close < previousStockCandle.close))) return false;
		if (!isWhiteShortDay(currentStockCandle)) return false;
		int start = index - UNIQUE_THREE_RIVER_BOTTOM_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendDown(start, index - 2)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles form three white soldiers (白色三兵).
	 * Example:
	 * 
	 *        |
	 *        □
	 *     |  □
	 *     □  □ 
	 *  |  □  □
	 *  □  □  |
	 *  □  □  
	 *  □  |
	 *  □
	 *  |
	 *  
	 * Definition:
	 * 1. All three candles are white long days.
	 * 2. The second candle opens within the body of the first candle. The current candle opens within the body
	 * of the second candle. TODO: Is that really necessary?
	 * 3. Trend before the first candle is bearish.
	 * 
	 * @param index
	 * @return True if the recent three candles form three white soldiers (白色三兵).
	 */
	public boolean isThreeWhiteSoldiers(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isWhiteLongDay(currentStockCandle) || !isWhiteLongDay(previousStockCandle) || !isWhiteLongDay(secondPreviousStockCandle)) return false;
		if (previousStockCandle.open >= secondPreviousStockCandle.close) return false;
		if (currentStockCandle.open >= previousStockCandle.close) return false;
		int start = index - THREE_WHITE_SOLDIERS_MIN_TREND_CANDLE_NUMBER - 2;
		if (isTrendDown(start, index - 3)) return true;
		return false;
	}
	
	/**
	 * Return true if the recent three candles form advance block (前进受阻).
	 * Example:
	 * 	
	 *        |
	 *     |  |
	 *     |  |
	 *     |  □ 
	 *  |  □  |
	 *  □  □  
	 *  □  |
	 *  □
	 *  □
	 *  |
	 *  
	 * Definition:
	 * 1. First candle is a white long day.
	 * 2. Second candle and current candle candle are white but they have long upper shadow.
	 * TODO: Does second candle and current candle need to open within the body of the previous candle? Do they need to be white?
	 * 3. Trend before the second candle is bullish.
	 * @param index
	 * @return
	 */
	public boolean isAdvanceBlock(int index) {
		if (index < 2) return false;
		StockCandle currentStockCandle, previousStockCandle, secondPreviousStockCandle;
		currentStockCandle = stockCandleArray.get(index);
		previousStockCandle = stockCandleArray.get(index - 1);
		secondPreviousStockCandle = stockCandleArray.get(index - 2);
		if ((currentStockCandle == null) || (previousStockCandle == null) || (secondPreviousStockCandle == null)) return false;
		if (!isWhiteLongDay(secondPreviousStockCandle)) return false;
		if (!isWhite(previousStockCandle) || !isWhite(currentStockCandle)) return false;
		if ((previousStockCandle.getUpperShadowLength() < ADVANCE_BLOCK_MIN_UPPER_SHADOW_LENGTH)
	     || (currentStockCandle.getUpperShadowLength() < ADVANCE_BLOCK_MIN_UPPER_SHADOW_LENGTH)) return false;
		int start = index - ADVANCE_BLOCK_MIN_TREND_CANDLE_NUMBER - 1;
		if (isTrendUp(start, index - 2)) return true;
		return false;
	}
	
	
}

