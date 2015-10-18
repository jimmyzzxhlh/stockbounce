package test;
import java.io.IOException;

import download.StockDataMerge;

public class StockDataMergeTest {

	public static void main(String[] args) {
		try {
			StockDataMerge.DataMerge();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
