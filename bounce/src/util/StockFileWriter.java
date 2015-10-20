package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Wrapper to make file write easier.
 * @author jimmyzzxhlh-Dell
 *
 */
public class StockFileWriter {
	
	private String filename;
	private File file;
	private FileWriter fw;
	private BufferedWriter bw;
	
	public StockFileWriter(String filename) throws Exception {
		this.filename = filename;
		file = new File(filename);
		if (!file.exists()) file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
	}
	
	public StockFileWriter(String filename, boolean appended) throws Exception {
		this.filename = filename;
		file = new File(filename);
		if (!file.exists()) file.createNewFile();
		fw = new FileWriter(file.getAbsoluteFile(), appended);
		bw = new BufferedWriter(fw);
	}

	public File getFile() {
		return file;
	}
	
	public void newLine() throws Exception {
		bw.newLine();
	}
	
	public void write(String str) throws Exception {
		bw.write(str);		
	}
	
	public void write(double d) throws Exception {
		write(Double.toString(d));
	}
	
	public void writeLine(String str) throws Exception {
		write(str);
		newLine();
	}
	
	public void writeLine(double d) throws Exception {
		write(d);
		newLine();
	}
	
	public void flush() throws Exception {
		bw.flush();
	}
	
	public void close() {
		try {
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
