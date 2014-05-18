package stock;

import java.io.BufferedReader;
import java.io.FileReader;

public abstract class StockParser {
	public String filename = null;
	public int lineNumber = 0;
	
	private BufferedReader br;
	
	public StockParser() {
		
	}
	
	public StockParser(String filename) {
		this.filename = filename;
	}
	
	public void startReadFile() throws Exception {
		this.br = new BufferedReader(new FileReader(this.filename));
	}
	
	public String nextLine() throws Exception {
		String line = null;
		if (br == null) return null;
		do {
			lineNumber++;
			line = br.readLine();
			if (line == null) return null;
		}
		while (!isLineValid(line));		
		return line;		
		
	}
	
	public abstract boolean isLineValid(String line);
	
	public void closeFile() throws Exception {
		if (br == null) return;
		br.close();
	}
	
	public abstract void parseLine(String line, StockPrice stockPrice);
		
}
