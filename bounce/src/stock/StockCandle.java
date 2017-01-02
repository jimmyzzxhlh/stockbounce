package stock;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import stock.StockEnum.StockCandleClass;
import stock.StockEnum.StockCandleDataType;

/**
 * This is a class to represent a candle. Should not initialize an instance of this class. 
 * However, I don't find a better way to restrict the usage. The class cannot be declared as 
 * an abstract class, otherwise we cannot clone it. 
 * @author jimmyzzxhlh-Dell
 *
 */
public abstract class StockCandle {
	protected double open;
	protected double close;
	protected double high;
	protected double low;
	protected long volume;
	protected DateTime instant;  //The instant will be assumed to be UTC time. This is intentional so that it is easier and consistent to implement.
//	protected double turnoverRate = 0;
		
	protected StockCandle(DateTime instant, double open, double close, double high, double low, long volume) {
		this.instant = instant;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
	}
	
	/**
	 * Clone a stock candle object.
	 * @param candle
	 */
//	public StockCandle(StockCandle candle) {
//		this(candle.instant, candle.open, candle.close, candle.high, candle.low, candle.volume);
//	}
	
	public abstract StockCandle copy();
	
	public DateTime getInstant()    { return instant; }
	public double getOpen()         { return open;    }
	public double getClose()        { return close;   }
	public double getHigh()         { return high;    }
	public double getLow()          { return low;     }
	public long getVolume()         { return volume;  }
//	public double getTurnoverRate() { return turnoverRate; }		
	
//	/**
//	 * Turnover rate = volume / shares outstanding
//	 * @param outstandingShares
//	 */
//	public void setTurnoverRate(long outstandingShares) {
//		this.turnoverRate = volume * 1.0 / outstandingShares;
//	}
	
	/**
	 * Get the specific price (open, close, high, low)
	 * @param dataType
	 * @return
	 */
	public double getStockPrice(StockCandleDataType dataType) {
		switch (dataType) {
		case OPEN: return open;
		case CLOSE: return close;
		case HIGH: return high;
		case LOW: return low;
		default:
			break;
		}
		return 0;
	}
	
	public boolean isWhiteCandle() {
		return (close > open);
	}
	
	public boolean isBlackCandle() {
		return (close < open);
	}
	
	public double getBodyLength() {
		return Math.abs(close - open);
	}
	
	public double getTotalLength() {
		return high - low;
	}

	public double getUpperShadowLength() {
		if (close > open) return high - close;
		return high - open;
	}
	
	public double getLowerShadowLength() {
		if (close > open) return open - low;
		return close - low;
	}
	
	public boolean isUpperShadowLonger() {
		return (getUpperShadowLength() >= getLowerShadowLength());
	}
	
	/**
	 * Get a candle class.
	 * @return
	 */
	public StockCandleClass getCandleClass() {
		double bodyLength = getBodyLength();
		if (isWhiteCandle()) {
			if (bodyLength / open >= StockConst.LONG_DAY_PERCENTAGE) return StockCandleClass.WHITE_LONG;
		}
		if (isBlackCandle()) {
			if (bodyLength / open >= StockConst.LONG_DAY_PERCENTAGE) return StockCandleClass.BLACK_LONG;
		}
		if (isUpperShadowLonger())
			return StockCandleClass.UPPER_LONGER;
		return StockCandleClass.LOWER_LONGER;
	}
	
	@Override
	public String toString() {
		return String.format("instant=%s, open=%d, close=%d, low=%d, high=%d, volume=%d",
							 instant, open, close, low, high, volume);
	}
}
