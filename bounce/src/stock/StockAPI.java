package stock;

public class StockAPI {
	public static double changeRate(double original, double result){
		if ((original <= 0)||(result <= 0))
			return 0;
		return (result-original)/original;
	}
}
