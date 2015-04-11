package stock;

public class StockEnum {
	
	public enum StockCandleDataType {
		OPEN, CLOSE, HIGH, LOW, VOLUME, DATE, OPENCLOSEMAX
	}
	
	public enum TrendCalculationMethod {
		SIMPLE_LINEAR_REGRESSION,
		WEIGHTED_ORDINARY_LINEAR_REGRESSION
	}
	
	public enum Exchange {
		NASDAQ,
		NYSE,
		AMEX,
		SSE,
		SZSE
	}
	
	public enum Country {
		US,
		CHINA
	}
	
	
	public enum StockCandleClass {
		WHITE_LONG,
		BLACK_LONG,
		UPPER_LONGER,
		LOWER_LONGER		
	}
	
	public enum EarningsTimeType {
		AMC,   //After market close
		BTO,   //Before the open
		DMT,   //During market time
		NONE		
	}
	
	public enum StockOrderType {
		BUY,
		SELL,
		BUY_LIMIT,
		SELL_LIMIT,
		BUY_STOP,
		SELL_STOP
	}
	
	public enum StockOrderStatus {
		NA,
		OPENED_MARKET,
		OPENED_LIMIT,
		CLOSED_MARKET,
		CLOSED_TAKE_PROFIT,
		CLOSED_STOP_LOSS,
		CANCELED
	}
	
	public enum StockPatternType {
		ADVANCE_BLOCK,
		BEARISH_BELT_HOLD,
		BEARISH_DOJI_STAR,
		BEARISH_ENGULFING,
		BEARISH_HARAMI,
		BEARISH_MEETING_LINE,
		BEARISH_TRI_STAR,
		BLACK,
		BLACK_HAMMER,
		BLACK_HANGING_MAN,
		BLACK_LONG_DAY,
		BLACK_MARUBOZU,
		BULLISH_BELT_HOLD,
		BULLISH_ENGULFING,
		BULLISH_HARAMI,
		BULLISH_MEETING_LINE,
		BULLISH_TRI_STAR,
		DARK_CLOUD_COVER,
		DOJI,
		DOJI_STAR,
		DRAGONFLY_DOJI,
		EVENING_STAR,
		GRAVESTONE_DOJI,
		HAMMER,
		HANGING_MAN,
		INVERTED_HAMMER,
		LONG_LEGGED_DOJI,
		MORNING_STAR,
		PAPER_UMBRELLA,
		PIERCING_LINE,
		SHOOTING_STAR,
		STAR,
		THREE_INSIDE_DOWN,
		THREE_INSIDE_UP,
		THREE_OUTSIDE_DOWN,
		THREE_OUTSIDE_UP,
		THREE_WHITE_SOLDIERS,
		UNIQUE_THREE_RIVER_BOTTOM,
		UPSIDE_GAP_TWO_CROWS,
		WHITE,
		WHITE_HAMMER,
		WHITE_HANGING_MAN,
		WHITE_LONG_DAY,
		WHITE_MARUBOZU
	}
	
	public static boolean isBuyOrder(StockOrderType type) {
		if (type == StockOrderType.BUY || type == StockOrderType.BUY_LIMIT || type == StockOrderType.BUY_STOP) { 
			return true;
		}
		return false;
	}
	
	public static boolean isSellOrder(StockOrderType type) {
		if (type == StockOrderType.SELL || type == StockOrderType.SELL_LIMIT || type == StockOrderType.SELL_STOP) { 
			return true;
		}
		return false;
	}
}
