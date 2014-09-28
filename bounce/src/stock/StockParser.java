package stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public abstract class StockParser {
	public String filename = null;
	public int lineNumber = 0;
	public File file = null;
	
	private BufferedReader br;
	
	public StockParser() {
		
	}
	
	public StockParser(String filename) {
		this.filename = filename;
	}
	
	public StockParser(File file) {
		this.file = file;
	}
	
	public void startReadFile() {
		try {
			if (filename != null) {
				this.br = new BufferedReader(new FileReader(filename));
			}
			else if (file != null) {
				this.br = new BufferedReader(new FileReader(file));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public abstract void parseLine(String line, StockCandle stockCandle);
		
}
